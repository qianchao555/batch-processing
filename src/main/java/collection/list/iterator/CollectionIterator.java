package collection.list.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/16 21:03
 * @version:1.0
 */
public class CollectionIterator {
    public static void main(String[] args) {
        Collection collection = new ArrayList();
        collection.add(new Book("金庸", "三国"));
        collection.add(new Book("钱超", "钱超写的书"));
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);
        }
//        iterator.next()这里会抛出异常，因为此时迭代器已经指向最后一个元素了
        /**
         * 增强For 是简化版的Iterator
         * 底层实现：Iterator
         */
        for (Object o : collection) {
            System.out.println(o);
        }

    }
}

class Book {
    private String author;
    private String name;

    public Book(String author, String name) {
        this.author = author;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
