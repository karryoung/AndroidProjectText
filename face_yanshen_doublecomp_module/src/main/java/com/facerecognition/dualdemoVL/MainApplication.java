package com.facerecognition.dualdemoVL;

import android.annotation.SuppressLint;
import android.app.Application;

@SuppressLint("NewApi")
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }
}
