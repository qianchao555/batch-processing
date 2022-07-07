## JUC

### 为什么需要多线程

众所周知，CPU、内存、I/O 设备的速度是有极大差异的，为了合理利用 CPU 的高性能，平衡这三者的速度差异，计算机体系结构、操作系统、编译程序都做出了贡献，主要体现为:

- CPU 增加了缓存，以均衡与内存的速度差异；// 导致 `可见性`问题
- 操作系统增加了进程、线程，以分时复用 CPU，进而均衡 CPU 与 I/O 设备的速度差异；// 导致 `原子性`问题
- 编译程序优化指令执行次序，使得缓存能够得到更加合理地利用。// 导致 `有序性`问题



---

### 并发编程的挑战

#### 上下文切换

即使是单核处理器也支持多线程执行代码，cpu通过给每个线程分配cpu时间片来实现

时间片是cpu分配给各个线程的时间，因为时间片非常短，所有cpu通过不停地切换线程执行，以至于使我们感觉多个线程是同时执行地，时间片一般在几十毫秒。cpu通过时间片分配算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。

任务切换前会保存上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。所以任务从保存再到加载的过程就是一次上下文切换

上下文切换会影响多线程的执行速度

##### 多线程一定快吗

不一定，因为线程创建和上下文切换需要开销

##### 如何减少上下文切换

1. 无锁并发编程
   - 多线程竞争锁时，会引起上下文切换，所以多线程处理数据时，可以使用一些方法来避免使用锁
2. CAS算法
   - Atomic包使用CAS算法来更新数据，不需要加锁
3. 使用最少线程
   - 避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态



#### 死锁

t1和t2两个线程互相等待对方释放锁

出现死锁需要通过dump线程，来查查到底是哪个线程出了问题



避免死锁的几种方法：

1. 避免一个线程同时获取多个锁
2. 避免一个线程在锁内同时占有多个资源，尽量保证每个锁只占用一个资源
3. 使用定时锁，例如：lock.tryLock（timeout)来替代内部锁机制
4. 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况

----

### 创建线程的方式

1. 继承Thread
2. 实现Runnable接口
3. 实现Callable接口
4. 采用线程池

实现接口的好处：

---

### 线程状态(生命周期)

![image-20220412212247848](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204122122015.png)



1. 新建(初始态)New：实现Runnable接口和继承Thread可以得到一个线程类，new一个实例出来，线程就进入了初始状态
2. 可运行态(就绪态)Runnable：线程创建后，调用start()方法，此时线程处于可运行线程池中，等待被线程调度选中，获取cpu是使用权
3. 运行态Running：线程获得了cpu时间片，执行任务
4. 阻塞态Blocked：线程由于某种原因放弃了cpu的使用权，直到线程进入可运行态，才有机会再次获得cpu时间片进入到运行态
5. 死亡态Dead：线程run()、main()方法执行结束，或者异常退出了，则该线程结束生命周期



---

### 多线程中i++等操作时线程安全问题

#### Java内存模型-JMM

Java内存模型：Java Memory Model

为什么要有内存模型：Java为了屏蔽硬件和操作系统访问内存的各种差异，提出了Java内存模型的规范，保证了Java程序在各种平台下对内存的访问都能得到一致效果

Java虚拟机会实现这个规范

Java内存模型定义了Java线程对内存数据进行交互的规范，线程之间的共享变量存储在主内存中，每个线程有自己私有的本地内存，本地内存存储了该线程以读/写共享变量的副本。本地内存是Java内存模型抽象的概念，并不是真实存在的

Java内存模型的抽象结构图：

![image-20220411231453088](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204112314267.png)

Java内存模型规定了：线程对变量的所以操作都必须在本地内存进行，不能直接读写主内存的变量。

JMM定义了8种操作来完成变量如何从主内存到本地内存，以及变量如何从本地内存到主内存

分别是：read、load、use、assign、store、write、lock、unlock

![image-20220412204327444](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204122043152.png)



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
   - ![image-20220411230210765](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204112302908.png)



如何解决上述问题？

原子引用更新

比较更新时，添加版本号--AtomicStampedReference

​	



为什么CAS而不用sync？
CAS保证一致性，并提高并发性

---

### Java内存模型

通信是指线程之间以何种机制来交换信息。在命令式编程中，线程之间的通信机制有两种：共享内存和消息传递

在共享内存的并发模型里，线程之间共享程序的公共状态，通过写-读内存中的公共状态进行隐式通信。在消息传递的并发模型里，线程之间没有公共状态，线程之间必须 通过发送消息来显式进行通信

Java 的并发采用的是共享内存模型，Java 线程之间的通信总是隐式进行，整个通信过程对程序员完全透明

#### JMM抽象结构

局部变量、方法定义参数、异常处理器参数这些是线程私有的，不会在线程之间共享，也即它们不会有内存可见性问题和线程安全问题

Java线程之间的通信由JMM控制，JMM 决定一个线程对共享变量的写入何时对另一个线程可见

JMM定义了线程和主内存之间的抽象关系：

1. 线程之间的共享变量存储在主内存中
2. 每个线程有自己私有的本地内存，本地内存中存储了该线程读、写共享变量的副本
3. JMM是抽象的概念，并不是真实存在的。它涵盖了缓存、写缓冲区、寄存器以及其他硬件和编译器优化

![image-20220629150030115](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202206291501788.png)



#### 源代码到指令序列的重排序

为了提高性能，编译器和处理器都会对指令做重排序

重排序分为：

1. 编译器优化的重排序
2. 指令级并行的重排序
3. 内存系统的重排序

2和3属于处理器重排序，这些重排序可能会导致多线程程序出现内存可见性问题

![image-20220629150439894](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202206291504623.png)



对于编译器重排序：JMM的编译器重排序规则会禁止特定类型的编译器重排序

对于处理器重排序：JMM的处理器重排序规则会要求Java编译器在生成指令序列的时候，插入特定类型的内存屏障，通过内存屏障来禁止特定类型的处理器重排序



#### happens-before

JSR-133使用happens-before概念来阐述操作之间的内存可见性

在 JMM 中，如果一个操作执行的结果需要对另一个操作可见，那么这两个操作之间必须要存在 happens-before 关系。这里提到的两个操作既可以是在一个线程之内，也可是在不同线程之间

注意：两个操作之间具有 happens-before 关系，并不意味着前一个操作必须要在后一个操作之前执行！happens-before 仅仅要求前一个操作（执行的结果）对后一个操作可见，且前一个操作按顺序排在第二个操作之前

happens-before与JMM关系：

一个happens-before规则对应于一个或多个编译器和处理器重排序规则

![image-20220629151932223](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202206291519550.png)



#### 重排序

重排序是指编译器和处理器为了优化程序性能而对指令序列进行重新排序的一种手段，编译器和处理器在重排序时，会遵循数据依赖性。不会改变存在数据依赖性关系的两个操作的执行顺序

##### 数据依赖性

1. 若两个操作访问同一个变量，且这两个操作中有一个为写操作，此时这两个操作之间存在数据依赖性

2. ![image-20220629152642190](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202206291526587.png)

   

数据依赖性仅针对单个处理器中执行的指令序列和单个线程中执行的操作，不同处理器之间和不同线程之间的数据依赖性不被编译器和处理器考虑



##### as-if-serial

as-if-serial语义：不管怎么重排序，程序(单线程)的执行结果不能被改变，编译器、处理器

为了遵循as-if-serial语义，编译器和处理器不会对存在数据依赖关系的操作做重排序，因为这种重排序会改变执行结果。但是，如果操作之间不存在数据依赖关系，这些操作就可能被编译器和处理器重排序

as-if-serial 语义把单线程程序保护了起来，遵守 as-if-serial 语义的编译器、runtime 和处理器共同为编写单线程程序的程序员创建了一个幻觉：单线程程序是按程序的顺序来执行的。asif-serial 语义使单线程程序员无需担心重排序会干扰他们，也无需担心内存可见性问题







---

### Volatile

解决可见性和有序性问题，不能保证原子性

#### volatile保证可见性

一个线程修改了共享变量的值，另一个线程能看见

原理：基于内存屏障实现

