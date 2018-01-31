package com.ljy.view.swipeBack;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by Mr.LJY on 2018/1/31.
 */

public class Utils {
    private Utils() {
    }

    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent", new Class[0]);
            method.setAccessible(true);
            method.invoke(activity, new Object[0]);
        } catch (Throwable var2) {
            ;
        }

    }

    public static void convertActivityToTranslucent(Activity activity) {
        if(Build.VERSION.SDK_INT >= 21) {
            convertActivityToTranslucentAfterL(activity);
        } else {
            convertActivityToTranslucentBeforeL(activity);
        }

    }

    public static void convertActivityToTranslucentBeforeL(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            Class[] var3 = classes;
            int var4 = classes.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Class clazz = var3[var5];
                if(clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[]{translucentConversionListenerClazz});
            method.setAccessible(true);
            method.invoke(activity, new Object[]{null});
        } catch (Throwable var7) {
            ;
        }

    }

    private static void convertActivityToTranslucentAfterL(Activity activity) {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions", new Class[0]);
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity, new Object[0]);
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            Class[] var5 = classes;
            int var6 = classes.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Class clazz = var5[var7];
                if(clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[]{translucentConversionListenerClazz, ActivityOptions.class});
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, new Object[]{null, options});
        } catch (Throwable var9) {
            ;
        }

    }
}
