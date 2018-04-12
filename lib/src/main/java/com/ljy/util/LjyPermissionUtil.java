package com.ljy.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    //-----------使用注解实现权限管理,具体实现写在baseActivity中------------------

    @Target(ElementType.METHOD)//注解的作用域
    @Retention(RetentionPolicy.RUNTIME)//注解的有效生命周期
    public @interface GetPermission {
        boolean permissionResult();

        int requestCode();
    }

    public static void injectActivity(Activity activity, boolean permissionResult, int requestCode) {
        Class clazz = activity.getClass();
//        clazz.getMethods();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(GetPermission.class)) {
                GetPermission annotation = method.getAnnotation(GetPermission.class);
                if (permissionResult == annotation.permissionResult() && annotation.requestCode() == requestCode) {
                    try {
                        method.setAccessible(true);
                        method.invoke(activity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

//    实例:调用拍照权限
//    需要优化之处:
//      1）加注解时必须是不参数的method
//      2）不能对多个权限进行统一设置
//    public class TestActivity  {
//
//        private final int PERMISSION_CAMERA = 100;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//            if(checkPermission(Manifest.permission.CAMERA, PERMISSION_CAMERA)){
//                toDoThing();
//            }
//
//        }
//
//        @PermissionUtils.PermissionHelper(permissionResult = true, requestCode = PERMISSION_CAMERA)
//        private void toDoThing(){
//
//        }
//
//    }


}
