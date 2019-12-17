package com.li.androidprojecttext.databing.viewmodel.fragment;

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

public class TwoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        Button button = view.findViewById(R.id.bt_two_fragment);
        final TextView textView = view.findViewById(R.id.tv_text_two_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.getUsers().postValue(new User("TwoFragment", "北京", 18));
            }
        });
        mainViewModel.getUsers().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                textView.setText(String.format(getString(R.string.formaStr),user.getName(), user.getCity(), user.getAge()));
            }
        });
        return view;
    }
}