1. 内存屏障：又称内存栅栏，是一个CPU指令
2. 在程序运行时，为了提高执行性能，编译器和处理器会对指令进行重排序，JMM 为了保证在不同的编译器和 CPU 上有相同的结果，通过插入特定类型的内存屏障来禁止+ 特定类型的编译器重排序和处理器重排序，插入一条内存屏障会告诉编译器和 CPU：不管什么指令都不能和这条 Memory Barrier 指令重排序

#### volatile实现有序性

原理：happens-before规则



---

### Final关键字

修饰类：该类不能被继承，类中的方法都隐式为final

接口中：所有变量隐式为final

final类如何扩展？例如String，现在有一个MyString想要复写所有String的方法，同时增加自己的toMyStrin()方法

~~~java
//因为不能继承，所有采用组合的方式实现
class MyString{

    private String innerString;

   // 支持老的方法
    public int length(){
        return innerString.length(); // 通过innerString调用老的方法
    }
    
     // ..其他方法类似

    // 添加新方法
    public String toMyString(){
        //...
    }
}
~~~

修饰方法：final方法可以被重载，但是不能被继承也即不能重写；private是隐式的final

修饰参数：方法的参数列表中将参数指明为final，意味着不能在方法中更改参数所指向的对象。这一特性主要用来向匿名内部类传递数据

修饰变量：并非所有final修饰的字段都是编译期常量

例如：

~~~java
public class Test {
    //编译期常量
    final int i = 1;
    final static int J = 1;
    final int[] a = {1,2,3,4};
    //非编译期常量
    Random r = new Random();
    final int k = r.nextInt();
    //方法...
}
k的值由随机数对象决定，所以不是所有的final修饰的字段都是编译期常量，只是k的值在被初始化后无法被更改
~~~

static final：占据一段不能改变的存储空间，必须在定义的时候进行赋值，否则编译器将不予通过

#### final域重排序规则

1. 写final域禁止重排序到构造函数之外
   - 因此写final域可以保证在对象引用为任意线程可见之前，对象的final域已经正确初始化了，而普通域没有这个保证
2. 读final域重排序规则：在一个线程中，初次读对象引用和初次读该对象包含的final域，JMM会禁止这两个操作的重排序
   - final域读操作限定了在读之前，已经读到了该对象的引用
3. 以上针对final基本数据类型
4. final引用类型
   - 额外增加约束
   - 禁止在构造函数对一个final修饰的对象的成员域的写入与随后将这个被构造的对象的引用赋值给引用变量 重排序





---

### happens-before

happens-before是JMM中最核心的概念，核心目标是找到一个平衡点：

1. 一方面，为程序员提供足够强的内存可见性保证
2. 另一方面，对编译器和处理器的限制要尽可能地放松

JSR(Java Specification Requests)：Java规范提案 

JSR-133使用 happens-before 的概念来指定两个操作之间的执行顺序。由于这两个操作可以在一个线程之内，也可以是在不同线程间。因此，JMM 可以通过 happens-before 关系向程序员提供跨线程的内存可见性保证（如果 A 线程的写操作 a 与 B 线程的读操作 b 之间存在 happens-before 关系，尽管 a 操作和 b 操作在不同的线程中执行，但JMM 向程序员保证 a 操作将对 b 操作可见









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

synchronized修饰的方法：无论正常执行结束还是抛出异常都会释放锁

##### 对象锁

锁对象为当前对象this

~~~java
synchronized (this){
}

public synchronized void xxMethod(){
    
}
~~~

##### 类锁

1. 指定锁对象为Class对象
2. synchronized修饰的静态方法

~~~java
public A{
    synchronized (A.class){
        
    }

    public static synchronized void xxMethod(){

    }
}
~~~

#### Synchronized原理

对class文件进行反编译可得出：

moniterenter 、moniterexit 指令

moniter：对象监视器

加锁原理：moniterenter

每一个对象在同一时间只能与一个moniter相关联，而一个moniter在同一时间只能被一个线程获得，一个线程在尝试获得这个对象相关联的moniter锁的所有权的时候，moniterenter指令会发生3种情况之一

1. moniter计数器为0，意味着当前没有线程获取到这个对象锁，这个线程立即获得这个对象锁，然后锁计数器+1，别的线程想要获取到这个锁对象只能等待
2. 若这个moniter已经拿到了这个锁的所有权，同时又重入了这把锁，此时锁计数器累加，随着重入的次数会一直累加
3. 这把锁被别的线程获取了，等待锁释放

释放原理：moniterexit

即释放对于moniter的所有权，释放过程即moniter的计数器-1，若减完之后计数器不是0，则代表这个锁是可重入的，当前线程继续持有这把锁的所有权，若计数器变为0，则代表当前线程不再拥有该moniter的所有权，即释放锁



对象、对象监视器、同步队列以及指向线程状态之间关系：

任意线程对Object的访问，首先要获得Object的监视器，如果获取失败，该线程就进入同步队列，线程状态变为BLOCKED，当Object的监视器占有者释放后，在同步队列中得线程就会有机会重新获取该监视器

![image-20220627231509809](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202206272315030.png)



##### 可重入锁

又名递归锁，是指在同一个线程在外层方法获取锁的时候，再进入该线程的内层方法会自动获取锁（前提锁对象得是同一个对象或者class），不会因为之前已经获取过还没释放而阻塞



#### JVM中锁优化

在JVM中monitorenter和monitorexit字节码依赖于底层的操作系统的Mutex Lock来实现的，但是由于使用Mutex Lock需要将当前线程挂起并从用户态切换到内核态来执行，这种切换的代价是非常昂贵的；然而在现实中的大部分情况下，同步方法是运行在单线程环境(无锁竞争环境)如果每次都调用Mutex Lock那么将严重的影响程序的性能

在jdk1.6中对锁的实现引入了大量的优化，如锁粗化(Lock Coarsening)、锁消除(Lock Elimination)、轻量级锁(Lightweight Locking)、偏向锁(Biased Locking)、适应性自旋(Adaptive Spinning)等技术来减少锁操作的开销

##### Java对象头

synchronized用的锁存在Java对象头里面

##### 锁类型及升级

Java1.6里的synchronized同步锁引入了偏向锁、轻量级锁，一共有4种锁状态：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态

这四种锁状态随着竞争情况逐渐升级，锁可以升级但是不能降级，目的是：为了提供获取锁和释放锁的效率

锁膨胀方向：无锁->偏向锁->轻量级锁->重量级锁，此过程不可逆

#### Synchronized缺点

1. 效率低
2. 不够灵活
3. 无法知道是否成功获得锁



---

#### Lock接口

锁是用来控制多个线程访问共享资源的方式，一般来说，一个锁能够防止多个线程同时访问共享资源（但是有些锁可以允许多个线程并发的访问共享资源，比如读写锁）。在 Lock 接口出现之前，Java 程序是靠 synchronized 关键字实现锁功能的，而 Java SE 5之后，并发包中新增了 Lock 接口（以及相关实现类）用来实现锁功能，它提供了与synchronized 关键字类似的同步功能，只是在使用时需要显式地获取和释放锁

Lock接口具有synchronized所不具备的主要特性

![image-20220703164843752](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031648891.png)



Lock是一个接口，它定义了锁获取和释放的基本操作，具体的实现由相应的实现类实现

![image-20220703164937947](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031649097.png)



Lock接口的实现基本上都是通过聚合了一个AbstractQueuedSynchronizer同步器的子类来完成线程访问控制的

---

#### 队列同步器AQS

参考文章：https://blog.csdn.net/mulinsen77/article/details/84583716

参考文章：https://www.cnblogs.com/waterystone/p/4920797.html

参考文章：Java并发编程的艺术=>经典之作



AbstractQueueSynchronizer  同步器，是用来构建锁或者其他同步组件的基础框架，它使用了一个int的成员变量表示同步状态（volatile state)，通过内置的FIFO队列来完成资源获取线程的排队工作。

实现同步器主要通过继承来实现，子类通过继承同步器并实现它的抽象方法来管理同步状态

子类推荐被定义为自定义同步组件的静态内部类，同步器自身没有实现任何同步接口，它仅仅是定义了若干同步状态获取和释放的方法来供自定义同步组件使用，同步器既可以支持独占式地获取同步状态，也可以支持共享式地获取同步状态，这样就可以方便实现不同类型的同步组件

同步器是实现锁的关键，锁是面向使用者的，同步器是面向锁的实现者、它简化了锁的实现方式,屏蔽了同步状态管理、线程的排队、等待与唤醒等底层操作

