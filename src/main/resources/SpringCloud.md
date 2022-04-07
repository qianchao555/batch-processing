## RPC

### RPC是什么

1. 远程过程调用协议（Remote Procedure Call），是一种通过网络从远程计算机上请求服务，而不需要了解底层网络技术的协议，是分布式系统常见的一种通信方法。
   - 例如：两台服务器A、B，一个应用部署在A服务器上，想要调用B服务器上应用提供的函数/方法，由于不在一个内存空间，所有不能直接调用，需要通过网络来表达调用的语义和传达调用的数据
2. 过程是什么：过程就是业务处理、计算任务，更直白说就是程序，就是像调用本地方法一样调用远程的过程
3. ![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/45366c44f775abfd0ac3b43bccc1abc3_1440w.jpg)

### RPC和本地调用区别

远程调用需要通过网络，所有响应比本地调用要慢几个数量级，也不那么可靠

### RPC模式

RPC采用客户端/服务端的模式，通过request-response消息模式实现



### RPC三个过程

1. 通讯协议
   - 首先，要解决通讯的问题，主要是通过在客户端和服务器之间建立TCP连接，远程过程调用所有交换的数据都在这个连接里传输。
   - 连接可以是按需连接，调用结束后就断掉，也可以是长连接，多个远程过程调用共享同一个连接
2. 寻址问题
   - 也就是说，A服务器上的应用怎么告诉底层的RPC框架，如何连接到B服务器（如主机或IP地址）以及特定的端口、方法的名称名称是什么，这样才能完成调用。
   - 比如基于Web服务协议栈的RPC，就要提供一个endpoint(端点) URI，或者是从UDDI服务上查找。如果是RMI调用的话，还需要一个RMI Registry来注册服务的地址
3. 数据序列化
   - 当A服务器上的应用发起远程过程调用时，方法的参数需要通过底层的网络协议如TCP传递到B服务器，由于网络协议是基于二进制的，内存中的参数的值要序列化成二进制的形式，也就是序列化（Serialize）或编组（marshal），通过寻址和传输将序列化的二进制发送给B服务器
   - B服务器收到请求后，需要对参数进行反序列化，恢复为内存中的表达方式，然后找到对应的方法（寻址的一部分）进行本地调用，然后得到返回值
   - 返回值还要发送回服务器A上的应用，也要经过序列化的方式发送，服务器A接到后，再反序列化，恢复为内存中的表达方式，交给A服务器上的应用

### 为什么要是使用RPC

1. 服务化/微服务
2. 分布式系统架构
3. 服务可重用
4. 系统间交互调用

### RPC的流程

1. 客户端处理过程中调用client sub，就像调用本地方法一样，传入参数
2. client sub将参数编组为消息，然后通过系统调用像服务端发送消息
3. 客户端本地的操作系统将消息从客户端发送到服务端
4. 服务端接收的数据包传递给server sub
5. server sub将接收到的数据解组为参数
6. server sub再调用服务端的过程，过程执行的结果以反方向的相同步骤响应给客户端

sub(存根)：分布式计算中存根是一段代码，它转换在远程过程调用期间client和server之间传递的参数



### RPC协议

RPC调用过程中需要将消息进行编组然后发送，接收方需要解组消息为参数，过程处理结果也需要经过编组、解组；消息由哪些部分构成以及消息的表示 形式就构成了消息协议。

RPC协议规定请求消息、响应消息的格式，在TCP之上我们可以选用或自定义消息协议来实现RPC的交互

### RPC框架

封装好了参数编组、消息解组、底层网络通信的RPC程序开发框架，可以直接在此基础上编写。常见的RPC框架：Dubbo、SpringCloud、apache Thrift、gRPC等等

### 服务暴露

远程服务提供者需要以某种形式提供服务调用的相关信息，服务调用者通过一定的途径获取远程访问调用的相关信息

### 远程代理对象

服务调用者使用的服务实际上是远程服务的本地代理，说白了就是通过动态代理实现

### 通信

RPC框架的通信与具体的协议无关，具体可基于Http或Tcp协议实现

### 序列化

传输方式和序列化会之间影响RPC的性能，不同的Rpc框架可以在序列化方式上采用不同的技术，从而提高性能

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

所有服务都在Eureka服务器上注册并通过调用Eureka服务器完成服务查找发现

主启动类上添加@EnableEurekaServer开启服务



#### Eureka心跳及自我保护机制

