package com.li.androidprojecttext.yansheng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.am.fras.ConvertUtils;
import com.am.fras.FaceDetectResult;
import com.am.fras.FaceDetector;
import com.am.fras.FaceDetectorParam;
import com.am.fras.FaceExtractor;
import com.am.fras.FaceTracker;
import com.am.fras.LicenseError;
import com.am.fras.RedSpoofAnti;
import com.facerecognition.dualdemoVL.LicenseUtils;
import com.facerecognition.dualdemoVL.ModelInit;
import com.li.androidprojecttext.R;
import com.li.androidprojecttext.activity.DoubleCameraActivity;
import com.li.androidprojecttext.model.IdentityCardEntity;

import java.io.IOException;

/**
 * @Description 主页面
 */
public class FaceYanshenTwoRec extends BaseFaceRecognized {
    public static final String TAG = "yanshen2.0";

    private DoubleCameraActivity activity;

    private static final float SIMILAR_THRESHOLD = 80F;//相似度的值，
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 480;

    //图像数据
    public static volatile byte[] visYuvData = null, nirYuvData = null;//相机的实时数据，为yuv格式

    //检测器
    public static FaceDetector visFaceDetector;//检测可见光人脸
    public static FaceDetector nirFaceDetector;
    public static FaceDetector faceViewDetector;
    public static FaceExtractor faceExtractor;
    public static RedSpoofAnti redSpoofAnti;
    public static FaceTracker faceTracker = null; //面部追踪

    private static boolean isInitEngine = false;
    private LivenessThread livingCheckThread;

    private Camera rgbCamera, nirCamera;
    private SurfaceView rgbSurfaceView, nirSurfaceView;//摄像头预览
    private SurfaceHolder rgbSurfaceHolder, nirSurfaceHolder;

    private IdentityCardEntity scanIdentityCardEntity;
    private long mDetectTime;
    private boolean shouldProcess;
    private int mPreviewWidth = 1080;
    private int mPreviewHeight = 1920;

