#### Spring AOP

1. 面向切面编程的思想里面，把功能分为核心业务功能，和周边功能
2. 核心功能：登录、增删改查都叫核心业务功能
3. 周边功能：性能统计、日志、事务管理等
4. 周边功能在Spring AOP思想里面，被定义为**切面**
5. 把切面功能和核心业务功能"编织"在一起==》AOP

AOP中的概念

Spring Aop是方法级别的Aop框架

1. 切入点（从这里进行拦截，然后进行增强）
   - 在哪些类、哪些方法上切入
   - **目标对象中要增强的方法**
2. 连接点
   - 目标对象中所有要进行增强的方法叫连接点
3. 通知/增强
   - 在方法执行的什么时间做什么
   - 什么时间=》方法前、后、前后
   - 做什么(增强的功能)
4. 切面
   - 切入点+通知
   - =》在什么时机，什么地方，做什么增强
5. 织入
   - 把切面加入到对象，并创建出代理对象的过程（由Spring完成）
6. 目标对象
   - 被代理对象
7. Proxy
   - 生成的代理对象

---

#### 面试题

##### spring aop 常用注解

1. 那几个通知 before、after 、around、 
2. spring4和spring5对这几个注解有啥区别
3. spring4 正常情况下：环绕之前、**before**、环绕之后、**after**、*afterreturn*ing
4. spring4异常：环绕之前、**before**、**after**、**afterThrowing**
5. spring5正常：环绕之前、before、afterRetruing、after、环绕之后
6. spring5异常：环绕之前、before、afterThrowing、after

##### spring的三级缓存  

循环依赖下面给出解释

##### spring循环依赖

1. 什么是循环依赖

   1. ~~~java
      //多个Bean之间相互依赖，形成一个闭环
      //案例：循环依赖
      class A {
          @Autowired
          private B b;
      }
      class B {
          @Autowired
          private C c;
      }
      class C {
          @Autowired
          private A a;
      }
      //获取Bean时，会报错
      //报错BeanCurrentlyInCreationException 循环依赖问题
      ~~~

2. 两种注入方式对循环依赖的影响

   - 官网解释
   - 构造方法依赖注入可能造成循环依赖
   - **setter方式注入比较好**
   - 结论：ABC循环依赖问题 只要注入方式是setter且是Singleton就不会有循环依赖问题

3. **spring内部解决方式**：三级缓存解决

   - spring三级缓存DefaultSingletonBeanRegistry
   - 也就是三个Map
   - 一级缓存ConcurrentHashMap  singletonObjects
     1. （单例池）存放已经经历了完整生命周期的Bean对象。=>成品Bean
   - 三级缓存 HashMap  singletonFactories
     1. 三级缓存：存放可以生成Bean的工厂=》准备构建Bean的

   - 二级缓存HashMap earlySingletonObjects
     1. 二级缓存：存放早期暴露出来的Bean对象，Bean的生命周期未结束（属性还未填充完成）=》半成品Bean

4. 结论：只有单例的Bean，会通过三级缓存提前暴露来解决循环依赖的问题。并非单例的Bean，每次从容器中获取的都是一个新的对象，都会重新创建，所以，非单例的Bean是没有缓存的，不会将其放到三级缓存中

---

#### 实现原理

##### 动态代理

实现方式：利用截取消息的方式，对该消息进行装饰，以取代原有对象行为的执行

~~~tex

1、原理：Aop框架负责动态生成Aop代理类，这个代理类的方法由增强+回调目标对象的方法组成。并将该对象作为目标对象使用。
2、动态代理：在运行期织入增强代码
    
~~~

1. JDK动态代理   底层通过反射机制动态生成

   ~~~tex
   JDK的动态代理主要涉及java.lang.reflect包中的两个类：Proxy和InvocationHandler。InvocationHandler只是一个接口，可以通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，动态的将横切逻辑与业务逻辑织在一起。
   Proxy利用InvocationHandler 动态创建一个符合某一接口的实例，生成目标类的代理对象。 其代理对象必须是某个接口的实现, 它是通过在运行期间创建一个接口的实现类来完成对目标 对象的代理.只能实现接口的类生成代理，而不能针对类
   ~~~

   

2. CGLIB动态代理

   CGLib采用底层的**字节码技术**，为一个类创建子类，并在子类中采用方法拦截的技术拦截所有 父类的调用方法，并顺势织入横切逻辑.它运行期间生成的代理对象是目标类的扩展子类.所以 无法通知final、private的方法,因为它们不能被覆写.是针对类实现代理,主要是为指定的类生 成一个子类，覆盖其中方法。 在spring中默认。况下使用JDK动态代理实现AOP,如果proxy-target-class设置为true或者 使用了优化策略那么会使用CGLIB来创建动态代理.Spring AOP在这两种方式的实现上基本 一样．以JDK代理为例，会使用JdkDynamicAopProxy来创建代理，在invoke()方法首先需 要织入到当前类的增强器封装到拦截器链中，然后递归的调用这些拦截器完成功能的织入，最 终返回代理对象。

   ---

   

##### 静态织入   

编译时期已经完成代理工作

实现方式：引入特定的语法创建"方面"，从而使得编译器可以在编译期间织入有关"方面"的代码

