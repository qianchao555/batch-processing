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

https://pdai.tech/md/db/nosql-redis/db-redis-introduce.html

www.reids.cn

Remote Dictionary Server：远程数据服务

redis是一款基于内存的高速缓存数据库，是一个k-v存储系统。可用于缓存、事件发布/订阅、高速队列、分布式锁等等

### 为什么要使用Redis

1. 读写性能优异
   - Redis读的速度大约是11万次/s，写的速度大约是8万次/s
2. 数据类型丰富
3. 原子性
   - redis索引操作都是原子性的，同时还支持对几个操作合并后原子性的执行
4. 丰富的特性
   - 支持发布/订阅、通知、key过期等等特性
5. 分布式

### Redis使用场景

平时应用redis场景还是相当多的，例如：

1. 热点数据的缓存
   - 缓存是redis最场景的应用，之所以应用是因为其读写性能优异，而且内部是支持事务的，在使用时能保证数据的一致性
   - 作为缓存使用时，一般有两种方式保存数据
     1. 读取前，先读redis，如果没有数据，读取数据库，然后将数据写入redis
     2. 插入数据时，同时写redis
2. 分布式锁
3. 限时业务运用
   - redis在可以利用key的过期时间，到时间后redis会删除它
   - 利用这一特性可以运用在限时优惠、手机验证码等等业务场景
4. 计数器相关问题
   - redis利用incrby命令可以实现原子性的递增，可以运用于高并发秒杀活动、分布式序列号的生成、一个接口一分钟限制多少请求、一个接口限制一天多少次调用等等
5. 利用key的特性，实现防止重复提交(实现幂等性)
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
2. 是二进制安全的，意味着Redis的String可以包含任何数据。比如：jpg图片、序列化对象等等
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

![image-20220608164910450](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206081649346.png)

1. set是自动去重的，当需要存储一个列表数据，又不希望出现重复数据时，采用set
1. set中的元素是不可以重复的
1. 添加、删除、查找复杂度都是O(1)

##### 底层数据结构

1. dict字典，dict字典采用哈希表实现 hash表，类似Java的HashSet  底层为HashMap

---

#### 哈希Hash

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

### Key的相关命令

以下命令为通用命令：

#### set

set k v

插入一条记录

```sql
127.0.0.1:6379> set k1 v1
OK
```



#### del

del key

删除一个已经存在的key



#### Dump

dump k

序列化给定的key



#### Exists

exists k

检测一个给定的key是否存在



#### Expire

expire  k  seconds

给key设置有效期，有效期过后key会被销毁

#### Pexpire

Pexpire k milSeconds

功能和expire基本一致，只不过这里设置的参数是毫秒



#### TTL

ttl key

查看一个给定key的有效时间

- -1 表示key没有设置过期时间
- -2 表示key不存在或者已经过期

#### Pttl

Pttl k

表示：功能和ttl基本一致，只不过pttl返回的是毫秒数



#### Persist

persist k 

表示：移除一个key的过期时间，这样key就永远不会过期

---

### String

#### Append

如果key已经存在，则会在value后面追加值。key不存在则会先创建一个value为空的字符串key，然后再追加

~~~sql
127.0.0.1:6379> set k1 v1
OK
127.0.0.1:6379> append k1 hello
(integer) 7
127.0.0.1:6379> get k1
"v1hello"
127.0.0.1:6379> append k1 world
(integer) 12
127.0.0.1:6379> get k1
"v1helloworld"
127.0.0.1:6379> 
~~~



#### Decr

实现对value减1操作，如果key不存在，则key对应的初始值会被置为0，如果key的value不为数字，则会报错

~~~sql
127.0.0.1:6379> get k1
"v1helloworld"
127.0.0.1:6379> decr k1
(error) ERR value is not an integer or out of range
127.0.0.1:6379> decr k2
(integer) -1
127.0.0.1:6379> get k2
"-1"
127.0.0.1:6379> keys *
1) "k1"
2) "k2"
127.0.0.1:6379> set k2 0
OK
127.0.0.1:6379> get k2
"0"
127.0.0.1:6379> decr k2
(integer) -1
127.0.0.1:6379> decr k2
(integer) -2
127.0.0.1:6379> decr k2
(integer) -3
127.0.0.1:6379> 

~~~



#### Decrby

同decr类似，不过decrby可以指定步长

~~~sql
127.0.0.1:6379> set k3 50
OK
127.0.0.1:6379> decrby k3 10
(integer) 40
127.0.0.1:6379> 
~~~



#### Get

获取对应key的value，如果key不存在则返回nil

#### GetRange

GETRANGE 用来返回 key 所对应的 value 的子串，子串由 start 和 end 决定，从左往右计算，如果下标是负数，则从右往左计算，其中 -1 表示最后一个字符， -2 是倒数第二个

~~~sql
127.0.0.1:6379> get k1
"v1helloworld"
127.0.0.1:6379> getrange k1 0 2
"v1h"
127.0.0.1:6379> getrange k1 0 1
"v1"
127.0.0.1:6379> getrange k1 -1 1
""
127.0.0.1:6379> getrange k1 -1 -5
""
127.0.0.1:6379> getrange k1 -5 -1
"world"
127.0.0.1:6379> 
~~~



#### GetSet

获取key是对应的value，并对key对应的value进行重置

~~~sql
127.0.0.1:6379> get k1
"v1helloworld"
127.0.0.1:6379> getset k1 helloworld2
"v1helloworld"
127.0.0.1:6379> get k1
"helloworld2"
127.0.0.1:6379> 
~~~



