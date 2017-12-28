package com.ljy.ljyutils;

import android.app.Application;

import com.ljy.util.LjyLogUtil;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LjyLogUtil.setIsLog(true);
    }
}
