package com.ljy.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.ljy.view.LjyMDDialogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

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

    /**
     * 8.0以上允许未知来源权限(更新app时使用到)
     */
    public static void checkRequestPermision(final Activity activity, PermissionResult result) {
        //8.0以上允许未知来源权限
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                //有权限直接更新
                result.success();
            } else {
                //没权限请求权限
                new LjyMDDialogManager(activity).alertTwoButton("申请权限", "安装应用需要打开未知来源权限，请去设置中开启权限",
                        "好的", new LjyMDDialogManager.OnPositiveListener() {
                            @Override
                            public void positive() {
                                //没权限-申请权限
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                                        111);

                            }
                        }, "", null, true);
            }
        } else {
            //有权限直接更新
            result.success();
        }
    }

    /**
     * 判断服务是否启动了
     *
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

    public static String getStringFromAssets(Context context, String fileName) {
        try {
            InputStream input = context.getResources().getAssets().open(fileName);
            return getString(input);
        } catch (IOException e) {
            return "";
        }
    }


    public static String getStringFromRaw(Context context, int resId) {

        InputStream input = context.getResources().openRawResource(resId);
        return getString(input);
    }

    @NonNull
    private static String getString(InputStream input) {
        try {
            InputStreamReader inputReader = new InputStreamReader(input, "utf-8");

            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 沉浸式状态栏
     *
     * @param activity
     */
    public static void noStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//底部虚拟按键
            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);//底部虚拟按键
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    /**
     * 创建某个界面的桌面快捷入口
     * 注意：manifest中需要添加权限，
     * 对应的activity中要
     * <intent-filter>
     * <action android:name="android.intent.action.MAIN"/>
     * </intent-filter>
     *
     * @param context
     * @param activityName
     * @param name
     * @param icon
     */
    public static void addShortcut(Context context, String activityName, String name, int icon) {
        //   创建快捷方式的intent广播
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //  添加快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //  快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        //  我们下次启动要用的Intent信息
        Intent carryIntent = new Intent(Intent.ACTION_MAIN);
        carryIntent.putExtra("name", name);
        carryIntent.setClassName(context, activityName);
        carryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //添加携带的Intent
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, carryIntent);
        //   发送广播
        context.sendBroadcast(shortcut);
    }

    /**
     * 获取app 的 appName
     */
    public static String getAppName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        return packInfo==null?"":packInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 获取app 的 versionName
     */
    public static String getVersionName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        return packInfo==null?"":packInfo.versionName;
    }

    /**
     * 获取app 的 versionName
     */
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        return packInfo==null?-1:packInfo.versionCode;
    }

    /**
     * 获取app 的 PackageInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        context=context.getApplicationContext();
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 复制文字到剪切板
     * @param context
     * @param info
     */
    public static void copyToClipboard(Context context,String info){
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("tag", info));

        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            c.setText(info);
        }
    }

}
