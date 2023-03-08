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

   -  防止误操作，在复制的那个配置文件里面操作
   -  cp redis.conf  /etc/redis.conf
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

### 什么是Redis

[Java全栈网站](https://pdai.tech/md/db/nosql-redis/db-redis-introduce.html)  、 [官网](www.reids.cn) 、推荐书籍：《Redis核心技术与实战》（作者：蒋德钧）

Remote Dictionary Server：远程数据服务



redis是一款基于内存的高速缓存数据库，是一个k-v存储系统。可用于**缓存**、事件发布/订阅、高速队列、**分布式锁**等场景。基于内存，但可持久化。



### 为什么要使用Redis

1. 读写性能优异
   - Redis读的速度大约是11万次/s，写的速度大约是8万次/s
2. 数据类型丰富：redis6中，支持5种基本数据类型
3. 原子性
   - redis索引操作都是原子性的，同时还支持对几个操作合并后原子性的执行
4. 丰富的特性
   - 支持发布/订阅模式、通知、key过期等等特性
5. 持久化：支持rdb、aof持久化方式
5. 分布式集群：Redis Cluster





### Redis使用场景

平时应用redis场景还是相当多的，例如：标黑为项目中用到过的

1. **热点数据的缓存**
   - **缓存是redis最常见的场景应用**，之所以应用是因为其读写性能优异，而且内部是支持事务的，在使用时能保证数据的一致性
   - 作为缓存使用时，一般有两种方式保存数据
     1. 读取前，先读redis，如果没有数据，读取数据库，然后将数据写入redis
     2. 插入数据时，同时写redis
2. **分布式锁**：采用redisson来操作redis。项目是为了保证单个pod执行
   - 主要利用redis的setnx命令进行
   - 因为我们服务器是集群的，定时任务可能在两台机器上都会运行，所以在定时任务中首先 通过setnx设置一个lock， 如果成功设置则执行，如果没有成功设置，则表明该定时任务已执行。当然结合具体业务，我们可以给这个lock加一个过期时间，比如说30分钟执行一次的定时任务，那么这个过期时间设置为小于30分钟的一个时间就可以，这个与定时任务的周期以及定时任务执行消耗时间相关
   - 分布式锁**主要应用在秒杀系统中**
3. **限时业务运用**：自己写的一个接口，一个ip一天最多点击10次这个接口
   - redis在可以利用key的过期时间，到时间后redis会删除它
   - 利用这一特性可以运用在限时优惠、手机验证码等等业务场景
4. 计数器相关问题
   - **redis利用incrby命令可以实现原子性的递增**，可以运用于高并发秒杀活动、分布式序列号的生成、一个接口一分钟限制多少请求、一个接口限制一天多少次调用等等
5. **利用key的特性，实现防止重复提交(实现幂等性)**
6. 延时操作
   - 例如订单产生后占用了库存，10分钟后去检验用户是否真正购买，若没有购买则将该订单设置无效，同时还原库存
7. 排行榜相关问题
   - sortedSet进行热点数据的排序
8. 点赞、好友等相互关系的存储
   - 利用redis集合的一些命令，例如交集、并集、差集等等
9. 简单队列

---



### 五大基本数据类型

https://redis.io/docs/manual/data-types/

redis中的数据都是以key-value形式存储的，所有的key都是字符串，五大数据类型主要指存储值value的数据类型

![image-20220607231156801](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206072311932.png)



#### 字符串String

![image-20220608001010556](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206080010678.png)

1. String是Redis最基本的类型，一个key对应一个value
2. 是二进制安全的，意味着Redis的**String可以包含任何数据。比如：jpg图片、序列化对象等等**
3. 一个Redis中，字符串value大小上限最多可以是512M

##### 底层数据结构

1. String的数据结构为**简单动态字符串**
2. 是可以修改的字符串
3. 内部结构类似与Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配

##### 实战场景

1. 缓存：把常用信息、字符串、图片、视频等等信息放入redis中作为缓存层，mysql作为持久化层，降低mysql的读写压力
2. session：spring session+redis实现session共享





---

#### 列表List

Redis中的List，其实就是链表，Redis用双端链表实现List

![image-20220608000947520](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206080009630.png)

1. 单键多值
2. 是一个简单的字符串列表，按照插入顺序排列，可以添加一个元素到列表的头部(left)或者尾部(right)，也可以从list的头部或者尾部弹出一个元素
3. 底层：双向链表，对两端的操作性能高、通过索引下标操作中间的节点性能较差
4. 常用命令
   - lpush/rpush <k> <v1> <v2> ...
   - lrang key start stop
   - lpop/rpop 取出一个值，值在键在，值光键亡

##### 底层数据结构

双端链表

1. List的数据结构为快速链表quickList
2. quickList
   - 列表元素较少的情况下，会使用一块连续的内存存储，这个结构为ziplist，即压缩列表
   - 它将所有的元素紧挨着一起存储，分配的是一块连续的内存
   - 当数据量较多时候，改成quicklist
   - 将链表和ziplist结合起来组成quicklist，也就是将多个ziplist使用双向指针串起来使用。既满足快速插入删除性能，又不会出现太大的空间冗余

##### 实战场景

1. 微博TimeLine：有人发布微博，用lpush加入时间戳，展示新的列表信息
2. 消息队列





---

#### 集合Set

Redis中，Set是String类型的无序集合，集合成员是唯一的，即：集合中没有重复的数据

![image-20220608164910450](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206081649346.png)

1. set是自动去重的，当需要存储一个列表数据，又不希望出现重复数据时，采用set
1. set中的元素是不可以重复的
1. redis中，集合是通过hash表实现的，所以，添加、删除、查找复杂度都是O(1)

##### 底层数据结构

1. dict字典，dict字典采用哈希表实现 hash表，类似Java的HashSet  底层为HashMap







---

#### 哈希Hash

Redis hash是一个string类型的field(字段)和value(值)的映射表，hash特别适合用于存储对象

![image-20220608165022527](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206081650403.png)

1. hash是一个键值对集合
2. redis hash是一个string类型的filed和value的映射表，hash特别**适用于存储对象**，类似Java的Map<String，Map<string，obj>>

##### 底层数据结构

1. ziplist： field-value长度短、数量少
2. hashtable：field-value数量多

##### 实战场景

缓存：相比String更节省空间





---

#### 有序集合ZSet

1. 有序集合与上述set类似，只是它是有序的
2. 有序集合的每个成员都关联了一个**评分**，这个评分被用来按照从最低分到最高分的方式排序集合中的成员
3. 集合的成员是唯一的，但是评分可以重复
4. 因为元素是有序的，所有可以很快的根据评分或者次序来获取一个范围的元素
4. redis真是通过分数来为集合中的成员进行从小到大的进行排序的

##### 底层数据结构

1. SortedSet
   - 一方面等价于Java的Map<String，Double>可以给每一个元素value赋值一个权值score
   - 一方面又类似与TreeSet，内部的元素会按照权重score进行排序，可以得到每个元素的名次，还可以通过score的范围来获取元素的列表
2. zset使用了两种数据结构
   - hash：关联元素value和权重score
   - 压缩列表ziplist：
   - 跳跃表skiplist ：给元素value排序，根据score的范围获取元素列表



---

### Redis6新数据类型

#### BitMaps

位图结构，每个位置上都是0 或1两种状态

使用场景：

1. 统计用户信息：活跃、不活跃；登录、未登录；打卡、未打卡 等等
2. 大数据量去重：例如Bloom filter布隆过滤器，Bitmap

优势：比如存储员工一年的打卡状态：365天，一天一个bit   ，一年占的空间仅仅365/8=46B；大大的节约空间



---

#### HyperLogLog

基数统计（不重复的元素：基数）

1. 解决基数问题
2. 基数：{1，3，5，7，5，7，8}  基数为5、7  ==》{1，3，5，7，8}
3. 会帮助去重
4. 每个HyperLogLog只需要12KB内存，就可以计算接近2的64次方个不同元素的基数

---

#### Geographic

地理信息

1. Geographic地理信息的缩写
2. 该类型就是元素的2维坐标，在地图上就是经纬度
3. redis基于该类型，提供了经纬度设置、查询、等操作

##### 底层结构

Geographic底层实际为Zset

使用场景：

1. 两地之间的距离
2. 附近的人



#### Stream

Redis5.0加入了Stream数据类型，借鉴了Kafka的设计，是一个新的强大的支持多播的可持久化的消息队列

Stream是对redis实现中消息队列的完善





---

### Redis的发布和订阅

~~~shell
客户端1：订阅频道
127.0.0.1:6379> subscribe channel1
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel1"
3) (integer) 1
hello
1) "message"
2) "channel1"
3) "hello"

1) "message"
2) "channel1"
3) "hello2"

客户端2：向频道发布消息
127.0.0.1:6379> publish channel1 hello
(integer) 1
127.0.0.1:6379> publish channel1 hello2
(integer) 1
127.0.0.1:6379> 


~~~



---



### 配置文件

已经实现拷贝了一份了    /etc/redis.conf

~~~shell
# Redis configuration file example.
#
# Note that in order to read the configuration file, Redis must be
# started with the file path as first argument:
#
# ./redis-server /path/to/redis.conf

# Note on units: when memory size is needed, it is possible to specify
这里定义一些基本的度量单位，只支持字节，不支持其他bit等，大小写不区分
# it in the usual form of 1k 5GB 4M and so forth:
#
# 1k => 1000 bytes
# 1kb => 1024 bytes
# 1m => 1000000 bytes
# 1mb => 1024*1024 bytes
# 1g => 1000000000 bytes
# 1gb => 1024*1024*1024 bytes
#
# units are case insensitive so 1GB 1Gb 1gB are all the same.
包含：当前文件可以包含其他文件
########### INCLUDES ###################


# If instead you are interested in using includes to override configuration
# options, it is better to use include as the last line.
#
# include /path/to/local.conf
# include /path/to/other.conf

##################### MODULES ########

# Load modules at startup. If the server is not able to load modules
# it will abort. It is possible to use multiple loadmodule directives.
#
# loadmodule /path/to/my_module.so
# loadmodule /path/to/other_module.so

网络相关配置
################# NETWORK##########
bind 
#bind 127.0.0.1 -::1   目前只能本地访问

开启保护模式：protected-mode
# protected-mode yes 只能本地访问
远程要访问时：设置为No

tcp-backlog
1.设置tcp的backlog
2.backlog是一个连接队列，backlog队列总和=未完成三次握手队列+已经完成三次握手队列

设置连接redis超时的时间   0永不超时
timeout 0

检测机制：心跳，检测连接是否还活着  =》是否操作了
300s
tcp-keepalive 300


############# GENERAL###################
pidfile:redis每次操作的进程号
pidfile /var/run/redis_6379.pid

日志级别
loglevel：
# debug (a lot of information, useful for development/testing)
# verbose (many rarely useful info, but not a mess like the debug level)
# notice (moderately verbose, what you want in production probably)
# warning (only very important / critical messages are logged)
loglevel notice

日志文件的输出文件
logfile ""




############### SECURITY ##################
密码
 requirepass ''  默认没有
 
##################### CLIENTS #############
最大连接数 默认1w
# maxclients 10000
~~~

---

### Jedis

1. 使用jedis连接远程Redis

2. 注意事项：

   - redis修改bind

   - redis关闭保护模式 protedcted-mode no

   - 开启防火墙firewalld、iptables

   - ~~~shell
     iptables -L -n
     
     iptables -I INPUT 1 -p tcp -m state --state NEW -m tcp --dport 6379 -j ACCEPT
     
     service iptables save
     
     ----------------------
     firewall-cmd --state
     firewall-cmd --permanent --add-port=6379/tcp
     firewall-cmd --permanent --query-port=6379/tcp
     firewall-cmd --permanent --list-ports
     firewall-cmd --reloads
     
     ~~~

   - 腾讯云防火墙添加6379端口

---

### 例子

1. 输入手机号，点击发送随机生成6位数字码，2分钟有效
2. 输入验证码，验证，返回成功或失败
3. 每个手机号每天只能输入三次
4. 解答：jedis.randomauthcode文件代码







---

### Redis进阶-数据结构：对象机制详解

基础类型的底层是如何实现的？

Redis每种对象其实都是redisObject(对象结构)与对应编码的数据结构组合而成

![image-20230307213124704](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202303072131027.png)

上图反应了Redis的每种对象其实都是对象结构redisObject与对应编码的数据结构组合而成，而每种对象类型对应若干编码方式，不同的编码方式所对应的底层数据结构是不同的



---

### Redis事务

**Redis事务的本质是一组命令的集合**，事务支持一次执行多个命令，一个事务中所有命令都会被序列化。事务执行过程中，会按照串行化执行队列中的命令，其他客户端提交的命令请求不会插入到事务执行命令序列中

总结：redis事务就是一次性、顺序性、排他性的执行一个队列中的一系列命令

1. redis事务是一个单独的隔离操作
2. 事务中的所有命令都会序列化、按顺序执行
3. 事务执行过程中，不会被其他客户端发送来的命令请求所打断
4. redis事务的主要作用：串联多个命令、防止别的命令插队



