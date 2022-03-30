## SpringMVC

是Spring提供的一个基于MVC设计模式的轻量级Web开发框架，本质上相当于Servlet

### SpringMvc流程

![image-20220330153933034](https://gitee.com/qianchao_repo/pic-typora/raw/master/springmvc_img/image-20220330153933034.png)



1. 用户发起一个 HTTP request 请求，该请求会被提交到 DispatcherServlet（前端控制器）；
2. 由 DispatcherServlet 请求一个或多个 HandlerMapping（处理器映射器），并返回一个执行链（HandlerExecutionChain）。
3. DispatcherServlet 将执行链返回的 Handler 信息发送给 HandlerAdapter（处理器适配器）；
4. HandlerAdapter 根据 Handler 信息找到并执行相应的 Handler（常称为 Controller）；
5. Handler 执行完毕后会返回给 HandlerAdapter 一个 ModelAndView 对象（Spring MVC的底层对象，包括 Model 数据模型和 View 视图信息）；
6. HandlerAdapter 接收到 ModelAndView 对象后，将其返回给 DispatcherServlet ；
7. DispatcherServlet 接收到 ModelAndView 对象后，会请求 ViewResolver（视图解析器）对视图进行解析；
8. ViewResolver 根据 View 信息匹配到相应的视图结果，并返回给 DispatcherServlet；
9. DispatcherServlet 接收到具体的 View 视图后，进行视图渲染，将 Model 中的模型数据填充到 View 视图中的 request 域，生成最终的 View（视图）；
10. 视图负责将结果显示到浏览器（客户端）



---



### SpringMvc中常用注解

#### @RequsetMapping

1. 用于处理请求url映射的注解，可用于类和方法上。
2. 用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径

#### @RequsetBody

该注解实现接收http请求的Json，将json转换为Java对象

#### @ResponseBody

该注解实现将controller方法返回对象转换为json对象响应给客户

---

### SpringMvc中控制器的注解一般采用那个

一般采用@Controller，也可以使用@RestController =>@ResposeBody+@Controller

---

### SpringMvc的控制器是单例吗

1. dispatcherServlet是单例的，在多线程访问的时候有线程安全问题。
2. 解决方案是：在控制器里面不能写可变状态变量，即无状态bean



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



















