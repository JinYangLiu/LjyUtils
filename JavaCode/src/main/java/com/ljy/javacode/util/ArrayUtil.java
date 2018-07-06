package com.ljy.javacode.util;

import com.ljy.javacode.structure.tree.VectorHeap;

import java.util.Arrays;

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
     *
     * @param array 目标数组
     * @param i     需要交换的索引i
     * @param j     需要交换的另一个索引j
     * @throws NullPointerException           if array is null
     * @throws ArrayIndexOutOfBoundsException if i|j is more than array.length-1
     */
    public static void swapPublic(int[] array, int i, int j) {
        if (array == null)
            throw new NullPointerException("数组不能是空");
        if (i >= array.length - 1 || j >= array.length - 1)
            throw new ArrayIndexOutOfBoundsException("需要交换的索引应在数组长度范围内");
        if (i == j)
            return;
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }

    private void swap(int[] array, int i, int j) {
        //断言失败将抛出AssertionError
        assert array != null;
        assert i < array.length && j < array.length;
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
     * 1. 直接选择排序
     * 每一趟从待排序的数据元素中选出最小（或最大）的一个元素，顺序放在已排好序的数列的最后，
     * 直到全部待排序的数据元素排完，它需要经过n-1趟比较。
     *
     * @param array
     */
    public void selectSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
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
        if (array == null || array.length <= 1)
            return;
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
     * 将待排序的数据元素按其关键字值的大小插入到前面的有序序列中
     */
    public void insertSort(int[] array) {
        insertSort(array, 0, array.length - 1);
    }

    public void insertSort(int[] array, int start, int end) {
        if (array == null || end < 1)
            return;
        for (int i = start + 1; i <= end; i++) {
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
     * 折半插入排序
     * 又叫二分插入排序
     */
    public void binaryInsertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                // 缓存i处的元素值
                int tmp = array[i];
                // 记录搜索范围的左边界
                int low = 0;
                // 记录搜索范围的右边界
                int high = i - 1;
                while (low <= high) {
                    // 记录中间位置
                    int mid = (low + high) / 2;
                    // 比较中间位置数据和i处数据大小，以缩小搜索范围
                    if (array[mid] < tmp) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                //将low~i处数据整体向后移动1位
                for (int j = i; j > low; j--) {
                    array[j] = array[j - 1];
                }
                array[low] = tmp;
                print(array);
            }
        }
    }

    //------------------- 归并排序 start --------------------

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
        if (array == null || array.length <= 1)
            return;
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
        System.out.println("lowPtr = " + lowPtr + ", highPtr = " + highPtr + ",lowerIndex = " + lowerIndex + ", upperIndex = " + upperIndex);

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
    //------------------- 归并排序 end --------------------

    /**
     * 希尔排序:
     * 又叫最小增量排序,基于插入排序,时间复杂度O(N*(logN)2),
     * 效率不算太高,适于中等大小数组,
     * 但是非常容易实现,代码既简单又短
     * <p>
     * 它通过加大插入排序中元素之间的间隔，并在这些有间隔的元素中进行插入排序，从而使数据项大跨度地移动，
     * 当这些数据项排过一趟序之后，希尔排序算法减小数据项的间隔再进行排序，依次进行下去，进行这些排序时
     * 的数据项之间的间隔被称为增量，习惯上用字母h来表示这个增量。
     * <p>
     * Shell排序是不稳定的，它的空间开销是O(1),时间开销估计在O(N3/2)~O(N7/6)之间
     * <p>
     * shell原稿中建议初始间距为N/2,但这被证明不是最好的数列,会使时间复杂度降低到O(N2)
     * 后又衍生出N/2.2
     * 其中的关键点在于间隔数列元素的互质性,这使得每一趟排序更有可能保持前一趟排序已排好的效果
     */
    public void shellSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
        print(array);
        //增量h
        int h = (int) (array.length / 2.2);
        while (h > 0) {
            System.out.println("增量h:" + h);
            for (int i = h; i < array.length; i++) {
                int tmp = array[i];
                int j = i - h;
                while (j >= 0 && array[j] > tmp) {
                    array[j + h] = array[j];
                    j -= h;
                }
                array[j + h] = tmp;
            }
            //设置新的增量
            h /= 2;
        }
        print(array);
    }

    /**
     * 快速排序:
     * - 划分:快速排序的根本机制, 把数据分为两组,使所有关键字大于特定值的数据项在一组,小于的在另一组,
     * 如全班学生的考试成绩以及格线60划分
     * - 时间复杂度: O(N*logN)
     * - 把一个数组分为两个子数组(划分), 然后递归的调用自身,为每个子数组进行快速排序
     * - 效率: 影响效率的关键点在于枢纽的选择(即上面划分中的关键字,例子中的60分),
     * 应尽量保证两个子数组的大小接近
     * - 通常来说关键字可以选择任意一项数据,一般选择头尾arr[0]或arr[arr.length-1],
     * 但是这样做算法的性能是不稳定的,因为待排序的数组可能是有序的,会使时间复杂度降到O(N*N),
     * 理想中的枢纽应为待排序数组的中值数据项, 但是选取中间值比较麻烦,所以一个折中的办法就是
     * '三项数据取中'划分,即数组头,尾,中,三个数据项的中值作为枢纽, 这样既简单又可以避免选择到最大
     * 或最小值作为枢纽的情况.
     */

    //------- 常规写法 ----------
    public void quickSort(int[] array) {
        print(array);
        quickSort(array, 0, array.length - 1);
        print(array);
    }

    public void quickSort(int[] array, int start, int end) {
        if (start >= end)
            return;
        //以起始索引为分界点
        int pivot = array[start];
        int i = start + 1;
        int j = end;
        while (true) {
            while (i <= end && array[i] < pivot) {
                i++;
            }
            while (j > start && array[j] > pivot) {
                j--;
            }
            if (i < j) {
                swap(array, i, j);
            } else {
                break;
            }
        }
        //交换 j和分界点的值
        swap(array, start, j);
        print(array);
        //递归左子序列
        quickSort(array, start, j - 1);
        //递归右子序列
        quickSort(array, j + 1, end);
    }

    // ----------快速排序 三项数据取中 写法 ------------
    public void quickSort3(int[] array) {
        print(array);
        quickSort3(array, 0, array.length - 1);
        print(array);
    }

    private void quickSort3(int[] array, int start, int end) {

        int size = end - start + 1;
//        if (size <= 3)
//            manualSort(array, start, end);
        //对于小划分的处理,上面是限制为3, 手动比较, 下面的没有限制, 使用插入排序,
        // 下面的更方便试用出不同的切割点, 以找到更好的执行效率,这一点很有意义
        if (size < 10)
            insertSort(array, start, end);
        else {
            //3项取中
            int median = medianOf3(array, start, end);
            //划分
            int pivotIndex = partitionIt(array, start, end, median);
            //递归左右子列
            quickSort3(array, start, pivotIndex - 1);
            quickSort3(array, pivotIndex + 1, end);
        }

    }

    //划分
    private int partitionIt(int[] array, int start, int end, int pivot) {
        int leftPtr = start;
        int rightPtr = end - 1;
        while (true) {
            while (array[++leftPtr] < pivot)
                ;
            while (array[--rightPtr] > pivot)
                ;
            if (leftPtr >= rightPtr)
                break;
            else
                swap(array, leftPtr, rightPtr);
        }
        swap(array, leftPtr, end - 1);
        return leftPtr;
    }

    //三项数据取中
    private int medianOf3(int[] array, int start, int end) {
        int center = (start + end) / 2;

        if (array[start] > array[center])
            swap(array, start, center);

        if (array[start] > array[end])
            swap(array, start, end);

        if (array[center] > array[end])
            swap(array, center, end);

        swap(array, center, end - 1);// put pivot on right

        return array[end - 1];
    }

    //当数组/子数组长度<=3时,进行手动排序
    private void manualSort(int[] array, int start, int end) {
        int size = end - start + 1;
        if (size <= 1)
            return;
        if (size == 2) {
            if (array[start] > array[end])
                swap(array, start, end);
            return;
        }
        if (size == 3) {
            if (array[start] > array[end - 1])
                swap(array, start, end - 1);
            if (array[start] > array[end])
                swap(array, start, end);
            if (array[end - 1] > array[end])
                swap(array, end - 1, end);
        }
    }

    //----------  快速排序 end ---------------

    /**
     * 基数排序
     * - 基数:一个数字系统的基,10是十进制系统的激素,2是二进制系统的基数
     * - 把数值拆分2为数字位,对每一位进行排序
     * - 缺点:以空间换时间
     * 使用: radixSort(data, 10, 4);
     */
    public void radixSort(int[] data, int radix, int d) {
        // 缓存数组
        int[] tmp = new int[data.length];
        // buckets用于记录待排序元素的信息
        // buckets数组定义了max-min个桶
        int[] buckets = new int[radix];

        for (int i = 0, rate = 1; i < d; i++) {

            // 重置count数组，开始统计下一个关键字
            Arrays.fill(buckets, 0);
            // 将data中的元素完全复制到tmp数组中
            System.arraycopy(data, 0, tmp, 0, data.length);

            // 计算每个待排序数据的子关键字
            for (int j = 0; j < data.length; j++) {
                int subKey = (tmp[j] / rate) % radix;
                buckets[subKey]++;
            }

            for (int j = 1; j < radix; j++) {
                buckets[j] = buckets[j] + buckets[j - 1];
            }

            // 按子关键字对指定的数据进行排序
            for (int m = data.length - 1; m >= 0; m--) {
                int subKey = (tmp[m] / rate) % radix;
                data[--buckets[subKey]] = tmp[m];
            }
            rate *= radix;
        }
    }


    /**
     * 堆排序
     */
    public void heapSort(int[] array) {
        print(array);
        VectorHeap<Integer, Integer> vectorHeap = new VectorHeap<>();
        for (int i = 0; i < array.length; i++)
            vectorHeap.insert(array[i], null);
        for (int i = 0; i < array.length; i++)
            array[array.length - 1 - i] = vectorHeap.remove().key;
        print(array);
    }


}
