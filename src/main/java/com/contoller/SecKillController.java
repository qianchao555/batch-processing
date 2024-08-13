package com.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/21 20:59
 * @version:1.0
 */

@RestController
public class SecKillController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @param uid    用户Id
     * @param prodId 商品Id
     * @return
     */
    @RequestMapping("/redisSecKill")
    public boolean secKill(@RequestParam String uid, @RequestParam String prodId) {
        if (uid == null || prodId == null) {
            return false;
        }
        //连接redis
        Jedis jedis = new Jedis("", 6379);
        jedis.auth("");
        //库存key  key-string
        String kcKey = "sk:" + prodId + ":qt";
        //秒杀成功用户的key  key-set
        String userKey = "sk:" + uid + ":user";
        //建视库存
        jedis.watch(kcKey);
        //获取库存
        String kc = jedis.get(kcKey);
        if (kc == null) {
            System.out.println("秒杀还未开始，请等待。。。");
            jedis.close();
            return false;
        }
        //判断用户是否重复秒杀
        Boolean sismember = jedis.sismember(userKey, uid);
        if (sismember) {
            System.out.println("用户已经秒杀成功过了，不能重复秒杀！");
            jedis.close();
            return false;
        }
        //判断库存是否大于0
        int kcInt = Integer.parseInt(kc);
        if (kcInt <= 0) {
            System.out.println("秒杀已经结束了。");
            jedis.close();
            return false;
        }
        //秒杀过程
        Transaction multi = jedis.multi();
        //组队操作
        multi.decr(kcKey);
//        jedis.decr(kcKey);
        //秒杀成功用户添加进成功清单
//        jedis.sadd(userKey, uid);
        multi.sadd(userKey, uid);
        List<Object> exec = multi.exec();
        if (exec == null || exec.size() == 0) {
            System.out.println("秒杀失败");
            jedis.close();
            return false;
        }
        System.out.println("秒杀成功了！");
        jedis.close();
        return true;
    }
}
