package com.ljy.javacode.structure.queue;

/**
 * Created by ljy on 2018/5/31.
 *
 * 使用数组实现优先级队列
 *
 * 也可以用一种特别的树--堆 来实现
 */

public class PriorityArrayQueue {
    private int maxSize;
    private long[] queArray;
    private int nItems;

    public PriorityArrayQueue(int maxSize){
        this.maxSize=maxSize;
        queArray=new long[maxSize];
        nItems=0;
    }

    public void insert(long value){
        int j;
        if (nItems==0){
            queArray[nItems++]=value;
        }else {
            //插入排序
            for(j=nItems-1;j>=0;j--){
                if (value>queArray[j])
                    //从队尾开始，依次循环，把所有比新值小的都后移一位，把新值插入移动后空出的位置
                    //如此插入，实现了从大到小排序，又因为remove方法是从队尾开始，这样就实现了优先处理较小值的需求
                    queArray[j+1]=queArray[j];
                else
                    break;
            }
            queArray[j+1]=value;
            nItems++;
        }
    }

    public long remove(){
        return queArray[--nItems];
    }

    public long peekMin(){
        return queArray[nItems-1];
    }

    public boolean isFull(){
        return nItems==maxSize;
    }

    public boolean isEmpty(){
        return nItems==0;
    }

    /**
     * 打印数组
     */
    public void display() {
        System.out.println("Stacker.size: "+nItems);
        System.out.print("Stacker: ");
        if (nItems!=0) {
            for (int i = nItems-1; i>= 0; i--) {
                System.out.print("[" + i + "]-->" + queArray[i] + "  ");
            }
        }
        System.out.println();

    }

}
