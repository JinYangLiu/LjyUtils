package com.ljy.javacode.structure.hash;

import com.ljy.javacode.structure.bean.DataItem;

/**
 * Created by ljy on 2018/6/8.
 * <p>
 * 哈希表的实现
 */

public class HashTable<T> {
    private static final int DEFAULT_SIZE = 10;
    private DataItem<Integer, T>[] array;
    private int arraySize;
    private DataItem<Integer, T> nonItem;//for deleted items

    private boolean isLineDetect=false;//控制时线性探测还是二次探测

    /**
     * 控制时线性探测还是二次探测
     * @param lineDetect
     */
    public void setLineDetect(boolean lineDetect) {
        isLineDetect = lineDetect;
    }

    public HashTable() {
        this.arraySize = DEFAULT_SIZE;
        array = new DataItem[arraySize];
        nonItem = new DataItem<>(-1);
    }

    //根据 关键字值 计算 哈希值
    private int hashFunc(int key) {
        int hashVal=key % arraySize;
        System.out.println("arraySize:"+arraySize+", key:"+key+", hashVal:"+hashVal);
        return hashVal;
    }

    /**
     * 向哈希表中插入数据
     *
     */
    public void insert(int key ,T data) {
        DataItem<Integer, T> item=new DataItem<>();
        item.key=key;
        item.data=data;
        int count = 0;
        int hashVal = hashFunc(key);
        int step=0;
        while (array[hashVal] != null && array[hashVal].key != -1) {
            if (++count > arraySize) {
                expandArray();//数组满了后进行扩充
            }
            if (isLineDetect) {
                // 线性探测
                ++hashVal;
            }else {
                //二次探测：目前这样写会有问题，数组没有满时就会扩充
                ++step;
                hashVal += step * step;
            }

            hashVal = hashFunc(hashVal);//关键点
        }
        array[hashVal] = item;
    }

    private void expandArray() {
        DataItem<Integer, T>[] temp = array;
        arraySize *= 2;
        array = new DataItem[arraySize];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i]!=null&&temp[i].key!=-1){
                insert(temp[i].key,temp[i].data);
                System.out.print("expandArray: ");
                display();
            }
        }
    }

    public DataItem<Integer, T> find(int key) {
        int count = 0;
        int hashVal = hashFunc(key);
        int step=0;
        while (array[hashVal] != null) {
            if (++count > arraySize)
                return null;

            if (array[hashVal].key == key)
                return array[hashVal];
            if (isLineDetect) {
                // 线性探测
                ++hashVal;
            }else {
                //二次探测
                ++step;
                hashVal += step * step;
            }
            hashVal = hashFunc(hashVal);
        }
        return null;
    }

    /**
     * 从哈希表中删除数据
     *
     * @param key
     * @return
     */
    public DataItem<Integer, T> delete(int key) {
        int count=0;
        int hashVal = hashFunc(key);
        int step=0;
        while (array[hashVal] != null) {
            if (++count > arraySize)
                return null;
            if (array[hashVal].key == key) {
                DataItem<Integer, T> temp = array[hashVal];
                array[hashVal] = nonItem;
                return temp;
            }
            if (isLineDetect) {
                // 线性探测
                ++hashVal;
            }else {
                //二次探测
                ++step;
                hashVal += step * step;
            }
            hashVal = hashFunc(hashVal);
        }
        return null;
    }


    /**
     * 打印哈希表
     */
    public void display() {
        System.out.print("HashTable: ");
        for (int i = 0; i < arraySize; i++) {
            if (array[i] != null)
                array[i].display();
            else
                System.out.print(" * ");
        }
        System.out.println();
    }
}
