package com.li.androidprojecttext.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
