package com.huawei.rumen;

import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/29 23:35
 * @version:1.0
 */
public class Hj22 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            if(a==0){
                return;
            }
            int count=0;
            while (a!=0){
                int f=a/3; //喝的瓶数
                count+=f;
                a=a%3+f;//现在的空瓶子数量
                if(a<3){
                    break;
                }
            }
            //有两瓶的时候可以给借一瓶
            if(a==2){
                count++;
            }
            System.out.println(count);

        }
    }
}
