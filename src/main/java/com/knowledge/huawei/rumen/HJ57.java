package com.knowledge.huawei.rumen;

import java.util.Scanner;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/30 22:11
 * @version:1.0
 */
public class HJ57 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            //解法一：BigInteger  so easy
//            String str1=in.nextLine();
//            String str2=in.nextLine();
//            BigInteger bigInteger=new BigInteger(str1);
//            BigInteger bigInteger2=new BigInteger(str2);
//            System.out.println(bigInteger.add(bigInteger2));
            //解法二：按位置加法，有进位则进位要循环来加，最终有进位，进位放在最前面
            String str1 = in.nextLine();
            String str2 = in.nextLine();
            int str1Length = str1.length();
            int str2Length = str2.length();
            int maxLength=Math.max(str1Length,str2Length);
            String s1 = new StringBuilder(str1).reverse().toString();
            String s2 = new StringBuilder(str2).reverse().toString();

            StringBuilder sb=new StringBuilder();
            //进位
            int carry=0;
            for(int i=0;i<maxLength;i++){
                int c1=0;
                int c2=0;
                if(str1Length>i){
                    c1=s1.charAt(i)-'0';
                }

                if(str2Length>i){
                   c2=s2.charAt(i)-'0';
                }

                //对应位置相加，再加进位
                int sum=c1+c2+carry;

                //加起来后，会产生进位
                carry=sum/10;

                //对应位置相加得到的数
                sb.append(sum%10);

            }
            //最高位是否有进位
            if(carry>0){
                sb.append(carry);
            }
            System.out.println(sb.reverse().toString());
        }
    }
}
