### Spring

#### Spring IoC、DI

https://blog.csdn.net/a745233700/article/details/80959716

##### IoC

1. Inversion of control：控制反转，指对象的控制权转移给Spring框架，由Spring来负责控制对象的生命周期(比如：创建、实例化、属性赋值、初始化、销毁)和对象间的依赖关系
2. 以前通过new来自己把控创建对象，而Ioc是由专门的容器来帮忙创建对象，将所有类都在Spring容器中登记，当需要某个对象时，只需要告诉Spring容器，spring就会在系统运行到适当的时机把想要的对象主动给你。
3. Spring Ioc容器帮我们创建、查找、住入依赖对象，而引用对象只是被动的接受依赖对象，所以叫控制反转

##### DI

1. Ioc在程序运行时，动态的向某个对象提供它所需要的其他对象，这一点是通过DI实现的，即：应用程序在运行时依赖IoC容器来动态注入对象所需要的外部依赖。
2. DI具体就是通过反射实现注入的，反射运行程序在运行的时候动态生成对象、执行对象的方法、改变对象的属性等等

##### IoC原理

Spring的IoC实现原理为：工厂模式+反射机制，

#### Spring AOP

1. 面向切面编程的思想里面，把功能分为核心业务功能和周边功能，作为面向对象的一种补充
2. 核心功能：登录、增删改查都叫核心业务功能
3. 周边功能：性能统计、日志、事务管理等
4. 周边功能在Spring AOP思想里面，被定义为**切面**
5. 把切面功能和核心业务功能"编织"在一起==》AOP

##### AOP中的概念

Spring Aop是方法级别的Aop框架

