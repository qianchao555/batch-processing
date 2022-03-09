## RPC

### RPC是什么

1. 远程过程调用协议（Remote Procedure Call），是一种通过网络从远程计算机上请求服务，而不需要了解底层网络技术的协议，是分布式系统常见的一种通信方法。
2. 过程是什么：过程就是业务处理、计算任务，更直白说就是程序，就是像调用本地方法一样调用远程的过程
3. ![img](https://pica.zhimg.com/80/45366c44f775abfd0ac3b43bccc1abc3_1440w.jpg?source=1940ef5c)

### RPC和本地调用区别

远程调用需要通过网络，所有响应比本地调用要慢几个数量级，也不那么可靠

### RPC模式

RPC采用客户端/服务端的模式，通过request-response消息模式实现



### RPC三个过程

1. 通讯协议：
2. 寻址
3. 数据序列化

### 为什么要是使用RPC

1. 服务化/微服务
2. 分布式系统架构
3. 服务可重用
4. 系统间交互调用

### RPC和其他协议的区别

1. RMI远程方法调用是RPC的一种具体实现，webService、RESTfull都是RPC，只是消息的组织形式、消息协议不同

### RPC的流程

1. 客户端处理过程中调用client sub，就像调用本地方法一样，传入参数
2. client sub将参数编组为消息，然后通过系统调用像服务端发送消息
3. 客户端本地的操作系统将消息从客户端发送到服务端
4. 服务端接收的数据包传递给server sub
5. server sub将接收到的数据解组为参数
6. server sub再调用服务端的过程，过程执行的结果以反方向的相同步骤响应给客户端

sub(存根)：分布式计算中存根是一段代码，它转换在远程过程调用期间client和server之间传递的参数

### RPC核心概念术语



### RPC协议

RPC调用过程中需要将消息进行编组然后发送，接收方需要解组消息为参数，过程处理结果也需要经过编组、解组；消息由哪些部分构成以及消息的表示 形式就构成了消息协议。

RPC协议规定请求消息、响应消息的格式，在TCP之上我们可以选用或自定义消息协议来实现RPC的交互

### RPC框架

封装好了参数编组、消息解组、底层网络通信的RPC程序开发框架，可以直接在此基础上编写。常见的RPC框架：Dubbo、SpringCloud、apache Thrift、GRPC等等

### 服务暴露

### 远程代理对象

### 通信

### 序列化

传输方式和序列化会之间影响RPC的性能

-----



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

#### Eureka两大组件

Eureka采用C/S架构。

1. Eureka Server:服务注册中心，主要用于提供服务注册功能。当微服务启动时，会将自己的服务注册到Eureka Server。Server维护一个可以服务列表，存储了所有注册到Eureka Server的可以服务的信息，这些服务可以在Server管理界面直观看到
2. Eureka Client:客户端，通常指的是微服务系统中各个微服务，主要用于和Server进行交互。微服务应用启动后，Client会向Server发送心跳（默认周期30s）。若Server在多个心态周期内没有收到某个Client的心跳，Server会将它从可用服务列表中移除（默认90s）

#### 服务注册和发现是什么

所有服务都在Eureka服务器上注册并通过调用Eureka服务器完成服务查找

#### Eureka如何实现高可用

1. Eureka集群

#### Eureka自我保护

1. 默认情况下，如果Eureka服务器在一定时间内(默认90s)没有收到某个微服务的心跳，Eureka会进入自我保护模式
2. 自我保护模式下，Eureka Service会保护服务注册表中的信息，不在删除注册表中的数据，当网络故障恢复后，Eureka Service节点会自动退出自我保护模式



#### DiscoverClient作用

可以从注册中心根据服务名 获取注册在Eureka上的服务的信息

#### Eureka和Zookeeper区别

---



### Ribbon

#### Ribbon是什么

1. ribbon是Netflix发布的开源项目
2. 主要功能是提供客户端软件的负载均衡算法和服务调用
3. Ribbon客户端组件提供了一系列完善的配置项，根据配置信息，Ribbon会自动基于某种规则去连接不同的机器

#### 负载均衡的意义？

#### Ribbon实现原理

使用discoverClient从注册中心读取目标服务信息，对同一接口请求进行计数，使用取余算法获取目标服务集群索引，返回获取到的目标服务信息，

#### Ribbon实现负载均衡

Ribbon 是一个客户端的负载均衡器，它可以与 Eureka 配合使用轻松地实现客户端的负载均衡。Ribbon 会先从 Eureka Server（服务注册中心）去获取服务端列表，然后通过负载均衡策略将请求分摊给多个服务端，从而达到负载均衡的目的

SpringCloud Ribbon 提供了一个IRule接口，该接口主要用来定义负载均衡策略，他有7个默认实现类，每一个实现类都是一个负载均衡策略

| 序号 | 实现类                    | 负载均衡策略                                                 |
| ---- | ------------------------- | ------------------------------------------------------------ |
| 1    | RoundRobinRule            | 按照线性轮询策略，即按照一定的顺序依次选取服务实例           |
| 2    | RandomRule                | 随机选取一个服务实例                                         |
| 3    | RetryRule                 | 按照 RoundRobinRule（轮询）的策略来获取服务，如果获取的服务实例为 null 或已经失效，则在指定的时间之内不断地进行重试（重试时获取服务的策略还是 RoundRobinRule 中定义的策略），如果超过指定时间依然没获取到服务实例则返回 null 。 |
| 4    | WeightedResponseTimeRule  | WeightedResponseTimeRule 是 RoundRobinRule 的一个子类，它对 RoundRobinRule 的功能进行了扩展。  根据平均响应时间，来计算所有服务实例的权重，响应时间越短的服务实例权重越高，被选中的概率越大。刚启动时，如果统计信息不足，则使用线性轮询策略，等信息足够时，再切换到 WeightedResponseTimeRule。 |
| 5    | BestAvailableRule         | 继承自 ClientConfigEnabledRoundRobinRule。先过滤点故障或失效的服务实例，然后再选择并发量最小的服务实例。 |
| 6    | AvailabilityFilteringRule | 先过滤掉故障或失效的服务实例，然后再选择并发量较小的服务实例。 |
| 7    | ZoneAvoidanceRule         | 默认的负载均衡策略，综合判断服务所在区域（zone）的性能和服务（server）的可用性，来选择服务实例。在没有区域的环境下，该策略与轮询（RandomRule）策略类似。 |

Spring Cloud Ribbon默认使用轮询策略选取服务实例，我们也可以根据自身的需求切换负载均衡策略



---



### Feign

#### 什么是Feign

声明式服务调用组件，我们只需要声明一个接口并通过注解进行简单的配置（类似与Dao接口上面的Mapper注解一样）即可实现对http接口的绑定

#### SpringCloud调用接口的方式

Feign(集成了ribbon)

RestTemplate+ribbon

- RestTemplate是Spring提供的用于访问Rest服务的客户端
- 它在Http客户端库(例如：HttpURLConnection、HttpComponents、okHttp等)的基础上，封装了简单易用的模板方法API
- restTemplate处理异常：ResponseErrorHandler、DefaultResponseErrorHandler等几个类



#### RestTemplate和Feing调用服务的区别

1. 调用方式不同
   - RestTemplate需要我们自己构建http请求，模拟http请求然后发给其他服务，步骤繁琐
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
6. Spring Cloud Config默认使用Git存储配置信息

#### 分布式配置中心的作用

动态变更项目配置信息，其不必重新部署项目

#### Spring Cloud Config工作原理

![img](http://c.biancheng.net/uploads/allimg/211210/1019425240-0.png)

工作流程：

1. 开发或运维人员提交配置文件到远程的Git仓库
2. Config服务端(Server)负责连接配置仓库git，并对config客户端暴露获取配置的接口
3. config客户端通过config服务端暴露出来的接口，拉取配置仓库中的配置
4. config客户端拉取获取到的配置信息，以支持服务的运行



#### Spring Cloud Config可以实现实时刷吗

SpringCloud Config实时刷新采用SpringCloud Bus消息总线实现

---

### Spring Cloud Bus

又称消息总线，它能够通过轻量级的消息代理(例如：RabbitMq、kafka等)将微服务架构中的各个服务连接起来，实现广播状态更改、事件推送等功能，还可以实现微服务间的通信功能

目前SpringCloud Bus支持的消息代理：RabbitMq、Kafka

#### 什么是SpringCloud Bus

1. bus就像是一个分布式执行器，用于扩展SpringBoot应用程序的配置文件，也可以用作应用程序直接的通信通道
2. 不能单独通信，需要配合MQ的支持
3. 一般与SpringCloud Config配合做配置中心

#### SpringCloud Bus基本原理

SpringCloud Bus会使用一个轻量级的消息代理来构建一个公共的消息主题(Topic)，这个Topic中的消息会被所有服务实例监听和消费。当其中的一个服务刷新数据时，SpringCloud Bus会把信息保存到Topic中，这样监听这个Topic的服务就收到消息并自动消费

#### SpringCloud Bus动态属性配置的原理

1. 当Git仓库中的配置发生了改变，我们只需要向某一个服务(既可以是Config服务端，也可以是Config客户端)发送一个post请求，SringCloud Bus就可以通过消息代理通知其他服务重新拉取最新配置，以实现配置的动态刷新
2. ![bus+config 动态刷新配置](http://c.biancheng.net/uploads/allimg/211210/101942GY-11.png)
3. 动态刷新步骤：
   - 当 Git 仓库中的配置发生改变后，运维人员向 Config 服务端发送一个 POST 请求，请求路径为“/actuator/refresh”。
   - Config 服务端接收到请求后，会将该请求转发给服务总线 Spring Cloud Bus。
   - Spring Cloud Bus 接到消息后，会通知给所有 Config 客户端。
   - Config 客户端接收到通知，请求 Config 服务端拉取最新配置。
   - 所有 Config 客户端都获取到最新的配置。



动态刷新配置可以全局通知，也可以定点通知

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





## SpringCloud Alibaba

Spring Cloud Alibaba 是阿里巴巴结合自身丰富的微服务实践而推出的微服务开发的一站式解决方案，是 Spring Cloud 第二代实现的主要组成部分

Spring Cloud Alibaba 吸收了 Spring Cloud Netflix 的核心架构思想，并进行了高性能改进。自 Spring Cloud Netflix 进入停更维护后，Spring Cloud Alibaba 逐渐代替它成为主流的微服务框架



### SpringCloud Alibaba组件

Nacos

Sentinel

Dubbo

Seata

Alibaba Cloud OSS

Alibaba Cloud Schedulerx



### Spring Cloud 两代实现组件对比

| Spring Cloud 第一代实现（Netflix） | 状态                                             | Spring Cloud 第二代实现（Alibaba） | 状态                                                 |
| ---------------------------------- | ------------------------------------------------ | ---------------------------------- | ---------------------------------------------------- |
| Ereka                              | 2.0 孵化失败                                     | Nacos Discovery                    | 性能更好，感知力更强                                 |
| Ribbon                             | 停更进维                                         | Spring Cloud Loadbalancer          | Spring Cloud 原生组件，用于代替 Ribbon               |
| Hystrix                            | 停更进维                                         | Sentinel                           | 可视化配置，上手简单                                 |
| Zuul                               | 停更进维                                         | Spring Cloud Gateway               | 性能为 Zuul 的 1.6 倍                                |
| Spring Cloud Config                | 搭建过程复杂，约定过多，无可视化界面，上手难点大 | Nacos Config                       | 搭建过程简单，有可视化界面，配置管理更简单，容易上手 |



### Spring Cloud Alibaba 服务注册与配置中心(Nacos)

Nacos：Dynamic Naming and Configuration Service，由阿里巴巴团队使用Java语言开发的开源项目。

Nacos是一个更易于帮助云原生应用的动态服务发现、配置和服务管理平台

| 组成部分 | 全称              | 描述                                                         |
| -------- | ----------------- | ------------------------------------------------------------ |
| Na       | naming/nameServer | 即服务注册中心，与 Spring Cloud Eureka 的功能类似。          |
| co       | configuration     | 即配置中心，与 Spring Cloud Config+Spring Cloud Bus 的功能类似。 |
| s        | service           | 即服务，表示 Nacos 实现的服务注册中心和配置中心都是以服务为核心的。 |



#### Nacos特性

##### 服务发现

Nacos支持基于DNS和RPC的服务发现，当服务提供者向Nacos注册服务后，服务消费者可以在Nacos上通过DNS TODO或Http&API 查找、发现服务

##### 服务健康监测

Nacos提供对服务的实时健康检查，能够阻止请求发送到不健康主机或服务实例上。Nacos提供了一个健康检查仪表盘，帮助我们根据健康状态管理服务的可用性以及流量

##### 动态配置服务

动态配置服务可以让我们以中心化、外部化、动态化的方式，管理所有环境的应用配置和服务配置

##### 动态DNS服务

Nacos提供动态DNS服务，能够让我们更容易地实现负载均衡、流量控制以及数据中心内网的简单DNS解析服务

---



#### Nacos两大组件

与Eureka类似，Nacos也采用C/S架构

| 组件                                                         | 描述                                                         | 功能                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Nacos Server                                                 | Nacos 服务端，与 Eureka Server 不同，Nacos Server 由阿里巴巴团队使用 Java 语言编写并将 Nacos Server 的下载地址给用户，用户只需要直接下载并运行即可。 | Nacos Server 可以作为服务注册中心，帮助 Nacos Client 实现服务的注册与发现。 |
| Nacos Server 可以作为配置中心，帮助 Nacos Client 在不重启的情况下，实现配置的动态刷新。 |                                                              |                                                              |
| Nacos Client                                                 | Nacos 客户端，通常指的是微服务架构中的各个服务，由用户自己搭建，可以使用多种语言编写。 | Nacos Client 通过添加依赖 spring-cloud-starter-alibaba-nacos-discovery，在服务注册中心（Nacos Server）中实现服务的注册与发现。 |
| Nacos Client 通过添加依赖 spring-cloud-starter-alibaba-nacos-config，在配置中心（Nacos Server）中实现配置的动态刷新。 |                                                              |                                                              |

#### Nacos服务注册中心

Nacos 作为服务注册中心可以实现服务的注册与发现，流程如下图。



![Nacos 服务注册与发现](http://c.biancheng.net/uploads/allimg/211210/1022563360-0.png)
图1：Nacos 服务注册与发现


在图 1 中共涉及到以下 3 个角色：

- 服务注册中心（Register Service）：它是一个 Nacos Server，可以为服务提供者和服务消费者提供服务注册和发现功能。
- 服务提供者（Provider Service）：它是一个 Nacos Client，用于对外服务。它将自己提供的服务注册到服务注册中心，以供服务消费者发现和调用。
- 服务消费者（Consumer Service）：它是一个 Nacos Client，用于消费服务。它可以从服务注册中心获取服务列表，调用所需的服务。


Nacos 实现服务注册与发现的流程如下：

1. 从 Nacos 官方提供的下载页面中，下载 Nacos Server 并运行。
2. 服务提供者 Nacos Client 启动时，会把服务以服务名（spring.application.name）的方式注册到服务注册中心（Nacos Server）；
3. 服务消费者 Nacos Client 启动时，也会将自己的服务注册到服务注册中心；
4. 服务消费者在注册服务的同时，它还会从服务注册中心获取一份服务注册列表信息，该列表中包含了所有注册到服务注册中心上的服务的信息（包括服务提供者和自身的信息）；
5. 在获取了服务提供者的信息后，服务消费者通过 HTTP 或消息中间件远程调用服务提供者提供的服务

#### Nacos配置中心

Nacos Server 还可以作为配置中心，对 Spring Cloud 应用的外部配置进行统一地集中化管理。而我们只需要在应用的 POM 文件中引入 spring-cloud-starter-alibaba-nacos-config 即可实现配置的获取与动态刷新。

从配置管理的角度看，Nacos 可以说是 Spring Cloud Config 的替代方案，但相比后者 Nacos 的使用更简单，操作步骤也更少

#### Nacos Server集群化部署（后期深入）





---

### Spring Cloud Alibaba Sentinel

是一种面向分布式微服务架构的轻量级高可用流量控制组件

Sentinel 主要以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度帮助用户保护服务的稳定性

功能上来说，Sentinel 与 Spring Cloud Netfilx Hystrix 类似，但 Sentinel 要比 Hystrix 更加强大，例如 Sentinel 提供了流量控制功能、比 Hystrix 更加完善的实时监控功能等等



























