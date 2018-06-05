package com.ljy.javacode.structure.array;

/**
 * Created by ljy on 2018/5/30.
 */

public class ArrayUtil {
    private static final ArrayUtil ourInstance = new ArrayUtil();

    public static ArrayUtil getInstance() {
        return ourInstance;
    }

    private ArrayUtil() {
    }

    /**
     * 对数组的两个元素换位
     */
    private void swap(int[] array, int i, int j) {
        if (i == j)
            return;
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }

    /**
     * 打印数组
     *
     * @param data
     */
    public void print(int[] data) {
        System.out.print("array >>>> ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + "\t");
        }
        System.out.println();
    }


    /**
     * 直接选择排序
     * 每一趟从待排序的数据元素中选出最小（或最大）的一个元素，顺序放在已排好序的数列的最后，
     * 直到全部待排序的数据元素排完，它需要经过n-1趟比较。
     *
     * @param array
     */
    public void selectSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex])
                    minIndex = j;
            }
            if (minIndex != i) {
                swap(array, i, minIndex);
                print(array);
            }
        }
    }

    /**
     * 冒泡排序
     * 比较两个相邻的元素，将值大的元素交换至右端
     *
     * @param array
     */
    public void bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            //记录某趟是否发生交换，若为false表示数组已处于有序状态
            boolean isSort = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    isSort = true;
                    print(array);
                }
            }
            if (!isSort) {
                break;// 若数组已处于有序状态，结束循环
            }
        }
    }

    /**
     * 直接插入排序
     * <p>
     * 将待排序的数据元素按其关键字值的大小插入到前面的有序序列中
     */
    public void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int temp = array[i];
            if (array[i] < array[i - 1]) {
                int j = i - 1;
                //将有序部分（i之前的都是有序的）与temp（i处的）比较，将大于temp的依次后移一位（移到了i），
                //while结束后就找到了temp应该插入的位置（>temp的后移了，<temp的没动，那么中间空出的就是temp应该插入的位置）
                while (j >= 0 && array[j] > temp) {
                    array[j + 1] = array[j];
                    j--;
                }
                array[j + 1] = temp;
                print(array);
            }
        }
    }

    /**
     * 归并排序
     * <p>
     * 该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。
     * 将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。
     * 若将两个有序表合并成一个有序表，称为二路归并。
     * <p>
     * 效率较高,时间复杂度为O(N*logN), 而冒泡,插入,选择的时间复杂度都是O(N*N)
     * <p>
     * 缺点是 需要在存储器中有一个大小等于被排序数组的中介数组,就是用空间换时间
     * <p>
     * 核心: 归并两个有序数组
     */
    public void mergerSort(int[] array) {
        int[] tempArr = new int[array.length];
        mergerSort(array, tempArr, 0, array.length - 1);
//        print(array);
    }

    private void mergerSort(int[] array, int[] tempArr, int lowerIndex, int upperIndex) {
        System.out.println("lowerIndex = " + lowerIndex + ", upperIndex = " + upperIndex);
        if (lowerIndex != upperIndex) {
            int mid = (lowerIndex + upperIndex) / 2;
            mergerSort(array, tempArr, lowerIndex, mid);
            mergerSort(array, tempArr, mid + 1, upperIndex);
            marge(array, tempArr, lowerIndex, mid + 1, upperIndex);
        }
    }

    //归并两个有序部分
    private void marge(int[] array, int[] tempArr, int lowPtr, int highPtr, int upperIndex) {
        int j = 0;
        int lowerIndex = lowPtr;
        int mid = highPtr - 1;
        int n = upperIndex - lowerIndex + 1;
        System.out.println("lowPtr = " + lowPtr + ", highPtr = " + highPtr+",lowerIndex = " + lowerIndex + ", upperIndex = " + upperIndex);

        while (lowPtr <= mid && highPtr <= upperIndex) {
            if (array[lowPtr] < array[highPtr])
                tempArr[j++] = array[lowPtr++];
            else
                tempArr[j++] = array[highPtr++];
        }

        while (lowPtr <= mid)
            tempArr[j++] = array[lowPtr++];

        while (highPtr <= upperIndex)
            tempArr[j++] = array[highPtr++];

        for (j = 0; j < n; j++) {
            array[lowerIndex + j] = tempArr[j];
        }
        print(array);
    }
}
