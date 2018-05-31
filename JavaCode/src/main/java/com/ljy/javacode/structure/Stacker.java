package com.ljy.javacode.structure;

/**
 * Created by ljy on 2018/5/31.
 *
 * 使用数组实现栈
 *
 * 后进先出 LIFO （last in first out）
 */

public class Stacker {
    private int maxSize;
    private String[] stackArray;
    private int top;

    public Stacker(int maxSize){
        this.maxSize=maxSize;
        stackArray=new String[maxSize];
        top=-1;
    }

    public void push(String value){
        stackArray[++top]=value;
    }

    public String pop(){
        return stackArray[top--];
    }

    public String peek(){
        return stackArray[top];
    }

    public boolean isEmpty(){
        return top==-1;
    }

    public boolean isFull(){
        return top==maxSize-1;
    }

    /**
     * 打印数组
     */
    public void display() {
        System.out.print("Stacker: ");
        for (int i = 0; i <=top; i++) {
            System.out.print("[" + i + "]-->" + stackArray[i] + "  ");
        }
        System.out.println();

    }

}
