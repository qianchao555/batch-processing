## Mycat 2

分布式数据库中间件

### 入门概述

Cobar：阿里开发，多年无维护更新

Mycat：基于Cobar二次开发，开源社区维护

#### 读写分离

![image-20220615214258959](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152143772.png)



#### 数据分片

垂直拆分：分库

水平拆分：分表

垂直+水平拆分：分库分表

![image-20220615214819239](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152148505.png)





#### 多数据源整合

![image-20220615214901004](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152149130.png)



#### 原理

Mycat原理中最重要的就是：拦截，它拦截用户发送过来的SQL语句，首先对SQL语句做一些特定的分析：比如分片分析、路由分析、读写分离分析、缓存分析等等，然后将此SQL发送到后端数据库，并将返回的结果做适当的处理，最终返回给用户

![image-20220615215424084](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152154217.png)



####  映射模型区别

![image-20220615222256882](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152222855.png)



![image-20220615222505302](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mycat_img/202206152225408.png)



### Mycat2相关概念

#### 分库分表

按照一定规则把数据库中的表拆分为多个带有数据库实例，物理库，物理表访问路径的分表

例如：

分库：电商项目，分为用户库、订单库等等

分表：订单表数据达到千万级别，达到mysql单表瓶颈，需要将数据分到多个数据库中的多张表中去



#### 逻辑库

数据库代理中的数据库，它可以包含多个逻辑表

Mycat里面定义的库，逻辑上存在，物理上在MySQL里面并不存在。有可能是多个MySQL数据库共同组成一个逻辑库

#### 逻辑表

数据库代理中的表，它可以映射代理连接的数据库中的表(物理表)

Mycat里面定义的表，逻辑上存在，可以映射的真实MySQL数据库的表，可以一对一、一对多



#### 物理库

MySQL里面真实数据库

#### 物理表

MySQL里面真实数据库里面的表



#### 拆分键

即：分片键，描述拆分逻辑表的数据规则的字段

按照哪个字段拆分，哪个字段就是拆分键

#### 物理分表

已经进行数据拆分的，在数据库上面的物理表，是分片表的一个分区

 多个物理分表里面的数据汇总，就是逻辑表的全部数据

#### 全局表、广播表

#### ER表

狭义：父子表中的子表，它的分片键指向父表的分片键

广义：具有相同数据分布的一组表  



---

### 配置文件

#### 服务

默认即可

#### 用户

配置用户相关信息



---

### 分库分表

两台主机上的两个数据库中的表，能关联查询吗？

不可以关联查询

分库原则：有紧密关联关系的表应该在一个库里面，相互没有关联关系的表可以分到不同的库



#### 全局序列

Mycat2默认使用雪花算法生成全局序列号，或使用官方提供的数据库脚本




