package com.huawei.rumen;

import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/30 23:01
 * @version:1.0
 */
public class Hj59 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        //标记当前字符出现的次数


        while (in.hasNextLine()) {
            int count=0;
            String str=in.nextLine();
            char[] chars = str.toCharArray();
            for(int i=0;i<chars.length;i++){
               count=0;
                for(int j=0;j<chars.length;j++){
                    if(chars[i]==chars[j]){
                        count++;
                    }
                }
                //当前位置只出现了一次
                if(count==1){
                    System.out.println(chars[i]);
                    break;
                }
            }
            if(count!=1){
                System.out.println(-1);
            }


        }
    }
}
