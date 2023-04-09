### Java IO

![image-20230406213715520](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202304062137523.png)

### IO分类

#### 按照数据传输方式分

- 字节流（给计算机看的）
  - InputStream
  - OutputStream
- 字符流（给人看的）
  - Reder
  - Writer
- 字节流和字符流区别
  - 字节流读取单个字节，字符流读取单个字符(一个字符根据编码不同，对应的字节也不同)
  - 字节流用来处理二进制文件(图片、视频文件等)，字符流用于处理文本文件(可以看作特殊的二进制文件，使用了某种编码，人类可以阅读)



#### 从数据操作上分

从数据源或操作对象角度看，IO类可以分为

1. **文件**
   - FileInput/OutputStream
   - FileReader/Writer
2. 数组
   - ByteArrayInput/OutputStream
   - CharArrayInput/OutputStream
3. 管道操作
   - PipedInput/OutputStream
   - PipedReader/Writer
4. 基本类型操作
   - DataInput/OutputStream
5. **缓冲操作**
   - BufferedInput/OutputStream
   - BufferedReader/Writer
6. 打印
   - PrintStream
   - PrintWriter
7. **对象序列化**
   - ObjectInputStream
   - ObjectOutputStream
8. 转换
   - InputStreamReader
   - OutputStreamWriter



### Java IO设计模式

> IO中使用了装饰者模式





### IO常见类

File相关

> 表示文件和目录信息，但是它不表示文件的内容



---







### IO模型-Unix IO模型

参考文章：https://www.nenggz.com/md/java/io/java-io-nio.html

https://blog.csdn.net/yinwenjie/article/details/48522403



一个输入操作通常包括两个阶段：

1. 等待数据准备好
2. 从内核向进程复制数据

对于一个套接字上的输入操作，第一步通常涉及等待数据从网络中到达，当所等待分组到达时，它被复制到内核中的某个缓冲区。第二步就是把数据从内核缓冲区复制到应用进程缓存区



Unix5种IO模型

#### 阻塞式IO

应用程序被阻塞，直到数据复制到应用程序缓冲区才返回

阻塞过程中，其他程序还是可以执行，因此阻塞不意味着整个操作系统被阻塞



#### 非阻塞式IO

应用程序执行系统调用之后，内核返回一个码。应用程序可以继续执行，但是需要不断的执行系统调用来获知I/O是否完成，这种方式称为轮询

由于CPU要处理更多的系统调用，因此这种模型比较低效



#### IO复用(select和poll)

使用select或者poll等待数据，并且可以等待多个套接字的任何一个变为0，这一过程会被阻塞，当某一个套接字可读时返回。之后再使用recvfrom把数据从内核赋值到进程中

它可以让单个进程具有处理多个I/O事件的能力，又被称为Event Driven I/O，即事件驱动I/O

如果一个 Web 服务器没有 I/O 复用，那么每一个 Socket 连接都需要创建一个线程去处理。如果同时有几万个连接，那么就需要创建相同数量的线程。并且相比于多进程和多线程技术，I/O 复用不需要进程线程创建和切换的开销，系统开销更小

##### IO多路复用工作模式

epoll的描述事件有两种触发模式：level trigger、edge trigger

1. LT模式

   - 当epoll_wait检测到描述符事件到达时，将此事件通知进程，进程可以不立即处理该事件，下次调用epoll_wait()会再次通知进程，是默认的一种模式，并且同时支持Blocking和No-Blocking

2. ET模式

   - 和 LT 模式不同的是，通知之后进程必须立即处理事件，下次再调用 epoll_wait() 时不会再得到事件到达的通知。

     很大程度上减少了 epoll 事件被重复触发的次数，因此效率要比 LT 模式高。只支持 No-Blocking，以避免由于一个文件句柄的阻塞读/阻塞写操作把处理多个文件描述符的任务饿死

##### 应用场景

1. select应用场景
   - select 的 timeout 参数精度为 1ns，而 poll 和 epoll 为 1ms，因此 select 更加适用于实时要求更高的场景，比如核反应堆的控制。
   - select 可移植性更好，几乎被所有主流平台所支持
2. poll应用场景
   - poll 没有最大描述符数量的限制，如果平台支持并且对实时性要求不高，应该使用 poll 而不是 select。
   - 需要同时监控小于 1000 个描述符，就没有必要使用 epoll，因为这个应用场景下并不能体现 epoll 的优势。
   - 需要监控的描述符状态变化多，而且都是非常短暂的，也没有必要使用 epoll。因为 epoll 中的所有描述符都存储在内核中，造成每次需要对描述符的状态改变都需要通过 epoll_ctl() 进行系统调用，频繁系统调用降低效率。并且epoll 的描述符存储在内核，不容易调试
