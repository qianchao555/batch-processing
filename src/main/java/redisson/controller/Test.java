package redisson.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class Test {
    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("myTest")
    public void getLock() throws InterruptedException {
        System.out.println("----");
        RLock rLock = redissonClient.getLock("myLock");
        try {
            boolean res = rLock.tryLock(0, TimeUnit.SECONDS);
            if (res) {
                System.out.println("getLock Success");
                Thread.sleep(5000);
            } else {
                System.out.println("getLock failure");
            }
            rLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
