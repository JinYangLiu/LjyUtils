package com.ljy.javacode.structure.graph;

/**
 * Created by LJY on 2018/6/11 17:31
 *
 *  图的顶点类
 */
public class Vertex<T> {
    public T value;
    public boolean wasVisited;//标志位,搜索算法时用到

    public Vertex(T value) {
        this.value = value;
        this.wasVisited=false;
    }
}