#### Incr

INCR 操作可以对指定 key 的 value 执行加 1 操作，如果指定的 key 不存在，那么在加 1 操作之前，会先将 key 的 value 设置为 0 ，如果 key 的 value 不是数字，则会报错

#### IncrBy

INCRBY 和 INCR 功能类似，不同的是可以指定增长的步长

#### IncrByFloat

INCRBYFLOAT 命令可以用来增长浮点数

#### Mget / Mset

批量获取值和设置值

~~~sql
127.0.0.1:6379> mget k1 k2 k3
1) "helloworld2"
2) "-3"
3) "40.0005"
127.0.0.1:6379> mset k1 helloworld k2 1 k3 40
OK
127.0.0.1:6379> mget k1 k2 k3
1) "helloworld"
2) "1"
3) "40"
127.0.0.1:6379> 
~~~



#### setex

set+expire：给key设置value并同时设置过期时间

setex  seconds  k v

~~~sql 
127.0.0.1:6379> setex k4 10 40
OK
127.0.0.1:6379> ttlk4
(error) ERR unknown command `ttlk4`, with args beginning with: 
127.0.0.1:6379> ttl k4
(integer) 2
127.0.0.1:6379> ttl k4
(integer) -2
127.0.0.1:6379> 
~~~



#### Psetex

PSETEX 的作用和 SETEX 类似，不同的是，这里设置过期时间的单位是毫秒

#### setnx

set if not exists :setnx

set命令：如果key已经存在则新值会覆盖旧值

setnx：如果key已经存在，则不做任何操作，如果key不存在，效果等同于set

~~~sql
127.0.0.1:6379> setnx k4 v4
(integer) 1
127.0.0.1:6379> get k4
"v4"
127.0.0.1:6379> setnx k4 k44
(integer) 0
127.0.0.1:6379> get k4
"v4"
~~~



#### Msetnx

MSETNX 兼具了 SETNX 和 MSET 的特性，但是 MSETNX 在执行时，如果有一个 key 存在，则所有的都不会执行

#### setRange

SETRANGE 用来覆盖一个已经存在的 key 的 value，但是如果已经存在的 key 的 value 长度小于 offset ，则不足的地方用 0 补齐

~~~sql
127.0.0.1:6379> get k1
"helloworld"
127.0.0.1:6379> setrange k1 5 WW
(integer) 10
127.0.0.1:6379> get k1
"helloWWrld"
127.0.0.1:6379> setrange k1 5 WWWWWWWW
(integer) 13
127.0.0.1:6379> get k1
"helloWWWWWWWW"
127.0.0.1:6379> setrange k1 10 KKKKK
(integer) 15
127.0.0.1:6379> get k1
"helloWWWWWKKKKK"
127.0.0.1:6379> setrange k1 20 KKKKK
(integer) 25
127.0.0.1:6379> get k1
"helloWWWWWKKKKK\x00\x00\x00\x00\x00KKKKK"
127.0.0.1:6379> 
~~~



#### strLen

计算key的value的长度

等等看官方文档



### String Bit相关命令

redis中的字符串都是以二进制的方式存储的。例如

~~~sql
127.0.0.1:6379> set k1 a
Ok
# a对应的ASCII码是97,转换为二进制为：01100001
# Bit命令都是对这个二进制数据进行操作的

~~~

#### GetBit

返回key对应的value在offset处的bit值，例如a : 01100001 ，所有当offset为0时，对应的bit值为0，offset为1、2时，对应的bit值为1  ....以此类推

~~~sql
127.0.0.1:6379> set k1 a
OK
127.0.0.1:6379> getbit k1 0
(integer) 0
127.0.0.1:6379> getbit k1 1
(integer) 1
127.0.0.1:6379> getbit k1 2
(integer) 1
127.0.0.1:6379> getbit k1 3
(integer) 0
127.0.0.1:6379> getbit k1 4
(integer) 0
127.0.0.1:6379> getbit k1 5
(integer) 0
127.0.0.1:6379> getbit k1 6
(integer) 0
127.0.0.1:6379> getbit k1 7
(integer) 1
 # 01100001  =>97   ascii=>a
~~~



#### SetBit

SETBIT 可以用来修改二进制数据，比如 a 对应的 ASCII 码为 97，c 对应的 ASCII 码为 99，97 转为二进制是 01100001 ，99 转为二进制是 01100011 ，两个的差异在于第六位一个是 0 一个是 1 ，通过 SETBIT 命令，我们可以将 k1 的第六位的 0 改为 1 （第六位是从 0 开始算）。改完后对应的值也会发生改变

~~~sql
127.0.0.1:6379> get k1
"a"
127.0.0.1:6379> setbit k1 6 1
(integer) 0
127.0.0.1:6379> get k1
"c"
127.0.0.1:6379> 
~~~



#### BitCount

BITCOUNT 可以用来统计这个二进制数据中 1 的个数

关于 BITCOUNT，redis 官网上有一个非常有意思的案例：用户上线次数统计。节选部分原文如下：

> 举个例子，如果今天是网站上线的第 100 天，而用户 peter 在今天阅览过网站，那么执行命令 SETBIT peter 100 1 ；如果明天 peter 也继续阅览网站，那么执行命令 SETBIT peter 101 1 ，以此类推。当要计算 peter 总共以来的上线次数时，就使用 BITCOUNT 命令：执行 BITCOUNT peter ，得出的结果就是 peter 上线的总天数。

