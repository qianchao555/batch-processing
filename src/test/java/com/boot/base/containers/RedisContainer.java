package com.boot.base.containers;

import lombok.Getter;
import org.junit.rules.ExternalResource;
import org.testcontainers.containers.GenericContainer;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.containers.GenericContainer;

/**
 * 模拟redis容器，暴露6379端口
 * @Author qianchao
 * @Date 2022/9/5
 **/
public class RedisContainer extends ExternalResource {

    private GenericContainer redisGenericContainer;

    @Getter
    private int port = 6379;

    /**
     * 构造函数
     *
     * @param version
     */
    public RedisContainer(String version) {
        this(version, 6379);
    }

    /**
     * 构造函数
     *
     * @param version
     * @param port
     */
    public RedisContainer(String version, int port) {
        this.port = port;
        if (redisGenericContainer == null) {
            redisGenericContainer = new GenericContainer(version)
                    .withExposedPorts(port);
            //启动redis
            redisGenericContainer.start();
            //获取容器的IP地址，如果不设置就是localhost
            System.setProperty("spring.redis.host", redisGenericContainer.getContainerIpAddress());
            //端口映射（暴露端口）
            System.setProperty("spring.redis.port", redisGenericContainer.getMappedPort(6379).toString());
        }

    }

}
