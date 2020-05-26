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
 * @CreateDate: 2020/5/21 17:14
 * @Description: 用做客户端的服务
 * @Author: 李想
 */
public class ClientLocalService extends Service {

    private static final String TAG = "ClientLocalService";
    private IMyAidlInterface aidlInterface;
    private boolean mIsBound;

    @Override
    public void onCreate() {
        super.onCreate();
        bindRemoteService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand:");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: 绑定 ClientLocalService");
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: 解绑 ClientLocalService");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: 销毁 ClientLocalService");
        super.onDestroy();

    }

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void bindSuccess() throws RemoteException {
            Log.e(TAG, "bindSuccess: RemoteService 绑定 ClientLocalService 成功");
        }

        @Override
        public void unbind() throws RemoteException {
            getApplicationContext().unbindService(connection);
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: RemoteService 链接成功");
            mIsBound = true;
            aidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                aidlInterface.bindSuccess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: RemoteService 断开连接，重新启动");
            mIsBound = false;
            createTransferActivity();
        }
    };

    private void createTransferActivity() {
        Intent intent = new Intent(this, ClientTransferActivity.class);
        intent.setAction(ClientTransferActivity.ACTION_FROM_SELF);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void bindRemoteService(){
        Intent intent = new Intent();
        intent.setAction("RemoteService");
        intent.setComponent(new ComponentName("com.li.androidprojecttext",
                "com.li.androidprojecttext.aidl.keeplive.RemoteService"));
        if (!getApplicationContext().bindService(intent, connection,
                Context.BIND_AUTO_CREATE)) {
            Log.e(TAG, "bindRemoteService: 绑定 RemoteService 失败");
            stopSelf();
        }
    }

    /**
     * 解除绑定 RemoteService
     */
    private void unbindRemoteService() {
        if (mIsBound) {
            try {
                // 先让 RemoteService 解除绑定 ClientLocalService
                aidlInterface.unbind();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // 解除 ClientLocalService 与 RemoteService
            getApplicationContext().unbindService(connection);
            stopSelf();
        }
    }
}
