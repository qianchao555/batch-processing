## JUC

### 创建线程的方式

4种类

#### 继承Thread

#### 实现Runnable接口

#### 实现Callable接口

1. 又返回值
2. 返回值封装在FutureTask中，采用get()阻塞获取返回值

#### 采用线程池

---



### 线程池

#### 工具类

##### Executors

1. newFixedThreadPool：一池固定线程数，底层ThreadPoolExcutor，数据结构采用LinkedBlockingQueue<Runnable>
2. newSingleThreadPool：一池一线程，底层为LinkedBlockingQueue<Runnable>
3. newCacheThreadPool：一池多线程，底层为SynchronusQueue<Runnable>



#### 核心

##### Executor中的ThreadPoolExecutor

构造器四大组件：ThreadPoolExecutor(corePool，maxImumPool，BlockingQueue，RejectExceptiionHandler)

构造器七大参数：

corePoolSize：核心线程数量

MaximumPoolSize：能容纳最大线程数

keepAliveTime：多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程被终止

TimeUnit：keepAliveTime的单位

BlockingQueue：暂存任务的工作队列

ThreadFactory：用于创建线程的工厂

RejectExceptionHandler：拒绝策略，当工作队列已满时，并且线程池创建的线程数量达到了设置的最大线程数时，触发拒绝策略

1. AbortPolicy：默认策略，该策略会直接抛出异常，组织系统正常工作
2. CallerRunsPolicy：该策略会把任务队列中的任务放在调用者线程中运行
3. DiscardOledestPolice：该策略会丢弃任务队列中最老的一个任务
4. DiscardPolice：该策略会丢弃无法处理的任务，不予任务处理
5. 可以自定义拒绝策略：实现RejectedExecutionHandler 

![image-20220411213851879](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204112139305.png)



如果任务量不大，可以采用无界队列，如果任务量非常大，需要采用有界队列，防止OOM

如果任务量大，还要求任务都处理成功，则要对提交的任务进行阻塞提交，并且重写拒绝策略，改为阻塞提交。保证不抛弃任何一个任务



#### 如何合理设置线程池容量大小

如何设置：首先根据主机cpu来判断，在考虑具体场景

##### CPU密集型

此时需要大量的计算，在消耗cpu，例如：while操作

此时需要的线程池大小为：cpu核心数+1 即可

##### IO密集型

证明此时需要大量的阻塞，所以需要的线程数量更多

线程池大小一般为：cpu核心数*2 或cpu核心数量/1-阻塞系数(0.8~0.9)

---

### 多线程中i++等操作时线程安全问题

#### Java内存模型-JMM

Java内存模型：Java Memory Model

为什么要有内存模型：Java为了屏蔽硬件和操作系统访问内存的各种差异，提出了Java内存模型的规范，保证了Java程序在各种平台下对内存的访问都能得到一致效果

Java虚拟机会实现这个规范

Java内存模型定义了Java线程对内存数据进行交互的规范，线程之间的共享变量存储在主内存中，每个线程有自己私有的本地内存，本地内存存储了该线程以读/写共享变量的副本。本地内存是Java内存模型抽象的概念，并不是真实存在的

https://www.cnblogs.com/Java3y/p/15468855.html#:~:text=Java%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B%E6%8A%BD%E8%B1%A1%E7%BB%93%E6%9E%84,%EF%BC%9A%E7%BA%BF%E7%A8%8B%E4%B9%8B%E9%97%B4%E7%9A%84%E3%80%8C%E5%85%B1%E4%BA%AB%E5%8F%98%E9%87%8F%E3%80%8D%E5%AD%98%E5%82%A8%E5%9C%A8%E3%80%8C%E4%B8%BB%E5%86%85%E5%AD%98%E3%80%8D%E4%B8%AD%EF%BC%8C%E6%AF%8F%E4%B8%AA%E7%BA%BF%E7%A8%8B%E9%83%BD%E6%9C%89%E8%87%AA%E5%B7%B1%E7%A7%81%E6%9C%89%E7%9A%84%E3%80%8C%E6%9C%AC%E5%9C%B0%E5%86%85%E5%AD%98%E3%80%8D%EF%BC%8C%E3%80%8C%E6%9C%AC%E5%9C%B0%E5%86%85%E5%AD%98%E3%80%8D%E5%AD%98%E5%82%A8%E4%BA%86%E8%AF%A5%E7%BA%BF%E7%A8%8B%E4%BB%A5%E8%AF%BB%2F%E5%86%99%E5%85%B1%E4%BA%AB%E5%8F%98%E9%87%8F%E7%9A%84%E5%89%AF%E6%9C%AC%E3%80%82

Java内存模型的抽象结构图：

![image-20220411231453088](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204112314267.png)

Java内存模型规定了：线程对变量的所以操作都必须在本地内存进行，不能直接读写主内存的变量



##### 	JMM特性

1. 可见性：某一线程修改主内存值时，对于其他线程都是可知的
2. 原子性：不可分割，不可被中断的一个或一系列操作
3. 有序性

通过这三个特性：线程安全获得保障

#### happen-before规则



#### Volatitle

轻量级同步机制

##### 特性

1. 保证可见性（与JMM可见性呼应）
2. 禁止指令重排序（与JMM有序性呼应，底层存在指令重排）
3. ​    1、2两点由内存屏障提供
4. 不保证原子性
   - i++、++i等不能保障原子性
   - 因为这些可以分解为：1、获取i，2、进行加操作，3、赋值
5. 如何解决不保证原子性
   - sync
   - 原子类

#### 原子类

##### Atomic

如何保证原子性

1. unsafe类

2. 类中变量value使用volatitle修饰，保证多线程内存间可见性

3. valueOffset：内存偏移地址，unsafe根据地址获取数据

   

原子类原理：CAS

#### CAS

比较并更新：当前值与主内存值进行比较
例子：

~~~java
AtomicInteger
i++:  getAndIncrement 
底层compareAndSet(Obj expect,Obj update)
底层unsafe.compareAndSwapInt(this,ValueOffset,Expect,Update)
	
    
    update：要修改为的值
	expect：主内存值
~~~

CAS底层思想：
CAS执行依赖于UnSafe类实现

1. UnSafe类是CAS核心类
2. 基于该类可以直接操作特定内存的数据

缺点

1. 循环时间长，开销大
2. 只能保证一个共享变量的原子操作
3. 产生ABA问题
   - 什么是ABA问题
   - ![image-20220411230210765](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204112302908.png)



如何解决上述问题？

原子引用更新

比较更新时，添加版本号--AtomicStampedReference

	



为什么CAS而不用sync？
CAS保证一致性，并提高并发性
	