package com.li.androidprojecttext.mvvm.bean;

import java.io.Serializable;

/**
 * Created by okkuaixiu on 2018/5/17.
 */

public class User implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
