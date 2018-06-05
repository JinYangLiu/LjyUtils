package com.ljy.javacode.structure.link;

import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ljy on 2018/5/31.
 * <p>
 * 单链表
 * <p>
 * 之前在Stacker.java中用数组实现了栈，现在我们知道了链表
 * 那么将Stacker中的数组替换为LinkList，push用insertFirst实现，pop用deleteFirst实现，也是完全可以的
 */

public class LinkList<T> {
    protected Link<T> first;//LinkList仅包含了一个数据项，即对第一个链结点的引用

    public boolean isEmpty() {
        return first == null;
    }

    /**
     * 向链表插入数据
     *
     * @param value
     */
    public void insertFirst(T value) {
        //新建链结点，把数据传入这个链结点，将这个新的链结点的next字段指向原来的first的值，将first指向这个新的链结点
        Link<T> newLink = new Link<>(value);
        newLink.next = first;
        first = newLink;
    }

    public Link<T> deleteFirst() {
        Link temp = first;
        first = first.next;
        return temp;
    }

    public void display() {
        System.out.print("LinkList:{ ");
        Link current = first;
        while (current != null) {
            current.display();
            if (current.next != null)
                System.out.print(", ");
            current = current.next;
        }
        System.out.println("} ");
    }

    public Link<T> find(T value) {
        Link current = first;
        //找到才跳出循环，否则一直找，直到next==null，该链结点链表的最后一个
        while (!current.data.equals(value)) {
            if (current.next == null)
                return null;
            else
                current = current.next;
        }
        return current;
    }

    public Link<T> delete(T value) {
        Link current = first;//当前链结点
        Link previous = first;//当前值的前一个链结点
        if (current == null)
            return null;
        //找到才跳出循环，否则一直找，直到next==null，该链结点链表的最后一个
        while (!current.data.equals(value)) {
            if (current.next == null)
                return null;
            else {
                previous = current;
                current = current.next;
            }
        }
        if (current == first)
            //如果所找的值对应first链结点，就把first指向下一个链结点，也就是first.next
            first = first.next;
        else
            //如果不是对应first，就把该链结点的前一个链结点的指针next指向该链结点的下一个链结点，即current.next
            previous.next = current.next;
        return current;
    }


    public LinkIterator<T> iterator(){
        return new LinkIterator<>(this);
    }

    public Link<T> getFirst() {
        return first;
    }
}