#### redis事务相关命令

1. Multi：开启事务，redis会将后续的命令逐个放入队列中，然后执行exec命令来原子化执行
2. Exec：执行事务中的所有命令
3. Discard ：取消组队，放弃执行事务块中的所有命令
3. watch：监视一个或多个key，如果事务在执行前，这个/些key被其他命令修改，则事务被中断，不会执行事务中的任何命令
3. unwatch：取消watch对key的监视



#### redis事务的错误处理

1. 语法错误（编译器错误）：组队时，有命令出错，exec时，命令都不会执行成功
2. 类型错误（运行时错误）：组队成功，exec执行时，没有错的命令都会成功执行



#### redis事务执行步骤

1. 开启：以Multi开启一个事务
2. 入队：将多个命令入队到事务中，接到这些命令后不会理解执行，而是放到等待执行的事务队列里面
3. 执行：exec触发事务执行，从而去执行命令



#### 为什么redis不支持回滚







#### redis事务冲突问题

##### redis悲观锁

1. 每次去拿数据的时候都认为别人会修改，所以每次拿到数据的时候都会上锁，其他线程拿这个数据时会block，知道获取到锁
2. 缺点：效率低

##### redis乐观锁

1. 每次去拿数据不会加锁，但是更新的时候会判断在此期间其他线程有没有去更新这个数据，采用版本号等机制解决
2. 适用于读多的应用类型，提高吞吐量
3. redis就是利用这种check-and-set机制实现事务的
4. 例如：抢票

#### Watch key [key .....]

1. 在执行Multi之前，先执行watch key1 [key2..]，可以监视一个或多个key
2. 如果在事务执行这个key被其他命令改动，那么事务将被打断
2. 如果有至少一个被 WATCH 监视的键在 EXEC 执行之前被修改了，那么整个事务都会被取消，EXEC 返回 nil-reply 来表示事务已经失败
2. unwatch 取消对key的监控

~~~sql
127.0.0.1:6379> set transactionK1 20
OK
127.0.0.1:6379> watch transactionK1
OK
127.0.0.1:6379> set transactionK1 30
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> set trasactionK1 40   #这里添加了一个键值，并没有修改，注意看单词不一样
QUEUED
127.0.0.1:6379(TX)> exec  #由于有watch,所有事务失败了
(nil)
127.0.0.1:6379> get transationK1
(nil)
127.0.0.1:6379> get transactionK1
"30"

~~~



#### Redis事务三特性

1. 单独的隔离操作
   - 事务中的所有命令都会序列化、按顺序执行
   - 事务在执行的过程中，不会被其他客户端送来的命令打断
2. 没有隔离级别的概念
   - 队列中的命令没有提交前不会被执行
3. 不保证原子性
   - 事务中，如果一条命令执行失败，其后的命令仍然会被执行，没有回滚

#### Redis事务其他实现

1. 基于Lua脚本，redis可以保证脚本内的命令一次性、顺序性的执行，同时也不提供事务运行错误的回滚，执行过程中如果部分命令运行错误，剩下的命令还是会继续运行完
2. 基于中间标记变量，通过另外的标记变量来标识事务是否执行完成，读取数据时先读取该标记变量判断事务是否执行完成

---



### Redis持久化

https://pdai.tech/md/db/nosql-redis/db-redis-x-rdb-aof.html#redis%E6%8C%81%E4%B9%85%E5%8C%96%E7%AE%80%E4%BB%8B

为了防止数据丢失以及服务器重启时能够恢复数据，Redis支持的数据持久化方式分为RDB和AOF

#### 为什么需要持久化？

Redis是个基于内存的数据库。那服务一旦宕机，内存中的数据将全部丢失。

通常的解决方案是从后端数据库恢复这些数据，但后端数据库有性能瓶颈，如果是大数据量的恢复，1、会对数据库带来巨大的压力，2、数据库的性能不如Redis。导致程序响应慢。所以对Redis来说，实现数据的持久化，避免从后端数据库中恢复数据，是至关重要的

#### RDB  

Redis Data Base，中文为**快照/内存持久化**，rdb持久化是把当前**进程数据**生成快照保存到磁盘上的过程，由于是某一时刻的快照，那么快照中的值早于或等于内存中的值

##### 触发方式

rdb持久化方式有两种

1. 手动触发
   - save命令：阻塞当前Redis服务器，直到rdb过程完成为止，对于内存较大的实例会造成长时间阻塞，**生产环境不建议使用**
   - bgsave：Redis进行fork创建出一个子进程，rdb持久化过程由子进程负责，完成后自动结束。阻塞只发生在fork阶段，一般时间很短
   - ![image-20220608204632114](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082046331.png)
   - 大致流程
     1. redis客户端执行bgsave命令或自动触发bgsave命令
     2. 主进程判断当前是否已经存在正在执行的子进程，若存在则主进程直接返回
     3. 若不存在，则fork一个子进程来进行持久化数据，fork的过程是阻塞的，fork操作完成后主进程可执行其他操作
     4. 子进程先将数据写入临时的rdb文件，待快照数据写入完成后再替换原来旧的rdb文件
     5. 同时发送信号给主进程，通知主进程rdb持久化完成，主进程更新相关的统计信息
2. 自动触发
   - 以下4种情况会自动触发rdb持久化操作
   - **redis.conf配置文件中：配置save m n**，即m秒内有n次修改，则自动触发bgsave生成rdb文件
   - 主从复制时，从节点要从主节点进行全量复制时，会触发bgsave，生成当时的快照发送到从节点
   - 执行debgu reload命令，重载redis时，会触发bgsave操作
   - 默认情况下执行shutdown，若没有开启aof持久化，也会触发bgsave操作



##### redis.conf中配置rdb

###### 快照周期

内存快照虽然可以通过手动执行save或bgsave命令，但是生产环境中，大多数情况会设置周期性执行条件

Redis中默认的周期性设置：

~~~shell
# 周期性执行条件的设置格式为
save <seconds> <changes>

# 默认的设置为：
save 900 1		#若900s 内有1个key信息发生变化，则进行快照
save 300 10
save 60 10000

# 以下设置方式为关闭RDB快照功能
save ""
~~~



1. 在指定**时间间隔**内将内存中的**数据集快照**写入磁盘，也就是Snapshot快照，redis重启时，将快照文件读入内存中
2. redis中的快照持久化默认开启的，即rdb方式是默认开启的
4. 备份是如何执行的？

   - Redis会单独fork一个子进程来进行持久化，先将数据写入一个临时文件，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件
   - 整个过程，主进程不进行任务IO操作，确保了极高的性能，如果需要大规模数据的恢复，且数据恢复的完整性不是非常敏感，那么rdb方式比aof方式更加高效
   - 默认文件为dump.rdb文件
   - 启动redis服务时，默认会去读取这个文件
   - 底层实现为：写时复用技术，即Copy-on-write

##### rdb核心思路

多数情况下，生产环境中为Redis开辟的内存区域都比较大（例如6GB），那么将内存中的数据同步到硬盘的过程可能就会持续比较长的时间，而实际情况是同步的这段时间里，Redis服务一般都会收到数据写操作请求。那么如何保证数据一致性？

**copy-on-write：写时复制**，保证在进行快照读操作的这段时间，需要压缩写入到磁盘上的数据在内存中不会发生变化。

在正常的快照操作中，redis主进程会fork一个新的快照进程来负责这个事情，从而保证了redis服务器不会停止对客户端包括写请求在内的任何相应。另一方面，这段时间发生的数据变化，会以副本的方式存在一个新的内存区域，待快照操作结束后，才会同步到原来的内存区域中

##### 快照期间，服务器奔溃怎么处理

在没有将数据全部写入到磁盘前，这次快照操作都不算成功。如果出现了服务崩溃的情况，将**以上一次完整的RDB快照文件作为恢复内存数据的参考**。也就是说，在快照操作过程中不能影响上一次的备份数据。因为Redis服务器会在磁盘上创建一个临时文件进行数据快照操作，待操作成功后才会用这个临时文件替换掉上一次的rdb文件

##### 每秒做一个快照？

这种方法是错误的，虽然bgsave执行时不阻塞主进程，但是频繁的执行全量快照，会带来其他方面的大量开销

1. 频繁将全量数据写入磁盘，磁盘的压力很大，多个快照竞争有限的磁盘带宽，前一个快照还没有写完后一个又开始做，容易造成恶性循环
2. bgsave子进程需要通过fork主进程创建出来，这个fork操作会阻塞主进程，而且主进程的内存越大，阻塞时间越长。若频繁fork出bgsave子进程，就会频繁阻塞主进程

##### rdb优缺点

1. 优势
   - 适合大规模的数据恢复，对数据完整性和一致性要求不高更适合使用
   - 节省磁盘空间：默认采用LZF压缩算法，压缩后的文件体积远远小于内存大小
   - 恢复速度快：redis加载rdb文件恢复数据速度远高于aof方式
2. 缺点
   - 实时性不强，无法做到秒级的持久化，**不适合实时持久化**
   - 每次调用bgsave都需要fork一个子进程 ，fork子进程属于重量级操作，空间膨胀
   - rdb文件是二进制文件，没有可读性，不像aof文件在了解了其结构的情况下可以手动修改或补全
   - **最后一次**（时间间隔内的）持久化后的数据可能丢失

---



#### AOF 

Redis是"写后"日志，即，**先写内存后写日志**，redis先执行命名，把数据写入内存，然后才记录日志。日志里记录的是redis收到的每一条写命令，这些命令以文本形式保存

大多数数据库采用的写前日志，例如MySQL，通过写前日志和两阶段提交，实现数据和逻辑的一致性

Append Of File：采用写后日志，即先写内存、后写日志



##### 为什么采用写后日志

1. **避免额外的检查开销**：redis向aof里面记录日志的时候，并不会先对命令进行语法检查，所有若先记录日志再执行命令的话，日志中就有可能记录了错误的命令，redis在使用日志恢复数据的时候，就会出错
2. **不会阻塞当前的写操作**，但是这种方式会存在潜在风险
   - 若命令执行完成后、写日志之前，服务器宕机了，会丢失数据
   - 主线程写磁盘压力大，导致写磁盘慢，阻塞后续操作



##### 如何实现AOF

aof日志记录redis的每个写命令，步骤分为：命令追加(append)、文件写入(write)、文件同步(sync)

1. 命令追加
   - 当aof持久化功能打开后，服务器在执行完一个写命令后，会以协议格式将被执行的写命令追加到服务器的aof_buf缓冲区
2. 文件写入和同步
   - 何时将aof_buf缓冲区的内容写入aof文件中，redis提供了三种写回策略
   - ![image-20220608220133162](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082201335.png)
   - always：同步写回，即每个写命令执行完后，立马同步地将aof_buf日志写回磁盘
   - **everysec**：**每秒写回，默认策略**，即每个写命令执行完后，只是先把日志写到aof_buf内存缓存区，每隔一秒把缓冲区的内容写入磁盘文件
   - no：操作系统控制的写回，即每个写命令执行完后，先把日志写到aof_buf缓冲区，由操作系统决定时候将缓冲区内容写回磁盘
3. 三种写回策略的优缺点
   - 上述三个写回策略体现了一个原则：trade-off，即取舍、权衡，指**在性能和可靠性之间做取舍**



##### redis.conf中配置aof

~~~shell
# appendonly参数开启AOF持久化,默认是没有开启aof持久化的
appendonly no

# AOF持久化的文件名，默认是appendonly.aof
appendfilename "appendonly.aof"

# AOF文件的保存位置和RDB文件的位置相同，都是通过dir参数设置的
dir ./

# 同步策略,即上述三个写回策略
# appendfsync always
appendfsync everysec
# appendfsync no

# aof重写期间是否同步
no-appendfsync-on-rewrite no

# 重写触发配置
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb  #表示启动AOF文件重写操作的AOF文件最小大小

# 加载aof出错如何处理
aof-load-truncated yes

# 文件重写策略
aof-rewrite-incremental-fsync yes
~~~



1. 以日志的形式来记录每个**写操作**，将Redis执行过的所有写操作指令记录下来（读操作不记录），**只许追加文件但不可改写文件**，redis启动时会读取该文件并重写构造数据
2. 也就是redis重启的话，会根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作
3. Aof默认是不开启的
   - 需要去手动修改redis.cof文件，开启 appendonly yes
   - 默认与dump.rdb都在启动文件目录下
   - 两个备份方式都开启后，redis启动时默认会去读取appenonly.aof文件，而不是dump.rdb
4. 异常恢复
   - 遇到AOF文件损坏，可通过/usr/local/bin/redis-check-aof--fix appendonly.aof进行恢复
   - 备份被写坏的AOF文件
5. Rewrite
   - aof采用文件追加方式，文件会越来越大
   - 加入重写机制，当aof文件的大小超过所设定的阈值时，redis会启动Aof文件内容的压缩，只保留可以恢复数据的最小集合
   - redis会记录上次重写时的AOF文件大小，默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时才触发
   - 可以使用命令bgrewriteaof 进行文件重写
   - 重写原理
     - fork一个进程来重写文件
     - 也是先写临时文件
