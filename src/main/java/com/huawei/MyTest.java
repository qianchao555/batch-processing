package com.huawei;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2022/6/17
 * @Version V1.0
 **/
public class MyTest {


    public static void main(String[] args) {

        System.out.println(Integer.MAX_VALUE*2);
        System.out.println(Integer.MIN_VALUE*2);
        int[][] arrays = {
                {1, 2, 8, 9},
                {2, 4, 9, 12},
                {4, 7, 10, 13},
                {6, 8, 11, 15}
        };
        int num = 11;
//        tttAb(arrays, num);


    }

    /**
     * 二维数组中是否包含某个数
     * @param arrays
     * @param num
     */
    public static void tttAb(int[][] arrays, int num) {
        int rows = arrays.length; //行
        int cols = arrays[0].length; //列
        int row = 0, col = cols - 1;//从右上角开始
        boolean flag = false;
        while (row <= rows - 1 && col >= 0) {
            if (arrays[row][col] == num) {
                flag = true;
                break;
            } else if (num > arrays[row][col]) {
                row++;
            } else {
                col--;
            }
        }
        if (flag) {
            System.out.println("包含此数字:" + num);
        }


    }


}
