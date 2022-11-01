## Spring

### Spring特性和优势

1. 非侵入式：
2. 控制反转：**将对象的创建权交给 Spring 去创建**。使用 Spring 之前，对象的创建都是由我们自己在代码中new创建。而使用 Spring 之后。对象的创建都是给了 Spring 框架
3. 依赖注入：指**依赖的对象不需要手动调用** setXX 方法去设置，而是通过配置赋值
4. 容器：Spring是一个容器，因为它包含并管理应用对象的生命周期
5. 面向切面编程AOP
6. 组件化：
   - Spring 实现了使用简单的组件配置组合成一个复杂的应用
   - 在 Spring 中可以使用XML和Java注解组合这些对象
7. 一站式：在 IOC 和 AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库

---



### Spring各个组件及构成



#### Core Container

Core Container层是：

Spring的核心容器，是其他模块构建的基础，由Beans模块、Core核心模块、Context上下文模块、SpEL表达式语言模块组成

Beans：提供了框架的基础部分，主要包括控制反转和依赖注入

Core：封装了Spring框架的底层部分，包括资源访问、类型转换以及一些常用工具类

Context：建立在Core和Beans模块基础之上，集成Beans模块功能并添加资源绑定、数据验证、国际化、JaveEE支持、容器生命周期、事件传播等。其中，**ApplicationContext接口是上下文接口的焦点**

SpEL：提供了强大的表达式语言的支持（不用太深入，了解使用即可）



#### AOP、Aspects、Instrumentation、Messaging



**AOP 模块**：提供了面向切面编程实现，提供比如日志记录、权限控制、性能统计等通用功能和业务逻辑分离的技术，并且能动态的把这些功能添加到需要的代码中，这样各司其职，降低业务逻辑和通用功能的耦合。

**Aspects 模块**：提供与 AspectJ 的集成，是一个功能强大且成熟的面向切面编程（AOP）框架。

**Instrumentation 模块**：提供了类工具的支持和类加载器的实现，可以在特定的应用服务器中使用。

**messaging 模块**：Spring 4.0 以后新增了消息（Spring-messaging）模块，该模块提供了对消息传递体系结构和协议的支持。

**jcl 模块**： Spring 5.x中新增了日志框架集成的模块。





#### Data Access/Integration

数据访问/集成层，主要包括：JDBC、ORM、OXM、JMS、Transactions模块

JDBC：提供了一个 JDBC 的样例模板，使用这些模板能消除传统冗长的 JDBC 编码还有必须的事务控制，而且能享受到 Spring 管理事务的好处

ORM：提供与流行的“对象-关系”映射框架无缝集成的 API，包括 JPA、JDO、Hibernate 和 MyBatis 等。而且还可以使用 Spring 事务管理，无需额外控制事务

OXM：提供了一个支持 Object /XML 映射的抽象层实现，如 JAXB、Castor、XMLBeans、JiBX 和 XStream。将 Java 对象映射成 XML 数据，或者将XML 数据映射成 Java 对象

JMS：Java消息服务，提供一套 “消息生产者、消息消费者”模板用于更加简单的使用 JMS，JMS 用于用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信

Transactions事务模块：支持**声明式**和编程式事务管理



#### Web模块

Spring的web模块主要包括：web、servlet、websocket、webflux组件

web模块：提供了基本的 Web 开发集成特性，例如多文件上传功能、使用的 Servlet 监听器的 IOC 容器初始化以及 Web 应用上下文

servlet模块：提供了一个 Spring MVC Web 框架实现

websocket模块：提供了简单的接口，用户只要实现响应的接口就可以快速的搭建 WebSocket Server，从而实现双向通讯

webflux模块： Spring WebFlux 是 Spring Framework 5.x中引入的新的**响应式web框架**。与Spring MVC不同，它不需要Servlet API，是完全异步且非阻塞的，并且通过Reactor项目实现了Reactive Streams规范。Spring WebFlux 用于创建基于事件循环执行模型的完全异步且非阻塞的应用程序



#### Test模块

Test 模块：Spring 支持 Junit 和 TestNG 测试框架，而且还额外提供了一些基于 Spring 的测试功能，比如在测试 Web 框架时，模拟 Http 请求的功能。

包含Mock Objects, TestContext Framework, Spring MVC Test, WebTestClient





---

### Spring各个组件是如何配合来完成工作的？



Spring框架管理这些Bean的创建工作，即由用户管理Bean转变为框架管理Bean，这个就叫**控制反转 - Inversion of Control (IoC)**

Spring 框架托管创建的Bean放在哪里呢？ 这便是**IoC Container**;

Spring 框架为了更好让用户配置Bean，必然会引入**不同方式来配置Bean？ 这便是xml配置，Java配置，注解配置**等支持

Spring 框架既然接管了Bean的生成，必然需要**管理整个Bean的生命周期**等；

应用程序代码从Ioc Container中获取依赖的Bean，注入到应用程序中，这个过程叫 **依赖注入(Dependency Injection，DI)** ； 所以说控制反转是通过依赖注入实现的，其实它们是同一个概念的不同角度描述。通俗来说就是**IoC是设计思想，DI是实现方式**

