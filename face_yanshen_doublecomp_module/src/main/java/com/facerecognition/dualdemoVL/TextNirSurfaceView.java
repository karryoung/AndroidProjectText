package com.facerecognition.dualdemoVL;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TextNirSurfaceView extends View {
    /**
     * 需要绘制的文字
     */
    private String mNirText;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 文本的大小
     */
    private int mTextSize;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public TextNirSurfaceView(Context context) {
        this(context, null);
    }

    public TextNirSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setFaces() {
        invalidate();
    }

    public TextNirSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        mNirText = "";
        //mTextColor = Color.WHITE;
        mTextSize = 30;

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        //mPaint.setColor(mTextColor);
        //获得绘制文本的宽和高
        mBound = new Rect();
        mPaint.getTextBounds(mNirText, 0, mNirText.length(), mBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        mNirText = LivenessThread.detectNirResultStr;
//        System.out.println("nir: " + mNirText);
        if("活体未通过".equals(mNirText) || "活体未通过，检测到黑白照片".equals(mNirText)){
            mPaint.setColor(Color.RED);
        }else{
            mPaint.setColor(Color.WHITE);
        }
        canvas.drawText(mNirText, 10, 30, mPaint);
    }

}
