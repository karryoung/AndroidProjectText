package com.li.androidprojecttext.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.li.androidprojecttext.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * fragment中使用viewpager适配器
 */
public class MainViewPagerAdapter extends LazyFragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments;
    public MainViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
