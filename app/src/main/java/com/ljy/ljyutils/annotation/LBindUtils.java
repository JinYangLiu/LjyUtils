package com.ljy.ljyutils.annotation;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

public class LBindUtils {
    public static void bind(Activity target) {
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            boolean isFExist = field.isAnnotationPresent(LBindView.class);
            if (isFExist) {
                LBindView lBindView = field.getAnnotation(LBindView.class);
                int viewResId = lBindView.value();
                try {
                    field.setAccessible(true);
                    field.set(target, target.findViewById(viewResId));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
