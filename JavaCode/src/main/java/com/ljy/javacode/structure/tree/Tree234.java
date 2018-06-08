package com.ljy.javacode.structure.tree;

import com.ljy.javacode.structure.bean.DataItem;

/**
 * Created by LJY on 2018/6/7 21:58
 * <p>
 * 2-3-4树
 */
public class Tree234<K extends Comparable<K>, T> {
    private Node234<K, T> root;

    public Tree234() {
        root = new Node234<>();
    }

    /**
     * 从234树中查找数据
     *
     * @param key
     * @return
     */
    public int find(K key) {
        Node234<K, T> current = root;
        int childNumber;
        while (true) {
            if ((childNumber = current.findItem(key)) != -1)
                return childNumber;
            else if (current.isLeaf())
                return -1;
            else
                current = getNextChild(current, key);
        }
    }

    /**
     * 向234树中插入数据
     *
     * @param key  关键字值
     * @param data 数据
     *             用key+data合成数据项
     */
    public void insert(K key, T data) {
        DataItem<K, T> newData = new DataItem<>();
        newData.key = key;
        newData.data = data;
        Node234<K, T> current = root;
        while (true) {
            if (current.isFull()) {
                split(current);
                current = current.getParent();
                current = getNextChild(current, newData.key);
            } else if (current.isLeaf()) {
                break;
            } else {
                current = getNextChild(current, newData.key);
            }
        }
        current.insertItem(newData);
    }

    private Node234<K, T> getNextChild(Node234<K, T> node, K key) {
        int i;
        int numItems = node.getNumItems();
        for (i = 0; i < numItems; i++) {
            if (key.compareTo(node.getItem(i).key) < 0)
                return node.getChild(i);
        }
        return node.getChild(i);
    }

    //分裂节点, assumes node is full
    private void split(Node234<K, T> node) {
        DataItem<K, T> itemB, itemC;
        Node234<K, T> parent, child2, child3;
        int itemIndex;
        // remove items from node
        itemC = node.deleteItem();
        itemB = node.deleteItem();
        //remove child from node
        child2 = node.disconnectChild(2);
        child3 = node.disconnectChild(3);
        //make new node
        Node234<K, T> newRight = new Node234<>();
        if (node == root) {
            //if node is root, node not have parent, so new one as root and node's parent
            //分裂根
            root = new Node234<>();
            parent = root;
            root.connectChild(0, node);
        } else
            parent = node.getParent();

        //deal with parent
        //1.parent insert new dataItem
        itemIndex = parent.insertItem(itemB);
        //2.parent set new child node
        int n = parent.getNumItems();
        for (int i = n - 1; i > itemIndex; i--) {
            Node234<K, T> temp = parent.disconnectChild(i);
            parent.connectChild(i + 1, temp);
        }
        parent.connectChild(itemIndex + 1, newRight);
        //deal with newRight
        //将node分裂出来的数据项C和子节点设置给新的节点
        newRight.insertItem(itemC);
        newRight.connectChild(0, child2);
        newRight.connectChild(1, child3);
    }

    /**
     * 打印234树
     */
    public void display() {
        display(root, 0, 0);
    }

    private void display(Node234<K, T> node, int level, int childNumber) {
        System.out.print(" level=" + level + ", child=" + childNumber + ",");
        node.display();
        int numItems = node.getNumItems();
        for (int i = 0; i < numItems + 1; i++) {
            Node234<K, T> nextNode = node.getChild(i);
            if (nextNode != null)
                display(nextNode, level + 1, i);
            else
                return;
        }
    }

}
