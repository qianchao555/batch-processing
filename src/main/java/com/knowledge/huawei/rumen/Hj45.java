package com.knowledge.huawei.rumen;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/4/3 22:28
 * @version:1.0
 */
public class Hj45 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();
        String []strArr=new String[n];
        for(int i=0;i<n;i++){
            strArr[i]=sc.nextLine();
            char[] charOneArr = strArr[i].toCharArray();
            System.out.println(compute(charOneArr));
        }
    }

    private static int compute(char[] charOneArr) {
        int []s=new int[27];
        for(int i=0;i<charOneArr.length;i++){
            //统计每个字母出现多少次
            s[charOneArr[i]- 96]++;
        }
        //排序字母出现的次数
        Arrays.sort(s);
        int p=26;
        int sum=0;
        for(int i=s.length-1;i>=0;i--){
            sum+=s[i]*p;
            p--;
        }
        return sum;
    }
}