同步器基于模板方法模式，基于AQS可以实现自己的很多锁类，比如ReentrantLock等等



##### 如何自定义同步器

1. 静态内部类(自定义同步器)继承自AQS
2. 重写AQS中可重写的方法，例如：tryAcquire、tryRelease等等
3. 将操作代理到我们自己的同步器上面
4. 调用AQS提供的模板方法，有一些相应的模板方法会去调用我们重写的相关方法



##### AQS底层使用了模板方法设计模式

同步器的设计基于模板方法设计模式的，如果需要自定义同步器一般是：

1. 使用者继承AbstractQueuedSynchronizer并重写指定的方法
2. 将AQS组合在自定义的同步组件的实现中，并调用模板方法，而这些模板方法会调用使用者重写的方法

同步器中提供的模板方法基本上分为3类，自定义同步组件将使用同步器提供的模板方法来实现自己的同步语义

1. 独占式获取与释放同步状态
2. 共享式获取与释放同步状态
3. 查询同步队列中等待线程情况

##### AQS核心思想/实现原理

一句话理解：AQS是基于CLH队列，用volatile修饰共享变量state，线程通过CAS去改变状态，成功则获取锁成功，失败则进入队列等待，等待被唤醒。AQS是自旋锁，在等待被唤醒的时候，经常会使用自旋( while(!cas() )的方式，不停的尝试获取锁，直到被其他线程获取成功

实现了AQS的锁有：自旋锁、互斥锁、读写锁、信号量、栅栏等等都是AQS的衍生物

###### 同步队列

同步器依赖内部的同步队列（一个 FIFO 双向队列）来完成同步状态的管理，当前线程获取同步状态失败时，同步器会将当前线程以及等待状态等信息构造成为一个节点 （Node）并将其加入同步队列，同时会阻塞当前线程，当同步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态

这个队列即：CLH队列

![image-20220414211911107](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032112637.png)

CLH：Craig Landin and Hagersten ，CLH队列（是FIFO）是一个虚拟的双向队列，虚拟的双向队列：即不存在队列实例，仅存在节点之间的关联关系

AQS是将每一条请求共享资源的线程封装成一个CLH锁队列的一个结点（Node），来实现锁的分配，每个节点保存获取同步状态失败的线程引用、等待状态、前驱后继节点等信息

节点的属性与名称以及描述信息如下：

![image-20220703204323638](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032043769.png)



节点是构造同步队列的基础，同步器拥有首尾节点，没有成功获取同步状态的线程将会成为节点加入到该队列的尾部。这个添加动作是线程安全的，采用compareAndSetTail（Node expect,Node Update)来完成。

![image-20220703205037345](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032050452.png)



将节点加入到同步队列过程：

![image-20220703210913033](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032109171.png)



同步队列遵循 FIFO，首节点是获取同步状态成功的节点，首节点的线程在释放同步状态时，将会唤醒后继节点，而后继节点将会在获取同步状态成功时将自己设置为首节点。

设置首节点是通过获取同步状态成功的线程来完成的，，由于只有一个线程能够成功获取到同步状态，因此设置头节点的方法并不需要使用 CAS 来保证，它只需要将首节点设置成为原首节点的后继节点并断开原首节点的 next 引用即可

![image-20220703211033520](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032110644.png)



AQS定义了两种资源共享方式：

1. 独占：只有一个线程能执行，例如ReentrantLock
2. 共享：多个线程可以同时执行，例如Semaphore、CountDownLatch、ReadWriteLock、CyclicBarrier
3. 不同的自定义的同步器争用资源的方式也不同

###### 独占式同步状态获取与释放

1. 获取同步状态

   - 调用同步器的acquire(int args)方法获取同步状态，该方法是对中断不敏感的，即线程获取同步状态失败后进入同步队列中，后续对该线程进行中断操作，该线程不会从同步队列中移出

   - 只有前驱节点是头结点才能够获取同步状态，原因：

     - 因为头结点是成功获取了同步状态的节点，头结点的线程释放同步状态后会唤醒后继节点，后继节点的线程被唤醒后需要检测自己的前驱节点是否是头结点
     - 为了维护同步队列的FIFO原则

   - 节点自旋获取同步状态的行为如下：

   - ![image-20220703214717303](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032147465.png)

   - 独占式同步状态获取流程如下：

   - ![image-20220703215327364](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207032153505.png)

   - 当从acquire()返回，对于锁这种并发组件而言，代表当前线程获取了锁

     

2. 独占式释放同步状态

   - 当前线程获取同步状态并执行了相应逻辑之后，就需要释放同步状态，使得后续节点能够继续获取同步状态
   - release(int args)，执行该方法后会唤醒后继节点线程，使用LockSupport来唤醒处于等待状态的线程

3. 总结

   - 在获取同步状态时，同步器维护一个同步队列，获取状态失败的线程都会被加入到队列中并在队列中进行自旋；移出队列（或停止自旋）的条件是前驱节点为头节点且成功获取了同步状态。
   - 在释放同步状态时，同步器调用 tryRelease(int arg)方法释放同步状态，然后唤醒头节点的后继节点



###### 共享式同步状态获取与释放

共享式获取与独占式获取最主要的区别在于同一时刻能否有多个线程同时获取到同步状态

1. 共享式同步状态的获取
   - 调用同步器的acquireShared(int arg)可以共享式的获取同步状态
   - 在doAcquireShared(int arg)方法的自旋过程中，如果当前节点的前驱为头节点时，尝试获取同步状态，如果返回值大于等于 0，表示该次获取同步状态成功并从自旋过程中退出
2. 共享式同步状态的释放
   - 通过调用 releaseShared(int arg)方法可以释放同步状态
   - 该方法在释放同步状态之后，将会唤醒后续处于等待状态的节点
   - 对于能够支持多个线程同时访问的并发组件（比如 Semaphore），它和独占式主要区别在于tryReleaseShared(int arg) 方法必须确保同步状态（或者资源数）线程安全释放，一般是通过循环和 CAS 来保证的，因为释放同步状态的操作会同时来自多个线程

###### 独占式超时获取同步状态

调用同步器的 doAcquireNanos(int arg,long nanosTimeout)方法可以**超时获取同步状态**，即在指定的时间段内获取同步状态，如果获取到同步状态则返回 true，否则，返回 false。

该方法提供了传统 Java 同步操作（比如 synchronized 关键字）所不具备的特性



#### ReentrantLock

##### 可重入锁

简单理解：同一个线程对于已经获得的锁，可以继续申请到该锁的使用权

内部为sync extends AQS内部类实现，其次，它的公平锁与非公平锁都是在xxx extends sync内部类中实现的。公平锁即等待时间最长的线程最优先获取锁

如何实现可重入：volitale类型的int state变量  0表示没有任何线程持有该锁，通过判断当前线程是否为获取锁的线程来决定获取操作是否成功，如果是获取锁的线程再次请求，则将同步状态值进行增加并返回 true，表示获取同步状态成功。成功获取锁的线程再次获取锁，只是增加了同步状态值，这也就要求 ReentrantLock 在释放同步状态时减少同步状态值，

释放锁：如果该锁被获取了 n 次，那么前(n-1)次 tryRelease(int releases)方法必须返回 false，而只有同步状态完全释放了，才能返回 true。可以看到，该方法将同步状态是否为 0 作为最终释放的条件，当同步状态为 0 时，将占有线程设置为 null，并返回 true，表示释放成功

**ReentrantLock公平锁与非公平锁区别**

1. 公平性针对获取锁而言，如果一个锁是公平的，那么锁的获取顺序就应该符合请求的绝对时间顺序，也就是 FIFO
2. 对于非公平锁，只要CAS 设置同步状态成功，则表示当前线程获取了锁，而公平锁实现则不同，唯一不同的位置为判断条件多了hasQueuedPredecessors()方法，即加入了同步队列中当前节点是否有前驱节点的判断，如果该方法返回 true，则表示有线程比当前线程更早地请求获取锁，因此需要等待前驱线程获取并释放锁之后才能继续获取锁。如果该方法返回 true，则表示有线程比当前线程更早地请求获取锁，因此需要等待前驱线程获取并释放锁之后才能继续获取锁
3. 非公平锁效率更高，因为公平锁为了保证FIFO，需要进行大量的线程切换。虽然非公平锁可能造成线程饥饿现象，但是线程极小的切换，保证了更大的吞吐量



