package com.ljy.javacode.structure.stack;

import com.ljy.javacode.structure.link.LinkList;

/**
 * Created by ljy on 2018/6/1.
 *
 * 使用单链表实现栈
 */

public class LinkStacker extends Stacker {

    private LinkList linkList;

    public LinkStacker() {
        linkList =new LinkList();
    }

    @Override
    public boolean isEmpty() {
        return linkList.getFirst()==null;
    }

    @Override
    public void push(String value) {
        linkList.insertFirst(value);
    }

    @Override
    public String pop() {
        return linkList.deleteFirst().data;
    }

    @Override
    public String peek() {
        return linkList.getFirst().data;
    }

    @Override
    public void display() {
        System.out.print("LinkStacker: ");
        linkList.display();
    }
}
