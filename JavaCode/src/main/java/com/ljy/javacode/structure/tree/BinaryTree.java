package com.ljy.javacode.structure.tree;

import java.util.Stack;

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
            if (key < current.key) //go left
                current = current.leftChild;
            else //go right
                current = current.rightChild;
            if (current == null)
                return null;
        }
        return current;
    }

    @Override
    public void insert(Integer key, T data) {
        //创建新节点
        BinaryNode<Integer, T> newNode = new BinaryNode<>();
        newNode.key = key;
        newNode.data = data;
        // 寻找要插入的位置
        if (root == null)
            root = newNode;
        else {
            BinaryNode<Integer, T> current = root;
            BinaryNode<Integer, T> parent;
            while (true) {
                parent = current;
                if (key < current.key) {// go left
                    current = current.leftChild;
                    if (current == null) {
                        parent.leftChild = newNode;
                        return;
                    }
                } else {//go right
                    current = current.rightChild;
                    if (current == null) {
                        parent.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean delete(Integer key) {
        //记录要删除的节点
        BinaryNode<Integer, T> current = root;
        //记录要删除的节点的父节点
        BinaryNode<Integer, T> parent = root;
        //记录要删除的节点是其父节点的左子节点还是右子节点
        boolean isLeftChild = true;
        //寻找要删除的子节点
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
        //找到后,分情况处理
        //1. 没有子节点时
        if (current.leftChild == null && current.rightChild == null) {
            if (current == root)
                root = null;
            else if (isLeftChild)
                parent.leftChild = null;
            else
                parent.rightChild = null;
            //只有左子节点时
        } else if (current.rightChild == null) {
            if (current == root)
                root = current.leftChild;
            else if (isLeftChild)
                parent.leftChild = current.leftChild;
            else
                parent.rightChild = current.leftChild;
            //只有右子节点时
        } else if (current.leftChild == null) {
            if (current == root)
                root = current.rightChild;
            else if (isLeftChild)
                parent.leftChild = current.rightChild;
            else
                parent.rightChild = current.rightChild;
            //有两个子节点时,要寻找中序后继,代替要删除的节点(即设置后继子节点为要删除节点父节点的子节点)
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

    //删除有两个子节点的节点时,寻找中序后继点代替该节点
    //后继子节点:关键字值比该节点大且最接近该节点的子节点(右子树的最左叶子节点)
    private BinaryNode<Integer, T> getSuccessor(BinaryNode<Integer, T> delNode) {
        BinaryNode<Integer, T> successorParent = delNode;
        BinaryNode<Integer, T> successor = delNode;
        BinaryNode<Integer, T> current = delNode.rightChild;
        while (current != null) {//寻找最左叶子节点
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }
        if (successor != delNode.rightChild) {
            //将后继子节点与其父节点的边断开,并设置其右子节点为要删除节点的右子节点
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }

    //前/中/后序遍历
    public void traverse(int type) {
        switch (type) {
            case 1:
                System.out.print("前序遍历: ");
                preOrder(root);
                break;
            case 2:
                System.out.print("中序遍历: ");
                inOrder(root);
                break;
            case 3:
                System.out.print("后序遍历: ");
                postOrder(root);
                break;
        }
    }

    private void preOrder(BinaryNode<Integer, T> localRoot) {
        if (localRoot != null) {

            localRoot.display();
            System.out.print(" ");

            preOrder(localRoot.leftChild);
            preOrder(localRoot.rightChild);
        }

    }

    private void inOrder(BinaryNode<Integer, T> localRoot) {
        if (localRoot != null) {

            inOrder(localRoot.leftChild);

            localRoot.display();
            System.out.print(" ");

            inOrder(localRoot.rightChild);
        }
    }

    private void postOrder(BinaryNode<Integer, T> localRoot) {
        if (localRoot != null) {
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);

            localRoot.display();
            System.out.print(" ");
        }
    }

    public void display() {
        Stack<BinaryNode<Integer, T>> globalStack = new Stack<>();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        System.out.println("........................");
        while (!isRowEmpty) {
            Stack<BinaryNode<Integer, T>> localStack = new Stack<>();
            isRowEmpty = true;
            for (int i = 0; i < nBlanks; i++)
                System.out.print(" ");
            while (!globalStack.isEmpty()) {
                BinaryNode<Integer, T> temp = globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.key+"");
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);
                    if (temp.leftChild != null || temp.rightChild != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("-");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int i = 0; i < nBlanks * 2 - 2; i++)
                    System.out.print(" ");
            }
            System.out.println();
            nBlanks/=2;
            while (!localStack.isEmpty())
                globalStack.push(localStack.pop());
        }
        System.out.println("........................");
    }
}
