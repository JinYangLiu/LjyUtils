package com.ljy.javacode.structure.queue;

import com.ljy.javacode.structure.link.FirstLastLinkList;

/**
 * Created by ljy on 2018/6/1.
 *
 * 用双端链表实现队列
 */

public class LinkQueue extends Queue {

    private FirstLastLinkList linkList;

    public LinkQueue() {
        linkList=new FirstLastLinkList();
    }

    @Override
    public boolean isEmpty() {
        return linkList.isEmpty();
    }

    @Override
    public void insert(String value) {
        linkList.insertLast(value);
    }

    @Override
    public String remove() {
        return linkList.deleteFirst().data;
    }

    @Override
    public void display() {
        System.out.print("LinkQueue: ");
        linkList.display();
    }
}
