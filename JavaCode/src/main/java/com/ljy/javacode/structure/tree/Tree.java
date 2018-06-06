package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/6 16:47
 *
 * 树类
 */
public abstract class Tree<E extends Node,K,T> {
    public E root;
    public abstract E find(K key);
    public abstract void insert(K key,T data);
    public abstract boolean delete(K key);
}
