package com.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 *
 * @author Chao C Qian
 * @date 16/07/2023
 */

@Configuration
public class RedissonConfig {
    private static final String FORMAT_STR = "redis://%s:%s";

    @Autowired
    private RedisProperties redisProperties;
    
    
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        String redisUrl = String.format(FORMAT_STR, redisProperties.getHost(),
                redisProperties.getPort() + StringUtils.EMPTY);
        config.useSingleServer().setAddress(redisUrl).setPassword(redisProperties.getPassword());
        config.useSingleServer().setDatabase(3);
        return Redisson.create(config);
    }
}
