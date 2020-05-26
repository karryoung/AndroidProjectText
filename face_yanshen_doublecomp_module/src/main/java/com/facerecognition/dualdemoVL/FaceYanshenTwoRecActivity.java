package com.facerecognition.dualdemoVL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.am.fras.ConvertUtils;
import com.am.fras.FaceDetectResult;
import com.am.fras.FaceDetector;
import com.am.fras.FaceDetectorParam;
import com.am.fras.FaceExtractor;
import com.am.fras.FaceTracker;
import com.am.fras.FaceTrackerResult;
import com.am.fras.License;
import com.am.fras.LicenseError;
import com.am.fras.RedSpoofAnti;

import java.io.File;
import java.io.IOException;

/**
 * @Description 主页面
 */
public class FaceYanshenTwoRecActivity extends Activity {
    //授权
    private String licPath = null;

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
    //面部追踪
    public static FaceTracker faceTracker = null;
    public static FaceTrackerResult faceTrackerResult = null;

    private boolean isInitEngine = false;
    private LivenessThread livingCheckThread;

    private SurfaceView topSurfaceView, bottomSurfaceView;//摄像头预览
    private SurfaceHolder topSurfaceHolder, bottomSurfaceHolder;

    private Camera topCamera, bottomCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(R.layout.activity_main);
        ModelInit.initFras();  //初始化授权库
        ModelInit.initModelDirMap(getApplication());
        initView();//初始化布局
    }

    private void initView() {
        topSurfaceView = findViewById(R.id.sv_top);
        bottomSurfaceView = findViewById(R.id.sv_nir);
        topSurfaceHolder = topSurfaceView.getHolder();
        bottomSurfaceHolder = bottomSurfaceView.getHolder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dualdemoVL"; //  /storage/sdcard0/dualdemoVL/
        File f = new File(storagePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        licPath = storagePath + "/license.dat";
        System.out.println("lic: " + licPath);
        if (!isInitEngine) {
            isInitEngine = true;
            initEngine();
        }
        openTwoCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTwoCamera();
    }

    public void initEngine() {
        // 授权成功后再初始化
        if (License.verifyLicense(licPath) == LicenseError.AM_E_SUCCESS) {

        } else {
            License.removeLicense(licPath);
            Toast.makeText(this, "授权情况：" + LicenseError.ErrnoStr(License.verifyLicense(licPath)), Toast.LENGTH_SHORT).show();
        }
        // 初始化活体引擎
        faceExtractor = FaceExtractor.createExtractor(ModelInit.getModelDirMap().get(R.raw.extract));
        redSpoofAnti = RedSpoofAnti.createSpoofAnti(ModelInit.getModelDirMap().get(R.raw.anti));

        //对各个检测器设置参数
        visFaceDetector = FaceDetector.createDetector(ModelInit.getModelDirMap().get(R.raw.detect));
        FaceDetectorParam visFaceDetectorParam = new FaceDetectorParam();
        visFaceDetectorParam.scaleFactor = 1.50f;
        visFaceDetectorParam.minObjSize = 10;
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
        if (topCamera == null) {//相机未打开，添加回调，启动相机
            //topSurfaceHolder.addCallback(topCameraCallBack);
            if (topCamera == null) {
                topCamera = Camera.open(1);
                topCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        visYuvData = data;//原始数据回显
                    }
                });
                try {
                    //topCamera.setDisplayOrientation(topCameraPreviewAngle);//设置旋转角度
                    topCamera.setPreviewDisplay(topSurfaceHolder);
                    Camera.Parameters topParams = topCamera.getParameters();
                    topParams.setPreviewSize(CAMERA_WIDTH, CAMERA_HEIGHT);
                    topCamera.setParameters(topParams);
                    topCamera.startPreview();//开始预览
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {//相机打开，直接打开预览
            topCamera.startPreview();
        }
    }

    /**
     * 关闭界面上方摄像头预览
     */
    private void closeVisCameraPreview() {
        if (null != topCamera) {
            //topSurfaceHolder.removeCallback(topCameraCallBack);
            topCamera.stopPreview();
            topCamera.setPreviewCallback(null);
            topCamera.release();
            topCamera = null;

        }
    }

    /**
     * 关闭界面下方摄像头预览
     */
    private void closeNirCameraPreview() {
        if (null != bottomCamera) {
            //bottomSurfaceHolder.removeCallback(nirCameraCallBack);
            bottomCamera.stopPreview();
            bottomCamera.setPreviewCallback(null);
            bottomCamera.release();
            bottomCamera = null;
        }
    }

    /**
     * 开启界面下方摄像头预览
     */
    private void openNirCameraPreview() {
        if (bottomCamera == null) {//相机未打开，添加回调，启动相机
            //bottomSurfaceHolder.addCallback(nirCameraCallBack);
            if (bottomCamera == null) {
                bottomCamera = Camera.open(0);
                bottomCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        nirYuvData = data;//原始数据回显
                    }
                });
                try {
                    //bottomCamera.setDisplayOrientation(bottomCameraPreviewAngle);//设置旋转角度
                    bottomCamera.setPreviewDisplay(bottomSurfaceHolder);
                    Camera.Parameters nirParams = bottomCamera.getParameters();
                    nirParams.setPreviewSize(CAMERA_WIDTH, CAMERA_HEIGHT);
                    bottomCamera.setParameters(nirParams);
                    bottomCamera.startPreview();//开始预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {//相机打开，直接打开预览
            bottomCamera.startPreview();
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
        livingCheckThread.start(this);
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
        private Bitmap visBitmap, nirBitmap;
        private byte[] visBgr, nirBgr;
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
        private Boolean shouldStop = false;

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
            shouldStop = false;
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
            while (!shouldStop) {
                // 采集可见光图片
                visBgr = ConvertUtils.yuvToBgrRotate(visYuvData, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
                if (visBgr == null) {
                    continue;
                }
                // 采集近红外图片
                nirBgr = ConvertUtils.yuvToBgrRotate(nirYuvData, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
                if (nirBgr == null) {
                    continue;
                }

                // 可见光图像翻转
                visBitmap = ConvertUtils.bgrToBitmapRotate(visBgr, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);//旋转
                if (visBitmap != null) {
                    visBgr = ConvertUtils.bitmapToBgrRotate(visBitmap, ConvertUtils.ROTATE_TYPE_0);
                    visBitmap.recycle();
                }

                // 可见光人脸检测
                FaceDetectResult visFaceRects = visFaceDetector.detectFace(visBgr, CAMERA_WIDTH, CAMERA_HEIGHT);
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
                nirBitmap = ConvertUtils.bgrToBitmapRotate(nirBgr, CAMERA_WIDTH, CAMERA_HEIGHT, ConvertUtils.ROTATE_TYPE_0);
                if (nirBitmap != null) {
                    nirBgr = ConvertUtils.bitmapToBgrRotate(nirBitmap, ConvertUtils.ROTATE_TYPE_0);
                    nirBitmap.recycle();
                }

                // 近红外人脸检测
                nirFaceRects = nirFaceDetector.detectFace(nirBgr, CAMERA_WIDTH, CAMERA_HEIGHT);
                if (nirFaceRects == null || nirFaceRects.landmarks == null) {
                    // "检测到攻击！";
                    continue;
                }

                nirEyeDistance = getEyeDistance(nirFaceRects.landmarks, "近红外双眼___");
                nirVerticalDistance = getVerticalDistance(nirFaceRects.landmarks, "近红外双眼___");
                Log.e("双眼", "---------------------------------------------------间距差:" + Math.abs(nirEyeDistance - visEyeDistance) + ",纵向差:" + Math.abs(nirVerticalDistance - visVerticalDistance));
                //眼间距差值不能大于5，眉心到嘴尖距离不能大于5
                //if (Math.abs(nirEyeDistance - visEyeDistance) > EYE_DISTANCE_VALUE || Math.abs(nirVerticalDistance - visVerticalDistance) > VERTICAL_DISTANCE_VALUE) {
                //  Log.e("TIME_COUNT", "### 双眼间距未通过");
                //  continue;
                //}

                // 近红外活体判断
                livingCheckResult = checkLiving(visFaceRects, nirFaceRects, visBgr, CAMERA_WIDTH, CAMERA_HEIGHT, visLivingScoreThreshold, nirBgr, CAMERA_WIDTH, CAMERA_HEIGHT, nirLivingScoreThreshold);
                if (!livingCheckResult) {
                    Log.e("TIME_COUNT", "### 非活体 ");
                    continue;
                } else {
                    Log.e("TIME_COUNT", "### 是活体 ");
                }

                Log.i("zhongyang", "visFaceRects:" + visFaceRects.toString());
                // 抽取特征
                feature = faceExtractor.extractFeature(visBgr, CAMERA_WIDTH, CAMERA_HEIGHT, visFaceRects);
                if (feature == null) {
                    continue;
                }
                Log.i("zhongyang", "feature:" + Base64.encodeToString(feature, Base64.DEFAULT));

                // 五、住户库比对
//            faceExtractor.matchFeatures(fea, featureTarget)
//            sendGateStatus(true, matchedUserId);
            }
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
            shouldStop = true;
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