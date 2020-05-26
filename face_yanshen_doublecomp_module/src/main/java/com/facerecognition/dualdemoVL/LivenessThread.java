package com.facerecognition.dualdemoVL;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import com.am.fras.ConvertUtils;
import com.am.fras.FaceDetectResult;
//import com.am.fras.FaceEngine;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @Description 活体检测线程
 */

public class LivenessThread implements Runnable {

    private static final String TAG = "LivenessThread";
    //图像数据
    public static volatile byte[] visYuvData = null, nirYuvData = null;//相机的实时数据，为yuv格式
    public static volatile int visCameraWidth, visCameraHeight, nirCameraWidth, nirCameraHeight;//相机的宽高
    //默认分值
    private int visLivingScoreThreshold = 90;//可见光活体分数阈值(1~100)，越大越严格
    private int nirLivingScoreThreshold = 40;//近红外活体分数阈值(1~100)，越大越严格
    private int compareScoreThreshold = 80;//特征比对分数阈值（1~100）,越大越严格
    private float rollAngle = 90;
    private float yawAngle = 90;
    private float pitchAngle = 90;
    //检测线程中需要的相关参数
    private Bitmap visBitmap, nirBitmap;
    private byte[] visBgr, nirBgr;
    private int visWidth, visHeight, nirWidth, nirHeight;
    private FaceDetectResult visFaceRects, nirFaceRects;//人脸检测结果

    int visEyeDistance = 0;//可见光双眼间距
    double visVerticalDistance = 0;//可见光眉心(6号点)到嘴尖(8号点)距离
    int nirEyeDistance = 0;//红外光双眼间距
    double nirVerticalDistance = 0;//红外光眉心(6号点)到嘴尖(8号点)距离
//    int EYE_DISTANCE_VALUE = 5;//双眼间距的差值
//    int VERTICAL_DISTANCE_VALUE = 5;//眉心(6号点)到嘴尖(8号点)距离

    private byte[] feature;//提取到的特征
    private boolean livingCheckFlag = true;//活体检测开关
    private boolean livingCheckResult;//活体检测结果
    public static String detectVisResultStr="";//可见光活体检测结果文字输出
    public static String detectNirResultStr="";//近红外活体检测结果文字输出
    private long haveFaceTime = 0;//出现人脸的时间
    private final int NONE_FACE_TIMEOUT = 1500;
    private final int FACETHREAD_SLEEP_TIME = 100;
    //其他
    private volatile Thread mainLoop = null;
    private MainActivity mainActivity = null;
    private Context context;
    private Boolean shouldStop = false;

