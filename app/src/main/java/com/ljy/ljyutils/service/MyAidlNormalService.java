package com.ljy.ljyutils.service;

import android.content.Intent;
import android.os.IBinder;

import com.ljy.ljyutils.stub.MyAidlInterfaceImpl;

/**
 * Created by Mr.LJY on 2018/1/16.
 * <p>
 * 远程服务：
 * 1. 与本地服务最大的区别是：远程Service与调用者不在同一个进程里（即远程Service是运行在另外一个进程）；而本地服务则是与调用者运行在同一个进程里
 */

public class MyAidlNormalService extends MyNormalService {

    MyAidlInterfaceImpl mStub = new MyAidlInterfaceImpl();

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }
}
