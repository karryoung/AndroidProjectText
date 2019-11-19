package com.li.androidprojecttext.mvvm.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.li.androidprojecttext.mvvm.model.User;

/**
 * Created by okkuaixiu on 2018/5/17.
 */

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;

    public LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void setUsernam(String username) {
        user.setValue(new User(1, username));
    }
}
