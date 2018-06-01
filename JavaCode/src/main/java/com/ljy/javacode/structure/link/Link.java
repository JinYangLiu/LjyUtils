package com.ljy.javacode.structure.link;

/**
 * Created by ljy on 2018/5/31.
 * <p>
 * 链表：
 * 一种物理存储单元上非连续、非顺序的存储结构,数据元素的逻辑顺序是通过链表中的指针链接次序实现的
 * <p>
 * 无序数组搜索慢，有序数组插入慢，且数组的删除效率低，大小固定
 * 链表则常用来替换数组，作为其他存储结构的基础，以解决上面问题
 * 除非需要频繁的用下标随机访问各个数据，否则很多地方都可以用链表代替数组
 * <p>
 * 分类：
 * 单链表
 * 双端链表
 * 有序链表
 * 双向链表
 * 有迭代器的链表
 * <p>
 * 链表由多个链结点组成，每个链结点由两部分组成，一个存储数据元素的数据域，一个存储下一个结点地址的指针域
 * <p>
 * 在链表中，寻找一个特定元素的唯一方法，就是沿着这个元素的链一直向下寻找
 */

//此类为链结点
public class Link<T> {
    public T data;//数据域
    public Link next;//指针域
    //此处next为一个和自己类型相同的字段，因此也叫"自引用"式


    public Link(T data) {
        this.data = data;
    }

    public void display(){
        System.out.print("Link:{"+data.toString()+"}");
    }
}
