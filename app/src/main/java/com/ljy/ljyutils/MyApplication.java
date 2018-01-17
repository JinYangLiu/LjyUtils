package com.ljy.ljyutils;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.ljy.ljyutils.bean.DaoMaster;
import com.ljy.ljyutils.bean.DaoSession;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;
import com.ljy.util.LjySPUtil;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class MyApplication extends Application {
    private static DaoSession daoSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LjyLogUtil.setIsLog(true);
        LjyRetrofitUtil.setBaseUrl("http://pub2.bbs.anxin.com");
        LjyRetrofitUtil.setTimeOut(30,60,60);
        //初始化spUtil
        LjySPUtil.getInstance().init(this);
        //配置数据库
        setupDatabase();
    }

    private void setupDatabase() {
        //创建数据库
        DaoMaster.DevOpenHelper dbHelper=new DaoMaster.DevOpenHelper(this,"phone.db");
        //获取可写数据库
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster=new DaoMaster(db);
        //获取dao对象管理者
        daoSession=daoMaster.newSession();

    }

    public static DaoSession getDaoInstance(){
        return daoSession;
    }
}
