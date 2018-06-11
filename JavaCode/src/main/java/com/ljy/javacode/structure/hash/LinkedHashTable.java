package com.ljy.javacode.structure.hash;

import com.ljy.javacode.structure.link.Link;
import com.ljy.javacode.structure.link.OrderedLinkList;

/**
 * Created by LJY on 2018/6/11 11:26
 *  链地址法 + 有序链表 实现 哈希表
 *
 *  这里因为Link是之前实现链表时写的,只有data字段,就不在改了,
 *  理想的Link应该有key(做关键字),value(即data,数据域)两个字段方便使用
 */
public class LinkedHashTable {
    private OrderedLinkList<Integer>[] linkArr;
    private int arrSize;

    public LinkedHashTable( int size) {
        this.arrSize = size;
        this.linkArr = new OrderedLinkList[arrSize];
        for (int i = 0; i < arrSize; i++) {
            linkArr[i]=new OrderedLinkList<>();
        }
    }

    public void display(){
        for (int i = 0; i < arrSize; i++) {
            System.out.print(i+"-->");
            linkArr[i].display();
        }
    }

    //根据 关键字值 计算 哈希值
    private int hashFunc(int key) {
        int hashVal = key % arrSize;
        System.out.println("hashFunc: arraySize:" + arrSize + ", key:" + key + ", hashVal:" + hashVal);
        return hashVal;
    }

    public void insert(int value){
        int hashVal=hashFunc(value);
        linkArr[hashVal].insert(value);
    }

    public Link<Integer> delete(int value){
        int hashVal=hashFunc(value);
        return linkArr[hashVal].delete(value);
    }

    public Link<Integer> find(int value){
        int hashVal=hashFunc(value);
        return linkArr[hashVal].find(value);
    }
}
