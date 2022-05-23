package com.collection.list.iterator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ClassName ConcurrentModificationDemo
 * @Author qianchao
 * @Date 2021/11/18
 * @Version designpattern V1.0
 **/
public class ConcurrentModificationDemo {
    public static void main(String[] args) {
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("qc1");
        arrayList.add("qc2");
        arrayList.add("qc3");
        arrayList.add("qc4");
        arrayList.add("qc5");
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            if("qc2".equals(iterator.next())){
                //Iterator遍历时，集合里面的内部被改变了，触发fail-fast机制，会抛出异常
//                arrayList.remove("qc2");
                iterator.remove();

            }
        }
        System.out.println(arrayList);

    }

}
