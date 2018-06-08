package com.ljy.javacode.structure.bean;

/**
 * Created by ljy on 2018/6/8.
 * <p>
 * 数据项的模型类
 */
public class DataItem<K, T> {
    public K key;
    public T data;

    public DataItem() {
    }

    public DataItem(K key) {
        this.key = key;
    }

    public void display() {
        System.out.print(" DataItem:{ key = " + (key==null?"null":key.toString())
                +", data = " +( data==null?"null":data.toString()) + "}");
    }

}
