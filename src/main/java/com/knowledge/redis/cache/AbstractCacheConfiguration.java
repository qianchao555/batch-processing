package com.knowledge.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Chao C Qian
 * @description springCache整合redis实现缓存
 * @date 09/07/2023
 */

@Slf4j
public abstract class AbstractCacheConfiguration extends CachingConfigurerSupport {

    /**
     * @description cba缓存空间名称，这里也可以不用定义这么死，可以做成其他模块能适应那种
     * @author Chao C Qian
     * @date 09/07/2023
     */
    public static final String cache_space_name = "cba_cache_space";

    private static final String splitter = ":";

    //默认缓存1天
    private static final int days = 1;

    @Autowired
    Environment env;

    

    /**
     * 可配置不同缓存空间对应的过期时间
     *
     * @return Map<String, Long> k-缓存名称，v-分钟
     */
    protected abstract Map<String, Long> defaultCacheNamesExpiredTime();


    /**
     * @description 配置缓存空间里 key生成策略, 默认是根据参数作为key 有可能会重复
     * 类名：方法名：参数
     * @author Chao C Qian
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return ((o, method, argObjects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName())
                    .append(splitter)
                    .append(method.getName())
                    .append(splitter);
            for (Object arg : argObjects) {
                sb.append(arg == null ? "null" : arg.toString());
            }
            return sb.toString();
        });

    }

    
    
    /*
     * @description TODO
     * @params [redisConnectionFactory]
     * @return RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(env.getProperty("spring.cache.default.expired", Integer.class, days)))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new KryoRedisSerializer<>(Object.class)))
                //缓存空间名字前缀，这里可以定义一个抽象方法，各自模块去定义各自的前缀
                .prefixCacheNameWith(cache_space_name);

        Map<String, Long> cacheNamesExpiredTimeMap = this.defaultCacheNamesExpiredTime();
        
        //为每个自定义的 缓存空间设置过期时间
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        Set<String> cacheNamesSet = new HashSet<>();
        if (cacheNamesExpiredTimeMap != null && !cacheNamesExpiredTimeMap.isEmpty()) {
            cacheNamesSet.addAll(cacheNamesExpiredTimeMap.keySet());
            cacheNamesExpiredTimeMap.entrySet().forEach(e ->
                    configMap.put(e.getKey(), redisCacheConfiguration.entryTtl(Duration.ofMinutes(e.getValue())))
            );
        }
        //根据redis缓存配置和redis连接工厂 生成redis缓存管理器
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .initialCacheNames(cacheNamesSet)
                .withInitialCacheConfigurations(configMap).build();
    }


    /**
     * @description 自定义缓存异常处理器
     * @author Chao C Qian
     */

    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        //用于捕获从Cache中进行CRUD时的异常的回调处理器
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                doHandlerCacheError(exception, key);
            }


            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                doHandlerCacheError(exception, key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                doHandlerCacheError(exception, key);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                doHandlerCacheError(exception, null);
            }
        };
    }

    protected void doHandlerCacheError(RuntimeException exception, Object key) {
        log.warn("Redis存在异常，key = {}", key, exception);
    }

}
