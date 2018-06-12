package com.ljy.javacode.structure.queue;

/**
 * Created by ljy on 2018/5/31.
 *
 * 使用数组实现队列
 *
 * 先进先出 FIFO （first in first out）
 *
 * 除了普通的队列，还有
 * 1。 双端队列：
 *  指允许两端都可以进行入队和出队操作的队列，其元素的逻辑结构仍是线性结构
 *  四个方法insertLeft(),removeLeft(),insertRight(),removeRight()
 *  如果禁止insertLeft(),removeLeft()（或right）双端队列的功能就和栈一样了
 *  如果禁止removeLeft(),insertRight()（或另两个）双端队列的功能就和队列一样了
 * 2。 优先级队列：
 *  如果我们给每个元素都分配一个数字来标记其优先级，不妨设较小的数字具有较高的优先级，
 *  这样我们就可以在一个集合中访问优先级最高的元素并对其进行查找和删除操作了。
 *  这样，我们就引入了 优先级队列 这种数据结构。
 *  优先级队列的实现见：PriorityQueue.java
 */

public class ArrayQueue<T> extends Queue<T> {
    private int maxSize;
    private T[] queArray;
    private int front;//队列首
    private int rear;//队列尾
    private int nItems;

    public ArrayQueue(int maxSize){
        this.maxSize=maxSize;
        queArray=(T[]) new Object[maxSize];
        front=0;
        rear=-1;
        nItems=0;
    }

    @Override
    public void insert(T value){
        if (rear==maxSize-1)
            rear=-1;
        queArray[++rear]=value;
        nItems++;
    }

    @Override
    public T remove(){
        T temp=queArray[front++];
        if (front==maxSize)
            front=0;
        nItems--;
        return temp;
    }

    public T peekFront(){
        return queArray[front];
    }

    public int size(){
        return nItems;
    }

    @Override
    public boolean isEmpty(){
        return nItems==0;
    }

    public boolean isFull(){
        return nItems==maxSize;
    }

    /**
     * 打印数组
     */
    @Override
    public void display() {
        System.out.println("ArrayQueue.size: "+size());
        System.out.print("ArrayQueue: ");
        if (size()!=0) {
            for (int i = front; i <= rear; i++) {
                System.out.print("[" + i + "]-->" + queArray[i] + "  ");
            }
        }
        System.out.println();

    }


}
