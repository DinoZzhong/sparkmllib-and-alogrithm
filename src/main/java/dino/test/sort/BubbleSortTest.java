package dino.test.sort;

import java.util.Arrays;

/**
 * Created by Dino on 2016/12/13.
 *
 *  ？？？ 实现问题，冒泡排序应当是从最后一个往前排？
 */
public class BubbleSortTest {

    //从前向后的排序
    public static int[] bubbleSort(int [] x ){
        for(int i=0;i<x.length;i++){
            for(int j=0;j<i;j++)
                if (x[i] < x[j]) {
                    int tmp = x[j];
                    x[j] = x[i];
                    x[i] = tmp;
                }
            System.out.println( Arrays.toString(x));
        }
        return x;

    }

    //从后向前的冒泡
    public static int[] realBubbleSort(int [] x){
        int swapNums=0;
        int compareNums;
        for(int i=x.length-1;i>0;i--){
            swapNums =0;
            compareNums=0;
            for(int j=x.length-1;j>0;j--){
                compareNums++;
                if(x[j] < x[j-1]){
                    int tmp=x[j];
                    x[j]=x[j-1];
                    x[j-1]=tmp;
                    swapNums++;
                }
            }
            System.out.println("swap:"+swapNums+" compare:"+compareNums+" result:"+Arrays.toString(x));
            if(swapNums<=1) return x;    // 优化问题：从后向前冒泡过程中，如果存在一次比较次数小于2的则表示排序完成应当退出比较
        }
        return x;
    }

    public static void main(String[] a){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
     //   System.out.println("RESULT:"+Arrays.toString(bubbleSort(nums)));
        System.out.println(Arrays.toString(realBubbleSort(nums)));
    }
}
