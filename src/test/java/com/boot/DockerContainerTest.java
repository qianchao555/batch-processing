package com.boot;

import com.boot.listener.DockerContainerConfig;
import org.springframework.test.context.TestExecutionListeners;

/**
 * docker配置，所有的单元测试集成该类即可开始进行单元测试
 * @ClassName DockerContainerTest
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
@TestExecutionListeners(DockerContainerConfig.class)
public class DockerContainerTest extends PgDatabaseContainerTest{

}
