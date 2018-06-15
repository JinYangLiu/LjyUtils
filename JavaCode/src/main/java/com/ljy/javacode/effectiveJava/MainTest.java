package com.ljy.javacode.effectiveJava;

import com.ljy.javacode.effectiveJava.bean.Person;

import java.util.Scanner;

/**
 * Created by ljy on 2018/6/15.
 */

public class MainTest {
    public static void main(String[] args) {
        System.out.print("请输入要执行的方法名：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            switch (param) {
                case "person":
                    Boolean.valueOf("true");
                    testPerson();
                    break;
                default:
                    break;
            }
        }

    }

    private static void testPerson() {
        //Class.newInstance()
        try {
            Person p=Person.class.newInstance();
            //非必要时不推荐使用这种写法
            //1。要处理各种异常，既不方便也不雅观
            //2。破坏了编译时对异常检查
            //3。无法知道Person.class是否存在无参的构造方法
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //通过构造器
        Person person1=new Person();
        Person person2=new Person("ljy");
        Person person3=new Person("ljy",12);
        //通过工厂方法
        Person person4=Person.person();
        Person person5=Person.personWithName("ljy");
        Person person6=Person.personWithNameAndAge("ljy",15);
        //通过建造者
        Person person=new Person.Builder()
                .setName("ljy")
                .setNickname("Mr.L")
                .setAge(18)
                .setHeight(170)
                .setSex("Man")
                .setEmail("123321@qq.com")
                .build();
        System.out.println(person.toString());

    }
}
