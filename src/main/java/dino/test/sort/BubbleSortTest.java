package dino.test.sort;

import java.util.Arrays;

/**
 * Created by Dino on 2016/12/13.
 *
 *
 */
public class BubbleSortTest {
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
    public static void main(String[] a){
        int[] nums = { 7, 2, 8, 3, 1, 6, 9, 0, 5, 4 ,3};
        System.out.println("RESULT:"+Arrays.toString(bubbleSort(nums)));
    }
}
