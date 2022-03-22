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

www.reids.cn

redis中的数据都是以key-value形式存储的，五大数据类型主要指value的数据类型

#### Redis键（key)

#### 字符串String

1. String是Redis最基本的类型，一个key对应一个value
2. 是二进制安全的，意味着Redis的String可以包含任何数据。比如：jpg图片、序列化对象等等
3. 一个Redis中，字符串value大小上限最多可以是512M

##### 底层数据结构

1. String的数据结构为**简单动态字符串**
2. 是可以修改的字符串
3. 内部结构类似与Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配

---

#### 列表List

1. 单键多值
2. 是一个简单的字符串列表，按照插入顺序排列，可以添加一个元素到列表的头部(left)或者尾部(right)，也可以从list的头部或者尾部弹出一个元素
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
1. set中的元素是不可以重复的

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

#### RDB  Redis Data Base

快照持久化

1. 在指定**时间间隔**内将内存中的**数据集快照**写入磁盘，也就是Snapshot快照，redis重启时，将快照文件读入内存中

1. redis中的快照持久化默认开启的

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
   - 启动redis服务时，默认会去读取这个文件
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
4. 如果只做纯内存缓存服务器，可以不使用任何持久化方式

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



### 缓存穿透！

当查询Redis中没有的数据时，该查询会下沉到数据库层，同时数据库层也没有该数据，当这种情况大量出现或被恶意攻击时，接口的访问全部透过Redis访问数据库，而数据库中也没有这些数据，我们称这种现象为"缓存穿透"

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

当热点数据key从缓存内失效时，大量访问同时请求这个数据，就会将查询下沉到数据库层，此时数据库层的负载压力会骤增，我们称这种现象为"缓存击穿"。

redis某个key过期了，大量访问中使用这个key

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
   - 缓存在失效的时候（判断拿出来的值为空），不是立即去加载DB
   - 锁机制 如何做 继续学习！

### 缓存雪崩！

缓存雪崩是缓存击穿的"大面积"版，缓存击穿是数据库缓存到Redis内的热点数据失效导致大量并发查询穿过redis直接击打到底层数据库，而缓存雪崩是指Redis中大量的key几乎同时过期，然后大量并发查询穿过redis击打到底层数据库上，此时数据库层的负载压力会骤增，我们称这种现象为"缓存雪崩"。事实上缓存雪崩相比于缓存击穿更容易发生，对于大多数公司来讲，同时超大并发量访问同一个过时key的场景的确太少见了，而大量key同时过期，大量用户访问这些key的几率相比缓存击穿来说明显更大。

在极少时间段，查询的key大量过期

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

![image-20220322142942618](https://gitee.com/qianchao_repo/pic-typora/raw/master/img/image-20220322142942618.png)

这是一种`本地加锁`的方式，在`分布式`情况下会带来数据不一致的问题：比如服务 A 获取数据后，更新缓存 key =100，服务 B 不受服务 A 的锁限制，并发去更新缓存 key = 99，最后的结果可能是 99 或 100，但这是一种未知的状态，**与期望结果不一致**

基于本地锁的问题，我们需要一种支持**分布式集群环境**下的锁：查询 DB 时，只有一个线程能访问，其他线程都需要等待第一个线程释放锁资源后，才能继续执行

上锁之后，对整个分布式系统中的机器都生效



#### 分布式锁的实现方式

1. 基于数据库分布式锁
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



##### Redisson锁续约 

WatchDog 看门狗

目的是为了某种场景下保证业务不影响，如任务执行超时但未结束，锁已经释放的问题

当一个线程持有了一把锁，由于并未设置超时时间leaseTime，Redisson默认配置了30S，开启watchDog，每10S对该锁进行一次续约，维持30S的超时时间，直到任务完成再删除锁





分布式锁和同步器

https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8#84-%E7%BA%A2%E9%94%81redlock

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

基于Redis的Redisson红锁RedissonRedLock对象实现了[Redlock](http://redis.cn/topics/distlock.html)加锁算法，该对象也可以用来将多个`RLock`对象关联为一个红锁，每个`RLock`对象实例可以来自于不同的Redisson实例

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





