这种统计方式最大的好处就是节省空间并且运算速度快。每天占用一个 bit，一年也就 365 个 bit，10 年也就 10*365 个 bit ，也就是 456 个字节，对于这么大的数据，bit 的操作速度非常快

~~~sql
127.0.0.1:6379> setbit qc 1 1
(integer) 0
127.0.0.1:6379> setbit qc 2 1
(integer) 0
127.0.0.1:6379> setbit qc 3 0
(integer) 0
127.0.0.1:6379> bitcount qc
(integer) 2
127.0.0.1:6379> 
~~~



#### BitOp

bitOp可以对一个或者多个二进制位串进行：并 and、或or、异或Xor、非 not运算

~~~sql
127.0.0.1:6379> set k1 a
OK
127.0.0.1:6379> set k2 c
OK
127.0.0.1:6379> BITOP and k3 k1 k2
(integer) 1
127.0.0.1:6379> get k3
"a"
127.0.0.1:6379> BITOP or k3 k1 k2
(integer) 1
127.0.0.1:6379> get k3
"c"
127.0.0.1:6379> BITOP xor k3 k1 k2
(integer) 1
127.0.0.1:6379> get k3
"\x02"

#这里会对 k4 的二进制位串取反，将取反结果交给 k3
127.0.0.1:6379> BITOP not k3 k4
(integer) 1
~~~



#### BitPos

bitPos 用来获取二进制位串中第一个 1 或者 0 的位置

~~~sql
127.0.0.1:6379> set k1 a
OK
127.0.0.1:6379> BITPOS k1 1
(integer) 1
127.0.0.1:6379> BITPOS k1 0
(integer) 0
~~~



---

### List

#### Lpush

将一个或者多个value插入列表key的表头，如果有多个value，那么各个value值按照从左到右顺序依次插入表头

~~~sql
127.0.0.1:6379> LPUSH k1 v1 v2 v3
(integer) 3
~~~



#### Lrange

返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定，下标 (index) 参数 start 和 stop 都以 0 为底，即 0 表示列表的第一个元素，1 表示列表的第二个元素，以此类推。我们也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推

~~~sql
127.0.0.1:6379> LRANGE k1 0 -1
1) "v3"
2) "v2"
3) "v1"
~~~



#### Rpush

RPUSH 与 LPUSH 的功能基本一致，不同的是 RPUSH 的中的 value 值是按照从右到左的顺序依次插入

~~~sql
127.0.0.1:6379> rpush k2 1 2 3 6 5
(integer) 5
127.0.0.1:6379> lrange k2 0 -1
1) "1"
2) "2"
3) "3"
4) "6"
5) "5"
~~~



#### Rpop  Lpop

Rpop  可以移除并返回列表 key 的尾元素

Lpop    移除并返回列表 key 的头元素

~~~sql
127.0.0.1:6379> lrange k2 0 -1
1) "1"
2) "2"
3) "3"
4) "6"
5) "5"
127.0.0.1:6379> rpop k2
"5"
127.0.0.1:6379> lpop k2
"1"
127.0.0.1:6379> 

~~~



#### Lindex

返回列表 key 中，下标为 index 的元素，正数下标 0 表示第一个元素，也可以使用负数下标，-1 表示倒数第一个元素

~~~sql
127.0.0.1:6379> lrange k2 0 -1
1) "2"
2) "3"
3) "6"
127.0.0.1:6379> lindex k2 0
"2"
127.0.0.1:6379> lindex k2 -3
"2"
127.0.0.1:6379> lindex k2 -2
"3"
127.0.0.1:6379> 

~~~



#### Ltrim

可以对一个列表进行修剪，即让列表**只保留指定区间内的元素**，不在指定区间之内的元素都将被删除

~~~sql
127.0.0.1:6379> lrange k1 0 -1
1) "v3"
2) "v2"
3) "v1"
127.0.0.1:6379> ltrim k1 0 1
OK
127.0.0.1:6379> lrange k1 0 -1
1) "v3"
2) "v2"
127.0.0.1:6379> 
~~~



#### BlPop

Block +Lpop

是阻塞式列表的弹出原语。它是命令 LPOP 的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BLPOP 命令阻塞。当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的头元素。同时，在使用该命令时也需要指定阻塞的时长，时长单位为秒，在该时长内如果没有元素可供弹出，则阻塞结束。返回的结果是 key 和 value 的组合

BRPOP、BPOPLPUSH、BRPOPLPUSH 都是相应命令的阻塞版本

---



### set

#### SAdd

SADD 命令可以添加一个或多个指定的 member 元素到集合的 key 中，指定的一个或者多个元素 member 如果已经在集合 key 中存在则忽略，如果集合 key 不存在，则新建集合 key ,并添加 member 元素到集合 key 中

~~~sql
127.0.0.1:6379> SADD k1 v1 v2 v3 v4
(integer) 4
~~~



#### SRem

SREM 命令可以在 key 集合中移除指定的元素，如果指定的元素不是 key 集合中的元素则忽略。如果 key 集合不存在则被视为一个空的集合，该命令返回 0

#### SIsMember

SISMEMBER 命令可以返回成员 member 是否是存储的集合 key 的成员

#### SCard

SCARD 命令可以返回集合存储的 key 的基数(集合元素的数量)

#### SMembers

SMEMBERS 命令可以返回 key 集合所有的元素

#### SRandMember

SRANDMEMBER 仅需我们提供 key 参数,它就会随机返回 key 集合中的一个元素

