package com.facerecognition.dualdemoVL;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Environment;

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

import java.io.IOException;
import java.io.File;

/**
 *
 * @Description 主页面
 */
public class MainActivity extends Activity {
    //授权
    private String licPath = null;
    LicenseLoginDialog mLicenseLoginDialog;
    //相机参数,每次启动从sp中读取
    private SharedPreferences sharedPreferences;
    public static boolean exchangeCamera = false;//上下相机是否交换
    public static int topCameraAngle;//上方相机的旋转角度
    public static int topCameraPreviewAngle = 90;//上方相机的预览角度
    public static int bottomCameraAngle;//下方相机的旋转角度
    public static int bottomCameraPreviewAngle = 90;//下方相机的预览角度
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
    LivenessThread livingCheckThread;
    PointThread pointThread;
    //界面
    EditText etUserId, etTopCameraPreviewAngle, etBottomCameraPreviewAngle;
    Button btnRegist, btnSaveSetting, btn_openAndClose;
    ToggleButton tbExchangeCamera;
    TextView tvResult;
    TextVisSurfaceView tvVisResult;
    TextNirSurfaceView tvNirResult;
    FaceView faceView;
    SurfaceView topSurfaceView, bottomSurfaceView;//摄像头预览
    SurfaceHolder topSurfaceHolder, bottomSurfaceHolder;
    private Camera topCamera, bottomCamera;
    //消息处理
    private final int MSG_NO_FACE = 0;//无人脸
    private final int MSG_ACCESS_RESULT = 1;//通行结果
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvVisResult.setFaces();
            tvNirResult.setFaces();
            switch (msg.what) {
                case MSG_NO_FACE://无人脸
                    tvResult.setText("");
                    break;
                case MSG_ACCESS_RESULT://检测结果
                    Bundle bundle = msg.getData();
                    if (bundle.getBoolean("status")) {//通行
                        tvResult.setText(bundle.getString("userId"));
                    } else {//不通行
                        tvResult.setText("");
                    }
                    break;
                default:
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(R.layout.activity_main);
        ModelInit.initFras();  //初始化授权库
        ModelInit.initModelDirMap(getApplication());
        initCameraSetting();//初始化相机参数
        initView();//初始化布局
    }

    private void initCameraSetting() {
        sharedPreferences = getSharedPreferences("CameraSetting", MODE_PRIVATE);
        exchangeCamera = sharedPreferences.getBoolean("exchangeCamera", false);
        topCameraAngle = sharedPreferences.getInt("topCameraAngle", ConvertUtils.ROTATE_TYPE_90);
        topCameraPreviewAngle = sharedPreferences.getInt("topCameraPreviewAngle", topCameraPreviewAngle);
        bottomCameraAngle = sharedPreferences.getInt("bottomCameraAngle", ConvertUtils.ROTATE_TYPE_90);
        bottomCameraPreviewAngle = sharedPreferences.getInt("bottomCameraPreviewAngle", bottomCameraPreviewAngle);
    }

