## SpringCloud

### 什么是微服务架构

1. 将单体的应用程序分为多个应用程序，每一个应用程序就是一个微服务模块
2. 每个微服务运行在自己的进程里，使用轻量级的机制通信

### SpringCloud是什么

1. springcloud是一系列框架的有序集合
2. 它利用springboot的开发便利性巧妙地简化了分布式系统基础设施的开发

### SpringCloud优缺点

### SpringBoot和SpringCloud区别

1. springboot专注于快速方便的开发单个个体微服务
2. springcloud关注全局的微服务协调整理治理框架，它将springboot开发的一个个单体微服务整合并管理
3. 为各个微服务之间提供：配置管理、服务发现、断路器、路由、分布式回话等等集成服务
4. springboot可以离开springcloud独立使用开发项目，但是springcloud离不开springboot，属于依赖的关系

### Springboot和springcloud之间版本对于关系

| springboot | springcloud |
| ---------- | ----------- |
| 2.2.x      | Hoxton      |
| 2.1.x      | Greenwich   |
| 2.0.x      | Finchley    |
| Edgware    | 1.5.x       |
| Dalston    | 1.5.x       |

### SpringCloud中的组件有哪些

Spring Cloud Eureka：服务注册与发现

Spring Cloud Zuul：网关

Spring Cloud Ribbon：客户端负载均衡

Spring Cloud Feign：声明式web服务 客户端

Spring Cloud Hystrix：断路器

Spring Cloud Config：分布式统一配置管理中心

等等。。

### 使用SpringBoot开发分布式微服务时，面临的问题



---

### Eureka

#### 什么是Eureka

1. 作为springcloud的服务注册服务器，它是服务注册中心，系统中的其他服务使用Eureka的客户端将其注册到Eureka Service中，并且保持心跳
2. 可以通过Eureka Service来监控各个微服务是否运行正常

#### 服务注册和发现是什么

所有服务都在Eureka服务器上注册并通过调用Eureka服务器完成服务查找

#### Eureka如何实现高可用

1. Eureka集群

#### Eureka自我保护

1. 默认情况下，如果Eureka服务器在一定时间内没有收到某个微服务的心跳，Eureka会进入自我保护模式
2. 自我保护模式下，Eureka Service会保护服务注册表中的信息，不在删除注册表中的数据，当网络故障恢复后，Eureka Service节点会自动退出自我保护模式



#### DiscoverClient作用

可以从注册中心根据服务名 获取注册在Eureka上的服务的信息

#### Eureka和Zookeeper区别

---



### Ribbon

#### Ribbon是什么

1. ribbon是Netflix发布的开源项目
2. 主要功能是提供客户端软件的负载均衡算法
3. Ribbon客户端组件提供了一系列完善的配置项，根据配置信息，Ribbon会自动基于某种规则去连接不同的机器

#### 负载均衡的意义？

#### Ribbon实现原理

使用discoverClient从注册中心读取目标服务信息，对同一接口请求进行计数，使用取余算法获取目标服务集群索引，返回获取到的目标服务信息

#### Nginx与Ribbon区别

---



### Feign

#### 什么是Feign

声明式服务调用组件，它在RestTemplate基础上做了进一步封装，我们只需要声明一个接口并通过注解进行简单的配置（类似与Dao接口上面的Mapper注解一样）即可实现对http接口的绑定

#### SpringCloud调用接口的方式

##### Feign

##### RestTemplate

#### Ribbon和Feing调用服务的区别

1. 调用方式不同
   - ribbon需要我们自己构建http请求，模拟http请求然后通过RestTemplate发给其他服务，步骤繁琐
   - Feign在Ribbon基础上进行了一次封装，采用接口的方式，将需要调用的服务方法定义成抽象方法保存在本地就可以了，不需要自己构建http请求，直接调用接口就可以了。不过需要调用方法要和本地抽象方法的签名完全一致

---

### OpenFeign

它是 Spring 官方推出的一种声明式服务调用与负载均衡组件，它的出现就是为了替代进入停更维护状态的 Feign

1. 里面集成了ribbon、feign的封装、hystrix
2. 其服务调用以及负载均衡是依靠底层Ribbon实现的，因此超时控制通过Ribbon来设置
3. OpenFeign里面默认没有开启Hystrix

#### 常用注解

@FeignClient

该注解用于通知OpenFeign组件对@RequestMapping注解下的接口进行解析，并通过动态代理的方式产生实现类，实现负载均衡和服务调用

@EnableFeignClient

启动类上添加，表示开启OpenFeign功能，SpringCloud启动时，会去扫描标记@FeignClient注解的接口生成代理并且注入到Spring容器中

---

### Hystrix

#### 什么是Hystrix

Spring Cloud Hystrix是一款优秀的服务容错与保护组件，它提供了熔断器功能，能有效地阻止分布式微服务系统中出现联动故障，以提高微服务系统的弹性。Hystrix具有服务降级、服务熔断、线程隔离、请求缓存、请求合并、以及实时故障监控等强大功能

