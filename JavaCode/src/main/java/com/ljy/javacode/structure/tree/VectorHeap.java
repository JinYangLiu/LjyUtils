package com.ljy.javacode.structure.tree;

import java.util.Vector;

/**
 * Created by LJY on 2018/6/11 15:52
 */
public class VectorHeap<K extends Comparable<K>, T> {
    private Vector<Node<K, T>> heapArray;

    public VectorHeap() {
        heapArray = new Vector<>();
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return heapArray.size() == 0;
    }

    /**
     * 插入数据
     */
    public boolean insert(K key, T value) {
        Node<K, T> newNode = new Node<>();
        newNode.key = key;
        newNode.data = value;
        heapArray.add( newNode);
        trickleUp(heapArray.size()-1);
        return true;
    }

    //移动
    private void trickleUp(int index) {
        int parent = (index - 1) / 2;
        Node<K, T> bottom = heapArray.get(index);
        while (index > 0 && heapArray.get(parent).key.compareTo(bottom.key) < 0) {
            heapArray.set(index, heapArray.get(parent));
            index = parent;
            parent = (parent - 1) / 2;
        }
        heapArray.set(index,bottom);
    }

    /**
     * 删除
     */
    public Node<K, T> remove() {
        Node<K, T> root = heapArray.get(0);
        heapArray.set(0, heapArray.get(heapArray.size()-1));
        trickleDown(0);
        return root;
    }

    //移动
    private void trickleDown(int index) {
        int largerChild;
        Node<K, T> top = heapArray.get(index);
        while (index < heapArray.size() / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            if (rightChild < heapArray.size() &&
                    heapArray.get(leftChild).key.compareTo(heapArray.get(rightChild).key) < 0)
                largerChild = rightChild;
            else
                largerChild = leftChild;

            if (top.key.compareTo(heapArray.get(largerChild).key) >= 0)
                break;
            heapArray.set(index,heapArray.get(largerChild));
            index = largerChild;
        }
        heapArray.set(index,top);
    }

    public boolean change(int index, K newKey, T newValue) {
        if (index < 0 || index >= heapArray.size())
            return false;
        K oldKey = heapArray.get(index).key;
        heapArray.get(index).key = newKey;
        heapArray.get(index).data = newValue;
        if (oldKey.compareTo(newKey) < 0)
            trickleUp(index);
        else
            trickleDown(index);
        return true;
    }

    public void display() {
        System.out.println("Heap:");
        for (int i = 0; i < heapArray.size(); i++) {
            if (heapArray.get(i) != null) {
                System.out.print(i + "-->");
                heapArray.get(i).display();
            } else {
                System.out.print("-- ");
            }
            System.out.println();
        }
    }

}

