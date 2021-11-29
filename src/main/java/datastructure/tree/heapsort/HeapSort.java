package datastructure.tree.heapsort;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/29 21:45
 * @version:1.0
 */
public class HeapSort {
    public static void main(String[] args) {
//        int[] arr = {4, 6, 8, 5, 9};
        int[] arr = new int[8000000];
        for (int i = 0; i < 8000000; i++) {
            arr[i] = (int) (Math.random() * 8000000);
        }
        System.out.println("排序前：");
        Date data1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(data1);
        System.out.println("排序前的时间：" + format);
        heapSort(arr);

        System.out.println("排序后");
        Date data2 = new Date();
        String format2 = simpleDateFormat.format(data2);
        System.out.println("排序前的时间：" + format2);
//        System.out.println(Arrays.toString(arr));

    }

    /**
     * 堆排序
     *
     * @param arr
     */
    public static void heapSort(int arr[]) {
        System.out.println("堆排序：");
//        //分步完成
//        adjustHeap(arr,1,arr.length);
//        System.out.println("第一次");
//        System.out.println(Arrays.toString(arr));
//        System.out.println("第二次");
//        adjustHeap(arr,0,arr.length);
//        System.out.println(Arrays.toString(arr));

        //第一次是从下往上的
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            adjustHeap(arr, i, arr.length);
        }

        //交换
        int temp;
        for (int j = arr.length - 1; j > 0; j--) {
            temp = arr[j];
            arr[j] = arr[0];
            arr[0] = temp;
            //?因为每次只调整了第一个和最后一个数，中间的数本来就是大顶堆，所有从0开始
            //反复调整
            //j 数组每次除开最大值、次小值...的大小   每次调整都会变小
            //以后的每一次都是从堆顶开始往下的
            adjustHeap(arr, 0, j);
        }

    }

    /**
     * 核心代码 数组转换为堆的形式
     * 以i 对应的非叶子节点 转换为大顶堆
     *
     * @param arr    待调整数组
     * @param i      非叶子节点在数组中的索引
     * @param length 对多少个元素进行调整,length在逐渐减少
     */
    public static void adjustHeap(int arr[], int i, int length) {
        int temp = arr[i];
        //开始调整
        //1. k=i*2+1是 i节点的左子树
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
            //左子节点小于右子节点
            if (k + 1 < length && arr[k] < arr[k + 1]) {
                //k指向右子节点
                k++;
            }
            //子节点大于父节点
            if (arr[k] > temp) {
                arr[i] = arr[k];
                //i指向k，继续循环比较
                i = k;
            } else {
                break;
            }
        }
        //for结束后，已经得到了以i为父节点的树的最大值，放在了最顶上（局部大顶堆）

        //将temp放到调整后的值
        arr[i] = temp;

    }
}