---

### Java中的读写锁

ReentrantLock、synchronized等等是排他锁，这些锁在同一时刻只允许一个线程进行访问，而读写锁在同一时刻可以允许多个读线程访问，但是在写线程访问时，所有的读线程和其他写线程均被阻塞

读写锁维护了一对锁，一个读锁和一个写锁，通过分离读锁和写锁，使得并发性相比一般的排他锁有了很大提升

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

实现：**按位切割**使用这个变量，将高16位表示读锁的个数，低16位表示写锁的个数

下图状态表示：一个线程已经获取到了写锁，且重入了两次，同时也连续获取了两次读锁

![image-20220418230634085](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204182306630.png)

读写锁如何确定各自的状态的？

通过位运算实现：假如当前同步状态为S，写状态通过 c=S & 0x0000FFFF(将高16位抹掉)，读状态等于c=S>>>16(无符号补0右移16位)

当写状态+1时：c（state字段的值）就等于S+1

当读状态+1时：c等于S+(1>>16)

根据状态的划分能得出一个推论：S 不等于 0 时，当写状态（S&0x0000FFFF）等于0 时，则读状态（S>>>16）大于 0，即读锁已被获取



##### 读锁的获取与释放

1. 读锁的获取

   - 读锁是一个支持重入的共享锁锁，它能够被多个线程同时获取，在没有其他写线程访问（或者写状态为 0）时，读锁总会被成功地获取，而所做的也只是（线程安全的）增加读状态
   - 如果当前线程已经获取了读锁，则增加读状态。如果当前线程在获取读锁时，写锁已被其他线程获取，则进入等待状态
   - 读状态是所有线程获取读锁次数的总和，而每个线程各自获取读锁的次数只能选择保存在 ThreadLocal 中，由线程自身维护，这使获取读锁的实现变得复杂

2. 读锁的释放

   - 读锁的每次释放（线程安全的，可能有多个读线程同时释放读锁）均减少读状态，减少的值为1（1<<16)

   

##### 写锁的获取与释放

1. 写锁的获取

   - 写锁是一个支持重入的排他锁
   - 如果当前线程已经获取写锁时，则增加写状态
   - 如果当前线程在获取写锁时，读锁已经被获取或者当前线程不是已经获取写锁的线程，则当前线程进入等待状

   除了重入条件（当前线程为获取了写锁的线程）之外，增加了一个读锁是否

   存在的判断。如果存在读锁，则写锁不能被获取，原因在于：读写锁要确保写锁的操作

   对读锁可见，如果允许读锁在已被获取的情况下对写锁的获取，那么正在运行的其他读

   线程就无法感知到当前写线程的操作。因此，只有等待其他读线程都释放了读锁，写锁

   才能被当前线程获取，而写锁一旦被获取，则其他读写线程的后续访问均被阻塞

2. 写锁的释放

   - 与ReentrantLock的释放过程类似，每次释放均减少写状态
   - 当写状态为 0 时表示写锁已被释放，从而等待的读写线程能够继续访问读写锁，同时前次写线程的修改对后续读写线程可见

##### 锁降级

指的是写锁降级成读锁。如果当前线程拥有写锁，其还可以再次获取读锁，然后将其读锁释放，最后再获取读锁，这种分段完成的过程不能称之为锁降级。锁降级是指把持住（当前拥有的）写锁，再获取到读锁，随后释放（先前拥有的）写锁的过程

一个线程有写锁后可以获取读锁，但是有读锁了后不能再去获取写锁

例如：因为数据不常变化，所以多个线程可以并发地进行数据处理，当数据变更后，如果当前线程感知到数据变化，则进行数据的准备工作，同时其他处理线程被阻塞，直到当前线程完成数据的准备工作

~~~java
public void processData() {
     readLock.lock();
     if (!update) {
         // 必须先释放读锁
         readLock.unlock();
         // 锁降级从写锁获取到开始
         writeLock.lock();
         try {
             if (!update) {
                 // 准备数据的流程（略）
                 update = true;
             }
             readLock.lock();
         } finally {
             writeLock.unlock();
         }
    }// 锁降级完成，写锁降级为读锁
    try { 
        // 使用数据的流程（略）
    } finally {
        readLock.unlock();
    } 
}
~~~



**锁降级中读锁的获取是否必要呢？**

答案是必要的。

主要是为了保证数据的可见性，如果当前线程不获取读锁而是直接释放写锁，假设此刻另一个线程（记作线程 T）获取了写锁并修改了数据，那么当前线程无法感知线程 T 的数据更新。如果当前线程获取读锁，即遵循锁降级的步骤，则线程 T 将会被阻塞，直到当前线程使用数据并释放读锁之后，线程 T 才能获取写锁进行数据更新



ReentantReadWriteLock不支持锁升级。目的：保证数据的可见性，如果读锁被多个线程获取，其中的任意线程成功获取写锁后并更新了数据，那么其更新对其他获取到读锁的线程是不可见的



---

### LockSupport工具

LockSupport定义了一组的公共静态方法，这些方法提供了最基本的线程阻塞和唤醒功能，而LockSupport也成为构建同步组件的基础工具

LockSupport定义了一组以park开头的方法用来阻塞当前线程，以及unpark(Thread thread)方法来唤醒一个被阻塞的线程。

**简单的说：LockSupport就是用来和AQS配合阻塞和唤醒线程的**

park开头的方法用来阻塞当前线程

unpark(Thread t)方法来唤醒一个被阻塞的线程

---

### Condition接口

任意一个 Java 对象，都拥有一组监视器方法（定义在 java.lang.Object 上），主要包括 wait()、 wait(long timeout)、notify()以及 notifyAll()方法，这些方法与 synchronized 同步关键字配合，可以实现等待/通知模式

condition接口提供了类似Object的监视器方法，与Lock接口配合可以实现等待/通知模式



![image-20220702233005015](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207022330387.png)



Condition 定义了等待/通知两种类型的方法，当前线程调用这些方法时，需要提前获取到 Condition 对象关联的锁。Condition 对象是由 Lock 对象（调用 Lock 对象的newCondition()方法）创建出来的，换句话说，Condition 是依赖 Lock 对象的

当调用 await()方法后，当前线程会释放锁并在此等待，而其他线程调用 Condition 对象的 signal()方法，通知当前线程后，当前线程才从 await()方法返回，并且在返回前已经获取了锁

#### Condition实现原理

ConditionObject是AQS的内部类，每个Condition对象都包含一个队列(等待队列)，该队列是Conditon对象实现等待/通知功能的关键

##### 等待队列

是一个FIFO的队列，在队列中的每个节点都包含了一个线程引用，该线程就是在 Condition 对象上等待的线程，如果一个线程调用了 Condition.await()方法，那么该线程将会释放锁、构造成节点加入等待队列并进入等待状态

节点的定义复用了同步器中节点的定义，也就是说，同步队列和等待队列中节点类型都是同步器的静态内部类 AbstractQueuedSynchronizer.Node

Condition对象拥有首尾节点，当前线程调用Condition.await()，将会以当前线程构造节点，并将节点从尾部加入等待队列

等待队列的基本结构如下：

![image-20220703153335835](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031533946.png)

Condition拥有首尾节点，新增节点只需要将原有的尾节点nextWaiter指向新增节点，并且更新lastWaiter节点即可



在Object的监视器模型上，一个对象拥有一个同步队列和一个等待队列

![image-20220703155225488](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031552664.png)

但是在并发包中的Lock(更确切的说是同步器AQS)拥有一个同步队列和**多个等待队列**

其对应关系如下：

![image-20220703154840889](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031548027.png)



##### 等待

调用Codition的await()，会使当前线程进入等待队列并释放锁，同时线程状态为等待状态

当从await()方法返回时，当前线程一定获取了Condition相关联的锁

从队列（同步队列和等待队列）的角度看 await()方法，当调用 await()方法时，相当于同步队列的首节点（获取了锁的节点）移动到 Condition 的等待队列中，然后释放同步状态，唤醒同步队列中的后继节点，然后当前线程进入等待状态

同步队列的首节点并不会直接加入等待队列，而是通过addConditionWaiter()方法把当前线程构造成一个新的节点并将其加入等待队列中

