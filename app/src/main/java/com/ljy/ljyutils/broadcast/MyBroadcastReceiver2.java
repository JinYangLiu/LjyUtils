package com.ljy.ljyutils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ljy.util.LjyLogUtil;

/**
 * Created by Mr.LJY on 2018/1/16.
 */

public class MyBroadcastReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String info = intent.getExtras().getString("info");
        LjyLogUtil.i("MyBroadcastReceiver___2:--->接收到info:--->"+info);
    }
}
