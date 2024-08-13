package com.knowledge.javase.generic;

import com.knowledge.javase.reflection.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 泛型
 * @author:xiaoyige
 * @createTime:2022/2/23 14:37
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) throws IllegalAccessException {
        /**
         * 对于 List<? super Integer> l1:
         * 正确的理解: ? super Integer 限定的是泛型参数. 令 l1 的泛型参数是 T, 则 T 是 Integer 或 Integer 的父类, 因此 Integer 或 Integer 的子类的对象就可以添加到 l1 中.
         * 错误的理解: ? super Integer限定的是插入的元素的类型, 因此只要是 Integer 或 Integer 的父类的对象都可以插入 l1 中
         * 对于 List<? extends Integer> l2:
         * 正确的理解: ? extends Integer 限定的是泛型参数. 令 l2 的泛型参数是 T, 则 org.apache.poi.ss.formula.functions.T 是 Integer 或 Integer 的子类, 进而我们就不能找到一个类 X, 使得 X 是泛型参数 T 的子类, 因此我们就不可以向 l2 中添加元素. 不过由于我们知道了泛型参数 T 是 Integer 或 Integer 的子类这一点, 因此我们就可以从 l2 中读取到元素(取到的元素类型是 Integer 或 Integer 的子类), 并可以存放到 Integer 中.
         * 错误的理解: ? extends Integer 限定的是插入元素的类型, 因此只要是 Integer 或 Integer 的子类的对象都可以插入 l2 中
         */

        //泛型代表某一个范围内的固定类
        // 泛型参数 ？ 表示Number以及Number的子类型  限定的是泛型参数,而非限定的是插入元素的类型
        List<? extends Number> numbers;
        //<? extends T>只能get,不能add 【add(null)除外】
        List<Integer> list = Arrays.asList(1, 2);
        numbers = list;
        for (Number number : numbers) {
            System.out.println(number);
        }

        List<? super Number> lists = new ArrayList<Object>();
        lists.add(12);
        lists.add(1.1f);
        lists.add(new Integer(12));
        //编译报错：因为只能添加<? super T> T类型以及T类型的子类型，这里？参数类型不一定是Object
        //list.add(new Object());

        List<?> p=new ArrayList<>();
        User user=new User("qianchao",1,12);
        User user2=new User("qianchao2",2,222);
        p = Arrays.asList(user,user2);
        for(int i=0;i<p.size();i++){
            Field[] declaredFields = p.get(i).getClass().getDeclaredFields();
            System.out.println(declaredFields);
            Object o = p.get(i);
            for (Field field : declaredFields) {
                field.setAccessible(true);
                System.out.print(field+" ");
                System.out.println(field.get(o));
            }
        }

    }
}