6. 总结：持久化流程
   - 客户端的请求写命令会被append追加到AOF缓冲区
   - 缓冲区根据AOF持久化策略（always、everysec、no）将操作同步到磁盘的AOF文件中
   - AOF文件大小超过重写策略或手动重写时，会对AOF文件rewrite重写，压缩AOF文件容量
   - redis重启时，会加载AOF文件中的写操作，以达到数据恢复的目的





##### aof文件重写

AOF会记录每个写命令到AOF文件，随着时间越来越长，AOF文件会变得越来越大。如果不加以控制，会对Redis服务器，甚至对操作系统造成影响，而且AOF文件越大，数据恢复也越慢。为了解决AOF文件体积膨胀的问题，**Redis提供AOF文件重写机制来对AOF文件进行压缩**

redis通过创建一个新的aof文件替换现有的aof文件，新旧两个aof文件保存的数据相同，但是新文件没有了冗余命令

![image-20220608222311422](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082223546.png)

###### **aof重写原理**

和rdb类似，后台同样是fork一个bgrewriteaof子进程，fork会把主进程程的内存拷贝一份给bgrewriteaof子进程(通过写时复制机制将数据复制给子进程)，这里面包含了redis数据库的最新数据，然后子进程就可以在不影响主进程的情况下，逐一把拷贝的数据写入重写日志，只会在fork的时候才会阻塞主进程



###### **何时重写**

两个配置项控制aof重写的触发

1. auto-aof-rewrite-percentage 100 ：
   - 这个值配置项表示，当前aof文件大小和上一次重写后aof文件大小的差值/上一次重写后aof文件大小。
   - 即：当前aof文件大小比上一次重写后aof文件的增量大小，和上一次重写后aof文件大小的比值。即默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时才触发
2. auto-aof-rewrite-min-size 64mb  ：表示启动AOF文件重写时，AOF文件的最小大小，默认64M



###### **重写时有新数据写入如何处理**

重写过程总结为：“一个拷贝，两处日志”。

在fork出子进程时的拷贝，以及在重写时，如果有新数据写入，主线程就会将命令记录到两个aof日志内存缓冲区中。如果AOF写回策略配置的是always，则直接将命令写回旧的日志文件，并且保存一份命令至AOF重写缓冲区，这些操作对新的日志文件是不存在影响的。（旧的日志文件：主线程使用的日志文件，新的日志文件：bgrewriteaof进程使用的日志文件）

而在bgrewriteaof子进程完成会日志文件的重写操作后，会提示主线程已经完成重写操作，主线程会将AOF重写缓冲中的命令追加到新的日志文件后面。这时候在高并发的情况下，AOF重写缓冲区积累可能会很大，这样就会造成阻塞，Redis后来通过Linux管道技术让aof重写期间就能同时进行回放，这样aof重写结束后只需回放少量剩余的数据即可。

最后通过修改文件名的方式，保证文件切换的原子性。

在AOF重写日志期间发生宕机的话，因为日志文件还没切换，所以恢复数据时，用的还是旧的日志文件





###### 重写过程中，主线程哪些地方会被阻塞

1. fork子进程时，需要拷贝虚拟页表，会阻塞主进程
2. 重写过程中，主进程有写入数据时，操作系统会创建页面的副本，并拷贝原有的数据，会对主进程阻塞
3. 子进程重写日志完成后，主进程追加aof重写缓冲区时，可能会对主线程阻塞



###### 为什么aof重写不复用原aof日志

1. 父子进程写同一个文件会产生竞争问题，影响父进程的性能
2. 若aof重写过程中失败了，间接的污染了原来的aof文件，无法恢复原有数据

##### aof优缺点

1. 优点

   - 备份机制更稳健，丢失数据概率更低

   - 可读的日志文本，可以操作aof文件，可以处理误操作

2. 缺点

   - 比RDB方式占用更多的空间

   - 备份恢复速度慢

   - 每次读写都同步的话，有一定的性能压力

#### 两种方式总结

1. 官方推荐两种方式都启用
2. 对数据不敏感，可以单独使用RDB
3. 不建议单独使用AOF
4. 如果只做纯内存缓存服务器，可以不使用任何持久化方式

#### RDB+AOF混合方式

内存快照以一定的频率执行，在两次快照之间，使用AOF日志记录这期间所有命令操作

这样一来，快照不用很频繁地执行，这就避免了频繁 fork 对主线程的影响。而且，AOF 日志也只用记录两次快照间的操作，也就是说，不需要记录所有操作了，因此，就不会出现文件过大的情况了，也可以避免重写开销

![image-20220608232830623](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082328732.png)



#### 从持久化中恢复数据

如果一台服务器中既有rdb又有aof，服务器启动的时候加载谁呢？

![image-20220608233023688](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082330807.png)

为什么优先加载aof?

因为aof保存的数据更完整，aof基本上最多损失1s的数据





---

### Redis主从复制

**要避免单点故障，即保证高可用，便需要副本方式来提供集群服务。**

redis提供了主从库模式，以保证数据副本的一致，主从库之间采用读写分离方式

**redis主从复制：将一台redis主服务器的数据，复制到其他的redis服务器，数据的复制是单向的，只能由主节点到从节点**

主机数据更新后，根据配置和策略，自动同步到备机的Master/Slaver机制，Master以写为主，Slave以读为主





#### 主从复制的作用

1. 读写分离，性能扩展
2. 容灾快速恢复：当主节点出现故障后，可以由从节点提供服务，实现快速的故障恢复
3. 负载均衡：在主从复制的基础上，配合读写分离，由主节点提供写服务，从节点提供读服务，即：写redis数据时，应用连接主节点，读redis数据时应用连接从节点，从而分担服务器负载。适用于写少读多的场景，通过多个从节点分担读负载，可以大大提高redis服务器的并发量
4. 主从库之间采用读写分离的方式
   - 读操作：主库、从库都可以接收
   - 写操作：首先到主库执行，然后主库将写操作同步到从库
   - ![image-20220609210402236](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092104175.png)



#### 主从复制原理

redis2.8前只有全量同步复制，2.8版本后有全量和增量同步复制

##### 全量同步复制

比如：第一次同步时

![image-20230308144249417](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202303081442135.png)

启动多个redis实例时，通过构建主从库的关系，按照三个阶段完成数据的第一次同步

1. **第一阶段：主从库将建立连接、协商同步的过程**
   - 主要是为全量做准备，从库与主库建立连接后，告诉主库即将进行同步，主库确认回复后，主从库之间就开始同步了
   - **第一次复制采取全量复制**
2. **第二阶段：主库将数据同步给从库**
   - 从库收到数据后，在本地完成数据加载，这个**过程依赖于内存快照生成的rdb文件**
   - 主库发送rdb文件给从库，从库收到文件后，会先情况当前数据库，然后加载rdb文件
     - 因为从库通过replicaof命令开始和主库同步前，可能保存了其他数据，所以需要清空
   - 主库将数据同步给从库过程中，主库不会被阻塞，仍然可以正常接收请求
     - 但是，这些请求中的写操作并没有记录到刚刚生成的rdb文件中
     - 为了保证主从数据一致性，主库会在内存中开辟一块replication buffer，记录rdb文件生成后，收到的所以写操作
3. **第三阶段：主库把第二阶段执行过程中新收到的写命令，再发送给从库**
   - 主库完成rdb文件发送后，把replication buffer中的修改操作发送给从库，从库重新执行这些操作。
   - 这三个阶段完成后，主从库就实现同步了







##### 增量同步复制

只会把主从库网络断连期间，主库收到的命令，同步给从库

![image-20230308144226826](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202303081442457.png)

如果主库和从库在命令传播的时候出现了网络闪断，那么从库就会和主库重新进行一次全量复制，开销非常大。从Redis2.8开始，网络断了之后，主从库会采用增量复制的方式继续同步



repl_backlog_buffer：

1. 设计目的：从库断开后，如何找到主从之间的数据差异而设计的环形缓冲区，从而避免全量复制带来的性能开销
2. 如果从库断开时间太久，该环形缓冲区被主库的命令覆盖，那么从库只能连上主库后进行一次全量复制。所以该区域尽量配置大一些，可以降低主从复制断网后全量复制的概率
3. 而在repl_backlog_buffer中找到主从差异后，如何发送给从库呢，这就用到了replication buffer



replication bufffer：



##### 异步复制

除去全量复制和增量复制环境下，当主节点每次收到写命令后，先在内部写入数据，反馈给客户端，再异步发现给从节点完成同步





##### 主服务器不进行持久化时，复制的安全性

进行主从复制设置时，强烈建议在主服务器上开启持久化。当不开启持久化时，考虑到其他问题，比如：延迟问题时，应该将实例配置为非自动重启



为什么不持久化的主服务器自动重启非常危险？

1. 设置A节点为主服务器，B、C为从服务器
2. 某个时间点A服务器出现了崩溃，但是Redis具有自动重启系统，重启了进程后，因为关闭了持久化，所以节点重启后只有一个空的数据集
3. B、C节点从A服务器进行复制，此时，导致B、C上数据清空
4. 即使在高可用系统在使用了Redis Sentinel，这种情况也是非常危险的。
   - 比如主服务器可能在很短的时间就完成了重启，以至于Sentinel都无法检测到这次失败，上面的情况就发生了







##### 为什么主从全量复制使用RDB而不是Aof

1. rdb文件内容是经过压缩的二进制文件，文件很小。而aof文件很大，在主从全量数据同步时，传输rdb文件可以尽量降低对主库的网络带宽消耗，从库在加载rdb文件时，一是文件小，读取文件的速度很快，二是rdb文件存储的是二进制数据，从库直接按照rdb协议解析还原数据即可，速度非常快。而aof需要依次重放每个命令这个过程冗长，恢复速度相比rdb慢很多，所有使用rdb进行主从全量复制的成本更低
2. 若使用aof做全量复制，意味着开启aof功能，开启aof就要选择文件刷盘的策略，选择不当会严重影响Redis性能。而Rdb只需要定时备份和主从全量复制数据时才会生成一次快照。而且，在丢失数据不敏感的业务场景，其实是不需要开启Aof的





#### 主从复制下-读写分离中的问题

在主从复制基础上实现的读写分离，可以实现Redis的读负载均衡：由主节点提供写服务，由一个或多个从节点提供读服务（多个从节点既可以提高数据冗余程度，也可以最大化读负载能力）；在读负载较大的应用场景下，可以大大提高Redis服务器的并发量



##### 延迟与不一致问题

由于**主从复制的命令是异步的**，所以，延迟与数据的不一致是不可避免的。这里的一致性指的是强一致性性，是无法满足的。但是，主从复制是可以做到最终一致的



从库滞后执行同步命令的主要情况？

1. 主从库之间的网络可能有传输延迟
2. 从库接收到主库命令后，但是，可能在处理其他复杂度高的命令而阻塞



如何避免主从延迟导致的不一致？

1. 忽略
   - 任何脱离业务的架构设计都是耍流氓，绝大部分业务，例如：百度搜索、淘宝订单、QQ消息都是允许短时间不一致的
   - 即：**如果业务能够接受，最推崇此方案**
2. 尽量保证主从节点间网络连接状况良好，避免主从节点在不同的机房
3. 开发一个程序来监控主从节点间的复制进度
4. redLock：总的来说用的比较少



##### 数据过期问题

单机Redis中，存在两种删除策略

1. 惰性删除：服务器不会主动删除数据，只有当客户端查询某个数据时，服务器判断该数据是否过期，如果过期则删除
2. 定期删除：服务器定时删除过期的数据(默认100ms)，就会随机选出一定数量的数据，检查它们是否过期，并把其中过期的数据删除，这样就可以及时释放一些内存。但是考虑到内存和cpu的折中，该删除的频率和执行时间都受到了限制

主从复制下的情况

主从复制场景下，为了主从节点数据的一致性，**从节点不会主动删除数据**，而是由主节点控制从节点中过期数据的删除。由于主节点的惰性删除和定期删除策略都不能保证主节点及时对过期数据执行删除操作，因此，当客户端通Redis从节点读取数据时，很容易读到已经过期的数据

Redis3.2中，从节点在读取数据时，增加了对数据是否过期的判断，如果该数据已经过期，则不返回给客户端，因此，应用主从集群时，尽量使用Redis3.2以及之后的版本



##### 故障切换问题

在**没有使用哨兵的读写分离场景下**，应用针对读和写分别连接不同的Redis节点；当主节点或从节点出现问题而发生更改时，需要及时修改应用程序读写Redis数据的连接；**连接的切换可以手动进行，或者自己写监控程序进行切换**，但前者响应慢、容易出错，后者实现复杂，成本都不算低

哨兵模式可解决



参考：

使用主从的读写分离前，可以考虑其他方法增加redis的读写负载能力：使用redis集群同时提高读负载和写负载能力。若使用读写分离，可以使用哨兵模式，使得主从节点的故障切换尽可能自动化





#### redis哨兵机制

哨兵模式可以看作是主从模式的高可用版本



redis sentinel：即redis哨兵，2.8版本开始引入。**核心功能是主节点故障后的自动转移**

