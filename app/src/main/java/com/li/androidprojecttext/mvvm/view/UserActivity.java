package com.li.androidprojecttext.mvvm.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.mvvm.model.User;
import com.li.androidprojecttext.mvvm.viewmodel.UserViewModel;

/**
 * Created by okkuaixiu on 2018/5/17.
 */

public class UserActivity extends AppCompatActivity {
    private TextView tvId;
    private TextView tvName;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initData();
    }

    private void initView() {
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
    }

    private void initData() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                updateView(user);
            }
        });
        userViewModel.setUsernam("dkskk");
    }

    private void updateView(User user){
        tvId.setText(user.getId() + "");
        tvName.setText(user.getName());
    }
}
