package com.collection.list.linkedlist;

import com.util.annotation.thinkinginjava.mytestannotation.MyTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @ClassName ResourceDemo
 * @Author qianchao
 * @Date 2021/11/18
 * @Version designpattern V1.0
 **/
public class ResourceDemo {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<Integer> arrayList=new ArrayList<>();
        arrayList.add(123);
        Method method = arrayList.getClass().getMethod("add",Object.class);
        method.invoke(arrayList,"泛型是Integer但是我添加的是String");
        arrayList.forEach(x-> System.out.println(x));

//        LinkedList<String> linkedList=new LinkedList<>();
//        linkedList.add("1");
//        linkedList.add("wo");
//        linkedList.add(1,"charu");
    }

    @Test
    public void test(){
        System.out.println("userVO".equalsIgnoreCase("uservo"));
        System.out.println("uservo".equalsIgnoreCase("userVO"));
        System.out.println("userVo".equalsIgnoreCase("userVO"));
    }

}