哨兵：是一个独立的进程，它会独立运行。其原理是通过发送命令，等待Redis服务器响应，从而监控运行的多个Redis实例



下图为典型的：哨兵集群监控逻辑图

![image-20220609214723922](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092147037.png)

##### 哨兵的功能

1. 监控：哨兵会不断地检查主节点和从节点是否运作正常
2. 自动故障转移：当主节点不能正常工作时，哨兵会开始自动故障转移操作，在从节点中选取一个作为新的主节点，并让其他从节点改为复制新的主节点数据
3. 配置提供者：客户端在初始化时，通过连接哨兵来获取当前redis服务的主节点地址
4. 通知：哨兵可以将故障转移的结果发生给客户端

其中监控和自动故障转移使得哨兵可以及时发现主节点故障并完成转移，而配置提供者和通知功能则需要通过与redis客户端的交互中体现



##### 哨兵集群的组建

哨兵实例之间可以相互发现，这个要归功于Redis的发布/订阅机制

![image-20220609215425479](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092154610.png)



##### 哨兵监控Redis库

![image-20220609215600099](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092156209.png)



##### 主库下线判断

1. 主观下线：任何一个哨兵都可以监控探测，并作出redis节点下线的判断
2. 客观下线：**由哨兵集群共同决定redis主节点是否下线**
3. ![image-20220609215806511](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092158623.png)



##### 哨兵集群的选举机制

判断主库下线后，哪个哨兵节点来执行主从切换？



为了避免哨兵的单点情况发生，所以需要一个哨兵的分布式集群。作为分布式集群，必然涉及共识问题（即选举问题）；同时故障的转移和通知都只需要一个主的哨兵节点就可以了

**选举机制**

哨兵的选举机制其实很简单，就是一个**Raft选举算法： 选举的票数大于等于num(sentinels)/2+1时，将成为领导者，如果没有超过，继续选举**

任何一个想成为Leader的哨兵要满足两个条件

1. 拿到半数以上的赞成票
2. 拿到票数的同时，还需要大于等于哨兵配置文件中的quorum值
3. 以 3 个哨兵为例，假设此时的 quorum 设置为 2，那么，任何一个想成为 Leader 的哨兵只要拿到 2 张赞成票，就可以了



> 更进一步理解**判定客观下线** 和 **是否能够主从切换（用到选举机制）** 两个概念
>
> 
>
> 
>
> Redis 1主4从，5个哨兵，哨兵配置quorum为2，如果3个哨兵故障，当主库宕机时，哨兵能否判断主库“客观下线”？能否自动切换？
>
> 1、哨兵集群可以判定主库“主观下线”。由于quorum=2，所以当一个哨兵判断主库“主观下线”后，询问另外一个哨兵后也会得到同样的结果，2个哨兵都判定“主观下线”，达到了quorum的值，因此，哨兵集群可以判定主库为“客观下线”。
>
> 2、但哨兵不能完成主从切换。哨兵标记主库“客观下线后”，在选举“哨兵领导者”时，一个哨兵必须拿到超过多数的选票(5/2+1=3票)。但目前只有2个哨兵活着，无论怎么投票，一个哨兵最多只能拿到2票，永远无法达到N/2+1选票的结果。所以，连哨兵Leader都没有选举出来，不能自动切换







##### 新主库的选出

主库判断客观下线了，如何在从库中选举出新的主库



1. 过滤到不健康的没有回复过哨兵ping相应的从节点
2. 选择salve-priority从节点休闲及最高的
3. 选择复制偏移量最大，只负责最完整的从节点
4. ![image-20220609221819291](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092218518.png)



##### 故障的转移

>  主库选择出来后，就可以进行故障的转移了

![image-20220609221933280](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092219412.png)



![image-20220609221948624](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092219738.png)



1. 将slave-1脱离原从节点（PS: 5.0 中应该是`replicaof no one`)，升级主节点，
2. 将从节点slave-2指向新的主节点
3. 通知客户端主节点已更换
4. 将原主节点（oldMaster）变成从节点，指向新的主节点

![image-20220609222122561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206092221692.png)





### redis集群(redis分片、redis cluster)

Redis cluster是Redis的分布式解决方案。参考文章：[Java全栈Redis集群讲解](https://pdai.tech/md/db/nosql-redis/db-redis-x-cluster.html)



> 虽然主从复制和哨兵机制保证了高可用，就读写分离而言虽然slave从节点扩展了主从的读并发能力，但是写能力和存储能力是无法进行扩展，就只能是master节点能够承载的上限。面对海量数据那么**必然需要构建master(主节点分片)之间的集群**。同时必然需要吸收高可用(主从复制和哨兵机制)能力，即每个master分片节点还需要有slave节点，这是分布式系统中典型的纵向扩展(集群的分片技术)的体现



面临的问题：

1. 机器容量不够，redis如何扩容？
2. 并发写操作，redis如何分摊？
3. 3.0之前通过代理主机、客户端分片、redis sentinel等方案来解决，redis3.0开始后提供了无中心集群配置





#### Redis Cluster提供的功能

1. 数据自动分片
   - 集群中，每个节点都会负责一定数量的slot，每个key会映射到一个具体的slot，通过这种方式就可以找到key具体保存在哪个节点上了
2. 提供hash tags功能
   - 通过hash tag功能可以将多个不同key映射到同一个slot上，这样就能够提供multi-key操作，hash tag的使用的方式是在key中包含“{}”，这样只有在“{...}”中字串被用于hash计算
3. 自动实现转移和手动失效转移
4. 减少硬件成本和运维成本



#### Redis Cluster原理

##### 数据分布

Redis**采用去中心化的设计方案**，通过16384个hash槽位，将每个key映射到一个具体的槽上，而每个redis节点可以负责管理一定数量的槽位。

例如cluster集群中存在三个节点，那么可能存在的一种分配如下：

1. 节点A包含0-5500号哈希槽
2. 节点B包含5501-11000号哈希槽
3. 节点C包含11001-16384号哈希槽



##### Keys hash tags

hash tags提供了一种途径，用来将多个不同的key分配到相同的hash slot中，这样就能够提供multi-key操作。hash tag的使用的方式是在key中包含“{}”，并且只有在“{...}”中字串被用于hash计算



##### Cluster nodes属性

每个节点在cluster中有唯一的名字。



##### 节点通信

1. Gossip协议
2. meet消息
3. ping
4. pong
5. fail
6. 消息格式



##### 扩容

##### 缩容

##### 故障转移

##### 故障发现

##### 故障恢复

##### 请求重定向



1. 什么是集群
   - Redis集群实现了对Redis的水平扩容，即启动N个redis节点，将整个数据库分布存储在这N个节点中，每个节点存储总书记的1/N
   - Redis集群通过分区（partition）来提供一定程度可用性：即使集群有一部分节点失效或者无法通讯，集群也可以继续处理命令请求
2. 分配原则
   - 尽量保证每个主数据库服务器运行在不同的ip地址，每个从库和主库不在一个ip地址上
3. slots
   - 一个Redis集群包含16384个slots，数据库中的每个键都属于这个16384个插槽的其中一个
   - 集群使用公式CRC16(key)% 16384计算出key属于哪个插槽，便放入哪个机器中
4. 故障恢复
   - 主机宕机，从机升级为主机
   - 主机重新启动后，成为当前主机的从机
   - 集群里面的一部分（主机、从机）都宕机了
     - cluster-require-full-coverage yes：整个集群都挂掉
     - cluster-require-full-coverage no：该插槽数据全都不能使用也不能存储
5. 集群的不足
   - 多建操作需要建立 组
   - 多建的Redis事务是不被支持的。lua脚本不被支持









---

### redis缓存

在高并发业务场景下，数据库大多数情况是用户并发访问最薄弱的环节。所以就需要做一个缓冲操作，让请求先访问缓存，而不是直接请求数据库，这样大大缓解了数据库的压力



当想要查询数据时，使用缓存的流程如下

![](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222313989.png)

#### 本地缓存

本地缓存也就是**使用内存中的缓存数据**，可以使用HashMap、数组等数据结构来做缓存

优点：

1. 减少和数据库的交互、降低了磁盘IO引起的性能问题
2. 加快响应速度

缺点：

1. 占用本地内存，机器宕机、重启后缓存丢失
2. 可能存在数据库数据和缓存数据不一致问题
3. 同一个机器的多个微服务缓存的数据不一致
   - ![image-20220322232320544](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222323695.png)
4. 集群环菌下存在缓存的数据不一致
   - ![image-20220322232336902](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222323056.png)

基于本地缓存的问题，引入了分布式缓存Redis







#### 分布式缓存redis

当缓存库出现时，必须考虑如下问题：缓存穿透、缓存击穿、缓存雪崩、缓存污染(或者满了)、缓存和数据库一致性等问题



### 缓存穿透

>  问题来源：redis缓存中和数据库中都没有的数据，而用户不断地发起请求



当查询Redis中没有的数据时，该查询会下沉到数据库层，同时数据库层也没有该数据，当这种情况大量出现或被恶意攻击时，接口的访问全部透过Redis访问数据库，而数据库中也没有这些数据，每次都返回null。如果每次查询都会走数据库，则缓存就失去了意义，这种现象为"缓存穿透"

在流量很大时，可能就会导致DB挂掉，若有人利用不存在的key恶意攻击我们的应用，这就是漏洞

#### 缓存穿透-解决方案：

1. 接口层增加校验。例如：用户鉴权、id等字段的基础校验等等
1. 设置可访问的名单（白名单）
- 使用bitmap定义可以访问的名单，每次访问时进行比较，存在可以进行访问，否则进行拦截，不允许访问
   
- 缺点：每次需要进行比较
2. 采用布隆过滤器-bloom filter

   - 底层是优化的bitmap，用于快速判某个元素是否存在于集合中，其**典型的应用场景就是快速判断一个key是否存在于某容器，不存在就直接返回**。布隆过滤器的关键就在于hash算法和容器大小
3. 进行实时监控

   - 当发现redis的命中率开始急速减低，需要排查访问对象和访问数据，可以进行黑名单限制服务





### 缓存击穿

> 问题来源：**缓存中没有数据，但数据库中有的数据**（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存时又没有读取到数据，导致大量请求直接去读取数据库，引起数据库压力瞬间增大，造成过大压力
>
> 当**某个key**设置了过期时间，此时，该key从缓存内正好失效时，此时大量访问**同时请求这个数据**，就会将查询下沉到数据库层，此时数据库层的负载压力会骤增，这种现象为"缓存击穿"。



**穿透与击穿的区别**：穿透表示底层数据库没有数据且缓存内也没有数据，击穿表示底层数据库有数据而缓存内没有数据



#### 缓存击穿-解决方案

1. 预先设置热门数据
   - 在redis高峰访问之前，把一些热门数据提取存入到redis，**加大这些key的过期时间或设置永不过期**
2. 接口限流、熔断或降级
   - 重要的接口一定要做好限流策略，防止用户恶意刷接口
   - 同时要做好降级准备，当接口中的某些服务不可用时，可以进行熔断，失败快速返回机制
3. 实施调整
   - 实时监控，实施调整key的过期时间
4. 使用互斥锁
   - 大量并发访问这个key时，只让一个请求可以获取到查询数据库的锁，其他请求需要等待，查到以后释放锁，其他请求获取到锁后，先查缓存，缓存中有数据，就不用查数据库





### 缓存雪崩

> 问题来源：缓存中的数据**大批量到了过期时间**，而查询数据量巨大，引起数据库压力过大甚至宕机





缓存雪崩是缓存击穿的"大面积"版，缓存击穿是数据库缓存到Redis内的热点数据失效导致大量并发查询穿过redis直接击打到底层数据库，而缓存雪崩是指Redis中大量的key几乎同时过期，然后大量并发查询穿过redis击打到底层数据库上，此时数据库层的负载压力会骤增，这种现象为"缓存雪崩"。

**事实上缓存雪崩相比于缓存击穿更容易发生**，对于大多数公司来讲，同时超大并发量访问同一个过时key的场景的确太少见了，而大量key同时过期，大量用户访问这些key的几率相比缓存击穿来说明显更大。



#### 解决方案

1. 构建多级缓存架构
   - nginx缓存+redis缓存+其他缓存  等等
2. 使用锁或者队列
   - 不适合高并发场景
3. 设置过期标识更新缓存
   - 记录缓存时间是否过期（设置提前量），如果过期会触发通知另外的线程在后台去更新实际key的缓存
4. **将缓存失效时间分散开，防止同一时间出现大量数据过期现象发生**
   - 例如：在原有的失效时间基础上加一个随机值，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件
5. 设置热点数据永不过期



### 缓存预热

1. 缓存预热如字面意思，当系统上线时，缓存内还没有数据，如果直接提供给用户使用，每个请求都会穿过缓存去访问底层数据库，如果并发大的话，很有可能在上线当天就会宕机，因此我们需要在上线前**先将数据库内的热点数据缓存至Redis内**再提供出去使用，这种操作就成为"缓存预热"。
2. 缓存预热的实现方式
   - 比较通用的方式：是写个批任务，在启动项目时或定时去触发任务，将底层数据库内的热点数据加载到缓存内



### 缓存污染(或满了)

指的是：**缓存中一些只会被访问一次或者几次的数据，被访问后再也访问不到，但是这部分数据依然保存在缓存中，消耗缓存空间**

缓存污染会随着数据的持续增加而逐渐显露，随着服务的不断运行，缓存中会存在大量的永远不会再次被访问的数据。缓存空间是有限的，如果缓存空间满了，再往缓存里写数据时就会有额外开销，影响Redis性能。这部分额外开销主要是指写的时候判断淘汰策略，根据淘汰策略去选择要淘汰的数据，然后进行删除操作



### 最大缓存设置多大

系统的设计选择是一个权衡的过程：大容量缓存是能带来性能加速的收益，但是成本也会更高，而小容量缓存不一定就起不到加速访问的效果。一般来说，**建议把缓存容量设置为总数据量的 15% 到 30%，兼顾访问性能和内存空间开销**

不过，缓存被写满是不可避免的，所以需要数据淘汰策略



### 缓存淘汰策略

Redis支持高达8种淘汰策略，主要可以分为3大类：不淘汰策略、全部数据淘汰策略、对设置了过期时间的数据进行淘汰



1. noeviction：不淘汰
   - **该策略是Redis的默认策略。在这种策略下，一旦缓存被写满了，再有写请求来时，Redis 不再提供服务，而是直接返回错误**
   - **这种策略不会淘汰数据，所以无法解决缓存污染问题。一般生产环境不建议使用**
2. 对设置了过期时间的数据进行淘汰
   - 随机：volatile-random，对设置了过期时间的k-v数据中，进行随机删除
     - 无法把不再访问的数据筛选出来，所以可能依然存在缓存污染的现象
   - ttl：volatile-ttl
     - redis在筛选需要删除的数据时，越早过期的数据越优先被选择
   - lru：volatile-lru
     - 按照最近最少被使用的原则来筛选数据
   - lfu：volatile-lfu
3. 全部数据进行淘汰
   - 随机：allkeys-random
   - lru：allkeys-lru
   - lfu：allkeys-lfu



### 数据库和缓存一致性

读取缓存步骤一般没什么问题，但是一旦涉及到数据更新：数据库和缓存更新时，就容易出现缓存(redis)和数据库间的数据一致性问题

不管是先写数据库再删除redis缓存；还是先删除缓存再写数据库，都有可能出现数据不一致情况。

例如

1. 若删除了缓存redis中的数据，还没来得及写数据库，另一个线程来读取该数据，发现缓存为空，此时读取数据库，并写入缓存，此时造成缓存中为脏数据
2. 若先写数据库，在删除缓存前，写库的线程宕机，此时没有删掉缓存，也会出现数据不一致情况

#### 缓存更新的4中相关模式

https://coolshell.cn/articles/17416.html

##### Cache Aside Pattern

最最常用的一种模式

1. 失效：应用程序先从cache读取数据，没有获取到则从数据库中取数据，成功后，放到缓存中
2. 命中：应用程序从cache中取到数据，取到后返回
3. 更新：先把数据存到数据库中，成功后，再让缓存失效

#### 解决方案

##### 延时双删策略

双删的策略就是保证每次在数据修改的时候去把redis 的数据删除 然后让他去查数据库

1. 在写库前后都进行redis.del(key)操作，并且设置合理的超时时间

   - ~~~java
     //伪代码
     public void write(String key,Object data){
      redis.delKey(key);
      db.updateData(data);
      Thread.sleep(500);
      redis.delKey(key);
      }
     ~~~

2. 具体步骤为

   - 先删除缓存

   - 再写数据库

   - 休眠500毫秒，根据项目的读数据业务逻辑的耗时设置，目的：确保读请求结束，写请求可以删除读操作造成的缓存脏数据

   - 再次删除缓存

3. 设置缓存过期时间

   - 从理论上来说，给缓存设置过期时间，是保证最终一致性的解决方案。所有的写操作以数据库为准，只要到达缓存过期时间，则后面的读请求自然会从数据库中读取新值然后回填缓存

4. 该方案的弊端

   - 结合双删策略+缓存超时设置，这样最差的情况就是在超时时间内数据存在不一致，而且又增加了写请求的耗时

##### 异步更新缓存(基于订阅binlog的同步机制)

1. 整体思路

   MySQL binlog增量订阅消费+消息队列+增量数据更新到redis

   - 读redis：热点数据基本都在redis
   - 写MySQL：增删改都是操作MySQL
   - 更新redis数据：更具MySQL的binlog，来更新到redis

2. redis更新

   - 数据操作分为：全量和增量

     - 全量：将数据一次写入redis
     - 增量：实时更新，这里的增量指的是update、insert、delete变更数据

   - 读取binlog后分析，利用消息队列，推送更新各台的redis缓存数据

     这样一旦 MySQL 中产生了新的写入、更新、删除等操作，就可以把 binlog 相关的消息推送至 Redis，Redis 再根据 binlog 中的记录，对 Redis 进行更新。

     其实这种机制，很类似 MySQL 的主从备份机制，因为 MySQL 的主备也是通过 binlog 来实现的数据一致性。

     这里可以结合使用 canal (阿里的一款开源框架)，通过该框架可以对 MySQL 的 binlog 进行订阅，而 canal 正是模仿了 mysql 的 slave 数据库的备份请求，使得 Redis 的数据更新达到了相同的效果。

     当然，这里的消息推送工具你也可以采用别的第三方：kafka、rabbitMQ 等来实现推送更新 Redis





---

### 基于Redis的分布式锁

目前拆分四个微服务。前端请求进来时，会被转发到不同的微服务。假如前端接收了 10 W 个请求，每个微服务接收 2.5 W 个请求，假如缓存失效了，每个微服务在访问数据库时加锁，通过锁（`synchronzied` 或 `lock`）来锁住自己的线程资源，从而防止`缓存击穿`。

![image-20220322142942618](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/image-20220322142942618.png)

这是一种`本地加锁`的方式，在`分布式`情况下会带来数据不一致的问题：比如服务 A 获取数据后，更新缓存 key =100，服务 B 不受服务 A 的锁限制，并发去更新缓存 key = 99，最后的结果可能是 99 或 100，但这是一种未知的状态，**与期望结果不一致**

基于本地锁的问题，我们需要一种支持**分布式集群环境**下的锁：查询 DB 时，只有一个线程能访问，其他线程都需要等待第一个线程释放锁资源后，才能继续执行

上锁之后，对整个分布式系统中的机器都生效



#### 分布式锁的实现方式

1. 基于数据库分布式锁
   - 通过唯一索引的方式
   - 通过乐观锁的方式
2. 基于缓存（redis等）
3. 基于Zookeeper
4. 每一种分布式锁方案都有各自的优缺点：
   - 性能：redis最高
   - 可靠性：zookeeper最高
5. Redisson



#### 使用Redis实现分布式锁

##### setnx key value

1. set if not exist：当key不存在时，设置key的值，存在时什么都不做
   - 命令：以下两个一样的
   - setnx key value
   - set key value nx   

~~~java
public void getTypeEntityListByRedisDistributedLock(){
    // 1.先抢占锁
    Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "123");
    if(lock) {
      // 2.抢占成功，执行业务
      List<TypeEntity> typeEntityListFromDb = getDataFromDB();
      // 3.解锁
      redisTemplate.delete("lock");
      return typeEntityListFromDb;
    } else {
      // 4.休眠一段时间？为什么？  因为该程序存在递归调用，可能会导致栈空间溢出
      sleep(100);
      // 5.抢占失败，等待锁释放
      return getTypeEntityListByRedisDistributedLock();
    }
}
~~~

