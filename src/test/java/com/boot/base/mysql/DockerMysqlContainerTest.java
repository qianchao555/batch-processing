package com.boot.base.mysql;

import com.boot.base.pgimpl.PgDatabaseContainerTest;
import com.boot.listener.DockerContainerConfig;
import org.springframework.test.context.TestExecutionListeners;

/**
 * docker配置,mysql数据库
 * 所有的单元测试继承该类,即可开始进行单元测试
 * 
 * @ClassName DockerPgContainerTest
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
@TestExecutionListeners(DockerContainerConfig.class)
public class DockerMysqlContainerTest extends MysqlDatabaseContainerTest {

}
