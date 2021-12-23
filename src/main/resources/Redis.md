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

---

#### 集合Set

1. set是自动去重的，当需要存储一个列表数据，又不希望出现重复数据时，采用set

##### 底层数据结构

1. dict字典，dict字典采用哈希表实现 hash表，类似Java的HashSet  底层为HashMap

---

#### 哈希Hash

1. hash是一个键值对集合
2. redis hash是一个string类型的filed和value的映射表，hash特别适用于存储对象，类似Java的Map<String，Map<string，obj>>

##### 底层数据结构

1. ziplist： field-value长度短、数量少
2. hashtable：field-value数量多

---

#### 有序集合ZSet

1. 有序集合与上述set类似，只是它是有序的
2. 有序集合的每个成员都关联了一个**评分**，这个评分被用来按照从最低分到最高分的方式排序集合中的成员
3. 集合的成员是唯一的，但是评分可以重复
4. 因为元素是有序的，所有可以很快的根据评分或者次序来获取一个范围的元素

##### 底层数据结构

1. SortedSet
   - 一方面等价于Java的Map<String，Double>可以给每一个元素value赋值一个权值score
   - 一方面又类似与TreeSet，内部的元素会按照权重score进行排序，可以得到每个元素的名次，还可以通过score的范围来获取元素的列表
2. zset使用了两种数据结构
   - hash：关联元素value和权重score
   - 跳跃表：给元素value排序，根据score的范围获取元素列表

##### 跳跃表 /跳表

---

### Redis6新数据类型

#### BitMaps

---

#### HyperLogLog

1. 解决基数问题
2. 基数：{1，3，5，7，5，7，8}  基数为5、7  ==》{1，3，5，7，8}
3. 会帮助去重
4. 每个HyperLogLog只需要12KB内存，就可以计算接近2的64次方个不同元素的基数

---

#### Geographic

1. Geographic地理信息的缩写
2. 该类型就是元素的2维坐标，在地图上就是经纬度
3. redis基于该类型，提供了经纬度设置、查询、等操作

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

### Redis事务

1. redis事务是一个单独的隔离操作
2. 事务中的所有命令都会序列化、按顺序执行
3. 事务执行过程中，不会被其他客户端发送来的命令请求所打断
4. redis事务的主要作用：串联多个命令、防止别的命令插队

Multi、Exec、Discard

1. Multi
2. Exec
3. Discard 取消组队

#### redis事务的错误处理

1. 组队时，有命令出错，exec时都不会执行
2. 组队成功，exec执行时，没有错的都会成功执行

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



#### Redis事务三特性

1. 单独的隔离操作
   - 事务中的所有命令都会序列化、按顺序执行
   - 事务在执行的过程中，不会被其他客户端送来的命令打断
2. 没有隔离级别的概念
   - 队列中的命令没有提交前不会被执行
3. 不保证原子性
   - 事务中，如果一条命令执行失败，其后的命令仍然会被执行，没有回滚

---

### Lua脚本

1. Lua脚本类似Redis事务，有一定的原子性，不会被其他命令插队，可以完成一些redis事务性的操作
2. Redis 2.6以上的版本才能使用
3. 利用lua脚本解决争抢问题，实际上是redis利用单线程的特性，用任务队列的方式解决多任务并发问题

### Redis持久化

#### RDB  Redis Data Base

1. 在指定**时间间隔**内将内存中的**数据集快照**写入磁盘，也就是Snapshot快照，它恢复时将快照文件之间读入内存中

2. ~~~shell
   [root@VM-12-9-centos ~]# cd /usr/local/bin
   [root@VM-12-9-centos bin]# ll
   busybox-x86_64
   dump.rdb
   redis-benchmark
   redis-check-aof -> redis-server
   redis-check-rdb -> redis-server
   redis-cli
   redis-sentinel -> redis-server
   redis-server
   ~~~

3. 备份是如何执行的？

   - Redis会单独fork一个子进程来进行持久化，先将数据写入一个临时文件，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件
   - 整个过程，主进程不进行任务IO操作，确保了极高的性能，如果需要大规模数据的恢复，且数据恢复的完整性不是非常敏感，那么rdb方式比aof方式更加高效
   - 默认文件为dump.rdb文件
   - 启动redis服务时，默认回去读取这个文件
   - 底层为：写时复用技术
   - 优势：
     - 适合大规模的数据恢复，对数据完整性和一致性要求不搞更适合使用
     - 节省磁盘空间
     - 恢复速度快
   - 缺点：
     - 需要fork一个进程，空间膨胀
     - **最后一次**（时间间隔内的）持久化后的数据可能丢失

4. Fork

   - 作用是复制一个与当前线程一样的线程，作为原进程的一个子进程

#### AOF Append Of File

1. 以日志的形式来记录每个**写操作**，将Redis执行过的所有写操作指令记录下来（读操作不记录），**只许追加文件但不可改写文件**，redis启动时会读取该文件并重写构造数据
2. 也就是redis重启的话，会根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作
3. Aof默认是不开启的
   - 需要去手动开启 appendonly yes
   - 默认与dump.rdb都在启动文件目录下
   - 两个备份方式都开启后，redis启动时默认会去读取appenonly.aof文件，而不是dump.rdb
4. 异常恢复
   - 遇到AOF文件损坏，可通过/usr/local/bin/redis-check-aof--fix appendonly.aof进行恢复
   - 备份被写坏的AOF文件
5. Rewrite
   - aof采用文件追加方式，文件会越来越大
   - 加入重写机制，当aof文件的大小超过所设定的阈值时，redis会启动Aof文件内容的压缩，只保留可以恢复数据的最小集合
   - redis会记录上次重写时的AOF文件大小，默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时才触发
   - 可以使用命令bgrewriteaof
   - 重写原理
     - fork一个进程来重写文件
     - 也是先写临时文件
6. 总结：持久化流程
   - 客户端的请求写命令会被append追加到AOF缓冲区
   - 缓冲区根据AOF持久化策略（always、everysec、no）将操作同步到磁盘的AOF文件中
   - AOF文件大小超过重写策略或手动重写时，会对AOF文件rewrite重写，压缩AOF文件容量
   - redis重启时，会加载AOF文件中的写操作，以达到数据恢复的目的
7. 优缺点
   - 优点
     - 备份机制更稳健，丢失数据概率更低
     - 可读的日志文本，可以操作aof文件，可以处理误操作
   - 缺点
     - 比RDB方式占用更多的空间
     - 备份恢复速度慢
     - 每次读写都同步的话，有一定的性能压力

#### 两种方式总结

1. 官方推荐两种方式都启用
2. 对数据不敏感，可以单独使用RDB
3. 不建议单独使用AOF
4. 如果只做纯内存缓存，可以都不用

---

### Redis主从复制

一主多从

#### 是什么

1. 主机数据更新后，根据配置和策略，自动同步到备机的Master/Slaver机制
2. Master以写为主，Slave以读为主

#### 能干嘛

1. 读写分离，性能扩展
2. 容灾快速恢复
   - 一台服务器宕机，可以从其他服务器继续工作

#### 集群

一主多从

一主多从

































