package com.boot.base;

import com.boot.listener.DataSetExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 单元测试基类
 * @Author qianchao
 * @Date 2022/9/5
 **/
@Slf4j
@TestExecutionListeners(DataSetExecutionListener.class)
public abstract class BaseContainerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    static {
        log.info("这里做一些基本的默认设置");
    }

    @Before
    /**
     * MockMvcWebTest,默认使用webApplicationContext.yml做配置文件
     */
    public void prepareMvc(){
        mockMvc= MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * 容器的启动,若需要其他容器也是类似添加即可，例如kafka,es等等
     */
//    @ClassRule
//    public static RedisContainer redisContainer=new RedisContainer("redis:5.0.0");

}
