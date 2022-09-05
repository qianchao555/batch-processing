package com.boot.containers;

import org.junit.rules.ExternalResource;

/**
 * 模拟redis容器，暴露6379端口
 * @Author qianchao
 * @Date 2022/9/5
 **/
public class RedisContainer extends ExternalResource {

    private GenericContainer genericContainer;
    private String port;

    /**
     * 构造函数
     * @param version
     */
    public RedisContainer (String version){
        this(version,"6379");
    }

    /**
     * 构造函数
     * @param version
     * @param port
     */
    public RedisContainer (String version,String port){

    }

}
