package com.huawei;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/4/1 23:14
 * @version:1.0
 */
public class mingmingRandom {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            TreeSet<Integer> number=new TreeSet<Integer>();
            int m=scanner.nextInt();
            if(m>0){
                for(int i=0;i<m;i++){
                    number.add(scanner.nextInt());
                }
            }
            for(Integer i:number){
                System.out.println(i);
            }
        }
    }
}
