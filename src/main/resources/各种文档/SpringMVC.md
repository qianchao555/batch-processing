## SpringMVC

> SpringMVC是Spring在Ioc容器和Aop等的基础上，遵循web MVC的规范推出的web开发框架
>
> 目的是：为了简化Java技术栈的web开发
>
> 是Spring提供的一个基于MVC设计模式的轻量级Web开发框架，本质上相当于Servlet



### 什么是MVC

> Model View Controller：模型-视图-控制器，一种软件设计规范
>
> 本质是为了解耦：将业务逻辑、数据、界面展示进行分离开来



### 前后端分离后

SpringMVC既适用于前后端一体的jsp类型的项目(一般是老项目了)，又适合于前后端分离项目，前者返回ModelAndView，后者返回Json数据

1. 分离之前：后端将视图渲染后(例如：对ModelAndView进行视图解析器渲染成真正的视图)，再返回给前端
2. 分离之后：后端仅仅返回前端所需的Json数据，后端不再渲染Html页面
3. 分离之后：SpringMvc的 视图解析器是不需要的



### SpringMvc请求流程

![image-20220330153933034](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springmvc_img/image-20220330153933034.png)



![image-20221022222703972](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springmvc_img/202210222227090.png)



1. 用户发起一个 HTTP request 请求，该请求会被提交到 DispatcherServlet（前端控制器），前端控制器收到请求后自己不进行处理，而是委托其他的解析器进行处理，作为统一访问点，进行全局的流程控制
2. DispatcherServlet->handlerMapping
   - HandlerMapping将会把请求映射为HandlerExecutionChain对象(包含一个Handler处理器对象、多个HandlerInterceptor拦截器对象)，通过这种策略模式，很容易添加新的映射策略
   - dispatcherServlet 请求一个或多个 HandlerMapping（处理器映射器），并返回一个执行链（HandlerExecutionChain）
3. dispatcherServlet->HandlerAdapter
   - HandlerAdapter将会把处理器包装为适配器，从而支持多种类型的处理器，即适配器设计模式的应用，从而很容易支持很多类型的处理器
   - DispatcherServlet 将执行链返回的 Handler 信息发送给 HandlerAdapter（处理器适配器）；
4. HandlerAdapter->处理器功能处理方法的调用
   - HandlerAdapter将会根据适配的结果，调用真正的处理器（Handler）的功能处理方法，Handler完成处理后，给HandlerAdapter返回结果
   - 前后端未分离处理结果：返回一个ModelAndView对象
   - 前后端分离处理结果：返回Json
   - HandlerAdapter 根据 Handler 信息找到并执行相应的 Handler（常称为 Controller）；
5. modelAndView的逻辑视图名->ViewResolver
   - HandlerAdapter 接收到处理结果后（ModelAndView或Json），将其返回给 DispatcherServlet 
   - DispatcherServlet 接收到 ModelAndView 对象后，会请求 ViewResolver（视图解析器）对视图进行解析，将把逻辑视图名解析为具体的View，通过这种策 略模式，很容易更换其他视图技术
6. view->渲染
   - DispatcherServlet 接收到具体的 View 视图后，进行视图渲染
   - view根据传进来的Model模型数据进行渲染，此处的Model 实际是一个Map 数据结构，因此 很容易支持其他视图技术
7. 返回控制权给dispatcherServlet
   - 前端控制器 负责将结果响应给用户，到此一个流程结束



---



### Controller

Controller控制器，控制器主要负责的内容

dispatcherServlet将请求委托给控制器进行处理

1. 收集、验证请求参数，并绑定到命令对象
2. 将命令对象交给业务对象，由业务对象处理，并返回数据

因此MVC中的C的表示(控制逻辑+功能处理)：dispatcherServlet+Controller组成



### SpringMVC类型转换器

Spring提供了一种Converter(类型转换器)的类型转换工具

作用：

1. 在控制器方法对请求进行处理前，先获取到请求发过来的请求参数
2. 将请求参数转换为 控制器(Controller)方法指定的数据类型，然后再将转换后的参数值传递给控制器方法的形参
3. 这样控制器方法就可以正确地获取请求中携带的参数了

#### 内置的类型转换器

SpringMVC提供了很多内置的类型转换器，大多数情况下，能满足开发人员的类型转换需求了（太多不去一一介绍了）

#### 自定义类型转换器

一些较为复杂的类型转换，例如：String转为Date类型、String转为指定的Java实体类型等等，以及开发人员自定义格式的数据的转换，需要根据自身的需求开发自定义类型转换器来进行转换

接口：Converter、ConverterFactory、GenericConverter

开发人员自定义转换器类，实现上面3个接口之一转换器即可

~~~java
//String转Date的转换
@Component
public Class MyDateConverter implements Converter<String,Date>{
    private String datePatten = "yyyy-MM-dd";
    @Override
    public Date convert(String source){
         System.out.println("前端传递过来的时间为：" + source);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePatten);
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("无效的日期格式，请使用正确的日期格式" + datePatten);
        }
    }
}
~~~



#### 配置自定义类型转换器

