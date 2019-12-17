package com.li.androidprojecttext.databing.viewmodel.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databing.viewmodel.model.User;
import com.li.androidprojecttext.databing.viewmodel.vm.MainViewModel;

public class OneFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        TextView textView = view.findViewById(R.id.tv_text_one_fragment);
        Button bt_one_fragment = view.findViewById(R.id.bt_one_fragment);
        bt_one_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.getUsers().postValue(new User("OneFragment", "上海", 29));
            }
        });

        mainViewModel.getUsers().observe(this, new Observer<User>() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onChanged(User user) {
                textView.setText(String.format(getString(R.string.formaStr),
                        user.getName(), user.getCity(), user.getAge()));
            }
        });
        return view;
    }
}
