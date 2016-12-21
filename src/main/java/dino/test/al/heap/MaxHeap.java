package dino.test.al.heap;

/**
 * Created by Dino on 2016/7/2.
 *
 * 最大堆结构
 */
public class MaxHeap <T extends Comparable<? super T>> {
    private T[] heap ;// 存放堆
    private int lastindex; // 最后一个元素的索引
    private static final int default_cap=20; // 存储数组的默认的大小


    public MaxHeap(int size){
        heap = (T[])new Comparable[size];
        lastindex=0;
    }
    public MaxHeap(){ // 调用上面的构造器
        this(default_cap);
    }
    public MaxHeap(T[] init){
        lastindex =init.length+1;
        for(int i= lastindex/2;i>=1;i--){

        }

    }

    public boolean isEmpty(){
        return lastindex==0;
    }
    public int getHeapSize(){
        return lastindex;
    }
    public void doubleArray(){ // 扩大数组
        int lenDouble = heap.length*2;
        T[] temp = (T[])new Comparable[lenDouble];
        for(int i=0;i<lastindex;i++){
            temp[i]=heap[i];
        }
        heap=temp;
    }

    //因为是大顶堆，从下往上比较，先比较左右在比较父节点
    public void add(T parm){
        lastindex++; // 指向下一个空的
        if(lastindex>=heap.length)doubleArray(); // 判断是否有下一个对象

        // 需要判断放入的位置
        int newIndex = lastindex;
        int parentIndex = newIndex/2; // newIndex的父节点
        System.out.println(parm+"  new:"+newIndex+" parent:"+parentIndex+"  parentValue:"+heap[parentIndex]);
        while(newIndex>1 && parm.compareTo(heap[parentIndex])>0){
            heap[newIndex]=heap[parentIndex]; //第一次的时候heap[newIndex]是空的 不断往子节点覆盖？？
            newIndex=parentIndex;
            parentIndex=newIndex/2;
        }
        heap[newIndex]=parm; // 最后到了正确的位置，并将最后节点的数往这里写，之后需要对这个节点之后数据往下移动
        disPlay();
        //树的左右比较
    }
    //根据父节点对子节点进行比较
    public void childSwap(int rootIndex){
        int left = rootIndex*2;
        int right = left+1;
        System.out.println("child swap:"+heap[right]+"  "+heap[left]+"  "+heap[left].compareTo(heap[right]));
        if(right<lastindex && heap[left].compareTo(heap[right])<0){ // 左比右大的时候才会替换，替换之后需要右节点进行子树核对
            T temp =  heap[right];
            heap[right]=heap[left];
            heap[left]=temp;
        }
    }


    public void reHeap(int rootIndex){
        boolean done=false;
        T root=heap[rootIndex];
        int lagerChildIndex = 2*rootIndex;
        while(!done && lagerChildIndex<=lastindex){
            int leftChildIndex=lagerChildIndex;
            int rightChildIndex=leftChildIndex+1;
            if(rightChildIndex<=lastindex && heap[rightChildIndex].compareTo(heap[lagerChildIndex])>0){
                lagerChildIndex=rightChildIndex;
            }
            if(root.compareTo(heap[lagerChildIndex])<0){
                heap[rootIndex] = heap[lagerChildIndex];
                lagerChildIndex = 2* rootIndex;
            }else{
                done=true;
            }
            heap[rootIndex]=root;
        }
    }
    public void disPlay(){
        for(int i=1;i<=lastindex;i++)
            System.out.print(" " + heap[i]);
        System.out.println("");
    }

    public static void main(String[] ar){
        MaxHeap<Integer> heap = new MaxHeap<Integer>();
        heap.add(4);
        heap.add(2);
        heap.add(6);

        heap.disPlay();
        heap.add(10);
        heap.add(11);

        heap.disPlay();
        heap.add(1);
        heap.add(5);
        heap.disPlay();

    }


}