在依赖注入时，有哪些方式呢？这就是构造器方式，@Autowired, @Resource, @Qualifier... 同时Bean之间存在依赖（可能存在先后顺序问题，以及**循环依赖问题**等）



### Spring IoC和DI

Inversion of Control：控制反转，是一种设计思想，Java中Ioc意味着将你设计好的对象交给容器来控制，而不是采用传统的自己在对象内部直接控制

谁控制谁：Spring Ioc容器控制对象

控制了什么：主要控制了外部资源(不只是对象，包括文件等等)

何为反转：由传统自己在对象中主动控制去直接获取依赖对象，反转为由容器来帮我们创建以及注入依赖对象，即由容器帮我们查找以及注入依赖对象，对象只是被动的接受依赖对象，所以是反转

哪些方面被反转了：依赖对象的获取被反转了



DI：依赖注入

组件之间的依赖关系由容器在运行期间决定，即由容器动态的将某个依赖关系注入到组件之中

依赖注入的目的：并非为软件系统带来更多功能，而是为了提升组件重用的频率，并为系统搭建一个灵活、可扩展的平台。通过依赖注入机制，我们只需要通过简单的配置，而无需任何代码就可指定目标需要的资源，完成自身的业务逻辑，而不需要关心具体的资源来自何处，由谁实现

谁依赖谁：应用程序依赖于Ioc容器

为什么需要依赖：应用程序需要Ioc容器来提供对象需要的外部资源

谁注入谁：Ioc容器注入应用程序的某个对象

注入了什么：注入某个对象所需要的外部资源(包括：对象、资源、常量数据等等)



#### Ioc和DI的关系

控制反转是通过依赖注入实现的，它们只是同一概念的不同角度的描述。通俗来讲就是：Ioc是设计思想，DI是实现方式



#### Ioc配置的三种方式

主流方式：注解+Java Config

1. xml
   - 缺点：配置繁琐，不易维护，扩展性差
2. Java config
   - 优点：配置方便，因为是纯Java代码，扩展性高，灵活方便
   - 缺点：声明不明显，如果大量配置，可读性比较差
3. 注解配置
   - 优点：开发便捷通俗易懂，方便维护，例如：@Service、@Component
   - 缺点：具有局限性，对于一些第三方资源，无法添加注释，第三方资源一般采用JavaConfig方式配置



---



### 自己设计一个容器应该怎么搞？

思想！思想！思想！

![image-20221031223741470](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202210312237610.png)



主体部分

- 加载Bean的配置
- 根据Bean的定义，加载生成Bean实例，并放在Bean容器中
- 除了基础的Bean外，还需要一些特殊的bean
- 对容器中的Bean提供统一的管理和调用
- ..........



---



### BeanFactory

Spring Bean，Spring中，bean就类似是定义的一个组件，而这个组件的作用就是实现某个功能

Bean的创建是典型的**工程模式**，通过一系列的Bean工厂，也即Ioc容器为开发者管理对象将的依赖关系提供了很多便利和基础服务

Spring中有许多的Ioc容器的实现供用户选择和使用，这是Ioc容器的基础，在顶层的结构设计主要围绕BeanFactory和BeanRegistry

BeanFactory：工厂模式定义了Ioc容器的基本功能规范

BeanRegistry：向Ioc容器中，手工注册BeanDefinition对象的方法



BeanFactory是Ioc最最基本的容器，负责生产和管理bean，它为其他具体的IOC容器提供了最基本的规范

XmlBeanFactory、ApplicationContext 等等具体的容器都是继承了BeanFactory，在其基础之上附加了其他的功能

ClassPathXmlApplicationContext没有继承自BeanFactory

Spring中的Ioc容器，大致分为两种：BeanFactory和ApplicationContext

1. BeanFactory
   - 是最最基础的Ioc容器，它提供了一个Ioc容器所需的基本功能。
   - BeanFactory默认采用延迟初始化策略，即当容器启动时，未完成Bean的初始化，只有当调用该Bean的实例时，才会完成其初始化操作，并进行依赖注入
2. ApplicationContext
   - ApplicationContext 是在 BeanFactory 的基础上实现的，BeanFactory 的功能它都有，算是一种高级容器
   - ApplicationContext 在 BeanFactory 的基础上提供了事件发布、国际化等功能
   - 同时，ApplicationContext 和 BeanFactory 还有一个很大的不同在于 ApplicationContext 在容器启动时，就会完成所有 Bean 的初始化，这也就意味着容器启动时间较长，并且对系统资源要求也较高

#### 如何将Bean注册到BeanFactory中

##### BeanDefinition

BeanDefinition：各种Bean对象以及其相关的关系，bean对象在Spring实现中是以它来描述的

Bean对象存在依赖嵌套等关系，所以设计了BeanDefinition，用来对Bean对象及其关系定义

理解时，抓住三点

BeanDefiniton：

BeanDefinitionReader：BeanDefinition的解析器

BeanDefinitionHolder：BeanDefinition的包装类，用来存储BeanDefinition



### ApplicationContext

ApplicationContext是Ioc容器的接口类，其继承自BeanFactory，表示应用的上下文，除了对Bean的管理外，还提供了额外的功能

- 访问资源：对不同方式的Bean进行加载（实现ResourcePatternResolver接口）
- 国际化：支持信息源，可实现国际化（实现MessageSource接口）
- 应用事件：支持应用事件（实现ApplicationEventPublisher接口）

