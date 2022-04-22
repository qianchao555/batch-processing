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

即execute(Runnable r)流程

1. 线程池判断 当前工作线程数(worker)是否小于核心线程数
   - 如果是则创建一个工作线程来执行任务（Worker里面创建，由ThreadFactory创建线程）
   - 如果不小于，则转到第二步
2. 判断线程池是否处于运行状态，并且判断任务是否可以加入任务队列
   - 任务队列没有满则将这个任务存储在工作队列里面
   - 满了则进入第三步
3. 线程池判断当前工作线程数是否小于最大线程数
   - 小于则创建一个线程来执行队列里面的任务，此任务放入队列里面
   - 如果最大线程池里面线程数量已经满了则交给饱和策略来处理这个任务

#### 如何优雅关闭线程池

shuwdown: 不接收新任务, 中断空闲线程, 继续处理完线程池中的任务

shutdownNow: 不接收新任务, 中断运行线程, 空闲线程和处理完正在运行的任务的线程会退出

使用shutdownNow方法，可能会引起报错，使用shutdown方法可能会导致线程关闭不了。所以当我们使用shutdownNow方法关闭线程池时，一定要对任务里进行异常捕获。

当我们使用shuwdown方法关闭线程池时，一定要确保任务里不会有永久阻塞等待的逻辑，否则线程池就关闭不了

shutdown()/  shutdownNow()+awaitTermination()

#### 线程池队列满了，还有任务来怎么办？

https://blog.csdn.net/weixin_38336658/article/details/119907919?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-5.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-5.pc_relevant_default&utm_relevant_index=8

无非就是从队列入手，增加消费者、增加消费者的处理效率；限制生产者生成速度

1. 首选：通过自定义实现拒绝策略RejectedExecutionHandler

   - ~~~java
     //采用put方法，会阻塞生产者
     public class BlockWhenQueueFullHandler implements RejectedExecutionHandler {
     
         public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
             pool.getQueue().put(new FutureTask(r));
         }
     }
     ~~~

   - 用sleep达到阻塞的目的

   - 把线程池无法执行的任务信息持久化写入数据库去，后台专门启动一个线程，后续等待线程池的工作负载降低了，这个后台线程就可以慢慢的从磁盘里读取之前持久化的任务重新提交到线程池

   - 在拒绝策略里面添加一个延迟任务队列，延迟任务重新投向线程池

2. 修改默认拒绝策略为：CallerRunsPolicy该策略队列满时，会把任务放在调用者线程中运行



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

synchronized实现同步的基础：Java中每一个对象都可以作为锁。具体有三种形式

1. 对于实例同步方法，锁是当前实例对象。
2. 对于静态同步方法，锁是当前对象的Class对象。
   - 多个线程争夺是同一个Class对象锁
3. 对于同步方法块，锁是Synchonized括号里配置的对象

在JVM层面实现，具体实现原理为：

1. Jvm基于进入和退出Moniter对象来实现方法同步和代码块同步

##### Java对象头

synchronized用的锁存在Java对象头里面

##### 锁升级

Java1.6引入了偏向锁、轻量级锁

一个有4中锁状态：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态



#### Lock接口

Lock接口的实现基本上都是通过聚合了一个AbstractQueuedSynchronizer同步器的子类来完成线程访问控制的

##### 队列同步器AQS

###### https://blog.csdn.net/mulinsen77/article/details/84583716

https://www.cnblogs.com/waterystone/p/4920797.html



AbstractQueueSynchronizer  同步器，是用来构建锁或者其他同步组件的基础框架，它使用了一个int的成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。

实现同步器主要通过继承来实现，子类通过继承同步器并实现它的抽象方法来管理同步状态

同步器是实现锁的关键，锁是面向使用者的，同步器是面向锁的实现者、它简化了锁的实现方式

同步器基于模板方法模式，基于AQS可以实现自己的很多锁类，比如ReentrantLock等等



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



#### ReentrantLock

##### 可重入锁

简单理解：同一个线程对于已经获得的锁，可以继续申请到该锁的使用权

