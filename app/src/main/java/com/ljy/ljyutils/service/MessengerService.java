package com.ljy.ljyutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.ljy.util.LjyLogUtil;

/**
 * Created by LJY on 2018/4/13.
 */

public class MessengerService extends Service {
    public static final int MSG_FROM_CLIENT = 101;

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            LjyLogUtil.i("MessengerHandler.handleMessage");
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    LjyLogUtil.i("MessengerService接收到客户端的消息: " + msg.getData().get("msg"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private MessengerHandler mHandler = new MessengerHandler();
    private final Messenger mMessenger = new Messenger(mHandler);

    @Override
    public void onCreate() {
        super.onCreate();
        LjyLogUtil.i("MessengerService.onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LjyLogUtil.i("MessengerService.onBind");
        return mMessenger.getBinder();
    }
}
