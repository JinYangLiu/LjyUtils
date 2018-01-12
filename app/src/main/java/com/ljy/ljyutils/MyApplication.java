package com.ljy.ljyutils;

import android.app.Application;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LjyLogUtil.setIsLog(true);
        LjyRetrofitUtil.setBaseUrl("http://pub2.bbs.anxin.com");
        LjyRetrofitUtil.setTimeOut(30,60,60);
    }
}
