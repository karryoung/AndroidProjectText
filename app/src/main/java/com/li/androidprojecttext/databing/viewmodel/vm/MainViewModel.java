package com.li.androidprojecttext.databing.viewmodel.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.li.androidprojecttext.databing.viewmodel.model.User;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<User> users;
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<User> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
            //这个时候的数据源可以从网络获取
        }
        return users;
    }

    //setValue():必须在主线程中才能使用  postValue():既可在主线程也可在子线程中调用 即在工作线程中调用
    public void setUsers(String name, String city, int age){
        users.setValue(new User(name, city, age));
    }

    public void setUsers(MutableLiveData<User> users) {
        this.users = users;
    }
}
