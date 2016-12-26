package dino.test.sort;

import java.util.Arrays;

/**
 * Created by zhong on 2016/12/27.
 *
 * 简单选择排序
 *  原理：给定序列选择第i小的值，并与序列[i]交换，完成一次选择排序
 *  不断迭代直到最后
 *
 *  相对于冒泡排序，记录移动的次数少,最不理想为n-1，最理想为0即此时本身已经有序，但是不管怎么样比较次数都为O(n^2)
 */
public class SimpleSeletionSortTest {

    // 循环实现
    public static int[] sort(int[] list){
        int compareNums=0; // 比较次数统计
        int swapNums=0;//交换次数
        int len = list.length;
        for(int i=0;i<len;i++){ // 这里以升序排序为例，每一次都进一位，即每一次获取到的都是未排序队列里的最小值
            int minIndex=i;
            for(int j=i;j<len;j++){
                compareNums++;
                if(list[minIndex]>list[j]){ // 获取到未排序队列的最小下标
                    minIndex=j;
                }
            }
            //判断当前是否需要调整，存在可能此时下标i的值即为最小值
            if(minIndex!=i){
                int temp=list[i];
                list[i]=list[minIndex];
                list[minIndex]=temp;
                System.out.println("第"+i+"次:"+Arrays.toString(list));
            }else{
                System.out.println("第"+i+"次未变化");
            }
        }
        System.out.println("compare nums:"+compareNums+"  swap nums:"+swapNums);
        return list;
    }
    //递归实现
    public  static int[] sort2(int[] list,int start){//每次传入的是剩下的未排序的队列的第一个下标
        int len=list.length;
        if(start==len)
            return list;
        int minIndex=start;
        for(int i=start;i<len;i++){
            if(list[i]<list[minIndex]){
                minIndex=i;
            }
        }
        if(start!=minIndex){
            int temp=list[start];
            list[start]=list[minIndex];
            list[minIndex]=temp;
        }
        sort2(list,start+1);
        return list;
    }


    public static void main(String[] s){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
        int[] res=sort(nums);
        System.out.println(Arrays.toString(sort(res)));
        System.out.println("递归实现");
        System.out.println(Arrays.toString(sort2(nums,0)));
    }
}
