package com;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2022/6/17
 * @Version  V1.0
 **/
public class MyTest {
    final int i;
    public MyTest(){
        i=0;
    }
    public static void main(String[] args) {
        final int i=10;
        String[] split = "UData,OWData,Sc,IData".split(",");

        if(ArrayUtils.contains(split,"ABC")){
            System.out.println("ABC在："+ Arrays.toString(split)+"数组中");
        }else {
            System.out.println("ABC不在："+Arrays.toString(split)+"数组中");
        }

        List<Integer> collect = Stream.of("UData","OWData","Sc","IData")
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(collect);

    }



}
