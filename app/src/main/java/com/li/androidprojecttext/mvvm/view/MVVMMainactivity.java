package com.li.androidprojecttext.mvvm.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.MvvmActivityBinding;
import com.li.androidprojecttext.databing.DatabingTestActivity;

/**
 * mvvm模式下入口activity
 */
public class MVVMMainactivity extends AppCompatActivity implements View.OnClickListener {

    private MvvmActivityBinding mvvmActivityBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvvmActivityBinding = DataBindingUtil.setContentView(this, R.layout.mvvm_activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_databing://databing页面
                Intent intent = new Intent(this, DatabingTestActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_demoactivity://每日一图页面
                Intent intentDemo = new Intent(this, DemoActivity.class);
                startActivity(intentDemo);
                break;
        }
    }
}
