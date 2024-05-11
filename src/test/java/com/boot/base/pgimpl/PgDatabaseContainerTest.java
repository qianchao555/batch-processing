package com.boot.base.pgimpl;

import com.boot.base.BaseContainerTest;
import com.boot.base.containers.PgContainer;
import org.junit.ClassRule;

/**
 * pg镜像库
 * @ClassName PgDatabaseContainerTest
 * @Author qianchao
 * @Date 2022/9/6
 * @Version V1.0
 **/
public class PgDatabaseContainerTest extends BaseContainerTest {
    @ClassRule
    PgContainer postgre =
            new PgContainer("postgres:11.1", "integration-test-db", "postgres", "postgres", "init.sql")
                    .setJdbcUrl("datasource.url")
                    .setJdbcUserName("datasource.username")
                    .setJdbcPassWord("datasource.password");
}
