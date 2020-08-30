package com.li.androidprojecttext.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.ActivityNavigationBinding;


/**
 * @CreateDate: 2020/4/29 12:01
 * @Description: Navigation的使用
 * @Author: 李想
 */
public class NavigationTestActivity extends AppCompatActivity {

    private ActivityNavigationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);
    }

}