#### 微服务中，如何保护服务

1. 一般采用Hystrix框架，实现服务隔离避免出现服务的雪崩效应，从而达到保护服务的效果
2. 当出现服务不可用，使用服务降级返回友好提示，从而避免单个服务崩溃引发整体服务的不可用

#### 服务雪崩效应

1. 当一个服务调用另一个服务时，由于网络原因或自身原因等等出现问题，调用者就会等待被调用者的响应，当更多的服务请求到这些资源导致更多的服务等待，发生连锁反应(雪崩效应)
2. 当某个服务发生宕机时，调用这个服务的其他服务也会发生宕机，这样就会将服务的不可用逐步扩大到各个其他服务中，从而使整个项目的服务宕机崩溃，发生服务雪崩
3. 发生雪崩的原因：单个服务存在bug、请求访问量激增导致服务崩溃、服务器的硬件故障等等

#### Hystrix解决雪崩问题的手段

服务降级、熔断、隔离

#### 谈谈服务降级、熔断、隔离

##### 服务降级(fallback)

1. 当某些服务不可用时，为了避免长时间等待造成服务卡顿或者雪崩效应，而主动执行备用的服务降级逻辑，立即返回一个友好的提示，以保障主体业务不受影响
2. Hystrix实现服务降级的功能是通过重写HystrixCommand中的getFallback方法，或者HystrixObservableCommand的resumeWithFallback()方法，使服务支持服务降级

##### 服务熔断

在服务降级的基础上更直接的一种保护方式，当在一个统计时间范围内的请求失败数量达到设定值或当前的请求错误率达到设定的错误率阈值时开启断路，之后的请求直接走fallback()方法，在设定时间后尝试恢复

1. 熔断机制是**为了应对雪崩效应**而出现的一种微服务链路保护机制
2. 当微服务系统中的某个微服务不可用或响应时间太长时，为了保护系统的整体可用性，熔断器会暂时切断请求对该服务的调用，并快速返回一个友好的错误响应。这种熔断状态不是永久的，在经历了一定的时间后，熔断器会再次检测该微服务是否恢复正常，若服务恢复正常则恢复其调用链路

###### 熔断状态

1. 断路器三种状态
   - 打开状态:一段时间内，达到一定的次数无法调用并且多次监测没有恢复的迹象，断路器完全打开，那么下次请求就不会请求该服务，而是快速返回失败响应
   - 半开状态：短时间内，又恢复迹象，断路器会将部分请求发给该服务，正常调用时，断路器关闭
   - 关闭状态：当服务一直处于正常状态，服务能正常调用
