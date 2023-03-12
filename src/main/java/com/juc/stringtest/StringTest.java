package com.juc.stringtest;

import jodd.util.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:TODO
 * @ClassName StringTest
 * @Author qianchao
 * @Date 2023/2/28
 * @Version V1.0
 **/
public class StringTest {
    public static AtomicInteger race = new AtomicInteger(0);
    private static final int THREADS_COUNT = 5;
    @Autowired
    RedissonClient redissonClient;

    private int a;

    @Test
    public void testRedisson() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://1.14.61.28:6379")
                .setPassword("991618ab@123");
        RedissonClient client = Redisson.create(config);
        RLock lock=client.getLock("sss");
        lock.lock(15, TimeUnit.SECONDS);
        ThreadUtil.sleep(20000);
        lock.unlock();

        //基于Redisson的分布式布隆过滤器
        client.getBloomFilter("sampleBloomFilter");
    }

    public static void increase() {

        race.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread[] threads = new Thread[THREADS_COUNT];
//        for (int i = 0; i < THREADS_COUNT; i++) {
//            threads[i] = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 10; i++) {
//                        increase();
//                    }
//                }
//            });
//            threads[i].start();
//        }
//        Thread.sleep(2000);
//        System.out.println(race);
       Object MyObject=new Object();
        System.out.println(MyObject);

    }

}
