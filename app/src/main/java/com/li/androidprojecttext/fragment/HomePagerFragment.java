package com.li.androidprojecttext.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 首页Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class HomePagerFragment extends Fragment {

    private static HomePagerFragment homePagerFragment;
    /*
    获取HomePagerFragment对象
     */
    public static HomePagerFragment newInstance(){
        return homePagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
