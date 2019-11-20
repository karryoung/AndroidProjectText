package com.li.androidprojecttext.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.li.androidprojecttext.R;

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

    @Override
    protected int getLayoutResource() {
        return R.layout.home_pager_fragment;
    }

    @Override
    protected void initView() {

    }
}