创建完成自定义类型转换器后，需要进行配置才会使得自定义类型转换器生效

SpringBoot中，将自定义的转换器注册到WebMvcConfigurer，实现 WebMvcConfigurer 接口来定制 Spring MvVC 配置

~~~java
public class WebConfig implements WebMvcConfigurer{
    @Autowired 
    private MyDateConverter myDateConverter;
    public void addFormatters(FormatterRegistry registry){
        registry.addConverter(myDateConverter);
    }
}
~~~

---

### SpringMVC格式化转换器

实际项目中，经常会涉及到一些进行格式化的数据，例如：金额、日期等等。这些数据需要经过一定的格式化处理才能正常使用

Spring提供了一个Formatter\<T>接口，它被称为格式化转换器



---



### SpringMVC Json数据交互

#### JSON数据转换

为了实现浏览器与控制器之间的JSON数据交互，SpringMVC提供了一个默认的MappingJackon2HttpMessageConverter类，顶层接口为HttpMessageConverter，用来处理Json格式请求和响应

通过它可以将JSON数据转换为Java对象，也可以将Java对象转换为JSON数据

#### JSON转换注解

SpringMVC提供了两个十分重要的与JSON格式转换相关的注解，分别是@RequestBody、@ResponseBody

RequestBody：作用于方法形参上，用于将请求体中的数据绑定到控制器方法的形参上

ResponseBody：该注解 用于将控制器方法的返回值直接作为响应报文的响应体，响应到浏览器上

#### MappingJackon2HttpMessageConverter

是SpringBoot中默认的Json消息转换器



#### JackSon

SpringMVC默认采用Jackson解析Json，尽管还有一些很优秀的json解析工具：Fast json、GSON但是出于最小依赖的考虑，也许Json解析第一选择就应该是Jackson

FastJson代码质量、漏洞、坑多问题，尽量避免使用FastJson



Jackson目前使用比较广泛的用来序列化和反序列化json的Java开源框架

 核心模块

1. jackson-core：核心包
2. jackson-annotations：注解包，提供标准注解功能
3. jackson-databind：数据绑定包，提供基于对象绑定解析相关的API（ObjectMapper)和树模型(JsonNode)

Jackson ObjectMapper类

是使用Jackson解析JSON最简单的方法，最常用的API就是基于对象绑定的ObjectMapper

1. ObjectMapper可以从字符串、流、文件中解析JSON，并创建表示已解析的Json的Java对象。将Json解析为Java对象也称为Json反序列化Java对象
2. 也可以从Java对象创建Json，将Java对象解析为Json称为Java对象序列化Json
3. Object映射器可以将JSON解析为自定义的类对象，也可以解析置Json树模型的对象

之所以称为ObjectMapper，是因为它可以将Json对象映射为Java对象(反序列化)，也可以将Java对象映射为Json对象(序列化)

---



### SpringMvc中常用注解

#### @RequsetMapping

1. 用于处理请求url映射的注解，可用于类和方法上。
2. 用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径

#### @RequsetBody

该注解实现接收http请求的Json，将json转换为Java对象

#### @ResponseBody

该注解实现将controller方法返回对象转换为json对象响应给客户

@RestController =>@ResposeBody+@Controller



---

### SpringMvc的控制器是单例吗

1. dispatcherServlet是单例的，在多线程访问的时候有线程安全问题。

2. controller是单例的

   

解决方案是：在控制器里面不能写可变状态变量，即无状态bean、或者使用ThreadLocal



### Springmvc参数传递

springmvc中，controller的**接收请求参数**有多种，有的适合get、有的适合post

1. HttpServletRequest

2. @PathVariable

   - 接收url中的请求参数

   - ~~~java
     @RequestMapping("/login/{name}/{pwd}")
     public String login(@PathVariable String name, @PathVariable String pwd, Model model) {
             return "login";
         }
     }
     ~~~

3. @RequestParam

4. @RequestBody

5. @ModelAttribute



### SpringMvc文件上传下载

#### MultipartResolver

用于处理上传请求，将上传请求包装成可以直接获取文件的数据，从而方便操作

MultpartiResolver 接口有以下两个实现类：

- StandardServletMultipartResolver：使用了 Servlet 3.0 标准的上传方式。
- **CommonsMultipartResolver**：使用了 Apache 的 commons-fileupload 来完成具体的上传操作。

#### 下载

利用OutputStream流进行下载





---

## Servlet

严格来说Servlet只是一套JavaWeb开发的规范，或者说是一套JavaWeb开发的技术标准

Servlet规范是开放的，目前常见的实现了Servlet规范的产品：Tomcat、Weblogic、Jetty、Jboss、WebSphere等等，它们称为Servlet容器，Servlet容器用来管理Servlet类

Servlet规范提供了两个抽象类：GenericServlet、HttpServlet，它们都实现了Servlet接口的很多功能，实际开发中一般继承HttpServlet类

### Servlet容器是什么

#### Web服务器

web服务器是一种对外提供Web服务的软件，它可以接收浏览器的Http请求，并将处理结果返回给浏览器

