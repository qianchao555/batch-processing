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

~~~yaml
person:
  name: 张三
  age: 13
  likes:
    - bike
    - girl
    - movie
    
system:
    enable: true
~~~

1. @PropertySouce

   - 用于指定资源文件读取的位置，不仅能读取properties文件，也能读取xml、yml

2. @Environment

   - 引入配置文件时，直接将Environment注入即可

   - ~~~java
      @Autowired
      	private Environment environment;
      		@RequestMapping(value="/env")
      	public String environmentTest() {
      		String value=environment.getRequiredProperty("person.name");
      		System.out.println(value);
      		return value;
      	}
      ~~~
     ~~~
     
     
     ~~~

3. @Value

   - 需要在每个属性上指定

   - ~~~java
     @Data
     @ToString
     @Component
     public class Person1 {
     	@Value("${person.name}")
     	private String name;
     	@Value("${person.age}")
     	private Integer age;
     	@Value("${person.likes}")
     	private List<String> likes;
     }
     ~~~

   - 运行时会报错，因为yaml中不能采用value方式进行list注入

   - 需要将yaml中likes属性修改为：likes:bike,girl,movie

4. @ConfigurationProperties   推荐使用

   - 将配置文件的属于，绑定到对象的相应字段上

   - 在配置类上添加该注解并指定prefix即可对该对象的属性进行复制

   - ~~~java
     @Data
     @ToString
     @ConfigurationProperties(prefix = "person")
     @Component
     public class Person {
     
     	private String name;
     	private Integer age;
     	private List<String> likes;
     }
     ~~~

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

starter启动器：简单来讲就是一个引入了一些相关依赖和一些初始化的配置

starter 只不过是把我们某一模块，比如web 开发时所需要的所有JAR 包打包好给我们而已

基本上都会使用到两个相同的内容：ConfigurationProperties和AutoConfiguration。因为Spring Boot坚信“约定大于配置”这一理念，所以我们使用ConfigurationProperties来保存我们的配置，并且这些配置都可以有一个默认值，即在我们没有主动覆写原始配置的情况下，默认值就会生效，这在很多情况下是非常有用的。除此之外，starter的ConfigurationProperties还使得所有的配置属性被聚集到一个文件中（一般在resources目录下的application.properties、yml）

