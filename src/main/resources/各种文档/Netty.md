## Netty

> Netty是由Jboss提供的一个开源框架
>
> 是一个异步的、基于事件驱动的网络应用框架，用以快速开发高性能、高可靠的网络IO程序
>
> Netty主要针对TCP协议下，面向Clients端的高并发应用，或者Peer to Peer场景下的大量数据持续传输的应用
>
> Netty本质上是一个NIO框架，适用于服务器通讯相关的应用场景，所以NIO需要掌握透彻



应用场景

1. 互联网行业
   - 高性能的RPC框架必不可少，Netty作为异步高性能的通信框架，往往作为基础通信组件被这些RPC框架使用
2. 典型场景
   - Dubbo的RPC框架使用Dubbo协议进行结点间通信
   - dubbo协议默认使用Netty作为基础通信组件，用于实现各进程节点之间的内部通信
3. 游戏行业
4. 大数据领域
   - Hadoop的高性能通信和序列化组件实现的RPC框架，采用了Netty进行跨节点通信



推荐书籍：Netty in action



### NIO

> No-Blocking IO，是面向块或缓冲区编程的



#### NIO三大核心组件

Selector

1. 监听通道的事件
2. 一个Selector对应一个线程
3. 一个Selector对应一个多个Channel
4. 切换到哪个channel，是由事件决定的，Event是一个重要的概念
   - Selector会根据不同的事件，在各个channel上切换

Channel

1. 每一个通道对应一个Buffer
2. channel和buffer都是双向的

Buffer

1. 就是利用它来实现非阻塞的，本质就是一个内存块，底层是数据



数据的读取或写入，都是在buffer上处理的



#### Buffer

> 缓冲区，底层数据结构是数组

在NIO中，Buffer是一个顶层抽象父类，常用的Buffer就是7大类，即除开boolean类型外的其他几种buffer

例如：IntBuffer、**ByteBuffer**最常用的就是ByteBuffer，因为网络传输底层都是以字节传输的