    /**
     * 开启检测线程
     *
     * @param ctx
     */
    public void start(Context ctx) {
        stop();
        context = ctx;
        mainActivity = (MainActivity) ctx;
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
            if (mainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_90 || mainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_270) {//交换宽高
                visWidth = this.visCameraHeight;
                visHeight = this.visCameraWidth;
            } else {
                visWidth = this.visCameraWidth;
                visHeight = this.visCameraHeight;
            }
            visBgr = ConvertUtils.yuvToBgrRotate(visYuvData, this.visCameraWidth, this.visCameraHeight, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
            if (visBgr == null) {
                continue;
            }
            // 采集近红外图片
            if (mainActivity.bottomCameraAngle == ConvertUtils.ROTATE_TYPE_90 || mainActivity.bottomCameraAngle == ConvertUtils.ROTATE_TYPE_270) {//交换宽高
                nirWidth = this.nirCameraHeight;
                nirHeight = this.nirCameraWidth;
            } else {
                nirWidth = this.nirCameraWidth;
                nirHeight = this.nirCameraHeight;
            }
            nirBgr = ConvertUtils.yuvToBgrRotate(nirYuvData, this.nirCameraWidth, this.nirCameraHeight, ConvertUtils.ROTATE_TYPE_0);//原始数据不需要转换
            if (nirBgr == null) {
                continue;
            }

            // 可见光图像翻转
            int topRotation = mainActivity.topCameraAngle;
            visBitmap = ConvertUtils.bgrToBitmapRotate(visBgr, this.visCameraWidth, this.visCameraHeight, topRotation);//旋转
            if (visBitmap != null) {
                visBgr = ConvertUtils.bitmapToBgrRotate(visBitmap, ConvertUtils.ROTATE_TYPE_0);
                visBitmap.recycle();
            }

            // 可见光人脸检测
            FaceDetectResult visFaceRects = MainActivity.visFaceDetector.detectFace(visBgr, visWidth, visHeight);
            if (visFaceRects == null || visFaceRects.landmarks == null) {
                long timeout = System.currentTimeMillis() - haveFaceTime;
                if (timeout > NONE_FACE_TIMEOUT) {
                    detectVisResultStr = "检测不到人脸！";
                    detectNirResultStr = "";
                    mainActivity.sendFaceStatus();
                }
                continue;
            }
            haveFaceTime = System.currentTimeMillis();

            // 人脸角度过滤
            if (Math.abs(visFaceRects.roll) > rollAngle || Math.abs(visFaceRects.yaw) > yawAngle || Math.abs(visFaceRects.pitch) > pitchAngle) {
                detectVisResultStr = "人脸角度过大！";
                detectNirResultStr = "";
                mainActivity.sendFaceStatus();
                continue;
            }

            int bottomRotation = mainActivity.bottomCameraAngle;
            // 活体检测
            if (livingCheckFlag) {
                // 近红外图像翻转
                nirBitmap = ConvertUtils.bgrToBitmapRotate(nirBgr, this.nirCameraWidth, this.nirCameraHeight, bottomRotation);
                if (nirBitmap != null) {
                    nirBgr = ConvertUtils.bitmapToBgrRotate(nirBitmap, ConvertUtils.ROTATE_TYPE_0);
                    nirBitmap.recycle();
                }

                // 近红外人脸检测
                nirFaceRects = MainActivity.nirFaceDetector.detectFace(nirBgr, nirWidth, nirHeight);
                if (nirFaceRects == null || nirFaceRects.landmarks == null) {
                    detectVisResultStr = "检测到攻击！";
                    detectNirResultStr = "检测到攻击！";
                    mainActivity.sendFaceStatus();
                    continue;
                }

                nirEyeDistance = getEyeDistance(nirFaceRects.landmarks, "近红外双眼___");
                nirVerticalDistance = getVerticalDistance(nirFaceRects.landmarks, "近红外双眼___");
                Log.e("双眼", "---------------------------------------------------间距差:" + Math.abs(nirEyeDistance - visEyeDistance) + ",纵向差:" + Math.abs(nirVerticalDistance - visVerticalDistance));
//                //眼间距差值不能大于5，眉心到嘴尖距离不能大于5
//                if (Math.abs(nirEyeDistance - visEyeDistance) > EYE_DISTANCE_VALUE || Math.abs(nirVerticalDistance - visVerticalDistance) > VERTICAL_DISTANCE_VALUE) {
//                    Log.e("TIME_COUNT", "### 双眼间距未通过");
//                    continue;
//                }

                // 近红外活体判断
                livingCheckResult = checkLiving(visFaceRects,nirFaceRects,visBgr,visWidth,visHeight,visLivingScoreThreshold,nirBgr,nirWidth,nirHeight,nirLivingScoreThreshold);
//                mainActivity.sendLivenessResult(detectVisResultStr,detectNirResultStr);
                if (!livingCheckResult) {
                    Log.e("TIME_COUNT", "### 非活体 ");
                    continue;
                } else {
                    Log.e("TIME_COUNT", "### 是活体 ");
                }
            }
            visFaceRects.left = Math.min(Math.max(visFaceRects.left, 0), visWidth - 1);
            visFaceRects.right = Math.min(Math.max(visFaceRects.right, 0), visWidth - 1);
            visFaceRects.top = Math.min(Math.max(visFaceRects.top, 0), visHeight - 1);
            visFaceRects.bottom = Math.min(Math.max(visFaceRects.bottom, 0), visHeight - 1);

            // 抽取特征
            feature = MainActivity.faceExtractor.extractFeature(visBgr, visWidth, visHeight, visFaceRects);
            if (feature == null) {
                continue;
            }

            // 五、住户库比对
            String matchedUserId = compareUser(feature, compareScoreThreshold);
            if (matchedUserId == null) {
                mainActivity.sendGateStatus(false, "");
                continue;
            }
            // 开门
            mainActivity.sendGateStatus(true, matchedUserId);
        }
    }

