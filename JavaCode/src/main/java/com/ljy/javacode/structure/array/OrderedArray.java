package com.ljy.javacode.structure.array;

/**
 * Created by ljy on 2018/5/30.
 */

public class OrderedArray {
    private long[] array;
    private int mElems;

    public OrderedArray(int max) {
        array = new long[max];
        mElems = 0;
    }

    public int size() {
        return mElems;
    }

    /**
     * 二分法查找
     * <p>
     * 数组越大，二分查找的优势就越明显
     * <p>
     * 公式：2的n次幂>=arr.size, n为查找所需次数 ,2的n次幂为n次查找可覆盖的范围
     * 即 范围是次数的指数，次数是范围的对数
     *
     * @param searchValue 要查找的值
     * @return 查找结果，该值对应的角标，-1表示未找到
     */
    public int find(long searchValue) {
        int lowerIndex = 0;
        int upperIndex = mElems - 1;
        int currIndex;
        while (true) {
            currIndex = (lowerIndex + upperIndex) / 2;
            System.out.println("array[" + currIndex + "]=" + array[currIndex]);
            if (lowerIndex > upperIndex) {
                return -1;
            } else if (array[currIndex] == searchValue) {
                return currIndex;
            } else if (array[currIndex] < searchValue) {
                lowerIndex = currIndex + 1;
            } else {
                upperIndex = currIndex - 1;
            }
        }
    }

    /**
     * 插入数据
     *
     * @param value
     */
    public void insert(long value) {
        int i;
        for (i = 0; i < mElems; i++) {
            if (array[i] > value)
                break;
        }
        for (int j = mElems; j < i; j--) {
            array[j] = array[j - 1];
        }
        array[i] = value;
        mElems++;
    }

    /**
     * 删除
     *
     * @param value
     * @return
     */
    public boolean delete(long value) {
        int i = find(value);
        if (i == -1) {
            return false;
        } else {
            for (int j = i; j < mElems; j++) {
                array[j] = array[j + 1];
            }
            mElems--;
            return true;
        }
    }

    /**
     * 打印数组
     */
    public void display() {
        System.out.print("OrderedArray: ");
        for (int i = 0; i < mElems; i++) {
            System.out.print("[" + i + "]-->" + array[i] + "  ");
        }
        System.out.println();

    }


}
