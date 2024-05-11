package com.boot.base.containers;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.testcontainers.containers.MySQLContainer;

/**
 * @ClassName PgContainer
 * @Author qianchao
 * @Date 2022/9/6
 * @Version V1.0
 **/
@Slf4j
public class MysqlContainer extends ExternalResource {
    private MySQLContainer mySQLContainer;

    /**
     * @param version  数据库脚本
     * @param dbName   数据库名
     * @param userName 数据库用户名
     * @param passWord 数据库密码
     * @param initSql      初始化sql脚本
     */
    public MysqlContainer(String version, String dbName, String userName, String passWord, String initSql) {
            if(mySQLContainer==null){
                new MySQLContainer<>(version)
//                        .withDatabaseName(dbName)
//                        .withUsername(userName).withPassword(passWord)
                        .withInitScript(initSql);
                mySQLContainer.setPrivilegedMode(true);
                mySQLContainer.setCommand("--lower-case-table-names=1");
                mySQLContainer.start();
            }
            System.setProperty("datasource.driver-class-name",mySQLContainer.getDriverClassName());
            log.info("postgresql:jdbc是：{}",mySQLContainer.getJdbcUrl());
    }

    /**
     * 设置jdbc url
     * @param propertyName 数据库配置的属性名
     */
    public MysqlContainer setJdbcUrl(String propertyName){
        System.setProperty(propertyName,mySQLContainer.getJdbcUrl()+"?useUnicode=true&characterEncoding=utf-8");
        return this;
    }

    /**
     * 设置数据库userName
     * @param propertyName 数据库配置的属性名  eg:datasource.username
     */
    public MysqlContainer setJdbcUserName(String propertyName){
        System.setProperty(propertyName,mySQLContainer.getUsername());
        return this;
    }
    /**
     * 设置数据库PassWord
     * @param propertyName 数据库配置的属性名
     */
    public MysqlContainer setJdbcPassWord(String propertyName){
        System.setProperty(propertyName,mySQLContainer.getPassword());
        return this;
    }

}
