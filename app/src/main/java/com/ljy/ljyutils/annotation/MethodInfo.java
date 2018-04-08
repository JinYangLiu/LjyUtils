package com.ljy.ljyutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInfo {
    String author() default "Mr.L";
    String date();
    int revision() default 1;//修订,版本
    String comments();//评论,注解
}