    private void initView() {
//        etUserId = findViewById(R.id.et_user_id);
//        etTopCameraPreviewAngle = findViewById(R.id.top_camera_preview_angle);
//        etBottomCameraPreviewAngle = findViewById(R.id.bottom_camera_preview_angle);
//        btnRegist = findViewById(R.id.btn_regist);
//        btnSaveSetting = findViewById(R.id.save_setting);
//        btn_openAndClose = findViewById(R.id.btn_openAndClose);
//        tbExchangeCamera = findViewById(R.id.exchange_camera);
//        tvResult = findViewById(R.id.tv_result);
//        tvVisResult = findViewById(R.id.tv_vis_result);
//        tvNirResult = findViewById(R.id.tv_nir_result);
        faceView = findViewById(R.id.face_view);
        topSurfaceView = findViewById(R.id.sv_top);
        bottomSurfaceView = findViewById(R.id.sv_nir);
        topSurfaceHolder = topSurfaceView.getHolder();
        bottomSurfaceHolder = bottomSurfaceView.getHolder();
        btnRegist.setOnClickListener(onClickListener);
        btnSaveSetting.setOnClickListener(onClickListener);
        btn_openAndClose.setOnClickListener(onClickListener);
        etTopCameraPreviewAngle.setText(topCameraPreviewAngle + "");
        etBottomCameraPreviewAngle.setText(bottomCameraPreviewAngle + "");
        tbExchangeCamera.setChecked(exchangeCamera);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dualdemoVL"; //  /storage/sdcard0/dualdemoVL/
        File f = new File(storagePath);
        if (!f.exists()) {
            f.mkdir();
        }
        licPath = storagePath+ "/license.dat";
        System.out.println("lic: " + licPath);
        mLicenseLoginDialog = new LicenseLoginDialog(this, licPath, R.style.AppTheme);
        if (!isInitEngine) {
            isInitEngine = true;
            initEngine();
        }
        new  Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openTwoCamera();
            }
        },1000);
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
            Toast.makeText(this, "人脸引擎初始化成功", Toast.LENGTH_SHORT).show();
            if (License.getLimitTime() <= 0) {
                License.removeLicense(licPath); // 未授权
                showLicenseDialog();
                Toast.makeText(this, "未授权", Toast.LENGTH_SHORT).show();
            } else if (License.getLimitTime() < System.currentTimeMillis()) {
                License.removeLicense(licPath);// 授权过期
                showLicenseDialog();
                Toast.makeText(this, "授权过期", Toast.LENGTH_SHORT).show();
            }
        } else {
            License.removeLicense(licPath);
            showLicenseDialog();
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
        int width=640;
        int height=480;
        if(topCameraPreviewAngle==90||topCameraPreviewAngle==270){
            width=480;
            height=640;
        }
        faceTracker = FaceTracker.createTracker(faceViewDetector, 10, 0.4f, 1,width, height);

    }

    private void showLicenseDialog() {
        Window dialogWindow = mLicenseLoginDialog.getWindow();
        WindowManager.LayoutParams winLp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL);
        winLp.x = 0;
        winLp.y = 0;
        winLp.alpha = 0.9f;
        dialogWindow.setAttributes(winLp);
        mLicenseLoginDialog.show();
    }

    // 检测到人脸时，发送通行状态
    public void sendGateStatus(boolean status, String userId) {
        if (mHandler != null) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            bundle.putBoolean("status", status);
            Message m = mHandler.obtainMessage();
            m.what = MSG_ACCESS_RESULT;
            m.setData(bundle);
            mHandler.sendMessage(m);
        }
    }

    //检测到人脸时，发送活体检测结果
    public  void  sendLivenessResult(float visResult, float nirResult) {
        if (mHandler != null) {
            Bundle bundle = new Bundle();
            bundle.putFloat("visResult", visResult);
            bundle.putFloat("nirResult", nirResult);
            Message m = mHandler.obtainMessage();
            m.what = MSG_ACCESS_RESULT;
            m.setData(bundle);
            mHandler.sendMessage(m);
        }
    }

    // 发送是否有人脸消息
    public void sendFaceStatus() {
        if (mHandler != null) {
            Message m = mHandler.obtainMessage();
            m.what = MSG_NO_FACE;
            mHandler.sendMessage(m);
        }
    }

    // 刷新FaceView
    public void notifyFaceView() {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    faceView.notifyView();
                }
            });
        }
    }

    /**
     * 界面上方的摄像头的回调
     */
    private SurfaceHolder.Callback topCameraCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    /**
     * 开启界面上方摄像头预览
     */
    private void openVisCameraPreview() {
        if (topCamera == null) {//相机未打开，添加回调，启动相机
            //topSurfaceHolder.addCallback(topCameraCallBack);
            if (topCamera == null) {
                if (!exchangeCamera) {//默认打开0
                    topCamera = Camera.open(0);
                } else {//交换后打开1
                    topCamera = Camera.open(1);
                }

                topCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        LivenessThread.visYuvData = data;//原始数据回显
                    }
                });
                try {
                    topCamera.setDisplayOrientation(topCameraPreviewAngle);//设置旋转角度
                    topCamera.setPreviewDisplay(topSurfaceHolder);
                    Camera.Parameters topParams = topCamera.getParameters();
                    topParams.setPreviewSize(640, 480);
                    topCamera.setParameters(topParams);
                    topCamera.startPreview();//开始预览
                    LivenessThread.visCameraWidth = topParams.getPreviewSize().width;
                    LivenessThread.visCameraHeight = topParams.getPreviewSize().height;
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
            topSurfaceHolder.removeCallback(topCameraCallBack);
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
            bottomSurfaceHolder.removeCallback(nirCameraCallBack);
            bottomCamera.stopPreview();
            bottomCamera.setPreviewCallback(null);
            bottomCamera.release();
            bottomCamera = null;
        }
    }

    /**
     * 界面下方的摄像头的回调
     */
    private SurfaceHolder.Callback nirCameraCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    /**
     * 开启界面下方摄像头预览
     */
    private void openNirCameraPreview() {
        if (bottomCamera == null) {//相机未打开，添加回调，启动相机
            //bottomSurfaceHolder.addCallback(nirCameraCallBack);
            if (bottomCamera == null) {
                if (!exchangeCamera) {//默认打开1
                    bottomCamera = Camera.open(1);
                } else {//交换后打开0
                    bottomCamera = Camera.open(0);
                }
                bottomCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        LivenessThread.nirYuvData = data;//原始数据回显
                    }
                });
                try {
                    bottomCamera.setDisplayOrientation(bottomCameraPreviewAngle);//设置旋转角度
                    bottomCamera.setPreviewDisplay(bottomSurfaceHolder);
                    Camera.Parameters nirParams = bottomCamera.getParameters();
                    nirParams.setPreviewSize(640, 480);
                    bottomCamera.setParameters(nirParams);
                    bottomCamera.startPreview();//开始预览
                    LivenessThread.nirCameraWidth = nirParams.getPreviewSize().width;
                    LivenessThread.nirCameraHeight = nirParams.getPreviewSize().height;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {//相机打开，直接打开预览
            bottomCamera.startPreview();
        }
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.btn_regist://注册人员
//                    String userId = etUserId.getText().toString().trim();
//                    if (TextUtils.isEmpty(userId)) {
//                        Toast.makeText(MainActivity.this, "请输入用户ID", Toast.LENGTH_SHORT).show();
//                    } else {
//                        int visWidth, visHeight;
//                        if (topCameraAngle == ConvertUtils.ROTATE_TYPE_90 || topCameraAngle == ConvertUtils.ROTATE_TYPE_270) {//交换宽高
//                            visWidth = LivenessThread.visCameraHeight;
//                            visHeight = LivenessThread.visCameraWidth;
//                        } else {
//                            visWidth = LivenessThread.visCameraWidth;
//                            visHeight = LivenessThread.visCameraHeight;
//                        }
//                        byte[] visBgr = ConvertUtils.yuvToBgrRotate(LivenessThread.visYuvData, LivenessThread.visCameraWidth, LivenessThread.visCameraHeight, ConvertUtils.ROTATE_TYPE_0);
//                        // 可见光图像翻转
//                        Bitmap visBitmap = ConvertUtils.bgrToBitmapRotate(visBgr, LivenessThread.visCameraWidth, LivenessThread.visCameraHeight, topCameraAngle);
//                        if (visBitmap != null) {
//                            visBgr = ConvertUtils.bitmapToBgrRotate(visBitmap, ConvertUtils.ROTATE_TYPE_0);
//                            visBitmap.recycle();
//                        }
//                        //对原图提取特征
//                        FaceDetectResult faceDetectResult = MainActivity.visFaceDetector.detectFace(visBgr, visWidth, visHeight);
//                        byte[] fea = null;
//                        if (faceDetectResult != null) {
//                            fea = MainActivity.faceExtractor.extractFeature(visBgr, visWidth, visHeight, faceDetectResult);
//                        }
//                        if (fea != null) {
//                            //将特征Base64编码
//                            String feaBase64 = Base64.encodeToString(fea, Base64.DEFAULT);
//                            //存储特征到数据库
//                            UserDB userPreference = new UserDB(MainActivity.this);
//                            userPreference.putFeature(userId, feaBase64);
//                            etUserId.setText("");
//                            Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "注册失败，请将面部对准摄像头", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    break;
//                case R.id.save_setting://保存相机设置
//                    String topStr = etTopCameraPreviewAngle.getText().toString();
//                    String bottomStr = etBottomCameraPreviewAngle.getText().toString();
//                    boolean exchange = tbExchangeCamera.isChecked();
//                    if (TextUtils.isEmpty(topStr) || TextUtils.isEmpty(bottomStr)) {
//                        Toast.makeText(MainActivity.this, "请输入以下角度:0，90，180，270", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    int top = Integer.parseInt(topStr);
//                    int topRotate;
//                    switch (top) {
//                        case 0:
//                            topRotate = ConvertUtils.ROTATE_TYPE_0;
//                            break;
//                        case 90:
//                            topRotate = ConvertUtils.ROTATE_TYPE_90;
//                            break;
//                        case 180:
//                            topRotate = ConvertUtils.ROTATE_TYPE_180;
//                            break;
//                        case 270:
//                            topRotate = ConvertUtils.ROTATE_TYPE_270;
//                            break;
//                        default:
//                            Toast.makeText(MainActivity.this, "请输入以下角度:0，90，180，270", Toast.LENGTH_SHORT).show();
//                            return;
//                    }
//
//                    int bottom = Integer.parseInt(bottomStr);
//                    int bottomRotate;
//                    switch (bottom) {
//                        case 0:
//                            bottomRotate = ConvertUtils.ROTATE_TYPE_0;
//                            break;
//                        case 90:
//                            bottomRotate = ConvertUtils.ROTATE_TYPE_90;
//                            break;
//                        case 180:
//                            bottomRotate = ConvertUtils.ROTATE_TYPE_180;
//                            break;
//                        case 270:
//                            bottomRotate = ConvertUtils.ROTATE_TYPE_270;
//                            break;
//                        default:
//                            Toast.makeText(MainActivity.this, "请输入以下角度:0，90，180，270", Toast.LENGTH_SHORT).show();
//                            return;
//                    }
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("exchangeCamera", exchange);
//                    editor.putInt("topCameraAngle", topRotate);
//                    editor.putInt("topCameraPreviewAngle", top);
//                    editor.putInt("bottomCameraAngle", bottomRotate);
//                    editor.putInt("bottomCameraPreviewAngle", bottom);
//                    editor.commit();
//                    Toast.makeText(MainActivity.this, "保存成功，下次启动生效", Toast.LENGTH_SHORT).show();
//                    break;
//
//                case R.id.btn_openAndClose:
//                    if("关闭".equals(btn_openAndClose.getText().toString())){
//                        closeTwoCamera();
//                        btn_openAndClose.setText("打开");
//                    }else{
//                        openTwoCamera();
//                        btn_openAndClose.setText("关闭");
//                    }
//
//                    break;
//            }
        }
    };
    private void openTwoCamera(){
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
        pointThread = new PointThread(this);
        pointThread.faceThreadStart();
    }
    private void closeTwoCamera(){
        //关闭人脸坐标线程
        if (pointThread != null) {
            pointThread.faceThreadStop();
            pointThread = null;
        }
        //关闭活体检测线程
        if (livingCheckThread != null) {
            livingCheckThread.stop();
            livingCheckThread = null;
        }
        closeVisCameraPreview();
        closeNirCameraPreview();
    }
}