#### SPop

SPOP 命令的用法和 SRANDMEMBER 类似，不同的是，SPOP 每次选择一个随机的元素之后，该元素会出栈，而 SRANDMEMBER 则不会出栈，只是将该元素展示出来

#### SMove

SMOVE 命令可以将 member 从 source 集合移动到 destination 集合中

~~~sql
127.0.0.1:6379> smembers setK1
1) "v2"
2) "v1"
3) "v4"
4) "v3"
127.0.0.1:6379> smove setK1 setK2 v1
(integer) 1
127.0.0.1:6379> smembers setK2
1) "v1"
127.0.0.1:6379> smembers setK1
1) "v2"
2) "v4"
3) "v3"
127.0.0.1:6379> 
~~~



#### SDiff

SDIFF 可以用来返回一个集合与给定集合的差集的元素

~~~sql
127.0.0.1:6379> sdiff setK1 setK2
1) "v2"
2) "v3"
3) "v4"
127.0.0.1:6379> sadd setK2 v2
(integer) 1
127.0.0.1:6379> sdiff setK1 setK2
1) "v3"
2) "v4"
127.0.0.1:6379> 
~~~



#### SDiffStore

SDIFFSTORE 命令与 SDIFF 命令基本一致，不同的是 SDIFFSTORE 命令会将结果保存在一个集合

#### SInter

SINTER 命令可以用来计算指定 key 之间元素的交集

#### SInterStore

SINTERSTORE 命令和 SINTER 命令类似，不同的是它会将结果保存到一个新的集合中

#### SUnion

SUNION 可以用来计算两个集合的并集

#### SUnionStore

SUNIONSTORE 和 SUNION 命令类似，不同的是它会将结果保存到一个新的集合

---

### Hash 散列

#### Hset

HSET 命令可以用来设置 key 指定的哈希集中指定字段的值

~~~~sql
127.0.0.1:6379> hset hashK1 h1 v1
(integer) 1
~~~~



#### Hget

HGET 命令可以用来返回 key 指定的哈希集中该字段所关联的值

~~~~~sql
127.0.0.1:6379> Hget hashK1 h1
"v1"
~~~~~



#### Hmset

HMSET 命令可以批量设置 key 指定的哈希集中指定字段的值

~~~sql
127.0.0.1:6379> hmset hashK2 h1 v1 h2 v2 h3 v3
OK
~~~



#### Hmget

HMGET 可以批量返回 key 指定的哈希集中指定字段的值

~~~sql
127.0.0.1:6379> hmget hashK2 h1 h2 
1) "v1"
2) "v2"
~~~



#### Hdel

HDEL 命令可以从 key 指定的哈希集中移除指定的域，在哈希集中不存在的域将被忽略

~~~sql
127.0.0.1:6379> hdel hashKey2 h1
(integer) 0
~~~



#### Hsetnx

HSETNX 命令只在 key 指定的哈希集中不存在指定的字段时，设置字段的值，如果字段已存在，该操作无效果

~~~sql
127.0.0.1:6379> hsetnx hashK1 h2 v2
(integer) 1
127.0.0.1:6379> hsetnx hashK1 h2 v3
(integer) 0
~~~



#### Hvals

HVALS 命令可以返回 key 指定的哈希集中所有字段的值

~~~sql
127.0.0.1:6379> hvals hashK1
1) "v1"
~~~



#### Hkeys

HKEYS 命令可以返回 key 指定的哈希集中所有字段的名字

~~~sql
127.0.0.1:6379> hkeys hashK2
1) "h1"
2) "h2"
3) "h3"
~~~



#### Hgetall

HGETALL 命令可以返回 key 指定的哈希集中所有的字段和值。返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍

~~~sql
127.0.0.1:6379> hgetall hashK2
1) "h1"
2) "v1"
3) "h2"
4) "v2"
5) "h3"
6) "v3"

~~~



#### Hexists

HEXISTS 命令可以返回 hash 里面 field 是否存在

~~~SQL
127.0.0.1:6379> hexists hashK1 hh
(integer) 0
127.0.0.1:6379> hexists hashK1 h1
(integer) 1
~~~



#### HincrBy

HINCRBY 可以增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0， HINCRBY 支持的值的范围限定在 64 位有符号整数

#### HincrByFloat

HINCRBYFLOAT 与 HINCRBY 用法基本一致，只不过这里允许 float 类型的数据

#### Hlen

HLEN 返回 key 指定的哈希集包含的字段的数量

#### Hstrlen

HSTRLEN 可以返回 hash 指定 field 的 value 的字符串长度，如果 hash 或者 field 不存在，返回 0

---

### ZSet 有序集合

有序集合类似 Sets ,但是每个字符串元素都关联到一个叫 score 浮动数值。里面的元素总是通过 score 进行着排序，因此它是可以检索的一系列元素

#### ZAdd

ZADD 命令可以将所有指定成员添加到键为 key 的有序集合里面。添加时可以指定多个分数/成员（score/member）对。 如果指定添加的成员已经是有序集合里面的成员，则会更新该成员的分数（scrore）并更新到正确的排序位置

~~~sql
127.0.0.1:6379> zadd zsetK1 60 v1
(integer) 1
127.0.0.1:6379> zadd zsetK1 70 v2 80 v3
(integer) 2
127.0.0.1:6379> 
~~~



#### ZScore

返回有序集 key 中，成员 member 的score 值

