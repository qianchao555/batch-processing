package com.boot.base.mysql;

import com.boot.base.BaseContainerTest;
import com.boot.base.containers.MysqlImageContainer;
import org.junit.ClassRule;

/**
 * @ClassName DatabaseContainerTest
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
public class MysqlDatabaseContainerTest extends BaseContainerTest {

    /**
     * mysql镜像库
     * url为docker服务器的ip地址
     */
    @ClassRule
    MysqlImageContainer mysqlImageContainer =
            new MysqlImageContainer("mysql:5.7.22", "integration-test-db", "test", "test", "init.sql")
                    .setJdbcUrl("datasource.url")
                    .setJdbcUserName("datasource.username")
                    .setJdbcPassWord("datasource.password");
}
