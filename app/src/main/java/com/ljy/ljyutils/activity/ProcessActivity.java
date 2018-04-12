package com.ljy.ljyutils.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.ProcessBean;
import com.ljy.ljyutils.bean.SeriBean;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
import com.ljy.util.LjyToastUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 多进程, 跨进程通信, 序列化
 * <p>
 * Serializable: java提供,空接口,开销大,大量的IO操作
 * Parcelable: android提供,通过intent,binder传递
 */

public class ProcessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        LjyLogUtil.i("ProcessBean.count=" + ProcessBean.count);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (checkPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101))
                    methodOut();
                break;
            case R.id.btn2:
                if (checkPremission(Manifest.permission.READ_EXTERNAL_STORAGE, 102))
                    methodIn();
                break;
        }
    }

    @LjyPermissionUtil.GetPermission(permissionResult = true, requestCode = 102)
    private void methodIn() {
        //反序列化
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(LjyFileUtil.getSDCardPath() + "cache.txt"));
            SeriBean seriBeanNew = (SeriBean) in.readObject();
            LjyLogUtil.i("seriBeanNew =" + seriBeanNew.toString());
            LjyToastUtil.toast(mContext, "seriBeanNew =" + seriBeanNew.toString());
//            ParceBean parceBeanNew = (ParceBean) in.readObject();
//            LjyLogUtil.i("parceBeanNew =" + parceBeanNew.toString());
//            LjyToastUtil.toast(mContext, "parceBeanNew =" + parceBeanNew.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @LjyPermissionUtil.GetPermission(permissionResult = true, requestCode = 101)
    private void methodOut() {
        //创建
        SeriBean seriBean = new SeriBean("小明");
        LjyLogUtil.i("seriBean =" + seriBean.toString());
        LjyToastUtil.toast(mContext, "seriBean =" + seriBean.toString());
//        Parce2Bean parce2Bean = new Parce2Bean("我是info啊");
//        ParceBean parceBean = new ParceBean("刘备", parce2Bean);
//        LjyLogUtil.i("parceBean =" + parceBean.toString());
//        LjyToastUtil.toast(mContext, "parceBean =" + parceBean.toString());
        //序列化
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(LjyFileUtil.getSDCardPath() + "cache.txt"));
            out.writeObject(seriBean);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
