package com.datastructure.sparsearray;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/21 20:35
 * @version:1.0
 */
public class SparseArray {
    public static void main(String[] args) {
        /**
         * 原始的二维数组
         * 0：表示没有棋子
         * 1：表示黑子
         * 2：表示蓝子
         */
        int chessArr1[][] = new int[11][11];
        //二行三列有一颗白子
        chessArr1[1][2] = 1;
        chessArr1[1][3]=1;
        //三行四列有一颗蓝子
        chessArr1[2][3] = 2;
        for (int[] row : chessArr1) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
        /**
         * 转换为稀疏数组思路
         */
        //1.遍历二维数组得到非0数据的个数
        int sum=0;
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                if(chessArr1[i][j]!=0){
                    sum++;
                }
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();
        //2.创建稀疏数组
        int spareseArr[][]=new int[sum+1][3];
        //稀疏数组赋值
        spareseArr[0][0]=11;//行
        spareseArr[0][1]=11;//列
        spareseArr[0][2]=sum;//二维数组非0的个数

        //遍历二维数组，将非0数据存放到稀疏数组中
        int count=0;//记录是第几个非0数据
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                if(chessArr1[i][j]!=0){
                    count++;
                    spareseArr[count][0]=i;
                    spareseArr[count][1]=j;
                    spareseArr[count][2]=chessArr1[i][j];
                }
            }
        }
        for (int[] row : spareseArr) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();


        /**
         * 稀疏矩阵恢复为二维数组
         */
        //1.读取稀疏数组第一行，创建二维数组
        int [][]chessArr2=new int[spareseArr[0][0]][spareseArr[0][1]];
        //2. 读取稀疏数组剩下的行，赋值到二维数组
        for(int i=1;i<spareseArr.length;i++){
            chessArr2[spareseArr[i][0]][spareseArr[i][1]]=spareseArr[i][2];
        }
        for (int[] row : chessArr2) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }

    }
}