3. epoll应用场景
   - 只需要运行在 Linux 平台上，并且有非常大量的描述符需要同时轮询，而且这些连接最好是长连接

#### 信号驱动式IO(SIGIO)

应用进程使用 sigaction 系统调用，内核立即返回，应用进程可以继续执行，也就是说等待数据阶段应用进程是非阻塞的。内核在数据到达时向应用进程发送 SIGIO 信号，应用进程收到之后在信号处理程序中调用 recvfrom 将数据从内核复制到应用进程中

#### 异步IO

进行aio_read系统调用会立即返回，应用程序继续执行，不会被阻塞，内核会在所有操作完成之后向应用程序发生信号

异步 I/O 与信号驱动 I/O 的区别在于，异步 I/O 的信号是通知应用进程 I/O 完成，而信号驱动 I/O 的信号是通知应用进程可以开始 I/O





---

### Java BIO

> Blocking IO：阻塞IO
>
> 应用程序向操作系统请求网络IO操作，这时应用程序会一直等待；另一方面，操作系统收到请求后，也会等待，直到网络上有数据传到监听端口；操作系统在收集数据后，会把数据发送给应用程序；最后应用程序收到数据，并解除等待状态

#### 几个重要概率

- 阻塞IO 和非阻塞IO

  这两个概念是`程序级别`的。**主要描述的是程序请求操作系统IO操作后**，如果IO资源没有准备好，那么程序该如何处理的问题: 前者等待；后者继续执行(并且使用线程一直轮询，直到有IO资源准备好了)

- 同步IO和 非同步IO

  这两个概念是`操作系统级别`的。主要描述的是操作系统在收到程序请求IO操作后，如果IO资源没有准备好，该如何响应程序的问题: 前者不响应，直到IO资源准备好以后；后者返回一个标记(好让程序和自己知道以后的数据往哪里通知)，当IO资源准备好以后，再用事件机制返回给程序



#### 传统BIO的问题

1. 同一时间，服务器只能接收来自一个客户端A的请求消息，虽然客户端A、B是同时进行请求的，但是客户端B发送的请求消息只能等到服务器完成A的请求处理后，才能被接收
2. 由于服务器一次只能处理一个客户端请求，当处理完成并返回后(或者异常时)，才能进行第二次请求的处理。很显然，**这样的处理方式在高并发的情况下，是不能采用的**



#### 多线程-伪异步方式

> 针对上述问题，可能会想到利用多线程技术来解决

![image-20230406231934088](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202304062319418.png)



但是，采用这种多线程方式来解决这个问题，还是有一定的局限性

1. 虽然服务端将请求的处理交给了一个独立的线程，但是操作系统通知accept()的方式还是单个的
   - 也就是：服务器实际接收到数据报文后的业务处理可以多线程，但是，数据报文的接收还是需要一个一个的来
2. 创建多线程的问题
   - 创建的线程越多，cpu切换所需要的时间也就越长，可能真正处理的业务时间还更短
3. 长连接问题
   - 如果使用长连接的话，线程是不会关闭的，容易造成资源消耗导致失控

综上：这种方式不是一个好的办法！



---

### Java NIO

> 弥补了原来BIO的不足，提供了高速的、面向块的IO

主要有三个对象：通道Chnnel、缓冲区Buffer、选择器Selector



标准IO是对字节流的读写，在进行IO之前，会先创建一个流对象，流对象进行读写操作都是按字节，一个字节一个字节的来读或写

NIO把IO抽象成块，类似磁盘的读写，每次IO操作的单位都是一个块，块被读入内存之后就是一个byte[]，NIO一次可以读或写多个字节



#### 流与块

I/O与块最重要的区别是数据打包和传输方式，I/O是以字节流的方式处理数据，NIO是以块的方式处理数据



#### 通道

> 通道Channel是对原I/O包中的流的模拟，可以通过它读取和写入数据
>
> 通道与流的不同之处：流只能在一个方向上移动，而通道是双向的，可以同时读写



通道类型：

1. FileChannel：从文件中读写数据
2. DatagramChannel：通过UDP读写网络中数据
3. SocketChannel：通过TCP读写网络中数据
4. ServerSocketChannel：可以监听新进来的TCP连接，对每一个新进来的连接都会创建一个SocketChannel



#### 缓冲区

发送给一个通道的所有数据都必须首先放到缓冲区中，同样地，从通道中读取的数据都要先读到缓冲区中。即不会直接对通道进行读写数据，都是先经过缓冲区

