package com.ljy.javacode.structure.queue;

import java.lang.reflect.Array;

/**
 * Created by ljy on 2018/5/31.
 * <p>
 * 使用数组实现优先级队列
 * <p>
 * 也可以用一种特别的树--堆 来实现
 */

public class PriorityArrayQueue <T extends Comparable<T>> {
    private int maxSize;
    protected T[] queArray;
    protected int size;

    public PriorityArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        //Java 创建泛型类型的数组方法1:
        queArray = (T[]) new Object[maxSize];
        size = 0;
    }
    public PriorityArrayQueue(int maxSize,T[] a) {
        this.maxSize = maxSize;
//        queArray = (T[]) new Object[maxSize];
        //由于PriorityArrayQueue <T extends Comparable<T>>,
        // 所以上面这条语句是错的，因为不能把Object数组造型为Comparable数组。
        //Java 创建泛型类型的数组方法2:
        queArray= (T[]) Array.newInstance(a.getClass().getComponentType(), a.length);
        size = 0;
    }

    /**
     * 插入数据
     *
     * @param value
     */
    public void insert(T value) {
        int j;
        if (size == 0) {
            queArray[size++] = value;
        } else {
            //插入排序
            for (j = size - 1; j >= 0; j--) {
                if (value.compareTo(queArray[j]) > 0)
                    //从队尾开始，依次循环，把所有比新值小的都后移一位，把新值插入移动后空出的位置
                    //如此插入，实现了从大到小排序，又因为remove方法是从队尾开始，这样就实现了优先处理较小值的需求
                    queArray[j + 1] = queArray[j];
                else
                    break;
            }
            queArray[j + 1] = value;
            size++;
        }
    }

    /**
     * 删除最小值
     *
     * @return
     */
    public T removeMin() {
        return queArray[--size];
    }

    /**
     * 删除指定下标元素
     *
     * @param index
     */
    public void remove(int index) {
        for (int i = index; i < size-1; i++) {
            queArray[i] = queArray[i + 1];
            size--;
        }
    }

    /**
     * 查看最小
     *
     * @return
     */
    public T peekMin() {
        return queArray[size - 1];
    }

    /**
     * 查看指定
     *
     * @param index
     * @return
     */
    public T peek(int index) {
        return queArray[index];
    }

    /**
     * 查找指定值
     */
    public int find(T value) {
        for (int i = 0; i < size; i++)
            if (queArray[i].compareTo(value) == 0)
                return i;
        return -1;
    }


    /**
     * 是否满了
     *
     * @return
     */
    public boolean isFull() {
        return size == maxSize;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 队列长度
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 打印数组
     */
    public void display() {
        System.out.println("Stacker.size: " + size);
        System.out.print("Stacker: ");
        if (size != 0) {
            for (int i = size - 1; i >= 0; i--) {
                System.out.print("[" + i + "]-->" + queArray[i] + "  ");
            }
        }
        System.out.println();

    }

}
