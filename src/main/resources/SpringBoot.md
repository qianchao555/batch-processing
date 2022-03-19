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

### 读取配置相关的注解

1. @PropertySouce
2. @Value
3. @Environment
4. @ConfigurationProperties

### 什么是JavaConfig

1. 提供了配置Spring Ioc容器的纯Java方法，避免了使用Xml配置
2. 面向对象的配置，由于被定义为JavaConfig中的类，所有可以充分利用Java中面向对象的功能，一个配置类可以继承另一个，重写它的@Bean方法等等
3. 减少或消除了Xml配置
4. 常用的Java Config：
   - @Configuration：在类上使用，表示这个类是配置类
   - @ComponentScan：在配置类上添加此注解，该注解会扫描该类所在包下的所有配置类
   - @Bean

### 自动装配原理

1. 启动类上的核心注解：@SpringBootApplication注解，有了这个注解启动时就会为SpringBoot开启一个@EnableAutoConfiguration注解自动配置功能

2. ：

   - 
   - 去重，并将exclude和excludeName属性携带的类排查
   - 过滤，将满足条件(@Conditional)的自动配置类返回

3. 原理

   - 有了这个EnableAutoConfiguration注解就会从配置文件Meta_INF/Spring.factories加载可能用到的自动配置类

   - 去重，并将exclude和excludeName属性携带的类排查

   - 过滤，将满足条件(@Conditional)的自动配置类返回

   - 借助@Import，将所有符合自动配置条件的Bean定义加载到Ioc容器

   - ~~~java
     @Target(ElementType.TYPE)
     @Retention(RetentionPolicy.RUNTIME)
     @Documented
     @Inherited
     @AutoConfigurationPackage
     @Import(AutoConfigurationImportSelector.class)
     public @interface EnableAutoConfiguration {
         String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
     //按类型排序不需要自动装配的类
         Class<?>[] exclude() default {};
     //按名称排除不需要自动装配的类
         String[] excludeName() default {};
     }
     ~~~

   - 借助AutoConfigurationImportSelector，帮助springboot应用将所有符合@Configuration配置都加载到Ioc容器

   - 同时借助于Spring框架原有的一个工具类：SpringFactoriesLoader，@EnableAutoConfiguration就可以实现智能的自动配置。

   - SpringFactoriesLoader中加载配置,SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置,即根据@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration作为查找的Key,获取对应的一组@Configuration类

   - 总结：从classpath路径中搜索所有的META_INF/spring.factories文件，并且将其中的org.springframework.boot.autoconfigure.EnableutoConfiguration作为key所对应的值通过反射实例化为对应的标注了@Configuration的JavaConfig形式的Ioc容器配置类，然后汇总为一个并且加载到容器中。这些功能的配置类要生效的话，回去classpath中找是否有该类的依赖类，并且配置类里面注入了默认属性值类，功能类可以引用并赋默认值

---

### SpringBoot条件注解

它的一个核心思想就是当满足某种条件的时候，某个 Bean 才会生效，而正是这一特性，支撑起了 Spring Boot 的自动化配置

http://www.javaboy.org/2019/0802/springboot-conditional.html

@Conditional

Spring中常用的profile实际上就是条件注解的一个特殊化。

条件注解还有一个进化版，那就是 @Profile。我们一般利用 Profile 来实现在开发环境和生产环境之间进行快速切换。其实 Profile 就是利用条件注解来实现的

#### 定义

Spring4 中提供了更加通用的条件注解，让我们可以在**满足不同条件时创建不同的 Bean**，这种配置方式在 Spring Boot 中得到了广泛的使用，大量的自动化配置都是通过条件注解来实现的

---

### 如何理解SpringBoot配置的加载顺序



---



### 运行SpringBoot有哪几种方式

1. 直接运行启动类
2. 打包jar 或者放到容器中运行  
3. maven插件     mvn spring-boot:run运行
4. Grandle插件
5. 打包war 一般都是jar

### SpringBoot需要独立的容器运行吗

不需要，内置了web容器：Tomcat、Jetty、Undertow，默认是Tomcat

### 开启SpringBoot特性有哪些方式

1. 继承spring-boot-starter-parent项目
2. 导入spring-boot-dependencies项目依赖

### SpringBoot启动时都做了什么

### Async异步调用方法

1. 使用@Async注解即可实现方法的异步调用
2. 需要在启动类上加@EnableAsync使异步调用@Async注解生效

---



## SpringBoot配置

###  什么是YAML

1. yaml是一种人类可读的数据序列化语言，通常用于配置文件
2. 与属性文件相比，yml文件更加结构化。yml具有分层配置数据

### SpringBoot 是否可以使用 XML 配置 

SpringBoot推荐使用Java配置而非Xml配置，但是也可以使用xml配置，通过@ImportResource注解可以引入一个xml配置

### bootstrap.properties和application.properties 有何区别 

1. bootstrap由父ApplicationContext加载，比application优先加载，配置在应用程序上下文的引导阶段生效
2. application由ApplicationContext加载，用于sringboot项目的自动化配置

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



### SpringBoot如何解决跨域问题

http://www.javaboy.org/2020/0611/cors-springsecurity.html

#### @CrossOrigin

#### SpringSecurity

如果使用了 Spring Security，跨域配置会失效，因为请求被 Spring Security 拦截了。

当引入了 Spring Security 的时候，我们有两种办法开启 Spring Security 对跨域的支持。

#### OAuth2

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

1. 使用SpringBoot DevTools工具模块

### SpringBoot 中的starter到底是什么

1. 基本上它还是基于Spring已有功能实现的
2. 提供了一个自动化配置类，一般命名为xxxAutoConfiguration，在这个配置类中通过条件注解来决定一个配置是否生效
3. 然后它还会提供一系列的默认配置，也允许开发者根据实际情况定义相关配置，然后通过类型安全的属性(spring.factories)注入，将这些配置属性注入进来，新注入的属性会代替默认属性

### Spring-boot-starter-parent有什么用

1. 定义版本信息
2. 统一编码格式
3. 插件管理、资源管理
4. profile的配置
5. 配置一些公共信息等等

### SpringBoot 打成的jar和普通的jar有什么区别 

1. SpringBoot打包的jar是可执行的jar包，这种包不可以作为普通的jar被其他项目依赖，即使依赖了也无法使用其中的类
2. 主要是两者的jar包结构不同
   - 普通jar解压后直接就是包名
   - springboot的jar解压后在classes目录下面，因此无法直接引用
3. 如果非要引用可以在pom中增加配置，将SpringBoot项目打包成两个jar，一个可执行，一个可引用

### 如何使用SpringBoot实现异常处理

Spring提供了一种使用ControllerAdvice处理异常的方法，通过实现这个类来处理控制器抛出的所有异常

### 如何使用SpringBoot实现分页和排序

使用Spring Data-JPA可以实现分页

###  SpringBoot微服务中如何实现 session 共享



### SpringBoot 中如何实现定时任务

1. 使用Spring中的@Scheduled实现
2. 第三方框架Quartz