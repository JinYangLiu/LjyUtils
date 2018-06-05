package com.ljy.javacode.structure;

import com.ljy.javacode.structure.array.ArrayUtil;
import com.ljy.javacode.structure.array.OrderedArray;
import com.ljy.javacode.structure.link.DoublyLinkList;
import com.ljy.javacode.structure.link.FirstLastLinkList;
import com.ljy.javacode.structure.link.LinkList;
import com.ljy.javacode.structure.queue.PriorityArrayQueue;
import com.ljy.javacode.structure.queue.ArrayQueue;
import com.ljy.javacode.structure.stack.ArrayStacker;

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
                default:
                    break;

            }
        }


    }

    private static void testDoublyLinkList() {
        DoublyLinkList<String> linkList=new DoublyLinkList<>();

        linkList.insertFirst("v_3");
        linkList.insertFirst("v_2");
        linkList.insertFirst("v_1");
        linkList.displayForward();
        linkList.displayBackground();

        linkList.insterLast("v_101");
        linkList.insterLast("v_102");
        linkList.insterLast("v_103");
        linkList.insertAfter("v_3","v_6");
        linkList.insertAfter("v_3","v_5");
        linkList.insertAfter("v_3","v_4");
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
                linkList.insertLast("data_"+i);
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
        PriorityArrayQueue priorityQueue = new PriorityArrayQueue(10);
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
        ArrayQueue queue = new ArrayQueue(10);
        for (int i = 100; i < 120; i++) {
            if (!queue.isFull()) {
                queue.insert("data—"+i);
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
                queue.insert("data-"+i);
                queue.display();
            }
        }

    }

    private static void testStack() {
        ArrayStacker stacker = new ArrayStacker(10);
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
        int[] arr = {1, 3, 5, 7, 9,
                11, 13, 15, 17, 19,
                21, 23, 25, 27, 29,
                31, 33, 35, 37, 39, 41};
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
            int resultIndex = orderedArray.find(Long.parseLong(param));
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
