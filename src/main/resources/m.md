### MySQL分库分表

#### 水平切分

1. 水平切分又叫：Sharding，它是将同一个表中的记录拆分到多个结构相同的表中。
2. 当一个表的数据不断增多时，Sharding 是必然的选择，它可以将数据分布到集群的不同节点上，从而缓解单个数据库的压力。
3. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/mysql/63c2909f-0c5f-496f-9fe5-ee9176b31aba.jpg)

#### 垂直切分

1. 垂直切分是将一张表切分成多个表，通常按照列的关系密集程度进行切分，也可以将经常使用的列和不经常使用的列切分到不同的表中
2. 在数据库的层面使用垂直切分将按数据库中表的密集程度部署到不同的库中，例如将原来的电商数据库垂直切分成商品数据库、用户数据库等
3. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/mysql/e130e5b8-b19a-4f1e-b860-223040525cf6.jpg)

#### Sharding策略

1. 哈希取模：hash(key)%Num_Db
2. 范围:可以是ID范围，也可以是时间范围
3. 映射表：使用单独的一个数据库来存储映射关系

### MySQL一条SQL的执行过程详解

#### MySQL驱动

1. 系统和MySQL数据库进行交互前，MySQL驱动会帮我们建立连接。一次SQL请求就会建立一个连接，多个请求就会建立多个连接。由于性能原因，引入数据库连接池

   ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-1.png)

2. 数据库连接池

   - 维护一定的连接数，方便系统获取连接，使用时从池子中获取，用完之后放回去就可以了。我们不用关系连接的创建和销毁，因为不必关心线程池是怎么去维护这些连接的
   - 常见的数据库连接池Druid、C3P0、DBCP
   - 好处：大大节省了不断创建和销毁连接而带来的性能开销

   ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-3.png)

#### 数据库连接池

1. MySQL架构提供了数据库连接池，双方都是通过数据库连接池来管理各个连接的，这样一方面线程之前不需要是争抢连接，更重要的是不需要反复的创建的销毁连接
2. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-4.png)

#### 网络连接必须由线程来处理

1. 所谓网络连接，说白了就是一次请求，每次请求都会有相应的线程去处理。对于 SQL 语句的请求在 MySQL 中是由一个个的线程去处理的
2. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-5.png)

#### SQL接口

MySQL中处理请求的线程在获取到请求以后，获取的SQL语句交给SQL接口去处理

#### 查询解析器

~~~sql
--例如：以下SQL
select studentName from students where id=1;
~~~

1. 解析器将SQL接口传递过来的SQL语句进行解析，翻译成MySQL数据库自己能认识的语言
2. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-6.png)
3. 现在请求的SQL语句已经解析为MySQL数据库能认识的样子了，下一步MySQL会按照自己任务效率最高的方式去执行SQL语句

#### MySQL查询优化器

1. MySQL会为我们生成一条条的执行计划。比如你创建了多个索引等等
2. 优化器选出最优索引等步骤后，由执行器去调用存储引擎接口，开始去执行被MySQL解析过和优化过的SQL语句
3. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-7.png)

#### 执行器

1. 执行器是一个非常重要的组件，因为前面那些组件的操作最终必须通过执行器去调用存储引擎接口才能被执行。执行器最终会根据一系列的执行计划去调用存储引擎的接口去完成 SQL 的执行
2. ![img](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/db/mysql/db-mysql-sql-8.png)

#### 存储引擎

查询优化器会调用存储引擎的接口，去执行 SQL，也就是说真正执行 SQL 的动作是在存储引擎中完成的。数据是被存放在内存或者是磁盘中的（存储引擎是一个非常重要的组件，后面会详细介绍