![img](https://img-blog.csdnimg.cn/2020120700443256.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70)

1. 切点Pointcut（从这里进行拦截，然后进行增强）
   - 在哪些类、哪些方法上切入；也就是要对哪些连接点进行拦截
   - **目标对象中要增强的方法**
   - 切点分为execution方式和annotation方式。execution方式可以用路径表达式指定对哪些方法拦截。annotation方式可以指定哪些注解修饰的代码进行拦截
2. 连接点Join Point
   - 目标对象中所有要进行增强的方法叫连接点
   - Spring Aop中，一个连接点总代表一个方法的执行
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

##### spring aop 常用注解

1. 那几个通知 before、after 、around、 
2. spring4和spring5对这几个注解有啥区别
3. spring4 正常情况下：环绕之前、**before**、环绕之后、**after**、*afterreturn*ing
4. spring4异常：环绕之前、**before**、**after**、**afterThrowing**
5. spring5正常：环绕之前、before、afterRetruing、after、环绕之后
6. spring5异常：环绕之前、before、afterThrowing、after

---

##### AOP实现原理

AOP代理主要分为静态代理和动态代理。静态代理的代表为AspectJ，动态代理则以Spring AOP为代表

###### AspectJ

aspectJ是静态代理，也称为编译时增强，AOP框架会在编译阶段生成AOP代理类，并将AspectJ（切面）织入到Java字节码中，运行的时候就是增强之后的Aop对象



###### Spring AOP

动态代理就是说Aop框架不会去修改字节码，而是每次运行时在内存中临时为方法生成一个Aop对象，这个Aop对象包含了目标对象的全部方法，并且在特定的切点做了增强处理，并回调原对象的方法。Spring Aop中的动态代理主要有两种方式：JDK动态代理，Cglib动态代理

###### 动态代理

实现方式：利用截取消息的方式，对该消息进行装饰，以取代原有对象行为的执行

~~~tex
1、原理：Aop框架负责动态生成Aop代理类，这个代理类的方法由增强+回调目标对象的方法组成。并将该对象作为目标对象使用。
2、动态代理：在运行期织入增强代码
~~~

1. JDK动态代理   底层通过反射机制动态生成

   ~~~tex
   1、JDK动态代理只提供接口的代理，不支持类的代理，要求被代理类实现接口。
   2、JDK的动态代理核心java.lang.reflect包中的两个类：Proxy和InvocationHandler。InvocationHandler只是一个接口，可以通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，动态的将横切逻辑与业务逻辑织在一起。
   2、Proxy利用InvocationHandler 动态创建一个符合某一接口的实例，生成目标类的代理对象。 其代理对象必须是某个接口的实现, 它是通过在运行期间创建一个接口的实现类来完成对目标 对象的代理.只能实现接口的类生成代理，而不能针对类
   ~~~

   

2. CGLIB动态代理

   - CGLib采用底层的**字节码技术**，可以在运行时动态为指定类创建一个子类对象，并在子类中采用方法拦截的技术拦截所有 父类的调用方法，并顺势织入横切逻辑。它运行期间生成的代理对象是目标类的扩展子类。所以 无法通知final、private的方法,因为它们不能被覆写。是针对类实现代理,主要是为指定的类生 成一个子类，覆盖其中方法。
   -  在spring中默认情况下使用JDK动态代理实现AOP，如果proxy-target-class设置为true或者 使用了优化策略那么会使用CGLIB来创建动态代理.Spring AOP在这两种方式的实现上基本 一样．以JDK代理为例，会使用JdkDynamicAopProxy来创建代理，在invoke()方法首先需 要织入到当前类的增强器封装到拦截器链中，然后递归的调用这些拦截器完成功能的织入，最 终返回代理对象。

静态代理与动态代理区别：在于生成Aop代理对象的时机不同，相对来说AspectJ的静态代理方式具有更好的性能，但是需要特定的编译器进行处理，而Spring Aop则无需特定的编译器处理

---

#### Spring框架中的Bean是线程安全的吗？

Spring容器本身没有提供Bean的线程安全策略，因此可以说Spring容器中的Bean本身不具备线程安全的特性，但是具体情况需要根据Bean的作用域来讨论

1. 对于prototype作用域的bean，每次都会创建一个新的对象，也就是线程之间不存在bean共享，因此不会有线程安全问题

2. 对于singleton作用域的bean，所有线程都共享一个单例的bean，因此是存在线程安全问题的。但是如果单例bean是一个无状态的，也就是线程的操作不会对Bean的成员执行查询以外的操作，那么这个单例bean就是线程安全的。比如：Controller、Service、Dao等，这些Bean是无状态的，只关注于方法本身。

   ~~~tex
   1、有状态bean:就是有实例变量的对象，可以保存数据，是非线程安全的
   2、无状态bean:就是没有实例变量的对象，不能保存数据，是不变类，是线程安全的
   
   3、对于有状态的bean（比如Model和View），就需要自行保证线程安全，最浅显的解决办法就是将有状态的bean的作用域由“singleton”改为“prototype”。
   
   也可以采用ThreadLocal解决线程安全问题，为每个线程提供一个独立的变量副本，不同线程只操作自己线程的副本变量。
   
   ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。同步机制采用了“时间换空间”的方式，仅提供一份变量，不同的线程在访问前需要获取锁，没获得锁的线程则需要排队。而ThreadLocal采用了“空间换时间”的方式。ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。
   ~~~

   

---

#### Spring的三级缓存  

循环依赖下面给出解释

##### spring循环依赖

https://blog.csdn.net/a745233700/article/details/110914620

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
      /**
      *A/B两个对象在三级缓存里面迁移过程
      */
      //1. A创建过程需要B,于时A将自己放到三级缓存里面，去实例化B
      //2. B实例化的时候发现需要A,于时B先去查询一级缓存没有，再去查询二级缓存，还是没有，查询三级缓存，找到A。把三级缓存里面的A放到二级缓存，并删除三级缓存里面的A
      //3.B完成初始化工作，将自己放到一级缓存（此时B里面的A还处于创建中状态），然后回来创建A,此时B已经创建结束，直接从一级缓存里面获取B,然后完成创建，并将自己放到一级缓存里面
      ~~~

2. 两种注入方式对循环依赖的影响

   - 官网解释
   - 构造方法依赖注入可能造成循环依赖
   - setter方式注入依赖且在多例模式下产生循环依赖。每一次getBean()时，都会产生一个新的Bean，如此反复下去就会有无穷无尽的Bean产生了，最终就会导致OOM问题的出现
   - setter方法进行依赖注入且是在单例模式下产生的循环依赖问题（spring内部解决了这个问题，通过三级缓存）
   - 结论：ABC循环依赖问题 只要注入方式是setter且是Singleton就不会有循环依赖问题

3. **spring内部解决方式**：Spring解决单例模式下的setter方法依赖注入引起的循环依赖通过三级缓存解决

   - spring三级缓存DefaultSingletonBeanRegistry
   - 也就是三个Map
   - 一级缓存ConcurrentHashMap  singletonObjects
     1. （单例池）存放已经经历了完整生命周期的Bean对象。=>成品Bean
   - 三级缓存 HashMap  singletonFactories
     1. 三级缓存：存放可以生成Bean的工厂=》准备构建Bean的

   - 二级缓存HashMap earlySingletonObjects
     1. 二级缓存：存放早期暴露出来的Bean对象，Bean的生命周期未结束（属性还未填充完成）=》半成品Bean

4. 结论：只有单例的Bean，会通过三级缓存提前暴露来解决循环依赖的问题。并非单例的Bean，每次从容器中获取的都是一个新的对象，都会重新创建，所以，非单例的Bean是没有缓存的，不会将其放到三级缓存中

5. 解决的核心原理就是：在对象实例化之后，依赖注入之前，Spring提前暴露的Bean实例的引用在第三级缓存中进行存储

##### 为什么要用三级缓存？两级、一级行不行？

如果仅仅是解决循环依赖问题，使用二级缓存就可以了，但是如果对象实现了AOP，那么注入到其他bean的时候，并不是最终的代理对象，而是原始的。这时就需要通过三级缓存的ObjectFactory才能提前产生最终的需要代理的对象

##### 解决构造函数相互注入造成的循环依赖：

1. 前面说Spring可以自动解决单例模式下通过setter()方法进行依赖注入产生的循环依赖问题。而对于通过构造方法进行依赖注入时产生的循环依赖问题没办法自动解决，那针对这种情况，我们可以使用@Lazy注解来解决。
2. 也就是说，对于类A和类B都是通过构造器注入的情况，可以在A或者B的构造函数的形参上加个@Lazy注解实现延迟加载。@Lazy实现原理是，当实例化对象时，如果发现参数或者属性有@Lazy注解修饰，那么就不直接创建所依赖的对象了，而是使用动态代理创建一个代理类。
3. 比如，类A的创建：A a=new A(B)，需要依赖对象B，发现构造函数的形参上有@Lazy注解，那么就不直接创建B了，而是使用动态代理创建了一个代理类B1，此时A跟B就不是相互依赖了，变成了A依赖一个代理类B1，B依赖A。但因为在注入依赖时，类A并没有完全的初始化完，实际上注入的是一个代理对象，只有当他首次被使用的时候才会被完全的初始化。

---



#### Spring bean生命周期

https://blog.csdn.net/knknknkn8023/article/details/107130806/

1. 普通Java对象
   - 实例化对象
   - 对象不使用时，垃圾回收机制进行回收
2. Spring bean
   - 实例化bean
   - 属性赋值
   - 初始化
   - bean的使用
   - 容器关闭时 销毁

![Spring 生命周期流程](http://c.biancheng.net/uploads/allimg/220119/1F32KG1-0.png)

##### Bean 生命周期的整个执行过程描述如下。

1. Spring 启动，查找并加载需要被 Spring 管理的 Bean，对 Bean 进行实例化。
2. 对 Bean 进行属性注入。
3. 如果 Bean 实现了 BeanNameAware 接口，则 Spring 调用 Bean 的 setBeanName() 方法传入当前 Bean 的 id 值。
4. 如果 Bean 实现了 BeanFactoryAware 接口，则 Spring 调用 setBeanFactory() 方法传入当前工厂实例的引用。
5. 如果 Bean 实现了 ApplicationContextAware 接口，则 Spring 调用 setApplicationContext() 方法传入当前 ApplicationContext 实例的引用。
6. 如果 Bean 实现了 BeanPostProcessor 接口，则 Spring 调用该接口的预初始化方法 postProcessBeforeInitialzation() 对 Bean 进行加工操作，此处非常重要，Spring 的 AOP 就是利用它实现的。
7. 如果 Bean 实现了 InitializingBean 接口，则 Spring 将调用 afterPropertiesSet() 方法。
8. 如果在配置文件中通过 init-method 属性指定了初始化方法，则调用该初始化方法。
9. 如果 BeanPostProcessor 和 Bean 关联，则 Spring 将调用该接口的初始化方法 postProcessAfterInitialization()。此时，Bean 已经可以被应用系统使用了。
10. 如果在 <bean> 中指定了该 Bean 的作用域为 singleton，则将该 Bean 放入 Spring IoC 的缓存池中，触发 Spring 对该 Bean 的生命周期管理；如果在 <bean> 中指定了该 Bean 的作用域为 prototype，则将该 Bean 交给调用者，调用者管理该 Bean 的生命周期，Spring 不再管理该 Bean。
11. 如果 Bean 实现了 DisposableBean 接口，则 Spring 会调用 destory() 方法销毁 Bean；如果在配置文件中通过 destory-method 属性指定了 Bean 的销毁方法，则 Spring 将调用该方法对 Bean 进行销毁。

#### Bean装配

bean装配是指在Spring容器中把bean组装到一起，前提是容器需要知道bean的依赖关系，如何通过依赖注入来把它们装配在一起

##### bean的自动装配

自动装配就是spring会在上下文中自动查找，并自动给bean装配到与其关联的属性

###### xml文件方式实现

1. no:默认的方式是不进行自动装配的，通过手工设置ref属性来进行装配bean
2. byName:通过bean的名称进行自动装配，如果一个bean的 property 与另一bean 的name 相同，就进行自动装配
3. byType:通过参数的数据类型进行自动装配
4. constructor:利用构造函数进行装配，并且构造函数的参数通过byType进行装配
5. autodetect:自动探测，如果有构造方法，通过 construct的方式自动装配，否则使用 byType的方式自动装配

###### 注解方式实现

1. @Autowired

   - 可以作用于：构造函数上、成员变量、setter方法上
   - 默认按照类型进行装配注入
   - 在启动spring IoC时，容器自动装载了一个AutowiredAnnotationBeanPostProcessor后置处理器，当容器扫描到@Autowied、@Resource或@Inject时，就会在IoC容器自动查找需要的bean，并装配给该对象的属性

   - 使用Autowired时，首先在容器中查询对应类型的bean，如果查询结果刚好有一个，就将该bean装配给@Autowired指定的数据，如果查询的结果不止一个，那么@Autowired会根据名称来查找
   - 如果上述查找的结果为空，会抛出异常。解决方法：required=false

2. @Resource

   - 默认按照名称来装配注入，只有当找不到与名称匹配的bean才会按照类型来装配注入

#### Spring事务的实现方式和实现原理

Spring事务的本质就是数据库对事务的支持，没有数据库对事务的支持，spring是无法提供事务功能的

Spring只提供统一事务关联接口，具体实现都是由各个数据库自己实现，数据库事务的提交和回滚是通过redo log和undo log实现的。

Spring会在事务开始时，根据当前环境中设置的隔离级别，调整数据库隔离级别，由此保持一致

##### Spring事务的种类

spring支持编程式事务管理和声明式事务管理

###### 编程式事务管理

使用：Transaction Template

###### 声明式事务管理

1. 声明式事务管理建立在Aop之上。本质是通过Aop功能，对方法前、后进行拦截，将事务处理的功能编织到拦截的方法中，也就是在目标方法开始之前启动一个事务，在执行目标方法之后根据执行情况提交或回滚事务
2. 声明式事务最大的优点：不需要在业务代码中参杂事务管理的代码，只需要在配置文件中做相关的事务规则声明或通过@Transaction注解的方式，便可以将事务规则应用到业务逻辑中，减少业务代码的污染。
3. 不足：最细粒度只能作用到方法级别，无法做到像编程式事务那样可以作用到代码块级别

##### Spring的事务传播机制

Spring事务的传播机制说的是，当多个事务同时存在时，spring如何处理这些事务的行为。

事务传播机制采用ThreadLocal实现的，所以如果调用的方法是在新线程调用的，那么，事务传播实际上是会失效的

Propagation_Required:(默认传播行为)如果当前没有事务，就创建一个新事务；如果当前存在事务，就加入该事务

Propagation_Requires_new:无论当前是否存在事务，都创建新事务进行执行

Propagation_Supports:如果当前存在事务，就加入该事务；如果当前不存在事务，就以非事务执行

Propagation_Not_Supported:以非事务方式执行操作，如果当前存在事务，就把当前事务挂起

Propagation_Nested:如果当前存在事务，则在嵌套事务内执行；如果当前没有事务，则按REQUIRED属性执行

Propagation_Mandatory:如果当前存在事务，就加入该事务；如果当前不存在事务，就抛出异常

Propagation_Never:以非事务方式执行，如果当前存在事务，则抛出异常

##### Spring中事务的隔离级别

Isolation_Default:这是个 PlatfromTransactionManager 默认的隔离级别，使用数据库默认的事务隔离级别

Isolation_Read_Uncommitted:读未提交，允许事务在执行过程中，读取其他事务未提交的数据

Isolation_Read_Committed:读已提交，允许事务在执行过程中，读取其他事务已经提交的数据

Isolation_Repeatable_Read:可重复读，在同一个事务内，任意时刻的查询结果都是一致的

Isolation_Serializable:所有事务逐个依次执行

















































