package com.ljy.javacode.structure.hash;

import com.ljy.javacode.structure.bean.DataItem;

/**
 * Created by ljy on 2018/6/8.
 * <p>
 * 哈希表的实现
 */

public class HashTable<T> {
    private static final int DEFAULT_SIZE = 11;
    public static final int TYPE_DETECT_LINE = 0;//线性探测
    public static final int TYPE_DETECT_SECOND = 1;//二次探测
    public static final int TYPE_DETECT_DOUBLE_HASH = 2;//再哈希探测
    private DataItem<Integer, T>[] array;
    private int arraySize;
    private DataItem<Integer, T> nonItem;//for deleted items

    public HashTable() {
        this.arraySize = DEFAULT_SIZE;
        array = new DataItem[arraySize];
        nonItem = new DataItem<>(-1);
    }

    //根据 关键字值 计算 哈希值
    private int hashFunc(int key) {
        int hashVal = key % arraySize;
        System.out.println("hashFunc: arraySize:" + arraySize + ", key:" + key + ", hashVal:" + hashVal);
        return hashVal;
    }

    // 再哈希法计算探测序列
    private int hashFunc2(int key) {
        int hashVal = 5 - key % 5;
        System.out.println("hashFunc_2: arraySize:" + arraySize + ", key:" + key + ", hashVal:" + hashVal);
        return hashVal;
    }

    /**
     * 向哈希表中插入数据
     * <p>
     * typeDetect 探测类型
     */
    public void insert(int key, T data) {
        insert(key, data, TYPE_DETECT_LINE);
    }

    public void insert(int key, T data, int typeDetect) {
        DataItem<Integer, T> item = new DataItem<>();
        item.key = key;
        item.data = data;
        int count = 0;
        int hashVal = hashFunc(key);
        int step = typeDetect == TYPE_DETECT_DOUBLE_HASH ? hashFunc2(key) : 0;
        while (array[hashVal] != null && array[hashVal].key != -1) {
            if (++count > arraySize) {
                expandArray(typeDetect);//数组满了后进行扩充
            }
            if (typeDetect == TYPE_DETECT_LINE) {
                // 线性探测
                ++hashVal;
            } else if (typeDetect == TYPE_DETECT_SECOND) {
                //二次探测：目前这样写会有问题，数组没有满时就会扩充,例如在磁盘存储数据,若用此方法就会导致磁盘没有被充分利用
                ++step;
                hashVal += step * step;
            } else if (typeDetect == TYPE_DETECT_DOUBLE_HASH) {
                hashVal += step;
            }
            System.out.print("防止溢出: ");
            hashVal = hashFunc(hashVal);//关键点
        }
        array[hashVal] = item;
    }

    private void expandArray(int typeDetect) {
        DataItem<Integer, T>[] temp = array;
        arraySize *= 2;
        array = new DataItem[arraySize];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null && temp[i].key != -1) {
                insert(temp[i].key, temp[i].data, typeDetect);
                System.out.print("expandArray: ");
                display();
            }
        }
    }

    public DataItem<Integer, T> find(int key) {
        return find(key, TYPE_DETECT_LINE);
    }

    public DataItem<Integer, T> find(int key, int typeDetect) {
        int count = 0;
        int hashVal = hashFunc(key);
        int step = typeDetect == TYPE_DETECT_DOUBLE_HASH ? hashFunc2(key) : 0;
        while (array[hashVal] != null) {
            if (++count > arraySize)
                return null;

            if (array[hashVal].key == key)
                return array[hashVal];
            if (typeDetect == TYPE_DETECT_LINE) {
                // 线性探测
                ++hashVal;
            } else if (typeDetect == TYPE_DETECT_SECOND) {
                //二次探测：目前这样写会有问题，数组没有满时就会扩充
                ++step;
                hashVal += step * step;
            } else if (typeDetect == TYPE_DETECT_DOUBLE_HASH) {
                hashVal += step;
            }
            hashVal = hashFunc(hashVal);//关键点
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
        return delete(key, TYPE_DETECT_LINE);
    }

    public DataItem<Integer, T> delete(int key, int typeDetect) {
        int count = 0;
        int hashVal = hashFunc(key);
        int step = typeDetect == TYPE_DETECT_DOUBLE_HASH ? hashFunc2(key) : 0;
        while (array[hashVal] != null) {
            if (++count > arraySize)
                return null;
            if (array[hashVal].key == key) {
                DataItem<Integer, T> temp = array[hashVal];
                array[hashVal] = nonItem;
                return temp;
            }
            if (typeDetect == TYPE_DETECT_LINE) {
                // 线性探测
                ++hashVal;
            } else if (typeDetect == TYPE_DETECT_SECOND) {
                //二次探测：目前这样写会有问题，数组没有满时就会扩充
                ++step;
                hashVal += step * step;
            } else if (typeDetect == TYPE_DETECT_DOUBLE_HASH) {
                hashVal += step;
            }
            hashVal = hashFunc(hashVal);//关键点
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
