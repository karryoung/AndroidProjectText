package com.li.androidprojecttext.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.li.androidprojecttext.R;

/**
 * MainActivity类
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private TextView homepager_tv, store_tv, message_tv, my_center_tv;//首页, 商店, 消息, 个人中心

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //控件初始化
    private void init() {
        viewPager = (ViewPager) findViewById(R.id.tab_viewPager);
        View view_tab = findViewById(R.id.main_tab);
        homepager_tv = (TextView) view_tab.findViewById(R.id.homepager_tv);
        homepager_tv.setOnClickListener(MainActivity.this);
        store_tv = (TextView) view_tab.findViewById(R.id.store_tv);
        store_tv.setOnClickListener(MainActivity.this);
        message_tv = (TextView) view_tab.findViewById(R.id.message_tv);
        message_tv.setOnClickListener(MainActivity.this);
        my_center_tv = (TextView) view_tab.findViewById(R.id.my_center_tv);
        my_center_tv.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homepager_tv://首页的点击事件
                break;
            case R.id.store_tv://商店的点击事件
                break;
            case R.id.message_tv://消息的点击事件
                break;
            case R.id.my_center_tv://个人中心的点击事件
                break;
        }
    }
}
