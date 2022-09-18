package com.boot;

import com.boot.containers.RedisContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName 单元测试基类
 * @Author qianchao
 * @Date 2022/9/5
 **/
public abstract class BaseContainerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    static {
        //做一些默认设置
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
    @ClassRule
    public static RedisContainer redisContainer=new RedisContainer("redis:5.0.0");

}
