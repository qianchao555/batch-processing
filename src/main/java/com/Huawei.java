package com;

import java.util.Scanner;

/**
 * @Description:TODO
 * @ClassName Huawei
 * @Author qianchao
 * @Date 2023/3/28
 * @Version V1.0
 **/
public class Huawei {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            int n = in.nextInt();
            int[]arrN=new int[n];
            for(int i=0;i<n;i++){
                arrN[i]=in.nextInt();
            }
            int fushu=0;
            int zhengshu=0;
            int sum=0;
            for(int i=0;i<arrN.length;i++){
                if(arrN[i]<0){
                    fushu++;
                }if(arrN[i]>0) {
                    zhengshu++;
                    sum+=arrN[i];
                }
            }
            System.out.println("负的个数"+fushu+" 正的个数"+zhengshu+" sum="+sum+" 平均数为："+sum/(zhengshu*1.0));

        }
    }
}