![image-20221031232134347](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202210312321505.png)

---



### FactoryBean

Spring BeanFacoty容器中管理两种bean：

1. 标准Java Bean  
2. 另一种是工厂Bean,   即实现了FactoryBean接口的bean  它不是一个简单的Bean 而是一个生产或修饰对象生成的工厂Bean

在向Spring容器获取bean时  对于标准的java Bean  返回的是类自身的实例。而FactoryBean 其返回的对象不一定是自身类的一个实例，返回的是该工厂Bean的getObject方法所返回的对象



 FactoryBean也是一个接口，为Ioc容器中Bean的实现提供了更加灵活的方式，当在IOC容器中的Bean实现了FactoryBean时，通过getBean(String BeanName)获取到的Bean对象并不是FactoryBean的实现类对象，而是这个实现类中的getObject()方法返回的对象。要想获取FactoryBean的实现类，就要getBean(&BeanName)，在BeanName之前加上& 。

当调用getBean("car")时，Spring通过反射机制发现CarFactoryBean实现了FactoryBean的接口，这时Spring容器就调用接口方法CarFactoryBean#getObject()方法返回。如果希望获取FactoryBean的实例，则需要在使用getBean(beanName)方法时在beanName前显示的加上"&"前缀：如getBean("&car")


一般情况下，Spring通过反射机制利用<bean>的class属性指定实现类实例化Bean，在某些情况下，实例化Bean过程比较复杂，如果按照传统的方式，则需要在<bean>中提供大量的配置信息。配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。Spring为此提供了一个org.springframework.bean.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑

FactoryBean接口在Spring框架中有着重要的地位，spring为此提供了很多种不同的实现类

```java
public interface FactoryBean<T> {
 @Nullable
 T getObject() throws Exception;
    
 @Nullable
 Class<?> getObjectType();
    
 default boolean isSingleton() {
  return true;
 }
}
```

- getObject：该方法返回 FactoryBean 所创建的实例，如果在 XML 配置文件中，我们提供的 class 是一个 FactoryBean 的话，那么当我们调用 getBean 方法去获取实例时，最终获取到的是 getObject 方法的返回值。
- getObjectType：返回对象的类型。
- isSingleton：表示getObject 方法所产生的对象是否单例形式存在与容器中。



![image-20220330222504591](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202203302225742.png)

~~~java
//代表这个bean本身就是一个工厂，可以生产其他bean实例，例如：这里生产了teacherBean
//在实例化阶段，BeanFacotry中会涉及到FactoryBean的逻辑，从而调用getObject()返回当前实例
@Component("studentBean")
public class StudentBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return new TeacherBean();
    }
   
    @Override
    public Class<?> getObjectType() {  //注意这个方法主要作用是：该方法返回的类型是在ioc容器中getbean所匹配的类型
        return StudentBean.class;
    }
    //一个学生学习方法
    public void study(){
        System.out.println("学生学习。。。");
    }
}

//*******************************************
public class TeacherBean {
    public void teacher(){
        System.out.println("老师教书。。。。");
    }
}


//***************************
public class Demo1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
        //这里会返回一个teacherBean,因为StudentBean implements FactoryBean，会返回getObject()中的bean
        StudentBean studentBean = (StudentBean)annotationConfigApplicationContext.getBean("studentBean");
        studentBean.study();
    }
}
//************************************
public class Demo1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
        //加上了“&”符号,这里返回的是studentBean
        StudentBean studentBean = (StudentBean)annotationConfigApplicationContext.getBean("&studentBean");
        studentBean.study();
    }
}
~~~



##### FactoryBean使用场景

1. 最为典型是则是创建Aop的代理对象，这个对象在Spring中就是ProxyFactoryBean

   ![image-20220330215722491](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202203302157931.png)

2. Mybatis中的SqlSessionFactoryBean

3. Hibernate中的SessionFactoryBean

4. Dubbo中的Consumer

---



FactoryBean与BeanFactory区别

1. 两者没有比较性，只是名称接近而已
2. BeanFactory是Ioc容器、负责生产和管理bean，所有的Bean都是由BeanFactory来进行管理的，给具体的Ioc容器的所需提供了规范
3. FactoryBean是一个bean，这个Bean不是简单的Bean，而是一个能生产或者修饰对象生成的工厂Bean。可以说是一个为Ioc容器中bean的实现提供了更加灵活的方式，FactoryBean在IOC容器的基础上给Bean的实现加上了一个简单工厂模式和装饰模式，我们可以在getObject()方法中灵活配置

---



### Spring IoC、DI

https://blog.csdn.net/a745233700/article/details/80959716

##### IoC

1. Inversion of control：控制反转，指对象的控制权转移给Spring框架，由Spring来负责控制对象的生命周期(比如：创建、实例化、属性赋值、初始化、销毁)和对象间的依赖关系
2. 以前通过new来自己把控创建对象，而Ioc是由专门的容器来帮忙创建对象，将所有类都在Spring容器中登记，当需要某个对象时，只需要告诉Spring容器，spring就会在系统运行到适当的时机把想要的对象主动给你。
3. Spring Ioc容器帮我们创建、查找、住入依赖对象，而引用对象只是被动的接受依赖对象，所以叫控制反转

