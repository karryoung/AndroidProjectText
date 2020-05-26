package com.facerecognition.dualdemoVL;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 *
 * @Description SP操作类
 */
public class UserDB {

    public static final String PREFERENCE_NAME = "AutnenMetric.db";

    private SharedPreferences mPreference;


    public UserDB(Context ctx) {
        mPreference = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 存储用户数据
     *
     * @param userId
     * @param feature
     * @return
     */
    public boolean putFeature(String userId, String feature) {
        return mPreference.edit().putString(userId, feature).commit();
    }

    /**
     * 获取用户数据
     *
     * @return
     */
    public Map<String, ?> getAllFeature() {
        return mPreference.getAll();
    }
}
