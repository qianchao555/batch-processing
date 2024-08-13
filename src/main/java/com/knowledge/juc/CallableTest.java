package com.knowledge.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName CallableTest
 * @Author qianchao
 * @Date 2022/7/6
 * @Version   V1.0
 **/
public class CallableTest {
    static ThreadPoolExecutor  executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
   static  CountDownLatch countDownLatch=null;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add(i);
        }
        countDownLatch=new CountDownLatch(list.size());
        List<Future<Integer>> list1=new ArrayList<>();
        list.forEach(o->{
            Callable<Integer> callable = new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    try {
                        //模拟耗时
                        System.out.println("每个线程处理的数据："+o);
                        Thread.sleep(2200);
                        return o + 1;
                    }finally {
                        countDownLatch.countDown();
                    }

                }

            };
            list1.add(executor.submit(callable));
        });
        System.out.println("主线程等待其他线程执行完毕");
        countDownLatch.await();
        System.out.println("for循环处理结束");
        int sum=0;
        for (int i = 0; i < list.size(); i++) {
            sum+=list1.get(i).get();
        }
        System.out.println(sum);
        executor.shutdown();
        executor.shutdownNow();
        while (true){
            if(executor.isTerminated()){
                System.out.println("线程池已经关闭了！");
                break;
            }
        }
    }

}
