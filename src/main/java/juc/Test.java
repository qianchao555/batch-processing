package juc;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,2,1,TimeUnit.SECONDS,new ArrayBlockingQueue(10));
//        threadPoolExecutor.execute();
        HashMap
    }
}
