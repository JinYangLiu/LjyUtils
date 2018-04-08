package com.ljy.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

public class LjyPermissionUtil {
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
     *
     * @param grantResults
     * @param permissionResult
     */
    public static void onPermissionResult(@NonNull int[] grantResults, PermissionResult permissionResult) {
        boolean hasPermission = true;
        List<Integer> disAllowIndexs = null;
        for (int i = 0; i < grantResults.length; i++) {
            boolean isAllow = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            hasPermission &= isAllow;
            if (!isAllow) {
                if (disAllowIndexs == null)
                    disAllowIndexs = new ArrayList<>();
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
     * 权限申请结果的回调接口
     */
    public interface PermissionResult {
        void success();

        //disAllowIndexs没有被允许的权限角标
        void fail(List<Integer> disAllowIndexs);
    }
}