~~~sql
127.0.0.1:6379> zscore zsetK1 v1
"60"
127.0.0.1:6379> zscore zsetK1 v1 v2
(error) ERR wrong number of arguments for 'zscore' command
127.0.0.1:6379> zscore zsetK1 v2
"70"
~~~



#### ZRange

ZRANGE 命令可以根据 index 返回 member ，该命令在执行时加上 withscores 参数可以连同 score 一起返回

~~~sql
127.0.0.1:6379> zrange zsetK1 0 3
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> zrange zsetK1 0 2
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> zrange zsetK1 0 1
1) "v1"
2) "v2"

127.0.0.1:6379> zrange zsetK1 0 1 withscores
1) "v1"
2) "60"
3) "v2"
4) "70"
~~~



#### ZRevRange

ZREVRANGE 和 ZRANGE 功能基本一致，不同的是 ZREVRANGE 是反着来的

#### ZCard

ZCARD 命令可以返回 key 的有序集元素个数

~~~sql
127.0.0.1:6379> zcard zsetK1
(integer) 3
~~~



#### ZCount

ZCOUNT 命令可以返回有序集 key 中，score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员个数

#### ZRangeByScore

ZRANGEBYSCORE 命令可以返回按照 score 范围元素，加上 withscores 可以连 score 一起返回

~~~sql
127.0.0.1:6379> zrangebyscore zsetK1 60 70
1) "v1"
2) "v2"
~~~



#### ZRank

ZRANK 命令可以返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。排名以 0 为底，即 score 值最小的成员排名为 0 。

~~~sql
127.0.0.1:6379> zrank zsetK1 v1
(integer) 0   #排名
127.0.0.1:6379> zrank zsetK1 v2
(integer) 1
127.0.0.1:6379> zrank zsetK1 v3
(integer) 2
~~~



#### ZRevRank

ZREVRANK 和 ZRANK 命令功能基本一致，不同的是，ZREVRANK 中的排序是从大到小

#### ZIncrBy

ZINCRBY 命令可以为有序集 key 的成员 member 的 score 值加上增量 increment 。如果 key 中不存在 member ，就在 key 中添加一个 member ，score 是 increment（就好像它之前的 score 是0.0）。如果 key 不存在，就创建一个只含有指定 member 成员的有序集合

~~~sql
127.0.0.1:6379> zincrby zsetK1 1 v1
"61"
~~~



#### ZInterStore

ZINTERSTORE 命令可以计算给定的 numkeys 个有序集合的交集，并且把结果放到 destination 中

#### ZRem

ZREM 命令可以从集合中弹出一个元素

#### ZLexCount

用于计算有序集合中指定成员之间的成员数量

#### ZRangeByLex

ZRANGEBYLEX 返回指定成员区间内的成员，按成员字典正序排序, 分数必须相同



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

---



### Redis持久化

https://pdai.tech/md/db/nosql-redis/db-redis-x-rdb-aof.html#redis%E6%8C%81%E4%B9%85%E5%8C%96%E7%AE%80%E4%BB%8B

为了防止数据丢失以及服务器重启时能够恢复数据，Redis支持的数据持久化方式分为RDB和AOF

#### 为什么需要持久化？

Redis是个基于内存的数据库。那服务一旦宕机，内存中的数据将全部丢失。通常的解决方案是从后端数据库恢复这些数据，但后端数据库有性能瓶颈，如果是大数据量的恢复，1、会对数据库带来巨大的压力，2、数据库的性能不如Redis。导致程序响应慢。所以对Redis来说，实现数据的持久化，避免从后端数据库中恢复数据，是至关重要的

#### RDB  

Redis Data Base，中文为快照/内存持久化，rdb持久化是把当前**进程数据**生成快照保存到磁盘上的过程，由于是某一时刻的快照，那么快照中的值早于或等于内存中的值

##### 触发方式

rdb持久化方式有两种

1. 手动触发
   - save命令：阻塞当前Redis服务器，直到rdb过程完成为止，对于内存较大的实例会造成长时间阻塞，生产环境不建议使用
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
   - redis.conf配置文件中：配置save m n，即m秒内有n次修改，则自动触发bgsave生成rdb文件
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
save 900 1		#若900s 内有1个key信息发送变化，则进行快照
save 300 10
save 60 10000

# 以下设置方式为关闭RDB快照功能
save ""
~~~



1. 在指定**时间间隔**内将内存中的**数据集快照**写入磁盘，也就是Snapshot快照，redis重启时，将快照文件读入内存中

2. redis中的快照持久化默认开启的

3. ~~~shell
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

4. 备份是如何执行的？

   - Redis会单独fork一个子进程来进行持久化，先将数据写入一个临时文件，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件
   - 整个过程，主进程不进行任务IO操作，确保了极高的性能，如果需要大规模数据的恢复，且数据恢复的完整性不是非常敏感，那么rdb方式比aof方式更加高效
   - 默认文件为dump.rdb文件
   - 启动redis服务时，默认会去读取这个文件
   - 底层实现为：写时复用技术，即Copy-on-write

##### rdb核心思路

多数情况下，生产环境中为Redis开辟的内存区域都比较大（例如6GB），那么将内存中的数据同步到硬盘的过程可能就会持续比较长的时间，而实际情况是同步的这段时间里，Redis服务一般都会收到数据写操作请求。那么如何保证数据一致性？

copy-on-write：写时复制，保证在进行快照读操作的这段时间，需要压缩写入到磁盘上的数据在内存中不会发生变化。

