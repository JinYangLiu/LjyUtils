package com.ljy.javacode.structure.link;

/**
 * Created by ljy on 2018/6/1.
 *
 * 有序链表
 *
 * 插入数据时进行比较
 *
 * 表插入排序:
 * 有序链表可以用于一种高效的排序机制,假设有一个无序数组,如果从这个数组中取出数据,
 * 然后一个一个的插入有序链表,他们自动的按顺序排列,把它们从有序表中删除,重新放入数组,
 * 那么数组就排好序了,这种排序方式比常用的插入排序效率更高些
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