![image-20220703155950871](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031559984.png)



##### 通知

调用Condition的signal()，将会唤醒在等待队列中等待时间最长的节点(即等待队列的首节点)，唤醒前，会将节点移步到同步队列中

![image-20220703161059033](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207031610154.png)

通过调用同步器的 enq(Node node)方法，等待队列中的头节点线程安全地移动到同步队列。当节点移动到同步队列后，当前线程再使用 LockSupport 唤醒该节点的线程。

被唤醒后的线程，将从 await()方法中的 while 循环中退出（isOnSyncQueue(Node node)方法返回 true，节点已经在同步队列中），进而调用同步器的 acquireQueued()方法加入到获取同步状态的竞争中。

成功获取同步状态（或者说锁）之后，被唤醒的线程将从先前调用的 await()方法返回，此时该线程已经成功地获取了锁



Condition 的 signalAll()方法，相当于对等待队列中的每个节点均执行一次 signal()方法，效果就是将等待队列中所有节点全部移动到同步队列中，并唤醒每个节点的线程





---

### Java并发容器和框架

#### ConcurrentHashMap

HashMap多线程下是线程不安全的，HashTable线程安全，但是效率低下，因为是对每个方法进行synchronized

concurrentHashMap：采用锁分段技术有效提升并发访问率

##### 实现原理

concurrentHashMap采用锁分段技术，首先将数据分为一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一段数据的时候，其他段的数据能被其他线程访问

##### concurrentHashMap结构

![image-20220414225538803](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204142255782.png)

由Segment数组结构和HashEntry数组构成。Segment是一个可重入锁，在concurrentHashMap里面扮演锁的角色，HashEntry则用于存储键值对数据

一个ConcurrentHashMap中包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表结构。一个Segment里面包含一个HashEntry数组，每个HashEntry是一个链表的元素。每个Segment拥有一个锁，当对HashEntry数组的数据进行修改时，必须先获得对应的Segment锁

segment：每一个segment用有一把锁，其继承自ReentrantLock

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

![image-20220418210204525](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204182102569.png)



##### concurrentHashMap的put操作

为了保证共享变量的线程安全，所有采用了加锁方式

1. put方法首先定位到segment，然后在segment里面进行插入操作
2. 插入时经过了两个步骤
   - 判断hashEntry数组是需要扩容：需要则创建一个容量是原来容量两倍的数组，然后将原数组里的元素进行再散列后插入到新数组里。为了高效ConcurrentHashMap不会对整个容器扩容，而是针对某个segment进行扩容
   - 定位添加元素的位置，然后将其插入到HashEntry数组中



---

#### ConcurrentLinkedQueue

要实现一个线程安全的队列的两种方式

1. 使用阻塞算法(入队和出队用一把锁，或者入队和出队用不同的锁)
2. 使用非阻塞算法（使用循环/自选的CAS方式实现）

concurrentLinkedQueue：非阻塞的线程安全的队列，采用循环的cas算法实现

它是一个基于链接节点的**无界线程安全队列**，采用先进先出的规则对节点进行排序，添加元素时，添加到队列的尾部，获取元素时，它会返回队列头部的元素。它采用了“wait-free”算法（即 CAS 算法）来实现，该算法在 Michael&Scott 算法上进行了一些修改

##### 数据结构

由head节点和tail节点组成，每个节点Node由节点元素和指向下一个节点的引用组成，节点与节点之间通过这个next关联起来，从而组成一张链表结构的队列

![image-20220704115451741](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207041155883.png)

![image-20220418212146272](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204182121424.png)



concurrentQueue由head和tail节点组成，每个节点由节点元素和指向下一个节点的引用组成，节点之间通过next关联起来从而构成一张链表结构的队列

默认情况下head节点存储的元素为空，tail节点等于head节点

~~~java
private transient volatile Node<E> tail=head;
public ConcurrentLinkedQueue() {
      head = tail = new Node<E>(null);
}
~~~



##### 入队列

将入队节点添加到队列的尾部

1. 通过cas算法入队列
2. 定位尾节点
3. 设置入队列节点为尾节点

##### 出队列

1. 从队列里返回一个节点元素，并清空该节点对元素的引用



---



#### Java中的阻塞队列

##### 什么是阻塞队列

Blocking Queue：是一个支持两个附加操作的队列，附加操作为：支持阻塞的插入和删除(获取)方法

阻塞的插入：当队列满时，队列会阻塞插入元素的线程，直到队列不满

阻塞的移除(获取)：当队列空时，获取元素的线程会阻塞等待队列变为非空



##### 使用场景：生产者消费者场景

阻塞队列用于生产者存放元素，消费者用来获取元素的容器



在阻塞队列不可用时，这两个附加操作提供了4种处理方式：抛出异常、返回特殊值、一直阻塞、超时退出

![image-20220418213120686](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204182131837.png)

1. 抛出异常：是指当阻塞队列满时，再往队列插入元素，会抛出 IllegalStateException("Queue full") 异常。当队列为空时，从队列里获取元素时会抛出 NoSuchElementException 异常
2. 返回特殊值：插入方法会返回是否成功，成功则返回 true。移除方法，则是从队列里拿出一个元素，如果没有则返回 null
3. 一直阻塞：当阻塞队列满时，如果生产者线程往队列里 put 元素，队列会一直阻塞生产者线程，直到队列可用或者响应中断退出。当队列空时，消费者线程试图从队列里 take 元素，队列也会阻塞消费者线程，直到队列不为空
4. 超时退出：当阻塞队列满时，队列会阻塞生产者线程一段时间，如果超过一定的时间，生产者线程就会退出

如果是无界队列，则队列不可能出现满的情况，所以使用put或offer插入元素的时候永远不会被阻塞，并且使用offer时，该方法永远返回true

##### 阻塞队列的分类

###### **ArrayBlockingQueue** 

一个由**数组结构组成的有界**阻塞队列。

1. 队列按照先进先出的原则对元素进行排序

2. 默认不保证线程公平的访问队列

   - 公平访问队列是指阻塞的线程，可以按照阻塞的先后顺序访问队列，即先阻塞线程先访问队列

   - 非公平性是对先等待的线程是非公平的，当队列可用时，阻塞的线程都可以争夺访问队列的资格，有可能先阻塞的

     线程最后才访问队列

3. 公平的访问阻塞队列

   - 为了保证公平性，会降低吞吐量，因为：加入了同步队列中当前节点是否有前驱节点的判断
   - ArraryBlockingQueue fairQueue=new ArrayBlockingQueue(1000,true)；

4. 访问者的公平性是通过重入锁ReentrantLock实现的

   ~~~java
   /**
   *实现方式，采用了ReentrantLock，通过condition+LockSupport实现阻塞与唤醒线程
   */
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



###### **LinkedBlockingQueue** 

一个由**链表结构组成的有界**阻塞队列

- 链表长度默认和最大长度为Integer.Max_Value，可以当作无界队列来看待
- 此阻塞队列按照先进先出的原则对元素进行排序



###### PriorityBlockingQueue 

一个**支持优先级排序**的无界阻塞队列

- 默认情况下：元素采用自然顺序升序
- 可以自定义类实现compareTo()来指定元素排序规则



###### DelayQueue

是一个支持优先级的延时获取元素的无界阻塞队列

1. 该队列使用PriorityQueue来实现
2. 队列中的元素必须实现Delayed接口，在创建元素时可以指定多久才能从队列中获取当前元素
3. 只有在延迟期满时才能从队列中提前元素
4. 应用场景
   - 缓存系统的设计：用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦从队列中获取元素时，表示缓存有效期到了
   - 定时任务调度：使用 DelayQueue 保存当天将会执行的任务和执行时间，一旦从DelayQueue 中获取到任务就开始执行，比如 TimerQueue 就是使用 DelayQueue实现的

如何实现Delayed接口，参考ScheduledThreadPoolExecutor 里 ScheduledFutureTask 类的实现

1. 对象创建时，初始化基本数据
2. 实现getDelay()方法
3. 实现compareTo()方法来指定元素的顺序

如何实现延时阻塞队列

当消费者线程从队列获取元素时，如果元素没有达到延时时间，就阻塞当前线程



###### SynchronousQueue

一个不存储元素的阻塞队列，每一个put操作必须等待一个take操作，否则不能继续添加元素

