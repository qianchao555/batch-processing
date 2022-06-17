## GP库

文档：https://gp-docs-cn.github.io/docs/best_practices/summary.html

https://cn.greenplum.org/

Greenplum数据库是一种shared nothing的分析型MPP(大规模并行处理架构)数据库，其架构特别针对管理大规模分析型数据仓库以及商业智能工作负载而设计

MPP：也称shared nothing架构，指有两个或者更多个处理器协同执行一个操作的系统，每一个处理器都有自己的内存、操作系统和磁盘

### Greenplum架构

Greenplum数据库通过将数据和处理 负载分布在多个服务器或主机上来存储和处理大量的数据

Greenplum是基于PostgreSQL数据库组成的阵列，阵列中的数据库工作在一起呈现了一个单一数据库的景象

Master节点是Greenplum数据库系统的入口，客户端会连接到这个数据库实例并提交SQL语句，Master协调系统中其他的Segment的数据库实例一起工作，Segment负责存储和处理数据

![image-20220617151820550](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171518218.png)



#### Greenplum-master

master是整个Greenplum数据库系统的入口，它负责接受连接和SQL查询，并且把工作分布到Segment实例上

用户与Greenplum交互时(通过master)，会觉得是在于一个典型的PostgreSQL数据库交互。使用类似psql之类的客户端或JDBC、ODBC等应用编程接口连接到数据库

master不包含任何用户的数据，数据只存储在Segment。master会认证客户端连接、处理到来的SQL命令、在segment直接分布工作负载、协调每一个segment返回的结果以及把最终结果呈现给客户端程序

Greenplum数据库使用预写式日志（WAL）来实现主/备镜像。在基于WAL的日志中，所有的修改都会在应用之前被写入日志，以确保对于任何正在处理的操作的数据完整性。

注意：Segment镜像还不能使用WAL日志

个人理解：类似与Mycat，用户与Mycat交互

#### Greenplum-segment

segment实例就是独立的PostgreSQL数据库，每一个实例都存储了数据的一部分，并且执行查询处理的主要部分

用户通过Greenplum的master连接到数据库，并且发出一个查询时，每个一segment数据库都会创建一些进程来处理该查询的工作

#### Greenplum-Interconnect

Interconnect是Greenplum数据库架构中的网络层

Interconnect指的是Segment之间的进程间通信以及这种通信所依赖的网络基础设施

Greenplum的Interconnect采用标准的以太交互网络

默认情况下，Interconnect使用带流控制的用户数据包协议（UDPIFC）在网络上发送消息。Greenplum软件在UDP之上执行包验证。这意味着其可靠性等效于传输控制协议（TCP）且性能和可扩展性要超过TCP。如果Interconnect被改为TCP，Greenplum数据库会有1000个Segment实例的可扩展性限制。对于Interconnect的默认协议UDPIFC则不存在这种限制



---

### 查询数据

#### Greenplum的查询处理

##### 查询规划与分发

master接收、解析并且优化查询。作为结果的查询计划可能是并行的或者定向的，master会把并行查询计划分发到所有的segment

![image-20220617163947377](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171639111.png)



若是定向查询，例如：单行的insert、update、delete、select操作或表分布键列过滤的查询，则master会把定向查询计划分发到单一的一个segment。这些查询中，查询计划不会被分发的所有的segment，而是定向给包含受影响或相关行的segment

![image-20220617164429256](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171644862.png)



##### Greenplum查询计划

查询计划是Greenplum数据库将要执行以产生查询答案的操作集合。计划中的每个*节点*或者步骤表示一个数据库操作，例如表扫描、连接、聚集或者排序。**计划的读取和执行按照从底向上的顺序进行**

Greenplum数据库还有一种额外的被称为*移动*的操作类型。移动操作涉及到在查询处理期间在Segment之间移动元组。并非每一个查询都需要移动操作。例如，定向查询计划就不需要通过Interconnect移动数据。

为了在查询执行期间达到最大并行度，**Greenplum将查询计划的工作划分成切片**。切片是Segment能够在其上独立工作的计划片段。**只要有一个移动操作出现在查询计划中，该查询计划就会被切片**，在移动的两端分别有一个切片

考虑下面两个表之间的连接的简单查询：

~~~sql
SELECT customer, amount
FROM sales JOIN customer USING (cust_id)
WHERE dateCol = '04-30-2016';
~~~

每个Segment都接收一个查询计划的拷贝，并且并行的根据计划工作

这个例子的查询计划中有一个重分布移动：它在Segment之间移动元组，已完成连接，重分布移动是必要的，因为customer表在Segment上按照cust_id分布，而sales表是按照sale_id分布。为了执行该连接，sales元组必须按照cust_id重新分布。该计划在重分布移动操作的两边被切换，形成了*slice 1*和*slice 2*。

这个查询计划由另一种称为***收集移动*的移动操作**。收集操作表示Segment何时将结果发回给Master，Master再将结果呈现给客户端。由于只要有移动产生查询计划就会被切片，这个计划在其最顶层也有一个隐式的切片（*slice 3*）。不是所有的查询计划都涉及收集移动。例如，一个CREATE TABLE x AS SELECT...语句不会有收集移动，因为元组都被发送到新创建的表而不是发给Master

![image-20220617171148577](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171711860.png)

##### Greenplum并行查询及执行

Greenplum会创建若干数据库进程来处理查询的工作。在Master上，查询工作者进程被称作*查询分发器*（QD）。QD负责创建并且分发查询计划。它也收集并且表达最终的结果。在Segment上，查询工作者进程被称为*查询执行器*（QE）。QE负责完成它那一部分的工作并且与其他工作者进程交流它的中间结果。

对查询计划的每一个*切片*至少要分配一个工作者进程。工作者进程独立地工作在分配给它的那部分查询计划上。在查询执行期间，每个Segment将有若干进程并行地为该查询工作。

为查询计划的同一个切片工作但位于不同Segment上的相关进程被称作*团伙*。随着部分工作的完成，元组会从一个进程团伙流向查询计划中的下一个团伙。这种Segment之间的进程间通信被称作Greenplum数据库的*Interconnect*组件。

[图 4](http://docs-cn.greenplum.org/v5/admin_guide/query/topics/parallel-proc.html#topic4__iy141495)展示了[图 3](http://docs-cn.greenplum.org/v5/admin_guide/query/topics/parallel-proc.html#topic3__iy140224)所示查询计划在Master和两个Segment实例上的查询工作者进行

![image-20220617171325585](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171713725.png)



查看GP版本信息

![image-20220617150404854](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/pg_gp_base_img/202206171506183.png)

Greenplum查看数据分布情况：

~~~sql
select gp_segment_id,count(*) from tablename group by 1 order by 1;
~~~



