package com.ljy.javacode.structure.link;

/**
 * Created by LJY on 2018/6/5 10:35
 */
public class DoublyLink<T>  {
    public T data;//数据域
    public DoublyLink<T> left;//指针域, 指向前一个链结点
    public DoublyLink<T> right;//指针域, 指向后一个链结点

    public DoublyLink(T data) {
        this.data = data;
    }

    public void display(){
        System.out.print("DoublyLink:{"+data.toString()+"}");
    }
}