##### DI

1. Ioc在程序运行时，动态的向某个对象提供它所需要的其他对象，这一点是通过DI实现的，即：应用程序在运行时依赖IoC容器来动态注入对象所需要的外部依赖。
2. DI具体就是通过反射实现注入的，反射运行程序在运行的时候动态生成对象、执行对象的方法、改变对象的属性等等

##### IoC原理

Spring的IoC实现原理为：工厂模式+反射机制

~~~java
interface Fruit {
   public abstract void eat();
 }
 
class Apple implements Fruit {
    public void eat(){
        System.out.println("Apple");
    }
}
 
class Orange implements Fruit {
    public void eat(){
        System.out.println("Orange");
    }
}
 //工厂
class Factory {
    public static Fruit getInstance(String ClassName) {
        Fruit f=null;
        try {
            //反射得到类实例
            f=(Fruit)Class.forName(ClassName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
 
class Client {
    public static void main(String[] a) {
        Fruit f=Factory.getInstance("io.github.dunwu.spring.Apple");
        if(f!=null){
            f.eat();
        }
    }
}
~~~





##### Spring Ioc容器整体脉络

Ioc在Spring里，只需要低级容器就可以实现，2个步骤：

 Spring 低级容器（BeanFactory）的 IoC：

1. 加载配置文件，解析成一个个的BeanDefiniton放在Map里面
2. 调用getBean时，从BeanDefinition所属的Map里面取出Class对象进行实例化，同时，如果有依赖关系，将递归调用getBean方式来完成依赖注入





1. Bean定义 BeanDefinition
   - 无论是注解驱动还是xml最终都会将这些配置，读取成一个个的Bean定义
2. 注册Bean定义 BeanDefinitionRegistry
   - 如何将Bean定义收集起来
   - 这个接口只是注册Bean定义，显然还需要一个组件将我们的xml或者配置类读取为Bean定义；
     不仅仅还有读取，我们通常还会配置扫描的包，只会扫描这个包下带有@Component这种注解的Bean才会最终被Spring管理
3. BeanDefinitionReader和ClassPathBeanDefinitionScanner
   - 这两个组件就是负责读取和扫描
4. 生产bean的大致流程
   - 实例化，可以通过反射（如xml配置的时候就是通过反射）或new（配置类中往往就是通过new）
   - 填充属性，比如通过@Autowired； @Value等注解注入的属性
   - 如果配置了init方法则去调用配置的初始化方法
   - 如果配置了销毁方法，当Bean销毁的时候就会调用对应的销毁方法
   - 最终会将这个Bean放到一个Map中（单例池）,一级缓存



---

### Spring 中bean的生命周期

https://blog.csdn.net/knknknkn8023/article/details/107130806/

#### 普通Java对象生命周期

1. 实例化对象
2. 对象不使用时，垃圾回收机制进行回收

#### Spring bean的生命周期

Spring只给我们管理单例作用域Bean的生命周期，在此作用域下，Spring 能够精确地知道该 Bean 何时被创建，何时初始化完成，以及何时被销毁。

对于Prototype作用域的Bean，Spring只负责创建，当容器创建了 Bean 的实例后，Bean 的实例就交给客户端代码管理，Spring 容器将不再跟踪其生命周期。每次客户端请求 prototype 作用域的 Bean 时，Spring 容器都会创建一个新的实例，并且不会管那些被配置成 prototype 作用域的 Bean 的生命周期



了解生命周期的意义：利用Bean在其存活期间的指定时刻，完成一些相关操作。一般情况下，会在Bean被初始化后和被销毁执行前执行一些相关操作



主要包含5个主要阶段，其他都是在这四个主要阶段前后的扩展点，5个主要阶段是：

其中实例化和属性赋值分别对应构造方法和setter方法的注入，初始化和销毁是用户能自定义扩展的两个阶段

1. 实例化bean
   - 容器寻找Bean的定义信息并将其实例化(构造方法、工厂方法等)
2. 属性赋值
   - 利用依赖注入，Spring按照Bean的定义信息配置Bean所有属性(setter等)
3. 初始化
4. 使用
5. 容器关闭时 销毁

![Spring 生命周期流程](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/1F32KG1-0.png)





![image-20221031234705679](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202210312347899.png)

##### Bean 生命周期的整个执行过程描述如下。

1. Spring 启动，查找并加载需要被 Spring 管理的 Bean，对 Bean 进行实例化。
2. 对 Bean 进行属性注入。
3. 如果 Bean 实现了 BeanNameAware 接口，则 Spring 调用 Bean 的 setBeanName() 方法，传入当前 Bean 的 id 值。
4. 如果 Bean 实现了 BeanFactoryAware 接口，则 Spring 调用 setBeanFactory() 方法传入当前工厂实例的引用。
5. 如果 Bean 实现了 ApplicationContextAware 接口，则 Spring 调用 setApplicationContext() 方法传入当前 ApplicationContext 实例的引用。  （在实际开发中，我们可能会遇到一些类，需要获取到容器的详细信息）
6. 如果 Bean 实现了 BeanPostProcessor 接口，则 Spring 调用该接口的预初始化方法 postProcessBeforeInitialzation() 对 Bean 进行加工操作(在对象创建后，对对象进行修改操作)。此处非常重要，Spring 的 AOP 就是利用它实现的。
   - 在before或者after方法里面对对象进行判断，看是否需要进行代理，需要则生成代理对象并把代理对象放入容器中
   -  SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference：解决循环依赖，获取一个拓展好的半成品的对象。解决循环依赖用三级工厂的原因，就是需要遇到注入AOP Bean的时候，通过这个地方解决代理
   - ![image-20220401171420361](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/image-20220401171420361.png)
7. 如果 Bean 实现了 InitializingBean 接口，则 Spring 将调用 afterPropertiesSet() 方法。
8. 如果在配置文件中通过 init-method 属性指定了初始化方法，则调用该初始化方法。
9. 如果 BeanPostProcessor 和 Bean 关联，则 Spring 将调用该接口的初始化方法 postProcessAfterInitialization()。此时，Bean 已经可以被应用系统使用了。
10. 如果在 <bean> 中指定了该 Bean 的作用域为 singleton，则将该 Bean 放入 Spring IoC 的缓存池中，触发 Spring 对该 Bean 的生命周期管理；如果在 <bean> 中指定了该 Bean 的作用域为 prototype，则将该 Bean 交给调用者，调用者管理该 Bean 的生命周期，Spring 不再管理该 Bean。
11. 如果 Bean 实现了 DisposableBean 接口，则 Spring 会调用 destory() 方法销毁 Bean；如果在配置文件中通过 destory-method 属性指定了 Bean 的销毁方法，则 Spring 将调用该方法对 Bean 进行销毁。





如何掌握？顶层思维！！将这些方法分类

1. Bean自身的方法
   - 这个包括了Bean本身调用的方法和通过配置文件中`<bean>`的init-method和destroy-method指定的方法
2. Bean级生命周期接口方法
   -  这个包括了BeanNameAware、BeanFactoryAware、ApplicationContextAware；当然也包括InitializingBean和DiposableBean这些接口的方法（可以被@PostConstruct和@PreDestroy注解替代)
3. 容器级生命周期接口方法
   - 一般称它们的实现类为：后处理器
   - 包括了：InstantiationAwareBeanPostProcessor 、BeanPostProcessor 这两个接口实现
4. 工厂后置处理器接口方法
   - 工厂后置处理器属于容器级的
   - 在应用上下文装配配置文件之后立即调用















---



### Spring AOP

Spring框架中，通过定义切面、拦截切点来实现了不同业务模块的解耦，这就是面向切面编程

1. 面向切面编程的思想里面，把功能分为核心业务功能和周边功能，作为面向对象的一种补充
2. 核心功能：登录、增删改查都叫核心业务功能
3. 周边功能：性能统计、日志、事务管理等
4. 周边功能在Spring AOP思想里面，被定义为**切面**
5. 把切面功能和核心业务功能"编织"在一起==》AOP

#### AOP中的概念

Spring Aop是方法级别的Aop框架

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/2020120700443256.png)

1. 切入点Pointcut（从这里进行拦截，然后进行增强）
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

![image-20221031222249945](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/202210312223524.png)

##### spring aop 常用注解

1. 那几个通知 before、after 、around、 
2. spring4和spring5对这几个注解有啥区别
3. spring4 正常情况下：环绕之前、**before**、环绕之后、**after**、*afterreturn*ing
4. spring4异常：环绕之前、**before**、**after**、**afterThrowing**
5. spring5正常：环绕之前、before、afterRetruing、after、环绕之后
6. spring5异常：环绕之前、before、afterThrowing、after

---

#### Spring框架如何实现AOP

Spring将Aop思想引入框架之后，通过预编译方式和运行期间动态代理，实现程序的同一维护



AOP代理主要分为静态代理和动态代理。静态代理的代表为AspectJ，动态代理则以Spring AOP为代表

##### AspectJ

aspectJ是一个Java实现的Aop框架，能够对Java代码进行Aop编译（一般在编译期间进行），让Java代码具有AspectJ的Aop功能(需要特殊的编译器)



是静态代理，也称为编译时增强，AOP框架会在编译阶段生成AOP代理类，并将AspectJ（切面）织入到Java字节码中，运行的时候就是增强之后的Aop对象

AspectJ可以单独使用，也可以整合其他框架。单独使用时使用专门的编译器ajc   

##### Spring AOP

SpringAop需要依赖Ioc容器来管理，并且只能作用于Spring容器

Spring整合了AspectJ使得可以使用aspectj语法来实现Aop

动态代理就是说Aop框架不会去修改字节码，而是每次运行时在内存中临时为方法生成一个Aop对象，这个Aop对象包含了目标对象的全部方法，并且在特定的切点做了增强处理，并回调原对象的方法。Spring Aop中的动态代理主要有两种方式：JDK动态代理，Cglib动态代理

**动态代理**

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
   - 在spring中默认情况下使用JDK动态代理实现AOP，如果proxy-target-class设置为true或者 使用了优化策略那么会使用CGLIB来创建动态代理.Spring AOP在这两种方式的实现上基本 一样．以JDK代理为例，会使用JdkDynamicAopProxy来创建代理，在invoke()方法首先需 要织入到当前类的增强器封装到拦截器链中，然后递归的调用这些拦截器完成功能的织入，最 终返回代理对象。

静态代理与动态代理区别：在于生成Aop代理对象的时机不同，相对来说AspectJ的静态代理方式具有更好的性能，但是需要特定的编译器进行处理，而Spring Aop则无需特定的编译器处理

##### AspectJ 与SpringAop区别

1. AspectJ属于静态织入，通过修改字节码实现，有几个织入的时机
   - 编译期织入：例如类A使用AspectJ添加了一个属性，类B引用了它，这个场景就需要编译期的时候就进行织入，否则无法编译B
   - 编译后织入：也就是生成了.class文件，这种情况需要增强处理的化就要用到编译后织入
   - 类加载后织入：指的是在类加载的时候进行织入。常见几种方法：自定义类加载器，在被织入类加载到JVM前对它进行加载；jvm启动的时候知道AspectJ提供的agent：-javaagent:xxx/xxx/aspectjweaver.jar
2. 可以做SpringAop做不了的事情，它是Aop编程的完全解决方案
3. SpringAop在容器启动的时候需要生成代理实例，在方法调用上也会增加栈的深度，使得SpringAop性能远不如AspectJ那么好
4. ![image-20220401135239914](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/spring_img/image-20220401135239914.png)



#### 开发中怎么用的AOP

1. 防止重复提交
   - 自定义注解+切面的环绕通知实现+分布式锁 redis  实现















---

### Spring框架中的Bean是线程安全的吗？

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

### Spring的三级缓存  

循环依赖下面给出解释

##### spring循环依赖

https://blog.csdn.net/a745233700/article/details/110914620

[三级缓存](https://cloud.tencent.com/developer/article/1497692)

1. 什么是循环依赖

   1. ~~~java
      //多个Bean之间相互依赖，形成一个闭环
      //案例：构造器注入，产生循环依赖
      @Service
      public class A {
          public A(B b) {
          }
      }
      @Service
      public class B {
          public B(A a) {
          }
      }
      
      //************field属性注入，不会产生循环依赖，spring已经帮我们解决了**************************//
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
      
      //************setter方式注入，原理和字段注入类似，不会产生循环依赖*************************//
      
      @Service
      class A {
          private B b;
          
          @Autowired
          public void setB(B b){
              this.b =b;
          }
      }
      @Service
      class B {
          
          private A a;
          @Autowired
          public void setA(A a){
              this.a =a;
          }
      }
      
      //获取Bean时，会报错
      //报错BeanCurrentlyInCreationException 循环依赖问题
      /**
      *A/B两个对象在三级缓存里面迁移过程
      */
      //1. A创建过程需要B,于时A(仅仅完成了实例化)将自己放到三级缓存里面，调用populateBean方法，注入依赖的对象B,于是去获取B,三级缓存中没发现B,于是去实例化B
      //2. B实例化的时候发现需要A,于时B先去查询一级缓存没有，再去查询二级缓存，还是没有，查询三级缓存，找到A。把三级缓存里面的A放到二级缓存，并删除三级缓存里面的A
      //3.B完成初始化工作，将自己放到一级缓存（此时B里面的A还处于创建中状态），然后回来创建A,此时B已经创建结束，直接从一级缓存里面获取B,然后完成创建，并将自己放到一级缓存里面
      ~~~

2. 两种注入方式对循环依赖的影响

   - 构造方法依赖注入可能造成循环依赖
   - setter方式注入依赖且在多例模式下产生循环依赖。每一次getBean()时，都会产生一个新的Bean，如此反复下去就会有无穷无尽的Bean产生了，最终就会导致OOM问题的出现

   

   - setter方法进行依赖注入且是在单例模式下产生的循环依赖问题（spring内部解决了这个问题，通过三级缓存）
   - 结论：ABC循环依赖问题 只要注入方式是setter且是Singleton就不会有循环依赖问题

3. **spring内部解决方式**：Spring解决单例模式下的setter方法依赖注入引起的循环依赖通过三级缓存解决

   - spring三级缓存DefaultSingletonBeanRegistry，也就是三个Map

   - ~~~java
     public class RDefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
     	...
     	// 从上至下 分表代表这“三级缓存”
     	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256); //一级缓存
     	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16); // 二级缓存
     	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16); // 三级缓存
     	...
     	
     	/** Names of beans that are currently in creation. */
     	// 这个缓存也十分重要：它表示bean创建过程中都会在里面呆着~
     	// 它在Bean开始创建时放值，创建完成时会将其移出~
     	private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
     
     	/** Names of beans that have already been created at least once. */
     	// 当这个Bean被创建完成后，会标记为这个 注意：这里是set集合 不会重复
     	// 至少被创建了一次的  都会放进这里~~~~
     	private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
     }
     ~~~

   - 

   - 一级缓存：singletonObjects=new ConcurrentHashMap  
     1. （单例池）存放已经经历了完整生命周期的Bean对象，从该缓存取出的bean可以直接使用
     
   - 二级缓存HashMap earlySingletonObjects
     1. 二级缓存：存放早期暴露出来的Bean对象，Bean的生命周期未结束（属性还未填充完成）=》半成品Bean
     1. 用于解决循环依赖
     
   - 三级缓存 HashMap  singletonFactories

     1. 三级缓存：单例对象工厂的cache，存放bean工厂对象 》准备构建Bean的
     2. 用于解决循环依赖

   

