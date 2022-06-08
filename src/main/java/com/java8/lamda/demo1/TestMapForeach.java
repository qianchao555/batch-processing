package com.java8.lamda.demo1;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestMapForeach
 * @Author qianchao
 * @Date 2022/6/8
 * @Version OPRA V1.0
 **/
public class TestMapForeach {
    @Test
    public  void test(){
        Map<String, Map<String,String>> mulMap=new HashMap<>();
        Map<String,String> innerMap=new HashMap<>();
        innerMap.put("F","muF");
        innerMap.put("J","muJ");
        mulMap.put("mu",innerMap);

        mulMap.forEach((k,v)->{
            System.out.print(k+":");
            System.out.println(v);
            StringBuilder stringBuilder=new StringBuilder();
            v.forEach((k1,v1)->{
                stringBuilder.append(v1);
            });
            System.out.println(stringBuilder.toString());
        });
    }

}
