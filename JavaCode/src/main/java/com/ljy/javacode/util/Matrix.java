package com.ljy.javacode.util;

import java.util.Arrays;

/**
 * Created by Mr.LJY on 2018/2/11.
 * <p>
 * 邻接矩阵的可达矩阵
 */

public class Matrix {
    public static void main(String args[]) {

        int[][] arr = {
                {0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1},
                {1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
                {1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1},
                {1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1},
                {1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        ReachableMatrix reachableMatrix = new ReachableMatrix(arr);
        reachableMatrix.invoke().printResult();
        reachableMatrix.invoke2().printResult();
    }

    private static class ReachableMatrix {
        private int[][] arr;
        private int[][] result;
        private int len;

        public ReachableMatrix(int[]... arr) {
            this.arr = arr;
        }

        public ReachableMatrix invoke2() {
            //1. 复制原数组,并与单位矩阵进行或(|)运算
            len = arr.length;
            result = new int[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    result[i][j] = i == j ? 1 : arr[i][j];
                }
            }
            //2.  Warshall算法利用图的邻接矩阵求出原图的传递闭包
            for (int i = 0; i < len; i++) //行
                for (int j = 0; j < len; j++) //列
                    if (result[i][j] == 1)// i-->j
                        for (int k = 0; k < len; k++)  // k-->i , 找到连接i的顶点
                            if (result[k][i] == 1 && k != j)
                                result[k][j] = 1;//k-->j
            return this;
        }

        public ReachableMatrix invoke() {
            len = arr.length;
            result = new int[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    result[i][j] = i == j ? 1 : arr[i][j];
                }
            }
            for (int k = 0; k < len; k++)
                for (int i = 0; i < len; i++)
                    for (int j = 0; j < len; j++)
                        result[i][j] |= (result[i][k] & result[k][j]);
            return this;
        }

        public void printResult() {
            System.out.println("--->");
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    System.out.print(result[i][j] + "\t");
                }
                System.out.print("\n");
            }
        }
    }
}


/*
*
* int[][] arr1 = {
                {0, 1, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 1, 0, 1},
                {0, 0, 1, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        int[][] arr2 = {
                {0, 0, 1, 1, 0, 1, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0}};
        int[][] arr3 =  {
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}
        };
*
* */
