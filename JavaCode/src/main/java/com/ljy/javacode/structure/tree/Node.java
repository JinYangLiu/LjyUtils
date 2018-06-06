package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/6 16:46
 *
 * 节点类
 */
public class Node<K, T> {
    public K key;//关键字
    public T data;//数据域

    public void display() {
        System.out.print("Node:{"
                + key.toString()
                + ", "
                + data.toString()
                + "}");
    }
}
