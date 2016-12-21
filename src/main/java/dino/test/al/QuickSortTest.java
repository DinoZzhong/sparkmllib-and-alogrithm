package dino.test.al;

import java.util.Arrays;

/**
 * Created by Dino on 2016/12/13.
 *
 * 快速排序
 *
 * 快速排序是对冒泡排序的改进，冒泡排序只扫描交换相邻的两个元素，消除一个逆序
 * 快速排序则通过交换两个不相邻的元素，消除待排序队列中多个逆序
 *
 * 扫描方式：扫描左边直到遇到一个需要交换的值，再扫描右边直到遇到一个需要交换的值
 *  迭代以上步骤，直到左右都没有值了，完成第一次排序，得到基于基数的分组有序两个子表
 *  对子表重复进行以上操作，直到所有的子表长度都不大于1
 *
 *  与归并排序区别
 *     归并是先划分到最小的分组(两个元素)，使分组内有序，之后再不断合并各单元
 *     快速排序则是从上到下，不断向下划分保证分组有序，一直到最小分组
 */
public class QuickSortTest {
    public static  void quickSort(int[] x,int left,int right){
        if(left<right) {
            int begin = left;
            int end = right;
            int mid = (begin + end) / 2; // 基数设置 取中间的(跟取第一个没区别)
            int temp = x[mid]; // 该值为基数 不会变
            System.out.println("mid:" + mid + " temp:" + temp);
            while (begin < end) {
                //左循环
                while (begin < mid && x[begin] <= temp) begin++;
                if (x[begin] > temp) {
                    x[mid] = x[begin];
                    x[begin] = temp;
                    mid = begin;
                }
                //右循环
                while (end > mid && x[end] >= temp) end--;
                if (x[end] < temp) {
                    x[mid] = x[end];
                    x[end] = temp;
                    mid = end;
                }
            }
            System.out.println(Arrays.toString(x));
            quickSort(x, left, mid - 1);
            quickSort(x, mid + 1, right);
        }else
            System.out.println("result:"+Arrays.toString(x));
    }

    public static void main(String[] a){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
        quickSort(nums,0,nums.length-1);

    }
}
