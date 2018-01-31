package com.ljy.ljyutils.base;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.ljy.ljyutils.bean.DaoMaster;
import com.ljy.ljyutils.bean.DaoSession;
import com.ljy.util.LjySPUtil;
import com.ljy.view.swipeBack.LjySwipeBackActivity;
import com.umeng.message.PushAgent;


/**
 * Created by Mr.LJY on 2018/1/22.
 */

public class BaseActivity extends LjySwipeBackActivity {
    public Context mContext = MyApplication.getMyApplicationContext();
    public Activity mActivity = this;
    private DaoSession daoSession;
    private LjySPUtil spUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //Umeng push要求，此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
        PushAgent.getInstance(mContext).onAppStart();
    }

    public DaoSession getDaoInstance() {
        if (daoSession == null) {
            //创建数据库
            DaoMaster.DevOpenHelper dbHelper = new DaoMaster.DevOpenHelper(this, "phone.db");
            //获取可写数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //获取数据库对象
            DaoMaster daoMaster = new DaoMaster(db);
            //获取dao对象管理者
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public LjySPUtil getSpUtilInstance() {
        if (spUtil == null)
            spUtil = new LjySPUtil(mContext);
        return spUtil;
    }
}
