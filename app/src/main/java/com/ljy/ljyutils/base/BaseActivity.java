package com.ljy.ljyutils.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ljy.ljyutils.bean.DaoMaster;
import com.ljy.ljyutils.bean.DaoSession;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
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
    private MyApplication mApplication;
    private BaseActivity currentAct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //Umeng push要求，此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
        PushAgent.getInstance(mContext).onAppStart();

        if (mApplication == null) {
            // 得到Application对象
            mApplication = (MyApplication) getApplication();
        }
        currentAct = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
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

    // 添加Activity方法
    public void addActivity() {
        if (mApplication != null && currentAct != null)
            mApplication.addActivity_(currentAct);// 调用myApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        if (mApplication != null && currentAct != null)
            mApplication.removeActivity_(currentAct);// 调用myApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        if (mApplication != null)
            mApplication.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    //销毁所有MainActivity之外的Activity方法
    public void removeALLActivityExceptMainTab() {
        if (mApplication != null)
            mApplication.removeALLActivityExceptMain_();// 调用myApplication的销毁所有Activity方法
    }

    private int permissionRequestCode=0;

    public boolean checkPremission(String permission ,int requestCode){
        if (PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(this,permission)){
            return true;
        }else{
            permissionRequestCode=requestCode;
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LjyLogUtil.i("=== onRequestPermissionsResult ===");
        if (grantResults.length>0){
            LjyPermissionUtil.injectActivity(this,grantResults[0]== PackageManager.PERMISSION_GRANTED,permissionRequestCode);
        }
    }
}
