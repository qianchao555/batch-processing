package com.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 延迟队列-测试
 *
 * @author Chao C Qian
 * @date 16/07/2023
 */
@RestController
public class RedissonDelayQueueTest {
    @Autowired
    RedissonDelayQueue redissonDelayQueue;
    
    /**
     * 初始化，根据不同的类型，创建不同的消费逻辑
     * 这里就可以根据自己的业务场景优化了
     * 
     * @param: 
     * @return void
     */ 
    
    
    @PostConstruct
    public void init(){
        Consumer consumer=(t)->{
            System.out.println(t);
        };
        redissonDelayQueue.processEvent(String.class,consumer);
    }

    @PostConstruct
    public void init2(){
        Consumer consumer=(t)->{
            System.out.println("我是延迟10s"+t);
        };
        redissonDelayQueue.processEvent(Integer.class,consumer);
    }
    
    @PostMapping("testQueue")
    public void testDelayQueue(String message){
        redissonDelayQueue.addQueue(message,5, TimeUnit.SECONDS);
        redissonDelayQueue.addQueue(new Integer(10),10, TimeUnit.SECONDS);
    }
}
