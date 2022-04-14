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

### 线程状态(生命周期)

![image-20220412212247848](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204122122015.png)



1. 新建(初始态)New：实现Runnable接口和继承Thread可以得到一个线程类，new一个实例出来，线程就进入了初始状态
2. 可运行态(就绪态)Runnable：线程创建后，调用start()方法，此时线程处于可运行线程池中，等待被线程调度选中，获取cpu是使用权
3. 运行态Running：线程获得了cpu时间片，执行任务
4. 阻塞态Blocked：线程由于某种原因放弃了cpu的使用权，直到线程进入可运行态，才有机会再次获得cpu时间片进入到运行态
5. 死亡态Dead：线程run()、main()方法执行结束，或者异常退出了，则该线程结束生命周期

---



### 线程池

高并发情况下，频繁的创建线程会大大降低系统的效率，因为频繁创建和销毁线程需要时间，所以引入了线程池技术

假设一个服务器完成一项任务所需时间为：创建线程时间T1 ，在线程中执行任务的时间T2 ， 销毁线程时间T3。如果：T1 + T3 远大于 T2，则可以采用线程池，以提高服务器性能

线程池：即一个存放线程的容器，调用线程池去执行并发任务时，从线程池中取出线程去执行任务，每个线程执行完任务后，并不被销毁，而是被线程池回收，下一次继续执行任务

#### 线程池基本组成部分

1. 线程池管理器ThreadPool：用于创建并管理线程池，包括创建线程池、销毁线程池、添加新任务等等
2. 工作线程PoolWorker：线程池中的线程，在没有任务时，处于等待状态，可以循环的执行任务
3. 任务接口Task：每个任务必须实现的接口，以供工作线程调度任务的执行
4. 任务队列taskQueue：存放没有处理的任务。提供一种缓冲机制

#### 线程池的实现原理

当向线程池中提交一个任务后，线程池是如何处理这个任务的呢？线程池处理流程如下：

1. 线程池判断 当前工作线程数是否小于核心线程数
   - 如果是则创建一个工作线程来执行任务
   - 如果不小于，则转到第二步
2. 线程池判断任务队列是否已经满了
   - 没有满则将这个任务存储在工作队列里面
   - 满了则进入第三步
3. 线程池判断当前工作线程数是否小于最大线程数
   - 没有小于则创建一个线程来执行这个任务
   - 如果最大线程池里面线程数量已经满了则交给饱和策略来处理这个任务



#### 工具类

##### Executors

1. newFixedThreadPool：一池固定线程数，底层ThreadPoolExcutor，数据结构采用LinkedBlockingQueue<Runnable>
2. newSingleThreadPool：一池一线程，底层为LinkedBlockingQueue<Runnable>
3. newCacheThreadPool：一池多线程，底层为SynchronusQueue<Runnable>



#### 核心

##### Executor中的ThreadPoolExecutor

构造器四大组件：ThreadPoolExecutor(corePool，maxImumPool，BlockingQueue，RejectExceptiionHandler)

构造器七大参数：

1. corePoolSize：核心线程数量
2. MaximumPoolSize：能容纳最大线程数
3. keepAliveTime：多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程被终止
4. TimeUnit：keepAliveTime的单位
5. BlockingQueue：暂存任务的工作队列
   - 一般采用以下几种阻塞队列
   - ArrayBlockingQueue
   - LinkedBlockingQueue
   - SynchronousQueue
6. ThreadFactory：用于创建线程的工厂
7. RejectExceptionHandler：拒绝处理任务时的策略，当工作队列已满时，并且线程池创建的线程数量达到了设置的最大线程数时，触发拒绝策略
   - AbortPolicy：默认策略，该策略会直接抛出异常，组织系统正常工作
   - CallerRunsPolicy：该策略会把任务队列中的任务放在调用者线程中运行
   - DiscardOledestPolice：该策略会丢弃任务队列中最老的一个任务
   - DiscardPolice：该策略会丢弃无法处理的任务，不予任务处理
   - 可以自定义拒绝策略：实现RejectedExecutionHandler 

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

Java内存模型的抽象结构图：

![image-20220411231453088](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204112314267.png)

Java内存模型规定了：线程对变量的所以操作都必须在本地内存进行，不能直接读写主内存的变量。