- 缺陷：从技术的角度看，setnx 占锁成功，但是业务代码出现异常或者服务器宕机，没有执行删除锁的逻辑，就造成了 死锁
- 如何规避：设置锁的  自动过期时间，过一段时间后，自动删除锁，这样其他线程就能获取到锁了



当业务代码异常或服务宕机，上述可能出现死锁情况，所有加一个自动过期时间

~~~java
// 1.先抢占锁
Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "123");
if(lock) {
    // 2.在 10s 以后，自动清理 lock
    redisTemplate.expire("lock", 10, TimeUnit.SECONDS);
    // 3.抢占成功，执行业务
    List<TypeEntity> typeEntityListFromDb = getDataFromDB();
    // 4.解锁
    redisTemplate.delete("lock");
    return typeEntityListFromDb;
}
~~~



但是占锁和设置过期时间是分两步执行的，在拿到lock之后，设置过期时间之前可能出现异常情况，那么也会导致锁永远不会过期，出现死锁情况

解决方案：原子指令 ，将占锁+设置锁过期时间 放在一步中执行

set key value PX <多少毫秒> NX  或者   set key value EX <多少秒> NX 

setex key value ：是一个原子操作，将key和过期时间两个动作在同一时间完成

set key value nx ex  过期时间秒：例如 set k1 v1 10    nx    =》k1这个键10秒后过期

~~~java
Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "123", 10, TimeUnit.SECONDS);
~~~



存在缺陷：

1. 用户A抢占锁，并且设置了这个锁10秒后自动开锁，锁的编号为123
2. 10秒后，A还在执行任务，此时锁被自动打开了  (过期删除)
3. 用户B抢占锁，锁的编号123，并设置锁的时间10秒  
4. 但是同一时间只允许一个用户执行这个任务，所有用户A和用户B产生了冲突
5. A用户在15秒后完成了任务，此时用户B还在执行任务。A主动打开了编号为123的锁？为什么打开(finaly 方法中unlock），因为锁的编号都叫做 `“123”`，用户 A 只认锁编号，看见编号为 `“123”`的锁就开，结果把用户 B 的锁打开了，此时用户 B 还未执行完任务
6. 此时B还是在执行任务，但是锁已经被打开了。
7. 用户C强制到锁，开始执行任务，此时BC冲突



解决上述问题：给每个锁设置不同的编号

~~~java
// 1.生成唯一 id
String uuid = UUID.randomUUID().toString();
// 2. 抢占锁
Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 10, TimeUnit.SECONDS);
if(lock) {
    System.out.println("抢占成功：" + uuid);
    // 3.抢占成功，执行业务
    List<TypeEntity> typeEntityListFromDb = getDataFromDB();
    // 4.获取当前锁的值
    String lockValue = redisTemplate.opsForValue().get("lock");
    // 5.如果锁的值和设置的值相等，则清理自己的锁
    if(uuid.equals(lockValue)) {
        System.out.println("清理锁：" + lockValue);
        redisTemplate.delete("lock");
    }
    return typeEntityListFromDb;
} else {
    System.out.println("抢占失败，等待锁释放");
    // 4.休眠一段时间
    sleep(100);
    // 5.抢占失败，等待锁释放
    return getTypeEntityListByRedisDistributedLock();
}
~~~



此方案的缺陷：第4步获取锁的值，和第5步比较不是原子性的。

- 时刻：0s。线程 A 抢占到了锁。
- 时刻：9.5s。线程 A 向 Redis 查询当前 key 的值。
- 时刻：10s。锁自动过期。
- 时刻：11s。线程 B 抢占到锁。
- 时刻：12s。线程 A 在查询途中耗时长，终于拿多锁的值。
- 时刻：13s。线程 A 还是拿自己设置的锁的值和返回的值进行比较，值是相等的，清理锁，但是这个锁其实是线程 B 抢占的锁

线程 A 查询锁和删除锁的逻辑不是`原子性`的，所以将查询锁和删除锁这两步作为原子指令操作就可以了

解决方案：Redis+Lua脚本



###### Redis+Lua脚本

我们先来看一下这段 Redis 专属脚本：

```lua
if redis.call("get",KEYS[1]) == ARGV[1]
then
    return redis.call("del",KEYS[1])
else
    return 0
end
```

这段脚本和上面方案的获取key，删除key的方式很像。先获取 KEYS[1] 的 value，判断 KEYS[1] 的 value 是否和 ARGV[1] 的值相等，如果相等，则删除 KEYS[1]。

那么这段脚本怎么在 Java 项目中执行呢？

分两步：先定义脚本；用 redisTemplate.execute 方法执行脚本。

```java
// 脚本解锁
String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
```

上面的代码中，KEYS[1] 对应`“lock”`，ARGV[1] 对应 `“uuid”`，含义就是如果 lock 的 value 等于 uuid 则删除 lock



###### Lua脚本

1. Lua脚本类似Redis事务，有一定的原子性，不会被其他命令插队，可以完成一些redis事务性的操作
2. Redis 2.6以上的版本才能使用
3. 利用lua脚本解决争抢问题，实际上是redis利用单线程的特性，用任务队列的方式解决多任务并发问题

---



#### Redisson

Redisson是一个在Redis的基础上实现的Java驻内存数据网格(In-Memory Data Grid)。它不仅提供了一系列分布式的Java常用对象，还提供了许多分布式服务。

Redisson提供了使用Redis的最简单和最便捷的方法。

Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上



##### Netty框架

Redission采用了基于NIO的Netty框架，不仅能作为Redis底层驱动客户端，还具备提供对Redis各种组态形式的连接功能，对Redis命令能以同步发送、异步形式发送、异步流形式发送、管道形式发送的功能，Lua脚本执行处理，以及处理返回结构的功能