2. ![熔断状态转换](http://c.biancheng.net/uploads/allimg/211210/10162355X-7.png)
3. SpringCloud中，熔断机制通过Hystrix实现。Hystrix会监控微服务间的调用情况，当失败调用到一定比例时，就会启动熔断机制
4. Hystrix 实现服务熔断的步骤如下：
   1. 当服务的调用出错率达到或超过 Hystix 规定的比率（默认为 50%）后，熔断器进入熔断开启状态。
   2. 熔断器进入熔断开启状态后，Hystrix 会启动一个休眠时间窗，在这个时间窗内，该服务的降级逻辑会临时充当业务主逻辑，而原来的业务主逻辑不可用。
   3. 当有请求再次调用该服务时，会直接调用降级逻辑快速地返回失败响应，以避免系统雪崩。
   4. 当休眠时间窗到期后，Hystrix 会进入半熔断转态，允许部分请求对服务原来的主业务逻辑进行调用，并监控其调用成功率。
   5. 如果调用成功率达到预期，则说明服务已恢复正常，Hystrix 进入熔断关闭状态，服务原来的主业务逻辑恢复；否则 Hystrix 重新进入熔断开启状态，休眠时间窗口重新计时，继续重复第 2 到第 5 步。

##### 服务隔离

Hystrix为隔离的服务开启一个独立的线程池，这样在高并发的情况下不会影响其他服务。服务隔离有线程池和信号量两种实现方式，一般使用线程池方式实现



---

### Spring Cloud Config

[参考地址](http://c.biancheng.net/springcloud/config.html)

分布式微服务体系中，几乎所有服务的运行都离不开配置文件的支持，这些配置文件通常由各个服务自行管理。例如：application.yml等等

这种配置文件散落在各个服务中的管理方式存在以下问题：

1. 管理难度大：配置文件散落在各个微服务中，难以管理
2. 安全性低：配置随着源代码保存在代码库中，容易造成配置泄露
3. 时效性差：微服务中的配置修改后，必须重启服务，否则无法生效
4. 局限性明显：无法支持动态调整、例如:日志开关、功能开关

为了解决以上这些问题，通常使用配置中心对配置进行统一管理

#### 分布式配置中心有哪些框架

百度：Disconf、淘宝：diamond、携程Apollo、zookeeper、spring cloud config

#### 什么是springcloud config

1. Spring Cloud Config为微服务架构中各个微服务各个环境下的配置提供集中化的外部配置支持
2. 简单说就是：SpringCloud Config可以将各个微服务的配置文件集中存储在一个外部的存储仓库或系统(Git、Svn等)中，对配置的统一管理，以支持各个微服务的运行
3. Spring Cloud Config 分为Config Server和Client两部分
4. Server也被称为分布式配置中心，它是一个独立运行的微服务应用，用来连接配置仓库并为客户端提供获取配置信息、加密信息、解密等信息的访问接口
5. Client指的是微服务架构中的各个微服务，他们通过Config Server对配置进行管理，并从Config Server中获取和加载配置信息

#### 分布式配置中心的作用

动态变更项目配置信息，其不必重新部署项目

#### Spring Cloud Config可以实现实时刷吗

SpringCloud Config实时刷新采用SpringCloud Bus消息总线实现

---

### Spring Cloud Bus

#### 什么是SpringCloud Bus

1. bus就像是一个分布式执行器，用于扩展SpringBoot应用程序的配置文件，也可以用作应用程序直接的通信通道
2. 不能单独通信，需要配合MQ的支持
3. 一般与SpringCloud Config配合做配置中心

---



### Spring Cloud Gateway

[参考文章](http://c.biancheng.net/springcloud/gateway.html)

#### API网关

1. 网关是一个服务器，也可以说是进入系统的唯一节点。网关负责请求转发、合成和协议转换等等
2. 所有来自客户端的请求都要经过网关，然后路由这些请求到对应的微服务
3. 可以在API网关中处理一些非业务功能的逻辑，例如：权限验证、监控、缓存、请求路由等等
4. 常见的API网关实现方案：SpringCloud gateway、zuul、Nginx+Lua、Kong、Traefik

#### 什么是Springcloud gateway

1. 作为SpringCloud官方推出的第二代网关框架，取代Zuul网关
2. 网关常见的功能：路由转发、权限校验、限流控制等待
3. 基于Spring5、SpringBoot2、Project Reactor等技术开发的高性能API网关组件

#### SpringCloud gateway三大核心概念

##### 路由(Route)

网关最基本的模块，由一个Id、一个目标Url、一组断言(Predicate)和一组过滤器(Filter)组成

##### 断言(Predicate)

1. 路由转发的判断条件，SpringCloud Gateway通过断言来实现路由的匹配规则，也就是只有满足了断言的条件，才会被转发到指定的服务上进行处理
2. 我们可以通过Predicate的Http请求进行匹配，例如：请求方式、请求路径、请求头、参数等等，如果请求与断言匹配成功，则将请求转发到相应的服务
3. 路由与断言的对应关系为：一对多，当一个请求想要转发到指定路由上时，必须同时满足路由上的所有断言，且请求只会被首个成功匹配的路由转发

##### 过滤器(Filter)

我们可以使用过滤器对请求进行拦截和修改，还可以使用它对上下文的相应进行再处理

###### 过滤器分类：

1. Pre类型：这种过滤器在**请求被转发到微服务之前**可以对请求进行拦截和修改，例如参数校验、权限校验、流量监控、日志输出以及协议转换等操作
2. Post类型：这种过滤器在**微服务对请求做出响应后可以对响应进行拦截和再处理**，例如修改响应内容或响应头、日志输出、流量监控等

作用范围划分：

1. GatewayFilter：应用在单个路由或一组路由上的过滤器
2. GlobalFilter：应用在所有的路由上的过滤器
3. 可以自定义全局过滤器：实现GlobalFileter、复写方法

#### SpringCloud Gateway工作流程

![Spring Cloud Gateway 工作流程](http://c.biancheng.net/uploads/allimg/211210/101P45T2-1.png)

1. 客户端将请求发送到SpringCloud Gateway上
2. gateway通过Gateway Handler Mapping找到与请求相匹配的路由，将其发送给Gateway Web Hander
3. Web Hander通过指定的过滤器链，将请求转发到实际的服务节点中执行相关业务逻辑，然后返回响应结果
4. 过滤器在转发之前(Pre)或之后(post)可能会执行相关的逻辑
5. 过滤器（Filter）可以在请求被转发到服务端前，对请求进行拦截和修改，例如参数校验、权限校验、流量监控、日志输出以及协议转换等
6. 过滤器可以在响应返回客户端之前，对响应进行拦截和再处理，例如修改响应内容或响应头、日志输出、流量监控等
7. 响应返回给客户端

#### Spring Cloud Gateway动态路由

默认情况下，gateway会根据服务注册中心中维护的服务列表，以服务名作为路径创建动态路由进行转发，从而实现动态路由功能

将配置文件中Route的uri地址修改为：

~~~yaml
lb://service-name
#lb:uri的协议，表示开启springcloud gateway的负载均衡功能
#service-name:服务名，springcloud gateway会根据它获取到的具体微服务地址
~~~









































