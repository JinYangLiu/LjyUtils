package com.ljy.lib;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjySystemUtil {

    /**
     * 判断当前栈顶的activity
     *
     * 使用时应注意className应该为activity的name，fragment等是不行的
     *
     * 需要权限：android.permission.GET_TASKS
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        context=context.getApplicationContext();
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            String currentClassName = cpn.getClassName().substring(cpn.getClassName().lastIndexOf(".") + 1);
            if (className.equals(currentClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 获取dpi
     */
    public static float getDPI(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }
}