##### 基础数据结构

将原生的Redis String 、List、Set、Hash、ZSet、Geo、HyperLogLog等数据结构封装为Java里的映射

##### 分布式数据结构

在基础上，还提供了分布式的多值映射、本地缓存映射、有序集、队列、阻塞队列、双端队列、阻塞双端队列、阻塞公平队列、延迟队列、**布隆过滤器**、原子整长行、**BitSet**等Redis原本没有的数据结构



##### 分布式锁

Redisson实现了Redis文档中提到的分布式锁Lock。此外，还在分布式锁的基础数还提供了联锁、红锁、读写锁、公平锁、信号量、可过期性信号量、闭锁等对多线程高并发至关重要的基本部件



##### Redisson锁续约   （看门狗机制）

WatchDog

1. 如果负责存储这个分布式锁的Redisson节点宕机，而这个锁正好处于锁住的状态，那么这个锁就会出现锁死的状态。为了避免这种情况发生，Redisson内部提供了一个监控锁的**看门狗**它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。

2. 为了某种场景下保证业务不受影响，如：任务执行超时但未结束，锁已经释放的问题。

   - 当一个线程持有了一把锁，由于并未设置超时时间leaseTime，Redisson默认配置了30S，开启watchDog，每10S对该锁进行一次续约，维持30S的超时时间，直到任务完成再删除锁。也即：在第20s的时候会去重置成30s，以此类推

   

如果我们未制定 lock 的超时时间，就使用 **30 秒作为看门狗的默认时间**。只要占锁成功，就会启动一个定时任务：每隔 10 秒重新给锁设置过期的时间，过期时间为 30 秒

~~~java
@ResponseBody
@GetMapping("test-lock")
public String TestLock() {
    // 1.设置分布式锁、获取锁，只要锁的名字一样，获取到的锁就是同一把锁。
    RLock lock = redisson.getLock("MyLock");

    // 2.加锁、占用锁
    lock.lock();
    try {
        System.out.println("加锁成功，执行后续代码。线程 ID：" +                     Thread.currentThread().getId());
        Thread.sleep(10000);
    } catch (Exception e) {
        //TODO
    } finally {
        lock.unlock();
        // 3.解锁
        System.out.println("Finally，释放锁成功。线程 ID：" + 				    Thread.currentThread().getId());
    }
    return "test lock ok";
}
~~~

上述代码未设置过期时间，所有默认为30s



可以指定leaseTime参数的加锁方法来指定加锁的时间。超过这个时间后锁便自动解开了，不会延长锁的有效期，此时看门狗不会启动

~~~java
lock.lock(15,TimeUnit.SECONDS);
~~~

上述代码为手动设置锁过期时间，如果业务代码执行超过15s，手动释放锁会报错。所有如果设置了锁的自动过期时间，则业务代码执行的时间一定要小于锁的自动过期时间，否则会报错



**缺点**
最大的问题，就是如果你对某个redis master实例，写入了myLock这种锁key的value，此时会异步复制给对应的master slave实例。但是这个过程中一旦发生redis master宕机，主备切换，redis slave变为了redis master。接着就会导致，客户端2来尝试加锁的时候，在新的redis master上完成了加锁，而客户端1也以为自己成功加了锁。此时就会导致多个客户端对一个分布式锁完成了加锁。这时系统在业务上一定会出现问题，导致脏数据的产生。所以这个就是redis cluster，或者是redis master-slave架构的主从异步复制导致的redis分布式锁的最大缺陷：在redis master实例宕机的时候，可能导致多个客户端同时完成加锁



分布式锁和同步器

https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8#84-%E7%BA%A2%E9%94%81redlock



前面分布式锁都是单机版本的。Redis一般都是集群部署的，集群下采取RedLock+Redisson



#### Redisson里面锁的实现

##### 分布式可重入锁(Reentrant Lock)

基于Redis的Redisson分布式可重入锁[`RLock`](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLock.html) Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口

~~~sql
RLock lock = redisson.getLock("anyLock");
// 最常见的使用方法
lock.lock();
~~~

##### 公平锁(Fair Lock)

它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。

所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒

~~~java
RLock fairLock = redisson.getFairLock("anyLock");
// 最常见的使用方法
fairLock.lock();
~~~



##### 联锁(MultiLock)

基于Redis的Redisson分布式联锁RedissonMultiLock对象，将多个Rlock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例

~~~java
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 所有的锁都上锁成功才算成功。
lock.lock();
...
lock.unlock();


//-----------------------------------------------
//另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。

RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
lock.lock(10, TimeUnit.SECONDS);

// 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
...
lock.unlock();
~~~



##### 红锁（RedLock)

RedissonRedLock 继承自RedissonMultiLock

如果线程一在Redis的master节点上拿到了锁，但是加锁的key还没同步到slave节点。恰好这时，master节点发生故障，一个slave节点就会升级为master节点，数据丢失。线程二就可以获取同个key的锁啦，但线程一也已经拿到锁了，锁的安全性就没了

分布式锁算法RedLock可以解决：核心思想是 搞多个Redis master部署，以保证它们不会同时宕掉。并且这些master节点是完全相互独立的，相互之间不存在数据同步。同时，需要确保在这多个master实例上，是与在Redis单实例，使用相同方法来获取和释放锁。

![image-20220322230000332](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222300931.png)



**集群加锁的总体思想是尝试锁住所有节点，当有一半以上节点被锁住就代表加锁成功。集群部署你的数据可能保存在任何一个redis服务节点上，一旦加锁必须确保集群内任意节点被锁住，否则也就失去了加锁的意义**

