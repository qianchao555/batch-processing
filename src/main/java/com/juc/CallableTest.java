package com.juc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName CallableTest
 * @Author qianchao
 * @Date 2022/7/6
 * @Version OPRA V1.0
 **/
public class CallableTest {
    static ThreadPoolExecutor  executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<100000;i++){
            list.add(i);
        }
        List<Future<Integer>> list1=new ArrayList<>();
        list.forEach(o->{
            Callable<Integer> callable = new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    return o + 1;
                }
            };
            list1.add(executor.submit(callable));
        });
        int sum=0;
        for (int i = 0; i < list.size(); i++) {
            sum+=list1.get(i).get();
        }
        System.out.println(sum);
    }

}
