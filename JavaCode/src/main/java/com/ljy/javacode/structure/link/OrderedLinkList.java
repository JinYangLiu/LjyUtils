package com.ljy.javacode.structure.link;

/**
 * Created by ljy on 2018/6/1.
 *
 * 有序链表
 *
 * 插入数据时进行比较
 */

public class OrderedLinkList extends LinkList<Long> {
    public void insert(Long value) {
        Link<Long> newLink = new Link<>(value);
        Link<Long> previous = null;
        Link<Long> current = first;
        while (current != null && value > current.data){
            previous=current;
            current=current.next;
        }
        if (previous==null)
            first=newLink;
        else
            previous.next=newLink;
        newLink.next=current;
    }
}
