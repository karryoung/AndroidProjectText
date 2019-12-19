package com.li.androidprojecttext.databing.viewmodel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.aspectjlibrary.DebugTrace;
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
        testApp();
        catchMethod();
        BackInt(4);
    }

    @DebugTrace
    private void catchMethod() {
        try {
            int sum = 100 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testApp() {
        int result = 0;
        for (int i = 1; i <= 100; i++) {
            result += i;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int BackInt(int a){
        return 7;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
