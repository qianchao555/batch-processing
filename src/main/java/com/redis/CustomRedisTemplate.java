package com.redis;

import com.redis.cache.KryoRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import jakarta.annotation.Resource;


@Configuration
public class CustomRedisTemplate {
    
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    
    
   
    /*
     * @description 自定义redisTemplate
     * @params []
     * @return RedisTemplate<?,?>
     */
    @Bean
    public RedisTemplate<?,?> redisTemplate(){
        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new KryoRedisSerializer(Object.class));
        
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new KryoRedisSerializer(Object.class));
        return redisTemplate;
    }
        
}
