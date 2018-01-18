package com.ljy.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/25.
 * <p>
 * 提供系统相关方法
 */

public class LjySystemUtil {

    /**
     * 判断当前栈顶的activity
     * <p>
     * 使用时应注意className应该为activity的name，fragment等是不行的
     * <p>
     * 需要权限：android.permission.GET_TASKS
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        context = context.getApplicationContext();
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

    /**
     * 判断当前手机api系统版本是否>=指定版本
     *
     * @param versionCode
     * @return
     */
    public static boolean checkSdkVersion(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }

    /**
     * 判断当前应用是否有指定权限，运行时权限的检测
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean hasPermission(Activity activity, String permission) {
        boolean ifSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        boolean ifPer = ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
        if (ifSdk && ifPer) {
            return false;
        }
        return true;

    }

    /**
     * 动态申请指定权限，配合hasPermission使用,注意在使用的activity中调用onRequestPermissionsResult权限申请结果的回调
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermission(final Activity activity, final String[] permissions, final int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 申请权限的结果回调，需要在Activity的onRequestPermissionsResult中调用
     * @param grantResults
     * @param permissionResult
     */
    public static void onPermissionResult( @NonNull int[] grantResults, PermissionResult permissionResult) {
        boolean hasPermission = true;
        List<Integer> disAllowIndexs=null;
        for (int i = 0; i < grantResults.length; i++) {
            boolean isAllow = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            hasPermission &= isAllow;
            if (!isAllow){
                if (disAllowIndexs==null)
                    disAllowIndexs=new ArrayList<>();
                disAllowIndexs.add(i);
            }
        }
        if (hasPermission) {
            permissionResult.success();
        } else {
            permissionResult.fail(disAllowIndexs);
        }

    }

    /**
     * 沉浸式状态栏
     * @param activity
     */
    public static void noStatusBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }


    /**
     * 权限申请结果的回调接口
     */
    public interface PermissionResult {
        void success();
        //disAllowIndexs没有被允许的权限角标
        void fail(List<Integer> disAllowIndexs);
    }

    /**
     * 判断服务是否启动了
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceWorked(Context context, Class className) {
        ActivityManager myManager = (ActivityManager) context.getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className.getName())) {
                return true;
            }
        }
        return false;
    }
}