4. 结论：只有单例的Bean，会通过三级缓存提前暴露来解决循环依赖的问题。并非单例的Bean，每次从容器中获取的都是一个新的对象，都会重新创建，所以，非单例的Bean是没有缓存的，不会将其放到三级缓存中

5. 解决的核心原理就是：在对象实例化之后，依赖注入之前，Spring提前暴露的Bean实例的引用在第三级缓存中进行存储

##### 三级缓存流程总结

 以上面`A`、`B`类使用属性`field`注入循环依赖的例子为例，对整个流程做文字步骤总结如下：

1. 使用`context.getBean(A.class)`，旨在获取容器内的单例A(若A不存在，就会走A这个Bean的创建流程)，显然初次获取A是不存在的，因此走**A的创建之路~**
2. `实例化`A（注意此处仅仅是实例化），并将它放进`缓存`（此时A已经实例化完成，已经可以被引用了）
3. 初始化`A：`@Autowired`依赖注入B（此时需要去容器内获取B）`
4. `为了完成依赖注入B，会通过`getBean(B)`去容器内找B。但此时B在容器内不存在，就走向**B的创建之路~**`
5. 实例化`B，并将其放入缓存。（此时B也能够被引用了）`
6. `初始化`B，`@Autowired`依赖注入A（此时需要去容器内获取A）
7. `此处重要`：初始化B时会调用`getBean(A)`去容器内找到A，上面我们已经说过了此时候因为A已经实例化完成了并且放进了缓存里，所以这个时候去看缓存里是已经存在A的引用了的，所以`getBean(A)`能够正常返回
8. **B初始化成功**（此时已经注入A成功了，已成功持有A的引用了），return（注意此处return相当于是返回最上面的`getBean(B)`这句代码，回到了初始化A的流程中~）。
9. 因为B实例已经成功返回了，因此最终**A也初始化成功**
10. 到此，B持有的已经是初始化完成的A，A持有的也是初始化完成的B，完美