**缓冲区实质是一个数组，但是它不仅仅是一个数组**。缓冲区提供了对数据的结构化访问，而且还可以跟踪系统的读/写进程

缓冲区类型：

1. ByteBuffer
2. CharBuffer
3. ShortBuffer
4. Int、Long、Float、DoubleBuffer

##### Buffer工作模式

1. 读模式：应用程序只能从缓冲区中读取数据，不能进行写操作
2. 写模式：应用程序可以进行读操作，这就标识可能出现脏读的清空，所有要从buffer中读取数据时，一定要将buffer的状态改为读模式

##### 缓冲区状态变量

1. capacity：最大容量
2. position：当前已经读写的字节数
3. limit：缓冲区最大可以进行操作的位置

![image-20220614231530186](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202206142315296.png)



#### 选择器

NIO常被叫做非阻塞IO，主要是因为NIO在网络通信中的非阻塞特性被广泛使用

**NIO实现了IO多路复用的Reactor模型**，一个线程Thread使用一个选择器selector，通过轮询的方式去监听多个通道**Channel上的事件**，从而让一个线程(selector多路选择器)就能处理多个事件

![image-20230406233302080](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202304062333517.png)



创建线程和切换线程的开销较大，因此使用一个线程来处理多个事件而不是一个线程处理一个事件，具有更好的性能

注意：只有SocketChannel才能配置为非阻塞

~~~java
thread 
    - >selector  ->Channel
    			->Channel
    			->Channel
~~~



##### 创建选择器

~~~java
Selector selector = Selector.open();
~~~

##### 将通道注册到选择器上

~~~java
ssChannel.configureBlocking(false);
ssChannel.register(selector, SelectionKey.OP_ACCEPT);
~~~

通道必须配置为非阻塞式的，否则使用selector就没有任务意义了，因为如果通道在某个事件上被阻塞，那么服务器就不能相应其他事件，必须等待这个事件处理完毕才能处理其他事件，显然这和选择器的作用背道而驰

将通道注册到选择器上时，还需要指定要注册的具体事件

- SelectionKey.OP_CONNECT
- SelectionKey.OP_ACCEPT
- SelectionKey.OP_READ
- SelectionKey.OP_WRITE

~~~java
//SelectionKey的定义
public static final int OP_READ = 1 << 0;
public static final int OP_WRITE = 1 << 2;
public static final int OP_CONNECT = 1 << 3;
public static final int OP_ACCEPT = 1 << 4;
~~~

##### 监听事件

~~~java
int num=selector.select();
~~~

##### 获取到达的事件

~~~java
Set<SelectionKey> keys = selector.selectedKeys();
Iterator<SelectionKey> keyIterator = keys.iterator();
while (keyIterator.hasNext()) {
    SelectionKey key = keyIterator.next();
    if (key.isAcceptable()) {
        // ...
    } else if (key.isReadable()) {
        // ...
    }
    keyIterator.remove();
}
~~~

##### 事件循环

因为一次select()调用不能处理完所有的事件，并且服务器端可能需要一直监听事件，因此服务器端处理数据的代码一般放在一个死循环内

~~~java
while (true) {
    int num = selector.select();
    Set<SelectionKey> keys = selector.selectedKeys();
    Iterator<SelectionKey> keyIterator = keys.iterator();
    while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();
        if (key.isAcceptable()) {
            // ...
        } else if (key.isReadable()) {
            // ...
        }
        keyIterator.remove();
    }
}
~~~





---

###  NIO多路复用详解

哪些框架使用了NIO的多路复用

1. Redis的多路复用程序
2. Netty



#### 典型的多路复用IO实现

目前流行的多路复用IO实现主要有四种：select、poll、epoll、kqueue

多路复用IO技术最适用场景：高并发场景，其他场景下，多路复用IO的技术优势并发挥不出来

高并发：1毫秒内，可能至少同时有上千个连接请求



| IO模型 | 相对性能 | 关键思路         | 操作系统      | JAVA支持情况                                                 |
| ------ | -------- | ---------------- | ------------- | ------------------------------------------------------------ |
| select | 较高     | Reactor          | windows/Linux | 支持,Reactor模式(反应器设计模式)。Linux操作系统的 kernels 2.4内核版本之前，默认使用select；而目前windows下对同步IO的支持，都是select模型 |
| poll   | 较高     | Reactor          | Linux         | Linux下的JAVA NIO框架，Linux kernels 2.6内核版本之前使用poll进行支持。也是使用的Reactor模式 |
| epoll  | 高       | Reactor/Proactor | Linux         | Linux kernels 2.6内核版本及以后使用epoll进行支持；Linux kernels 2.6内核版本之前使用poll进行支持；另外一定注意，由于Linux下没有Windows下的IOCP技术提供真正的 异步IO 支持，所以Linux下使用epoll模拟异步IO |
| kqueue | 高       | Proactor         | Linux         | 目前JAVA的版本不支持                                         |



