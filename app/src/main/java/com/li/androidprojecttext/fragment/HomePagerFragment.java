package com.li.androidprojecttext.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.activity.DoubleCameraActivity;
import com.li.androidprojecttext.activity.FaceRecognitionDemoActivity;
import com.li.androidprojecttext.activity.NavigationActivity;
import com.li.androidprojecttext.activity.Viewpager2TestActivity;
import com.li.androidprojecttext.aidl.keeplive.ClientLocalService;
import com.li.androidprojecttext.aidl.keeplive.RemoteService;
import com.li.androidprojecttext.databinding.HomePagerFragmentBinding;
import com.li.androidprojecttext.databing.DatabingTestActivity;
import com.li.androidprojecttext.mvvm.view.DemoActivity;
import com.li.permission.LivePermissions;
import com.li.permission.PermissionResult;

/**
 * 首页Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class HomePagerFragment extends ViewPagerBaseFragment {

    //（volatile关键字在JDK1.5版本以上才会起作用，会屏蔽jvm做的代码优化，使用双重锁定机制有可能导致程序性能降低）
    private static volatile HomePagerFragment homePagerFragment;

    private HomePagerFragment() {
    }

    /**
     * 获取HomePagerFragment对象
     * 使用双重锁定机制
     */
    public static HomePagerFragment newInstance() {
        if (homePagerFragment == null) {
            synchronized (HomePagerFragment.class) {
                if (homePagerFragment == null) {
                    homePagerFragment = new HomePagerFragment();
                }
            }
        }
        return homePagerFragment;
    }

    HomePagerFragmentBinding binding;

    @Override
    protected int getLayoutResource() {
        return R.layout.home_pager_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }
        binding = DataBindingUtil.bind(view);
        binding.graySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGrayScale(0);
                } else {
                    setGrayScale(1);
                }
            }
        });
        binding.databingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatabingTestActivity.class);
                startActivity(intent);
            }
        });
        binding.mvvmDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DemoActivity.class));
            }
        });
        binding.viewpager2Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Viewpager2TestActivity.class));
            }
        });
        binding.faceTest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {


                new LivePermissions(HomePagerFragment.this).request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).observe(HomePagerFragment.this, new Observer<PermissionResult>() {
                    @Override
                    public void onChanged(PermissionResult permissionResult) {

                        if (permissionResult.equals(PermissionResult.Grant.INSTANCE)) {
                            Toast.makeText(getContext(), "权限允许", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), FaceRecognitionDemoActivity.class));
                        } else {
                            if (permissionResult instanceof PermissionResult.Deny) {
                                for (String result :
                                        ((PermissionResult.Deny) permissionResult).getPermission()) {
                                    Toast.makeText(getContext(), "拒绝且勾选不再询问" + result, Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (permissionResult instanceof PermissionResult.Rationale) {
                                for (String result :
                                        ((PermissionResult.Rationale) permissionResult).getPermission()) {
                                    Toast.makeText(getContext(), "权限拒绝" + result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
//                //第二个参数是需要申请的权限
//                if (ContextCompat.checkSelfPermission(getContext(),
//                        Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //权限还没有授予，需要在这里写申请权限的代码
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                            2);
//                } else {
//                    //权限已经被授予，在这里直接写要执行的相应方法即可
//                    startActivity(new Intent(getActivity(), FaceRecognitionDemoActivity.class));
//                }
            }
        });

        binding.doubleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DoubleCameraActivity.class));
            }
        });
        binding.doubleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //两个服务都要启动
                Intent intent1 = new Intent(getActivity(), RemoteService.class);
                getActivity().startService(intent1);
                Intent intent = new Intent(getActivity(), ClientLocalService.class);
                getActivity().startService(intent);
            }
        });
        binding.navigationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NavigationActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getActivity(), FaceRecognitionDemoActivity.class));
        }
    }

    /**
     * 设置灰度
     */
    private void setGrayScale(int saturation) {
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);//设置灰度0-1
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        getActivity().getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

}
