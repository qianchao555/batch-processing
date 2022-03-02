## SpringMVC

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