内部为sync extends AQS内部类实现，其次，它的公平锁与非公平锁都是在xxx extends sync内部类中实现的

如何实现可重入：volitale类型的int state变量  0表示没有任何线程持有该锁





#### Condition接口

condition接口提供了类似Object的监视器方法，与Lock接口配合可以实现等待/通知模式





---



### Java中的读写锁

假设你的程序中涉及到对一些共享资源的读和写操作，且写操作没有读操作那么频繁。在没有写操作的时候，两个线程同时读一个资源没有任何问题，所以应该允许多个线程能在同时读取共享资源。但是如果有一个线程想去写这些共享资源，就不应该再有其它线程对该资源进行读或写

也即是：读读共存，读写、写写不能共存

#### ReadWriteLock接口

##### 主要实现：ReentrantReadWriteLock

提供了以下特性：

1. 公平性的选择：支持公平与非公平（默认）的锁获取方式，吞吐量非公平优先于公平
2. 可重入：读线程获取读锁之后可以再次获取读锁，写线程获取写锁之后可以再次获取写锁、同时也可以获取读锁
3. 锁可降级：写线程获取写锁之后，其还可以再次获取读锁，然后释放写锁的次序，那么此时该线程是读锁状态，也就是降级操作



核心：基于AQS的同步器Sync，然后扩展出ReadLock、WriteLock所构成

主要包括：读写状态的设计、读锁的获取与释放、写锁的获取与释放、锁降级

##### 读写状态的设计

同步状态需要在一个int类型的state字段上维护多个多线程和一个写线程的状态，使得该状态的设计成为读写锁实现的关键

这个状态在抽象的AQS中，由具体同步器(如ReentrantReadWriteLock)的内部类Sync来维护，Sync继承自AQS

如何用一个int类型变量来维护多种状态？

实现：**按位切割使用**这个变量，将高16位表示读锁的个数，低16位表示写锁的个数

下图状态表示：一个线程已经获取到了写锁，且重入了两次，同时也连续获取了两次读锁

![image-20220418230634085](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204182306630.png)

读写锁如何确定各自的状态的？

通过位运算实现：假如当前同步状态为S，写状态通过 S & 0x0000FFFF(将高16位抹掉)，读状态等于S>>16(无符号补0右移16位)

当写状态+1时：就等于S+1

当读状态+1时：等于S+(1>>16)



##### 读锁的获取与释放

读锁是一个支持重入的共享锁

读锁的释放：

减少读状态值为1（1<<16)

##### 写锁的获取与释放

写锁是一个支持重入的排他锁。

如果当前线程已经获取写锁时，则增加写状态。

如果当前线程在获取写锁时，读锁已经被获取或者当前线程不是已经获取写锁的线程，则当前线程进入等待状态

写锁的释放：

与ReentrantLock的释放过程类似，每次释放均减少写状态

##### 锁降级

指的是写锁降级成读锁。写线程获取写锁之后，其还可以再次获取读锁，然后释放写锁的次序，那么此时该线程是读锁状态，也就是降级操作

ReentantReadWriteLock不支持锁升级。目的：保证数据的可见性，如果读锁被多个线程获取，其中的任意线程成功获取写锁后并更新了数据，那么其更新对其他获取到读锁的线程是不可见的

---



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

##### ConcurrentHashMap主要参数



segment数组：2的n 次方。原因：保证能够通过按位与的散列算法来定位到segments数组的索引。默认segment数组大小为16

segmentShifi：段偏移量

segmentMask：段掩码

HashEntry数组



##### 定位segment

因为ConcurrentHashMap使用分段锁Segment来保护不同段的数据，那么在插入和获取元素的时候，必须先通过hash算法定位到segment

concurrentHashMap是采用hash的变种算法，对元素的hashCode进行一次再散列。目的是减少hash冲突，使元素能均匀的分布在不同的segment上，从而提高容器的存取效率

~~~java
/**
*h为元素的hashcode值，然后再进行散列
*/
private static int hash (int h){
    
}
~~~



##### concurrentHashMap的get操作

1. 根据key进行进行一次再散列，然后使用这个散列值通过散列运算定位到segment

