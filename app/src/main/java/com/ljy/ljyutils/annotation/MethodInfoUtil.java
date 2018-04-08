package com.ljy.ljyutils.annotation;

import com.ljy.util.LjyLogUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

public class MethodInfoUtil {
    public static void showInfo(Object obj) {
        Class<?> targetClass = obj.getClass();
        for (Method method : targetClass.getDeclaredMethods()) {
            // checks if MethodInfo annotation is present for the method
            if (method.isAnnotationPresent(MethodInfo.class)) {
                for (Annotation anno : method.getDeclaredAnnotations()) {
                    LjyLogUtil.i("-------\nMethod: " + method + "\nAnnotation: " + anno+"\n-------");
                    MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
                    LjyLogUtil.i("author:" + methodInfo.author());
                    LjyLogUtil.i("date:" + methodInfo.date());
                    LjyLogUtil.i("comments:" + methodInfo.comments());
                    LjyLogUtil.i("revision:" + methodInfo.revision());
                }
            } else {
                LjyLogUtil.i("-------\n000000 " + method);
            }
        }
    }
}