可以看成是一个传球手，负责把生产者线程处理的数据直接传递给消费者线程

队列本身并不存储任何元素，非常适合传递性场景

LinkedTransferQueue：一个由**链表结构组成的无界**阻塞队列。

LinkedBlockingDeque：一个由**链表结构组成的双向**阻塞队列



##### 阻塞队列实现原理

原理：Lock锁的多条件等待/通知模式实现

通知模式：当生产者往满的队列里添加元素时，会阻塞生产者，当消费者消费了队列里面的元素后，会通知生产者当前队列可用

例如ArryBlockingQueue采用了ReentantLock+Condition的等待/通知（await/signal）+LockSupport类 来实现

例如：队列插入一个元素时，如果队列不可用，那么阻塞生产者。主要通过LockSupport.park(this)->调用unsafe.park 这个native方法来实现阻塞

线程被阻塞队列阻塞时，线程会进入等待状态Waiting

被阻塞的当前线程在以下4种情况下发生时，该方法会返回

1. 与park对于的unpark执行
2. 线程被中断时
3. 等待完time参数指定的毫秒数时
4. 异常现象发生时



---

### Java中的13个原子操作类

JDK1.5开始提供了juc包下atomic包，这个包中的原子操作类提供了一种用法简单、性能高效、线程安全地更新一个变量的方式

因为变量的类型有很多种，所以在 Atomic 包里一共提供了 13 个类，属于 4 种类型的原子更新方式，分别是原子更新基本类型、原子更新数组、原子更新引用和原子更新属性（字段）

Atomic 包里的类基本都是使用 Unsafe 实现的包装类

#### 原子更新基本类型

1. AtomicBoolean、AtomicInteger、AtomicLong
   - 这3个类提供的方法几乎一模一样
   - 底层为unsafe.compareAndSwapxxx实现

#### 原子更新数组

AtomicIntegerArray：原子更新整型数组里的元素

AtomicLongArray：原子更新长整型数组里的元素

AtomicReferenceArray：原子更新引用类型数组里的元素

AtomicIntegerArray 类主要是提供原子的方式更新数组里的整型，其常用方法

这几个类提供的方法几乎一摸一样

底层实现也是unsafe.compareAndSwapxxx实现

#### 原子更新引用类型

原子更新基本类型的 AtomicInteger，只能更新一个变量，如果要原子更新多个变量，就需要使用这个原子更新引用类型提供的类

AtomicReference：原子更新引用类型

AtomicReferenceFieldUpdater：原子更新引用类型里的字段AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记位和引用类型

这几个类提供的方法几乎一摸一样

#### 原子更新字段

如果需原子地更新某个类里的某个字段时，就需要使用原子更新字段类

 

AtomicIntegerFieldUpdater：原子更新整型的字段的更新器

 AtomicLongFieldUpdater：原子更新长整型字段的更新器

 AtomicStampedReference：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题



---

### Java中的并发工具类

countDownLatch、CyclicBarrier、Semaphore、Exchanger

前三者用于并发流量的控制、第四个为在线程间交换数据的一种手段

#### CountDownLatch

它允许一个或多个线程等待其他线程完成操作后，再执行。例如：应用程序的主线程希望在负责启动框架访问的线程已经启动所有服务框架后再启动

内部为Sync继承自AQS的内部类，主要还是在AQS那一套东西上修改而来

~~~java
//构造器传入int类型参数，作为计数器使用，这个值是赋值给了AQS的state变量，主要还是维护这个变量
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
3. 工作中的用法：定义两个countdownlatch

##### 实现原理

![image-20220419213953040](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204192139189.png)

上面的流程，如果落实到代码，把 state置为0的那个线程，会判断head指向节点的状态，如果为通知状态，则唤醒后续节点，即线程3节点，然后head指向线程3节点，head指向的旧节点会被删除掉。当线程3恢复执行后，发现自身为通知状态，又会把head指向线程4节点，然后删除自身节点，并唤醒线程4


将调用await()方法的线程，组装成Node，放在CLH同步队列尾中。一个线程在阻塞前会把它前面的节点设置为通知状态，这样便实现了链式唤醒机制

![image-20220419213822123](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204192138362.png)





#### CyclicBarrier

同步屏障/栅栏：让一组线程到达一个屏障(同步点)时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行

CyclicBarrier(int parties)：参数代表屏障拦截的线程数量

每个线程调用await()表示告诉CyclicBarrier当前线程已经到达屏障，然后当前线程被阻塞

CyclicBarrier 还提供一个更高级的构造函数 CyclicBarrier（int parties，Runnable barrierAction），用于在线程到达屏障时，优先执行barrierAction，方便处理更复杂的业务场景

实现阻塞原理：组合了ReentrantLock

典型应用场景：多线程计算数据，最后合并计算结果的场景

#### CyclicBarrier、CountDownLatch区别

1. countdownlatch计数器只能使用一次、cyclicBarrier可以使用reset()进行重置

2. cyclicBarrier可以处理更复杂的业务场景，例如：计算发生错误，可以重置计数器，并让线程重新执行一次

   

#### Semaphore

用于控制同时访问特定资源的线程数量，用于控制并发线程的数量，它通过协调各个线程，以保证合理的使用公共资源

应用场景：流量控制，特别是公用资源有限的应用场景，例如：数据库连接

底层还是Sync内部类继承AQS



#### Exchanger

线程间交换数据

Exchanger（交换者）是一个用于线程间协作的工具类。Exchanger 用于进行线程间的数据交换

它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据

这两个线程通过 exchange 方法交换数据，如果第一个线程先执行 exchange()方法，它会一直等待第二个线程也执行 exchange 方法，当两个线程都到达同步点时，这两个线程就可以交换数据，将本线程生产出来的数据传递给对方

应用场景：遗传算法、校对工作

比如我们需要将纸制银行流水通过人工的方式录入成电子银行流水，为了避免错误，采用 AB 岗两人进行录入，录入到 Excel 之后，系统需要加载这两个 Excel，并对两个Excel 数据进行校对，看看是否录入一致

如果两个线程有一个没有执行 exchange()方法，则会一直等待，如果担心有特殊情况发生，避免一直等待，可以使用 exchange（V x，longtimeout，TimeUnit unit）设置最大等待时长



---

### 线程池

几乎所有需要异步或并发执行任务的程序都可以使用线程池

高并发情况下，频繁的创建线程会大大降低系统的效率，因为频繁创建和销毁线程需要时间，所以引入了线程池技术

假设一个服务器完成一项任务所需时间为：创建线程时间T1 ，在线程中执行任务的时间T2 ， 销毁线程时间T3。如果：T1 + T3 远大于 T2，则可以采用线程池，以提高服务器性能

线程池：即一个存放线程的容器，调用线程池去执行并发任务时，从线程池中取出线程去执行任务，每个线程执行完任务后，并不被销毁，而是被线程池回收，下一次继续执行任务

#### 使用线程池带来的好处

1. 降低资源消耗：重复利用已创建的线程降低线程创建和销毁造成的消耗
2. 提高响应速度：当任务到达时，任务可以不需要等到线程创建就能立即执行
3. 提高线程的可管理性：线程是稀缺资源，如果无限制地创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一分配、调优和监控

#### 线程池基本组成部分

1. 线程池管理器ThreadPool：用于创建并管理线程池，包括创建线程池、销毁线程池、添加新任务等等
2. 工作线程PoolWorker：线程池中的线程，在没有任务时，处于等待状态，可以循环的执行任务
3. 任务接口Task：每个任务必须实现的接口，以供工作线程调度任务的执行
4. 任务队列taskQueue：存放没有处理的任务。提供一种缓冲机制

#### 线程池的实现原理

当向线程池中提交一个任务后，线程池是如何处理这个任务的呢？线程池处理流程如下

ThreadPoolExcutor执行execute(Runnable r)流程如下：

![image-20220705163327727](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207051633883.png)



![image-20220705165413340](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207051654066.png)





1. 线程池判断 当前工作线程数(worker)是否小于核心线程数
   - 如果是则创建一个工作线程来执行任务（Worker里面创建，由ThreadFactory创建线程）
   - 如果不小于，则转到第二步
2. 判断线程池是否处于运行状态，并且判断任务是否可以加入任务队列
   - 任务队列没有满则将这个任务存储在工作队列里面
   - 满了则进入第三步
