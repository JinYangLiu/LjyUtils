package com.ljy.ljyutils.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.activity.ServiceActivity;

/**
 * Created by Mr.LJY on 2018/1/16.
 *
 * 前台Service：
 * 1.和后台Service（普通）最大的区别就在于：前台Service在下拉通知栏有显示通知（如下图），但后台Service没有
 * 2.前台Service优先级较高，不会由于系统内存不足而被回收；后台Service优先级较低，当系统出现内存不足情况时，很有可能会被回收
 */

public class MyForegroundNormalService extends MyNormalService {

    @Override
    public void onCreate() {
        super.onCreate();
        //添加下列代码将后台Service变成前台Service
        //构建"点击通知后打开MainActivity"的Intent对象
        Intent notificationIntent = new Intent(this, ServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //新建Builer对象
        Notification notification = new Notification.Builder(this)
                .setContentTitle("前台服务通知的标题")
                .setContentText("前台服务通知的内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);//让Service变成前台Service,并在系统的状态栏显示出来
    }
}
