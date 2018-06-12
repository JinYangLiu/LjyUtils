package com.ljy.javacode.structure.queue;

/**
 * Created by ljy on 2018/6/1.
 */

public abstract class Queue<T> {
    public abstract boolean isEmpty();

    public abstract void insert(T value);

    public abstract T remove();

    public abstract void display();
}