##### 为什么要用三级缓存？两级、一级行不行？

如果仅仅是解决循环依赖问题，使用二级缓存就可以了。但是如果对象实现了AOP，那么给属性注入到其他bean的时候，并不是最终的代理对象，而是原始的。这时就需要通过三级缓存的ObjectFactory才能提前产生最终的需要代理的对象，调用这个对象的getObject方法返回一个封装后的对象，而对象既可以是原始对象也可以是代理对象，再将这个对象返回给需要注入的类

##### 解决构造函数相互注入造成的循环依赖：

1. 前面说Spring可以自动解决单例模式下通过setter()方法进行依赖注入产生的循环依赖问题。而对于通过构造方法进行依赖注入时产生的循环依赖问题没办法自动解决，那针对这种情况，我们可以使用@Lazy注解来解决。
2. 也就是说，对于类A和类B都是通过构造器注入的情况，可以在A或者B的构造函数的形参上加个@Lazy注解实现延迟加载。@Lazy实现原理是，当实例化对象时，如果发现参数或者属性有@Lazy注解修饰，那么就不直接创建所依赖的对象了，而是使用动态代理创建一个代理类。
3. 比如，类A的创建：A a=new A(B)，需要依赖对象B，发现构造函数的形参上有@Lazy注解，那么就不直接创建B了，而是使用动态代理创建了一个代理类B1，此时A跟B就不是相互依赖了，变成了A依赖一个代理类B1，B依赖A。但因为在注入依赖时，类A并没有完全的初始化完，实际上注入的是一个代理对象，只有当他首次被使用的时候才会被完全的初始化。

