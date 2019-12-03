package com.li.androidprojecttext.fragment;

import com.li.androidprojecttext.R;

/**
 * 商店Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class StoreFragment extends ViewPagerBaseFragment {

    /**
     * 饿汉式单例模式
     * 在同一classLoader下，该方式可以解决多线程同步的问题，
     * 但是该种单例模式没有办法实现懒加载
     */
    private static StoreFragment storeFragment = new StoreFragment();
    private StoreFragment(){
    }
    public static StoreFragment newInstance(){
        return storeFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.store_fragment;
    }

    @Override
    protected void initView() {

    }
}
