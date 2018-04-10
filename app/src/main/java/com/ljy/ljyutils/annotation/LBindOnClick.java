package com.ljy.ljyutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LJY on 2018/4/10.
 *
 * 注解实现setOnClickListener
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LBindOnClick {
    int[] value();
}