    /**
     * 判断红外光是否检测通过
     *
     * @param visFaceRects                     可见光人脸检测结果
     * @param nirFaceRects                     近红外人脸检测结果
     * @param visBgr                            可见光bgr数据
     * @param visWidth                          可见光照片的宽
     * @param visHeight                         可见光照片的高
     * @param visLivingScoreThreshold         可见光活体分数阈值
     * @param nirBgr                            近红外bgr数据
     * @param nirWidth                         近红外照片的宽
     * @param nirHeight                        近红外照片的高
     * @param nirLivingScoreThreshold        近红外活体分数阈值
     * @return 红外活体达到阈值返回true，反之返回false
     */
    private boolean checkLiving(FaceDetectResult visFaceRects, FaceDetectResult nirFaceRects, byte[] visBgr, int visWidth, int visHeight, int visLivingScoreThreshold, byte[] nirBgr, int nirWidth, int nirHeight, int nirLivingScoreThreshold) {

        if (visFaceRects== null) {
            Log.d(TAG, "可见光无人脸");
            return false;
        }
        if (nirFaceRects == null) {
            Log.d(TAG, "近红外无人脸");
            return false;
        }

        float visAntiScore = MainActivity.redSpoofAnti.getBlackAndWhiteScore(visBgr, visWidth, visHeight, 10,visFaceRects) * 100;
        float nirAntiScore = MainActivity.redSpoofAnti.getAntiScore(nirBgr, nirWidth, nirHeight, nirFaceRects) * 100;

        Log.e("TIME_COUNT", "## 可见光活体分数 " + visLivingScoreThreshold + ":" + visAntiScore);
        Log.e("TIME_COUNT", "## 近红外活体分数 " + nirLivingScoreThreshold + ":" + nirAntiScore);

        if (nirAntiScore < nirLivingScoreThreshold) {
            detectVisResultStr = "活体未通过" ;
            detectNirResultStr = "活体未通过" ;
            mainActivity.sendLivenessResult(visAntiScore, nirAntiScore);
            return false;
        } else if (visAntiScore > visLivingScoreThreshold){
            detectVisResultStr = "活体未通过，检测到黑白照片";
            detectNirResultStr = "活体未通过，检测到黑白照片";
            mainActivity.sendLivenessResult(visAntiScore, nirAntiScore);
            return false;
        }else {
            detectVisResultStr = "活体通过" ;
            detectNirResultStr = "活体通过" ;
           // mainActivity.sendLivenessResult(visAntiScore, nirAntiScore);
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

    /**
     * 在本地数据库中进行比对,SQLite版本
     *
     * @param fea         提取出来的人脸特征
     * @param threshScore 比对分数阈值
     * @return 比对成功，返回对应的人员id
     */
    private String compareUser(byte[] fea, int threshScore) {
        //取出人员数据
        UserDB userPreference = new UserDB(context);
        Map<String, ?> featureMap = userPreference.getAllFeature();
        //数据为空返回null
        if (featureMap.size() == 0) {
            return null;
        }
        //取userId集合
        Set<String> userIdSet = featureMap.keySet();
        Iterator<String> keyIterator = userIdSet.iterator();
        String userId;
        byte[] featureTarget;
        float score = 0;
        while (keyIterator.hasNext()) {
            userId = keyIterator.next();//取key值
            String feature = (String) featureMap.get(userId);
            featureTarget = Base64.decode(feature, Base64.DEFAULT);
            score = MainActivity.faceExtractor.matchFeatures(fea, featureTarget);
            score *= 100;//分值为0.86之类的小数，需要乘100
            if (score > 100) {
                score = 100;
            }
            if (score > threshScore) {
                //detectVisResultStr = detectVisResultStr +"   比对分数:";
                return userId;
            }
        }
        return null;
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
        context = null;
        mainActivity = null;
    }
}