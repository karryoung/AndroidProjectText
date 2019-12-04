package com.li.androidprojecttext.mvvm.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.DemoActivityBinding;
import com.li.androidprojecttext.mvvm.model.BaseData;
import com.li.androidprojecttext.mvvm.model.ImageBean;
import com.li.androidprojecttext.mvvm.viewmodel.DemoActivityViewModel;

public class DemoActivity extends AppCompatActivity {

    private DemoActivityBinding activityDemoBinding;
    private DemoActivityViewModel demoActivityViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demoActivityViewModel = ViewModelProviders.of(this).get(DemoActivityViewModel.class);
        activityDemoBinding = DataBindingUtil.setContentView(this, R.layout.demo_activity);
        activityDemoBinding.setViewModel(demoActivityViewModel);
        activityDemoBinding.setEventListener(new OnEventListener());

        progressDialog = new ProgressDialog(DemoActivity.this);
        progressDialog.setMessage("加载中.....");

        demoActivityViewModel.getImage();
        progressDialog.show();
        demoActivityViewModel.getImageBean().observe(this, new Observer<BaseData<ImageBean>>() {
            @Override
            public void onChanged(BaseData<ImageBean> imageBeanBaseData) {
                if (imageBeanBaseData.getData() != null) {
                    activityDemoBinding.setImage(imageBeanBaseData.getData().getImages().get(0));
                } else {
                    Toast.makeText(DemoActivity.this, imageBeanBaseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public class OnEventListener {
        public void onClick(View view) {
            progressDialog.show();
            switch (view.getId()) {
                case R.id.tv_previous:
                    demoActivityViewModel.getPreviousImage();
                    break;
                case R.id.tv_next:
                    demoActivityViewModel.getNextImage();
                    break;
            }
        }
    }
}
