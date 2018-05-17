package com.li.androidprojecttext.mvvm.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.li.androidprojecttext.R;

/**
 * Created by okkuaixiu on 2018/5/17.
 */

public class UserActivity extends AppCompatActivity {
    private TextView tvId;
    private TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}
