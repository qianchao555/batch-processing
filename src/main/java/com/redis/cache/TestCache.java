package com.redis.cache;

import com.redis.CustomRedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestCache {
    
    @Autowired
    CustomRedisTemplate customRedisTemplate;
    
    //本地缓存测试
    @PostMapping("/testLocalCache")
//    @Cacheable(cacheNames = CbaCacheConfig.CacheNameSpace.CACHE_5_MIN,cacheManager = CbaCacheConfig.CacheManageName.cbaCacheManageName
//    )
    public String t(String arg){
        //利用上下文，解决aop不生效问题：利用容器上下文来找到bean，解决aop失效问题
        TestCache bean = InnerInvokeCacheSpringContextSupport.getBean(TestCache.class);
        String cacheVale = bean.getCacheVale();
        
        System.out.println(arg+cacheVale);
        return cacheVale+"你好！";
    }
    
    
    //不指定缓存管理器 — 默认用redis缓存
    @Cacheable(cacheNames = CbaCacheConfig.CacheNameSpace.CACHE_5_MIN)

    //指定缓存管理器-这里用的是内存来缓存
//    @Cacheable(cacheNames = CbaCacheConfig.CacheNameSpace.CACHE_5_MIN,cacheManager = CbaCacheConfig.CacheManageName.cbaCacheManageName
//    )
    public  String getCacheVale(){
        log.info("进入这个方法了！");
        return "缓存中的值";
    }
}