在正常的快照操作中，redis主进程会fork一个新的快照进程来负责这个事情，从而保证了redis服务器不会停止对客户端包括写请求在内的任何相应。另一方面，这段时间发生的数据变化，会以副本的方式存在一个新的内存区域，待快照操作结束后，才会同步到原来的内存区域中

##### 快照期间，服务器奔溃怎么处理

在没有将数据全部写入到磁盘前，这次快照操作都不算成功。如果出现了服务崩溃的情况，将以上一次完整的RDB快照文件作为恢复内存数据的参考。也就是说，在快照操作过程中不能影响上一次的备份数据。因为Redis服务器会在磁盘上创建一个临时文件进行数据快照操作，待操作成功后才会用这个临时文件替换掉上一次的rdb文件

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
   - 实时性不强，无法做到秒级的持久化，不适合实时持久化
   - 每次调用bgsave都需要fork一个子进程，fork子进程属于重量级操作，空间膨胀
   - rdb文件是二进制文件，没有可读性，不像aof文件在了解了其结构的情况下可以手动修改或补全
   - **最后一次**（时间间隔内的）持久化后的数据可能丢失

---



#### AOF 

Append Of File："写后"日志 即先写内存后写日志，redis先执行命名，把数据写入内存，然后才记录日志。日志里记录的是redis收到的每一条写命令，这些命令以文本形式保存

##### 为什么采用写后日志

1. 避免额外的检查开销：redis向aof里面记录日志的时候，并不会先对命令进行语法检查，所有若先记录日志再执行命令的话，日志中就有可能记录了错误的命令，redis在使用日志恢复数据的时候，就会出错
2. 不会阻塞当前的写操作，但是这种方式会存在潜在风险
   - 若命令执行完成后、写日志之前，服务器宕机了，会丢失数据
   - 主线程写磁盘压力大，导致写磁盘慢，阻塞后续操作

##### 如何实现AOF

aof日志记录redis的每个写命令，步骤分为：命令追加(append)、文件写入(write)、文件同步(sync)

1. 命令追加
   - 当aof持久化功能打开后，服务器在执行完一个写命令后，会以将被执行的写命令追加到服务器的aof_buf缓冲区
2. 文件写入和同步
   - 何时将aof_buf缓冲区的内容写入aof文件中，redis提供了三种写回策略
   - ![image-20220608220133162](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082201335.png)
   - always：同步写回，即每个写命令执行完后，立马同步地将日志写回磁盘
   - everysec：每秒写回，默认策略，即每个写命令执行完后，只是先把日志写到aof_buf内存缓存区，每隔一秒把缓冲区的内容写入磁盘文件
   - no：操作系统控制的写回，即每个写命令执行完后，先把日志写到aof_buf缓冲区，由操作系统决定时候将缓冲区内容写回磁盘
3. 三种写回策略的优缺点
   - 上述三个写回策略体现了一个原则：trade-off，即取舍、权衡，指在性能和可靠性之间做取舍

##### redis.conf中配置aof

~~~shell
# appendonly参数开启AOF持久化
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

##### aof重写

AOF会记录每个写命令到AOF文件，随着时间越来越长，AOF文件会变得越来越大。如果不加以控制，会对Redis服务器，甚至对操作系统造成影响，而且AOF文件越大，数据恢复也越慢。为了解决AOF文件体积膨胀的问题，Redis提供AOF文件重写机制来对AOF文件进行压缩

redis通过创建一个新的aof文件替换现有的aof文件，新旧两个aof文件保存的数据相同，但是新文件没有了冗余命令

![image-20220608222311422](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202206082223546.png)

###### **aof重写原理**

和rdb类似，后台同样是fork一个bgrewriteaof子进程，fork会把主进程程的内存拷贝一份给bgrewriteaof子进程(通过写时复制机制将数据复制给子进程)，这里面包含了redis数据库的最新数据，然后子进程就可以在不影响主进程的情况下，逐一把拷贝的数据写入重写日志，只会在fork的时候才会阻塞主进程

###### **何时重写**

auto-aof-rewrite-percentage 100 ：这个值配置项表示，当前aof文件大小和上一次重写后aof文件大小的差值/上一次重写后aof文件大小。即：当前aof文件大小比上一次重写后aof文件的增量大小，和上一次重写后aof文件大小的比值。即默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时才触发
auto-aof-rewrite-min-size 64mb  ：表示启动AOF文件重写时，AOF文件的最小大小，默认64M

###### **重写时有新数据写入如何处理**

重写过程总结为：“一个拷贝，两处日志”。在fork出子进程时的拷贝，以及在重写时，如果有新数据写入，主线程就会将命令记录到两个aof日志内存缓冲区中。如果AOF写回策略配置的是always，则直接将命令写回旧的日志文件，并且保存一份命令至AOF重写缓冲区，这些操作对新的日志文件是不存在影响的。（旧的日志文件：主线程使用的日志文件，新的日志文件：bgrewriteaof进程使用的日志文件）

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

一主多从

#### 是什么

1. 主机数据更新后，根据配置和策略，自动同步到备机的Master/Slaver机制
2. Master以写为主，Slave以读为主

#### 能干嘛

1. 读写分离，性能扩展
2. 容灾快速恢复
   - 一台服务器宕机，可以从其他服务器继续工作

#### 主从复制

一主多从：

1. 创建/myredis文件夹
2. 复制配置文件 /etc/redis.conf  到文件夹中 redis6379.conf、  redis6380.conf 、redis6381.conf
3. 在三个配置文件中分别配置
4. info replication查看主机信息
5. 配置从机
   - slaveof  主机ip  主机端口
