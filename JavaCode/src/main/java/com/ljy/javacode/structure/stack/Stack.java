package com.ljy.javacode.structure.stack;

/**
 * Created by ljy on 2018/6/1.
 */

public abstract class Stack<T> {

    public abstract boolean isEmpty();

    public abstract void push(T value);

    public abstract T pop();

    public abstract T peek();

    public abstract void display();


}
