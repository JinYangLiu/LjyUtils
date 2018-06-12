package com.ljy.javacode.structure;

import com.ljy.javacode.structure.graph.Digraph;
import com.ljy.javacode.structure.graph.Graph;
import com.ljy.javacode.structure.tree.Node;
import com.ljy.javacode.structure.tree.VectorHeap;
import com.ljy.javacode.util.ArrayUtil;
import com.ljy.javacode.structure.array.OrderedArray;
import com.ljy.javacode.structure.bean.DataItem;
import com.ljy.javacode.structure.hash.HashTable;
import com.ljy.javacode.structure.hash.LinkedHashTable;
import com.ljy.javacode.structure.link.DoublyLinkList;
import com.ljy.javacode.structure.link.FirstLastLinkList;
import com.ljy.javacode.structure.link.Link;
import com.ljy.javacode.structure.link.LinkIterator;
import com.ljy.javacode.structure.link.LinkList;
import com.ljy.javacode.structure.queue.PriorityArrayQueue;
import com.ljy.javacode.structure.queue.ArrayQueue;
import com.ljy.javacode.structure.stack.ArrayStack;
import com.ljy.javacode.structure.tree.BinaryTree;
import com.ljy.javacode.structure.tree.RedBlackTree;
import com.ljy.javacode.structure.tree.Tree234;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by ljy on 2018/5/30.
 */

