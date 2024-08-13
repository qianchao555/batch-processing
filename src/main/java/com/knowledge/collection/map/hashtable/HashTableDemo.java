package com.knowledge.collection.map.hashtable;

import java.util.Hashtable;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/20 23:01
 * @version:1.0
 */
public class HashTableDemo {
    public static void main(String[] args) {
        Hashtable hashTable=new Hashtable();
        hashTable.put("john",100);
        hashTable.put(null,100);//报错
//        hashTable.put(100,null);//报错
        hashTable.put("john",200);//替换
    }
}
