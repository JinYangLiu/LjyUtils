package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/7 14:11
 * <p>
 * 红黑树类
 */
public class RedBlackTree<K extends Comparable<K>, T> extends Tree<RedBlackNode<K, T>, K, T> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private RedBlackNode<K, T> parentOf(RedBlackNode<K, T> node) {
        return node == null ? null : node.parentNode;
    }

    private boolean colorOf(RedBlackNode<K, T> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(RedBlackNode<K, T> node) {
        return node != null && node.color == RED;
    }

    private boolean isBlack(RedBlackNode<K, T> node) {
        return !isRed(node);
    }

    private void setRed(RedBlackNode<K, T> node) {
        if (node != null)
            node.color = RED;
    }

    private void setBlack(RedBlackNode<K, T> node) {
        if (node != null)
            node.color = BLACK;
    }

    private void setParent(RedBlackNode<K, T> node, RedBlackNode<K, T> nodeParent) {
        if (node != null)
            node.parentNode = nodeParent;
    }

    private void setColor(RedBlackNode<K, T> node, boolean color) {
        if (node != null)
            node.color = color;
    }


    /**
     * 前序遍历红黑树
     */
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(RedBlackNode<K, T> currentNode) {
        if (currentNode != null) {
            System.out.print(currentNode.key + " ");
            preOrder(currentNode.leftChild);
            preOrder(currentNode.rightChild);
        }
    }

    /**
     * 中序遍历红黑树
     */
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(RedBlackNode<K, T> currentNode) {
        if (currentNode != null) {
            inOrder(currentNode.leftChild);
            System.out.print(currentNode.key + " ");
            inOrder(currentNode.rightChild);
        }
    }

    /**
     * 后序遍历红黑树
     */
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(RedBlackNode<K, T> currentNode) {
        if (currentNode != null) {
            postOrder(currentNode.leftChild);
            postOrder(currentNode.rightChild);
            System.out.print(currentNode.key + " ");
        }
    }

    /**
     * 查找最小节点
     */
    public RedBlackNode<K, T> getMin() {
        return getMin(root);
    }

    private RedBlackNode<K, T> getMin(RedBlackNode<K, T> current) {
        RedBlackNode<K, T> temp = current;
        if (temp == null)
            return null;
        while (temp.leftChild != null)
            temp = temp.leftChild;
        return temp;
    }

    /**
     * 查找最大节点
     */
    public RedBlackNode<K, T> getMax() {
        return getMax(root);
    }

    public RedBlackNode<K, T> getMax(RedBlackNode<K, T> current) {
        RedBlackNode<K, T> temp = current;
        if (temp == null)
            return null;
        while (temp.rightChild != null)
            temp = temp.rightChild;
        return temp;
    }

    /**
     * 寻找当前节点的后继节点, 即关键字大于该节点的最小节点, 右子树的最左叶子节点
     */
    public RedBlackNode<K, T> getSuccessor(RedBlackNode<K, T> node) {
        RedBlackNode<K, T> current = node;
        if (current.rightChild != null)
            return getMin(current.rightChild);
        //若current没有右子节点,则有以下两种可能：
        //1. current是"一个左孩子"，则"current的后继结点"为 "它的父结点"。
        //2. current是"一个右孩子"，则查找"current的最低的父结点，并且该父结点要具有左孩子"，
        // 找到的这个"最低的父结点"就是"current的后继结点"
        RedBlackNode<K, T> parent = current.parentNode;
        while (parent != null && current == parent.rightChild) {
            current = parent;
            parent = parent.parentNode;
        }
        return parent;
    }

    /**
     * 寻找当前节点的前驱节点, 即关键字小于该节点的最大节点, 左子树的最右叶子节点
     */
    public RedBlackNode<K, T> getPredecessor(RedBlackNode<K, T> node) {
        RedBlackNode<K, T> current = node;
        if (current.leftChild != null)
            return getMax(current.leftChild);
        RedBlackNode<K, T> parent = current.parentNode;
        while (parent != null && current == parent.leftChild) {
            current = parent;
            parent = parent.parentNode;
        }
        return parent;
    }

    /**
     * 左旋
     */
    private void leftRotate(RedBlackNode<K, T> current) {
        RedBlackNode<K, T> rightChild = current.rightChild;
        //1. 用current的右子节点的左子节点代替current的右子节点, 与current建立连接(边)
        //连接是双向的,这里就是指的引用,所以分两步进行
        //1.1 将current右子节点的左子节点设为current的右子节点
        current.rightChild = rightChild.leftChild;
        //1.2 若current右子节点的左子节点不为空,
        //则将current右子节点的左子节点的父节点设为current
        if (rightChild.leftChild != null)
            rightChild.leftChild.parentNode = current;
        //2.current右子节点代替current,与current的父节点建立边
        //2.1 将current的父节点设为current右子节点的父节点
        rightChild.parentNode = current.parentNode;
        //2.2
        if (current.parentNode == null)
            //若current没有父节点,则说明current是根节点,那么就将current的右子节点代替current作为根节点
            root = rightChild;
        else {
            //若current有父节点,则将current右子节点代替current,设为current父节点的子节点
            if (current.parentNode.leftChild == current)
                current.parentNode.leftChild = rightChild;
            else
                current.parentNode.rightChild = rightChild;
        }
        //3. 用current代替current的右子节点的左子节点
        // (由于1中的操作,rightChild的左子节点被搞走了,出现了个坑)
        // (由于2中的操作,current被rightChild代替了,current没地方去了,多了个萝卜)
        // (正好把萝卜放坑里)
        //3.1
        rightChild.leftChild = current;
        //3.2
        current.parentNode = rightChild;
    }

    /**
     * 右旋
     */
    private void rightRotate(RedBlackNode<K, T> current) {
        RedBlackNode<K, T> leftChild = current.leftChild;
        //1.1
        current.leftChild = leftChild.rightChild;
        //1.2
        if (leftChild.rightChild != null)
            leftChild.rightChild.parentNode = current;
        //2.1
        leftChild.parentNode = current.parentNode;
        //2.2
        if (current.parentNode == null)
            root = leftChild;
        else {
            if (current.parentNode.rightChild == current)
                current.parentNode.rightChild = leftChild;
            else
                current.parentNode.leftChild = leftChild;
        }
        //3.1
        leftChild.rightChild = current;
        //3.2
        current.parentNode = leftChild;
    }

    /**
     * 根据关键字查找节点
     *
     * @param key
     * @return
     */
    @Override
    public RedBlackNode<K, T> find(K key) {
        return find(root, key);
    }

    private RedBlackNode<K, T> find(RedBlackNode<K, T> current, K key) {
        if (current == null)
            return null;
        int cmp = key.compareTo(current.key);
        if (cmp < 0)
            return find(current.leftChild, key);
        else if (cmp > 0)
            return find(current.rightChild, key);
        else
            return current;
    }


    /**
     * 插入节点
     *
     * @param key
     * @param data
     */
    @Override
    public void insert(K key, T data) {
        RedBlackNode<K, T> local = root;
        RedBlackNode<K, T> parent = null;
        RedBlackNode<K, T> newNode = new RedBlackNode<>();
        newNode.key = key;
        newNode.data = data;
        int cmp;
        //1. 插入节点到二叉树
        while (local != null) {
            parent = local;
            cmp = newNode.key.compareTo(local.key);
            if (cmp < 0)
                local = local.leftChild;
            else
                local = local.rightChild;
        }
        newNode.parentNode = parent;
        if (parent != null) {
            cmp = newNode.key.compareTo(parent.key);
            if (cmp < 0)
                parent.leftChild = newNode;
            else
                parent.rightChild = newNode;
        } else {
            root = newNode;
        }
        //2. 设置新节点为红色
        newNode.color = RED;
        //3.修正
        insertFix(newNode);
    }

    /**
     * 插入修正
     * 向红黑树中插入节点后,树失去平衡,要调用此函数修正
     * 目的:重新塑造一颗红黑树
     *
     * @param current
     */
    private void insertFix(RedBlackNode<K, T> current) {
        RedBlackNode<K, T> parent, gparent;
        //若父节点存在,且父节点为红色
        while ((parent = parentOf(current)) != null && isRed(parent)) {
            gparent = parentOf(parent);
            //若父节点是祖父节点的左子节点
            if (parent == gparent.leftChild) {
                RedBlackNode<K, T> uncle = gparent.rightChild;
                //若祖父节点的右子节点(叔父)存在且为红色
                if (uncle != null && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    current = gparent;
                } else {
                    //若叔父节点是黑色
                    //若当前节点是父节点的右子节点
                    if (parent.rightChild == current) {
                        RedBlackNode<K, T> temp;
                        leftRotate(parent);
                        temp = parent;
                        parent = current;
                        current = temp;
                    }
                    setBlack(parent);
                    setRed(gparent);
                    rightRotate(gparent);
                }
            } else {
                //若父节点是祖父节点的右子节点
                //叔父节点为祖父节点的左子节点
                RedBlackNode<K, T> uncle = gparent.leftChild;
                //若叔父节点存在且是红色
                if (uncle != null && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    current = gparent;
                } else {
                    //若叔父节点是黑色
                    //若当前节点是父节点的左子节点
                    if (parent.leftChild == current) {
                        RedBlackNode<K, T> temp;
                        rightRotate(parent);
                        temp = parent;
                        parent = current;
                        current = temp;
                    }
                    setBlack(parent);
                    setRed(gparent);
                    leftRotate(gparent);
                }
            }
        }
        //将根节点设为黑色
        setBlack(root);
    }

    /**
     * 删除指定关键字值的节点
     *
     * @param key
     * @return
     */
    @Override
    public boolean delete(K key) {
        boolean color;
        RedBlackNode<K, T> child, parent;
        RedBlackNode<K, T> node = find(key);
        if (node == null)
            return false;
        //若待删除节点的左右子节点都不为空
        if (node.leftChild != null && node.rightChild != null) {
            //用后续子节点代替
            RedBlackNode<K, T> successor = node;
            successor = successor.rightChild;
            while (successor.leftChild != null)
                successor = successor.leftChild;

            if (parentOf(successor) != null) {
                //successor不是根节点
                if (parentOf(node).leftChild == node)
                    parentOf(node).leftChild = successor;
                else
                    parentOf(node).rightChild = successor;
            }
            child = successor.rightChild;
            parent = parentOf(successor);
            color = colorOf(successor);
            if (parent == node) {
                //待删除节点是后继节点的父节点
                parent = successor;
            } else {
                if (child != null)
                    setParent(child, parent);
                parent.leftChild = child;
                successor.rightChild = node.rightChild;
                setParent(node.rightChild, successor);
            }
            successor.parentNode = node.parentNode;
            successor.color = node.color;
            successor.leftChild = node.leftChild;
            node.leftChild.parentNode = successor;
            if (color == BLACK)
                deleteFix(child, parent);
            node = null;
            return true;
        } else {
            if (node.leftChild != null)
                child = node.leftChild;
            else
                child = node.rightChild;
            parent = node.parentNode;
            color = node.color;
            if (child != null)
                child.parentNode = parent;
            if (parent != null) {
                if (parent.leftChild == node)
                    parent.leftChild = child;
                else
                    parent.rightChild = child;
            } else {
                root = child;
            }
            if (color = BLACK)
                deleteFix(child, parent);
            node = null;
            return true;
        }
    }


    /**
     * 删除修正
     *
     * @param node 待修正的节点
     */
    private void deleteFix(RedBlackNode<K, T> node, RedBlackNode<K, T> parent) {
        RedBlackNode<K, T> brother;//node的兄弟节点
        while ((node == null || isBlack(node)) && (node != root)) {
            if (parent.leftChild == node) {
                brother = parent.rightChild;
                if (isRed(brother)) {
                    //若兄弟节点是红色
                    setBlack(brother);
                    setRed(parent);
                    leftRotate(parent);
                    brother = parent.rightChild;
                }
                if (brother==null) {
                    System.out.println("----- brother == null ------");
                    break;
                }else {
                    System.out.println("----- brother != null ------");
                }
                //此时兄弟节点肯定是黑色了
                if ((brother.leftChild == null || isBlack(brother.leftChild))
                        && (brother.rightChild == null || isBlack(brother.rightChild))) {
                    //若兄弟节点的两个子节点也是黑色
                    setRed(brother);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    //若兄弟节点的两个子节点不都是黑色
                    if (brother.rightChild == null || isBlack(brother.rightChild)) {
                        //若兄弟节点的左子节点是红色, 右子节点是黑色
                        setBlack(brother.leftChild);
                        setRed(brother);
                        rightRotate(brother);
                        brother = parent.rightChild;
                    }
                    //若兄弟节点的右子节点是红色,左子节点任意
                    setColor(brother, colorOf(parent));
                    setBlack(parent);
                    setBlack(brother.rightChild);
                    leftRotate(parent);
                    node = root;
                    break;
                }
            } else {
                brother = parent.leftChild;
                if (isRed(brother)) {
                    setBlack(brother);
                    setRed(parent);
                    rightRotate(parent);
                    brother = parent.leftChild;
                }
                if (brother==null) {
                    System.out.println("----- brother == null ------");
                    break;
                }else {
                    System.out.println("----- brother != null ------");
                }
                if ((brother.leftChild == null || isBlack(brother.leftChild))
                        && (brother.rightChild == null || isBlack(brother.rightChild))) {
                    setRed(brother);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    if (brother.leftChild == null || isBlack(brother.leftChild)) {
                        setBlack(brother.rightChild);
                        setRed(brother);
                        leftRotate(brother);
                        brother = parent.leftChild;
                    }
                    setColor(brother, colorOf(parent));
                    setBlack(parent);
                    setBlack(brother.leftChild);
                    rightRotate(parent);
                    node = root;
                    break;
                }
            }
        }
        if (node != null)
            setBlack(node);
    }

    /**
     * 销毁红黑树
     */
    public void destroy() {
        destroy(root);
        root = null;
    }

    private void destroy(RedBlackNode<K, T> node) {
        if (node == null)
            return;
        destroy(node.leftChild);
        destroy(node.rightChild);
        node = null;
    }

    private void display(RedBlackNode<K, T> node, K key, int direction) {
        if (node != null) {
            if (direction == 0)
                // node是根节点
                System.out.printf("%2d(B) is root\n", node.key);
            else
                // node是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n",
                        node.key, isRed(node) ? "R" : "B", key, direction == 1 ? "right" : "left");
            display(node.leftChild, node.key, -1);
            display(node.rightChild, node.key, 1);
        }
    }

    public void display() {
        if (root != null)
            display(root, root.key, 0);
    }


    public void traverse(int type) {
        switch (type) {
            case 1:
                System.out.print("前序遍历: ");
                preOrder();
                break;
            case 2:
                System.out.print("中序遍历: ");
                inOrder();
                break;
            case 3:
                System.out.print("后序遍历: ");
                postOrder();
                break;
        }
    }
}
