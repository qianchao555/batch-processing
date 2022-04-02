## SpringBoot基础

### 什么是SpringBoot

是Spring组件的一站式解决方案，简化了使用Spring的难度，简化了繁重的配置，提供各种启动器，使开发者能快速上手

1. 简化Spring应用的搭建以及开发过程
2. 创建独立的Spring的应用
3. 嵌入的tomcat无需部署war文件
4. 自动配置Spring、添加对应的功能starter自动化配置

### SpringBoot哪些优点

1. w为Spring开发提供一个更快、更广泛的入门体验
1. 开箱即用，**减少了繁琐的配置**
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



---



### 什么是JavaConfig

1. 提供了配置Spring Ioc容器的纯Java方法，避免了使用Xml配置
2. 面向对象的配置，由于被定义为JavaConfig中的类，所有可以充分利用Java中面向对象的功能，一个配置类可以继承另一个，重写它的@Bean方法等等
3. 减少或消除了Xml配置
4. 常用的Java Config：
   - @Configuration：在类上使用，表示这个类是配置类
   - @ComponentScan：在配置类上添加此注解，该注解会扫描该类所在包下的所有配置类
   - @Bean

---

### starter 是什么

并非新的技术点，基本上还是基于Spring已有功能来实现的

首先它提供了一个自动化配置类，一般命名为XXXAutoConfiguration，在这个类中通过条件注解来决定一个配置是否生效，然后它还会提供一系列的默认配置，也允许开发者根据实际情况自定义相关配置，然后通过安全的属性注入将这些配置属性注入进来，新注入的属性会代替默认属性。正因为如此，很多第三方框架，我们只需要引入依赖就可以直接使用

1. 基本上它还是基于Spring已有功能实现的
2. 提供了一个自动化配置类，一般命名为xxxAutoConfiguration，在这个配置类中通过条件注解来决定一个配置是否生效
3. 然后它还会提供一系列的默认配置，也允许开发者根据实际情况定义相关配置，然后通过类型安全的属性(spring.factories)注入，将这些配置属性注入进来，新注入的属性会代替默认属性

---

### Spring-boot-starter-parent 作用

新建一个SpringBoot项目，默认都是有parent的，这个parent就是spring-boot-starter-parent，作用：

1. 定义Java编译版本为1.8
2. 统一UTF-8编码格式
3. 定义依赖的版本信息
4. 执行打包操作的配置
5. 插件配置、资源过滤管理
6. profile的配置
7. 配置一些公共信息等等

### 自动装配原理

简答：Spring Boot启动的时候会通过@EnableAutoConfiguration注解找到META-INF/spring.factories配置文件中的所有自动配置类，并对其进行加载，而这些自动配置类都是以AutoConfiguration结尾来命名的，它实际上就是一个JavaConfig形式的Spring容器配置类，它能通过以Properties结尾命名的类中取得在全局配置文件中配置的属性如：server.port，而XxxxProperties类是通过@ConfigurationProperties注解与全局配置文件中对应的属性进行绑定的。