public class MainTest {
    public static void main(String[] args) {
        System.out.print("请输入要执行的方法名：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            switch (param) {
                case "ordered":
                    //测试有序数组，主要是二分法查找
                    testOrderedArray();
                    break;
                case "sort":
                    //测试排序
                    testSort();
                    break;
                case "stack":
                    //测试自己用数组实现的栈
                    //简单实例：单词逆序
                    testStack();
                    break;
                case "queue":
                    //测试自己用数组实现的队列
                    testQueue();
                    break;
                case "priorityQueue":
                    //测试优先级队列
                    testPriorityQueue();
                    break;
                case "linkList":
                    //测试单链表
                    testLinkList();
                    break;
                case "fllist":
                    //测试双端链表
                    testFirstLastLinkList();
                case "doublyLinkList":
                    //测试双向链表
                    testDoublyLinkList();
                    break;
                case "linkIterator":
                    testLinkIterator();
                    break;
                case "trangle":
                    //用循环和递归两种方法实现获取三角数字第n项的算法
                    testTrangle();
                    break;
                case "factorial":
                    //用递归实现阶乘计算
                    testFactorial();
                    break;
                case "hanoi":
                    //用递归解决汉诺塔问题
                    testHanoiTower();
                    break;
                case "mergerSort":
                    //测试归并排序
                    testMergerSort();
                    break;
                case "shellSort":
                    //测试希尔排序
                    testShellSort();
                    break;
                case "quickSort":
                    //测试快速排序
                    testQuickSort();
                    break;
                case "binaryTree":
                    //测试二叉树
                    testBinaryTree();
                    break;
                case "redBlackTree":
                    //测试红黑树
                    testRedBlackTree();
                    break;
                case "234Tree":
                    //测试234树
                    test234Tree();
                    break;
                case "HashTable":
                    //测试哈希表
                    testHashTable();
                    break;
                case "LinkedHashTable":
                    //测试哈希表 = 链地址法 + 有序链表
                    testLinkedHashTable();
                    break;
                case "Heap":
                    //测试堆
                    testHeap();
                    break;
                case "heapSort":
                    //测试堆排序
                    testHeapSort();
                    break;
                case "graph":
                    //测试无向无权图
                    testGraph();
                    break;
                case "topoSort":
                    //测试有向无环图的拓扑排序
                    testTopoSort();
                    break;
                default:
                    break;
            }
        }
    }

    private static void testTopoSort() {
        Digraph<Character> digraph=new Digraph<>();
        digraph.addVertex('A');
        digraph.addVertex('B');
        digraph.addVertex('C');
        digraph.addVertex('D');
        digraph.addVertex('E');
        digraph.addVertex('F');
        digraph.addVertex('G');
        digraph.addVertex('H');

        digraph.addEdge(0,3);//AD
        digraph.addEdge(0,4);//AE
        digraph.addEdge(1,4);//BE
        digraph.addEdge(2,5);//CF
        digraph.addEdge(3,6);//DG
        digraph.addEdge(4,6);//EG
        digraph.addEdge(5,7);//FH
        digraph.addEdge(6,7);//GH
        digraph.topoSort();
        System.out.println(" ---------- display ----------");
        digraph.display();
        System.out.println("warshall.....");
        digraph.warshall();
        System.out.println(" ---------- display ----------");
        digraph.display();
    }

    private static void testGraph() {
        Graph<Character> graph = new Graph<>();
        graph.addVertex('A');//0 ,start for dfs
        graph.addVertex('B');//1
        graph.addVertex('C');//2
        graph.addVertex('D');//3
        graph.addVertex('E');//4

        graph.addEdge(0, 1);//AB
        graph.addEdge(1, 2);//BC
        graph.addEdge(0, 3);//AD
        graph.addEdge(3, 4);//DE

        //上图结构可以如下表示:
        //    B -- C
        //  /
        //A
        //  \
        //    D -- E

        System.out.println("Graph:");
        System.out.print("深度优先搜索:");
        graph.displayDFS();
        System.out.println();
        System.out.print("广度优先搜索:");
        graph.displayBFS();
        System.out.println();
        graph.addEdge(0, 1);//AB
        graph.addEdge(0, 2);//AC
        graph.addEdge(0, 3);//AD
        graph.addEdge(0, 4);//AE
        graph.addEdge(1, 2);//BC
        graph.addEdge(1, 3);//BD
        graph.addEdge(1, 4);//BE
        graph.addEdge(2, 3);//CD
        graph.addEdge(2, 4);//CE
        graph.addEdge(3, 4);//DE
        System.out.print("最小生成树:");
        graph.mst();
        System.out.println();

    }

    private static void testHeapSort() {
        int[] array = new int[30];
        for (int i = 0; i < array.length; i++) {
            int value = (int) (Math.random() * 128);
            array[i] = value;
        }
        ArrayUtil.getInstance().heapSort(array);
    }

    private static void testHeap() {
//        Heap<Integer, String> heap = new Heap<>(30);
        VectorHeap<Integer, String> heap = new VectorHeap<>();
        for (int i = 0; i < 28; i++) {
            int key = (int) (10 + Math.random() * 128);
            System.out.println("insert:" + key);
            String data = "data_" + key;
            heap.insert(key, data);
            System.out.println("---------- display ----------");
            heap.display();
        }
        int k = 208;
        System.out.println("insert:" + k);
        heap.insert(k, "data-" + k);
        System.out.println(" ---------- display ----------");
        heap.display();
        System.out.println("change:3, 111, \"change_111\"");
        heap.change(3, 111, "change_111");
        System.out.println(" ---------- display ----------");
        heap.display();
        System.out.print("delete:");

        Node<Integer, String> dataD = heap.remove();
        if (dataD != null)
            dataD.display();
        else
            System.out.println("null");
        System.out.println("\n ---------- display ----------");
        heap.display();
    }

    private static void testLinkedHashTable() {
        LinkedHashTable hashTable = new LinkedHashTable(10);
        for (int i = 0; i < 28; i++) {
            int key = (int) (10 + Math.random() * 128);
            System.out.println("insert:" + key);
            hashTable.insert(key);
            System.out.println("\n ---------- display ----------");
            hashTable.display();
        }
        int k = 208;
        System.out.println("insert:" + k);
        hashTable.insert(k);
        System.out.println("\n ---------- display ----------");
        hashTable.display();
        System.out.print("find:");
        Link<Integer> dataF = hashTable.find(k);
        if (dataF != null)
            dataF.display();
        else
            System.out.println("null");
        System.out.println("\n ---------- display ----------");
        hashTable.display();
        System.out.print("delete:");

        Link<Integer> dataD = hashTable.delete(k);
        if (dataD != null)
            dataD.display();
        else
            System.out.println("null");
        System.out.println("\n ---------- display ----------");
        hashTable.display();
    }

    private static void testHashTable() {
        HashTable<String> hashTable = new HashTable<>();
        for (int i = 0; i < 28; i++) {
            int key = (int) (10 + Math.random() * 128);
            String data = "data_" + key;
            System.out.println("insert:" + key);
            hashTable.insert(key, data, HashTable.TYPE_DETECT_DOUBLE_HASH);
            System.out.println("\n ---------- display ----------");
            hashTable.display();
        }

        int k = 208;
        System.out.println("insert:" + k);
        hashTable.insert(k, "data-" + k, HashTable.TYPE_DETECT_DOUBLE_HASH);
        System.out.println("\n ---------- display ----------");
        hashTable.display();
        System.out.print("find:");
        DataItem<Integer, String> dataF = hashTable.find(k, HashTable.TYPE_DETECT_DOUBLE_HASH);
        if (dataF != null)
            dataF.display();
        else
            System.out.println("null");
        System.out.println("\n ---------- display ----------");
        hashTable.display();
        System.out.print("delete:");
        DataItem<Integer, String> dataD = hashTable.delete(k, HashTable.TYPE_DETECT_DOUBLE_HASH);
        if (dataD != null)
            dataD.display();
        else
            System.out.println("null");
        System.out.println("\n ---------- display ----------");
        hashTable.display();
    }

    private static void test234Tree() {
        Tree234<Integer, String> tree234 = new Tree234<>();
        for (int i = 0; i < 20; i++) {
            int key = (int) (10 + Math.random() * 128);
            String data = "data_" + key;
            tree234.insert(key, data);
            System.out.println("\n ---------- display ----------");
            tree234.display();
        }

        System.out.print("\n ---------- insert ----------");
        tree234.insert(200, "data-200");
        System.out.println("\n ---------- display ----------");
        tree234.display();
        System.out.print("\n ---------- find ----------");
        tree234.find(200);
    }

    private static void testRedBlackTree() {
        RedBlackTree<Integer, String> redBlackTree = new RedBlackTree<>();
        for (int i = 0; i < 10; i++) {
            int key = (int) (10 + Math.random() * 128);
            String data = "data_" + key;
            redBlackTree.insert(key, data);
            System.out.println("\n ---------- display ----------");
            redBlackTree.display();
        }

        redBlackTree.insert(200, "data-200");
        System.out.print("\n ---------- find ----------");
        redBlackTree.find(200).display();
        System.out.println();
        redBlackTree.insert(8, "data-8");
        for (int i = 0; i < 10; i++) {
            int key = (int) (10 + Math.random() * 128);
            String data = "data_" + key;
            redBlackTree.insert(key, data);
            System.out.println("\n ---------- display ----------");
            redBlackTree.display();
        }
        redBlackTree.insert(20, "data-20");
        redBlackTree.display();
        System.out.println("\n ---------- 前序遍历 ----------");
        redBlackTree.traverse(1);
        System.out.println("\n ---------- 中序遍历 ----------");
        redBlackTree.traverse(2);
        System.out.println("\n ---------- 后序遍历 ----------");
        redBlackTree.traverse(3);

        System.out.println("\n ---------- display ----------");
        redBlackTree.display();
        System.out.println("\n ---------- delete ----------");
        redBlackTree.delete(200);

        System.out.println("\n ---------- display ----------");
        redBlackTree.display();
        System.out.println("\n ---------- delete ----------");
        redBlackTree.delete(8);

        System.out.println("\n ---------- display ----------");
        redBlackTree.display();
        System.out.println("\n ---------- delete ----------");
        redBlackTree.delete(20);

        redBlackTree.display();
        System.out.println("\n ---------- display ----------");

        redBlackTree.destroy();

    }

    private static void testBinaryTree() {
        BinaryTree<String> binaryTree = new BinaryTree<>();
        for (int i = 0; i < 20; i++) {
            int key = (int) (Math.random() * 128);
            String data = "data_" + key;
            binaryTree.insert(key, data);
            System.out.println("\n ---------- display ----------");
            binaryTree.display();
        }
        binaryTree.insert(200, "data-200");
        System.out.println("\n ---------- find ----------");
        binaryTree.find(200).display();
        System.out.println("\n ---------- display ----------");
        binaryTree.display();
        binaryTree.delete(200);
        System.out.println("\n ---------- display ----------");
        binaryTree.display();
        System.out.println("\n ---------- 前序遍历 ----------");
        binaryTree.traverse(1);
        System.out.println("\n ---------- 中序遍历 ----------");
        binaryTree.traverse(2);
        System.out.println("\n ---------- 后序遍历 ----------");
        binaryTree.traverse(3);
    }

    private static void testQuickSort() {
        int[] arr = new int[20];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 128);
        }
        //常规
