package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:03
 * @version:1.0
 */
//默认扫描启动程序所在包，以及子包
//@ComponentScan(basePackages ={"com.springbootproject"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@SpringBootApplication
//@MapperScan("com.springbootproject.dao")
public class SpringBootRun {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringBootRun.class);
        springApplication.run(args);

    }

}
