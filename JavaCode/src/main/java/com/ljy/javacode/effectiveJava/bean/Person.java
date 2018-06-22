package com.ljy.javacode.effectiveJava.bean;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ljy on 2018/6/14.
 */

public class Person implements Cloneable, Comparable<Person> {
    private String name;//姓名
    private String nickname;//昵称
    private int age;//年龄
    private int height;//身高
    private String sex;//性别
    private String email;//邮箱
    private Person father;

    public void setAge(int age) {
        this.age = age;
    }
    //----- 1。提供公有构造器

    public Person() {
        this("");//重叠构造器
    }

    public Person(String name) {
        this(name, 0);//重叠构造器
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

    public Person(String name, int age, int height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }


    //----- 2。提供静态工厂方法,可以随心所欲的组合，而且顾名知义

    public static Person person() {
        return new Person();
    }

    public static Person personWithName(String name) {
        Person person = new Person();
        person.name = name;
        return person;
    }

    public static Person personWithNameAndAge(String name, int age) {
        Person person = new Person();
        person.name = name;
        person.age = age;
        return person;
    }

    public static Person personWithNameAndHeight(String name, int height) {
        Person person = new Person();
        person.name = name;
        person.height = height;
        return person;
    }


    //----- 3. 建造者模式

    private Person(Builder builder) {
        this.name = builder.name;
        this.nickname = builder.nickname;
        this.sex = builder.sex;
        this.age = builder.age;
        this.height = builder.height;
        this.email = builder.email;
        this.father = builder.father;
    }

    @Override
    public int compareTo(@NonNull Person other) {
        //可以按关键域的顺序进行比较
        //1.年龄
        if (age != other.age)
            //这里不用age-other.age,是因为没有限制age可否为负数的情况
            return age > other.age ? 1 : -1;

        //2.出生具体时间
        if (birthDate != null && other.birthDate != null
                && birthDate.getTime() != other.birthDate.getTime()) {
            return birthDate.getTime() > other.birthDate.getTime() ? 1 : -1;
        }
        return 0;
    }

    public void printHello() {
        System.out.println("hello");
    }

    public static class Builder {
        private String name;
        private String nickname;
        private int age;
        private int height;
        private String sex;
        private String email;
        private Person father;

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

        public Builder setFather(Person father) {
            this.father = father;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    //------ 4。 避免创建不必要的对象
    //新增一个出生日期属性
    private Date birthDate;

    //增加一个判断是否生育高峰期出生的方法：
    //1。不好的写法：
    public boolean isBabyBoomer_bad() {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomStart = gmtCal.getTime();
        gmtCal.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomEnd = gmtCal.getTime();
        return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
    }
    //上面这样写每次调用都会创建一个Calendar，一个TimeZone，两个Date的实例，尤其Calendar的创建是比较好性能的
    //仔细回想一下，我们平时是不是也经常写这种代码呢？

    //2。使用静态初始化器改进
    private static final Date BOOM_START;
    private static final Date BOOM_END;

    static {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_START = gmtCal.getTime();
        gmtCal.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_END = gmtCal.getTime();
    }

    public boolean isBabyBoomer_good() {
        return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0;
    }


//    public boolean equals(Person obj) {
//        return true;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person p = (Person) obj;
            System.out.println(String.format("hash:_%d, p.hash_%d", this.hashCode(), p.hashCode()));
            return this.hashCode() == p.hashCode()
                    && this.name.equals(p.name)
                    && this.age == p.age
                    && this.height == p.height;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return initHashCode();
    }

    int result = 0;

    private int initHashCode() {
        //如果一个类是不可变的，并且计算散列码卡小比较大，应该考虑把散列码缓存起来，并使用延迟初始化
        if (result == 0) {
            //1.将某个非零常数值赋值给result
            result = 17;
            //2.a 为每个关键域f（指equals涉及的域）计算散列码c
            int nameHash = name.hashCode();
            int ageHash = age;
            int heightHash = height;
            //2.b 把2.a中计算得到的散列码合并到result中
            result = 31 * result + nameHash;
            result = 31 * result + ageHash;
            result = 31 * result + heightHash;
        }
        //3. 返回result
        return result;
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
                ", father=" + father +
                '}';
    }

    @Override
    public Person clone() throws CloneNotSupportedException {
        Person p = (Person) super.clone();
        if (this.father != null)
            p.father = this.father.clone();//深拷贝
        return p;
    }

//    class Son extends Person implements Cloneable,Comparable<Son>{
//        @Override
//        public int compareTo(@NonNull Person o) {
//            return 0;
//        }
//    }

    private static final Person[] familys = {new Person("bob"), new Person("lily")};
    //1. 公有+可变对象的数组
    public static final Person[] FAMILYS_1 = familys;
    //2. 私有数组+公有不可变列表
    public static final List<Person> FAMILYS_2 = Collections.unmodifiableList(Arrays.asList(familys));

    //3. 私有数组+公有方法返回私有数组备份
    public static Person[] getFamilys() {
        return familys.clone();
    }

    public static BigInteger safeInstance(BigInteger val) {

//        if (val instanceof BigInteger)//这里不能用instanceOf，因为val可能是其子类
        if (val.getClass() != BigInteger.class)
            return new BigInteger(val.toByteArray());
        return val;
    }

    class MyList {

        private ArrayList<String> mList = new ArrayList<>();

//        public boolean add(String value){
//           return mList.add(value);
//        }

        //问题2：如果有一天add方法进行了这样的修改，对空值进行判断,那么子类中的size就可能不准确了
        public boolean add(String value) {
            if (!TextUtils.isEmpty(value))
                return mList.add(value);
            return false;
        }

        public int addAll(String[] values) {
            int count = 0;
            for (String val : values) {
                if (add(val))
                    count++;
            }
            return count;
        }
    }

    //演示继承可能引发的问题
    class MySizeList extends MyList {
        private int size;

        @Override
        public boolean add(String value) {
            size++;
            return super.add(value);
        }

        @Override
        public int addAll(String[] values) {
            //问题1：这里看着逻辑上没有问题，然而super.addAll底层调用的是add方法，就会导致size被加类两遍
            size += values.length;
            return super.addAll(values);
        }

        public int getSize() {
            return size;
        }
    }

    //使用复合/转发代替继承
    class MyForwardList {

        private MyList myList;

        private int size;

        public void add(String value) {
            if (myList.add(value))
                size++;
        }

        public void addAll(String[] values) {
            size += myList.addAll(values);
        }
    }

//    //比较器接口
//    public interface Comparator<T>{
//        public int compare(T t1,T t2);
//    }
//
//    //宿主类
//    class Host{
//        //通过长度比较字符串的比较器, 这里使用静态内部类，而不是匿名内部类，以便具体的策略类实现第二个Serializable接口
//        private static class StrLenCmp implements Comparator<String>,Serializable {
//
//            @Override
//            public int compare(String t1, String t2) {
//                return t1.length()-t2.length();
//            }
//        }
//
//        //函数对象表示策略
//        public static final StrLenCmp STR_LEN_CMP=new StrLenCmp();
//
//        public String name;
//
//        public int compare(Host other){
//            return STR_LEN_CMP.compare(this.name,other.name);
//        }
//    }


}
