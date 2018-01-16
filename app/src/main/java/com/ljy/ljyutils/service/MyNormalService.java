package com.ljy.ljyutils.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.ljy.util.LjyLogUtil;

/**
 * Created by Mr.LJY on 2018/1/16.
 *
 * 普通的后台service
 */

public class MyNormalService extends Service {

    private MyBinder mBinder=new MyBinder();

    //手动调用
    @Override
    public ComponentName startService(Intent service) {
        LjyLogUtil.i("startService");
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        LjyLogUtil.i("stopService");
        return super.stopService(name);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        LjyLogUtil.i("bindService");
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        LjyLogUtil.i("unbindService");
    }

    //自动调用
    @Override
    public void onCreate() {
        super.onCreate();
        LjyLogUtil.i("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LjyLogUtil.i("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LjyLogUtil.i("onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        LjyLogUtil.i("onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LjyLogUtil.i("onUnbind");
        return super.onUnbind(intent);
    }

    //Binder
    public class MyBinder extends Binder{
        public void doSomething(){
            LjyLogUtil.i("doSomething....");
        }
    }
}
