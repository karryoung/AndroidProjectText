package com.li.androidprojecttext.databing;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.li.androidprojecttext.BR;

/**
 * databing使用于DatabingTestActivity
 *
 * 在字段@Bindable必须要添加，否则BR中没有具体字段
 */
public class User extends BaseObservable {

    private String name;
    private String age;
    private String school;

    public User(String name, String age, String school) {
        this.name = name;
        this.age = age;
        this.school = school;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //名称更新的时候更新所有字段
        notifyChange();
    }

    @Bindable
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        //只更新本字段
        notifyPropertyChanged(BR.age);
        notifyPropertyChanged(BR.school);
    }

    @Bindable
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
