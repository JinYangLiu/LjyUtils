package com.ljy.ljyutils.bean;

import javax.inject.Inject;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

public class Dog {
    private String name;

    @Inject
    public Dog(){
    }
    public String watchHome(){
        return String.format("%s.watchHome",name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
