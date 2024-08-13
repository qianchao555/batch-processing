package com.knowledge.huawei.rumen;

import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/30 21:26
 * @version:1.0
 */
public class Hj55 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int count = 0;
            for (int i = 7; i <= n; i++) {
                //整除的情况 或包含7就算一个
                if (i % 7 == 0 || contains(i)) {
                   count++;
                }
            }
            System.out.println(count);
        }
    }

    private static boolean contains(int i) {
        int tmp = i;
        while (tmp>= 7) {
            if (tmp % 10 == 7) {
                return true;
            }
            tmp = tmp / 10;
        }
        return false;
    }
}
