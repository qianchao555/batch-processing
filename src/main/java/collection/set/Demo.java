package collection.set;

import java.util.*;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/16 20:38
 * @version:1.0
 */
public class Demo {
    public static void main(String[] args) {
        Set set=new HashSet();
        set.add("a");
        set.add("a");
        set.add("b");
        set.add(null);
        set.add(null);
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object next =  iterator.next();

        }
        String s = new String("a");
        String s1 = new String("a");
        String s2="a";
        System.out.println(s.equals(s1));
        System.out.println(s==s2);
        System.out.println(s1==s2);
        System.out.println("a".equals(null));


    }
}