---





### Bean装配

bean装配是指：在Spring容器中把bean组装到一起，前提是容器需要知道bean的依赖关系，**如何通过依赖注入来把它们装配在一起**

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
   - 使用Autowired时，首先在容器中查询对应类型的bean，如果查询结果刚好有一个，就将该bean装配给@Autowired指定的数据，如果查询的结果不止一个，则会报错
   - 上述解决方法：Autowired+Qualifier
   - 如果上述查找的结果为空，会抛出异常。解决方法：required=false
2. @Qualifier：按照类型注入的基础上，再按照名称注入
2. @Resource

   - 默认按照名称来装配注入，只有当找不到与名称匹配的bean才会按照类型来装配注入



---



### Spring事务的实现方式和实现原理

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



## @Transactional

1. transactional是spring声明式事务管理的注解配置方式，底层通过AOP的方式实现。本质是对方法的前后进行拦截，然后在目标方法开始之前创建或加入一个事务，执行完目标方法之后根据执行情况提交或回滚事务

2. 通过该注解就能让spring为我们管理事务，免去了重复的事务管理逻辑，减少对业务代码的侵入，使得开发人员能够专注于业务层面开发

3. ![image-20220315212611513](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203152126724.png)

   

4. @Transactional(rollbackFor=Exception.class)：如果方法抛出异常，就会回滚，数据库里面的数据也会回滚。如果不配置rollbaceFor属性，则事务只会在遇到运行时异常RuntimeException才会回滚

   



#### Transactional失效场景

结合：Spring事务的传播机制及原因分析

