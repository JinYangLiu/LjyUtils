package com.ljy.ljyutils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ljy.util.LjyLogUtil;

/**
 * Created by Mr.LJY on 2018/1/16.
 */

public class MyBroadcastReceiver5 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LjyLogUtil.i("action:"+intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            LjyLogUtil.i("屏幕关闭了哦");
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            LjyLogUtil.i("屏幕打开了哦");
        }
    }
}
