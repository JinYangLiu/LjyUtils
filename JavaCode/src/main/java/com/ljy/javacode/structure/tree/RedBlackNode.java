package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/7 14:05
 *
 * 红黑树的节点
 */
public class RedBlackNode<K, T> extends Node<K, T> {
    public RedBlackNode<K, T> leftChild;//左子节点
    public RedBlackNode<K, T> rightChild;//右子节点
    public RedBlackNode<K, T> parentNode;//父节点
    public boolean color;

}
