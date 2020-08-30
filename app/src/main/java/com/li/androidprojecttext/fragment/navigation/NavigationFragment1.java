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
import com.li.androidprojecttext.databinding.FragmentNavigation1Binding;


/**
 * @CreateDate: 2020/4/29 11:49
 * @Description: 类作用描述
 * @Author: 李想
 */
public class NavigationFragment1 extends Fragment {

    FragmentNavigation1Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation1, container, false);
        binding.fragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("et", binding.et.getText().toString());
                Navigation.findNavController(binding.getRoot()).navigate(R.id.actionfragment1, bundle);
            }
        });
        return binding.getRoot();
    }
}