#### Reactor事件驱动模型

传统IO模型：其主要是一个Server对接N个客户端，在客户端连接之后，为每个客户端都分配一个执行线程

传统IO模型中，由于线程在等待连接以及进行IO操作时都会阻塞当前线程，这部分是损耗非常大的

JDK1.4提供了一套非阻塞IO的API，java NIO。**该API本质上是以事件驱动来处理网络事件**的，而Reactor是基于该API提出的一套IO模型

![image-20220614224250088](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202206142242251.png)

Reactor模型中的四个角色：客户端连接、Reactor、Acceptor、Handler

acceptor会不断的接收客户端的连接，然后将接收到的连接交给Reactor进行分发，最后由Handler进行处理

**相对于传统IO模型的优点**

1. Reactor模型是以事件进行驱动的，其能够将接收客户端连接、网络读和写、业务计算进行拆分，从而极大的提升处理效率
2. Reactor模型是异步非阻塞模型，工作线程在没有网络事件时可以处理其他的任务，而不用像传统IO那样必须阻塞等待

##### Reactor模型--业务处理与IO分离

上面的模型中，由于网络读写和业务操作都在同一个线程中，高并发情况下，这里的系统瓶颈主要有：

1. 高频率的网络读写事件处理
2. 大量的业务操作处理

基于上述问题，在单线程Reactor基础上提出了使用线程池的方式处理业务操作的模型

![image-20220614225807574](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202206142258703.png)

这种模式相较于前面的模式性能有了很大的提升，主要在于在进行网络读写的同时，也进行了业务计算，从而大大提升了系统的吞吐量。

但是这种模式也有其不足，主要在于：网络读写是一个比较消耗CPU的操作，在高并发的情况下，将会有大量的客户端数据需要进行网络读写，此时一个线程将不足以处理这么多请求

#### Reactor模型--并发读写

针对上述问题，提出了使用线程池进行网络读写，而仅仅使用一个线程专门接收客户端连接

![image-20220614230217814](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202206142302960.png)



将Reactor拆分为两个Reactor，mainReactor主要进行客户端连接的处理，完成之后将连接交由subReactor以处理客户端的网络读写。

这里的subReactor则是使用一个线程池来支撑的，其读写能力将会线程数的增加而大大增加。对于业务操作，也是使用了一个线程池，而且每个业务请求都只需要进行编解码和业务计算。通过这种方式，服务器的性能大大提升，基本上可以支撑百万连接

#### Java对多路复用IO的支持

![image-20220614230801898](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javaio_img/202206142308056.png)





#### Java NIO框架 简要设计分析

通过上文的描述，我们知道了多路复用IO技术是操作系统的内核实现。在不同的操作系统，甚至同一系列操作系统的版本中所实现的多路复用IO技术都是不一样的。

那么作为跨平台的JAVA JVM来说如何适应多种多样的多路复用IO技术实现呢? 

面向对象的威力就显现出来了: 无论使用哪种实现方式，他们都会有“选择器”、“通道”、“缓存”这几个操作要素，那么可以为不同的多路复用IO技术创建一个统一的抽象组，并且为不同的操作系统进行具体的实现

#### 多路复用IO优缺点

1. 不用再使用多线程来进行IO处理：针对操作系统内核IO管理模块和应用程序进程而言
2. 同一个端口可以处理多种协议



---

### Java AIO 异步IO

异步IO采用订阅-通知模式，即：应用程序向操作系统注册IO监听，然后继续做自己的事情，当操作系统发生IO事件，并且准备好数据后，再主动通知应用程序，触发相应的函数

windows提供一种异步IO技术：IOCP

Linux使用epoll对异步IO进行模拟





### Java N(A)IO框架-Netty

> Netty是一个高性能、异步事件驱动的NIO框架，提供了对TCP、UDP和文件传输的支持。
>
> 作为当前流行的NIO框架，Netty在互联网领域、大数据分布式计算领域、游戏行业、通信行业等获得了广泛的应用，很多开源组件也基于Netty构建，例如：RPC框架、zookeeper等

Netty由JBoss提供的一个Java开源框架。Netty提供异步的、基于事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠的网络服务器和客户端程序



#### NIO框架

1. 原生Java NIO框架
2. Apache Mina2
3. Netty4、Netty5







