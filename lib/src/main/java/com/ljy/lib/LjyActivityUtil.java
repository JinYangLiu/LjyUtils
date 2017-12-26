package com.ljy.lib;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjyActivityUtil {

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
}