3. 线程池判断当前工作线程数是否小于最大线程数
   - （小于则创建一个线程来执行队列里面的任务，此任务放入队列里面）这两个待确认
   - 小于则创建一个线程来执行任务
   - 如果最大线程池里面线程数量已经满了则交给饱和策略来处理这个任务

工作线程：线程池创建线程时，会将线程封装成Worker，Worker在执行完任务后，会循环获取工作队列里的任务来执行。工作队列是一个阻塞队列



#### 向线程池提交任务

execute()和submit()

1. execute方法用于提交不需要返回值的任务，所有无法判断任务是否被线程池执行成功
   - execute()方法参数为Runnable r
2. submit
   - 用于提交需要返回值的任务
   - 线程池会返回一个Future对象，通过这个Future可以判断任务是否执行成功，并且通过future的get()可以获取返回值，get()方法会阻塞当前线程直到任务完成。get(long timeout，TimeUnit unit)方法会阻塞当前线程一段时间后立即返回，这时候任务可能没有执行完



#### 如何优雅关闭线程池

shuwdown: 不接收新任务, 中断空闲线程, 继续处理完线程池中的任务

- 将线程状态设置为Shutdown，然后中断所有没有正在执行任务的线程

shutdownNow: 不接收新任务, 中断运行线程, 空闲线程和处理完正在运行的任务的线程会退出

- 将线程池状态设置为Stop，然后尝试停止所有的线程，并返回等待执行任务的列表

只要调用了这两个关闭方法中的任意一个，isShutdown 方法就会返回 true。当所有的任务都已关闭后，才表示线程池关闭成功，这时调用 isTerminaed 方法会返回 true



使用shutdownNow方法，可能会引起报错，使用shutdown方法可能会导致线程关闭不了。所以当我们使用shutdownNow方法关闭线程池时，一定要对任务里进行异常捕获。

当我们使用shuwdown方法关闭线程池时，一定要确保任务里不会有永久阻塞等待的逻辑，否则线程池就关闭不了

shutdown()/  shutdownNow()+awaitTermination()



#### 如何合理设置线程池容量大小

如何设置：首先根据主机cpu来判断，在考虑具体场景

##### CPU密集型

此时需要大量的计算，在消耗cpu，例如：while操作

此时需要的线程池大小为：cpu核心数+1 即可

##### IO密集型

证明此时需要大量的阻塞，所以需要的线程数量更多

线程池大小一般为：cpu核心数*2 或cpu核心数量/1-阻塞系数(0.8~0.9)

##### 任务的优先级

优先级高、中、低

优先级不同的任务可以放在优先级队列PriorityBlockingQueue里面处理

##### 任务执行的时间

执行时间不同的任务可以交给不同规模的线程池来处理，或者使用优先级队列，让执行时间短的任务先执行

##### 任务的依赖性

是否依赖其他系统资源，比如：数据库连接

依赖于数据库连接池的任务，因为线程提交SQL后需要等待数据库返回结果，等待的时间越长则CPU空闲时间就越长，那么此时线程数应该设置的越大，这样才能更好地利用cpu



#### 线程池的监控

如果在系统中大量使用线程池，则有必要对线程池进行监控，方便在出现问题时，可以根据线程池的使用状况快速定位问题

可以通过线程池提供的参数进行监控，在监控线程池的时候可以使用以下属性

1. taskCount：线程池需要执行的任务数量
2. getPoolSize：线程池的线程数量
3. getActiveCount：获取活动的线程数
4. largestPoolSize：线程池里曾经创建过的最大线程数量。通过这个数据可以知道线程池是否曾经满过。如该数值等于线程池的最大大小，则表示线程池曾经满过

可以通过扩展线程池进行监控

1. 通过继承线程池来自定义线程池，重新线程池的beforeExecute、afterExecute、terminated方法，这几个方法在线程池里是空方法，即没有方法体
2. 也可以在任务执行前、后和线程池关闭前执行一些代码来进行监控
   - 例如：监控任务的平均执行时间、最大执行时间、最小执行时间等等

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



---

### Executor框架

Java的线程即是工作单元，也是执行机制。从JDK5开始，把工作单元于执行机制分离开来

工作单元包括：Runnable、Callable

执行机制由Executor框架提供

#### Excutor框架的两级调度模型

在 HotSpot VM 的线程模型中，Java 线程（java.lang.Thread）被一对一映射为本地操作系统线程。Java 线程启动时会创建一个本地操作系统线程；当该 Java 线程终止时，这个操作系统线程也会被回收。操作系统会调度所有线程并将它们分配给可用的 CPU

在上层，Java 多线程程序通常把应用分解为若干个任务，然后使用用户级的调度器（Executor 框架）将这些任务映射为固定数量的线程；在底层，操作系统内核将这些线程映射到硬件处理器上

两级调度模型如图所示，Executor框架控制上层的调度，下层的调度由操作系统内核控制，下层的调度不受应用程序的控制

![image-20220705203750837](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052038128.png)



#### Executor框架的结构

主要由3大部分组成

1. 任务
   - 包括被执行任务需要实现的接口：Runnable接口、Callable接口
   - 即一个类实现了这个接口复写run或call方法，这个类即将理解为一个任务
2. 任务的执行
   - 包括任务执行机制的核心接口Executor，以及继承自Executor得ExecutorService接口
   - Executor框架有两个关键类实现了ExecutorService接口：ThreadPoolExecutor、ScheduledThreadPoolExecutor
3. 异步计算的结果
   - Future接口、Future接口实现类FutureTask

Executor类与接口示意图：

![image-20220705214122243](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052141415.png)

Executor框架使用示意图：

![image-20220705213818512](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052138986.png)

主线程可以执行get来阻塞获取结果，也可以cancel()来取消任务的执行



#### Executor框架成员

Executor框架主要成员有：ThreadPoolExecutor、ScheduledThreadPoolExecutor、Future接口、Runnable接口、Callable接口和Executors

##### ThreadPoolExecutor

ThreadPoolExecutor通常由工厂类Executors来创建，Executors可以创建3中类型的ThreadPoolExecutor

1. FiexdThreadPool：创建固定的线程数
   - 适用于为了满足资源管理的需求而需要现在当前线程数量的应用场景
   - 一般适用于负载比较重的服务器
2. SingleThreadExecutor：创建单个线程
   - 适用于需要保证顺序地执行各个任务，并且在任意时间点，不会有多个线程是活动的应用场景
3. CachedThreadPoll：根据需要创建新的线程

##### ScheduledThreadPoolExecutor

ThreadPoolExecutor通常由工厂类Executors来创建，Executors可以创建2种类型的ThreadPoolExecutor

1. ScheduledThreadPoolExecutor：包含若干个线程

   - 适用于需要多个线程执行周期性任务，同时满足需要限制线程数量的应用场景

2. SingleThreadScheduledPoolExecutor：只包含一个线程

   - 适用于单个线程执行周期性任务，同时需要保证顺序地执行各个任务的应用场景

   ---

##### Future接口

Future接口和实现Future接口的FutureTask类用来表示异步计算的结果

当我们把Runnable接口、Callable接口的实现类提交给ThreadPoolExecutor时，其会给我们返回一个FutureTask对象



##### Runnable和Callable

Runnable不会返回结果，Callable可以返回结果

可以利用工厂里Executors把一个Runnable包装成一个Callable

~~~java
public static Callable<Object> callable(Runnable task)
~~~

也可以利用Executors把一个Runnable和一个带饭回的结果包装成一个Callable

~~~java
public static <T> Callable<T> callable(Runnable task, T result)
~~~



#### ThreadPoolExecutor

Executor框架中最核心的类，它是线程池的实现类

主要由4个组件构成，构造器四大主要组件：ThreadPoolExecutor(corePool，maxImumPool，BlockingQueue，RejectExceptiionHandler)

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

![image-20220411213851879](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202204112139305.png)



如果任务量不大，可以采用无界队列，如果任务量非常大，需要采用有界队列，防止OOM

如果任务量大，还要求任务都处理成功，则要对提交的任务进行阻塞提交，并且重写拒绝策略，改为阻塞提交。保证不抛弃任何一个任务



##### ScheduledThreadPoolExecutor详解

其继承自ThreadPoolExecutro，主要用来在给定延迟之后运行任务或者定期执行任务