基于Redis的Redisson红锁RedissonRedLock对象实现了[Redlock](http://redis.cn/topics/distlock.html)加锁算法，该对象也可以用来将多个`RLock`对象关联为一个红锁，每个`RLock`对象实例可以来自于不同的Redisson实例



RedLock的实现步骤：

1. 获取当前时间，以毫秒为单位。
2. 按顺序向5个master节点请求加锁。客户端设置网络连接和响应超时时间，并且超时时间要小于锁的失效时间。（假设锁自动失效时间为10秒，则超时时间一般在5-50毫秒之间,我们就假设超时时间是50ms吧）。如果超时，跳过该master节点，尽快去尝试下一个master节点。
3. 客户端使用当前时间减去开始获取锁时间（即步骤1记录的时间），得到获取锁使用的时间。当且仅当超过一半（N/2+1，这里是5/2+1=3个节点）的Redis master节点都获得锁，并且使用的时间小于锁失效时间时，锁才算获取成功。（如上图，10s> 30ms+40ms+50ms+4m0s+50ms）
4. 如果取到了锁，key的真正有效时间就变啦，需要减去获取锁所使用的时间。
5. 如果获取锁失败（没有在至少N/2+1个master实例取到锁，有或者获取锁时间已经超过了有效时间），客户端要在所有的master节点上解锁（即便有些master节点根本就没有加锁成功，也需要解锁，以防止有些漏网之鱼）。

简化下步骤就是：

1. 按顺序向5个master节点请求加锁
2. 根据设置的超时时间来判断，是不是要跳过该master节点。
3. 如果大于等于3个节点加锁成功，并且使用的时间小于锁的有效期，即可认定加锁成功啦。
4. 如果获取锁失败，解锁！

~~~java
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 红锁在大部分节点上加锁成功就算成功。
lock.lock();
...
lock.unlock();
~~~



##### 读写锁(ReadWriteLock)

分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态

写锁是一个拍他锁（互斥锁），读锁是一个共享锁。

- 读锁 + 读锁：相当于没加锁，可以并发读。
- 读锁 + 写锁：写锁需要等待读锁释放锁。
- 写锁 + 写锁：互斥，需要等待对方的锁释放。
- 写锁 + 读锁：读锁需要等待写锁释放

~~~java
RReadWriteLock rwlock = redisson.getReadWriteLock("anyRWLock");
// 最常见的使用方法
rwlock.readLock().lock();
// 或
rwlock.writeLock().lock();
~~~



##### 信号量(Semaphore)

基于Redis的Redisson的分布式信号量（[Semaphore](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphore.html)）Java对象`RSemaphore`采用了与`java.util.concurrent.Semaphore`相似的接口和用法。同时还提供了[异步（Async）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreAsync.html)、[反射式（Reactive）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreReactive.html)和[RxJava2标准](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreRx.html)的接口

~~~java
RSemaphore semaphore = redisson.getSemaphore("semaphore");
semaphore.acquire();
//或
semaphore.acquireAsync();
semaphore.acquire(23);
semaphore.tryAcquire();
//或
semaphore.tryAcquireAsync();
semaphore.tryAcquire(23, TimeUnit.SECONDS);
//或
semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
semaphore.release(10);
semaphore.release();
//或
semaphore.releaseAsync();
~~~



##### 过期性信号量(PermitExpirableSemphore)

基于Redis的Redisson可过期性信号量（[PermitExpirableSemaphore](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphore.html)）是在`RSemaphore`对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了[异步（Async）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreAsync.html)、[反射式（Reactive）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreReactive.html)和[RxJava2标准](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreRx.html)的接口

~~~java
RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");
String permitId = semaphore.acquire();
// 获取一个信号，有效期只有2秒钟。
String permitId = semaphore.acquire(2, TimeUnit.SECONDS);
// ...
semaphore.release(permitId);
~~~



##### 闭锁(CountDownLatch)

基于Redisson的Redisson分布式闭锁（[CountDownLatch](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RCountDownLatch.html)）Java对象`RCountDownLatch`采用了与`java.util.concurrent.CountDownLatch`相似的接口和用法

~~~java
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.trySetCount(1);
latch.await();

// 在其他线程或其他JVM里
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.countDown();
~~~





### zk分布式锁

![image-20220511205617605](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205112056393.png)



---

### 分布式缓存

#### 项目中怎么用缓存的

#### 为什么要用缓存

1. 提高性能
   - 把一些负责操作耗时的查询出结果后，若后面有很多请求都需要读取，那么可以将结果放进缓存，后面的请求读取缓存就可以了
   - ![image-20220508203048601](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082030789.png)
2. 提高并发
   - ![image-20220508203132273](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082031397.png)

#### 不用行不行

#### 用了缓存后会有什么不良的后果

场景的缓存问题：

1. 缓存与数据库双写不一致
2. 缓存雪崩
3. 缓存穿透
4. 缓存并发竞争



---

### Redis单线程模型

### Redis事件

redis采用事件驱动机制，来处理大量的网络I/O

事件机制分为文件事件和时间事件，Redis中的事件驱动库只关注网络IO，以及定时器

#### 文件事件 file event

用于处理redis服务器和客户端之间的网络IO

redis基于reactor模式开发了网络事件处理器，这个处理器叫做文件事件处理器，file event handler。这个文件事件处理器，是单线程的，redis才叫做单线程的模型，采用IO多路复用机制同时监听多个socket，根据socket上的事件来选择对应的事件处理器来处理这个事件。

单线程主要是指：Redis的网络IO和键值对读写是由一个线程来完成的

单线程的redis为啥这么快？

采用多路复用机制，使其在网络IO操作中能并发处理大量的客户端请求，实现高吞吐率

1. 文件事件处理器

   - 文件事件处理器是单线程模式运行的，但是通过IO多路复用机制监听多个socket，可以实现高性能的网络通信模型，又可以跟内部其他单线程的模块进行对接，保证了redis内部的线程模型的简单性。
   - 文件事件处理器的结构包含4个部分：多个socket，IO多路复用程序，文件事件分派器，事件处理器（命令请求处理器、命令回复处理器、连接应答处理器，等等）。
   - 多个socket可能并发的产生不同的操作，每个操作对应不同的文件事件，IO多路复用程序会监听多个socket，并将socket放入一个队列中排队，每次从队列中取出一个socket给事件分派器，事件分派器把socket给对应的事件处理器。
   - 然后一个socket的事件处理完之后，IO多路复用程序才会将队列中的下一个socket给事件分派器。文件事件分派器会根据每个socket当前产生的事件，来选择对应的事件处理器来处理。
   - ![image-20220613224944811](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206132250669.png)

2. 文件事件

   文件事件：是对套接字socket操作的抽象，如果被监听的socket准备好执行accept、read、write、close等操作的时候，跟该操作对应的文件事件就会产生，这个时候文件事件处理器就会调用之前关联好的事件处理器来处理这个事件。

   - 当socket变得可读时（比如客户端对redis执行write操作，或者close操作），或者有新的可以应答的sccket出现时（客户端对redis执行connect操作），socket就会产生一个AE_READABLE事件。
   - 当socket变得可写的时候（客户端对redis执行read操作），socket会产生一个AE_WRITABLE事件。
   - IO多路复用程序可以同时监听AE_REABLE和AE_WRITABLE两种事件，要是一个socket同时产生了AE_READABLE和AE_WRITABLE两种事件，那么文件事件分派器优先处理AE_REABLE事件，然后才是AE_WRITABLE事件。

3. I/O多路复用程序

   - 负责监听多个socket，并向文件事件分派器传递那些产生了事件的socket
   - 多个文件事件可能并发的出现，但是IO多路复用程序会将所产生的套接字放到一个队列中，然后事件处理器会有序、同步、单个socket的方式处理队列里面的套接字，也就是处理就绪的文件事件

4. 文件事件处理器

   - 如果是客户端要连接redis，那么会为socket关联连接应答处理器
   - 如果是客户端要写数据到redis，那么会为socket关联命令请求处理器
   - 如果是客户端要从redis读数据，那么会为socket关联命令回复处理器

5. redis客户端与服务端通信的一次流程

   - 在redis启动初始化的时候，redis会将连接应答处理器跟AE_READABLE事件关联起来，接着如果一个客户端跟redis发起连接，客户端向服务端发起建立 socket 连接的请求，此时会产生一个AE_READABLE事件，然后由连接应答处理器来处理跟客户端建立连接，创建客户端对应的socket，同时将这个socket的AE_READABLE事件跟命令请求处理器关联起来。
   - 当客户端向redis发起请求的时候（不管是读请求还是写请求，都一样），首先就会在socket产生一个AE_READABLE事件，然后由对应的命令请求处理器来处理。这个命令请求处理器就会从socket中读取请求相关数据，然后进行执行和处理。同时将命令请求处理器与ae_writeable事件关联起来
   - 接着redis这边准备好了给客户端的响应数据之后，就会将socket的AE_WRITABLE事件跟命令回复处理器关联起来，当客户端这边准备好读取响应数据时，就会在socket上产生一个AE_WRITABLE事件，会由对应的命令回复处理器来处理，就是将准备好的响应数据写入socket，供客户端来读取。
   - 命令回复处理器写完之后，就会删除这个socket的AE_WRITABLE事件和命令回复处理器的关联关系。

![image-20220508205702299](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082057463.png)



#### 时间事件

redis服务器中的一些操作，需要在给定的时间点执行，而时间事件就是处理这类定时操作的

定时事件：让程序在指定的时间之后执行一次

周期性事件：让程序每隔指定时间就执行一次

#### aeEventLoop

是整个事件驱动的核心，它管理文件事件和事件事件列表，不断地循环处理着就绪的文件事件和到期的时间事件





**为什么redis单线程模型也能效率这么高实现高并发？**

Redis的瓶颈主要在I/O而不是CPU，在Redis6版本前是单线程模型；其次，Redis的单线程主要指的是Redis的网络I/O和键值对读写是由一个线程来完成的。但是Redis的其他功能，例如持久化、异步删除、集群数据同步等等都是额外的线程执行的

1. 采用了**非阻塞的IO多路复用程序**，使其在网络I/O操作中能并发处理大量客户端请求，实现高吞吐率
2. **纯内存操作**
3. 单线程反而避免了多线程上下文切换问题



---

### Redis过期策略

redis是基于内存的，缓存里面的数据是会过期的，要么自己设置一个过期时间，要么redis自己清理这些数据

1. 设置key过期时间
   - set key value expireTime
2. redis是如何删除key的：定期删除+惰性删除
   - 定期删除：redis默认每100ms随机抽取设置了过期时间的key，检查是否过期，过期就删除。这个随机抽取并没有遍历所有设置了过期时间的key，这样便会导致某些过期key到了时间并没有删除的情况，所有有了惰性删除
   - 惰性删除：获取某个key的时候，redis会检查一下 ，这个key如果设置了过期时间那么是否过期了？如果过期了此时就会删除，不会给你返回任何东西

通过以上两个手段，保证过期的key一定被删除

但是，如果定期删除漏掉了很多过期key，然后也没及时去查这些key，也就没走惰性删除，此时会怎么样？如果大量过期key堆积在内存里，导致redis内存块耗尽了，咋整？

答案是：走内存淘汰机制

#### 内存淘汰机制

redis的内存占用过多的时候，此时会进行内存淘汰，有如下一些策略：

1. noeviction：当内存不足以容纳新写入数据时，新写入操作会报错，这个一般没人用吧，实在是太恶心了
2. allkeys-lru：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的key（这个是最常用的）
   -  redis 10个key，现在已经满了，redis需要删除掉5个key
   -  1个key，最近1分钟被查询了100次
   - 1个key，最近10分钟被查询了50次
   - 1个key，最近1个小时倍查询了1次，这个key会被删除
3. allkeys-random：当内存不足以容纳新写入数据时，在键空间中，随机移除某个key，这个一般没人用吧，为啥要随机，肯定是把最近最少使用的key给干掉啊
4. volatile-lru：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，移除最近最少使用的key（这个一般不太合适）
5. volatile-random：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，随机移除某个key
6. volatile-ttl：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，有更早过期时间的key优先移除





问题1：往redis里面写的数据怎么没了？

答：往redis里面写的数据太多，内存满了。或者触发redis的内存淘汰机制自动清理了一些数据

问题2：数据设置了过期时间，但是到了时间redis中key还存在，还占用着内存？

答：定期删除+惰性删除  



#### 自己实现一个LRU?

思路即可：知道如何利用已有的jdk数据结构实现一个java版的LRU

```java
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    
private final int CACHE_SIZE;
 
    // 这里就是传递进来最多能缓存多少数据
    public LRUCache(int cacheSize) {
        // 这块就是设置一个hashmap的初始大小
        //true:指的是让linkedhashmap按照访问顺序来进行排序，最近访问的放在头，最老访问的就在尾
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true); 
        CACHE_SIZE = cacheSize;
    }
 
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        //当map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据
        return size() > CACHE_SIZE; 
    }
 
}
```






---

### Redis高并发高可用

redis是支撑高并发架构里面，非常重要的，

如何redis通过读写分离承载请求QPS超过10w+

#### redis不能支撑高并发的瓶颈

单机的reids瓶颈

![image-20220508225817298](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082258459.png)

#### 如果redis要支撑超过10w+

读写分离，一般缓存是用来支撑读高并发，写的请求比较少。可能写请求也就一秒钟几千，一两千。大量的请求都是读，一秒钟二十万次读



写多读少：可能就会采用异步写+队列方式了

读写分离架构：

主从架构->读写分离->支撑10w+读QPS的架构

![image-20220508230358292](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082303428.png)





#### Redis主从复制原理

redis replication

master ->slave的数据同步，是采用异步方式的

redis主从架构->读写分离架构->可支持水平扩展的读高并发架构

redis replication的核心机制

（1）redis采用异步方式复制数据到slave节点，不过redis 2.8开始，slave node会周期性地确认自己每次复制的数据量
（2）一个master node是可以配置多个slave node的
（3）slave node也可以连接其他的slave node
（4）slave node做复制的时候，是不会block master node的正常工作的
（5）slave node在做复制的时候，也不会block对自己的查询操作，它会用旧的数据集来提供服务; 但是复制完成的时候，需要删除旧数据集，加载新数据集，这个时候就会暂停对外服务了
（6）slave node主要用来进行横向扩容，做读写分离，扩容的slave node可以提高读的吞吐量







master持久化对于主从架构的安全保障的意义?

如果采用了主从架构，那么建议必须开启master node的持久化！

不建议用slave node作为master node的数据热备，因为那样的话，如果你关掉master的持久化，可能在master宕机重启的时候数据是空的，然后可能一经过复制，salve node数据也丢了

master -> RDB和AOF都关闭了 -> 全部在内存中

master宕机，重启，是没有本地数据可以恢复的，然后就会直接认为自己IDE数据是空的

master就会将空的数据集同步到slave上去，所有slave的数据全部清空

100%的数据丢失

master节点，必须要使用持久化机制

第二个，master的各种备份方案，要不要做，万一说本地的所有文件丢失了; 从备份中挑选一份rdb去恢复master; 这样才能确保master启动的时候，是有数据的

即使采用了后续讲解的高可用机制，slave node可以自动接管master node，但是也可能sentinal还没有检测到master failure，master node就自动重启了，还是可能导致上面的所有slave node数据清空故障



主从复制原理：

![image-20220508231943564](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082319699.png)

1 主从架构的核心原理

当启动一个slave node的时候，它会发送一个PSYNC命令给master node

如果这是slave node重新连接master node，那么master node仅仅会复制给slave部分缺少的数据; 否则如果是slave node第一次连接master node，那么会触发一次full resynchronization

开始full resynchronization的时候，master会启动一个后台线程，开始生成一份RDB快照文件，同时还会将从客户端收到的所有写命令缓存在内存中。RDB文件生成完毕之后，master会将这个RDB发送给slave，slave会先写入本地磁盘，然后再从本地磁盘加载到内存中。然后master会将内存中缓存的写命令发送给slave，slave也会同步这些数据。

slave node如果跟master node有网络故障，断开了连接，会自动重连。master如果发现有多个slave node都来重新连接，仅仅会启动一个rdb save操作，用一份数据服务所有slave node。

2、主从复制的断点续传

从redis 2.8开始，就支持主从复制的断点续传，如果主从复制过程中，网络连接断掉了，那么可以接着上次复制的地方，继续复制下去，而不是从头开始复制一份

master node会在内存中常见一个backlog，master和slave都会保存一个replica offset还有一个master id，offset就是保存在backlog中的。如果master和slave网络连接断掉了，slave会让master从上次的replica offset开始继续复制

但是如果没有找到对应的offset，那么就会执行一次resynchronization

3、无磁盘化复制

master在内存中直接创建rdb，然后发送给slave，不会在自己本地落地磁盘了

repl-diskless-sync
repl-diskless-sync-delay，等待一定时长再开始复制，因为要等更多slave重新连接过来

4、过期key处理

slave不会过期key，只会等待master过期key。如果master过期了一个key，或者通过LRU淘汰了一个key，那么会模拟一条del命令发送给slave。

#### Redis哨兵模式







---

## Redisson

Redisson是一个在Redis的基础上实现的Java驻内存数据网格(In-Memory Data Grid)。它不仅提供了一系列分布式的Java常用对象，还提供了许多分布式服务。

Redisson提供了使用Redis的最简单和最便捷的方法。

Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上



### Netty框架

Redission采用了基于NIO的Netty框架，不仅能作为Redis底层驱动客户端，还具备提供对Redis各种组态形式的连接功能，对Redis命令能以同步发送、异步形式发送、异步流形式发送、管道形式发送的功能，Lua脚本执行处理，以及处理返回结构的功能

### 配置

1. 编程式配置：通过Config对象实例来执行

2. 声明式配置：通过Json或Yaml文件中加载

   - ~~~java
     Config config = Config.fromYAML(new File("config-file.yaml"));  
     RedissonClient redisson = Redisson.create(config);
     ~~~



### 基础数据结构

将原生的Redis String 、List、Set、Hash、ZSet、Geo、HyperLogLog等数据结构封装为Java里的映射



### 分布式对象

每个Redisson对象都绑定到一个Redis键，且可以通过getName()读取

~~~java
RMap redissonMap=redisson.getMap("myMap");
redissonMap.getName();  //myMap
~~~

所有和Redis键相关的操作，都被抽象到了RKeys接口

~~~java
RKeys keys = redisson.getKeys();
Iterable<String> allKeys = keys.getKeys();
Iterable<String> foundedKeys = keys.getKeysByPattern('key*');
long numOfDeletedKeys = keys.delete("obj1", "obj2", "obj3");
long deletedKeysAmount = keys.deleteByPattern("test?");
String randomKey = keys.randomKey();
long keysAmount = keys.count();
~~~



#### Object

Redisson分布式的RBucket对象，可用作任意类型对象的通用容器

~~~java
RBucket<AnyObject> bucket = redisson.getBucket("anyObject");
bucket.set(new AnyObject(1));
AnyObject obj = bucket.get();
bucket.trySet(new AnyObject(3));
bucket.compareAndSet(new AnyObject(4), new AnyObject(5));
bucket.getAndSet(new AnyObject(6));
~~~



#### BitSet

RBitSet对象具有类似与java.util.Bitset的结构，且表示的位向量会根据需要增长。BitSet的大小由Redis限制位 4294967295

~~~java
RBitSet set = redisson.getBitSet("simpleBitset");
set.set(0, true);
set.set(1812, false);
set.clear(0);
set.addAsync("e");
set.xor("anotherBitset");
~~~

#### AtomicLong、AtomicDouble

RAtomicLong类似与java.util.concurrent.atomic.AtomicLong对象的结构

#### Topic

分布式的RTopic实现了发布/订阅机制



#### Bloom filter

Redis4.0版本中提供的新功能，它被作为插件加载到Redis服务器中，给Redis提供强大的去查功能

相比于Set集合的去查功能而言，布隆过滤器在空间上能节约90%以上，但是它的不足之处在于去重率大约在99%左右，即存在1%左右的误判率。这种误差是由布隆过滤器的自身结构决定的，但是这种误差在处理海量数据时几乎可以忽略

应用场景：

1. 海量数据去重
2. 公司kafka去重

##### 工作原理

Bloom Filter是一个高空间利用率的概率性数据结构，由二进制向量(即位数组)（类似于Bitset)和一系列随机映射函数(哈希函数)两部分组成

布隆过滤器使用exists()来判断某个元素是否存在与自身结构中，当布隆过滤器判定某个值存在时，其实是值这个值可能存在，当它说某个值不存在时，那这个值肯定不存在，这个误判率大约在1%及更小

