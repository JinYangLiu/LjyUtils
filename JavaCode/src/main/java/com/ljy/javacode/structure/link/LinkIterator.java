package com.ljy.javacode.structure.link;

/**
 * Created by LJY on 2018/6/5 13:58
 *
 *  linkList 的 迭代器
 */
public class LinkIterator<T> {
    private LinkList<T> linkList;
    private Link<T> current;
    private Link<T> previous;

    public LinkIterator(LinkList<T> linkList) {
        this.linkList = linkList;
        reset();
    }

    private void reset() {
        current=linkList.getFirst();
        previous=null;
    }

    public Link<T> getCurrent() {
        return current;
    }

    public boolean hasNext(){
        return current!=null&&current.next!=null;
    }

    public Link<T> next(){
        previous=current;
        current=current.next;
        return previous;
    }

    public void remove(){
        if (current==linkList.first)
            linkList.first=linkList.first.next;
        else
            previous.next=current.next;
    }

}
