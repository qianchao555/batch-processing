package com.knowledge.collection.set.hashset;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/19 21:56
 * @version:1.0
 */
public class HashSetDemoTwo {
    public static void main(String[] args) {
        HashSet hashSet=new HashSet();
        EmployeeTwo employeeTwo=new EmployeeTwo("qianchao","N",new MyDate("2020","11","3"));
        EmployeeTwo employeeTwo2=new EmployeeTwo("qianchao2","N",new MyDate("2020","11","3"));
        EmployeeTwo employeeTwo3=new EmployeeTwo("qianchao","7",new MyDate("2020","11","3"));
        hashSet.add(employeeTwo);
        hashSet.add(employeeTwo2);
        hashSet.add(employeeTwo3);
        System.out.println(hashSet);
        LinkedHashSet linkedHashSet=new LinkedHashSet();
        linkedHashSet.add("12");
    }
}
