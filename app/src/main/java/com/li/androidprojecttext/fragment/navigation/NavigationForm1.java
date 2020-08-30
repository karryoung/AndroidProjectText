package com.li.androidprojecttext.fragment.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.NavigationFrom1Binding;

/**
 * @CreateDate: 2020/6/16 10:05
 * @Description: 类作用描述
 * @Author: 李想
 */
class NavigationForm1 extends Fragment {

    private NavigationFrom1Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_from1, container, false);
        binding.form1.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.form1_to_form2);
        });
        return binding.getRoot();
    }
}
