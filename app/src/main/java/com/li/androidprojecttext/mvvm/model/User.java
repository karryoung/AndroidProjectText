package com.li.androidprojecttext.mvvm.model;

import java.io.Serializable;

/**
 * UserActivity的数据源
 */

public class User implements Serializable {
    private int id;
    private String name;

    public User() {

    }


    public User(int i, String username) {
        id = i;
        name = username;
    }

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
