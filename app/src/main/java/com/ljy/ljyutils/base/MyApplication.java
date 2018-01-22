package com.ljy.ljyutils.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class MyApplication extends Application {
    private static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();
        LjyLogUtil.setIsLog(true);
        LjyRetrofitUtil.setBaseUrl("http://pub2.bbs.anxin.com");
        LjyRetrofitUtil.setTimeOut(30,60,60);

    }

    public static Context getMyApplicationContext(){
        return applicationContext;
    }

}