DelayQueue是一个无界队列，所有最大线程池数量没啥效果

ScheduledThreadPoolExecutor的执行主要分为两大部分

1. 调用其scheduledAtFixedRate()或scheduleWithFixedDelay，会向队列里面添加实现了RunnableScheduledFuture接口的ScheduledFutureTask任务
2. 线程池中的线程从DelayQueue中获取任务，然后执行任务

![image-20220705222506561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052225487.png)

ScheduledThreadPoolExecutor为了实现周期性任务，对ThreadPoolExecutor做了如下修改

1. 使用DelayQueue阻塞队列作为任务队列
2. 获取任务的方式不同
3. 执行周期任务后，增加了额外的处理
3. 

##### ScheduledThreadPoolExecutor 的实现

ScheduledThreadPoolExecutor 会把待调度的任务（ScheduledFutureTask）放到一个 DelayQueue 中

ScheduledFutureTask 主要包含 3 个成员变量，如下。

1. long 型成员变量 time，表示这个任务将要被执行的具体时间
2. long 型成员变量 sequenceNumber，表示这个任务被添加到ScheduledThreadPoolExecutor 中的序号
3. long 型成员变量 period，表示任务执行的间隔周期

DelayQueue 封装了一个 PriorityQueue，这个 PriorityQueue 会对队列中的 ScheduledFutureTask 进行排序。排序时，time 小的排在前面（时间早的任务将被先执行）。如果两个 ScheduledFutureTask 的 time 相同，就比较 sequenceNumber，sequenceNumber 小的排在前面（也就是说，如果两个任务的执行时间相同，那么先提交的任务将被先执行）

看 ScheduledThreadPoolExecutor 中的线程执行周期任务的过程：

![image-20220706093828236](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207060938657.png)

1. 线程1从DelayQueue中获取已到期的ScheduledFutureTask，到期任务是指：ScheduledFutureTask的time大于等于当前时间
2. 线程1执行ScheduledFutureTask
3. 线程1修改ScheduledFutureTask的time变量为下次将要被执行的时间
4. 线程1把这个修改time之后的ScheduledFutureTask放回DelayQueue中





---

#### FutureTask

Future接口和实现类FutureTask，代表异步计算的结果

FutureTask实现了RunnableFuture，RunnableFuture继承自Runnable、Future。即FutureTask实现了Runnable与Future接口，因此FutureTask可以交给Executor执行，也可以由调用线程之间执行(FutureTask.run)

##### FutureTask的状态

根据FutureTask.run方法被执行的时机，FutureTask可以处于下面3种状态

1. 未启动：FutureTask.run方法还没有被执行之前
2. 已启动：FutureTask.run方法被执行的过程中
3. 已完成：FutureTask.run方法执行完后正常退出，或被取消(FutureTask.cancel)，或抛出异常而异常结束

FutureTask的状态迁移示意图：

![image-20220705230852940](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052308134.png)



当 FutureTask 处于未启动或已启动状态时，执行 FutureTask.get()方法将导致调用线程阻塞

当 FutureTask 处于已完成状态时，执行 FutureTask.get()方法将导致调用线程立即返回结果或抛出异常



FutureTask的get和cancel方法执行示意图

![image-20220705231054820](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052310964.png)



##### FutureTask使用

当一个线程需要等待另一个线程把某个任务执行完后它才能继续执行，此时可以使用 FutureTask

项目中使用：主线程等待多个线程计算完成后，统计计算结果



##### FutureTask的实现

FutureTask的实现基于AQS，基于AQS的有：ReentrantLock、Semaphore、ReentrantWriteLock、CountDownLatch、FutureTask等等

本质：AQS对FutureTask状态的控制，原理与ReentrantLock阻塞、唤醒类似

每一个基于AQS实现的同步器都会包含两种类型的操作

1. 至少一个acquire操作

   - 这个操作阻塞调用线程，除非/直到AQS的状态允许这个线程执行
   - FutureTask的acquire操作为get()/get(long timeout，TimeUnit unit)

2. 至少一个release操作

   - 这个操作改变 AQS 的状态，改变后的状态可允许一个或

     多个阻塞线程被解除阻塞

   - FutureTask 的 release 操作包括 run()方法和 cancel(…)方法

当执行 FutureTask.get()方法时，如果 FutureTask 不是处于执行完成状态 RAN 或已取消状态 CANCELLED，当前执行线程将到 AQS 的线程等待队列中等待（见下图的线程A、B、C 和 D）。当某个线程执行 FutureTask.run()方法或 FutureTask.cancel（...）方法时，会唤醒线程等待队列的第一个线程

![image-20220705234621981](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/juc_img/202207052346112.png)

假设开始时 FutureTask 处于未启动状态或已启动状态，等待队列中已经有 3 个线程（A、B 和 C）在等待。此时，线程 D 执行 get()方法将导致线程 D 也到等待队列中去等待。

当线程 E 执行 run()方法时，会唤醒队列中的第一个线程 A。线程 A 被唤醒后，首先把自己从队列中删除，然后唤醒它的后继线程 B，最后线程 A 从 get()方法返回。线程B、C 和 D 重复 A 线程的处理流程。最终，在队列中等待的所有线程都被级联唤醒并从get()方法返回



---

### 并发编程实践

#### 生产者和消费者模式

生产者=>阻塞队列  <=消费者

阻塞队列作用：解决生产者和消费者的强耦合问题，生产者与消费者彼此之间不之间通信，而是通过阻塞队列来进行通信，所以生产者生产完数据之后不用等待消费者处理，直接扔给阻塞队列，消费者不找生产者要数据，而是直接从阻塞队列里取，阻塞队列就相当于一个缓冲区，平衡了生产者和消费者的处理能力

大多数设计模式都会找一个第三章出来进行解耦，例如：工程模式的第三者是工厂类、模板模式的第三者是模板类，在学习这些设计模式的时候，先找到这个模式的第三者能快速的熟悉一个设计模式



#### 多生产者与多消费者



#### 线程池与生产者消费者模式

Java的线程池类，其实就是一种生产者和消费者模式的实现，实现方式比普通生产者消费者更加高明

生产者把任务丢给线程池，线程池创建线程并处理任务，如果将要运行的任务数大于线程池的基本线程数就把任务扔到阻塞队列里，这种做法比只使用一个阻塞队列来实现生产者和消费者模式显然要高明很多，因为消费者能够处理直接就处理掉了，这样速度更快，而生产者先存，消费者再取这种方式显然慢一些



使用场景

1. 线程池
2. 文件上传后处理
   - 用户把文件上传到系统后，系统把文件丢到队列里面，然后给用户返回上传成功消息
   - 消费者去队列里面取出文件进行处理
3. 消息队列
4. 等等等



---

### 线上问题定位

由于线上问题不能调试代码，所有线上问题定位就只能看日志、系统状态、dump线程等

ps：好像IDEA可以配置连接到远程环境，不过配置比较繁琐，不推荐

常用工具

1. 服务器中，在linux命令行使用Top命令查看每个进程的情况
2. jstack pid :dump出线程线程信息









### ThreadLocal

线程安全的解决思路

1. 互斥同步：synchronized、Lock
2. 非阻塞同步：CAS、atomic包
3. 无同步方案：ThreadLocal(本地存储)、栈封闭、可重入代码
   - 线程隔离是通过副本保证线程访问资源安全性，他不保证线程之间还存在共享关系的狭义上的安全

ThreadLocal：在每个线程中，对共享变量会创建一个副本，即每个线程内部都会有一个该变量，且在线程内部任何地方都可以使用，线程之间互不影响，这样就不存在线程安全问题，也不会严重影响程序性能

是一个以Thread

#### 如何实现线程隔离(原理)

主要是用到了一个ThreadLocalMap类型的变量threadLocals，负责存储当前线程

##### ThreadLocalMap

1. ThreadLocal的一个静态内部类
2. ThreadLocalMap的Entry实现继承了WeakReference<ThreadLocal<?>>
3. 用一个Entry数组来存储k-v，Entry不是链表形式，而是每个bucket里面放一个Entry
4. 

#### 应用场景

1. 每个线程维护一个序列号
   - 例如：希望某个类将状态(例如用户id、事务id等)与线程关联起来
2. Session的管理
   - 经典例子：mybatis的session
3. 



