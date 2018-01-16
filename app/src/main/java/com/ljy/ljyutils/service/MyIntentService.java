package com.ljy.ljyutils.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.ljy.util.LjyLogUtil;

/**
 * Created by Mr.LJY on 2018/1/16.
 * IntentService:
 * 1. 作用：处理异步请求 & 实现多线程
 * 2.使用场景：线程任务 需按顺序、在后台执行
 * 最常见的场景：离线下载
 * 不符合多个数据同时请求的场景：所有的任务都在同一个Thread looper里执行
 */

public class MyIntentService extends IntentService {

    /**
     * 在构造函数中传入线程名字
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * 复写onHandleIntent()方法
     * 根据 Intent实现 耗时任务 操作
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 根据 Intent的不同，进行不同的事务处理
        if (intent == null)
            return;
        //模拟耗时操作
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //模拟任务处理
        LjyLogUtil.i("currentThread.name:" + Thread.currentThread().getName());
        String taskName = intent.getExtras().getString("taskName");
        String info="do " + taskName+" success";
        LjyLogUtil.i(info);

        //t通过广播通知UI线程，可以用来更新UI
        Intent intent2 = new Intent("123321");
        intent2.putExtra("info", info);
        sendBroadcast(intent2);

        //实际使用时可以根据不同的任务做不同的处理
        switch (taskName) {
            //case ...
            default:
                break;
        }
    }

    @Override
    public void onCreate() {
        LjyLogUtil.i("onCreate");
        super.onCreate();
    }

    /**
     * 复写onStartCommand()方法
     * 默认实现 = 将请求的Intent添加到工作队列里
     **/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LjyLogUtil.i("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LjyLogUtil.i("onDestroy");
        super.onDestroy();
    }

}
