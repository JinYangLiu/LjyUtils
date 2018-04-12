package com.ljy.ljyutils.bean;

import java.io.Serializable;

/**
 * Created by LJY on 2018/4/12.
 */

public class SeriBean implements Serializable {
    public String name;

    public SeriBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SeriBean{" +
                "name='" + name + '\'' +
                '}';
    }


}
