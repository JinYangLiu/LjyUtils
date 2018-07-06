package com.ljy.javacode.effectiveJava.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Retention，Target这种用在注解类型声明中的注解被称为元注解
@Retention(RetentionPolicy.RUNTIME)//表示test注解应在运行时保留
@Target(ElementType.METHOD)//表示test注解只在方法声明中才合法，不能用在类声明，域声明或其他元素
public @interface Test {
    Class<? extends Exception>[] value();
}
