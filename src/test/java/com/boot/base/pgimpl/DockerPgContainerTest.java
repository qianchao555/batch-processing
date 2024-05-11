package com.boot.base.pgimpl;

import com.boot.listener.DockerContainerConfig;
import org.springframework.test.context.TestExecutionListeners;

/**
 * docker配置,pg数据库
 * 所有的单元测试继承该类,即可开始进行单元测试
 * @ClassName DockerPgContainerTest
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
@TestExecutionListeners(DockerContainerConfig.class)
public class DockerPgContainerTest extends PgDatabaseContainerTest {

}
