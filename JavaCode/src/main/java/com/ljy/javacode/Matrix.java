package com.ljy.javacode;

/**
 * Created by Mr.LJY on 2018/2/11.
 * <p>
 * 邻接矩阵的可达矩阵
 */

public class Matrix {
    public static void main(String args[]) {

        int[][] arr={
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
        ReachableMatrix reachableMatrix = new ReachableMatrix(arr).invoke();
        reachableMatrix.printResult();

    }

    private static class ReachableMatrix {
        private int[][] arr;
        private int[][] result;
        private int len;

        public ReachableMatrix(int[]... arr) {
            this.arr = arr;
        }

        public int[][] getResult() {
            return result;
        }

        public int getLen() {
            return len;
        }

        public ReachableMatrix invoke() {
            len = arr.length;
            result = new int[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    result[i][j] = i==j?1:arr[i][j];
                }
            }
            for (int k = 0; k < len; k++)
                for (int i = 0; i < len; i++)
                    for (int j = 0; j < len; j++)
                        result[i][j] |= (result[i][k] & result[k][j]);
            return this;
        }

        public void printResult(){
            System.out.println("--->");
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    System.out.print( result[i][j]+"\t");
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