![image-20220606231417166](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206062314801.png)

**添加元素**

当使用布隆过滤器添加 key 时，会使用不同的 hash 函数对 key 存储的元素值进行哈希计算，从而会得到多个哈希值。根据哈希值计算出一个整数索引值，将该索引值与位数组长度做取余运算，最终得到一个位数组位置，并将该位置的值变为 1。每个 hash 函数都会计算出一个不同的位置，然后把数组中与之对应的位置变为 1。通过上述过程就完成了元素添加(add)操作

**判断元素是否存在**

当我们需要判断一个元素是否存时，其流程如下：首先对给定元素再次执行哈希计算，得到与添加元素时相同的位数组位置，判断所得位置是否都为 1，如果其中有一个为 0，那么说明元素不存在，若都为 1，则说明元素有可能存在

**为什么是可能存在？**

那些被置为1的位置可能是由于其他元素的操作而改变的。例如：元素1和元素2同时将某个位置变为1，如上图所示，这种情况就不能判断元素1 一定存在，这就是误判的根本原因

分布式的RBloomFilter对象

~~~java
RBloomFilter<SomeObject> bloomFilter = redisson.getBloomFilter("sample");
// initialize bloom filter with 
// expectedInsertions = 55000000
// falseProbability = 0.03
bloomFilter.tryInit(55000000L, 0.03);
bloomFilter.add(new SomeObject("field1Value", "field2Value"));
bloomFilter.add(new SomeObject("field5Value", "field8Value"));
bloomFilter.contains(new SomeObject("field1Value", "field8Value"));
~~~



布隆过滤器使用时需要单独安装

**常用命令**

| 命令       | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| bf.add     | 只能添加元素到布隆过滤器。                                   |
| bf.exists  | 判断某个元素是否在于布隆过滤器中。                           |
| bf.madd    | 同时添加多个元素到布隆过滤器。                               |
| bf.mexists | 同时判断多个元素是否存在于布隆过滤器中。                     |
| bf.reserve | 以自定义的方式设置布隆过滤器参数值，共有 3 个参数分别是 key、error_rate(错误率)、initial_size(初始大小)。 |



**解决错误率过高问题**

bf.reserve()

~~~java
key  #指定存储元素的键，若已经存在,则bf.reserve会报错
error_rate=0.01 #表示错误率
initial_size=100 #表示预计放入布隆过滤器中的元素数量
~~~

当放入过滤器中的元素数量超过了 initial_size 值时，错误率 error_rate 就会升高。因此就需要设置一个较大 initial_size 值，避免因数量超出导致的错误率上升

错误率越低，所需要的空间也会越大，因此就需要我们尽可能精确的估算元素数量，避免空间的浪费。我们也要根据具体的业务来确定错误率的许可范围，对于不需要太精确的业务场景，错误率稍微设置大一点也可以。

注意：如果要使用自定义的布隆过滤器需要在 add 操作之前，使用 bf.reserve 命令显式地创建 key，格式如下：

```
client.execute_command("bf.reserve", "keyname", 0.001, 50000)
```



**与其他数据结构的区别**

布隆过滤器相比于平时常用的的列表、散列、集合等数据结构，

1. 其占用空间更少、效率更高
2. 但缺点就是返回的结果具有概率性，并不是很准确。在理论情况下，添加的元素越多，误报的可能性就越大。再者，存放于布隆过滤器中的元素不容易被删除，因为可能出现会误删其他元素情况

---



### 分布式集合

在基础上，还提供了分布式的多值映射、本地缓存映射、有序集、队列、阻塞队列、双端队列、阻塞双端队列、阻塞公平队列、延迟队列、**布隆过滤器**、原子整长行、**BitSet**等Redis原本没有的数据结构



---



### 分布式锁

Redisson分布式可重入锁，实现了java.util.concurrent.locks.Lock接口，并且支持TTL

Redisson实现了Redis文档中提到的分布式锁Lock。此外，还在分布式锁的基础数还提供了联锁、红锁、读写锁、公平锁、信号量、可过期性信号量、闭锁等对多线程高并发至关重要的基本部件



### Redisson锁续约   （看门狗机制）

WatchDog

1. 如果负责存储这个分布式锁的Redisson节点宕机，而这个锁正好处于锁住的状态，那么这个锁就会出现锁死的状态。为了避免这种情况发生，Redisson内部提供了一个监控锁的**看门狗**它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。

2. 为了某种场景下保证业务不受影响，如：任务执行超时但未结束，锁已经释放的问题。

   - 当一个线程持有了一把锁，由于并未设置超时时间leaseTime，Redisson默认配置了30S，开启watchDog，每10S对该锁进行一次续约，维持30S的超时时间，直到任务完成再删除锁。也即：在第20s的时候会去重置成30s，以此类推

   

如果我们未制定 lock 的超时时间，就使用 **30 秒作为看门狗的默认时间**。只要占锁成功，就会启动一个定时任务：每隔 10 秒重新给锁设置过期的时间，过期时间为 30 秒

~~~java
@ResponseBody
@GetMapping("test-lock")
public String TestLock() {
    // 1.设置分布式锁、获取锁，只要锁的名字一样，获取到的锁就是同一把锁。
    RLock lock = redisson.getLock("MyLock");

    // 2.加锁、占用锁
    lock.lock();
    try {
        System.out.println("加锁成功，执行后续代码。线程 ID：" +                     Thread.currentThread().getId());
        Thread.sleep(10000);
    } catch (Exception e) {
        //TODO
    } finally {
        lock.unlock();
        // 3.解锁
        System.out.println("Finally，释放锁成功。线程 ID：" + 				    Thread.currentThread().getId());
    }
    return "test lock ok";
}
~~~

上述代码未设置过期时间，所有默认为30s



可以指定leaseTime参数的加锁方法来指定加锁的时间。超过这个时间后锁便自动解开了，不会延长锁的有效期，此时看门狗不会启动

~~~java
lock.lock(15,TimeUnit.SECONDS);
~~~

上述代码为手动设置锁过期时间，如果业务代码执行超过15s，手动释放锁会报错。所有如果设置了锁的自动过期时间，则业务代码执行的时间一定要小于锁的自动过期时间，否则会报错



**缺点**
最大的问题，就是如果你对某个redis master实例，写入了myLock这种锁key的value，此时会异步复制给对应的master slave实例。但是这个过程中一旦发生redis master宕机，主备切换，redis slave变为了redis master。接着就会导致，客户端2来尝试加锁的时候，在新的redis master上完成了加锁，而客户端1也以为自己成功加了锁。此时就会导致多个客户端对一个分布式锁完成了加锁。这时系统在业务上一定会出现问题，导致脏数据的产生。所以这个就是redis cluster，或者是redis master-slave架构的主从异步复制导致的redis分布式锁的最大缺陷：在redis master实例宕机的时候，可能导致多个客户端同时完成加锁



分布式锁和同步器

https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8#84-%E7%BA%A2%E9%94%81redlock



前面分布式锁都是单机版本的。Redis一般都是集群部署的，集群下采取RedLock+Redisson



#### Redisson里面锁的实现

##### 分布式可重入锁(Reentrant Lock)

基于Redis的Redisson分布式可重入锁[`RLock`](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLock.html) Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口

~~~sql
RLock lock = redisson.getLock("anyLock");
// 最常见的使用方法
lock.lock();
~~~

##### 公平锁(Fair Lock)

它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。

所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒

~~~java
RLock fairLock = redisson.getFairLock("anyLock");
// 最常见的使用方法
fairLock.lock();
~~~



##### 联锁(MultiLock)

基于Redis的Redisson分布式联锁RedissonMultiLock对象，将多个Rlock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例

~~~java
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 所有的锁都上锁成功才算成功。
lock.lock();
...
lock.unlock();


//-----------------------------------------------
//另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。

RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
lock.lock(10, TimeUnit.SECONDS);

// 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
...
lock.unlock();
~~~



##### 红锁（RedLock)

RedissonRedLock 继承自RedissonMultiLock

如果线程一在Redis的master节点上拿到了锁，但是加锁的key还没同步到slave节点。恰好这时，master节点发生故障，一个slave节点就会升级为master节点，数据丢失。线程二就可以获取同个key的锁啦，但线程一也已经拿到锁了，锁的安全性就没了

分布式锁算法RedLock可以解决：核心思想是 搞多个Redis master部署，以保证它们不会同时宕掉。并且这些master节点是完全相互独立的，相互之间不存在数据同步。同时，需要确保在这多个master实例上，是与在Redis单实例，使用相同方法来获取和释放锁。

![image-20220322230000332](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222300931.png)



**集群加锁的总体思想是尝试锁住所有节点，当有一半以上节点被锁住就代表加锁成功。集群部署你的数据可能保存在任何一个redis服务节点上，一旦加锁必须确保集群内任意节点被锁住，否则也就失去了加锁的意义**

基于Redis的Redisson红锁RedissonRedLock对象实现了[Redlock](http://redis.cn/topics/distlock.html)加锁算法，该对象也可以用来将多个`RLock`对象关联为一个红锁，每个`RLock`对象实例可以来自于不同的Redisson实例



RedLock的实现步骤：

1. 获取当前时间，以毫秒为单位。
2. 按顺序向5个master节点请求加锁。客户端设置网络连接和响应超时时间，并且超时时间要小于锁的失效时间。（假设锁自动失效时间为10秒，则超时时间一般在5-50毫秒之间,我们就假设超时时间是50ms吧）。如果超时，跳过该master节点，尽快去尝试下一个master节点。
3. 客户端使用当前时间减去开始获取锁时间（即步骤1记录的时间），得到获取锁使用的时间。当且仅当超过一半（N/2+1，这里是5/2+1=3个节点）的Redis master节点都获得锁，并且使用的时间小于锁失效时间时，锁才算获取成功。（如上图，10s> 30ms+40ms+50ms+4m0s+50ms）
4. 如果取到了锁，key的真正有效时间就变啦，需要减去获取锁所使用的时间。
5. 如果获取锁失败（没有在至少N/2+1个master实例取到锁，有或者获取锁时间已经超过了有效时间），客户端要在所有的master节点上解锁（即便有些master节点根本就没有加锁成功，也需要解锁，以防止有些漏网之鱼）。

简化下步骤就是：

1. 按顺序向5个master节点请求加锁
2. 根据设置的超时时间来判断，是不是要跳过该master节点。
3. 如果大于等于3个节点加锁成功，并且使用的时间小于锁的有效期，即可认定加锁成功啦。
4. 如果获取锁失败，解锁！

~~~java
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 红锁在大部分节点上加锁成功就算成功。
lock.lock();
...
lock.unlock();
~~~



##### 读写锁(ReadWriteLock)

实现了 `java.util.concurrent.locks.ReadWriteLock` 接口并支持 TTL

分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态

写锁是一个拍他锁（互斥锁），读锁是一个共享锁。

- 读锁 + 读锁：相当于没加锁，可以并发读。
- 读锁 + 写锁：写锁需要等待读锁释放锁。
- 写锁 + 写锁：互斥，需要等待对方的锁释放。
- 写锁 + 读锁：读锁需要等待写锁释放

~~~java
RReadWriteLock rwlock = redisson.getReadWriteLock("anyRWLock");
// 最常见的使用方法
rwlock.readLock().lock();
// 或
rwlock.writeLock().lock();
~~~



##### 信号量(Semaphore)

基于Redis的Redisson的分布式信号量（[Semaphore](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphore.html)）Java对象`RSemaphore`采用了与`java.util.concurrent.Semaphore`相似的接口和用法。同时还提供了[异步（Async）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreAsync.html)、[反射式（Reactive）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreReactive.html)和[RxJava2标准](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RSemaphoreRx.html)的接口

~~~java
RSemaphore semaphore = redisson.getSemaphore("semaphore");
semaphore.acquire();
//或
semaphore.acquireAsync();
semaphore.acquire(23);
semaphore.tryAcquire();
//或
semaphore.tryAcquireAsync();
semaphore.tryAcquire(23, TimeUnit.SECONDS);
//或
semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
semaphore.release(10);
semaphore.release();
//或
semaphore.releaseAsync();
~~~



##### 过期性信号量(PermitExpirableSemphore)

基于Redis的Redisson可过期性信号量（[PermitExpirableSemaphore](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphore.html)）是在`RSemaphore`对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了[异步（Async）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreAsync.html)、[反射式（Reactive）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreReactive.html)和[RxJava2标准](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RPermitExpirableSemaphoreRx.html)的接口

~~~java
RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");
String permitId = semaphore.acquire();
// 获取一个信号，有效期只有2秒钟。
String permitId = semaphore.acquire(2, TimeUnit.SECONDS);
// ...
semaphore.release(permitId);
~~~



##### 闭锁(CountDownLatch)

基于Redisson的Redisson分布式闭锁（[CountDownLatch](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RCountDownLatch.html)）Java对象`RCountDownLatch`采用了与`java.util.concurrent.CountDownLatch`相似的接口和用法

~~~java
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.trySetCount(1);
latch.await();

// 在其他线程或其他JVM里
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.countDown();
~~~

