package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/6 17:08
 * <p>
 * 二叉树
 */
public class BinaryTree<T> extends Tree<BinaryNode<Integer, T>, Integer, T> {


    @Override
    public BinaryNode<Integer, T> find(Integer key) {
        BinaryNode<Integer, T> current = root;
        while (!current.key.equals(key)) {
            if (key < current.key)
                current = current.leftChild;
            else
                current = current.rightChild;
            if (current == null)
                return null;
        }
        return current;
    }

    @Override
    public void insert(Integer key, T data) {
        BinaryNode<Integer, T> nNode = new BinaryNode<>();
        nNode.key = key;
        nNode.data = data;
        if (root == null)
            root = nNode;
        else {
            BinaryNode<Integer, T> current = root;
            BinaryNode<Integer, T> parent;
            while (true) {
                parent = current;
                if (key < current.key) {// go left
                    current = current.leftChild;
                    if (current == null) {
                        parent.leftChild = nNode;
                        return;

                    }
                } else {//go right
                    current = current.rightChild;
                    if (current == null) {
                        parent.rightChild = nNode;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean delete(Integer key) {
        BinaryNode<Integer, T> current = root;
        BinaryNode<Integer, T> parent = root;
        boolean isLeftChild = true;
        while (!current.key.equals(key)) {
            parent = current;
            if (key < current.key) {
                isLeftChild = true;
                current = current.leftChild;
            } else {
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null)
                return false;
        }
        if (current.leftChild == null && current.rightChild == null) {
            if (current == root)
                root = null;
            else if (isLeftChild)
                parent.leftChild = null;
            else
                parent.rightChild = null;
        } else if (current.rightChild == null) {
            if (current == root)
                root = current.leftChild;
            else if (isLeftChild)
                parent.leftChild = current.leftChild;
            else
                parent.rightChild = current.leftChild;
        } else if (current.leftChild == null) {
            if (current == root)
                root = current.rightChild;
            else if (isLeftChild)
                parent.leftChild = current.rightChild;
            else
                parent.rightChild = current.rightChild;
        } else {
            BinaryNode<Integer, T> successor = getSuccessor(current);
            if (current == root)
                root = successor;
            else if (isLeftChild)
                parent.leftChild = successor;
            else
                parent.rightChild = successor;
        }
        return true;
    }

    private BinaryNode<Integer, T> getSuccessor(BinaryNode<Integer, T> delNode) {
        BinaryNode<Integer, T> successorParent = delNode;
        BinaryNode<Integer, T> successor = delNode;
        BinaryNode<Integer, T> current = delNode.rightChild;
        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }
        if (successor != delNode.rightChild) {
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }
}
