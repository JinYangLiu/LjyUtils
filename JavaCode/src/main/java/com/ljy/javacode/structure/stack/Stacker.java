package com.ljy.javacode.structure.stack;

/**
 * Created by ljy on 2018/6/1.
 */

public abstract class Stacker {

    public abstract boolean isEmpty();

    public abstract void push(String value);

    public abstract String pop();

    public abstract String peek();

    public abstract void display();


}
