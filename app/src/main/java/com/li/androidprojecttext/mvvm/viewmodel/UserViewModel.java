package com.li.androidprojecttext.mvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
