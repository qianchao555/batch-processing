package juc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/4/18 20:50
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) {
        ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap();
        concurrentHashMap.put("name","qc");
        concurrentHashMap.get("name");
        ReentrantReadWriteLock
    }
}
