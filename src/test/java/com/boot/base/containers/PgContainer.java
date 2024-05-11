package com.boot.base.containers;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @ClassName PgContainer
 * @Author qianchao
 * @Date 2022/9/6
 * @Version V1.0
 **/
@Slf4j
public class PgContainer extends ExternalResource {
    private PostgreSQLContainer postgre;

    /**
     * @param version  数据库脚本
     * @param dbName   数据库名
     * @param userName 数据库用户名
     * @param passWord 数据库密码
     * @param initSql      初始化sql脚本
     */
    public PgContainer(String version, String dbName, String userName, String passWord, String initSql) {
            if(postgre==null){
                postgre= (PostgreSQLContainer) new PostgreSQLContainer(version).withDatabaseName(dbName)
                        .withUsername(userName).withPassword(passWord)
                        .withInitScript(initSql);
                //最大连接数
                postgre.addEnv("max_connections","1000");
                postgre.start();
            }
            System.setProperty("datasource.driver-class-name","org.postgresql.Driver");
            log.info("postgresql:jdbc是：{}",postgre.getJdbcUrl());
    }

    /**
     * 设置jdbc url
     * @param propertyName 数据库配置的属性名
     */
    public PgContainer setJdbcUrl(String propertyName){
        System.setProperty(propertyName,postgre.getJdbcUrl());
        return this;
    }

    /**
     * 设置数据库userName
     * @param propertyName 数据库配置的属性名  eg:datasource.username
     */
    public PgContainer setJdbcUserName(String propertyName){
        System.setProperty(propertyName,postgre.getUsername());
        return this;
    }
    /**
     * 设置数据库PassWord
     * @param propertyName 数据库配置的属性名
     */
    public PgContainer setJdbcPassWord(String propertyName){
        System.setProperty(propertyName,postgre.getPassword());
        return this;
    }

}
