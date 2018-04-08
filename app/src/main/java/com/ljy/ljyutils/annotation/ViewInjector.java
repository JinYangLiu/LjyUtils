package com.ljy.ljyutils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

//    @Target:
//    作用：用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
//    取值(ElementType)有：
//　　　　1.CONSTRUCTOR:用于描述构造器
//　　　　2.FIELD:用于描述域/字段
//　　　　3.LOCAL_VARIABLE:用于描述局部变量
//　　　　4.METHOD:用于描述方法
//　　　　5.PACKAGE:用于描述包
//　　　　6.PARAMETER:用于描述参数
//　　　　7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
//    @Retention：
//    作用：表示需要在什么级别保存该注释信息，用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
//　　取值（RetentionPoicy）有：
//　　　　1.SOURCE:在源文件中有效（即源文件保留）
//　　　　2.CLASS:在class文件中有效（即class保留）
//　　　　3.RUNTIME:在运行时有效（即运行时保留）
//   @Documented:
// 指明拥有这个注解的元素可以被javadoc此类的工具文档化。
// 这种类型应该用于注解那些影响客户使用带注释的元素声明的类型。
// 如果一种声明使用Documented进行注解，这种类型的注解被作为被标注的程序成员的公共API。
//   @Inherited:
// 指明该注解类型被自动继承。如果用户在当前类中查询这个元注解类型并且当前类的声明中不包含这个元注解类型，
// 那么也将自动查询当前类的父类是否存在Inherited元注解，这个动作将被重复执行知道这个标注类型被找到，
// 或者是查询到顶层的父类。
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface ViewInjector {
    int value();
}