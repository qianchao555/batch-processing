package com;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2022/6/17
 * @Version  V1.0
 **/
public class Test {
    public static void main(String[] args) {
        String[] split = "UData,OWData,Sc,IData".split(",");

        if(ArrayUtils.contains(split,"ABC")){
            System.out.println("ABC在："+ Arrays.toString(split)+"数组中");
        }else {
            System.out.println("ABC不在："+Arrays.toString(split)+"数组中");
        }

        if(ArrayUtils.contains(split,null)){
            System.out.println("空串在"+ Arrays.toString(split)+"数组中");
        }else {
            System.out.println("空串不在："+Arrays.toString(split)+"数组中");
        }

        if(ArrayUtils.contains(split,"IData")){
            System.out.println("IData在："+ Arrays.toString(split)+"数组中");
        }else {
            System.out.println("IData不在："+Arrays.toString(split)+"数组中");
        }

    }

}
