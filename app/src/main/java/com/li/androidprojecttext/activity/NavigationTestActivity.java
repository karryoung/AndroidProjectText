package com.li.androidprojecttext.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.li.androidprojecttext.R;


/**
 * @CreateDate: 2020/4/29 12:01
 * @Description: Navigation的使用
 * @Author: 李想
 */
public class NavigationTestActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_navigation);
    }

}
