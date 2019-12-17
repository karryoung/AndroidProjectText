package com.li.androidprojecttext.databing.viewmodel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.ActivityViewmodelBinding;
import com.li.androidprojecttext.databing.viewmodel.vm.MainViewModel;

/**
 * viewmodel包下入口，展示view model与LiveData的结合
 */
public class ViewModelActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        ActivityViewmodelBinding activityViewmodelBinding = DataBindingUtil.setContentView(this, R.layout.activity_viewmodel);
        activityViewmodelBinding.setData(mainViewModel);
        activityViewmodelBinding.setLifecycleOwner(this);
        activityViewmodelBinding.btActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setUsers("Activity", "南京", 19);
            }
        });
    }
}
