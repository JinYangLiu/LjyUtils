package com.ljy.ljyutils.interfac;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Mr.LJY on 2018/1/11.
 *
 * dagger2自定义注解
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface CatPet {
}