1. 启动类上的核心注解：@SpringBootApplication注解，有了这个注解启动时就会为SpringBoot开启一个@EnableAutoConfiguration注解自动配置功能

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

   - 总结：从classpath路径中搜索所有的META_INF/spring.factories文件，并且将其中的org.springframework.boot.autoconfigure.EnableutoConfiguration作为key所对应的值通过反射实例化为对应的标注了@Configuration的JavaConfig形式的Ioc容器配置类，然后汇总为一个并且加载到容器中。这些功能的配置类要生效的话，会去classpath中找是否有该类的依赖类，并且配置类里面注入了默认属性值类，功能类可以引用并赋默认值
   
   
   
   AutoConfigurationImportSelector
   
   - ![image-20220402154803603](https://gitee.com/qianchao_repo/pic-typora/raw/master/springboot_img/image-20220402154803603.png)

SpringFactories



### 自定义starter

所谓的starter本质就是一个普通的maven项目

因此自定义starter需要首先创建一个普通的maven项目，创建完成后，添加starter的自动化配置类即可

例如：

1. 配置文件

   ~~~yaml
   #qc.name=qc
   #qc.msg=java
   qc:
   	name:qc
   	msg:java
   ~~~

   

2. 创建一个HelloProperties属性类，用来接收配置文件中注入的值

   ~~~java
   @ConfigurationProperties(prefix = "qc")
   publicclass HelloProperties {
       privatestaticfinal String DEFAULT_NAME = "qq";
       privatestaticfinal String DEFAULT_MSG = "cc";
       private String name = DEFAULT_NAME;
       private String msg = DEFAULT_MSG;
       public String getName() {
           return name;
       }
       public void setName(String name) {
           this.name = name;
       }
       public String getMsg() {
           return msg;
       }
       public void setMsg(String msg) {
           this.msg = msg;
       }
   }
   ~~~

   

3. 创建一个HelloService，里面定义一个简单的hello()方法

   ~~~java
   publicclass HelloService {
       private String msg;
       private String name;
       public String sayHello() {
           return name + " say " + msg + " !";
       }
       public String getMsg() {
           return msg;
       }
       public void setMsg(String msg) {
           this.msg = msg;
       }
       public String getName() {
           return name;
       }
       public void setName(String name) {
           this.name = name;
       }
   }
   ~~~

   

4. **重要：**自动配置类的定义。

   ~~~java
   @Configuration
   //注解是使我们之前配置的 @ConfigurationProperties 生效，让配置的属性成功的进入 Bean 中。
   @EnableConfigurationProperties(HelloProperties.class)
   @ConditionalOnClass(HelloService.class)
   publicclass HelloServiceAutoConfiguration {
       @Autowired
       HelloProperties helloProperties;
   
       @Bean
       HelloService helloService() {
           HelloService helloService = new HelloService();
           helloService.setName(helloProperties.getName());
           helloService.setMsg(helloProperties.getMsg());
           return helloService;
       }
   }
   ~~~

5. **重要**：在maven项目的resources目录下创建一个名为META-INF文件夹，然后创建一个名为spring.factories的文件，在文件中指定我们的自动化配置类的路径即可

   ~~~yaml
   org.springframework.boot.autoconfigure.EnableAutoConfiguration=org.qc.mystarter.HelloServiceAutoConfiguration
   ~~~

6. 使用starter：加入我们自定义的starter依赖即可

   ```java
   <dependency>
       <groupId>org.qc</groupId>
       <artifactId>mystarter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   
       public class TTT{
       @Autowired
       HelloService helloService;
           
           @Test
           public void T01(){
   		helloService.hello();
           }
       }
   
   ```

以上就完成了自定义starter。实际中需要将其打包，然后传到maven私服上，供其他同事使用

---

### SpringBoot条件注解

它的一个核心思想就是当满足某种条件的时候，某个 Bean 才会生效，而正是这一特性，支撑起了 Spring Boot 的自动化配置

http://www.javaboy.org/2019/0802/springboot-conditional.html

@Conditional

Spring中常用的profile实际上就是条件注解的一个特殊化。

条件注解还有一个进化版，那就是 @Profile。我们一般利用 Profile 来实现在开发环境和生产环境之间进行快速切换。其实 Profile 就是利用条件注解来实现的

#### 定义

Spring4 中提供了更加通用的条件注解，让我们可以在**满足不同条件时创建不同的 Bean**，这种配置方式在 Spring Boot 中得到了广泛的使用，大量的自动化配置都是通过条件注解来实现的

#### @Conditional的扩展注解

@**ConditionalOnBean**：仅仅在当前上下文中存在某个对象时，才会实例化一个Bean。
@**ConditionalOnClass**：class类路径上存在某个Class时，才会实例化一个Bean。
@**ConditionalOnExpression**：当表达式为true的时候，才会实例化一个Bean。
@**ConditionalOnMissingBean**：仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean。
@**ConditionalOnMissingClass**：某个class类路径上不存在的时候，才会实例化一个Bean。
@ConditionalOnNotWebApplication：不是web应用，才会实例化一个Bean。
@ConditionalOnBean：当容器中有指定Bean的条件下进行实例化。
@ConditionalOnMissingBean：当容器里没有指定Bean的条件下进行实例化。
@ConditionalOnClass：当classpath类路径下有指定类的条件下进行实例化。
@ConditionalOnMissingClass：当类路径下没有指定类的条件下进行实例化。
@ConditionalOnWebApplication：当项目是一个Web项目时进行实例化。
@ConditionalOnNotWebApplication：当项目不是一个Web项目时进行实例化。
@ConditionalOnProperty：当指定的属性有指定的值时进行实例化。
@ConditionalOnExpression：基于SpEL表达式的条件判断。
@ConditionalOnJava：当JVM版本为指定的版本范围时触发实例化。
@ConditionalOnResource：当类路径下有指定的资源时触发实例化。
@ConditionalOnJndi：在JNDI存在的条件下触发实例化。
@ConditionalOnSingleCandidate：当指定的Bean在容器中只有一个，或者有多个但是指定了首选的Bean时触发实例化

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

### bootstrap.properties和application.properties 有何区别 

1. bootstrap由父ApplicationContext加载，比application优先加载，配置在应用程序上下文的引导阶段生效
2. application由ApplicationContext加载，用于sringboot项目的自动化配置

![image-20220320225202514](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203202252266.png)

###  什么是YAML

1. yaml是一种人类可读的数据序列化语言，通常用于配置文件

2. 与属性文件相比，yml文件更加结构化。yml具有分层配置数据

3. 支持数组

   ~~~yaml
   my:
     servers:
   	- dev.example.com
   	- another.example.com
   ~~~

   

   ~~~java
   @ConfigurationProperties(prefix="my")
   @Component
   public class Config {
   
   	private List<String> servers = new ArrayList<String>();
   
   	public List<String> getServers() {
   		return this.servers;
   	}
   }
   //主要是引入 @ConfigurationProperties(prefix = “my”) 注解，并且配置了属性的前缀，此时会自动将 Spring 容器中对应的数据注入到对象对应的属性中，就不用通过 @Value 注解挨个注入了，减少工作量并且避免出错
   //项目启动后，配置中的数组会自动存到servers集合中
   ~~~

4. 优缺点

   - yaml配置是有序的，在一些配置中是非常有用的，例如Zuul的配置中，配置代理规则时，顺序显得尤为重要
   - yaml配置不支持@PropertySource注解
   - properties文件是无序的

### 读取配置文件常用方式

#### @Value

字段上配置@Value(${配置项的key:默认值})

#### Environment对象获取

使用很简单，直接使用spring的注解@Autowired引入即可

```java
    @Autowired
    private Environment environment;
```

#### @ConfigurationProperties方式获取（强烈推荐）

为了更契合java的面向对象，我们采用自动配置的方式映射配置文件属性，配置完成后直接当做java对象即可使用

#### @PropertySource方式获取

---



### SpringBoot 是否可以使用 XML 配置 

SpringBoot推荐使用Java配置而非Xml配置，但是也可以使用xml配置，通过@ImportResource注解可以引入一个xml配置



---

### 什么是Spring Profiles

1. Spring Profiles允许用户根据配置文件(dev、test、prod等等)来注册bean



---



### 如何在自定义端口上运行SpringBoot应用程序

## SpringBoot安全性

### 如何实现SpringBoot应用程序的安全性

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

Spring Data 是 Spring 的一个子项目。用于简化数据库访问，支持NoSQL 和 关系数据存储。其主要目标是使数据库的访问变得方便快捷。Spring Data 具有如下特点：

1. SpringData 项目支持 NoSQL 存储：
2. MongoDB （文档数据库）
3. Neo4j（图形数据库）
4. Redis（键/值存储）
5. Hbase（列族数据库）

SpringData 项目所支持的关系数据存储技术：

1. JDBC
2. JPA

Spring Data Jpa 致力于减少数据访问层 (DAO) 的开发量. 开发者唯一要做的，就是声明持久层的接口，其他都交给 Spring Data JPA 来帮你完成！Spring Data JPA 通过规范方法的名字，根据符合规范的名字来确定方法需要实现什么样的逻辑



---



### 前后端分离，如何维护接口文档

1. swagger广泛用于可视化API，使用swagger UI可以为前端人员提供在线沙箱

2. Swagger是用于生成RESTful Web服务的可视化表示的工具

3. 使用 Swagger 我们可以快速生成一个接口文档网站，接口一旦发生变化，文档就会自动更新，所有开发工程师访问这一个在线网站就可以获取到最新的接口文档，非常方便

   

eoLinker：维护接口的一个东西

---



### SpringBoot项目如何热部署

1. 使用SpringBoot DevTools工具模块，这样当编译文件发生变化时，Spring Boot 就会自动重启
1. 

### SpringBoot 打成的jar和普通的jar有什么区别 

1. SpringBoot打包的jar是可执行的jar包，这种包不可以作为普通的jar被其他项目依赖，即使依赖了也无法使用其中的类
2. 主要是两者的jar包结构不同
   - 普通jar解压后直接就是包名
   - springboot的jar解压后在classes目录下面，因此无法直接引用
3. 如果非要引用可以在pom中增加配置，将SpringBoot项目打包成两个jar，一个可执行，一个可引用

### 如何使用SpringBoot实现异常处理

Spring提供了一种使用ControllerAdvice处理异常的方法，通过实现这个类来处理控制器抛出的所有异常

---



### 如何使用SpringBoot实现分页和排序

使用Spring Data-JPA可以实现分页

---



###  SpringBoot微服务中如何实现 session 共享

在微服务中，一个完整的项目被拆分成多个不相同的独立的服务，各个服务独立部署在不同的服务器上，各自的 session 被从物理空间上隔离开了，但是经常，我们需要在不同微服务之间共享 session ，常见的方案就是 Spring Session + Redis 来实现 session 共享。将所有微服务的 session 统一保存在 Redis 上，当各个微服务对 session 有相关的读写操作时，都去操作 Redis 上的 session 。这样就实现了 session 共享，Spring Session 基于 Spring 中的代理过滤器实现，使得 session 的同步操作对开发人员而言是透明的，非常简便

---



### SpringBoot 中如何实现定时任务

1. 使用Spring中的@Scheduled实现
2. 第三方框架Quartz