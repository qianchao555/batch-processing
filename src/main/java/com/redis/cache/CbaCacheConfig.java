package com.redis.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
@Slf4j
public class CbaCacheConfig extends AbstractCacheConfiguration {

    /**
     * 根据业务选定不同的缓存池,每个缓存空间有自己的过期时间
     * 不选择缓存池默认为一天
     */
    @Override
    protected Map<String, Long> defaultCacheNamesExpiredTime() {
        Map<String, Long> caches = new HashMap<>();
        caches.put(CacheNameSpace.CACHE_5_MIN, 5L);
        caches.put("Data:cache-60min", 60L);
        return caches;
    }

    @Override
    public KeyGenerator keyGenerator() {
        log.debug("使用的抽象类里面自定义的key计算策略,自己也可以在这里自定义");
        return super.keyGenerator();
    }

    /**
     * @description 不指默认使用redis缓存
     * @author Chao C Qian
     */
    @Override
    @Bean
    @Primary
    public CacheManager cacheManager() {
        log.info("初始化了redis缓存池");
        return super.cacheManager();
    }

    /**
     * @return org.springframework.cache.CacheManager
     * @description 使用：本地内存 来缓存
     * @author Chao C Qian
     * @params org.springframework.cache.CacheManager
     */
//    @Bean(name = "cbaSimpleCacheManager")
    public CacheManager cbaSimpleCacheManager() {
        log.info("初始化本地内存缓存池");
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        Map<String, Long> cachesMap = defaultCacheNamesExpiredTime();
        List<CaffeineCache> cacheList = new ArrayList<>();
        Preconditions.checkArgument(!MapUtils.isEmpty(cachesMap), "请配置缓存池!");
        cachesMap.forEach(
                (k, v) -> {
                    //为每一个缓存空间设置过期时间
                    CaffeineCache cache = new CaffeineCache(k, 
                            Caffeine.newBuilder()
                             //统计功能，后期可以开发个接口来查询
                            .recordStats()
                             //访问后经过固定时间过期
                            .expireAfterWrite(v, TimeUnit.MINUTES)
                            //最大缓存条数,即key的最大个数
                            .maximumSize(4000L)
                            .build(), 
                            //允许null值，即方法返回null时候，也会缓存，这个应该改为false
                            true);
                    cacheList.add(cache);
                });
        simpleCacheManager.setCaches(cacheList);
        return simpleCacheManager;
    }
    
    /**
     * @description 缓存空间名称
     */
    public  static final class CacheNameSpace{
        private CacheNameSpace(){}

        /**
         * 5分钟缓存组 用于放置 临时缓存
         * 大部分集中处理
         * 当选择内存缓存器时 只允许使用改变量
         * defualt
         * 推荐使用
         */
        public static final String CACHE_5_MIN = "Data:cache-5min";
        /**
         * 30分钟缓存组
         * 大部分非集中处理缓存地方
         */
        public static final String CACHE_30_MIN = "Data:cache-30mim";
        /**
         * 24小时缓存组
         * 长时间缓存放置处
         */
        public static final String CACHE_24_H = "Data:cache-24h";
    }
    
    public static final class CacheManageName{
        public CacheManageName(){}
        
        public static final String cbaCacheManageName="cbaSimpleCacheManager";
    }
}
