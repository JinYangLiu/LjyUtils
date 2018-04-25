package com.ljy.ljyutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ljy.ljyutils.stub.BinderPool;

/**
 * Created by LJY on 2018/4/25.
 */

public class BinderPoolService extends Service {

    private Binder mBinderPool=new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
