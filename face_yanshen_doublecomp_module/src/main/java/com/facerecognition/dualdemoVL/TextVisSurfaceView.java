package com.facerecognition.dualdemoVL;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TextVisSurfaceView extends View {
    /**
     * 需要绘制的文字
     */
    private String mVisText;
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

    public TextVisSurfaceView(Context context) {
        this(context, null);
    }

    public TextVisSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setFaces() {
        invalidate();
    }

    public TextVisSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        mVisText = "";
        //mTextColor = Color.WHITE;
        mTextSize = 30;

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);

        //获得绘制文本的宽和高
        mBound = new Rect();
        mPaint.getTextBounds(mVisText, 0, mVisText.length(), mBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        mVisText = LivenessThread.detectVisResultStr;
//        System.out.println("vis: " + mVisText);
        if("活体未通过".equals(mVisText) || "活体未通过，检测到黑白照片".equals(mVisText)){
            mPaint.setColor(Color.RED);
        }else{
            mPaint.setColor(Color.WHITE);
        }
        canvas.drawText(mVisText, 10, 30, mPaint);
    }
}
