## SpringBoot基础

### 什么是SpringBoot

1. 简化Spring应用的搭建以及开发过程
2. 创建独立的Spring的应用
3. 嵌入的tomcat无需部署war文件
4. 自动配置Spring、添加对应的功能starter自动化配置

### SpringBoot哪些优点

1. 

### 核心注解、由哪些注解组成

1. 核心注解

   - ```java
     SpringBootApplication
     ```

2. 核心注解的组成

   - ```java
     @SpringBootConfiguration
     @EnableAutoConfiguration
     @ComponentScan(
         excludeFilters = {@Filter(
         type = FilterType.CUSTOM,
         classes = {TypeExcludeFilter.class}
     ), @Filter(
         type = FilterType.CUSTOM,
         classes = {AutoConfigurationExcludeFilter.class}
     )
     public @interface SpringBootApplication
     ```

   - SpringBootConfiguration：组合了@Configuration注解，实现配置文件的功能

   - EnableAutoConfiguration：开启自动配置的功能

   - ComponentScan:Spring组件扫描

### 什么是JavaConfig

1. 提供了配置SpringIoc容器的纯Java方法，避免了使用Xml配置
2. 

### 自动装配原理

### 如何理解SpringBoot配置的加载顺序

### 运行SpringBoot有哪几种方式

### SpringBoot需要独立的容器运行吗

### 开启SpringBoot特性有哪些方式

### SpringBoot、Spring MVC和Spring有什么区别

### SpringBoot启动时都做了什么

## SpringBoot配置

###  什么是YAML

### YAML 配置的优势在哪里

### SpringBoot 是否可以使用 XML 配置 

### SpringBoot核心配置文件是什么

### bootstrap.properties和application.properties 有何区别 

### 什么是Spring Profiles

### 如何在自定义端口上运行SpringBoot应用程序

## SpringBoot安全性

### 如何实现SpringBoot应用程序的安全性

###  比较一下Spring Security 和Shiro各自的优缺点

### SpringBoot中如何解决跨域问题

### SpringBoot 中的监视器是什么

### 如何在SpringBoot中禁用Actuator端点安全性

###  如何监视所有SpringBoot微服务

## SpringBoot进阶

### 什么是 WebSockets

### 什么是 Spring Data

### Swagger用过吗？他用来做什么？

### 前后端分离，如何维护接口文档

### SpringBoot项目如何热部署

### SpringBoot 中的starter到底是什么

### spring-boot-starter-parent

### SpringBoot 打成的jar和普通的jar有什么区别 

### 如何使用SpringBoot实现异常处理

### 如何使用SpringBoot实现分页和排序

###  微服务中如何实现 session 共享

### SpringBoot 中如何实现定时任务