package com.collection.collections;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2022/1/10
 * @Version  V1.0
 **/
public class Demo {
    public static void main(String[] args) {
        String s="123,124";
        List<String> list = Collections.singletonList(s);
        System.out.println(list);
        System.out.println(list.size());
        //language=JSON
        String sss="{\"name\": \"qc\",\"age\": 18,\"add\": \"chengdu\"}";
        String sss2="{\"yhc\": \"ganni\"}";
        System.out.println(sss);
        System.out.println(sss2);
    }

}
