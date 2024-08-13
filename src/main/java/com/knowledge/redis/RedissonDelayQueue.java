package com.knowledge.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 基于Redisson的分布式延迟队列实现消息的延迟处理
 *
 * @author Chao C Qian
 * @date 16/07/2023
 */

@Component
@Slf4j
public class RedissonDelayQueue {

    private static final String delay_queue_prefix = "delayQueue:%s:%s";

    @Value("${spring.application.name:'qqcc'}")
    private String applicationName;

    @Autowired
    private RedissonClient redissonClient;

    /*
     * @description 一个队列启用一个线程
     */
    private Map<String, Thread> threadMap = new HashMap<>();


    /**
     * 描述: 添加延时消息
     *
     * @param: t 泛型对象, 需要延时处理的对象
     * @param: delayTime 延时时间
     * @param: timeUnit 时间单位
     * @return: void
     */

    public <T> void addQueue(T t, long delayTime, TimeUnit timeUnit) {
        String queueName = String.format(delay_queue_prefix, applicationName, t.getClass().getName());
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        delayedQueue.offer(t, delayTime, timeUnit);
    }

    /**
     * 延迟处理消息的方法逻辑
     *
     * @return void
     * @param: clazz 消息的泛型类
     * @param: consumer Consumer<T>
     */
    
    public <T> void processEvent(Class clazz, Consumer<T> consumer) {
        String queueName = String.format(delay_queue_prefix, applicationName, clazz.getName());
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        if (!threadMap.containsKey(queueName)) {
            //此线程需要常驻，直接创建即可，不用交给线程池管理。
            //每一个队列，创建一个线程
            Thread thread = new Thread((() -> {
                while (true) {
                    T take = null;
                    try {
                        take = blockingQueue.take();
                    } catch (InterruptedException e) {
                        log.error("处理延迟消息错误：", e);
                    }
                    consumer.accept(take);
                }
            }), "DelayQueue-" + queueName);
            
            threadMap.put(queueName,thread);
            thread.start();
        }
    }

}
