package com.li.androidprojecttext.fragment.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.FragmentNavigation2Binding;

/**
 * @CreateDate: 2020/4/29 15:18
 * @Description: 类作用描述
 * @Author: 李想
 */
public class NavigationFragment2 extends Fragment {

    FragmentNavigation2Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation2, container, false);
        binding.fg2Tv.setText(getArguments().getString("et"));
        return binding.getRoot();
    }
}