JMM定义了8种操作来完成变量如何从主内存到本地内存，以及变量如何从本地内存到主内存

分别是：read、load、use、assign、store、write、lock、unlock

![image-20220412204327444](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204122043152.png)



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

##### volatitle原理

JMM为了实现volatitle的有序性和可见性，定义了4种内存屏障(规范)，分别是LoadLoad / LoadStore / StoreLoad /StoreStore

就是在volatitle前后加上内存屏障，使得编译器和cpu无法进行重排序，并且写volatitile变量对其他线程可见

##### MESI

MEMI是cpu缓存一致性协议，不同cpu架构都不一样



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

​	



为什么CAS而不用sync？
CAS保证一致性，并提高并发性
	

---

### Java中的锁

#### Synchronized关键字

---



#### Lock接口

Lock接口的实现基本上都是通过聚合了一个AbstractQueuedSynchronizer同步器的子类来完成线程访问控制的

##### 队列同步器AQS

###### https://blog.csdn.net/mulinsen77/article/details/84583716

https://www.cnblogs.com/waterystone/p/4920797.html



AbstractQueueSynchronizer  同步器，是用来构建锁或者其他同步组件的基础框架，它使用了一个int的成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。

实现同步器主要通过继承来实现，子类通过继承同步器并实现它的抽象方法来管理同步状态

同步器是实现锁的关键，锁是面向使用者的，同步器是面向锁的实现者、它简化了锁的实现方式

同步器基于模板方法模式



###### AQS核心思想

如果请求的共享资源空闲，则将当前请求资源的线程设置为有效的线程，并将共享资源设置为锁定状态，如果请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是采用CLH队列锁实现的，即：将暂时获取不到锁的线程构造成一个节点加入队列中

CLH：Craig Landin and Hagersten ，CLH队列（是FIFO）是一个虚拟的双向队列，虚拟的双向队列：即不存在队列实例，仅存在节点之间的关联关系

AQS是将每一条请求共享资源的线程封装成一个CLH锁队列的一个结点（Node），来实现锁的分配，每个节点保存线程的信息、等待状态、前驱后继节点等信息



直白：AQS是基于CLH队列，用volatile修饰共享变量state，线程通过CAS去改变状态，成功则获取锁成功，失败则进入队列等待，等待被唤醒

AQS是自旋锁，在等待被唤醒的时候，经常会使用自旋( while(!cas() )的方式，不停的尝试获取锁，直到被其他线程获取成功



实现了AQS的锁有：自旋锁、互斥锁、读写锁、信号量、栅栏等等都是AQS的衍生物

AQS的具体实现：

![image-20220414211911107](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204142119244.png)

AQS维护了一个volatile的int类型的state，和一个FIFO线程等待队列，多线程争用资源被阻塞的时候就会进入到这个队列

AQS定义了两种资源共享方式：

1. 独占：只有一个线程能执行，例如ReentrantLock
2. 共享：多个线程可以同时执行，例如Semaphore、CountDownLatch、ReadWriteLock、CyclicBarrier
3. 不同的自定义的同步器争用资源的方式也不同



##### AQS底层使用了模板方法设计模式

同步器的设计基于模板方法设计模式的，如果需要自定义同步器一般是：

1. 使用者继承AbstractQueuedSynchronizer并重写指定的方法
2. 将AQS组合在自定义的同步组件的实现中，并调用模板方法，而这些模板方法会调用使用者重写的方法







#### Condition接口

condition接口提供了类似Object的监视器方法，与Lock接口配合可以实现等待/通知模式



### Java并发容器和框架

#### ConcurrentHashMap

线程安全的HashMap

##### 实现原理

concurrentHashMap采用锁分段技术，首先将数据分为一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一段数据的时候，其他段的数据能被其他线程访问

##### concurrentHashMap结构

![image-20220414225538803](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204142255782.png)

由Segment数组结构和HashEntry数组构成

Segment是一个可重入锁，在concurrentHashMap里面扮演锁的角色，HashEntry则用于蹭饭键值对数据

一个ConcurrentHashMap中包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表结构。一个Segment里面包含一个HashEntry数组，每个HashEntry是一个链表的元素。每个Segment拥有一个锁，当对HashEntry数组的数据进行修改时，必须先获得对应的Segment锁


























