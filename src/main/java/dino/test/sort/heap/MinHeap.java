package dino.test.sort.heap;

/**
 * Created by Dino on 2016/7/3.
 *
 * 大顶堆 小顶堆 如果实现的是自定义的类... 实现Comparale接口调整里面方法的返回值就可以了....
 */
public class MinHeap <T extends Comparable<? super T>>{
    private T[] heap;
    private int lastindex;
    private static final int CAPACITY =30;

    public MinHeap(){
        this(CAPACITY);
    }
    public MinHeap(int size){
        heap = (T[])new Comparable[size];
        lastindex = 0;
    }
    // 将对象初始化为一个最小堆
    public MinHeap(T[] in){

    }

    public boolean isEmpty(){
        return lastindex==0;
    }
    public int getHeapSize(){
        return lastindex;
    }

    // 增加堆的长度大小
    public void incrementToDouble(){
        int size = heap.length;
        T[] temp = (T[])new Comparable[size*2];
        for(int i=0;i<size;i++){
            temp[i]=heap[i];
        }
        heap=temp;
    }

    // 插入一个对象
    public void add(T parm){
        int newIndex = lastindex; // 当前位置
        int parentIndex = newIndex/2;
        while(newIndex>0 && parm.compareTo(heap[parentIndex])<0){// 如果比父节点还小就向上浮动
           // System.out.println("新增："+parm+" 父："+heap[parentIndex]+" 新元素比其父节点值更小，父元素下沉");
            heap[newIndex]=heap[parentIndex];
            newIndex = parentIndex;
            parentIndex = newIndex/2;
        }
        heap[newIndex]=parm;
        lastindex++;
    }


    //循环实现删除根节点
    public void deleteRoot(){
        heap[0]=heap[lastindex-1]; //与最后一个交换,之后将其与左右节点迭代下沉
        T last =heap[0];
        int parent=0;
        int child=2*parent+1;
        int comp =child;
        while(child<lastindex){ //判断是否有叶子节点
            int left=child;
            int right =left+1;
            if(right<=lastindex && heap[right].compareTo(heap[left])<0){ //右边比较小 选择右子树下沉
                comp=right;
            }
           // System.out.println("最小子节点为 "+heap[comp]+"  "+heap[parent]);
            if(heap[comp].compareTo(last)>0){ //如果子树都比父节点大 则跳出循环
                break;
            }else{
                heap[parent]=heap[comp];
                parent=comp;
                child=2*parent+1;
                comp =child;
                System.out.println(parent+"  "+child+"....");
            }
        }
        heap[parent]=last;
        lastindex--;
    }


    // 对指定的树进行自上而下排序
    public void heapAdjust(int rootIndex){
        int index=rootIndex*2+1; // 决定比较的节点下标
        if(index<=lastindex){
            if(index+1<=lastindex && heap[index].compareTo(heap[index+1])>0){
                index++; //右节点小 选择右节点
            }
            //System.out.println(heap[rootIndex]+"  "+heap[index]+"  ");
            if(heap[rootIndex].compareTo(heap[index])>0){
                T temp=heap[rootIndex];
                heap[rootIndex]=heap[index];
                heap[index]=temp;
                heapAdjust(index);
            }
        }
    }
    // 保持最小堆,如果添加的元素不大于最小值则放弃
    public void addAndKeep(T parm){
        if(parm.compareTo(heap[0])>0){
            heap[0]=parm;
            heapAdjust(0);
        }else{
            System.out.println(parm+"<="+heap[0]+" break..");
        }
    }
    //删除第一个元素
    public T  pop(){
        if(!isEmpty()) { // 如果堆是空的则返回空值
            T temp = heap[0];
            heap[0] = heap[--lastindex];
            heapAdjust(0);
            return temp;
        }
        return null;
    }
    // 堆排序.... 最小堆，所以是升序排序
    public T[] sortHeap(){
        int size = lastindex; // lastindex是动态的
        T[] sort =(T[])new Comparable[size];
        for(int i=0;i<size;i++) {
            sort[i] = pop();
        }
        return sort;
    }

    public void show(){
        for(int i=0;i<lastindex;i++)
            System.out.print(heap[i] + " ");
        System.out.println("");
    }

    public static void main(String[] args){
        MinHeap<Student> student = new MinHeap<Student>();
        student.add(new Student("Dino1",70.0));
        student.add(new Student("Dino",30.0));
        student.add(new Student("Jack",40.0));
        student.add(new Student("Dino",50.0));
        student.add(new Student("Meimei",80.0));
        student.add(new Student("Jobn",35.0));
        student.show();
       /* MinHeap<Integer> heap= new MinHeap<Integer>();
        heap.add(5);
        heap.add(10);
        heap.add(8);
        heap.add(1);
        heap.add(80);
        heap.add(97);
        heap.add(18);
        heap.add(80);
        heap.add(46);
        heap.add(15);
        heap.add(59);
        heap.show();
        Comparable[] sortHeap = heap.sortHeap();
        for(int i=0;i<sortHeap.length;i++)
            System.out.print(sortHeap[i]+" ");
        System.out.println("!!!!");
        heap.show();
        System.out.println( (heap.pop() == null) +"   00000");
        heap.deleteHead();
        heap.show();
        heap.deleteHead();
        heap.show();
        heap.deleteHead();
        heap.show();
        heap.pop();
        heap.show(); //8 15 10 80 46 59 18 97 80*/
    }
}
