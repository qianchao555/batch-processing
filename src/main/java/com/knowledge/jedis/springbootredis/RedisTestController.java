package com.knowledge.jedis.springbootredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:xiaoyige
 * @createTime:2021/12/19 20:50
 * @version:1.0
 */
@RestController
@RequestMapping("/redisTest")
public class RedisTestController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public String set() {
        //设置值到redis
        redisTemplate.opsForValue().set("myKey", "myValue");
        //取
        String myKey = redisTemplate.opsForValue().get("myKey");
        System.out.println(myKey);
        return myKey;
    }
}
