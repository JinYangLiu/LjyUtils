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

/**
 * RemoteViews:
 * 顾名思义,远程View,在其他进程中显示,可跨进程更新界面
 * 使用场景:通知栏和桌面小部件(实现:NotificationManager和AppWidgetProvider)
 */
public class RemoteViewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_views);

    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification1:
                Intent intent = new Intent(mContext, CustomViewActivity.class);
                showNotification(1, R.drawable.ic_notification, "王者日报", "hello world", intent,1);
                break;
            case R.id.btn_notification2:
                Intent intent2 = new Intent(mContext, DesignPatternActivity.class);
                showNotification(2, R.drawable.ic_music, "人民日报", "hello Android", intent2,2);
                break;
            case R.id.btn_notification3:
                Intent intent3 = new Intent(mContext, BallActivity.class);
                showRemoteNotification(3, R.drawable.ic_notification, "东京日报", "open", intent3,1);
                break;
        }
    }

    private void showRemoteNotification(int id, int icon, String title, String content, Intent intent,int requestCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(id, manager);
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_msg,title);
        remoteViews.setTextViewText(R.id.tv_open,content);
        remoteViews.setImageViewResource(R.id.iv_icon,R.mipmap.ic_launcher);
        PendingIntent openIntent=PendingIntent.getActivity(mContext,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_open,openIntent);
        Notification notification = new NotificationCompat.Builder(mContext, ""+id)
               .setSmallIcon(icon)
                .setAutoCancel(true)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .build();
        if (manager != null) {
            manager.notify(id, notification);
        }
    }

    private int num=0;
    private void showNotification(int id, int icon, String title, String content, Intent intent,int requestCode) {
        PendingIntent pendingIntent = intent==null?null:PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(id, manager);
        Notification notification = new NotificationCompat.Builder(mContext, ""+id)
                .setSmallIcon(icon)
                .setTicker("hello!!!")
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis() )//通知所显示的时间
                .setAutoCancel(true)//将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setContentIntent(pendingIntent)
                .setNumber(++num)
                .build();
        if (manager != null) {
            manager.notify(id, notification);//notifcation要显示多条，那么NotificationManager.notify( id, notify);  中的要保持不一样
        }
    }

    private void setNotificationChannel(int id, NotificationManager manager) {
        //解决channel为null的报错
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "channel_" + id;
            NotificationChannel channel = new NotificationChannel(""+id, channelName,
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
