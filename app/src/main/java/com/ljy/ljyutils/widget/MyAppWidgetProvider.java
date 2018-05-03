package com.ljy.ljyutils.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyBitmapUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LJY on 2018/5/3 15:35
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.ljy.ljyutils.action.CLICK";
    private int[] imgRes={R.drawable.girl,R.mipmap.mountain,R.drawable.music_bg,R.drawable.cat};
    private static int count=0;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        LjyLogUtil.i("MyAppWidgetProvider.onReceive:action=" + intent.getAction());
        //判断action
        if (intent.getAction().equals(CLICK_ACTION)) {
            LjyToastUtil.toast(context, "clicked Widget");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            PendingIntent pendingIntent = getPendingIntent(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_1);
            remoteViews.setImageViewResource(R.id.iv_widget, imgRes[(count++)%imgRes.length]);
            remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);
            appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
        }
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        intentClick.setComponent(new ComponentName(context, MyAppWidgetProvider.class));//8.0以上版本必须写
        return PendingIntent.getBroadcast(context, 666, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 每次桌面小部件更新时调用
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        LjyLogUtil.i("MyAppWidgetProvider.onUpdate");
        PendingIntent pendingIntent = getPendingIntent(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_1);
        remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);
        final int counter = appWidgetIds.length;
        LjyLogUtil.i("counter=" + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

}
