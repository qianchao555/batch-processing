package juc;

import java.util.ArrayList;
import java.util.concurrent.*;

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
        Long a=10l;
        a=null;
        Integer b=null;
         System.out.println(b==null?null:Long.valueOf(5));
//        threadPoolExecutor.execute();
    }
}
