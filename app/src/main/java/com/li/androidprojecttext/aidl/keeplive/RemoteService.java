package com.li.androidprojecttext.aidl.keeplive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.li.androidprojecttext.IMyAidlInterface;

/**
 * @CreateDate: 2020/5/21 18:07
 * @Description: 用做服务端的service
 * @Author: 李想
 */
public class RemoteService extends Service {

    private static final String TAG = "RemoteService";
    private IMyAidlInterface iMyAidlInterface;
    private boolean mIsBound;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 创建 RemoteService");
        bindLocalService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void bindSuccess() throws RemoteException {
            Log.e(TAG, "bindSuccess: LocalService 绑定 RemoteService 成功");
        }

        @Override
        public void unbind() throws RemoteException {
            Log.e(TAG, "unbind: 此处解除 RemoteService 与 LocalService 的绑定");
            getApplicationContext().unbindService(connection);
        }
    };

    private void bindLocalService() {
        Intent intent = new Intent();
        intent.setAction("ClientLocalService");
        intent.setComponent(new ComponentName("com.li.androidprojecttext",
                "com.li.androidprojecttext.aidl.keeplive.ClientLocalService"));
        if (!getApplicationContext().bindService(intent
                , connection, Context.BIND_AUTO_CREATE)) {
            Log.e(TAG, "bindLocalService: 绑定 LocalService 失败");
            stopSelf();

        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: LocalService 链接成功");
            mIsBound = true;
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                iMyAidlInterface.bindSuccess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: LocalService 断开链接，重新启动");
            mIsBound = false;
            createTransferActivity();
        }
    };

    private void createTransferActivity() {
        Intent intent = new Intent(this, RemoteTransferActivity.class);
        intent.setAction(RemoteTransferActivity.ACTION_FROM_SELF);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 解除绑定 LocalService
     */
    private void unbindLocalService() {
        if (mIsBound) {
            try {
                iMyAidlInterface.unbind();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            getApplicationContext().unbindService(connection);
            stopSelf();
        }
    }
}