6. 宕机
   - 从机宕机之后重启会成为一个新的主服务器，需要重新加入到一主多从的环境中，且重启后的服务器里面会复制主机的数据，从而使得数据一致
   - 主机宕机：从机不变，主服务器重启后还是主服务器
   - 主机宕机：从机中配置了slave no one  ，那么将该从机升级为主机
   - 主机宕机：哨兵模式：自动从从机中选取一个机器作为主机
7. 同步数据方式
   - 主服务器直接管理一个从服务器，由从服务器去管理其他从服务器
   - 主服务器
8. 主从复制原理
   - 从服务器连接上主服务器后，向主服务器发送进行数据同步的消息
   - 主服务器接收到消息后，把当前主服务器中的数据持久化，把rdb/aof文件发送给从服务器，从服务器拿到文件进行读取
   - 每次主服务器写操作后，主动和从服务器进行同步
9. 主从复制哨兵模式
   - 能够自动监控后台主机是否故障，如果故障了那么会自动进行投票自动将从服务器转换为主服务器
   - sentinel monitor 主机ip 端口 值
   - sentinel monitor mymaster 127.0.0.1 6379 1
   - mymaster:为监控对象起的服务器名称
   - 1：至少有多少个哨兵同意迁移的数量
   - 启动：**redis-sentinel**   /root/myredis/sentinel.conf
   - 主机宕机之后，从机自动选举上位为主服务器，之前的主机重启后会变成现在主机的从机
   - 选举规则：
     - 选择优先级高的   配置：之前的版本slave-priority:10   6版本replica-priority:100
     - 值越小优先级越高
     - 优先级相同，选举偏移量大的   （谁和主机的数据同步至最高）
     - 选举runid最小的:redis实例启动后会随机生成一个40位的runid



### 集群

1. 机器容量不够，redis如何扩容？
2. 并发写操作，redis如何分摊？
3. 早期通过代理主机解决，redis3.0开始后提供了无中心集群配置
4. 什么是集群
   - Redis集群实现了对Redis的水平扩容，即启动N个redis节点，将整个数据库分布存储在这N个节点中，每个节点存储总书记的1/N
   - Redis集群通过分区（partition）来提供一定程度可用性：即使集群有一部分节点失效或者无法通讯，集群也可以继续处理命令请求
5. 分配原则
   - 尽量保证每个主数据库服务器运行在不同的ip地址，每个从库和主库不在一个ip地址上
6. slots
   - 一个Redis集群包含16384个slots，数据库中的每个键都属于这个16384个插槽的其中一个
   - 集群使用公式CRC16(key)% 16384计算出key属于哪个插槽，便放入哪个机器中
7. 故障恢复
   - 主机宕机，从机升级为主机
   - 主机重新启动后，成为当前主机的从机
   - 集群里面的一部分（主机、从机）都宕机了
     - cluster-require-full-coverage yes：整个集群都挂掉
     - cluster-require-full-coverage no：该插槽数据全都不能使用也不能存储
8. 集群的不足
   - 多建操作需要建立 组
   - 多建的Redis事务是不被支持的。lua脚本不被支持

### redis缓存

通过将部分数据放入缓存中，来提高访问速度，数据库承担存储的工作

当想要查询数据时，使用缓存的流程如下

![](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203222313989.png)

#### 本地缓存

本地缓存也就是使用内存中的缓存数据，可以使用HashMap、数组等数据结构来做缓存

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

---



### 缓存穿透！

当查询Redis中没有的数据时，该查询会下沉到数据库层，同时数据库层也没有该数据，当这种情况大量出现或被恶意攻击时，接口的访问全部透过Redis访问数据库，而数据库中也没有这些数据，每次都返回null。如果每次查询都会走数据库，则缓存就失去了意义，就像穿透了缓存意义。我们称这种现象为"缓存穿透"

1. 应用服务器压力变大了
2. Redis命中率减低了，访问缓存（redis)获取不存在的信息
3. 一直查询数据库 =》导致数据库压力增加，导致崩溃
4. 上述为:缓存穿透
5. **解决方案：**
   - 设置可访问的名单（白名单）
     - 使用bitmaps定义可以访问的名单，每次访问时进行比较，存在可以进行访问，否则进行拦截，不允许访问
     - 缺点：每次需要进行比较
   - 采用布隆过滤器
     - 底层是优化的bitmaps
   - 进行实时监控
     - 当发现redis的命中率开始急速减低，需要排查访问对象和访问数据，可以进行黑名单限制服务

### 缓存击穿！

击穿于穿透的区别是：穿透表示底层数据库没有数据且缓存内也没有数据，击穿表示底层数据库有数据而缓存内没有数据。

当**某个key**设置了过期时间，从缓存内正好失效时，大量访问**同时请求这个数据**，就会将查询下沉到数据库层，此时数据库层的负载压力会骤增，我们称这种现象为"缓存击穿"。

#### 特点、现象

1. 数据库访问压力瞬时增加
2. Redis里面没有出现大量的key过期
3. Redis正常运行

#### 解决方案

1. 预先设置热门数据
   - 在redis高峰访问之前，把一些热门数据提取存入到redis，加大这些key的时长
2. 实施调整
   - 实时监控，实施调整key的过期时间
3. 使用锁
   - 锁机制
   - 大量并发时，只让一个请求可以获取到查询数据库的锁，其他请求需要等待，查到以后释放锁，其他请求获取到锁后，先查缓存，缓存中有数据，就不用查数据库

