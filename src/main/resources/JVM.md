### JVM的主要组成部分以及作用

- 程序执行之前，把Java代码编译为字节码文件（class文件），JVM首先需要把字节码 通过类加载器，把文件加载到运行时数据区。
- 而字节码文件是Jvm的一套指令集规范，不能直接交给底层操作系统去执行，因此需要特定的命令解析器（执行引擎）将字节码翻译成底层系统指令，再交给CPU去执行，执行过程中，需要调用其他语言的接口（本地库接口）来实现整个程序的功能

1. 类加载器
2. 运行时数据区
3. 执行引擎
4. 本地库接口

---

### 运行时数据区

1. 程序计数器
   - 当前线程所执行到的字节码的行号指示器
   - 字节码解析器的工作是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、异常、线程恢复等基础功能都需要依赖这个计数器来完成
   - 线程私有的，每个线程都有独立的程序计数器
   - 异常规定：无，也是Jvm规范中没有规定OOM的区域 OutOfMemoryError
2. Java虚拟机栈
   - 描述的是Java方法执行的内存模型，每个方法在执行的同时都会创建一个"栈帧"，用于存储局部变量表、操作数栈、对象引用、动态链接、方法出口等信息，每个方法从调用直至执行完成的过程，都对应着一个栈帧在虚拟机栈中入栈到出栈的过程
   - 特性：线程私有，什么周期同线程相同
   - 异常规定
     - StackOverflowError：线程请求的栈深度大于虚拟机所允许的栈深度就会抛出该异常
     - OOM：扩展时，无法申请足够的内存就会抛出该异常
3. 本地方法栈
   - 与Java虚拟机栈作用一样，只不过服务于native方法
4. Java堆
   - Java虚拟机中内存最大的一块，所有线程共享，在虚拟机启动时创建，几乎所有的对象实例都在这里分配内存
   - 异常规定：OOM 堆中没有内存完成实例分配，并且堆不可以再扩展时
5. 方法区 / （HostSpot称为永久代 ，方法区的一种实现方式而已） （非堆）
   - 存储已被虚拟机加载的类信息、常量、静态变量、即时编译后的代码等数据
   - 运行时常量池：方法区的一部分，存放编译期生成的各种字面量和符号引用 
     - 字面量： String str ="abc"   abc就是字面量
     - 符号引用  sout(str)  str就是符号引用
6. 直接内存：并不是运行时数据区的一部分，这部分内存被频繁使用可能导致OOM，JDK 1.4中新加入了NIO类，引入了一种基于Channel与缓冲区Buffer的IO方式，它通过一个存储在Java堆中的DirectByteBuffer对象作为这块内存的引用操作，它因此更高效，它避免了Java堆和Native堆来回交换数据的时间

- 线程共享
- ![image-20211125140148249](C:/Users/2521573/AppData/Roaming/Typora/typora-user-images/image-20211125140148249.png)

---

### 堆栈区别



---



### Java中的参数传递时传值呢？还是传引⽤？

### **Java** 对象的⼤⼩是怎么计算的

### 对象的访问定位的两种⽅式

### 判断垃圾可以回收的⽅法有哪些

### 垃圾回收是从哪⾥开始的呢

### 被标记为垃圾的对象⼀定会被回收吗

### **谈谈对** **Java** 中引⽤的了解

### 谈谈对内存泄漏的理解

### 内存泄露的根本原因是什么

### 举⼏个可能发⽣内存泄漏的情况

**尽量避免内存泄漏的⽅法？**

常⽤的垃圾收集算法有哪些



什么是浮动垃圾？**

谈谈你对 **CMS** **垃圾收集器的理解

**G1** 收集器的理



如何利⽤监控⼯具调优





JVM的⼀些参数？**





谈谈你对类加载机制的了解



类加载各阶段的作⽤分别是什么



有哪些类加载器？分别有什么作⽤



类与类加载器的关系



**双亲委派模型的⼯作过程：**



**使⽤双亲委派模型的好处：**





怎么打破双亲委派模型



有哪些实际场景是需要打破双亲委派模型的



谈谈你对编译期优化和运⾏期优化的理解



**HotSpot** **虚拟机要使⽤解释器与编译器并存的架构？**



### Java**内存模型的理解
