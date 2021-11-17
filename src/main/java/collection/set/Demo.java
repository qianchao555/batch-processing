package collection.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    }
}
