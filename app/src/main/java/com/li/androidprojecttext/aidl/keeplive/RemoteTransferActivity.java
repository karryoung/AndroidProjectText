package com.li.androidprojecttext.aidl.keeplive;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @CreateDate: 2020/5/22 14:20
 * @Description: 类作用描述
 * @Author: 李想
 */
public class RemoteTransferActivity extends Activity {


    private static final String TAG = "RemoteTransferActivity";
    public static final String ACTION_FROM_SELF = "RemoteTransferActivity.FROM_SELF";
    public static final String ACTION_FROM_OTHER = "RemoteTransferActivity.FROM_OTHER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "oncreate: 创建中转Activity");
        Window window = getWindow();
        window.setGravity(Gravity.START|Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = attributes.height = 1;
        attributes.x = attributes.y = 0;
        if (getIntent() != null) {
            Intent intent;
            if (ACTION_FROM_OTHER.equals(getIntent().getAction())) {
                intent = new Intent(this, RemoteService.class);
                startService(intent);
            } else if (ACTION_FROM_SELF.equals(getIntent().getAction())){
                intent = new Intent("ClientTransferActivity.FROM_OTHER");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.li.androidprojecttext.aidl.keeplive",
                        "com.li.androidprojecttext.aidl.keeplive.ClientTransferActivity"));
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: 销毁中转 Activity");
    }

    /**
     *判断Service是否在运行
     */
    private boolean isServiceRunning(Context context, String serviceName) throws ClassNotFoundException {

        if (("").equals(serviceName) || serviceName == null) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
