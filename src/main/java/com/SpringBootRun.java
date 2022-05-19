package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:03
 * @version:1.0
 */
@SpringBootApplication
@MapperScan("com.mapper")
public class SpringBootRun {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringBootRun.class);
        springApplication.run(args);

    }

}
