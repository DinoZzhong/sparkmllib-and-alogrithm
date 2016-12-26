package dino.test.sort;

import java.util.Arrays;

/**
 * Created by Dino on 2016/12/12.
 *
 *
 * 在merge中不断的new一个数组，消耗可能过大
 * 在单线程环境下可以传递进一个待排序数据一样大的数组作为辅助数组
 */
public class MergeSortTest {
    public static void sortArray(int[] x,int start,int end){
        if(start>=end) return ;  // 注意是大于等于，在等于的情况下不需要再处理
        int mid = (start+end)/2;
        if(start<end){
            sortArray(x,start,mid);
            sortArray(x,mid+1,end);
            merageArray(x,start,end,mid);
        }
        System.out.println(Arrays.toString(x));

    }
    public static int[] merageArray(int[] x,int start,int end,int mid){
        int temp[] = new int[end-start+1];
        int i = start;
        int j = mid+1;
        int k =0;
        while(i<=mid && j<=end){
            if(x[i]<=x[j])
                temp[k++]=x[i++];
            else
                temp[k++]=x[j++];
        }
        while(i<=mid)
            temp[k++]=x[i++];
        while(j<=end)
            temp[k++]=x[j++];

        for(int n =0;n < temp.length;n++)
            x[n+start]=temp[n];

        return  x;
    }

    public static void main(String[] a){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
        sortArray(nums, 0, nums.length-1);
    }

}
