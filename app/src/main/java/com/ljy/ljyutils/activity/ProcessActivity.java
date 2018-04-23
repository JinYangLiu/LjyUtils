package com.ljy.ljyutils.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.Book;
import com.ljy.ljyutils.bean.ProcessBean;
import com.ljy.ljyutils.bean.SeriBean;
import com.ljy.ljyutils.provider.BookProvider;
import com.ljy.ljyutils.service.MessengerService;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
import com.ljy.util.LjyToastUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 多进程, 跨进程通信, 序列化
 * <p>
 * Serializable: java提供,空接口,开销大,大量的IO操作
 * Parcelable: android提供,通过intent,binder传递
 */

public class ProcessActivity extends BaseActivity {

    @BindView(R.id.text_info)
    TextView mTextViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ButterKnife.bind(mActivity);
        LjyLogUtil.i("ProcessBean.count=" + ProcessBean.count);
    }

    //Messenger
    private Messenger mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MessengerService.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "Hello Messenger ~~~");
            msg.setData(data);
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onBtnClick(View view) {
        mTextViewInfo.append("----------" + ((Button) view).getText() + "----------\n");
        LjyLogUtil.setAppendLogMsg(true);
        switch (view.getId()) {
            case R.id.btn1:
                if (checkPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101))
                    methodOut();
                break;
            case R.id.btn2:
                if (checkPremission(Manifest.permission.READ_EXTERNAL_STORAGE, 102))
                    methodIn();
                break;
            case R.id.btn3:
                Intent intent = new Intent(this, MessengerService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn4:
                Uri bookUri = BookProvider.BOOK_CONTENT_URI;
                ContentValues values = new ContentValues();
                values.put("_id", 6);
                values.put("name", "红楼梦");
                getContentResolver().insert(bookUri, values);
                Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
                while (bookCursor.moveToNext()) {
                    Book book = new Book();
                    LjyLogUtil.i("id:" + bookCursor.getInt(0) + ",name:" + bookCursor.getString(1));
                    book.name = bookCursor.getString(1);
                    LjyLogUtil.i(book.toString());
                }
                bookCursor.close();

                Uri userUri = BookProvider.USER_CONTENT_URI;
                Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "age"}, null, null, null);
                while (userCursor.moveToNext()) {
                    LjyLogUtil.i("user--> id:" + userCursor.getInt(0) + ",name:" + userCursor.getString(1) + ",age:" + userCursor.getInt(2));
                }
                userCursor.close();

                break;

        }
        mTextViewInfo.append(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
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
