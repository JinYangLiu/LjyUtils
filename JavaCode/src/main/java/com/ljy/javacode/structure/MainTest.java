package com.ljy.javacode.structure;

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
                    testPriorityQueue();
                    break;
                default:
                    break;

            }
        }


    }

    private static void testPriorityQueue() {
        PriorityQueue priorityQueue=new PriorityQueue(10);
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
        while (!priorityQueue.isEmpty()){
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
        Queue queue = new Queue(10);
        for (int i = 100; i < 120; i++) {
            if (!queue.isFull()) {
                queue.insert(i);
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
                queue.insert(i);
                queue.display();
            }
        }

    }

    private static void testStack() {
        Stacker stacker = new Stacker(10);
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