### 缓存雪崩！

缓存雪崩是缓存击穿的"大面积"版，缓存击穿是数据库缓存到Redis内的热点数据失效导致大量并发查询穿过redis直接击打到底层数据库，而缓存雪崩是指Redis中大量的key几乎同时过期，然后大量并发查询穿过redis击打到底层数据库上，此时数据库层的负载压力会骤增，我们称这种现象为"缓存雪崩"。

事实上缓存雪崩相比于缓存击穿更容易发生，对于大多数公司来讲，同时超大并发量访问同一个过时key的场景的确太少见了，而大量key同时过期，大量用户访问这些key的几率相比缓存击穿来说明显更大。

#### 特点、现象

1. 数据库压力变大，最终导致服务器崩溃
2. **在极少时间段，查询的key大量过期**

#### 解决方案

1. 构建多级缓存架构
   - nginx缓存+redis缓存+其他缓存  等等
2. 使用锁或者队列
   - 不适合高并发场景
3. 设置过期标识更新缓存
   - 记录缓存时间是否过期（设置提前量），如果过期会触发通知另外的线程在后台去更新实际key的缓存
4. 将缓存失效时间分散开
   - 例如：在原有的失效时间基础上加一个随机值，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件

### 缓存预热

1. 缓存预热如字面意思，当系统上线时，缓存内还没有数据，如果直接提供给用户使用，每个请求都会穿过缓存去访问底层数据库，如果并发大的话，很有可能在上线当天就会宕机，因此我们需要在上线前先将数据库内的热点数据缓存至Redis内再提供出去使用，这种操作就成为"缓存预热"。
2. 缓存预热的实现方式有很多，比较通用的方式是写个批任务，在启动项目时或定时去触发将底层数据库内的热点数据加载到缓存内

### 缓存更新

### 缓存降级













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

1. 文件事件处理器
   - redis基于reactor模式开发了网络事件处理器，这个处理器叫做文件事件处理器，file event handler。这个文件事件处理器，是单线程的，redis才叫做单线程的模型，采用IO多路复用机制同时监听多个socket，根据socket上的事件来选择对应的事件处理器来处理这个事件。
   - 如果被监听的socket准备好执行accept、read、write、close等操作的时候，跟操作对应的文件事件就会产生，这个时候文件事件处理器就会调用之前关联好的事件处理器来处理这个事件。
   - 文件事件处理器是单线程模式运行的，但是通过IO多路复用机制监听多个socket，可以实现高性能的网络通信模型，又可以跟内部其他单线程的模块进行对接，保证了redis内部的线程模型的简单性。
   - 文件事件处理器的结构包含4个部分：多个socket，IO多路复用程序，文件事件分派器，事件处理器（命令请求处理器、命令回复处理器、连接应答处理器，等等）。
   - 多个socket可能并发的产生不同的操作，每个操作对应不同的文件事件，但是IO多路复用程序会监听多个socket，但是会将socket放入一个队列中排队，每次从队列中取出一个socket给事件分派器，事件分派器把socket给对应的事件处理器。
   - 然后一个socket的事件处理完之后，IO多路复用程序才会将队列中的下一个socket给事件分派器。文件事件分派器会根据每个socket当前产生的事件，来选择对应的事件处理器来处理。
2. 文件事件
   - 当socket变得可读时（比如客户端对redis执行write操作，或者close操作），或者有新的可以应答的sccket出现时（客户端对redis执行connect操作），socket就会产生一个AE_READABLE事件。
   - 当socket变得可写的时候（客户端对redis执行read操作），socket会产生一个AE_WRITABLE事件。
   - IO多路复用程序可以同时监听AE_REABLE和AE_WRITABLE两种事件，要是一个socket同时产生了AE_READABLE和AE_WRITABLE两种事件，那么文件事件分派器优先处理AE_REABLE事件，然后才是AE_WRITABLE事件。
3. 文件事件处理器
   - 如果是客户端要连接redis，那么会为socket关联连接应答处理器
   - 如果是客户端要写数据到redis，那么会为socket关联命令请求处理器
   - 如果是客户端要从redis读数据，那么会为socket关联命令回复处理器
4. 客户端与redis通信的一次流程
   - 在redis启动初始化的时候，redis会将连接应答处理器跟AE_READABLE事件关联起来，接着如果一个客户端跟redis发起连接，此时会产生一个AE_READABLE事件，然后由连接应答处理器来处理跟客户端建立连接，创建客户端对应的socket，同时将这个socket的AE_READABLE事件跟命令请求处理器关联起来。
   - 当客户端向redis发起请求的时候（不管是读请求还是写请求，都一样），首先就会在socket产生一个AE_READABLE事件，然后由对应的命令请求处理器来处理。这个命令请求处理器就会从socket中读取请求相关数据，然后进行执行和处理。
   - 接着redis这边准备好了给客户端的响应数据之后，就会将socket的AE_WRITABLE事件跟命令回复处理器关联起来，当客户端这边准备好读取响应数据时，就会在socket上产生一个AE_WRITABLE事件，会由对应的命令回复处理器来处理，就是将准备好的响应数据写入socket，供客户端来读取。
   - 命令回复处理器写完之后，就会删除这个socket的AE_WRITABLE事件和命令回复处理器的关联关系。

![image-20220508205702299](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/redis_img/202205082057463.png)



为什么redis单线程模型也能效率这么高实现高并发

1. **非阻塞的IO多路复用程序**
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

