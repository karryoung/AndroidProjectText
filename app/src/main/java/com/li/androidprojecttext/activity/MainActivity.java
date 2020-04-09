package com.li.androidprojecttext.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.adapter.MainViewPagerAdapter;
import com.li.androidprojecttext.fragment.BaseFragment;
import com.li.androidprojecttext.fragment.HomePagerFragment;
import com.li.androidprojecttext.fragment.MessageFragment;
import com.li.androidprojecttext.fragment.MyCenterFragment;
import com.li.androidprojecttext.fragment.StoreFragment;

import java.util.ArrayList;

/**
 * MainActivity类
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private TextView homepager_tv, store_tv, message_tv, my_center_tv;//首页, 商店, 消息, 个人中心
    private HomePagerFragment homePagerFragment;//首页Fragment
    private StoreFragment storeFragment;//商店Fragment
    private MessageFragment messageFragment;//消息Fragment
    private MyCenterFragment myCenterFragment;//个人中心Fragment

    ArrayList<BaseFragment> fragmentList;
    private FragmentManager fragmentManager;
    private MainViewPagerAdapter mainViewPagerAdapter;

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
        fragmentManager = getSupportFragmentManager();
        homePagerFragment = (HomePagerFragment) instantiateFragment(viewPager, 0, HomePagerFragment.newInstance());
        messageFragment = (MessageFragment) instantiateFragment(viewPager, 1, MessageFragment.newInstance());
        storeFragment = (StoreFragment) instantiateFragment(viewPager, 2, StoreFragment.newInstance());
        myCenterFragment = (MyCenterFragment) instantiateFragment(viewPager, 3, MyCenterFragment.newInstance());
        fragmentList = new ArrayList<>();
        fragmentList.add(homePagerFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(storeFragment);
        fragmentList.add(myCenterFragment);
        viewPager.setOffscreenPageLimit(4);//设置viewpager的缓存页面的个数
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mainViewPagerAdapter);
    }


    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult) {
        //注意这里获取的tag的方式，如果adapter里面的获取tag的改变了，此处也要改变（建议使用viewpager2）
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
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

    public void chooseFragment(int fragmentOrder){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //设置正常的图标
//        setTabNormalImag();
        //隐藏全部fragment
//        hideFragmentAll(transaction);
        //更换图标
        switch (fragmentOrder){
            case 1://首页
                //设置点击后变换的图标
//                homepager_tv.setCompoundDrawablesWithIntrinsicBounds(null,
//                        getResources().getDrawable(R.drawable.home_select_img), null, null);
                if (homePagerFragment == null) {
                    homePagerFragment = HomePagerFragment.newInstance();
//                    transaction.add(R.id.main_layout, homePagerFragment, fragmentOrder + "");
                    fragmentList.add(homePagerFragment);
                } else {
                    homePagerFragment = (HomePagerFragment) fragmentManager.findFragmentByTag(fragmentOrder + "");
                    transaction.show(homePagerFragment);
                }
                break;
//            case 2://资讯
//                advisory_tab_tv.setCompoundDrawablesWithIntrinsicBounds(null,
//                        getResources().getDrawable(R.drawable.advisory_select_img), null, null);
//                if (advisoryFragment == null) {
//                    advisoryFragment = AdvisoryFragment.getInstance();
//                    transaction.add(R.id.main_layout, advisoryFragment, fragmentOrder + "");
//                    fragmentList.add(advisoryFragment);
//                } else {
//                    advisoryFragment = (AdvisoryFragment) fragmentManager.findFragmentByTag(fragmentOrder + "");
//                    transaction.show(advisoryFragment);
//                }
//                break;
//            case 3://爱车
//                mycar_tab_tv.setCompoundDrawablesWithIntrinsicBounds(null,
//                        getResources().getDrawable(R.drawable.car_select_img), null, null);
//                if (myCarFragment == null) {
//                    myCarFragment = MyCarFragment.getInstance();
//                    transaction.add(R.id.main_layout, myCarFragment, fragmentOrder + "");
//                    fragmentList.add(myCarFragment);
//                } else {
//                    myCarFragment = (MyCarFragment) fragmentManager.findFragmentByTag(fragmentOrder + "");
//                    transaction.show(myCarFragment);
//                }
//                break;
//            case 4://我的
//                mycenter_tab_tv.setCompoundDrawablesWithIntrinsicBounds(null,
//                        getResources().getDrawable(R.drawable.center_select_img), null, null);
//                if (myCenterFragment == null) {
//                    myCenterFragment = CenterFragment.getInstance();
//                    transaction.add(R.id.main_layout, myCenterFragment, fragmentOrder + "");
//                    fragmentList.add(myCenterFragment);
//                } else {
//                    myCenterFragment = (CenterFragment) fragmentManager.findFragmentByTag(fragmentOrder + "");
//                    transaction.show(myCenterFragment);
//                }
//                break;
        }
        transaction.commit();
    }
}