![image-20220403233124973](https://gitee.com/qianchao_repo/pic-typora/raw/master/springboot_img/202204032331255.png)



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

1. 创建maven项目
2. 创建ConfigurationProperties保存配置信息
3. 创建XXAutoConfiguration，引用定义好多配置信息。并在这个类中实现所有starter应该完成的操作
4. 把XXAutoConfiguration加入spring.factories配置文件中进行声明
5. 打包，之后就可以依赖了

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

   

2. 创建一个HelloProperties属性类，用来接收配置文件中注入的值。

   ~~~java
   @Data
   @ConfigurationProperties(prefix = "qc")
   publicclass HelloProperties {
       //如果配置文件中配置了qc.name、qc.msg则下列默认值会被覆盖
       private String name="www";
       private String msg="baidu.com";
   }
   ~~~
   
   
   
3. 创建一个HelloService，里面定义一个简单的hello()方法

   ~~~java
   @setter
   @getter
   publicclass HelloService {
       private String msg;
       private String name;
       public String sayHello() {
           return name + " say " + msg + " !";
       }
   }
   ~~~
   
   
   
4. **重要：**自动配置类的定义。

   ~~~java
   @Configuration
   //该注解是使我们之前配置的 @ConfigurationProperties 生效，将这个配置注册到Spring容器中
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



### SpringBoot启动流程？





### Async异步调用方法

1. 使用@Async注解即可实现方法的异步调用
2. 需要在启动类上加@EnableAsync使异步调用@Async注解生效



### 拦截器

Aop的一种实现，主要拦截Controller层的请求

拦截器是在servlet执行之前执行的程序(可以理解为Controller层之前)，主要用于拦截用户请求并作相应的处理。比如：判断用户登录信息、权限信息管理、日志记录等等

SpringBoot中的拦截器实现和SpringMvc中的一样，大致流程是：定义一个拦截器，这个类实现HandlerInterceptor类或者继承HandlerInterceptorAdapter都可以实现拦截器。然后需要将将自己定义的拦截器注入到适配器中，这里也有两种方式：一是实现WebMvcConfigure接口，一是继承WebMvcConfigureAdapter

#### HandlerInterceptor接口

1. preHandle：在业务处理器处理请求前被调用。例如：预处理、安全控制、权限校验等等处理
2. postHandle：业务处理器处理请求完成之后、生成视图前执行。
3. afterCompletion：在dispatcherServlet完成处理后被调用，可用于清理资源等等

#### 拦截器链

我们可以定义多个拦截器组成一个拦截器链，然后在适配器中注入多个拦截器。按照拦截器注入的顺序，拦截器的执行顺序应该是：拦截器1，拦截器2，拦截器2处理，拦截器1处理，拦截器2结束，拦截器1结束。

---



## SpringBoot配置

启动上下文时，SpringCloud会创建一个Bootstrap Context，作为Spring应用的Application Context的父上下文。bootstrap是应用程序的父上下文，初始化的时候负责从外部源加载配置属性并解析配置，bootstrap属性有高优先级，默认情况下不会被本地配置覆盖

bootstarp典型应用场景：获取配置中心的配置

![image-20220404214434467](https://gitee.com/qianchao_repo/pic-typora/raw/master/springboot_img/202204042145421.png)



### bootstrap.properties和application.properties 有何区别 

1. bootstrap由父ApplicationContext加载，比application优先加载，配置在应用程序上下文的引导阶段生效
2. application由ApplicationContext加载，用于sringboot项目的自动化配置

![image-20220320225202514](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203202252266.png)

###  什么是YAML

1. yaml是一种人类可读的数据序列化语言，通常用于配置文件

2. 与属性文件相比，yml文件更加结构化。yml具有分层配置数据

3. 层级关系

   - 用缩进表示层级关系，缩进只能用空格，不能用Tab
   - 缩进的空格数量不重要，但是同一层级的元素必须左对齐

4. 数据结构与类型

   - **对象**

   ~~~yaml
   key: value
   
   key:
    key1: v1
    key2: v2
    
   key: {key1:v1,key2:v2}
   ~~~

   - 布尔值
   - 整数、浮点数
   - 空
   - 时间戳
   - 数组

   ~~~yaml
   #格式1
   my:
     servers:
   	- dev.example.com
   	- another.example.com
   #格式2	
   values: [value1,value2]
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

5. 优缺点

   - yaml配置是有序的，在一些配置中是非常有用的，例如Zuul的配置中，配置代理规则时，顺序显得尤为重要
   - yaml配置不支持@PropertySource注解
   - properties文件是无序的

#### 

---



### SpringBoot 是否可以使用 XML 配置 

SpringBoot推荐使用Java配置而非Xml配置，但是也可以使用xml配置，通过@ImportResource注解可以引入一个xml配置



---

### 什么是Spring Profiles

实际项目中会存在多个多个环境，如：开发、测试、用户uat、生成、预上线等等环境。不同环境的配置也相同，此时就需要profile提供不同环境下不同的配置提供支持，可以通过激活、指定参数等等方式快速切换环境

#### 多Profile文件格式

application-{profile}.properties/yml

例如：

- application.yml：主配置文件
- application-dev.yml：开发环境配置文件
- application-test.yml：测试环境配置文件
- application-prod.yml：生产环境配置文件

#### 激活Profile

此时主配置文件中，可以通过配置激活不同环境的profile。例如激活生产环境：

1. 通过配置文件激活

   ~~~yaml
   spring:
     profiles:
       active: prod
   ~~~

2. 命令行激活：将该项目打包成 JAR 文件后，打开命令行窗口跳转到 JAR 所在目录

   ~~~shell
   java -jar helloworld-0.0.1-SNAPSHOT.jar   --spring.profiles.active=dev
   ~~~

3. 虚拟机参数激活：将该项目打包成 JAR 文件后，打开命令行窗口跳转到 JAR 所在目录

   ~~~shell
   java -Dspring.profiles.active=prod -jar helloworld-0.0.1-SNAPSHOT.jar
   ~~~

   

---

## SpringBoot安全性

### 如何实现SpringBoot应用程序的安全性

Spring security?

### SpringBoot中如何解决跨域问题

1. 前后端分离：前端配置Nginx即可

2. 后端全局配置

   ~~~java
   //实现WebMvcConfigurer,重写addCorsMappings()
   // 请求跨域
   @Configuration
   public class CorsConfig implements WebMvcConfigurer {
       @Override
       public void addCorsMappings(CorsRegistry registry) {
           //添加映射路径
           registry.addMapping("/**")
                   //是否发送Cookie
                   .allowCredentials(true)
                   //设置放行哪些原始域   SpringBoot2.4.4下低版本使用.allowedOrigins("*")    
                   .allowedOriginPatterns("*")
                   //放行哪些请求方式
                   .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                   //.allowedMethods("*") //或者放行全部
                   //放行哪些原始请求头部信息
                   .allowedHeaders("*")
                   //暴露哪些原始请求头部信息
                   .exposedHeaders("*");
       }
   }
   ~~~

   

3. 后端局部配置：在controller层配置需要跨域的类或方法上添加@CrossOrigin

   ~~~java
   @CrossOrigin(origins = "*",maxAge = 3600)
   public class UserController {
    final UserService userService;
    
    @GetMapping("/getOne/{id}")
    public User getOne(@PathVariable("id") Integer id) {
     return userService.getById(id);
    }
   ~~~

4. 定义跨域过滤器

   ~~~java
   //1、编写 跨域过滤器
   @Component
   public class CORSFilter implements Filter {
    
       @Override
       public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
     //*号表示对所有请求都允许跨域访问
           HttpServletResponse res = (HttpServletResponse) response;
           res.addHeader("Access-Control-Allow-Credentials", "true");
           res.addHeader("Access-Control-Allow-Origin", "*");
           res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
           res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
           if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
               response.getWriter().println("Success");
               return;
           }
           chain.doFilter(request, response);
       }
    
       @Override
       public void destroy() {
    
       }
    
       @Override
       public void init(FilterConfig filterConfig) throws ServletException {
    
       }
   }
   
   
   //2、注册过滤器
   @Configuration
   public class CorsConfig {
       @Bean
       public CorsFilter corsFilter() {
           CorsConfiguration corsConfiguration = new CorsConfiguration();
           corsConfiguration.addAllowedOrigin("*");
           corsConfiguration.addAllowedHeader("*");
           corsConfiguration.addAllowedMethod("*");
           corsConfiguration.setAllowCredentials(true);
           UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
           urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
           return new CorsFilter(urlBasedCorsConfigurationSource);
       }
   
   }
   ~~~

5. SpringSecurity，如果使用了 Spring Security，跨域配置会失效，因为请求被 Spring Security 拦截了。

   [SpringSecurity解决跨域问题参考链接](http://www.javaboy.org/2020/0611/cors-springsecurity.html)

   

---

### SpringBoot  监视器 Actuator

1. SpringBoot Actuator模块是Spring启动框架中的重要功能之一，提供了生产级别的功能。比如：健康检查、审计、指标收集、Http跟踪等等，
2. 监视器可以帮助我们访问生产环境中正在运行的应用程序的状态，帮助我们监控和管理SpringBoot应用、bean的加载情况、环境变量、日志信息、线程信息、JVM堆信息等等
3. 这个模块是一个采集应用内部信息暴露给外部的模块，上述功能都可以通过http和jmx访问

#### Endpoint

Springboot给外部提供了所谓endpoints(端点)来与应用程序进行访问和交互。比如： actuatro/health端点提供了关于应用健康情况的一些基础信息

根据端点的作用分为

应用配置类：获取应用程序中加载的应用配置、环境变量、自动化配置报告等与springboot应用密切相关的配置类信息

度量指标类：获取应用程序运行过程中用于监控的度量指标，例如内存信息、线程池信息、Http请求统计等等

操作控制类：提供了对应用的关闭等操作类功能

##### 常用的内置endpoint

actuator/health：查看程序健康信息

actuator/info：展示应用程序的信息=》程序的一些基础信息

actuator/metrics：查看监视标准=》当前应用的各类重要度量指标

actuator/beans：列出程序中的Spring Bean

actuator/env：列出程序运行所有信息



#### 如何在SpringBoot中禁用Actuator端点安全性

1. 默认情况下，所有敏感的http端点都是安全的，只有具有Actuator角色的用户才能访问它们

2. 安全性是使用标准的HttpServletRequest.isUserInRole方法实施的

3. ~~~yaml
   #监视程序运行端口
   management.server.port: 8080 
      
       
   # 激活所有的内置Endpoints=》打开所有的监控点
   management.endpoints.web.exposure.include: '*'
   
   #禁用端点安全性
   management.security.enabled: false
   
   ~~~
   
   
   
   ![image-20220404224926002](https://gitee.com/qianchao_repo/pic-typora/raw/master/springboot_img/202204042249167.png)
   
   

####  如何监视所有SpringBoot微服务

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