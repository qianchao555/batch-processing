## Redis

/opt  软件位置

默认启动位置/usr/local/bin    里面类型windows 的启动程序

​	busybox-x86_64

​	redis-benchmark:性能测试工具

​	redis-check-aof：修复有问题的aof文件

​	redis-check-rdb：修复有问题的rdb文件

​	redis-cli：客户端，操作入口

​	redis-sentinel：redis集群

​	redis-server：redis服务器启动命令

### 启动

1. 前台启动 不推荐

   - [root@vm-qc-centos bin]#  redis-server

   - 当前窗口关闭后，相当于redis服务就关了

2. 后台启动

   -  cp redis.conf  /etc/redis.conf
   - 防止误操作，在复制的哪个文件里面操作
   - 后台启动设置为yes: dam   yes
   - redis-server /etc/redis.conf
   - 查看进程：ps -ef | grep redis
   - redis-cli  通过客户端访问redis服务

### 关闭

- shutdown
- 找到进程号  kill

---

### 相关知识

1. 端口：6379
2. 默认16个数据库，类似数组下标从0开始，初始默认使用0号数据库

Redis是单线程+多路IO复用技术

### 五大基本数据类型

http:www.reids.cn/commans.html

#### Redis键（key)

#### 字符串String

1. String是Redis最基本的类型，一个key对应一个value
2. 是二进制安全的，意味着Redis的String可以包含任何数据。比如：jpg图片、序列化对象等等
3. 一个Redis中，字符串value最多可以是512M

##### 底层数据结构

1. String的数据结构为**简单动态字符串**
2. 是可以修改的字符串
3. 内部结构类似与Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配

---

#### 列表List

1. 单键多值
2. 是简单的字符串列表，按照插入顺序排列，可以添加一个元素到列表的头部或者尾部
3. 底层：双向链表，对两端的操作性能高、通过索引下标操作中间的节点性能较差
4. 常用命令
   - lpush/rpush <k> <v1> <v2> ...
   - lrang key start stop
   - lpop/rpop 取出一个值，值在键在，值光键亡

##### 底层数据结构

1. List的数据结构为快速链表quickList
2. quickList
   - 列表元素较少的情况下，会使用一块连续的内存存储，这个结构为ziplist，即压缩列表
   - 它将所有的元素紧挨着一起存储，分配的是一块连续的内存
   - 当数据量较多时候，改成quicklist
   - 将链表和ziplist结合起来组成quicklist，也就是将多个ziplist使用双向指针串起来使用。既满足快速插入删除性能，又不会出现太大的空间冗余













