package com.ljy.ljyutils.bean;

/**
 * Created by Mr.LJY on 2017/9/21.
 */

public class User {
    public String name;
    private String pwd;
    private String age;

    public User() {

    }

    private User(String name) {
        this.name = name;
    }

    public User(String name, String pwd, String age) {
        this.name = name;
        this.pwd = pwd;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
