## SpringBoot基础

### 什么是SpringBoot

是Spring组件的一站式解决方案，简化了使用Spring的难度，简化了繁重的配置，提供各种启动器，使开发者能快速上手

1. 简化Spring应用的搭建以及开发过程
2. 创建独立的Spring的应用
3. 嵌入的tomcat无需部署war文件
4. 自动配置Spring、添加对应的功能starter自动化配置

### SpringBoot哪些优点

1. 
2. 使用JavaConfig有助于使用XML
3. 总结就是：配置、部署、监控等等变得更加简单

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
2. 面向对象的配置，由于被定义为JavaConfig中的类，所有可以充分利用Java中面向对象的功能，一个配置类可以继承另一个，重写它的@Bean方法等等
3. 减少或消除了Xml配置

### 自动装配原理

### 如何理解SpringBoot配置的加载顺序

### 运行SpringBoot有哪几种方式

### SpringBoot需要独立的容器运行吗

### 开启SpringBoot特性有哪些方式

### SpringBoot、Spring MVC和Spring有什么区别

### SpringBoot启动时都做了什么

---



## SpringBoot配置

###  什么是YAML

1. yaml是一种人类可读的数据序列化语言，通常用于配置文件
2. 与属性文件相比，yml文件更加结构化。yml具有分层配置数据

### YAML 配置的优势在哪里

### SpringBoot 是否可以使用 XML 配置 

### SpringBoot核心配置文件是什么

### bootstrap.properties和application.properties 有何区别 

### 什么是Spring Profiles

1. Spring Profiles允许用户根据配置文件(dev、test、prod等等)来注册bean

---



### 如何在自定义端口上运行SpringBoot应用程序

## SpringBoot安全性

### 如何实现SpringBoot应用程序的安全性

###  比较一下Spring Security 和Shiro各自的优缺点

### SpringBoot中如何解决跨域问题

### SpringBoot 中的监视器是什么

1. SpringBoot Actuator是Spring启动框架中的重要功能之一
2. 监视器可以帮助我们访问生成环境中正在运行的应用程序的状态

---

### 如何在SpringBoot中禁用Actuator端点安全性

1. 默认情况下，所有敏感的http端点都是安全的，只有具有Actuator角色的用户才能访问它们

2. 安全性是使用标准的HttpServletRequest.isUserInRole方法实施的

3. ~~~yaml
   management:
   	security:
   		enabled:false
   #使用这个配置来禁用安全性
   ~~~

   ---

   

###  如何监视所有SpringBoot微服务

1. SpringBoot提供监视器端点以监控各个微服务的度量
2. 这些端点对于获取有关应用程序的信息以及它们的组件是否正常运行很有帮助
3. 但是监视器一个主要的缺点是，我们必须单独打开应用程序以了解其状态或健康状况

---



## SpringBoot进阶

### 什么是 WebSockets

1. 是一种计算机通信协议，通过单个TCP连接提供全双工通信信道
2. 使用webSocket客户端或服务器可以发起消息发送
3. 客户端和服务器通信是相互的

---



### 什么是 Spring Data



---



### Swagger用过吗？他用来做什么？

1. swagger广泛用于可视化API，使用swagger UI可以为前端人员提供在线沙箱
2. Swagger是用于生成RESTful Web服务的可视化表示的工具

---



### 前后端分离，如何维护接口文档

eoLinker

---



### SpringBoot项目如何热部署

### SpringBoot 中的starter到底是什么

### spring-boot-starter-parent

### SpringBoot 打成的jar和普通的jar有什么区别 

### 如何使用SpringBoot实现异常处理

### 如何使用SpringBoot实现分页和排序

###  微服务中如何实现 session 共享

### SpringBoot 中如何实现定时任务