https://www.jianshu.com/p/befc2d73e487 ：事务失效例子比较全面

1. Transactional注解标注在非public方法上时
   - 失效原因：因为@Transactional是基于AOP动态代理实现的，在bean初始化过程中，对含有@Transactional注解的实例创建代理对象，这里存在一个spring扫描该注解信息的过程，但是该注解标注在了非public方法上，那么就默认方法的@Transactional注解信息为空，便不会对bean进行代理对象创建
2. 在一个类中A方法上标注注解@Transactional，B方法未标注该注解，B方法中调用A方法，导致A方法上的事务注解失效。但是A方法调用B的事务是会生效的。。
   - spring默认的传播机制：Propagation_Required，即：支持当前事务，如果当前没有事务，就新建一个事务。
   - 因为B方法没有该注解，所以线程内的connection属性autocommit=true，那么传播给A方法的也为true，执行完自动提交，即使A方法标注了该注解，也会失效。
   - B方法调用A方法时，是之间通过this对象来调用方法，绕过了代理对象，也即没有代理逻辑了
3. 一个类中A方法和B方法都标注了@Transactional注解，A调用B，会导致B方法的事务失效
4. rollbackFor 可以指定能够触发事务回滚的异常类型。**Spring默认抛出了未检查unchecked异常（继承自 RuntimeException 的异常）或者 Error才回滚事务**；其他异常（uncheck异常）不会触发回滚事务。如果在事务中抛出其他类型的异常，但却期望 Spring 能够回滚事务，就需要指定rollbackFor属性
5. 事务方法内部手动捕捉了异常，没有抛出新的异常，导致事务操作不会进行回滚
6. 多线程与Transaction
   - 如果在一个被@Transactional修饰的方法内启用多线程，那么该方法的事务与线程内的事务是两个完全不相关的事务
   - 也就是说在@Transactional注解的方法会产生一个新的线程的情况下，事务是不会从调用者线程传播到新建线程的
   - spring数据库连接信息都放在了ThreadLocal中，所以不同的线程享有不同的连接信息，所以不存在于同一个事务中



---

## Spring、SpringMvc常用注解

#### 组件类

@Componet、Controller、Service、Repository

#### 装配Bean时使用的注解

@Autowired、@Qualifier

@Resource

@PostConstruct、@PreDestory



@Primary该注解标注的bean指示了优先注入的bean

~~~java
...
@Autowired
private MyBean myBean(); // 注入myBean1

@Primary
@Bean
public MyBean myBean1() {
    return new MyBean();
}

@Bean
public MyBean myBean2() {
    return new MyBean();
}
...

~~~



@Lazy

Spring IoC容器(ApplicationContext)会在启动的时候实例化所有单例Bean，用于指定Bean是否取消预初始化



#### Componet+Bean

在@Component类中使用方法或字段时不会使用CGLIB增强(即不使用代理类：调用任何方法，使用任何变量，拿到的是原始对象

在@Component中调用@Bean注解的方法和字段则是普通的Java语义，不经过CGLIB处理

#### Configuration+Bean

在Configuration类中使用方法或字段时，使用Cglib代理对象。当调用@Bean注解的方法时它不是普通的Java语义，而是从容器中拿到由Spring生命周期管理、被Spring代理的Bean的代理对象引用

被@Configuration修饰的类，spring容器中会通过cglib给这个类创建一个代理，代理会拦截所有被@Bean修饰的方法，默认情况（bean为单例）下确保这些方法只被调用一次，从而确保这些bean是同一个bean，即单例的



@Import

用于导入普通类的注入、@Configuration注解的配置类、声明@Bean注解的bean方法、导入ImportSelector的实现类或导入ImportBeanDefinitionRegistrar的实现类



---

## Spring启动流程

spring的启动入口有很多，在xml中有xml的方式，在注解中有注解的方式，在web的方式中也有web的注解启动方式。

如果是xml则入口是ClassPathXmlApplicationContext，注解方式启动则入口是AnnotationConfigApplicationContext，它们俩的共同特征便是都继承了 AbstractApplicationContext 类，而大名鼎鼎的 refresh()便是在这个类中定义的

若是以注解的配置类的方式启动，就是传入一个配置类，这个配置类包含了你需要注册的到容器中的bean的一些信息，比如扫描类路径信息，但是这个启动入口类是不支持容器的重复刷新的，也就是refresh只能调用一次，而使用AnnotationWebConfigApplicationContext这个是支持容器的重复刷新的，以AnnotationConfigApplicationContext来讲解下spring的启动过程

大致为三个步骤：（基于java-config技术分析）

1. 初始化Spring容器，注册内置的BeanPostProcessor的BeanDefinition到容器中
2. 将配置类的BeanDefinition注册到容器中
3. 调用refresh()方法刷新容器



注册器A  发送邮件B  发送消费券C

聋子看到火车开灯就跳舞



事件：事件源发起的某个动作，所以事件中理所当然的包含了事件源本身，以便知晓是哪个事件源发起的事件。开灯

事件源（事件广播器）：包含监听器集合，以便触发事件时，通知监听器。火车

事件监听器：事件源触发某个事件时，监听器对该事件有兴趣，就能够做出相应的相应，这个响应就是具体的业务逻辑。聋子






