2. 再通过散列算法定位到元素

3. ~~~java
   public V get(Object key){
       int hash=hash(key.hashCode());
       return segmentFor(hash).get(key,hash);
   }
   //之前的JDK版本
   ~~~

4. ~~~java
   //会发现源码中没有一处加了锁
   public V get(Object key) {
       Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
       int h = spread(key.hashCode()); //计算hash
       if ((tab = table) != null && (n = tab.length) > 0 &&
           (e = tabAt(tab, (n - 1) & h)) != null) {//读取首节点的Node元素
           if ((eh = e.hash) == h) { //如果该节点就是首节点就返回
               if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                   return e.val;
           }
           //hash值为负值表示正在扩容，这个时候查的是ForwardingNode的find方法来定位到nextTable来
           //eh=-1，说明该节点是一个ForwardingNode，正在迁移，此时调用ForwardingNode的find方法去nextTable里找。
           //eh=-2，说明该节点是一个TreeBin，此时调用TreeBin的find方法遍历红黑树，由于红黑树有可能正在旋转变色，所以find里会有读写锁。
           //eh>=0，说明该节点下挂的是一个链表，直接遍历该链表即可。
           else if (eh < 0)
               return (p = e.find(h, key)) != null ? p.val : null;
           while ((e = e.next) != null) {//既不是首节点也不是ForwardingNode，那就往下遍历
               if (e.hash == h &&
                   ((ek = e.key) == key || (ek != null && key.equals(ek))))
                   return e.val;
           }
       }
       return null;
   }
   ~~~

   

整个get过程不需要加锁：因为所有的共享变量都定义为了volatile类型，Node中的val是用volatile修饰的

![image-20220418210204525](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204182102569.png)



##### concurrentHashMap的put操作

为了保证共享变量的线程安全，所有采用了加锁方式

1. put方法首先定位到segment，然后在segment里面进行插入操作
2. 插入时经过了两个步骤
   - 判断hashEntry数组是需要扩容：需要则创建一个容量是原来容量两倍的数组，然后将原数组里的元素进行再散列后插入到新数组里。为了高效ConcurrentHashMap不会对整个容器扩容，而是针对某个segment进行扩容
   - 定位添加元素的位置，然后将其插入到HashEntry数组中



---

#### ConcurrentLinkedQueue

非阻塞的线程安全的队列，采用循环的cas算法实现

它是一个基于链接节点的无界线程安全队列，采用先进先出的规则对节点进行排序，添加元素时，添加到队列的尾部，获取元素时，它会返回队列头部的元素

##### 数据结构

由head节点和tail节点组成，每个节点Node由节点元素和指向下一个节点的引用组成，节点与节点之间通过这个next关联起来，从而组成一张链表结构的队列

![image-20220418212146272](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204182121424.png)

默认情况下head节点存储的元素为空，tail节点等于head节点

~~~java
public ConcurrentLinkedQueue() {
      head = tail = new Node<E>(null);
}
~~~



##### 入队列

1. 通过cas算法入队列
2. 定位尾节点
3. 设置入队列节点为尾节点

##### 出队列

1. 从队列里返回一个节点元素，并清空该节点对元素的引用



---



#### Java中的阻塞队列

##### 什么是阻塞队列

阻塞队列：支持阻塞插入和阻塞移除(获取)方法

当队列满时，队列会阻塞插入元素的线程，直到队列不满

当队列空时，获取元素的线程会阻塞等待队列变为非空

##### 使用场景：生产者消费者场景



##### 阻塞队列的4种处理方式

![image-20220418213120686](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204182131837.png)

1. 抛出异常：是指当阻塞队列满时，再往队列插入元素，会抛出 IllegalStateException("Queue full") 异常。当队列为空时，从队列里获取元素时会抛出 NoSuchElementException 异常
2. 返回特殊值：插入方法会返回是否成功，成功则返回 true。移除方法，则是从队列里拿出一个元素，如果没有则返回 null
3. 一直阻塞：当阻塞队列满时，如果生产者线程往队列里 put 元素，队列会一直阻塞生产者线程，直到队列可用或者响应中断退出。当队列空时，消费者线程试图从队列里 take 元素，队列也会阻塞消费者线程，直到队列不为空
4. 超时退出：当阻塞队列满时，队列会阻塞生产者线程一段时间，如果超过一定的时间，生产者线程就会退出

