package com.ljy.javacode.structure.link;

/**
 * Created by ljy on 2018/6/1.
 * <p>
 * 有序链表
 * <p>
 * 插入数据时进行比较
 * <p>
 * 表插入排序:
 * 有序链表可以用于一种高效的排序机制,假设有一个无序数组,如果从这个数组中取出数据,
 * 然后一个一个的插入有序链表,他们自动的按顺序排列,把它们从有序表中删除,重新放入数组,
 * 那么数组就排好序了,这种排序方式比常用的插入排序效率更高些
 */

public class OrderedLinkList<T extends Comparable<T>> extends LinkList<T> {
    public void insert(T value) {
        Link<T> newLink = new Link<>(value);
        Link<T> previous = null;
        Link<T> current = first;
        while (current != null && value.compareTo(current.data) > 0) {
            previous = current;
            current = current.next;
        }
        if (previous == null)
            first = newLink;
        else
            previous.next = newLink;
        newLink.next = current;
    }

    public Link<T> find(T value) {
        Link<T> current = first;
        while (current != null && current.data.compareTo(value) <= 0) {
            if (current.data == value)
                return current;
            current = current.next;
        }
        return null;
    }

    public Link<T> delete(T value) {
        Link<T> previous = null;//当前值的前一个链结点
        Link<T> current = first;//当前链结点
        while (current != null && current.data.compareTo(value) < 0) {
            previous = current;
            current = current.next;
        }
        if (current.data != value)
            return null;
        if (current == first)
            //如果所找的值对应first链结点，就把first指向下一个链结点，也就是first.next
            first = first.next;
        else
            //如果不是对应first，就把该链结点的前一个链结点的指针next指向该链结点的下一个链结点，即current.next
            previous.next = current.next;
        return current;
    }
}
