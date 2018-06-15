package com.ljy.javacode.structure.stack;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Created by ljy on 2018/5/31.
 *
 * 使用数组实现栈
 *
 * 栈也可以用链表来实现
 *
 * 后进先出 LIFO （last in first out）
 */

public class ArrayStack<T> extends Stack<T> {

    protected int maxSize;
    protected int top;
    private T[] stackArray;

    public ArrayStack(int maxSize){
        stackArray= (T[]) new Object[maxSize];
        this.maxSize=maxSize;
        top=-1;
    }

    @Override
    public boolean isEmpty(){
        return top==-1;
    }

    public int size(){
        return top+1;
    }

    @Override
    public void push(T value){
        ensureCapacity();//扩充数组
        stackArray[++top]=value;
    }

    private void ensureCapacity() {
        if (stackArray.length==top+1)
            stackArray= Arrays.copyOf(stackArray,2*stackArray.length+1);
    }

    @Override
    public T pop(){
        if (top<0)
//            throw new EmptyStackException();
            return null;
        T temp=stackArray[top];
        stackArray[top]=null;//防止内存泄漏
        top--;
        return temp;
    }

    @Override
    public T peek(){
        if (top<0)
            return null;
        return stackArray[top];
    }


    /**
     * 打印数组
     */
    @Override
    public void display() {
        System.out.print("ArrayStacker: ");
        for (int i = 0; i <=top; i++) {
            System.out.print("[" + i + "]-->" + stackArray[i] + "  ");
        }
        System.out.println();

    }

}
