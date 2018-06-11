package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/11 14:27
 * 一种完全二叉树 堆 的实现
 * 由于堆的特性:
 * 1 完全二叉树
 * 2 只要求沿着从根到叶子的每一条路径,节点都是有序的
 * 所以这里用数组实现堆,而不是之前实现二叉树是=时那样用引用,其结构如下:
 * {A , A1,A2, A11,A12,A21,A22, A111,A112,A121,A122,A211,A212,A221,A222, ...}
 * 每层节点最多个数依次为: 1,2,4,8,16, ...
 */
public class Heap<K extends Comparable<K>, T> {
    private Node<K, T>[] heapArray;
    private int maxSize;
    private int currentSize;

    public Heap(int maxSize) {
        this.maxSize = maxSize;
        currentSize = 0;
        heapArray = new Node[maxSize];
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * 插入数据
     */
    public boolean insert(K key, T value) {
        if (currentSize == maxSize)
            return false;
        Node<K, T> newNode = new Node<>();
        newNode.key = key;
        newNode.data = value;
        heapArray[currentSize] = newNode;
        trickleUp(currentSize++);
        return true;
    }

    //移动
    private void trickleUp(int index) {
        int parent = (index - 1) / 2;
        Node<K, T> bottom = heapArray[index];
        while (index > 0 && heapArray[parent].key.compareTo(bottom.key) < 0) {
            heapArray[index] = heapArray[parent];
            index = parent;
            parent = (parent - 1) / 2;
        }
        heapArray[index] = bottom;
    }

    /**
     * 删除
     */
    public Node<K, T> remove() {
        Node<K, T> root = heapArray[0];
        heapArray[0] = heapArray[--currentSize];
        trickleDown(0);
        return root;
    }

    //移动
    private void trickleDown(int index) {
        int largerChild;
        Node<K, T> top = heapArray[index];
        while (index < currentSize / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            if (rightChild < currentSize &&
                    heapArray[leftChild].key.compareTo(heapArray[rightChild].key) < 0)
                largerChild = rightChild;
            else
                largerChild = leftChild;

            if (top.key.compareTo(heapArray[largerChild].key) >= 0)
                break;
            heapArray[index] = heapArray[largerChild];
            index = largerChild;
        }
        heapArray[index] = top;
    }

    public boolean change(int index, K newKey, T newValue) {
        if (index < 0 || index >= currentSize)
            return false;
        K oldKey = heapArray[index].key;
        heapArray[index].key = newKey;
        heapArray[index].data = newValue;
        if (oldKey.compareTo(newKey) < 0)
            trickleUp(index);
        else
            trickleDown(index);
        return true;
    }

    public void display() {
        System.out.println("Heap:");
        for (int i = 0; i < currentSize; i++) {
            if (heapArray[i] != null) {
                System.out.print(i + "-->");
                heapArray[i].display();
            } else {
                System.out.print("-- ");
            }
            System.out.println();
        }
    }

}
