package dino.test.sort;

import java.util.Arrays;

/**
 * Created by Dino on 2016/12/14.
 *
 * 直接插入排序
 *   每次从无序表里取一个数插入到有序表里 保证有序表仍然有序
 *   第一趟比较前两个数字，第二趟把第三个数与前两个数从前向后比较，将第三个数插入有序表中
 *   依次进行，进行n-1次扫描以后就完成了整个排序过程
 */
public class DirectInsertSortTest {
    public  static void directInsertSort(int[] x){
        for(int i =1 ;i<x.length;i++){
            for(int j=0;j<i;j++){
               if(x[i]<x[j]){
                   int tmp = x[j];
                   x[j]=x[i];
                   x[i]=tmp;
               }
            }
            System.out.println(String.format("第%d次：%s",i,Arrays.toString(x) ));
        }
    }

    public static void main(String[] a){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
        directInsertSort(nums);
    }
}
