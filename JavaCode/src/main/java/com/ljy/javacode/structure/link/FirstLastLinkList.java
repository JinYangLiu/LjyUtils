package com.ljy.javacode.structure.link;

/**
 * Created by ljy on 2018/5/31.
 *
 * 双端链表
 *
 * 新增一个last变量，最后一个链结点的引用
 *
 * 之前在Queue.java中用数组实现了队列，那么将Queue中的数组替换为FirstLastLinkList，
 * insert用insertLast实现，remove用deleteFirst实现，就可以用双端链表实现队列了
 *
 */

public class FirstLastLinkList {
    private Link<String> first;
    private Link<String> last;

    public boolean isEmpty(){
        return first==null;
    }

    public void insertFirst(String value){
        Link<String> newLink=new Link<>(value);
        if (isEmpty())
            last=newLink;
        newLink.next=first;
        first=newLink;
    }

    public void insertLast(String value){
        Link<String> newLink=new Link<>(value);
        if (isEmpty())
            first=newLink;
        else
            last.next=newLink;
        last=newLink;
    }

    public Link<String> deleteFirst(){
        Link temp=first;
        if (first.next==null)
            last=null;
        first=first.next;
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

}
