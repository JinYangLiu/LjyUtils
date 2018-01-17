package com.ljy.ljyutils.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Mr.LJY on 2018/1/17.
 */

@Entity//告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
public class Phone {
    @Id//对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
    private long id;
    @Unique//该属性值必须在数据库中是唯一值
    private String name;
    @NotNull//属性不能为空
    private int price;
    @Property(nameInDb = "phone_description")//可以自定义字段名，注意外键不能使用该属性
    private String info;
    @Transient//使用该注释的属性不会被存入数据库的字段中
    private String info2;
    @Generated(hash = 1046515579)
    public Phone(long id, String name, int price, String info) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.info = info;
    }
    @Generated(hash = 429398894)
    public Phone() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getInfo() {
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    //@Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改


    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", info='" + info + '\'' +
                ", info2='" + info2 + '\'' +
                '}';
    }
}
