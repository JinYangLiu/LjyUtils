package com.ljy.javacode.structure.stack;

import com.ljy.javacode.structure.link.LinkList;

/**
 * Created by ljy on 2018/6/1.
 *
 * 使用单链表实现栈
 */

public class LinkStack<T> extends Stack<T> {

    private LinkList<T> linkList;

    public LinkStack() {
        linkList =new LinkList();
    }

    @Override
    public boolean isEmpty() {
        return linkList.getFirst()==null;
    }

    @Override
    public void push(T value) {
        linkList.insertFirst(value);
    }

    @Override
    public T pop() {
        return linkList.deleteFirst().data;
    }

    @Override
    public T peek() {
        return linkList.getFirst().data;
    }

    @Override
    public void display() {
        System.out.print("LinkStacker: ");
        linkList.display();
    }
}