##### 分类

**ArrayBlockingQueue** ：一个由数组结构组成的有界阻塞队列。

- 队列按照先进先出的原则对元素进行排序

- 默认不保证线程公平的访问队列

- 公平的阻塞队列采用可重入锁实现

- ~~~java
  public ArrayBlockingQueue(int capacity,boolean fair){
      if(capacity<=0){
          throw new IllegalArgumentException();
      }
      this.items=new Object(capacity);
      lock=new ReentrantLock(fair);
      notEmpty=lock.newCondition();
      notFull=lock.newCondition();
  }
  ~~~

  

**LinkedBlockingQueue** ：一个由链表结构组成的有界阻塞队列。

- 链表长度默认为Integer.Max_Value
- 按照先进先出的原则对元素进行排序

PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。

DelayQueue：一个使用优先级队列实现的无界阻塞队列。

**SynchronousQueue**：一个不存储元素的阻塞队列。

- 不存储元素的阻塞队列
- 每一个put操作必须等待一个take操作，否则不能继续添加元素
- 适用与传递性的场景

LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。

LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列



##### 实现原理

原理：Lock锁的多条件等待/通知模式实现

通知模式：当生产者往满的队列里添加元素时，会阻塞生产者，当消费者消费了队列里面的元素后，会通知生产者当前队列可用

例如ArryBlockingQueue采用了ReentantLock+Condition的等待/通知（await/signal）来实现



---

### Java中的并发工具类

前三者用于并发流量的控制、第四个为在线程间交换数据的一种手段

#### CountDownLatch

它允许一个或多个线程等待其他线程完成操作后，再执行。例如：应用程序的主线程希望在负责启动框架访问的线程已经启动所有服务框架后再启动

内部为Sync继承自AQS的内部类

~~~java
//构造器传入int类型参数，作为计数器使用，这个值是赋值给了AQS的state变量
//每调用一次countdown()后count会减一
public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

//await()方法会阻塞当前线程，直到count变为0
~~~

##### 经典用法

1. 某一线程开始运行前等待n个线程执行完毕
   - 将CountDownLatch的计数器初始化为new CountDownLatch(n) ，每当一个任务线程执行完毕，就将计数器减1 countdownlatch.countDown()，当计数器的值变为0时，在CountDownLatch上 await() 的线程就会被唤醒。
   - 一个典型应用场景就是启动一个服务时，主线程需要等待多个组件加载完毕，之后再继续执行
2. 实现多个线程开始执行任务的最大并行性
   - 注意是并行性，不是并发，强调的是多个线程在某一时刻同时开始执行。类似于赛跑，将多个线程放到起点，等待发令枪响，然后同时开跑。做法是初始化一个共享的CountDownLatch(1)，将其计数器初始化为1，多个线程在开始执行任务前首先 coundownlatch.await()，当主线程调用 countDown() 时，计数器变为0，多个线程同时被唤醒。

##### 实现原理

![image-20220419213953040](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204192139189.png)

上面的流程，如果落实到代码，把 state置为0的那个线程，会判断head指向节点的状态，如果为通知状态，则唤醒后续节点，即线程3节点，然后head指向线程3节点，head指向的旧节点会被删除掉。当线程3恢复执行后，发现自身为通知状态，又会把head指向线程4节点，然后删除自身节点，并唤醒线程4


将调用await()方法的线程，组装成Node，放在CLH同步队列尾中。一个线程在阻塞前会把它前面的节点设置为通知状态，这样便实现了链式唤醒机制

![image-20220419213822123](https://gitee.com/qianchao_repo/pic-typora/raw/master/juc_img/202204192138362.png)



#### CyclicBarrier



#### Semaphore

用于控制同时访问特定资源的线程数量，用于控制并发线程的数量

应用场景：流量控制

底层还是Sync内部类继承AQS



#### Exchanger



