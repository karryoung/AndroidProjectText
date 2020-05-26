package com.facerecognition.dualdemoVL;



import android.graphics.Bitmap;
import android.os.SystemClock;

import com.am.fras.ConvertUtils;


/**
 *
 * @Description 人脸检测线程，用于绘制人脸框
 */
public class PointThread implements Runnable {
    private static final String TAG = "PointThread";

    private final int FACETHREAD_SLEEP_TIME = 30;

    private volatile static Thread mainLoop = null;
    private Boolean shouldStop = false;
    private Bitmap visBitmap;
    private int visWidth, visHeight;
    private byte[] visBgr = null;
    private MainActivity mainActivity;

    public PointThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void faceThreadStart() {
        faceThreadStop();
        while (mainLoop != null) {
            SystemClock.sleep(FACETHREAD_SLEEP_TIME);
        }
        shouldStop = false;
        if (mainLoop == null) {
            mainLoop = new Thread(this);
            mainLoop.start();
        } else {
            mainLoop.start();
        }
    }

    public void faceThreadStop() {
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


    @Override
    public void run() {
        while (!shouldStop) {
            if (mainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_90 || mainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_270) {//交换宽高
                visWidth = LivenessThread.visCameraHeight;
                visHeight = LivenessThread.visCameraWidth;
            } else {
                visWidth = LivenessThread.visCameraWidth;
                visHeight = LivenessThread.visCameraHeight;
            }
            visBgr = ConvertUtils.yuvToBgrRotate(LivenessThread.visYuvData, LivenessThread.visCameraWidth, LivenessThread.visCameraHeight, ConvertUtils.ROTATE_TYPE_0);
            visBitmap = ConvertUtils.bgrToBitmapRotate(visBgr, LivenessThread.visCameraWidth, LivenessThread.visCameraHeight, ConvertUtils.ROTATE_TYPE_0);
            if (visBitmap != null) {
                visBgr = ConvertUtils.bitmapToBgrRotate(visBitmap, ConvertUtils.ROTATE_TYPE_0);
                visBitmap.recycle();
                visBitmap = null;
            }
            if (visBgr != null && LivenessThread.visCameraWidth > 0 && LivenessThread.visCameraHeight > 0) {
                mainActivity.faceTrackerResult = mainActivity.faceTracker.track(visBgr,visWidth, visHeight, visWidth*3,24);
                mainActivity.notifyFaceView();//绘制人脸框消息
            }
        }
    }
}