1. 应用服务启动后，各节点会像Eureka Server发送心跳，默认周期为30s，如果Eureka Server在多个心跳周期(默认90s）没有收到某个节点的心跳，Eureka Server将会从服务注册列表中把这个服务节点移除。

2. Eureka服务器向客户端公开下面资源以让其发送心跳

   - PUT /eureka/apps/{app id}/{instance id}?status={status}
   - {instance id}采用 hostname:app id:port，其中app id代表标识唯一的Eureka客户端实例，Eureka服务器会识别一些状态数值：UP; DOWN; STARTING; OUT_OF_SERVICE; UNKNOWN.

3. 客户端发送心跳时的URL 例如：

   PUT /eureka/apps/ORDER-SERVICE/localhost:order-service:8886?status=UP
   
4. 自我保护机制

   - Eureka Server在运行期间会去统计心跳成功的比例在15分钟之内是否低于85% , 如果低于85%， Eureka Server会认为当前实例的客户端与自己的心跳连接出现了网络故障，那么Eureka Server会把这些实例(节点)保护起来，让这些实例不会过期导致实例剔除。
   - 这样做就是为了防止Eureka Client可以正常运行, 但是与Eureka Server网络不通情况下， Eureka Server不会立刻将Eureka Client服务剔除
   - 自我保护模式下，Eureka Service会保护服务注册表中的信息，不再删除注册表中的数据，当网络故障恢复后，Eureka Service节点会自动退出自我保护模式

5. 关闭自我保护机制，保证不可用服务及时剔除

   ~~~yaml
   eureka:
     server:
     	enable-selt-preservation: false
   ~~~

#### DiscoverClient作用

可以从注册中心根据服务名 获取注册在Eureka上的服务的信息



#### Eurek遵循AP原则

Eureka Server各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查找发现服务，客户端向某个Eureka服务端注册或查询服务时，如果发现连接失败，则会自动切换到其他可用服务节点，只要有一台服务可用，就能保证服务的可用性。只不过查到的可能不是最新的(不保证强一致性)，即消费者可能获取到过期的服务列表



#### Eureka如何实现高可用

Eureka Server集群，相互注册



#### Eureka数据同步方式

分布式系统数据在多个副本之间的复制方式主要有：

1. 主从复制

   有一个主副本，其他为从副本，所有写操作都提交到主副本，再由主副本更新到其他从副本。写压力主要集中在主副本上，是系统的瓶颈，从副本可用分担读请求

2. 对等复制

   副本之间不分主从，任何副本都可以接收写操作，然后每个副本间互相进行数据更新。对等复制模式下，任何副本都可以接收写请求，不存在写压力瓶颈，但是各个副本间的数据同步时可能产生数据冲突

Eureka Server集群采用对等复制即Peer to Peer模式，节点通过彼此相互注册来提高可用性

Server每当自己的信息变更后，就会把自己的最新信息通知给其他Eureka Server保持数据同步



### Consul

作为注册中心使用，属于CP

### Zookeeper

1. zk作为注册中心需要jdk的支持
2. zk注册服务时，存储的是临时节点



### Eureka和Zookeeper区别

1. Eureka满足AP

   当向注册中心查询服务列表时，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务直接down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性

2. Zookeeper满足CP

   但是ZooKeeper会出现这样一种情况，当Master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30 ~ 120s，且选举期间整个ZooKeeper集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得ZooKeeper集群失去Master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的

---



### Ribbon

#### Ribbon是什么

1. ribbon是Netflix发布的开源项目

2. 主要功能是**提供客户端软件的负载均衡算法和服务调用(并不是它发起调用，而是它决定调用哪个服务)**

3. Ribbon客户端组件提供了一系列完善的配置项，根据配置信息，Ribbon会自动基于某种规则(如：轮询、随机等）去连接不同的机器。

   

#### 负载均衡

##### 软件负载均衡

nginx：Nginx是**服务器负载均衡**，客户端所有请求都会交给nginx,然后由nginx实现转发请求。即负载均衡是由服务端实现的

##### 负载均衡类型

集中式负载均衡：在消费者和服务提供方中间使用独立的代理方式进行负载，有硬件的（比如 F5），也有软件的（比如 Nginx）

进程内负载均衡：将LB逻辑集成到消费方,消费方从服务注册中心获知有哪些地址可用，然后自己再从这些地址中选择出一个合适的服务器。Ribbon就属于进程内LB,它只是一个类库, 集成于消费方进程,消费方通过它来获取到服务提供方的地址。



#### Ribbon实现原理

使用discoverClient**从注册中心读取目标服务信息**，对同一接口请求进行计数，使用取余算法获取目标服务集群索引，返回获取到的目标服务信息，

#### Ribbon实现负载均衡

Ribbon 是一个客户端的负载均衡器，它可以与 Eureka 配合使用轻松地实现客户端的负载均衡。Ribbon 会先从 Eureka Server（服务注册中心）去获取服务端列表，然后通过负载均衡策略将请求分摊给多个服务端，从而达到负载均衡的目的

Ribbon本地负载均衡，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到JVM本地,从而在本地实现RPC远程服务调用技术。



Ribbon核心组件：IRule

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



#### RestTemplate

1. RestTemplate作用是发送http请求的客户端，用于2个服务之间的请求发送
2. 其简化了Rest Api调用，只需要使用它的一个方法，就可以完成请求、响应、Json转换

API

1. getForObject/getForEntity  (url，转换的类型.class，提交的参数)

   ![image-20220406212604863](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204062126157.png)

​		![image-20220406212830912](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204062128387.png)

1. postForObject/postForEntity   （url，协议体数据，转换的类型.calss)

RestTemplate并没有限制Http的客户端类型，目前常用的三种都支持：HttpClient、OkHttp、Jdk原生的UrlConnection



RestTemplate不+Ribbon也可以调用，只是没有负载均衡而已

RestTemplate+Ribbon可实现 ：带负载均衡的远程调用

~~~java
@Configuration
public class ApplicationContext {
    @Bean
    @LoadBalanced
    public RestTemplate GetrestTemplate(){
        return new RestTemplate();
    }
}
~~~



---



### Feign

#### 什么是Feign

声明式服务调用组件，我们只需要声明一个接口并通过注解进行简单的配置（类似与Dao接口上面的Mapper注解一样）即可实现对http接口的绑定

它底层是基于http协议，是一个http请求调用的轻量级框架，可以以Java接口注解的方式调用Http请求，Feign通过处理注解，将请求模板化，当时就调用的时候，传入参数，再应用到请求上，从而转化成真正的请求，封装了http调用流程

Feign默认使用JDK原生的URLConnection发送Http请求，没有连接池、对每个请求地址会保持一个长连接。建议换用Apache HttpClient作为底层的http client包，从而获取连接池、超时时间等与性能相关的控制能力

Feign真正发送Http请求是委托为feign.client来做的

~~~yaml
#换底层http客户端
feign.httpclient.enabled: true
~~~



使用方法：定义一个服务接口，然后在上面添加注解



原理：

1. 程序启动时，会扫描所有包下@FeignClient注解的类，并将这些类注入到Spring Ioc容器中，当定义的Fiegn中的接口被调用时，通过Jdk动态代理为其生成RequestTemplate
2. RequestTemplate中包含了请求的所有信息。例如：请求参数、请求url等等
3. 



Feign内置了一个重试器，当Http请求出现IO异常时，会有一个最大尝试次数发送请求

#### SpringCloud服务间通信方式

Feign(集成了ribbon)

RestTemplate+ribbon



这两种方式都是采用Restful API接口调用服务的http接口，参数和结果都采用默认的jackson进行序列化和反序列化

- RestTemplate是Spring提供的用于访问Rest服务的客户端
- 它在Http客户端库(例如：HttpURLConnection、HttpComponents、okHttp等)的基础上，封装了简单易用的模板方法API
- restTemplate处理异常：ResponseErrorHandler、DefaultResponseErrorHandler等几个类

#### Feign超时配置控制

其服务调用以及负载均衡是依靠底层Ribbon实现的，因此超时控制通过Ribbon来设置

~~~yaml
ribbon:
  ReadTimeout: 6000 #建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间
  ConnectionTimeout: 6000 #建立连接后，服务器读取到可用资源的时间
~~~



#### RestTemplate和Feing调用服务的区别

1. 调用方式不同
   - RestTemplate需要我们自己构建http请求，模拟http请求然后发给其他服务，步骤繁琐
   - Feign在Ribbon基础上进行了一次封装，采用接口的方式，将需要调用的服务方法定义成抽象方法保存在本地就可以了，不需要自己构建http请求，直接调用接口就可以了。不过需要调用方法要和本地抽象方法的签名完全一致

---

### OpenFeign

它是 Spring 官方推出的一种声明式服务调用与负载均衡组件，它的出现就是为了替代进入停更维护状态的 Feign

1. 里面集成了ribbon、feign的封装、hystrix
1. feign不支持SpringMvc注解，所有OpenFeign在Feign的基础上支持SpringMvc的注解，例如@RequestMapping等等
1. @FeignClient可以解析SpringMVC的@RequestMapping注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务
2. 其服务调用以及负载均衡是依靠底层Ribbon实现的，因此超时控制通过Ribbon来设置
3. OpenFeign里面默认没有开启Hystrix

#### 常用注解

@EnableFeignClient

启动类上添加，表示开启OpenFeign功能，SpringCloud启动时，会去扫描标记@FeignClient注解的接口生成代理并且注入到Spring容器中

@FeignClient

该注解用于通知OpenFeign组件对@RequestMapping注解下的接口进行解析，并通过动态代理的方式产生实现类，实现负载均衡和服务调用

---



### Hystrix

#### 什么是Hystrix

Spring Cloud Hystrix是一款优秀的服务容错与保护组件，它提供了熔断器功能，能有效地阻止分布式微服务系统中出现联动故障，以提高微服务系统的弹性。Hystrix具有服务降级、服务熔断、线程隔离、请求缓存、请求合并、以及实时故障监控等强大功能

能够保证一个依赖出错的情况下，不会导致整体服务的失败，避免级联故障，提高分布式系统的弹性

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

1. 当某些服务不可用时，为了避免长时间等待造成服务卡顿或者雪崩效应，而主动执行备用的服务降级逻辑，立即返回一个友好的提示，以保障主体业务不受影响，fallback
2. Hystrix实现服务降级的功能是通过重写HystrixCommand中的getFallback方法，或者HystrixObservableCommand的resumeWithFallback()方法，使服务支持服务降级

什么情况下会触发服务降级

1. 程序运行异常
2. 超时
3. 服务熔断发生服务降级
4. 线程池、信号量也会导致服务降级

##### 服务熔断

在服务降级的基础上更直接的一种保护方式，当在一个统计时间范围内的请求失败数量达到设定值或当前的请求错误率达到设定的错误率阈值时开启断路，之后的请求直接走fallback()方法，在设定时间后尝试恢复。

类似与保险丝达到最大访问后，直接拒绝访问，拉闸限电，然后调用访问降级的方法来返回友好提示

![image-20220406231253626](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204062312840.png)

1. 熔断机制是**为了应对雪崩效应**而出现的一种微服务链路保护机制
2. 当微服务系统中的某个微服务不可用或响应时间太长时，为了保护系统的整体可用性，熔断器会暂时切断请求对该服务的调用，并快速返回一个友好的错误响应。这种熔断状态不是永久的，在经历了一定的时间后，熔断器会再次检测该微服务是否恢复正常，若服务恢复正常则恢复其调用链路

具体配置：[参考](https://blog.csdn.net/weixin_44449838/article/details/110765752)

###### 熔断状态及原理

1. 断路器三种状态
   - 打开状态:一段时间内，达到一定的次数无法调用并且多次监测没有恢复的迹象，断路器完全打开，那么下次请求就不会请求该服务，而是快速返回失败响应
   - 半开状态：短时间内，又恢复迹象，断路器会将部分请求发给该服务，正常调用时，断路器关闭
   - 关闭状态：当服务一直处于正常状态，服务能正常调用
2. ![熔断状态转换](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/10162355X-7.png)
3. SpringCloud中，熔断机制通过Hystrix实现。Hystrix会监控微服务间的调用情况，当失败调用到一定比例时，就会启动熔断机制
4. Hystrix 实现服务熔断的步骤如下：
   1. 当服务的调用请求失败数量达到设定值(默认10s内超过20次请求)达到或超过 Hystix 规定的比率（默认为10s内超过 50%请求失败）后，熔断器进入熔断开启状态。
   2. 熔断器进入熔断开启状态后，Hystrix 会启动一个休眠时间窗，在这个时间窗内，该服务的降级逻辑会临时充当业务主逻辑，而原来的业务主逻辑不可用。
   3. 当有请求再次调用该服务时，会直接调用降级逻辑快速地返回失败响应，以避免系统雪崩。
   4. 当休眠时间窗到期后（默认5s），Hystrix 会进入半熔断转态，允许部分请求对服务原来的主业务逻辑进行调用，并监控其调用成功率。
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

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/1019425240-0.png)

工作流程：

1. 开发或运维人员提交配置文件到远程的Git仓库
2. Config服务端(Server)负责连接配置仓库git，并对config客户端暴露获取配置的接口
3. config客户端通过config服务端暴露出来的接口，拉取配置仓库中的配置
4. config客户端拉取获取到的配置信息，以支持服务的运行



服务端：分布式配置中心，是一个独立的微服务

客户端：读取配置

将配置信息以Rest接口的形式暴露



#### Spring Cloud Config可以实现实时刷吗

自身不能

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
2. ![bus+config 动态刷新配置](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/101942GY-11.png)
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
2. 网关常见的功能：**路由转发、权限校验、限流控制等等**
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

   <img src="https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204072311361.png" alt="image-20220407231132016" style="zoom:150%;" />

#### SpringCloud Gateway工作流程

![Spring Cloud Gateway 工作流程](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/101P45T2-1.png)

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



### 服务链路追踪 Zipkin

Zipkin是一个分布式链路追踪系统，可以采集时序数据来协助定位延迟等相关问题

Sleuth提供了一套完整的服务追踪的解决方案，在分布式系统中提供追踪解决方案并且兼容支持了Zipkin

下载：https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/

运行

服务启动后web端地址：http://localhost:9411/zipkin/

架构图

![image-20220407221143409](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204072212823.png)

#### 结构

zipkin包括四个组件：collector、storage、search、webUI，其中collector中有两个参数

1. span：表示一个追踪节点，有唯一标识
2. Trace：表示一条调用链路，根据Span的parentId串联起来

#### 跟踪web请求

springcloud中，采用sleuth来做跟踪处理，具体通过一个拦截器实现

#### 发送跟踪数据

#### 数据存储

zipkin支持MySQL、ES等存储方式

---



## SpringCloud Alibaba

Spring Cloud Alibaba 是阿里巴巴结合自身丰富的微服务实践而推出的微服务开发的一站式解决方案，是 Spring Cloud 第二代实现的主要组成部分

Spring Cloud Alibaba 吸收了 Spring Cloud Netflix 的核心架构思想，并进行了高性能改进。自 Spring Cloud Netflix 进入停更维护后，Spring Cloud Alibaba 逐渐代替它成为主流的微服务框架



### SpringCloud Alibaba三大组件

Nacos：服务注册中心和配置中心

Sentinel：熔断限流

Seata：分布式事务





### Spring Cloud 两代实现组件对比

| Spring Cloud 第一代实现（Netflix） | 状态                                             | Spring Cloud 第二代实现（Alibaba） | 状态                                                 |
| ---------------------------------- | ------------------------------------------------ | ---------------------------------- | ---------------------------------------------------- |
| Ereka                              | 2.0 孵化失败                                     | Nacos Discovery                    | 性能更好，感知力更强                                 |
| Ribbon                             | 停更进维                                         | Spring Cloud Loadbalancer          | Spring Cloud 原生组件，用于代替 Ribbon               |
| Hystrix                            | 停更进维                                         | Sentinel                           | 可视化配置，上手简单                                 |
| Zuul                               | 停更进维                                         | Spring Cloud Gateway               | 性能为 Zuul 的 1.6 倍                                |
| Spring Cloud Config                | 搭建过程复杂，约定过多，无可视化界面，上手难点大 | Nacos Config                       | 搭建过程简单，有可视化界面，配置管理更简单，容易上手 |



### Spring Cloud Alibaba 服务注册与配置中心(Nacos)

[官网地址](https://nacos.io/zh-cn/)

等价于Eureka+config+bus

属于AP模型

Nacos：Dynamic Naming and Configuration Service，由阿里巴巴团队使用Java语言开发的开源项目。

Nacos是一个更易于帮助云原生应用的动态服务发现、配置和服务管理平台

| 组成部分 | 全称              | 描述                                                         |
| -------- | ----------------- | ------------------------------------------------------------ |
| Na       | naming/nameServer | 即服务注册中心，与 Spring Cloud Eureka 的功能类似。          |
| co       | configuration     | 即配置中心，与 Spring Cloud Config+Spring Cloud Bus 的功能类似。 |
| s        | service           | 即服务，表示 Nacos 实现的服务注册中心和配置中心都是以服务为核心的。 |



Nacos可集成Ribbon，可以使用Ribbon来进行负载均衡

#### Nacos特性

##### 服务发现

Nacos支持基于DNS和RPC的服务发现，当服务提供者向Nacos注册服务后，服务消费者可以在Nacos上通过DNS TODO或Http&API 查找、发现服务

##### 服务健康监测

Nacos提供对服务的实时健康检查，能够阻止请求发送到不健康主机或服务实例上。Nacos提供了一个健康检查仪表盘，帮助我们根据健康状态管理服务的可用性以及流量

##### 动态配置服务

动态配置服务可以让我们以中心化、外部化、动态化的方式，管理所有环境的应用配置和服务配置

Nacos默认动态刷新配置文件，历史版本默认保留30天，还有一键回滚功能

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



![Nacos 服务注册与发现](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/1022563360-0.png)
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

##### 动态刷新

@RefreshScope，当配置中心更改配置后，响应的getId值会刷新

~~~java
@RefreshScope
public class IdEntity {
    @Value("${id}")
    private int id;
    public int getId(){
        return this.id;
    }
}
~~~





#### Nacos服务配置三种方案

1. DataId
   - 指定DataId
   - 在web端配置一个config
   - 在ymal中激活对应的环境
2. Group
   - 通过Group实现环境区分
   - 新建group
   - 修改配置文件信息
3. Namespace
   - 新建命名空间
   - 新建DataId

三者关系：![image-20220407230120882](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204072301174.png)







#### Nacos Server集群化部署（后期深入）

![image-20220407230202144](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204072302345.png)

三种部署方式：

1. 单机模式：用于调试
2. 集群模式：生产环境
3. 多集群模式：多数据环境



#### Nacos持久化

默认自带嵌入式数据库derby

derby切换到本地库步骤

1. 在nacos安装目录下的conf中找到sql脚本，然后导入本地数据库中
2. 找到application.properties，修改其默认数据库配置信息
3. 重启nacos，这时，使用的就是笨的数据库了

---

### Spring Cloud Alibaba Sentinel

[参考文章](http://c.biancheng.net/springcloud/sentinel.html)

是一种面向分布式微服务架构的轻量级高可用流量控制组件

Sentinel 主要以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度帮助用户保护服务的稳定性

功能上来说，Sentinel 与 Spring Cloud Netfilx Hystrix 类似，但 Sentinel 要比 Hystrix 更加强大，例如 Sentinel 提供了流量控制功能、比 Hystrix 更加完善的实时监控功能等等

#### Sentinel的组成

1. Sentinel核心库：Sentinel的核心库不依赖任何框架或库，能够运行与Java8及以上的版本的运行时环境，同时对Spring Cloud、Dubbo等微服务框架提供了很好的支持
2. Sentinel控制台(Dashboard)：Sentinel提供了一个轻量级的开源控制台，它为用户提供了机器自发现、簇点链路自发现、监控、规则配置等功能

Sentinel核心库不依赖Sentinel Dashboard，但两者结合使用可以有效的提高效率，让Sentinel发挥它最大的作用

#### Sentinel基本概念



---

### Spring Cloud Alibaba Seata

[参考文章](http://c.biancheng.net/springcloud/seata.html)

Seata是一个分布式事务处理框架，阿里巴巴和蚂蚁金服共同开源的分布式事务解决方案，能够在微服务架构下提供高性能且简单易用的分布式事务服务

#### 分布式事务相关概念

**事务**：由一组操作构成的可靠、独立的工作单元，事务具备ACID特性，即：原子性、一致性、隔离性、持久性

**本地事务**：本地事务由本地资源管理器(通常指：数据库管理系统，例如MySQL、Oracle等等)管理，严格地支持ACID特性，高效可靠。本地事务不具备分布式事务的处理能力，隔离的最小单位受限于资源管理器，即本地事务只能对自己数据库的操作进行控制，对于其他数据库的操作则无能为力

**分支事务**：分布式事务中，就是一个个受全局事务管辖和协调的本地事务

**全局事务**：指的是一次性操作多个资源管理器完成的事务，由一组分支事务组成

分布式事务可以理解为：一个包含了若干个分支事务的全局事务。全局事务的职责是协调其管辖的各个分支事务达成一致，要么一起成功提交，要么一起失败回滚。通常，分支事务本身就是一个满足ACID特性的本地事务

---



#### Seata整体工作流程

Seata对分布式事务的协调和控制主要通过XID和3个核心组件实现

##### XID

XID是全局事务的唯一标识，它可以在服务的调用链路中传递，绑定到服务的事务上下文中

##### 核心组件

- TC（Transaction Coordinator）:事务协调器，它是事务的协调者，主要负责维护全局事务和分支事务的状态，驱动全局事务提交或回滚
- TM（Transaction Manager)：事务管理器，它是事务的发起者，负责定义全局事务的范围，并根据TC维护的全局事务和分支事务状态，做出开始事务、提交事务、回滚事务的决议
- RM（Resource Manager)：资源管理器，它是资源的管理者（可以理解为各服务使用的数据库）。它负责管理分支事务上的资源，向TC注册分支事务，汇报分支事务状态，驱动分支事务的提交或回滚



以上三个组件相互协作，TC 以 Seata 服务器（Server）形式独立部署，TM 和 RM 则是以 Seata Client 的形式集成在微服务中运行，其整体工作流程如下图。

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/102A115W-0.png)

 

Seata 的整体工作流程如下：

1. TM 向 TC 申请开启一个全局事务，全局事务创建成功后，TC 会针对这个全局事务生成一个全局唯一的 XID；
2. XID 通过服务的调用链传递到其他服务;
3. RM 向 TC 注册一个分支事务，并将其纳入 XID 对应全局事务的管辖；
4. TM 根据 TC 收集的各个分支事务的执行结果，向 TC 发起全局事务提交或回滚决议；
5. TC 调度 XID 下管辖的所有分支事务完成提交或回滚操作。



#### Seata AT模式

Seata提供了AT、TCC、SAGA、XA四种事务模式，可以快速有效地对分布式事务进行控制，四种模式中，使用最多、最方便的就是AT模式。与其他事务模式相比，AT模式可以应对大多数的业务场景，且基本可以做到无业务入侵，开发人员有更多的精力关注于业务逻辑开发

##### AT模式的前提

任何应用想要使用Seata的AT模式对分布式事务进行控制，必须满足以下2个前提：

1. 必须使用支持本地ACID事务特性的关系型数据库
2. 应用程序必须是使用JDBC对数据库进行访问的Java应用

此外，我们还需要针对业务中涉及的各个数据库表，分别创建一个 UNDO_LOG（回滚日志）表。不同数据库在创建 UNDO_LOG 表时会略有不同，以 MySQL 为例，其 UNDO_LOG 表的创表语句如下：

```mysql
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

##### AT模式的工作机制

大致分为两个阶段（后期深入）



##### @GlobalTransactional注解

分布式微服务中，Seata提供了@GlobalTransactional注解实现分布式事务的开启、管理和控制

当调用这个注解标记的方法时，TM会先向TC注册全局事务，TC生成一个全局的XID，返回给TM

该注解可以在类上使用，亦可以在方法上使用，该注解的使用位置决定了全局事务的范围

- 在类中某个方法使用时，全局事务的范围就是该方法以及它所涉及的所有服务。
- 在类上使用时，全局事务的范围就是这个类中的所有方法以及这些方法涉及的服务。







---

## Dubbo

### Dubbo是什么

是阿里开源的基于Java的高性能RPC分布式服务框架，一款微服务开发框架，它提供了RPC通信和微服务治理两大关键功能

基本原理架构及调用关系：

![image-20220405225319484](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204052253144.png)

1. 将服务转载容器中，服务容器负责启动、加载、运行服务提供者
2. 服务提供者在启动时，向注册中心注册自己提供的服务
3. 服务消费者在启动时，在注册中心订阅自己所需的服务
4. 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者
5. 消费者从提供者列表中，基于软负载均衡算法，选择一台提供者进行调用，如果调用失败会再选另一台进行调用
6. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心

### 为什么要用Dubbo

1. 内部使用了netty、zookeeper保证了高性能可用性
1. 使用Dubbo可以将核心业务抽取出来，作为独立的服务，逐渐形成稳定的服务中心，可用于提高业务复用灵活扩展，使前端应用能更快速的响应多变的市场需求。
2. 分布式架构可以承受更大规模的并发流量

### Dubbo和SpringCloud区别

1. 通信方式不同
   - Dubbo 使用的是 RPC 通信，而Spring Cloud 使用的是HTTP RESTFul 方式
2. 组件组成不同
   - dubbo的服务注册中心为Zookeerper，服务监控中心为dubbo-monitor，无消息总线、服务跟踪、批量任务等组件
   - springcloud的服务注册中心为eureka、服务监控中心为springboot admin，有消息总线，数据流、服务跟踪、批量任务等组件

### Dubbo需要Web容器吗

不需要，如果硬要用Web容器，只会增加复杂性，也浪费资源

### Dubbo内置的服务容器

1. Spring Container
2. Jetty Container
3. Log4j Container

Dubbo的服务容器只是一个简单的Main方法，并加载一个简单的Spring容器，用于暴露服务

---



### Dubbo支持的协议

1. dubbo://（推荐）
1. rmi://
1. hessian://
2. http://
2. webservice://
2. thrift://
3. rest://
4. redis://
5. memcached://

---



### Dubbo里面的几种节点角色

1. provide：暴露服务的服务提供方
2. consumer：调用远程服务的服务消费方
3. registry：服务注册与发现的注册中心
4. monitor：统计服务调用次数和调用时间的监控中心
5. container：服务运行容器

---



### Dubbo服务注册与发现

Dubbo基于消费者端的自动服务发现能力，其基本原理如图：

![image-20220405234944103](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204052349243.png)

#### Dubbo默认注册中心

Dubbo推荐使用Zookeeper作为注册中心，还有redis、multicast、simple等注册中心

服务发现的的一个核心组件是注册中心，Provider 注册地址到注册中心，Consumer 从注册中心读取和订阅 Provider 地址列表。 因此，要启用服务发现，需要为 Dubbo 增加注册中心配置：

~~~yaml
dubbo:
  registry:
    address: 
      zookeeper: //127.0.0.1:2181
~~~





### 服务流量管理

Dubbo通过定义路由规则，实现对流量分布的控制

#### 流量管理

流量管理本质是将请求根据定制好的路由规则分发到应用服务上

![image-20220406100247529](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/image-20220406100247529.png)



- 路由规则可以有多个，不同的路由规则之间存在优先级。如：Router(1) -> Router(2) -> …… -> Router(n)
- 一个路由规则可以路由到多个不同的应用服务。如：Router(2)既可以路由到Service(1)也可以路由到Service(2)
- 多个不同的路由规则可以路由到同一个应用服务。如：Router(1)和Router(2)都可以路由到Service(2)
- 路由规则也可以不路由到任何应用服务。如：Router(m)没有路由到任何一个Service上，所有命中Router(m)的请求都会因为没有对应的应用服务处理而导致报错
- 应用服务可以是单个的实例，也可以是一个应用集群





---

### Dubbo部署架构——三大中心化组件

这三个中心并不是运行Dubbo的必要条件，用户可以根据自身业务情况决定启用一个或多个，以达到简化部署的目的。通常情况下，所有用户都会以独立的注册中心 开始 Dubbo 服务开发，而配置中心、元数据中心则会在微服务演进的过程中逐步的按需被引入进来

Dubbo微服务组件与各个中心交互：

![image-20220406102116248](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/image-20220406102116248.png)

#### 注册中心

协调Consumer和Provider之间的地址注册与发现，它承载着服务注册和服务发现的职责。目前Dubbo支持两种粒度的服务注册和发现，分别是接口级别、应用级别，注册中心可以按需进行部署



#### 配置中心

1. 存储Dubbo启动阶段的全局配置，保证配置的跨环境共享与全局一致性
2. 负责服务治理规则(路由规则、动态配置等等)的存储与推送

#### 元数据中心

1. 接收Provider上报的服务接口元数据，为Admin等控制台提供运维能力(如：服务测试、接口文档等等)
2. 作为服务发现机制的补充，提供额外的接口/方法级别配置信息的同步能力，相当于注册中心的额外配置

---



### Dubbo服务间调用是阻塞的吗

默认是同步等待结果阻塞的，支持异步调用

Dubbo是基于NIO的非阻塞实现并行调用，客户端不需要启动多线程即可完成并行调用多个远程服务，相对多线程开销较小，异步调用会返回一个Future对象

---



### Dubbo序列化框架

默认使用Hessian序列化，还有Duddo、FastJson、Java自带序列化

---



### Dubbo核心配置

![image-20220405234317683](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/202204052343833.png)

---



### 在Provider上可以配置Consumer端的属性有哪些

1. timeout：方法调用超时
2. retries：失败重试次数，默认重试2次
3. loadbalance：负载均衡算法，默认随机
4. actives：消费者端最大并发调用限制

---



### Dubbo负载均衡策略

1. random loadbalance：权重设置随机概率，默认负载均衡策略
2. rounddrobin loadbalance：轮询
3. lastactive loadbalance：最少活跃调用数，若相同则随机
4. 4、consistenthash loadbalance：一致性hash，相同参数的请求总是发送到同一提供者

---



### Dubbo网络通信框架

Dubbo 默认使用 Netty 框架作为网络通信，也是推荐的选择，另外内容还集成有Mina、Grizzly

netty是基于NIO的，它封装了JDK的NIO，使用起来更方便

---



### Netty



#### NIO/BIO

BIO是面向流(字节/字符流)同步阻塞的

NIO是面向缓冲区(或面向块)和非阻塞的，数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区前后移动

#### NIO非阻塞模式

1. 非阻塞读：一个线程从某通道读取数据时，仅能读到目前准备好的数据，如果数据还没准备好就立即返回，该线程可以继续做别的事情，而不是像BIO那样保持线程阻塞
2. 非阻塞写：一个线程写入一些数据到某通道时，先将数据写到缓冲区，等到数据可写时，再将缓冲区的数据写到通道。而BIO调用了write之后就阻塞在这里，即使数据不可写也要一直等待，直到数据完全写出去

##### NIO三大组件

1. IO多路复用器selector
2. 基于缓冲区的双向管道，channel和buffer

![image-20220406113048060](https://gitee.com/qianchao_repo/pic-typora/raw/master/springcloud_img/image-20220406113048060.png)

NIO是面向缓冲和非阻塞的，在一个NIO中一个线程处理selector，一个selector可以处理多个channel。使用selector时，必须将channel注册到selector中

##### Buffer

Buffer本质是一个内存块，基于数组实现，Buffer是一个抽象类，7个基本类型(除了boolean)都有自己的实现类，常用的为ByteBuffer，因为网络数据都是以字节方式传输的

buffer读模式和写模式。既可以读又可以写，但是读写要做切换，也就是说，如果buffer当前模式为写模式，则要显式切换到读模式才可以读数据，反之亦然

// 调用flip方法  buffer.flip();   切换到读模式

##### Channel

常见的几种channel

FileChannel

DatagramChannel(用于udp)

ServerSocketChannel(用于TCP)

SocketChannel(用于TCP)





---



### Dubbo集群容错方案

1. Failover Cluster：失败自动切换，自动重试其他服务器，默认方式
2. Failfast Cluster：快速失败，立即报错，只发起一次调用
3. Failsafe Cluster：失败安全，出现异常时直接忽略
4. Failback Cluster：失败自动恢复，记录失败请求，定时重发
5. Forking Cluster：并行调用多个服务器，只要一个成功即返回
6. Broadcast Cluster：广播逐个调用所有提供者，任意一个报错则报错



### Dubbo分布式事务

暂时不支持！



