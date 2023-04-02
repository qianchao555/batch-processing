package com.huawei;

import java.util.Scanner;

/**
 * @description:最长公共子串问题
 * 1. 暴力枚举
 * 2. 动态规划
 * @author:xiaoyige
 * @createTime:2023/4/2 22:34
 * @version:1.0
 */
public class Hj65 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while (sc.hasNextLine()){
            String a=sc.nextLine();//短一点的串
            String b=sc.nextLine();//长一点的串
            //比较的时候把短的放在上面，长的放在下面
            //例如
            //abcd123
            //bcd12d34

            //先判断那一个串更长
            if(a.length()>b.length()){
                String temp=a;
                a=b;
                b=temp;
            }
            //暴力
            baoli(a,b);
        }
    }

    private static void baoli(String a, String b) {
        String maxSubString="";
        //从短字符串的第一个开始
        for(int i=0;i<a.length();i++){
            for(int j=i;j<a.length();j++){
                String subString=a.substring(i,j+1);
                if(!b.contains(subString)){
                    break;
                }else {
                    if(subString.length()>maxSubString.length()){
                        maxSubString=subString;
                    }
                }
            }
        }
        System.out.println(maxSubString);
    }

    /**
     * 动态规划
     * 解动态规划需要至少明确3点
     * 1. base case
     * 2. 状态转移方程：一般对于字符串都是利用状态转移表 来帮助我们得到转移方程
     * 3. 自下而上
     * 一言以蔽之就是利用 「base case 」和「状态转移方程」然后「自下而上」得求得最终问题的解
     * @param a
     * @param b
     */
    private static void dp(String a,String b){
        https://blog.csdn.net/weixin_44572229/article/details/121816153
        https://blog.csdn.net/weixin_41055260/article/details/107948303
    }
}