在部署Servlet网站时，同样需要类似的软件，例如：Tomcat、Jboss等等，它们称为Web容器，而不是服务器

#### Web容器

Servlet容器就是Servlet代码的运行环境，它除了实现 Servlet 规范定义的各种接口和类，为 Servlet 的运行提供底层支持，还需要管理由用户编写的 Servlet 类，比如实例化类（创建对象）、调用方法、销毁类等

Web容器往往会自带Web服务器模块，提供基本的Http服务，所以可以不用安装Apache、IIS等传统意义上的服务器，正因为如此 有人把Tomcat称为Web容器 ，有人又叫Tomcat为Web服务器，两者的概念已经非常模糊了



### Servlet创建方式

在Servlet中，一个动态网页对应一个Servlet类，访问一个动态网页的过程实际上是将对应的Servlet类进行加载、实例化、调用相关方法过程

Servlet规范最顶层接口是一个jakarta.servlet.Servlet接口

主要创建Servlet类的方式

1. 实现Servlet接口，实现其方法
2. 继承GenericServlet抽象类，重新service方法
3. 继承HttpServlet抽象类，重写doGet、doPost方法



#### HttpServlet抽象类

在 HTTP/1.1 协议中共定义了 7 种请求方式，即 GET、POST、HEAD、PUT、DELETE、TRACE 和 OPTIONS。

HttpServlet 针对这 7 种请求方式分别定义了 7 种方法，即 doGet()、doPost()、doHead()、doPut()、doDelete()、doTrace() 和 doOptions()

### ServletContext

Servlet容器启动时，会为每个Web应用创建一个唯一的ServletContex对象，该对象被称为Servlet上下文

该上下文对象的生命周期为：Servlet容器启动到容器关闭或应用被卸载时结束

Web应用中的所有Servlet共享一个ServletContext对象，不同Servlet之间可以通过ServletContext对象实现数据通讯，因此，该对象也被称为Context域对象



### Servlet处理Http请求流程

1. 浏览器发送Http请求到Servlet容器
2. Servlet容器接收来自客户端的Http请求后，针对该请求分别创建一个HttpServletRequest和HttpServletResponse对象
3. Servlet通过HttpServletRequest对象获取客户端信息一起请求的相关信息
4. 对Http请求就像处理，请求处理完成后，将相应现象返回给客户端
5. 响应信息返回给客户端后，这两个对象被销毁

### HttpServletRequest接口

一般浏览器(客户端)通过Http协议访问服务器的资源，Servlet主要用来处理Http请求

该对象专门用于封装Http请求信息，简称request对象



### Servlet请求转发

web应用在处理客户端请求时，需要多个web资源共同协作才能生成响应结果，但是由于Servlet对象无法直接调用其他Servlet的service()方法，所以Servlet提出了请求转发的解决方案

请求转发：属于服务器行为，容器接收到请求后，Servlet会先对请求做一些预处理，然后将请求传递给其他Web资源，来完成包括生成响应在内的后续工作

#### RequestDispatcher接口

该对象由Servlet容器创建，用于封装由路径所标识的Web资源，利用该对象可以把请求转发给其他的Web资源





### Servlet Filter

Servlet过滤器，在Servlet规范中定义的，能够对Servlet容器传递给Web资源的request请求对象和response对象进行检查和修改

Filter是Servlet规范中最实用的技术，通过它可以对服务器管理的所有Web资源进行拦截，从而实现一些特殊的功能，例如：用户的权限控制、过滤敏感词、统一设置等等



**过滤器前->Servlet ->拦截器前 ->controller ->拦截器后  ->过滤器后**



HttpServletRequest到达Servlet之前，拦截HttpServletRequest请求

HttpServletResponse到达客户端之前，拦截HttpServletResponse



Filter可以拦截多个请求或响应；一个请求或响应可以被多个Filter拦截



---





## DispatcherServlet

>  SpringMVC的核心，其实质是一个HttpServlet，需要根据Servlet规范使用Java配置或xml声明和映射



DispatcherServlet需要WebApplicationContext（继承自 ApplicationContext）来配置

![image-20230327150614373](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springmvc_img/202303271506670.png)







![image-20221021231600219](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springmvc_img/202210212316402.png)



DispatcherServlet和原生Servlet类似，都是用来接收用户请求



当Servlet没有实现SingleThreadModel接口时，Servlet才是单例的，如果实现该接口，那么每次请求相同的Servlet将会创建一个新的实例

DispatcherServlet其生命周期跟随Servlet容器，没有实现SingleThreadModel接口，所以只有一个实例



### DispatcherServlet如何初始化

> 首先，它本质是一个Servlet，Servlet有自己的生命周期的方法

![image-20230327151537767](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springmvc_img/202303271515999.png)

其init()方法在HttpServletBean中，







## Tomcat

servlet容器 默认采用单实例多线程的方式处理多个请求

1. web容器启动的时候，Servlet就被加载并实例化（只存在一个Servlet实例）
2. 当请求到达时，Servlet容器通过调度线程，调度它管理线程池中等待执行的线程给请求者
3. 线程执行Servlet的service方法
4. 请求结束，放回线程池，等待被调用





