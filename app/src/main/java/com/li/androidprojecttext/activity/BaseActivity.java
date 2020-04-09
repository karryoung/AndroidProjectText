package com.li.androidprojecttext.activity;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.li.androidprojecttext.view.GrayFrameLayout;

/**
 * 所有Activity公共类
 * Created by okkuaixiu on 2017/3/15.
 */

public class BaseActivity extends FragmentActivity {

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        return setGrayScale1(name, context, attrs);
        return super.onCreateView(name, context, attrs);
    }

    /**
     * @param name
     * @param context
     * @param attrs
     * @return
     * 设置灰度的第一种方法
     */
    public View setGrayScale1(String name, Context context, AttributeSet attrs){
        //这个步骤可以将对应的页面变为黑白色，状态栏需要单独设置
        if("FrameLayout".equals(name)){
            int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                String attributeName = attrs.getAttributeName(i);
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeName.equals("id")) {
                    int id = Integer.parseInt(attributeValue.substring(1));
                    String idVal = getResources().getResourceName(id);
                    if ("android:id/content".equals(idVal)) {
                        GrayFrameLayout grayFrameLayout = new GrayFrameLayout(context, attrs);
                        //如果Activity中的window设置了background,那么需要取出背景图再重新设置一下
                        Drawable drawable = getWindow().getDecorView().getBackground();
                        if (drawable != null) {
                            grayFrameLayout.setBackgroundDrawable(drawable);
                        }
                        //如果是theme 中设置的 windowBackground，那么需要从 theme 里面提取 drawable
//                        TypedValue a = new TypedValue();
//                        getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
//                        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
//                            // windowBackground is a color
//                            int color = a.data;
//                            grayFrameLayout.setBackgroundColor(color);
//                        } else {
//                            // windowBackground is not a color, probably a drawable
//                            Drawable c = getResources().getDrawable(a.resourceId);
//                        if (c != null) {
//                            grayFrameLayout.setBackground(c);
//                        }
//                        }
                        return grayFrameLayout;
                    }
                }
            }
        }
        return super.onCreateView(name, context, attrs);
    }

    /**
     * 设置灰度的第二种方法
     */
    private void setGrayScale(int saturation) {
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);//设置灰度0-1
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        //如果使用这种方式不需要单独设置状态栏
    }
}
