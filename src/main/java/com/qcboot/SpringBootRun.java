package com.qcboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:03
 * @version:1.0
 */
//默认扫描启动程序所在包，以及子包
@SpringBootApplication
@ComponentScan("com")
@MapperScan("com.dao")
//@EnableFeignClients
//@EnableAsync
//@EnableMyTestAnnotation
public class SpringBootRun   {



    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringBootRun.class);
        springApplication.run(args);
//        AnnotationConfigApplicationContext anno=new AnnotationConfigApplicationContext(SpringBootRun.class);
//        anno.getBean(MySelectorService.class).printTestSelector();
//        String[] beanDefinitionNames = anno.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }

    }

}
