package com.knowledge.huawei;

import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/4/1 23:36
 * @version:1.0
 */
public class String16 {
    public static void main(String[] args) {

        //方式一：自带API
//        String s="0xAA";
//        int i = Integer.parseInt(s.substring(2), 16);
//        System.out.println(i);

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
//            long num = 0;
//            for (int i = 2; i < line.length(); i++) {
//                num *= 16;
//                char ch = line.charAt(i);
//                int n = 0;
//                if (ch >= 'A' && ch <= 'F') {
//                    n = ch - 'A' + 10;
//                } else if (ch >= 'a' && ch <= 'f') {
//                    n = ch - 'a' + 10;
//                } else {
//                    n = ch - '0';
//                }
//                num += n;
//            }
//            System.out.println(num);




            //这种方式更好理解


            //最低位的权重
            int weight = 1;
            int sum = 0;
            for (int i = line.length() - 1; i >= 0; i--) {
                //记录当前位置对应的10进制数值
                int n = 0;
                char c = line.charAt(i);
                if (c >= '0' && c <= '9') {
                    n = c - '0';
                } else if (c >= 'A' && c <= 'F') {
                    n = c - 'A' + 10;
                } else if (c >= 'a' && c <= 'f') {
                    n = c - 'a' + 10;
                }
                sum = sum + n * weight;

                //下一次的权重需要扩大16倍
                weight = weight * 16;
            }
            System.out.println(sum);
        }
    }
}
