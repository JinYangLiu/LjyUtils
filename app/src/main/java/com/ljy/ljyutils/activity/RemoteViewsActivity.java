package com.ljy.ljyutils.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.base.MainActivity;
import com.ljy.util.LjyColorUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjySystemUtil;

import java.util.Random;

/**
 *  A. RemoteViews:
 * 顾名思义,远程View,在其他进程中显示,可跨进程更新界面
 * 使用场景:通知栏和桌面小部件(实现:NotificationManager和AppWidgetProvider)
 * <p>
 * B. PendingIntent:
 * 1. 与Intent的区别是在将来的某个不确定的时刻发生
 * 2. 使用场景: 给RemoteViews添加单击事件
 * 3. 主要方法: getActivity,getService,getBroadcast
 * 4. 参数:
 * (1) requestCode:发送方的请求码,多数情况设为0即可,会影响到flags的效果
 * (2) flags:
 * (匹配规则:内部Intent相同,requestCode也相同那么两个PendingIntent就是相同的)
 * (Intent的ComponentName和intent-filter都相同,那么Intent就是相同的)
 * a. PendingIntent.FLAG_ONE_SHOT
 * 当前PendingIntent只被使用一次,之后会自动cancel,如果后续还有相同的PendingIntent,那么send方法会调用失败
 * id不同,PendingIntent相同时,多个通知会和第一个通知保持一致包括Extras,单击任何一条后,剩下的无法再打开,直到清除所有通知
 * b. PendingIntent.FLAG_NO_CREATE
 * 当前PendingIntent不会主动创建,调用上面的三个方法会直接返回null,无法单独使用,日常开发很少用到
 * c. PendingIntent.FLAG_CANCEL_CURRENT
 * 当前PendingIntent如果已经存在,就会被cancel,然后系统会创建一个新的
 * id不同,PendingIntent相同时,多个通知只有最新的可以打开
 * d. PendingIntent.FLAG_UPDATE_CURRENT
 * 当前PendingIntent如果已存在,就会被更新,即他们的Intent中的Extras会被替换成最新的
 * id不同,PendingIntent相同时,多个通知,之前的通知的PendingIntent会被更新,和最新的保持一致包括Extras,并且都是可以打开的
 */
public class RemoteViewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_views);

    }

    Intent intent = new Intent(mContext, CustomViewActivity.class);
    int num=1;
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification_1:
                showNotification(PendingIntent.FLAG_ONE_SHOT);
                break;
            case R.id.btn_notification_2:
                showNotification(PendingIntent.FLAG_CANCEL_CURRENT);
                break;
            case R.id.btn_notification_3:
                showNotification(PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case R.id.btn_notification2:
                num=1;
                Intent intent2 = new Intent(mContext, DesignPatternActivity.class);
                showNotification(2, R.drawable.ic_music, "人民日报", "hello Android", intent2, 2,PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case R.id.btn_notification3:
                Intent intent3 = new Intent(mContext, BallActivity.class);
                showRemoteNotification(3, R.drawable.ic_notification, "东京日报", "open", intent3, 1);
                break;
        }
    }
    private void showNotification(int flag){
        showNotification(1, R.drawable.ic_notification, "王者日报_"+num++, "hello world", intent, 1,flag);
    }
    private void showRemoteNotification(int id, int icon, String title, String content, Intent intent, int requestCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(id, manager);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_msg, title);
        remoteViews.setTextViewText(R.id.tv_open, content);
        remoteViews.setImageViewResource(R.id.iv_icon, R.mipmap.ic_launcher);
        PendingIntent openIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_open, openIntent);
        Notification notification = new NotificationCompat.Builder(mContext, "" + id)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .build();
        if (manager != null) {
            //id不同才会显示多个通知
            manager.notify(id, notification);
        }
    }


    private void showNotification(int id, int icon, String title, String content, Intent intent, int requestCode,int flag) {
        PendingIntent pendingIntent = intent == null ? null : PendingIntent.getActivity(mContext, requestCode, intent, flag);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(id, manager);
        Notification notification = new NotificationCompat.Builder(mContext, "" + id)
                .setSmallIcon(icon)
                .setTicker("hello!!!")
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())//通知所显示的时间
                .setAutoCancel(true)//将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setContentIntent(pendingIntent)
                .build();
        if (manager != null) {
            manager.notify(num, notification);//notifcation要显示多条，那么NotificationManager.notify( id, notify);  中的要保持不一样
        }
    }

    private void setNotificationChannel(int id, NotificationManager manager) {
        //解决channel为null的报错
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "channel_" + id;
            NotificationChannel channel = new NotificationChannel("" + id, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(LjyColorUtil.getInstance().randomColor());
            channel.setShowBadge(true);//是否在久按桌面图标时显示此渠道的通知
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