//        ArrayUtil.getInstance().quickSort(arr);
        //三项取中划分
        ArrayUtil.getInstance().quickSort3(arr);
    }

    private static void testShellSort() {
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 100 - i;
        }
        ArrayUtil.getInstance().shellSort(arr);
    }

    private static void testMergerSort() {
        int[] arr = {
                9, 2, 5,
                3, 8, 6,
                7, 4, 5
        };
        ArrayUtil.getInstance().mergerSort(arr);
    }

    private static void testHanoiTower() {
        System.out.print("请输入要计算第几层：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            int n = Integer.parseInt(param);
            hanoiTower(n, 'A', 'B', 'C');
            System.out.println(n + "层汉诺塔需要移动" + count + "次");
        }
    }

    private static int count = 0;

    private static void hanoiTower(int n, char from, char inter, char to) {
        //inter 中介塔
        if (n == 1) {
            System.out.println("hanoiTower >>>> n=" + n + ", from=" + from + ", to=" + to);
        } else {
            hanoiTower(n - 1, from, to, inter);
            System.out.println("hanoiTower >>>> n=" + n + ", from=" + from + ", to=" + to);
            hanoiTower(n - 1, inter, from, to);
        }
        count++;
    }

    private static void testFactorial() {
        System.out.print("请输入要计算第几项：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            int result = factorial(Integer.parseInt(param));
            System.out.print("result: " + result);
        }
    }

    private static int factorial(int n) {
        if (n == 1)
            return 1;
        else
            return n * factorial(n - 1);
    }

    private static void testTrangle() {
        System.out.print("请输入要计算第几项：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            int result = trangle(Integer.parseInt(param));
            System.out.print("result: " + result);
        }
    }

    private static int trangle(int n) {
        //1. 循环
//        int total = 0;
//        while (n > 0) {
//            total = total + n;
//            --n;
//        }
//        return total;
        //2. 递归
        if (n == 1)
            return 1;
        else
            return n + trangle(n - 1);
    }

    private static void testLinkIterator() {
        LinkList<String> linkList = new LinkList<>();
        for (int i = 0; i < 10; i++) {
            linkList.insertFirst("data_" + i);
            linkList.display();
        }
        System.out.println("----> iterator:");
        LinkIterator<String> iterator = linkList.iterator();
        while (iterator.hasNext()) {
            Link<String> link = iterator.getCurrent();
            link.display();
            System.out.println();
            if (link.data.equals("data_6")) {
                iterator.remove();
            }
            iterator.next();
        }
        linkList.display();
    }

    private static void testDoublyLinkList() {
        DoublyLinkList<String> linkList = new DoublyLinkList<>();

        linkList.insertFirst("v_3");
        linkList.insertFirst("v_2");
        linkList.insertFirst("v_1");
        linkList.displayForward();
        linkList.displayBackground();

        linkList.insterLast("v_101");
        linkList.insterLast("v_102");
        linkList.insterLast("v_103");
        linkList.insertAfter("v_3", "v_6");
        linkList.insertAfter("v_3", "v_5");
        linkList.insertAfter("v_3", "v_4");
        linkList.displayForward();
        linkList.displayBackground();

        linkList.deleteFirst();
        linkList.deleteLast();
        linkList.delete("v_6");
        linkList.displayForward();
        linkList.displayBackground();
    }

    private static void testFirstLastLinkList() {
        FirstLastLinkList linkList = new FirstLastLinkList();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0)
                linkList.insertFirst("data_" + i);
            else
                linkList.insertLast("data_" + i);
            linkList.display();
        }

        linkList.deleteFirst();
        linkList.deleteFirst();
        System.out.println(">>执行两次deleteFirst<<");
        linkList.display();

    }

    private static void testLinkList() {
        LinkList linkList = new LinkList();
        for (int i = 0; i < 10; i++) {
            linkList.insertFirst("data_" + i);
            linkList.display();
        }

//        while (!linkList.isEmpty()){
//            linkList.deleteFirst();
//            linkList.display();
//        }

        System.out.println();
        linkList.find("data_3").display();
        System.out.println();
        linkList.delete("data_5").display();
        System.out.println();
        linkList.delete("data_7").display();
        System.out.println();
        linkList.delete("data_8").display();
        System.out.println();
        linkList.display();
    }

    private static void testPriorityQueue() {
        PriorityArrayQueue<Integer> priorityQueue = new PriorityArrayQueue<>(10);
        priorityQueue.insert(3);
        priorityQueue.display();
        priorityQueue.insert(9);
        priorityQueue.display();
        priorityQueue.insert(5);
        priorityQueue.display();
        priorityQueue.insert(0);
        priorityQueue.display();
        priorityQueue.insert(7);
        priorityQueue.display();
        priorityQueue.insert(2);
        priorityQueue.display();
        while (!priorityQueue.isEmpty()) {
            priorityQueue.remove();
            priorityQueue.display();
        }
        priorityQueue.insert(23);
        priorityQueue.display();
        priorityQueue.insert(12);
        priorityQueue.display();
        priorityQueue.insert(29);
        priorityQueue.display();

    }

    private static void testQueue() {
        ArrayQueue<String> queue = new ArrayQueue<>(10);
        for (int i = 100; i < 120; i++) {
            if (!queue.isFull()) {
                queue.insert("data—" + i);
                queue.display();
            }
        }
        System.out.println("peekFront:" + queue.peekFront());
        System.out.println("peekFront:" + queue.peekFront());
        System.out.println("peekFront:" + queue.peekFront());
        System.out.println("peekFront:" + queue.peekFront());

        while (!queue.isEmpty()) {
            System.out.println("remove:" + queue.remove());
            queue.display();
        }
        for (int i = 120; i < 140; i++) {
            if (!queue.isFull()) {
                queue.insert("data-" + i);
                queue.display();
            }
        }

    }

    private static void testStack() {
        ArrayStack<String> stacker = new ArrayStack<>(10);
        for (int i = 100; i < 120; i++) {
            if (!stacker.isFull()) {
                stacker.push("" + i);
                stacker.display();
            }
        }
        System.out.println("peek:" + stacker.peek());
        System.out.println("peek:" + stacker.peek());
        System.out.println("peek:" + stacker.peek());
        System.out.println("peek:" + stacker.peek());
        System.out.println("peek:" + stacker.peek());
        while (!stacker.isEmpty()) {
            System.out.println("pop:" + stacker.pop());
            stacker.display();
        }

        System.out.println("请输入要逆序的字符串：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            char[] chars = param.toCharArray();
            for (char aChar : chars) {
                if (!stacker.isFull())
                    stacker.push(aChar + "");
            }
            System.out.print("逆序后的字符串：");
            while (!stacker.isEmpty()) {
                System.out.print(stacker.pop());
            }
        }
    }

    private static void testSort() {
        int[] arr = {5, 3, 6, 2, 1, 9};
        System.out.println(Arrays.toString(arr) + "_len:" + arr.length);
        //直接选择排序
//        ArrayUtil.getInstance().selectSort(arr);
        //冒泡排序
//        ArrayUtil.getInstance().bubbleSort(arr);
        //直接插入排序
        ArrayUtil.getInstance().insertSort(arr);
    }

    private static void testOrderedArray() {
        int[] arr = {
                31, 33, 35, 37, 39, 41,
                11, 13, 15, 17, 19,
                1, 3, 5, 7, 9,
                21, 23, 25, 27, 29,
        };
        //初始化数组
        OrderedArray orderedArray = new OrderedArray(100);
        //插入
        for (int item : arr) {
            orderedArray.insert(item);
        }
        //size
        System.out.println("orderedArray.size=" + orderedArray.size());
        //打印
        orderedArray.display();
        //查找
        System.out.print("请输入要查找的数字：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
//            int resultIndex = orderedArray.find(Long.parseLong(param));
            int resultIndex = orderedArray.find(Long.parseLong(param), 0, orderedArray.size() - 1);
            System.out.println("resultIndex:" + resultIndex);
        }

        //删除
        System.out.print("请输入要删除的数字：");
        Scanner sc2 = new Scanner(System.in);
        if (sc2.hasNext()) {
            String param = sc2.next();
            boolean result = orderedArray.delete(Long.parseLong(param));
            System.out.println("result:" + result);
        }
        //size
        System.out.println("orderedArray.size=" + orderedArray.size());
        //打印
        orderedArray.display();
    }
}
