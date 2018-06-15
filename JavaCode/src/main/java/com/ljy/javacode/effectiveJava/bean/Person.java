package com.ljy.javacode.effectiveJava.bean;

import android.provider.ContactsContract;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ljy on 2018/6/14.
 */

public class Person {
    private String name;//姓名
    private String nickname;//昵称
    private int age;//年龄
    private int height;//身高
    private String sex;//性别
    private String email;//邮箱

    //----- 1。提供公有构造器

    public Person() {
        this("");//重叠构造器
    }

    public Person(String name) {
        this(name,0);//重叠构造器
    }
    //如果想再提供用昵称来初始化对象的构造器呢？

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    //如果想提供姓名+身高初始化对象的构造器呢，可以通过下面的参数换位实现

    public Person(int height, String name) {
        this.height = height;
        this.name = name;
    }

    //那么如果想再提供昵称+年龄，昵称+身高，年龄+性别等来初始化的构造器呢？


    //----- 2。提供静态工厂方法,可以随心所欲的组合，而且顾名知义

    public static Person person() {
        return new Person();
    }

    public static Person personWithName(String name) {
        Person person = new Person();
        person.name = name;
        return person;
    }

    public static Person personWithNameAndAge(String name,int age) {
        Person person=new Person();
        person.name=name;
        person.age=age;
        return person;
    }

    public static Person personWithNameAndHeight(String name,int height) {
        Person person=new Person();
        person.name=name;
        person.height=height;
        return person;
    }


    //----- 3. 建造者模式

    private Person(Builder builder){
        this.name=builder.name;
        this.nickname=builder.nickname;
        this.sex=builder.sex;
        this.age=builder.age;
        this.height=builder.height;
        this.email=builder.email;
    }

    public static class Builder{
        private String name;
        private String nickname;
        private int age;
        private int height;
        private String sex;
        private String email;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Person build(){
            return new Person(this);
        }
    }

    //------ 4。 避免创建不必要的对象
    //新增一个出生日期属性
    private  Date birthDate;

    //增加一个判断是否生育高峰期出生的方法：
    //1。不好的写法：
    public boolean isBabyBoomer_bad(){
        Calendar gmtCal=Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1990,Calendar.JANUARY,1,0,0,0);
        Date boomStart =gmtCal.getTime();
        gmtCal.set(2018,Calendar.JANUARY,1,0,0,0);
        Date boomEnd=gmtCal.getTime();
        return birthDate.compareTo(boomStart)>=0&&birthDate.compareTo(boomEnd)<0;
    }
    //上面这样写每次调用都会创建一个Calendar，一个TimeZone，两个Date的实例，尤其Calendar的创建是比较好性能的
    //仔细回想一下，我们平时是不是也经常写这种代码呢？

    //2。使用静态初始化器改进
    private static final Date BOOM_START;
    private static final Date BOOM_END;
    static {
        Calendar gmtCal=Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1990,Calendar.JANUARY,1,0,0,0);
        BOOM_START =gmtCal.getTime();
        gmtCal.set(2018,Calendar.JANUARY,1,0,0,0);
        BOOM_END=gmtCal.getTime();
    }
    public boolean isBabyBoomer_good(){
        return birthDate.compareTo(BOOM_START)>=0&&birthDate.compareTo(BOOM_END)<0;
    }







    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
