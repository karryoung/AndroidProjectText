package com.li.androidprojecttext.fragment;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.HomePagerFragmentBinding;
import com.li.androidprojecttext.databing.DatabingTestActivity;
import com.li.androidprojecttext.mvvm.view.DemoActivity;

/**
 * 首页Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class HomePagerFragment extends ViewPagerBaseFragment {

    //（volatile关键字在JDK1.5版本以上才会起作用，会屏蔽jvm做的代码优化，使用双重锁定机制有可能导致程序性能降低）
    private static volatile HomePagerFragment homePagerFragment;

    private HomePagerFragment() {
    }

    /**
     * 获取HomePagerFragment对象
     * 使用双重锁定机制
     */
    public static HomePagerFragment newInstance(){
        if (homePagerFragment == null) {
            synchronized (HomePagerFragment.class){
                if (homePagerFragment == null) {
                    homePagerFragment = new HomePagerFragment();
                }
            }
        }
        return homePagerFragment;
    }

    HomePagerFragmentBinding binding;
    @Override
    protected int getLayoutResource() {
        return R.layout.home_pager_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }
        binding = DataBindingUtil.bind(view);
        binding.graySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGrayScale(0);
                } else {
                    setGrayScale(1);
                }
            }
        });
        binding.databingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatabingTestActivity.class);
                startActivity(intent);
            }
        });
        binding.mvvmDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DemoActivity.class));
            }
        });
    }

    /**
     * 设置灰度
     */
    private void setGrayScale(int saturation) {
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);//设置灰度0-1
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        getActivity().getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

}
