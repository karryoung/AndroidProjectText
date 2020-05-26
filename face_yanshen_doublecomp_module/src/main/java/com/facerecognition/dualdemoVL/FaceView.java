package com.facerecognition.dualdemoVL;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.am.fras.ConvertUtils;
import com.am.fras.FaceTrackerResult;

/**
 *
 * @Description 自定义人脸框
 */
@SuppressLint("AppCompatCustomView")
public class FaceView extends ImageView {
    private Paint paint;
    private int lineLength = 35;
    public FaceTrackerResult face = null;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(6f);
        paint.setAlpha(180);
    }

    /**
     * 更新数据，重绘View
     */
    public void notifyView() {
        invalidate();
        face=MainActivity.faceTrackerResult;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (face == null || face.faceList.length < 1) {
            return;
        }
        canvas.save();
        float width = getWidth();
        float scaleWidth;
        float height = getHeight();
        float scaleHeight;
        if (MainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_90 || MainActivity.topCameraAngle == ConvertUtils.ROTATE_TYPE_270) {//交换宽高
            scaleWidth = width / LivenessThread.visCameraHeight;
            scaleHeight = height / LivenessThread.visCameraWidth;
        } else {
            scaleWidth = width / LivenessThread.visCameraWidth;
            scaleHeight = height / LivenessThread.visCameraHeight;
        }


        float left, top, right, bottom;
        for (int i = 0; i <  face.faceList.length; i++) {
            left =  face.faceList[i].faceDetectResult.left * scaleWidth;
            top =  face.faceList[i].faceDetectResult.top * scaleHeight;
            right =  face.faceList[i].faceDetectResult.right * scaleWidth;
            bottom =  face.faceList[i].faceDetectResult.bottom * scaleHeight;
            lineLength = (int) ((right - left) * 0.2);

            canvas.drawLine(left, top, left + lineLength, top, paint);
            canvas.drawLine(left, top, left, top + lineLength, paint);

            canvas.drawLine(right, top, right, top + lineLength, paint);
            canvas.drawLine(right, top, right - lineLength, top, paint);

            canvas.drawLine(left, bottom, left, bottom - lineLength, paint);
            canvas.drawLine(left, bottom, left + lineLength, bottom, paint);

            canvas.drawLine(right, bottom, right, bottom - lineLength, paint);
            canvas.drawLine(right, bottom, right - lineLength, bottom, paint);

        }
        canvas.restore();
        super.onDraw(canvas);
    }


}