    private void initView() {
        rgbSurfaceView = activity.findViewById(R.id.cameraSurfaceView);
        nirSurfaceView = activity.findViewById(R.id.cameraInfraredView);
        rgbSurfaceHolder = rgbSurfaceView.getHolder();
        nirSurfaceHolder = nirSurfaceView.getHolder();
        ConstraintLayout layoutView = activity.findViewById(R.id.layoutView);
        ViewTreeObserver viewTreeObserver = layoutView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = layoutView.getWidth();
                int height = layoutView.getHeight();
                Log.d(TAG, "width, height = " + width + ", " + height);

                mPreviewWidth = width;
                mPreviewHeight = height;
            }
        });
    }

    @Override
    public void onCreate(DoubleCameraActivity activity) {
        this.activity = activity;
        ModelInit.initFras();  //初始化授权库
        ModelInit.initModelDirMap(activity.getApplication());
        initView();//初始化布局

        int licenseFlag = LicenseUtils.verifyLicense();
        Log.i(TAG, "licenseFlag:" + licenseFlag);
        if (licenseFlag != LicenseError.AM_E_SUCCESS) {
            Toast.makeText(this.activity, "人脸识别未激活，请联系IT人员处理！", Toast.LENGTH_LONG).show();
            LicenseUtils.requestLicenseOnThread();
        }
    }

    @Override
    public void onResume() {
        if (!isInitEngine) {
            isInitEngine = true;
            initEngine();
        }
        openTwoCamera();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        shouldProcess = false;
        if (livingCheckThread != null) {
            livingCheckThread.stop();
        }
        closeTwoCamera();
    }

    public void initEngine() {
        // 初始化活体引擎
        faceExtractor = FaceExtractor.createExtractor(ModelInit.getModelDirMap().get(R.raw.extract));
        redSpoofAnti = RedSpoofAnti.createSpoofAnti(ModelInit.getModelDirMap().get(R.raw.anti));

        //对各个检测器设置参数
        visFaceDetector = FaceDetector.createDetector(ModelInit.getModelDirMap().get(R.raw.detect));
        FaceDetectorParam visFaceDetectorParam = new FaceDetectorParam();
        visFaceDetectorParam.scaleFactor = 0f;
        visFaceDetectorParam.minObjSize = 0;
        visFaceDetectorParam.deviceId = 0;
        visFaceDetectorParam.stepFactor = 0.0f;
        visFaceDetector.setParam(visFaceDetectorParam);

        nirFaceDetector = FaceDetector.createDetector(ModelInit.getModelDirMap().get(R.raw.detect));
        FaceDetectorParam nirFaceDetectorParam = new FaceDetectorParam();
        nirFaceDetectorParam.scaleFactor = 0.0f;
        nirFaceDetectorParam.minObjSize = 0;
        nirFaceDetectorParam.deviceId = 0;
        nirFaceDetectorParam.stepFactor = 0.0f;
        nirFaceDetector.setParam(nirFaceDetectorParam);

        faceViewDetector = FaceDetector.createDetector(ModelInit.getModelDirMap().get(R.raw.detect));
        FaceDetectorParam faceViewDetectorParam = new FaceDetectorParam();
        faceViewDetectorParam.scaleFactor = 1.50f;
        faceViewDetectorParam.minObjSize = 0;
        faceViewDetectorParam.deviceId = 0;
        faceViewDetectorParam.stepFactor = 0.0f;
        faceViewDetector.setParam(faceViewDetectorParam);
        faceTracker = FaceTracker.createTracker(faceViewDetector, 10, 0.4f, 1, CAMERA_WIDTH, CAMERA_HEIGHT);

    }

    /**
     * 开启界面上方摄像头预览
     */
    private void openVisCameraPreview() {
        if (rgbCamera == null) {//相机未打开，添加回调，启动相机
            if (rgbCamera == null) {
                rgbCamera = Camera.open(0);
                rgbCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        visYuvData = data;//原始数据回显
                    }
                });
                try {
                    rgbCamera.setPreviewDisplay(rgbSurfaceHolder);
                    Camera.Parameters topParams = rgbCamera.getParameters();
                    topParams.setPreviewSize(CAMERA_WIDTH, CAMERA_HEIGHT);
                    rgbCamera.setParameters(topParams);
                    rgbCamera.startPreview();//开始预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {//相机打开，直接打开预览
            rgbCamera.startPreview();
        }
    }

    /**
     * 关闭界面上方摄像头预览
     */
    private void closeVisCameraPreview() {
        if (null != rgbCamera) {
            rgbCamera.stopPreview();
            rgbCamera.setPreviewCallback(null);
            rgbCamera.release();
            rgbCamera = null;

        }
    }

    /**
     * 关闭界面下方摄像头预览
     */
    private void closeNirCameraPreview() {
        if (null != nirCamera) {
            nirCamera.stopPreview();
            nirCamera.setPreviewCallback(null);
            nirCamera.release();
            nirCamera = null;
        }
    }

    /**
     * 开启界面下方摄像头预览
     */
    private void openNirCameraPreview() {
        if (nirCamera == null) {//相机未打开，添加回调，启动相机
            if (nirCamera == null) {
                nirCamera = Camera.open(1);
                nirCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        nirYuvData = data;//原始数据回显
                    }
                });
                try {
                    nirCamera.setPreviewDisplay(nirSurfaceHolder);
                    Camera.Parameters nirParams = nirCamera.getParameters();
                    nirParams.setPreviewSize(CAMERA_WIDTH, CAMERA_HEIGHT);
                    nirCamera.setParameters(nirParams);
                    nirCamera.startPreview();//开始预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {//相机打开，直接打开预览
            nirCamera.startPreview();
        }
    }

    private void openTwoCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                openVisCameraPreview();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                openNirCameraPreview();
            }
        }).start();

        livingCheckThread = new LivenessThread();
        livingCheckThread.start(activity);
    }

    private void closeTwoCamera() {
        //关闭活体检测线程
        if (livingCheckThread != null) {
            livingCheckThread.stop();
            livingCheckThread = null;
        }
        closeVisCameraPreview();
        closeNirCameraPreview();
    }

    // 业务
    private String idcardPhotoBase64 = "";
    private String idCardNumber = "";

    private Bitmap currentBitmap;
    private Bitmap idcardBitmap;
    private byte[] idcardFeature;

    @Override
    public void continueCapture() {
        mDetectTime = System.currentTimeMillis();
        shouldProcess = true;
    }

    private void updatePassportBitmap() {
        Log.i(TAG, "updatePassportBitmap:" + (idcardPhotoBase64 != null));
        if (idcardPhotoBase64 == null) return;
        Bitmap bitmap = null;
        try {
            bitmap = getBitmap(idcardPhotoBase64);
            //UtilsSave.saveBitmap(idCardNumber + ".jpg",bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateIdCardBitmap(bitmap);
    }

    private void updateIdCardBitmap(Bitmap bitmap) {
        Log.i(TAG, "updateIdCardBitmap:" + (bitmap != null));
        if (bitmap == null || visFaceDetector == null) {
            return;
        }
        idcardBitmap = bitmap;
        int bitmapWidth = idcardBitmap.getWidth();
        int bitmapHeight = idcardBitmap.getHeight();
        byte[] idcardBGR = ConvertUtils.bitmapToBgrRotate(idcardBitmap, ConvertUtils.ROTATE_TYPE_0);
        FaceDetectResult idcardDetect = visFaceDetector.detectFace(idcardBGR, bitmapWidth, bitmapHeight);
        Log.i(TAG, "idcardDetect is OK?" + (idcardDetect != null));
        if (idcardDetect != null) {
            idcardFeature = faceExtractor.extractFeature(idcardBGR, bitmapWidth, bitmapHeight, idcardDetect);
        }
        Log.i(TAG, "idcardFeature is OK?" + (idcardFeature != null));
    }

    @Override
    public void updateIDCardPhotoBase64(String idPhotoBase64, String cardNumber) {
        Log.i(TAG, "updateIDCardPhotoBase64:" + cardNumber);
        idcardPhotoBase64 = idPhotoBase64;
        idCardNumber = cardNumber;
        updatePassportBitmap();
    }

    @Override
    public void updateIdentityCardEntity(IdentityCardEntity identityCardEntity) {
        scanIdentityCardEntity = identityCardEntity;
    }

    public Bitmap getBitmap(String encodeBitmapStr) {
        byte[] decodedString = Base64.decode(encodeBitmapStr, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    // 活体检测线程
    class LivenessThread implements Runnable {
        private static final String TAG = "LivenessThread";
        //各个参数的阙值
        private final int nirLivingScoreThreshold = 40;//默认分值可见光活体分数阈值(1~100)，越大越严格
        private final int visLivingScoreThreshold = 90;//近红外活体分数阈值(1~100)，越大越严格
        private final float rollAngle = 90;
        private final float yawAngle = 90;
        private final float pitchAngle = 90;

        //检测线程中需要的相关参数
        private Bitmap rgbBitmap, nirBitmap;
        private byte[] rgbBinary, nirBinary;
        private FaceDetectResult visFaceRects, nirFaceRects;//人脸检测结果

        private int visEyeDistance = 0;//可见光双眼间距
        private int nirEyeDistance = 0;//红外光双眼间距
        private double visVerticalDistance = 0;//可见光眉心(6号点)到嘴尖(8号点)距离
        private double nirVerticalDistance = 0;//红外光眉心(6号点)到嘴尖(8号点)距离
        // int EYE_DISTANCE_VALUE = 5;//双眼间距的差值
        // int VERTICAL_DISTANCE_VALUE = 5;//眉心(6号点)到嘴尖(8号点)距离

        private byte[] feature;//提取到的特征
        private boolean livingCheckResult;//活体检测结果
        private final int FACETHREAD_SLEEP_TIME = 100;
        //其他
        private volatile Thread mainLoop = null;
        private boolean threadstop;

        /**
         * 开启检测线程
         *
         * @param ctx
         */
        public void start(Context ctx) {
            stop();
            while (mainLoop != null) {
                SystemClock.sleep(FACETHREAD_SLEEP_TIME);
            }
            threadstop = false;
            if (mainLoop == null) {
                mainLoop = new Thread(this);
                mainLoop.setPriority(Thread.MAX_PRIORITY);
            }
            mainLoop.start();
        }

        /**
         * 检测线程
         */
        @Override
        public void run() {
            //循环检测
            while (!threadstop) {
                if (shouldProcess && System.currentTimeMillis() - mDetectTime > 30 * 1000) {
                    shouldProcess = false;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //超时
                            getInterface().faceCompareFailed("人证对比不匹配");
                        }
                    });
                    continue;
                }

                // 采集可见光图片
                rgbBinary = ConvertUtils.yuvToBgrRotate(visYuvData, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
                if (rgbBinary == null) {
                    continue;
                }
                // 采集近红外图片
                nirBinary = ConvertUtils.yuvToBgrRotate(nirYuvData, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
                if (nirBinary == null) {
                    continue;
                }

                // 可见光图像翻转
                rgbBitmap = ConvertUtils.bgrToBitmapRotate(rgbBinary, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//旋转
                if (rgbBitmap != null) {
                    rgbBinary = ConvertUtils.bitmapToBgrRotate(rgbBitmap, ConvertUtils.ROTATE_TYPE_0);
                    rgbBitmap.recycle();
                }

                // 可见光人脸检测
                FaceDetectResult visFaceRects = visFaceDetector.detectFace(rgbBinary, CAMERA_WIDTH, CAMERA_HEIGHT);
                if (visFaceRects == null || visFaceRects.landmarks == null) {
                    // 无人脸信息
                    continue;
                }

                // 人脸角度过滤
                if (Math.abs(visFaceRects.roll) > rollAngle || Math.abs(visFaceRects.yaw) > yawAngle || Math.abs(visFaceRects.pitch) > pitchAngle) {
                    //"人脸角度过大！";
                    continue;
                }

                // 活体检测
                // 近红外图像翻转
                nirBitmap = ConvertUtils.bgrToBitmapRotate(nirBinary, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);
                if (nirBitmap != null) {
                    nirBinary = ConvertUtils.bitmapToBgrRotate(nirBitmap, ConvertUtils.ROTATE_TYPE_0);
                    nirBitmap.recycle();
                }

                // 近红外人脸检测
                nirFaceRects = nirFaceDetector.detectFace(nirBinary, CAMERA_WIDTH, CAMERA_HEIGHT);
                if (nirFaceRects == null || nirFaceRects.landmarks == null) {
                    // "检测到攻击！";
                    continue;
                }

                nirEyeDistance = getEyeDistance(nirFaceRects.landmarks, "近红外双眼___");
                nirVerticalDistance = getVerticalDistance(nirFaceRects.landmarks, "近红外双眼___");
                Log.e(TAG, "-----------------双眼----------------------------------间距差:" + Math.abs(nirEyeDistance - visEyeDistance) + ",纵向差:" + Math.abs(nirVerticalDistance - visVerticalDistance));
                //眼间距差值不能大于5，眉心到嘴尖距离不能大于5
                //if (Math.abs(nirEyeDistance - visEyeDistance) > EYE_DISTANCE_VALUE || Math.abs(nirVerticalDistance - visVerticalDistance) > VERTICAL_DISTANCE_VALUE) {
                //  Log.e("TIME_COUNT", "### 双眼间距未通过");
                //  continue;
                //}

                // 近红外活体判断
                livingCheckResult = checkLiving(visFaceRects, nirFaceRects, rgbBinary, CAMERA_WIDTH, CAMERA_HEIGHT, visLivingScoreThreshold, nirBinary, CAMERA_WIDTH, CAMERA_HEIGHT, nirLivingScoreThreshold);
                if (!livingCheckResult) {
                    Log.e(TAG, "### 非活体 ");
                    continue;
                } else {
                    Log.e(TAG, "### 是活体 ");
                }

//                if (!isValidFaceInfo(visFaceRects)) {
//                    //人脸不在框内
//                    continue;
//                }

                //Log.i(TAG, "visFaceRects:" + visFaceRects.toString());
                // 抽取特征
                feature = faceExtractor.extractFeature(rgbBinary, CAMERA_WIDTH, CAMERA_HEIGHT, visFaceRects);
                if (feature == null) {
                    continue;
                }

                // 五、住户库比对
                if (idcardFeature == null && scanIdentityCardEntity != null) {
                    updateIdCardBitmap(scanIdentityCardEntity.getBitmap());
                }
                Log.i(TAG, "feature:" + (feature == null) + " " + (idcardFeature == null));
                if (shouldProcess && (feature != null) && (idcardFeature != null)) {
                    float compareResult = faceExtractor.matchFeatures(feature, idcardFeature) * 100f;
                    Log.i(TAG, "compareResult:" + compareResult);
                    if (compareResult > SIMILAR_THRESHOLD) {
                        shouldProcess = false;
                        currentBitmap = ConvertUtils.bgrToBitmapRotate(rgbBinary, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//旋转
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getInterface().faceCompareSuccess(compareResult, currentBitmap, idCardNumber, System.currentTimeMillis());
                            }
                        });
                    }
                }
            }
        }

        public Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
            int frameSize = width * height;
            int[] rgba = new int[frameSize];

            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    int y = (0xff & ((int) data[i * width + j]));
                    int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                    int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                    y = y < 16 ? 16 : y;

                    int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                    int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                    int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                    r = r < 0 ? 0 : (r > 255 ? 255 : r);
                    g = g < 0 ? 0 : (g > 255 ? 255 : g);
                    b = b < 0 ? 0 : (b > 255 ? 255 : b);

                    rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
                }

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(rgba, 0, width, 0, 0, width, height);
            return bmp;
        }

        /**
         * 判断红外光是否检测通过
         *
         * @param visFaceRects            可见光人脸检测结果
         * @param nirFaceRects            近红外人脸检测结果
         * @param visBgr                  可见光bgr数据
         * @param visWidth                可见光照片的宽
         * @param visHeight               可见光照片的高
         * @param visLivingScoreThreshold 可见光活体分数阈值
         * @param nirBgr                  近红外bgr数据
         * @param nirWidth                近红外照片的宽
         * @param nirHeight               近红外照片的高
         * @param nirLivingScoreThreshold 近红外活体分数阈值
         * @return 红外活体达到阈值返回true，反之返回false
         */
        private boolean checkLiving(FaceDetectResult visFaceRects, FaceDetectResult nirFaceRects, byte[] visBgr, int visWidth, int visHeight, int visLivingScoreThreshold, byte[] nirBgr, int nirWidth, int nirHeight, int nirLivingScoreThreshold) {
            if (visFaceRects == null) {
                Log.d(TAG, "可见光无人脸");
                return false;
            }
            if (nirFaceRects == null) {
                Log.d(TAG, "近红外无人脸");
                return false;
            }

            float visAntiScore = redSpoofAnti.getBlackAndWhiteScore(visBgr, visWidth, visHeight, 10, visFaceRects) * 100;
            float nirAntiScore = redSpoofAnti.getAntiScore(nirBgr, nirWidth, nirHeight, nirFaceRects) * 100;

            Log.e("TIME_COUNT", "## 可见光活体分数 " + visLivingScoreThreshold + ":" + visAntiScore);
            Log.e("TIME_COUNT", "## 近红外活体分数 " + nirLivingScoreThreshold + ":" + nirAntiScore);

            if (nirAntiScore < nirLivingScoreThreshold) {  //活体未通过
                return false;
            } else if (visAntiScore > visLivingScoreThreshold) { // 活体未通过，检测到黑白照片
                return false;
            } else {   // 活体通过
            }
            return true;
        }

        /**
         * 根据关键点，获取两眼间距
         *
         * @param landmarks 关键点
         * @param logTag    用于打印的标识
         * @return
         */
        private int getEyeDistance(float[] landmarks, String logTag) {
            int absX = (int) Math.abs(landmarks[0] - landmarks[2]);
            int absY = (int) Math.abs(landmarks[1] - landmarks[3]);
            int distance = (int) Math.sqrt(absX * absX + absY * absY);
            Log.e(logTag, "双眼坐标：(" + (int) landmarks[0] + "," + (int) landmarks[1] + "),(" + (int) landmarks[2] + "," + (int) landmarks[3] + "),间距：" + distance);
            return distance;
        }

        private int getVerticalDistance(float[] landmarks, String logTag) {
            int absX = (int) Math.abs(landmarks[12] - landmarks[16]);
            int absY = (int) Math.abs(landmarks[13] - landmarks[17]);
            int distance = (int) Math.sqrt(absX * absX + absY * absY);
            Log.e(logTag, "纵向坐标：(" + (int) landmarks[0] + "," + (int) landmarks[1] + "),(" + (int) landmarks[2] + "," + (int) landmarks[3] + "),间距：" + distance);
            return distance;
        }


        public void stop() {
            threadstop = true;
            shouldProcess = false;

            if (mainLoop != null) {
                mainLoop.interrupt();
                try {
                    mainLoop.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mainLoop = null;
        }
    }
}