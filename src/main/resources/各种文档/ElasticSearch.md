## ElasticSearch

极客时间

1. 





























> ES是一款非常强大的、基于Lucene的开源**搜索及分析引擎**
>
> 它是一个实时的分布式搜索分析引擎，能让你以前所未有的速度和规模去探索你的数据

它被用作==全文索引、结构化搜索、分析==，以及这三个功能的组合

除了搜索，结合Kibana、Logstash、Beats开源产品、Elastic技术栈（ELK)还被广泛运用在大数据近实时分析领域。包括：日志分析、指标监控、信息安全等等。它可以帮助我们探索海量结构化、非结构化数据，按需创建可视化报表，对监控数据设置报警阈值，通过机器学习，自动识别异常状况

ES基于Restful Web Api，使用Java开发的搜索引擎库，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎



应用场景

1. 开源搜索引擎，例如Github上搜索及高亮
2. 网上购物的商品推荐
3. 打车回家的时候帮助你定位附件的乘客或司机
4. ELK
5. 大数据分析
6. 等等





### 为什么不直接使用Lucene

> ElasticSearch是基于Lucene的



Lucene：**全文搜索引擎**

- Lucene是当下最先进、高性能、全功能的搜索引擎库，但是 ==Lucene 仅仅只是一个库==
- Java语言开发的，Hadoop之父开发Doug Cutting开发的
- 只能基于Java语言开发，类库的接口学习难度高
- 原生不支持水平扩展



2004年，ES创始人`Shay Banon`基于Lucene开发了Compass

2010年，Shay Banon重写了Compass,取名ElasticSearch

- 支持分布式、可水平扩展

- 降低全文索引的学习曲线，可以被任何编程语言调用

   

![image-20250104205130113](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250104205132977.png)



Elasticsearch 也是使用 Java 编写的，它的内部使用 Lucene 做索引与搜索，但是它的目的是使全文检索变得简单，**通过隐藏 Lucene 的复杂性，取而代之的提供一套简单一致的 Restful Web API**

然而，ES不仅仅是Lucene，并且也不仅仅是一个全文搜索引擎，它可以被这样准确的描述：

1. 一个分布式的==实时文档存储==，每个字段都可以被索引与搜索
2. 一个分布式==实时分析搜索引擎==
3. 能胜任上百个服务器节点的扩展，并**支持PB级别的结构化或非结构化数据**
   - PB级别的数据存储容量：1024GB=1TB，1024TB=1PB





#### lucene实现全文索引流程

![image-20220623212318865](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206232123692.png)





### ES主要功能与使用场景

主要功能

1. 海量数据的分布式存储以及集群管理
   - 实现服务与数据的高可用、水平扩展
2. 近实时搜索、性能卓越
   - 对结构化、全文、地理位置等数据类型的处理
3. 海量数据的近实时分析
   - 聚合功能



使用场景

1. 网站搜索、垂直搜索、代码搜索
2. 日志管理于分析、安全指标监控、应用性能监控、web抓取舆情分析等等



### Elastic Stack生态(ELK)

> Beats+ElasticSearch+Logstash+Kibana

下图展示了ELK生态以及基于ELK的场景

![image-20220619214741901](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192147011.png)





X-Pack最初是收费的，在2018年的时候开源了，提供了几个订阅版本，base版是免费使用的

 

![image-20220619214931561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192149659.png)





#### Beats

> ES对它的定义是轻量的数据采集器
>
> Beats是一个面向**轻量型采集器**的平台，这些采集器可以从边缘机器向Logstash、ElasticSearch发送数据

它是由Go语言进行开发的，运行效率方面比较快。



从下图中可以看出，不同Beats的套件是针对不同的数据源。

1. Filebeat：用于监听日志数据，可以替代logstash-input-file
2. packetbeat：用于监控网络流量
3. winlogbeat：用于搜索windows事件日志
4. 。。。

![image-20230407234355171](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304072343966.png)





#### Logstash

> Logstash是**动态数据收集管道**，拥有可扩展的插件生态系统，支持从不同来源采集数据，转换数据，并将数据发送到不同的存储库中。其能够与ElasticSearch产生强大的协同作用，后被Elastic公司在2013年收购

2009年诞生最初用于日志处理，2013年被ES收购



它具有如下特性：

1. 几乎可以访问任何数据
2. 实时解析和转换数据
   - 例如：从Ip地址破译出地理坐标
3. 可扩展
   - 具有200多个插件（日志/数据库/Arcisigh/Netflow)
4. 可靠性、安全性
   - Logstash会通过持久化队列来保证至少将运行中的事件送达一次
   - 同时将数据进行传输加密
5. 监控





主要组成部分：

1. Shipper—发送日志数据
2. Broker—收集数据，缺省内置Redis
3. Indexer—数据写入

Logstash提供了很多功能强大的滤网以满足你的各种应用场景。是一个input | filter | output 的数据流



#### ElasticSearch

ElasticSearch对数据进行**搜索、分析和存储**，它**是基于JSON的分布式搜索和分析引擎**，专门为实现水平可扩展性、高可靠性和管理便捷性而设计的

它的实现原理主要分为以下几个步骤

1. 首先用户将数据提交到ElasticSearch数据库中
2. 再通过分词控制器将对应的语句分词
3. 将分词结果及其权重一并存入，以备用户在搜索数据时，根据权重将结果排名和打分，将返回结果呈现给用户



#### Kibana

> Kibana=Kiwifruit+Banana,奇异果+香蕉
>
> 最早是基于Logstash的工具，2013年加入ES



Kibana实现**数据可视化**，其作用就是在ElasticSearch中进行搜索并展示。Kibana能够以图表的形式呈现数据，并且具有可扩展的用户界面，可以全方位的配置和管理ElasticSearch

1. Kibana可以提供各种可视化的图表
2. 可以通过机器学习的技术，对异常情况进行检测，用于提前发现可以问题



一个典型的日志系统包括

1. 收集：能够从多种数据源采集日志数据
2. 传输：能稳定的把日志数据解析过滤，并传输到存储系统
3. 存储：存储日志数据
4. 分析：支持UI分析
5. 告警：能够提供错误报告、监控机制





日志收集系统的演变

1. beats+es+kibana

   - beats采集数据、存储在ES中、Kibana进行展示
2. beats+logstash+es+kibana

   - Logstash具有基于磁盘的自适应缓冲系统，该系统将吸收传入的吞吐量，从而减轻背压
3. beats+MQ+logstash+es+kibana

   - 加MQ的好处：降低对日志机器的影响

   - 如果有很多台机器需要做日志收集，那么让每台机器都向Elasticsearch持续写入数据，必然会对Elasticsearch造成压力，因此需要对数据进行缓冲，同时，这样的缓冲也可以一定程度的保护数据不丢失






### ES架构

![image-20220623213234829](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206232132945.png)

1. Gateway是ES用来存储索引的文件系统，支持多种类型
2. Gateway的上层是一个分布式的lucene框架。
3. Lucene之上是ES的模块，包括：索引模块、搜索模块、映射解析模块等
4. ES模块之上是 Discovery、Scripting和第三方插件。
5. Discovery是ES的节点发现模块，不同机器上的ES节点要组成集群需要进行消息通信，集群内部需要选举master节点，这些工作都是由Discovery模块完成。支持多种发现机制，如 Zen 、EC2、gce、Azure。
6. Scripting用来支持在查询语句中插入javascript、python等脚本语言，scripting模块负责解析这些脚本，使用脚本语句性能稍低。ES也支持多种第三方插件。
7. 再上层是ES的传输模块和JMX.传输模块支持多种传输协议，如 Thrift、memecached、http，默认使用http。JMX是java的管理框架，用来管理ES应用。
8. 最上层是ES提供给用户的接口，可以通过RESTful接口和ES集群进行交互







### ES基础概念-相关名词

- Near Realtime(NRT) 
  - 近实时，数据提交索引后，立马就可以搜索到
- Cluster集群
  - 一个集群由一个唯一的名字标识，默认为elasticsearch。集群名称非常重要，**具有相同集群名的节点才会组成一个集群**，集群名称可以在配置文件中指定
- Node节点
  - 存储集群的数据，参与集群的索引和搜索功能。
  - 每个节点也有自己的名称，默认在启动时会以一个随机的UUID的前七个字符作为节点的名字，可以为其指定任意的名字
  - 通过集群名在网络中发现同伴组成集群。一个节点也可是集群
- Index索引
  - 一个索引是一个**文档的集合**
  - 每个索引有唯一的名字，通过这个名字来操作它
  - 一个集群中可以有任意多个索引
- Type类型
  - 指在一个索引中，可以索引不同类型的文档，如用户数据、博客数据
  - 从6.0.0 版本起已废弃，一个索引中只存放一类数据
- Document文档
  - 被索引的一条数据，索引的基本信息单元，以JSON格式来表示
  - 类似与关系型数据库中的一条记录
- 字段
  - 文档中的字段，对文档数据根据不同属性进行分类的标识
  - 相当于关系型数据库中一条记录的一个字段
- 映射
  - 是处理数据的方式和规则方面做一些限制
  - 如某个字段的数据类型、默认值、分析器、是否被索引等等，这些都是映射里面可以设置的
  - 其它就是处理es里面数据的一些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射，并且需要思考如何建立映射才能对性能更好
- 分片
  - 在创建一个索引时，可以指定分成多少个分片来存储
  - 每个分片也是一个功能完善且独立的'索引'，可以被放置在集群的任意节点上
- Replication
  - 备份，一个分片可以有多少个副本备份







### 文档

ES是面向文档的，文档是所有可搜索数据的最小单位

- 日志文件中的每一个日志项，就是一个文档
- 一本电影的具体信息、一张唱片的信息，就是一个文档
- 一首歌、一篇pdf文档中的具体内容，就是一个文档
- 等等



文档会被序列化为Json格式，存储在ES中

- Json文档格式灵活，不需要预定义格式
- 字段类型可以手动指定或者通过ES自动推算
  - 生产环境中，推荐使用手动Mapping,自动推算可能不准确



每个文档具有Unique Id

- 可以自己指定id、可以ES自己生成



#### 文档的元数据

> 用于标注文档的相关信息

- _index：文档所在的索引名称
- _type：文档的所属类型，ES7开始，只有一个那就是 _doc
- _id：文档唯一id
- _score：相关性打分
- _version：文档版本，若文档修改了，该字段会增加
- _source：文档的原始Json数据

~~~json
{
  "_index" : "test_logs2",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "_seq_no" : 0,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "uid" : 1,
    "username" : "test"
  }
}
~~~



### 集群

Cerebro：ES可视化集群工具

ES天生就是分布式的，它知道如何通过管理多个节点来提高扩容性和可用性，这也意味着我们的应用无需关注这个问题



Master eligible Nodes

- 每个节点启动后，默认就是一个Master eligible节点
- eligible节点可以参加选主流程，成为Master
- 第一个节点启动的时候，它会将自己选举出Master节点
- 每个节点上都保存了集群的状态，只有master节点才能修改集群的状态信息



应对故障

> ES可以应对节点故障，当主节点发生故障时，会立即选举出一个新的主节点



Data Node

- 可以保存数据的节点，负责保存分片信息，在数据扩展上起到了至关重要的作用



Coordinating Node

- 负责接收Client的请求，将请求分发到合适的节点，最终把结果汇聚在一起
- 每个节点默认起到了Coordinating Node的职责



Hot& Warm Node

- 不同硬件配置的Data Node，用来实现Hot & Warm 架构，降低集群部署的成本



Machine Learning Node

- 负责跑机器学习的Job，用来做异常检测



Tribe Node

- 5.3开始采用Cross Clustor Search,Tribe Node连接到不同ES集群，并支持将这些集群当作一个单独的集群处理



节点启动时候，读取配置文件，知道自己承担什么样的角色 



当一个节点被选举成为 *主* 节点时， 它将负责管理集群范围内的所有变更，例如增加、删除索引，或者增加、删除节点等。 而**主节点并不需要涉及到文档级别的变更和搜索等操作**，所以当集群只拥有一个主节点的情况下，即使流量的增加它也不会成为瓶颈。 任何节点都可以成为主节点

作为用户，我们可以将请求发送到 *集群中的任何节点* ，包括主节点。 每个节点都知道任意文档所处的位置，并且能够将我们的请求直接转发到存储我们所需文档的节点。 无论我们将请求发送到哪个节点，它都能负责从各个包含我们所需文档的节点收集回数据，并将最终结果返回給客户端。 Elasticsearch 对这一切的管理都是透明的



### 集群分布式及选主问题

- ES分布式架构中，不同的集群通过名字来区分，默认名字是“elasticsearch”



节点

- 一个节点就是一个ES实例
  - 本质就是一个Java进程
  - 一台机器可以运行多个ES进程，但是生产一般一台机器一个ES实例
- 每个节点都有名字
- 每个节点在启动之后，会分配一个UID，保存在data目录下



Coordinating Node

- 处理请求的节点，叫Coordinating Node
  - 路由请求到正确的节点，例如：创建索引的请求，需要路由到Master
- 所有的节点默认是Coordinating Node



Data Node

- 保存数据的节点
  - 节点启动后，默认就是Data Node
- 保存分片数据
  - 由于Master Node决定如何把分片分发到数据节点上
- 通过增加数据节点
  - 可以解决数据水平扩展和解决数据单点问题

Master Node

- 职责
  - 处理创建、删除等请求/决定分片被分配到哪个节点/负责索引的创建删除
  - 维护并更新Cluster State
- 最佳实践
  - 部署要考虑单点问题
  - 为一个集群设置多个master/每个节点只承担Master的单一角色







#### 集群健康

> Elasticsearch 的集群监控信息中包含了许多的统计数据，其中最为重要的一项就是 *集群健康* ， 它在 `status` 字段中展示为 `green` 、 `yellow` 或者 `red`

~~~sh
GET /_cluster/health
~~~

~~~sh
{
   "cluster_name":          "elasticsearch",
   "status":                "green", 
   "timed_out":             false,
   "number_of_nodes":       1,
   "number_of_data_nodes":  1,
   "active_primary_shards": 0,
   "active_shards":         0,
   "relocating_shards":     0,
   "initializing_shards":   0,
   "unassigned_shards":     0
}
~~~

status字段

1. green：所有主分片和副本分片都正常运行
2. yellow：所有主分片运行正常，但不是所有的副本分片都正常运行
3. red：有的主分片没正常运行





#### 添加索引

> 往ES添加数据时，需要用到索引，它是保存数据的地方，索引实际上是指向一个或多个物理分片的逻辑命名空间。即：一个索引中的数据，可能保存在多个分片上面。（和kafka里面topic的分片类似）

一个分片是一个底层的工作单元，它仅保存了全部数据中的一部分，一个分片是一个Lucene实例，并且一个分片本身就是一个完整的搜索引擎

我们的文档被存储和索引到分片内，**但是应用程序是直接与索引进行交互而不是与分片进行交互**



Elasticsearch 是利用分片将数据分发到集群内各处的。分片是数据的容器，文档保存在分片内，分片又被分配到集群内的各个节点里。 当你的集群规模扩大或者缩小时， Elasticsearch 会自动的在各节点中迁移分片，使得数据仍然均匀分布在集群里



一个分片可以是主分片或副本分片，索引内任意一个文档都归属于一个主分片，所以主分片的数量决定着索引能保存的最大数据量。技术上来说一个主分片最大能存储的文档数量为：Interger.Max_value-128



一个副本分片只是一个主分片的拷贝，副本分片是作为硬件故障时保障数据不丢失的冗余备份，**并且为搜索和返回文档等读操作提供服务**

索引建立的时候就确定了主分片数，但是**副本分片是可以随时修改**的



例如：创建一个索引，分配3个主分片和一个副本(每个主分片一个副本)

~~~sh
PUT /blogs
{
   "settings" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1
   }
}
~~~

因为现在是单节点集群，所以3个主分片都被分配到了Node1

![image-20220622000210180](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206220002289.png)

再次查看集群健康状态

~~~sh
{
  "cluster_name": "elasticsearch",
  "status": "yellow", 
  "timed_out": false,
  "number_of_nodes": 1,
  "number_of_data_nodes": 1,
  "active_primary_shards": 3,
  "active_shards": 3,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 3, 
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 50
}
~~~



因为是单节点集群，所以副本并没有被分配到任何节点，即unassiged_shards：3这三个副本分片都没有分配到任何节点



#### 添加故障转移

> 集群中只有一个节点运行时，意味着单节点故障问题—没有设计冗余

启动一个节点时候，将cluster.name配置为之前集群的名字，这个节点就会**自动发现集群并加入到其中**



所有新添加的文档都将会保存在主分片上，然后被并行复制到对应的副本分片上。这样保证了既可以从主分片又可以从副本分片获得文档



#### 水平扩容

分片是一个功能完整的搜索引擎，它拥有使用一个节点上的所有资源的能力。

主分片的数目在索引创建的时候就确定下来了，实际上，这个数目定义了这个索引能够存储的最大数据量（实际大小取决于你的数据、硬件和使用场景）。但是，读取操作可以同时被主分片和副本分片处理，所以当拥有越多的副本分片时，就意味着拥有更高的吞吐量

![image-20230410104200452](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304101042297.png)

`Node 1` 和 `Node 2` 上各有一个分片被迁移到了新的 `Node 3` 节点，现在每个节点上都拥有2个分片，而不是之前的3个。 这表示每个节点的硬件资源（CPU, RAM, I/O）将被更少的分片所共享，每个分片的性能将会得到提升

分片是一个功能完整的搜索引擎，它拥有使用一个节点上的所有资源的能力。 我们这个拥有6个分片（3个主分片和3个副本分片）的索引可以最大扩容到6个节点，每个节点上存在一个分片，并且每个分片拥有所在节点的全部资源。



所以，水平扩容时候，可以**动态调整副本分片的数目**，按需伸缩集群

但是，**如果只是在相同节点数目**的集群上增加更多的副本分片并不能提高性能，因为每个分片从节点上获得的资源会变少。 你需要**增加更多的硬件资源**来提升吞吐量。同时，副本分片的增多会提高数据的冗余







### 分片

- 主分片，用以解决数据水平扩展问题
  - 通过主分片，可以将一个索引数据分布到集群内的不同节点上
  - 一个分片运行一个Lucene实例
  - 主分片数在索引创建时指定，后续不允许修改，除非ReIndex
- 副本
  - 副本分片数，可以动态调整
  - 增加副本数，可以一定程度上提供访问的可用性（读取的吞吐增加）



分片数的设定

- 对于生产环境中分片的设定，需要提前做好容量规划
- 分片数量过小
  - 后续无法增加节点实现水平扩展
  - 单个分片的数据量太大，导致数据重新分配时耗时
- 分片数量过大
  - 7.0开始，默认主分片设置为1
  - 影响搜索结果的相关性打分，影响统计结果的准确性



集群健康状况

- Green:健康状态，主分片与副本分片都正常
- yellow：亚健康，主分片全部正常使用，有副本分片不能使用
- Red:不健康，部分主分配不能使用

~~~json
{
  "cluster_name": "elasticsearch",
  "status": "yellow",
  "timed_out": false,
  "number_of_nodes": 1,
  "number_of_data_nodes": 1,
  "active_primary_shards": 32,
  "active_shards": 32,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 2,
  "unassigned_primary_shards": 0,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 94.11764705882352
}
~~~





分片与集群故障的转移

重新选择主分片，与kafka zk哪些类似



#### 分片的内部原理

- 什么是ES分片
  - ES中最小的工作单元（是一个Lucene的Index)
- 一些问题
  - 为什么ES的搜索是近实时的（1s后被搜索到）？
  - ES如何保证断电时数据不丢失？
  - 为什么删除文档，并不会立即释放空间？



##### 倒排索引的不可变性

- 倒排索引采用Immutable Design,一旦生成，不可更改
- 不可变性，带来的好处
  - 无需考虑并发写文件的问题，避免了锁机制带来的性能问题
  - 一旦读入内核的文件系统缓存，便留在那里。只要文件系统有足够的空间，部分请求就会直接请求内存，不会命中磁盘，提升了很大的性能
  - 缓存容易生成和维护，系统可以充分利用缓存
  - 倒排索引允许数据被压缩
- 不可变性，带来的挑战
  - ==如果需要让一个新的文档可以被索引，需要重建整个索引==



##### Lucene Index

- Lucene中，单个倒排索引文件被称为Segment，Segement是自包含的，不可变更的。多个Segment汇聚在一起，就称为Lucene的Index，其对应的就是ES中的一个Shard分片
- 当有新的文档写入时，会生成新的Segment，查询时，会同时查询所有的Segments，并对结果汇总。Lucene中有一个文件CommitPoint，用来记录所有的Segements信息
- 删除的文档信息，保存在`.del`文件中

![image-20250107210018237](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107210019887.png)



##### ES中的Refresh

![image-20250107210757851](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107210759162.png)

- ES在写入文档的时候，会先将文档写入Index Buffer的内存空间
- Index buffer中的文档写入Segment的过程，叫做Refresh,Refresh不执行fsync操作
  - 当文档写入Segment中后，这些文档就可以被搜索到了
- Refresh频率
  - 默认是1s发生一次，可配置
  - ==这就是为什么ES被称为近实时搜索的原因==
- 如果系统有大量的数据写入，就会产生很多的Segment
- Index buffer被占满时，会触发Refresh，默认大小是JVM的10%



##### Transaction Log

![image-20250107212123830](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107212125835.png)

- Segement写入磁盘的过程相对耗时，借助文件系统缓存，==Refresh时，先将Segment写入缓存以开放查询==
- 为了保证数据不丢失，所以在Index文档时，同时写入Transaction Log，高版本中，Transaction Log是默认落盘，每个分片有一个Transaction Log
- 在ES Refresh时，Index buffer被清空，Transaction Log不会清空
  - 这就是为什么ES断电后，数据不丢失，因为Transaction Log已经落盘了



##### ES的Flush

![image-20250107212626282](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107212627411.png)

- 调用Refresh，Index buffer清空并且Refresh
- 调用fsync(操作系统级别)，将缓存的Segments写入磁盘
- 清空Transaction Log
- 何时触发Flush（比较重）
  - 默认30min，调用一次
  - Transaction Log满（512MB)



##### ES的Merge

ES完成Flush后，Segments会写入磁盘，随着时间的推移，Segment会越来越多，所以会定期处理

ES的Merge主要做2个事情

- Segment很多，需要定期合并

  - 减少Segments/删除已经删除的文档

- ES和Lucene会自动进行Merge操作

  - ~~~
    post _index/_forcemerge
    ~~~











### 文档分布式存储

文档是存储在分片上

> 当索引(存储、查询）一个文档的时候，文档会被存储到一个主分片中。ES如何知道这个文档放到哪个分片？



- 文档会存储在具体的某个主分片和副本分片上
- 文档到分片的映射算法
  - 确保文档能均匀分布在所有分片上
  - 潜在的算法
    - 随机/Round Robin
    - 维护文档到分片的映射关系，当文档数据量大时，维护成本高
    - 实时计算，通过文档id，自动计算需要映射到哪个分片上



#### 路由一个文档到一个分片

ES文档到分片的路由算法

- shard=hash(_routing)%number_of_primary_shards
- hash算法确保文档均匀分散到分片中



1. `_routing`是一个可变值，默认为文档_id
   - 这个值，也可以设置为自定义的值
   - **通过这个参数我们可以自定义文档到分片的映射**。一个自定义的路由参数可以用来确保所有相关的文档——例如所有属于同一个用户的文档——都被存储到同一个分片中
2. `number_of_primary_shards`：主分片的数量
   - ES中，设置好Index Settings后，主分片数量不能修改的原因就是这个，会导致路由算法错误
   - **这就解释了为什么我们要在创建索引的时候就确定好主分片的数量 并且永远不会改变这个数量：因为如果数量变化了，那么所有之前路由的值都会无效，文档也再也找不到了**





#### 主分片与副本分片交互

假设有一个集群由3个节点组成。包含一个blogs索引，两个主分片，每个主分片两个副本分片。相同的主分片与副本分片不会放在同一个节点

![image-20220622163611805](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221636449.png)



我们可以发送请求到集群中的任一节点。 每个节点都有能力处理任意请求。 每个节点都知道集群中任一文档位置，所以可以直接将请求转发到需要的节点上。 

在下面的例子中，将所有的请求发送到master `Node 1` ，我们将其称为 *协调节点(coordinating node)* 

当发送请求的时候， 为了扩展负载，更好的做法是轮询集群中所有的节点。

#### 新建、索引、删除文档

![image-20220622164644609](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221646160.png)

这三个操作都是写操作，必须在主分片上面完成之后，才能被复制到相关的副本分片中

以下是在主副分片和任何副本分片上面 成功新建，索引和删除文档所需要的步骤顺序：

1. 客户端向 `Node 1` 发送新建、索引或者删除请求。
2. **节点使用文档的 `_id` 确定文档属于分片 0** 。因为分片 0 的主分片目前被分配在 `Node 3` 上，请求会被转发到 `Node 3`
3. `Node 3` 在主分片上面执行请求。如果成功了，它将请求并行转发到 `Node 1` 和 `Node 2` 的副本分片上。一旦所有的副本分片都报告成功, `Node 3` 将向协调节点报告成功，协调节点向客户端报告成功



#### 取回一个文档

可以从主分片或者从其它任意副本分片检索文档

![image-20220622165813493](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221658278.png)

以下是从主分片或者副本分片检索文档的步骤顺序：

1. 客户端向 `Node 1` 发送获取请求。
2. 节点使用文档的 `_id` 来确定文档属于分片 `0` 。分片 `0` 的副本分片存在于所有的三个节点上。 在这种情况下，它将请求转发到 `Node 2` 。
3. `Node 2` 将文档返回给 `Node 1` ，然后将文档返回给客户端

在处理读取请求时，**协调结点在每次请求的时候都会通过轮询所有的副本分片**来达到负载均衡

在文档被检索时，已经被索引的文档可能已经存在于主分片上但是还没有复制到副本分片。 在这种情况下，副本分片可能会报告文档不存在，但是主分片可能成功返回文档。 **一旦索引请求成功返回给用户，文档在主分片和副本分片都是可用的**（创建时，只有返回成功，证明主、副分片上面都有了）





#### 分布式存储-更新一个文档

1. 请求发送到一个Coordination node节点
   - （所有节点默认都是coordination node,即都可以扮演协调者角色)
2. hash算法算出这个请求需要路由到哪个==主分片==
3. 请求发送到节点分片上
4. ES对文档的更新是，先删除
5. 在重新创建出这个数据
6. 返回给Coordination node
7. 返回给用户

![image-20250107204258555](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107204300024.png)



#### 分布式存储-删除一个文档

与更新类似

![image-20250107205116212](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107205117521.png)







#### 多文档模式

`mget` 和 `bulk` API 的模式类似于单文档模式。区别在于协调节点知道每个文档存在于哪个分片中。 它将整个多文档请求分解成 *每个分片* 的多文档请求，并且将这些请求并行转发到每个参与节点。

协调节点一旦收到来自每个节点的应答，就将每个节点的响应收集整理成单个响应，返回给客户端

![image-20220622172209455](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221722764.png)



使用单个 `mget` 请求**取回多个文档**所需的步骤顺序：

1. 客户端向 `Node 1` 发送 `mget` 请求。
2. `Node 1` 为每个分片构建多文档获取请求，然后并行转发这些请求到托管在每个所需的主分片或者副本分片的节点上。一旦收到所有答复， `Node 1` 构建响应并将其返回给客户端。

可以对 `docs` 数组中每个文档设置 `routing` 参数



![image-20220622172244340](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221722708.png)

`bulk` API 按如下步骤顺序执行：

1. 客户端向 `Node 1` 发送 `bulk` 请求。
2. `Node 1` 为每个节点创建一个批量请求，并将这些请求并行转发到每个包含主分片的节点主机。
3. 主分片一个接一个按顺序执行每个操作。当每个操作成功时，主分片并行转发新文档（或删除）到副本分片，然后执行下一个操作。 一旦所有的副本分片报告所有操作成功，该节点将向协调节点报告成功，协调节点将这些响应收集整理并返回给客户端。

`bulk` API 还可以在整个批量请求的最顶层使用 `consistency` 参数，以及在每个请求中的元数据中使用 `routing` 参数





### 索引

> 索引是文档的容器，是一类文档的集合

- Index是逻辑空间的概念
  - 每个索引都有自己Mapping定义，用于定义包含的文档的字段名和字段类型
- Shard是物理空间的概念
  - 索引中的数据，分散存储在不同的Shard分片上



索引的Mapping与Settings

- Mapping定义文档字段的类型
- Settings定义不同的数据分布



索引的不同语义

- 名词
  - 一个ES集群中，可以创建多个不同的索引
- 动词
  - 将一个文档保存到ES的过程，叫做索引
  - ES中，创建一个倒排索引的过程
- 名词（抛开ES说）
  - 一个B-Tree索引、倒排索引等



以下语句会动态创建一个customer的索引index

~~~sh
PUT /customer/_doc/1
{
  "name": "John Doe"
}
~~~

而这个索引实际上已经自动创建了它里面的字段(name)的类型，以下为它自动创建的映射mapping

~~~json
{
  "mappings": {
    "_doc": {
      "properties": {
        "name": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        }
      }
    }
  }
}
~~~

若需要对这个索引的建立过程进行更多的操作，则需要关闭自动创建索引，并且手动创建索引

~~~sh
#禁止自动创建索引
#config/elasticsearch.yml的每个节点下添加配置
action.auto_create_index: false
~~~



#### 索引库配置管理：settings

1. 通过settings可以修改索引的分片数和副本数
2. 零停机重新索引数据
   - es在字段mapping建立后，就不能再次修改mapping的值了
   - 利用拷贝，将一个索引拷贝成另一个索引来实现



#### 索引的格式

在请求体里面传入设置或类型映射：settings

~~~sh
PUT /my_index
{
    "settings": { ... any settings ... },
    "mappings": {
        "properties": { ... any properties ... }，
       
    }
}
~~~

1. settings：设置分片、副本等等信息
2. mappings：字段映射、类型等等，可以看作是数据表列的约束
   - properties：由于type在后续版本会被去除掉，所以无需对type嵌套



#### 文档CRUD

- 创建索引
  - POST、PUT
    - Index方式：先删除后写入，原有文档先删除，后写入的version会+1
    - Create方式
- 删除索引
- 更新索引
  - Post
    - update api:version+1
- 查询文档
- Bulk Api
  - 一次api调用，对索引进行不同的操作
  - 并且可以多不同索引进行操作
  - 操作中，单条失败，不会影响其他操作
- 批量读取：mget Api
- 批量查询







### 倒排索引

倒排索引又叫反向索引，==是搜索引擎中，非常重要的数据结构==



#### 正向索引

用户发起查询时(假设查询为一个关键词)，搜索引擎会扫描索引库中的所有文档，找出所有包含关键词的文档，这样依次**从文档中去查找是否含有关键词的方法叫做正向索引**

互联网上存在的网页(或文档)不计其数，这样遍历的索引结构效率低下，无法满足用户需求

正向索引结构：

文档1的id——>单词1的信息；单词2的信息；单词n的信息

文档n的id——>单词1的信息；单词2的信息；单词n的信息

![image-20220621214736369](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212147599.png)



#### 反向索引

为了提升效率，搜索引擎会把正向索引变为反向索引(倒排索引)，即把文档——>单词的形式变为单词——>文档 的形式

倒排索引结构：

单词1→文档1的ID；文档2的ID；文档3的ID…
单词2→文档1的ID；文档4的ID；文档7的ID…

![image-20220621214940479](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212149605.png)



#### 单词-文档矩阵

单词-文档矩阵是表达两者之间所具有的一种包含关系的概念模型

现有以下几个文档：

- D1：乔布斯去了中国。
- D2：苹果今年仍能占据大多数触摸屏产能。
- D3：苹果公司首席执行官史蒂夫·乔布斯宣布，iPad2将于3月11日在美国上市。
- D4：乔布斯推动了世界，iPhone、iPad、iPad2，一款一款接连不断。
- D5：乔布斯吃了一个苹果

此时用户查询"苹果 and (乔布斯 or ipad2)"：表示包含苹果，同时包含乔布斯或ipad2

![image-20220621215355925](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212153021.png)

搜索引擎的索引其实就是实现 "单词-文档"矩阵的具体数据结构。可以有不同的方式来实现上述模型，比如：倒排索引、签名文件、后缀树等等方式，但是倒排索引是实现单词到文档映射关系的最佳实现方式



#### 倒排索引

> 核心分成：单词词典、倒排列表

倒排索引是实现单词-文档矩阵的一种具体存储形式，通过倒排索引可以根据单词快速获取包含这个单词的文档列表

倒排索引主要由两部分组成：单词词典、倒排文档

1. 单词词典
   - 会记录所有文档的单词，记录单词->倒排列表的关联关系
   - 单词词典是由文档集合中出现过的所有单词构成的字符串集合，单词词典内每条索引项记载单词本身的一些信息以及指向倒排列表的指针
   - 单词词典一般比较大，可以通过B+Tree或哈希拉链法实现，以满足高性能的插入和查询
2. 倒排列表
   - posting List，记录了单词对应的文档集合，由倒排索引项组成
   - 倒排索引项
     - 文档ID
     - 词频TF term frequency：该单词在文档中出现的次数，用于相关性评分
     - 位置：单词在文档中分词的文章，用于语句搜索
     - 偏移：记录单词的开始结束位置，用于高亮显示
3. 倒排文件
   - Inverted file，所有单词的倒排列表往往是顺序的存储在磁盘的某个文件里，这个文件称为倒排文件，倒排文件是存储倒排索引的物理文件

![image-20220621221334545](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212213673.png)

![image-20250104231359229](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250104231400618.png)





==ES的倒排索引==

- ES的Json文档中的每个字段，都有自己的倒排索引
- 可以指定对某些字段不做索引
  - 优点：节省存储空间
  - 缺点：字符无法被搜索



#### 案例

文档中的数字代表文档的ID，例如Doc1中的1

- Doc1：乔布斯去了中国。
- Doc2：苹果今年仍能占据大多数触摸屏产能。
- Doc3：苹果公司首席执行官史蒂夫·乔布斯宣布，iPad2将于3月11日在美国上市。
- Doc4：乔布斯推动了世界，iPhone、iPad、iPad2，一款一款接连不断。
- Doc5：乔布斯吃了一个苹果。

通过以上5个文件，建立简单的倒排索引

| 单词ID(WordID) | 单词(Word) | 倒排列表(DocID) |
| -------------- | ---------- | --------------- |
| 1              | 乔布斯     | 1，3，4，5      |
| 2              | 苹果       | 2，3，5         |
| 3              | iPad2      | 3，4            |
| 4              | 宣布       | 3               |
| 5              | 了         | 1，4，5         |
| …              | …          | …               |

首先分词系统会把文档自动切分成单词序列，这样就由文档转换为单词序列构成的数据流，并对每个不同的单词赋予唯一的单词编号(wordId)，并且每个单词都有对应的含有该单词的文档列表即倒排列表

例如：单词Id 1对应的单词为乔布斯，单词乔布斯的倒排列表为{1，3，4，5}即文档1、3、4、5都包含单词乔布斯

下面是更复杂的包含更多信息的倒排索引

TF(term frequency)：单词在文档中出现的次数

pos：单词在文档中出现的位置

| 单词ID(WordID) | 单词(Word) | 倒排列表(DocID;TF;<Pos>)                |
| -------------- | ---------- | --------------------------------------- |
| 1              | 乔布斯     | (1;1;<1>),(3;1;<6>),(4;1;<1>),(5;1;<1>) |
| 2              | 苹果       | (2;1;<1>),(3;1;<1>),(5;1;<5>)           |
| 3              | iPad2      | (3;1;<8>),(4;1;<7>)                     |
| 4              | 宣布       | (3;1;<7>)                               |
| 5              | 了         | (1;1;<3>),(4;1;<3>)(5;1;<3>)            |
| …              | …          | …                                       |

比如单词“乔布斯”对应的倒排索引里的第一项(1;1;<1>)意思是，文档1包含了“乔布斯”，并且在这个文档中只出现了1次，位置在第一个



### 分词

Analysis、Analyzer

- Analysis:文本分析，是把全文转换为一系列单词(term/token)的过程，也叫分词
  - Analysis是通过Analyzer实现的
  - 可以使用ES内置的分析器或自定义分析器
- 除了在数据写入时转换词条，匹配Query语句时，也需要用相同的分词器对使用同样的分词器对查询语句进行分析



Analyzer

分词器由专门处理分词的组件构成

- Character Filters：字符过滤器
  - 针对原始文本处理
  - **字符过滤器的任务**是在分词前整理字符串，例如：去掉html、将&转化为and等等
- Tokenizer：分词器
  - 按照规则切分为单词
- Token Filters：Token(分词、词条)过滤器
  - 将切分后的单词进行二次加工
  - 这个过程可能会改变词条(例如：小写化)、删除词条(例如：a、and、the这些无用词)、增加词条



ES内置分词器

==_analyze API可以帮助我们分析如何分词的==

- Standard Analyzer：默认分词器
  - 按词分词，小写处理
- Simple Analyzer
  - 按照非字母切分，非字母的都去除
  - 小写处理
- Whitespace Analyzer
  - 按照空格进行切分分词
- Stop  Analyzer
  - 相比Simple Analyzer多了Stop filter
  - 会把the、a、is等这些修饰词去除
- Keyword  Analyzer
  - 不分词，将输入当作一个结果term输出
- Pattern Analyzer
  - 通过正则表达式进行分词
  - 默认是\W+,非字符的符号进行分隔
- Language Analyzer
  - 不同国家的语言提供了支持



中文分词的难点

- 中文句子，切分一个一个词（不是一个个字）
- 英文中，单词有自然的空格作为分隔
- 一句中文，在不同的上下文，有不同的理解，所以更难





什么时候会使用到分析器

1. 当我们索引一个文档，它的全文域被分析成词条，以用来创建倒排索引
2. 全文查询中，需要将查询字符串通过相同的分析过程，以保证我们搜索的词条格式与索引中的词条格式一致





#### ES安装中文分词器

ICU Analyzer、ik、Thulac、HanLP等等中文分词器

> ES自带的英文分词器太简单，对于中文的分词，需要安装一个叫做**iK分词器**来解决对中文的分词问题



ik分词器带有两个分词器

1. ik_max_word
   - 会将文本做**最细粒度的拆分**，尽可能的拆分初词语句子
   - 例如：我爱我的都祖国
   - 结果：我、爱、我的、祖、国、祖国
2. ik_smart
   - 会**做最粗粒度的拆分**，已被分出的词语不会再次被其他词语占有
   - 例如：中华人民共和国国歌
   - 结果：中华人民共和国、国歌





### Search API

- URL Search:在Url中使用查询参数
- Request Body Search
  - 使用ES提供的基于Json格式的更完备的查询语句：Query Domain Specific Language，DSL



| 语法                   | 范围              |
| ---------------------- | ----------------- |
| /_search               | 集群中所以的索引  |
| /index1/_search        | index1            |
| /index1,index2/_search | index1、index2    |
| /index*/_search        | 以index开头的索引 |



~~~json
{
  "took": 0,  //花费的时间
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {  //符合条件的总文档数量
      "value": 1,
      "relation": "eq"
    },
    "max_score": 1,
    "hits": [  //返回结果集，默认前10个文档
      {
        "_index": "test-index-user",
        "_id": "xPZsMJQBcLXQ9h4VdKV3",
        "_score": 1, //文档相关性评分
        "_source": { //文档原始信息
          "name": "pdai test name",
          "age": 18,
          "remarks": "hello wo shi remarks"
        }
      }
    ]
  }
}
~~~



衡量相关性

- Information Retrieval
  - Precision 查准率: 尽可能返回较少的无关文档
  - Recall 查全率：尽量返回较多的相关文档
  - Ranking：是否能够按照相关度排序









### 查询

查询参数比较多，需要时在查文档即可



#### 查询数据

~~~sh
GET /bank/_search
{
  "query": { 
      "match_all": {},

      //match查询表达式,abc与def是oR的条件
      "match":{
        "address":"abc def"
      }
      "query_string":{
      	"fields":[a,b]
      	"query":xxx
      },
      "simple_query_string":{
      	"fields":[a,b]
      	"query":xxx
      }
  },
  //排序
  "sort": [
    { "account_number": "asc" }
  ],
  // 分页查询
  "from": 10,
  "size": 10
}
~~~





#### 脚本字段

> script_fields,可以利用它做一些计算后，返回新的字段，使用时详细看







#### 聚合查询

Aggregation，SQL中叫做group by，ES中叫Aggregation即聚合运算

例如：统计每个州的数量

~~~sh
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      }
    }
  }
}

~~~

#### 嵌套聚合

聚合条件的嵌套，例如计算每个州的平均结余，需要在state分组基础上，嵌套计算avg(balance)

~~~sh
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
~~~



#### 对聚合结果排序

 对嵌套计算出的avg(balance)，这里是average_balance，进行排序

~~~sh
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword",
        "order": {
          "average_balance": "desc"
        }
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
~~~















#### ES查询-DSL

> 在查询中，会有多种条件组合的查询，在ES中称为复合查询



ES的查询基于JSON风格的DSL来实现的，DSL:Domain Specific Language，即
领域特定语言；使用Json构造一个请求体

常用的查询类型包括

1. 查询所有：查询出所有的数据，一般测试用。例如：match_all
2. 全文检索：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如： match_query ; multi_match_query
3. 精确查询：根据精确词条值查找数据，一般是查找keyword、数值、日前、boolean等类型字段。例如：ids ；range; term
4. 复合查询
5. 地理查询

在查询中会有多种条件组合的查询，在ES中叫做复合查询。它提供了5钟复合查询方式



#### bool query

布尔查询，通过布尔逻辑将较小的查询组合成较大的查询，只要一个子查询条件不匹配那么搜索的数据就不会出现

bool查询包含4种操作符，它们均是一种数组，数组里面是对应的判断条件

1. must：必须匹配，贡献算分
2. must_not：过滤子句，必须不能匹配，但不贡献算分
3. should：选择性匹配，至少满足一条，贡献算分
4. filter：过滤子句，必须匹配，不贡献算分



#### boosting query

提高查询：降低了显示的权重/优先级(即score)

比如搜索逻辑是 name = 'apple' and type ='fruit'，对于只满足部分条件的数据，不是不显示，而是降低显示的优先级（即score)

例如：

~~~sh
#创建数据
POST /test-dsl-boosting/_bulk
{ "index": { "_id": 1 }}
{ "content":"Apple Mac" }
{ "index": { "_id": 2 }}
{ "content":"Apple Fruit" }
{ "index": { "_id": 3 }}
{ "content":"Apple employee like Apple Pie and Apple Juice" }
~~~



~~~sh
#对pie进行降级
GET /test-dsl-boosting/_search
{
  "query": {
    "boosting": {
      "positive": {
        "term": {
          "content": "apple"
        }
      },
      "negative": {
        "term": {
          "content": "pie"
        }
      },
      "negative_boost": 0.5
    }
  }
}
~~~

![image-20220620115153981](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201152867.png)

#### constant_score

固定分数查询：查询某个条件时，固定的返回指定的score，显然当不需要计算score时，只需要filter条件即可，因为filter忽略分数

例如：score为1.2

~~~sh
GET /test-dsl-constant/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": { "content": "apple" }
      },
      "boost": 1.2
    }
  }
}
~~~



#### dis_max

disjunction max query：分离最大化查询，指的是，将任何与任意查询匹配的文档作为结果返回，但只将**最佳匹配的评分作为查询的评分结果返回** 。

最佳匹配查询

分离的意思是 或 or

~~~sh
GET /test-dsl-dis-max/_search
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Brown fox" }},
                { "match": { "body":  "Brown fox" }}
            ],
            "tie_breaker": 0
        }
    }
}
~~~



#### function_score

函数查询：自定义函数的方式计算score

ES的自定义函数：

1. script_score：使用自定义的脚本来完全控制分值计算逻辑。如果需要预定义函数之外的功能，可以根据需要通过脚本进行实现
2. weight：对每份文档使用一个简单的提升，且该提升不会被归约。当weight为2时，结果为2 * _score
3. random_score：使用一致性随机分值计算来对每个用户采用不同的结果排序方式，对相同用户仍然使用相同的排序方式。
4. field_value_factor：使用文档中某个字段的值来改变_score，比如将受欢迎程度或者投票数量考虑在内
5. 衰减函数(decay function)：linear，exp，gauss





---

#### ES查询-全文搜索

DSL查询极为常用的是对文本进行搜索，即全文搜索

![image-20230408204816731](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304082048447.png)





#### Match类型

数据如下：

~~~sh
PUT /test-dsl-match
{ "settings": { "number_of_shards": 1 }} 

POST /test-dsl-match/_bulk
{ "index": { "_id": 1 }}
{ "title": "The quick brown fox" }
{ "index": { "_id": 2 }}
{ "title": "The quick brown fox jumps over the lazy dog" }
{ "index": { "_id": 3 }}
{ "title": "The quick brown fox jumps over the quick dog" }
{ "index": { "_id": 4 }}
{ "title": "Brown fox brown dog" }
~~~

##### Match单个词

查询数据：

~~~sh
GET /test-dsl-match/_search
{
    "query": {
        "match": {
            "title": "QUICK!"
        }
    }
}
~~~



match查询步骤：

1. 检查字段类型
2. 分析查询字符串
   - 将查询字符串quick传入标准分析器中，输出项的结果是单个项quick。因为只有一个单词项，所以match查询执行的是单个底层term查询
3. 查找匹配文档
   - 用 term 查询在倒排索引中查找 quick 然后获取一组包含该项的文档，本例的结果是文档：1、2 和 3 
4. 为每个文档评分
   - 用 term 查询计算每个文档相关度评分 _score ，这是种将词频（term frequency，即词 quick 在相关文档的 title 字段中出现的频率）和反向文档频率（inverse document frequency，即词 quick 在所有文档的 title 字段中出现的频率），以及字段的长度（即字段越短相关度越高）相结合的计算方式

##### Match多个词

~~~sh
GET /test-dsl-match/_search
{
    "query": {
        "match": {
            "title": "BROWN DOG"
        }
    }
}
~~~

match多个词的本质

它在内部实际上先执行两次term查询，然后**将两次查询的结果合并作为最终结果输出**。为了做到这点，它将两个term查询合并到了一个bool查询中

~~~sh
GET /test-dsl-match/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "term": {
            "title": "brown"
          }
        },
        {
          "term": {
            "title": "dog"
          }
        }
      ]
    }
  }
}
~~~

should：任意一个满足，是因为match有一个operator参数，默认是or，所以对应的是should

等同于：

~~~sh
GET /test-dsl-match/_search
{
  "query": {
    "match": {
      "title": {
        "query": "BROWN DOG",
        "operator": "or"
      }
    }
  }
}
~~~



#### query_string类型

> 这一类查询具有严格语法的解析器提供的查找字符串返回文档

此查询使用一种语法根据运算符（and or）来解析和拆分提供的查询字符串，然后，查询在返回匹配文档之前，独立分析每个拆分文本

可以使用`query_string`查询来创建包含通配符、跨多个字段的搜索等复杂搜索。 虽然用途广泛，但查询很严格，如果查询字符串包含任何无效语法，则会返回错误



#### Intervals类型

> 这一类是  Intervals：时间间隔，本质上是将多个规则按照顺序匹配







---

#### DSL查询-Term

> DSL查询另一种极为常用的是**对词项进行搜索**，官方称为term level查询
>
> (基于单词查询)

![image-20220620155926112](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201559600.png)

单个分词匹配term

~~~sh
GET /test-dsl-term-level/_search
{
  "query": {
    "term": {
      "programming_languages": "php"
    }
  }
}
~~~



多个分词匹配terms

按照单个分词匹配，它们之间是or关系

~~~sh
GET /test-dsl-term-level/_search
{
  "query": {
    "terms": {
      "programming_languages": ["php","c++"]
    }
  }
}
~~~













### Mapping

> GET /index/_mapping查看Mapping关系

Mapping

- Mapping类似数据库中的schema字段定义
- 定义索引中的字段的名称、数据类型
- 字段，倒排索引的相关配置（Analyzer等）



Mapping会把Json文档映射成Luncene所需的扁平格式

一个Mapping属于一个索引的Type

- 每个文档属于一个Type
- 一个Type有一个Mapping定义
- ES7.0开始，不需要再Maping中指定type信息了，只有一个`_doc类型`



字段的数据类型

- 简单类型
  - 字符串: `string`
  - 整数 : `byte`, `short`, `integer`, `long`
  - 浮点数: `float`, `double`
  - 布尔型: `boolean`
  - 日期: `date`
- 复杂类型
  - 对象类型、嵌套类型
  - 多值域（类似数组）
- 特殊类型
  - geo_point、geo_shape、percolator



Dynamic Mapping

- 在写入文档的时候，如果这个文档不存在，会自动创建索引
- Dynamic mapping机制，使得我们无需手动定义Mapping
- ES会自动根据文档信息，推算出字段的类型
- 但是，有时候会推算不对
  - 当类型设置不对时，会导致一些功能无法正常运行





更改Mapping的字段类型

- 新增字段

  - Dynamic为Ture时，一旦有新字段的文档写入，mapping也会同时更新
  - Dynamic为False时，Mapping不会被更新，新增的字段也无法被索引，但是信息会出现在`_source`中
  - Dynamic为Strict时，文档写入失败

- 已存在的字段

  - 一旦已经有数据写入，就不支持修改字段定义
  - Lucene（英：鲁森~）实现的倒排索引，一旦生成后，就不允许修改
  - 如果想要改变字段类型，必须使用ReIndex Api,重建索引

  

![image-20250105231108572](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250105231111069.png)



#### 自定义mapping

~~~json
put index_name
{
    "mappings":{
        "properties":{
            ........
        }
    }
}
~~~



如何写？

- 参考API文档，纯手写
- 为了减少工作量、减少出错概率，可以参考以下步骤
  - 创建一个临时的Index，写入一些样本数据
  - 通过访问Mapping Api获得临时文档的动态Mapping的定义
  - 修改这个定义，配置我们自己需要的索引定义
  - 删除临时索引



控制当前字段是否被索引

> index属性，控制当前字段是否能被索引，默认True
>
> 当不需要索引某些字段时，这个字段就不会被搜索到了，同时也会节省很大磁盘开销

Index Options:索引配置级别

- docs:记录doc id
- freqs:记录doc id、term frequnencies
- positions:记录doc id、term frequnencies、term position
- offsets:记录doc id、term frequnencies、term position、charachter offsets

Text类型默认记录positions，其他类型默认docs

记录内容越多，占用存储空间越大



Null_Value

> Lucene是不能存储null值的，所以认为存在null值的域为空域

- 需要对Null值实现搜索
- 只有keyword类型支持设定Null_value



Mapping 多字段属性及配置自定义Analyzer

- 默认text字属性，会加上一个keyword类型的子字段，用于精确值搜索

- 使用不同的analyzer

  - 不同语言

  - 还支持为搜索和索引指定不同的analyzer

  - 自定义analyzer,es提供的都不能满足我们需求时，我们自己写一个

    - 主要就是定义这3个组件

    - Character Filters：字符过滤器

      - 针对原始文本处理

    - Tokenizer：分词器

      - 按照规则切分为单词

    - Token Filters：Token(分词、词条)过滤器

      

    



精确值(Exact values) vs 全文本（Full Text)

- 精确值包含数字/日期/具体的字符串
  - ES中的keyword
- 全文本：非结构化的文本数据
  - ES中的text
- 区别
  - 精确值不需要做分词的处理









#### 内部对象的映射

> ES会动态监测新的对象域，并映射它们为对象，在properties属性下列出内部域

~~~json
{
  "gb": {
    "tweet": { 		//根对象
      "properties": {
        "tweet":            { "type": "string" },
        "user": {  //内部对象
          "type":             "object",
          "properties": {
            "id":           { "type": "string" },
            "gender":       { "type": "string" },
            "age":          { "type": "long"   },
            "name":   { //内部对象
              "type":         "object",
              "properties": {
                "full":     { "type": "string" },
                "first":    { "type": "string" },
                "last":     { "type": "string" }
              }
            }
          }
        }
      }
    }
  }
}
~~~



#### 内部对象是如何索引的

> Lucene是不理解内部对象的，Lucene文档是由一组k-v对列表组成的
>
> 为了能让ES有效的索引内部对象，它会把文档做转换

即：内部域通过名称引用，出现多级时，跟着对象的引用一直引用下去就行了。例如：user.name.full

~~~json
{
    "tweet":            [elasticsearch, flexible, very],
    "user.id":          [@johnsmith],
    "user.gender":      [male],
    "user.age":         [26],
    "user.name.full":   [john, smith],
    "user.name.first":  [john],
    "user.name.last":   [smith]
}
~~~



#### 内部对象数组

~~~json
{
    "followers": [
        { "age": 35, "name": "Mary White"},
        { "age": 26, "name": "Alex Jones"},
        { "age": 19, "name": "Lisa Smith"}
    ]
}
~~~

这个域，文档在处理的时候，会向上面一样，进行扁平化处理

~~~json
{
    "followers.age":    [19, 26, 35],
    "followers.name":   [alex, jones, lisa, smith, mary, white]
}
~~~





### 索引模板

> Index Template

- 帮助我们设定Mappings和Settings，并按照一定的规则，自动匹配到新创建的索引上
  - 模板仅在一个索引被新创建时，才会产生作用，修改模板不会影响已经创建的索引
  - 可以设定多个索引模板，这些设置会被"merge"在一起
  - 可以指定"order"的数值，控制"merging"的过程



Dynamic Template

- 根据ES识别的数据类型，结合字段名称，来动态设定字段类型
  - 所有的字符串类型都设定成keyword，或者关闭keyword字段
  - is开头的字段都设置为boolean
  - long_开头的都设置成long类型



批量和脚本化是提供一种模板方式快速构建和管理索引的方式，索引模板，它是一种告诉ES在创建索引时如何配置索引的方法，为了更好的复用性，7.8版本开始引入了组件模板

索引模板是告诉ES在创建索引时，如何配置索引的方法。在创建索引之前可以先配置模板，这样在创建索引(手动或文档建立索引)时，模板设置将用作创建索引的基础

#### 模板类型

##### 组件模板

是可重用的构建块，用于配置映射，设置和别名；它们不会直接应用于一组索引

##### 索引模板

可以包含组件模板的集合，也可以直接指定设置，映射和别名

#### 索引模板中的优先级

1. 可组合模板优先于旧模板。如果没有可组合模板匹配给定索引，则旧版模板可能仍匹配并被应用
2. 如果使用显式设置创建索引并且该索引也与索引模板匹配，则创建索引请求中的设置将优先于索引模板及其组件模板中指定的设置
3. 如果新数据流或索引与多个索引模板匹配，则使用优先级最高的索引模板



#### 内置索引模板

ES具有内置索引模板，每个索引模板的优先级为100，适用于以下索引模式

1. logs-* -*
2. metrics-* -*
3. synthetics-* -*

在涉及内建索引模板时，要避免索引模式冲突





### 聚合查询Aggregation

- ES除了提供搜索功能外，提供的针对ES数据统计分析功能，就是Aggregation
  - 实时性高
  - Hadoop(T+1)
- 通过聚合，可以得到一个数据的概念，聚合是分析和总结全套的数据，而不是寻找单个文档
- 高性能，只需要一条语句，就可以从ES得到分析结果

Kibana的可视化报表中，很多功能都是ES的聚合功能实现的





ES提供了几大类聚合方式

- Bucket Aggretation:桶聚合
  - ==一些列==满足特定条件的文档的集合

- Metric Aggretation：指标聚合
  - 一些数学运算，可以对文档字段进行统计分析

- Pipeline Aggretation：管道聚合
  - 对其他的聚合结果进行二次聚合

- Matrix Aggretation：矩阵聚合
  - 支持对多个字段的操作，并提供一个结果矩阵



#### Bucket & Metric

- Bucket桶的概念类似于SQL中的分组，

- Metric指标类似于SQL中的统计函数count、sum、max等



1. 桶Buckets：满足特定条件的文档的集合
2. 指标Metrics：对桶内的文档进行统计计算

指标聚合与桶聚合大多数情况组合在一起使用，桶聚合本质上是一种特殊的指标聚合，它的聚合指标就是数据的条数(count)



官网给出了好几十种桶聚合，但是肯定是不能一个一个去看的，所以要站在设计的角度上来分类理解，主要有以下三类：

![image-20220620163557154](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201635557.png)



#### 多个聚合

~~~sh
GET /test-agg-cars/_search
{
    "size" : 0,
    "aggs" : { 
    // agg的名字
        "popular_colors" : { 
        //terms桶的类型
            "terms" : { 
              "field" : "color.keyword"
            }
        },
        //agg的名字
        "make_by" : { 
            "terms" : { 
              "field" : "make.keyword"
            }
        }
    }
}
~~~

![image-20220620165554791](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201655354.png)



##### 聚合的嵌套

这个新的聚合层让我们可以将 avg 度量嵌套置于 terms 桶内。实际上，这就为每个颜色生成了平均价格

~~~sh
GET /test-agg-cars/_search
{
   "size" : 0,
   "aggs": {
      "colors": {
         "terms": {
            "field": "color.keyword"
         },
         "aggs": { 
            "avg_price": { 
               "avg": {
                  "field": "price" 
               }
            }
         }
      }
   }
}
~~~

![image-20220620165933604](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201659248.png)



##### 动态脚本的聚合

ES支持一些基于脚本(生成运行时的字段)的复杂的动态聚合



---

#### ES聚合-Metrics聚合

> 指标聚合Metrics Aggregation，类似于sql中的avg()、count()、min()、max()

适用时候，查看文档即可





#### ES聚合-Pipline

> 管道聚合：让上一步的聚合结果成为下一个聚合的输入，这就是管道集合

管道机制的场景场景

1. Tomcat管道机制
2. 责任链模式
   - 管道机制在设计模式上属于责任链模式
3. ElasticSearch的管道机制
   - 简单而言：让上一步的聚合结果，成为下一个聚合的输入





1. 第一个维度：管道聚合有很多不同**类型**，每种类型都与其他聚合计算不同的信息，可以将这些类型分为两类

   - 父级：父级聚合的输出提供了一组管道聚合，它可以计算新的存储桶或新的聚合以添加到现有存储桶中
   - 兄弟：同级聚合的输出提供的管道聚合，并且能够计算与该同级聚合处于同一级别的新聚合

2. 第二个维度：根据功能设计的意图

   - 前置聚合可能是Bucket聚合，后置的可能是基于Metric聚合，那么它就可以成为一类管道

   - 进而引出了：`xxx bucket`

   - Bucket聚合->metric聚合：bucket聚合的结果，成为下一步metric聚合的输入

     - Average bucket

     - Min/Max/Sum bucket

     - Stats bucket

     - Extended stats bucket





### 基于词项和全文的搜索

#### 基于Term词项的查询

- Term
  - 是表达语意的最小单位，搜索和利用统计语言模型进行自然语言处理都需要处理Term
- 特点
  - Term Level Query
    - Term Query/Range查询/Exists查询/Prefix查询/Wildcard通配符查询
  - ES中，Term查询，对输入的搜索词==不做分词==。
    - 会将输入作为一个整体作为搜索条件，在倒排索引中查找精确的词项，并且使用相关度算分公式，给每个包含该词项的文档进行相关性算分后返回。例如："Apple Store"的匹配
  - 可以通过`Constanc Score`将一个查询转换为：==Filter,避免算分==，并利用缓存，提供性能
  - 想要精确查询：增加子字段keyword，进行严格匹配



#### 基于全文本的查询

- Match Query/Match Phrase Query/Query String Query
- 特点
  - 索引和搜索时，都会进行分词，查询字符串先传递到一个合适的分词器，然后生成一个供查询的词项列表
  - 查询时
    - 先对输入的查询进行分词
    - 然后每个词逐项进行底层查询，最终将结果进行合并
    - 为每个文档生成一个算分，分数高的排在前面









### Query&Filter与多字符串多字段查询

ES中，有Query和Filter两种不同的Context

- Query Context：会进行相关性算分
- Filter Context：不需要算分，可以利用Cache，获得更好的性能



符合查询bool查询

- 一个bool查询，是一个或多个查询子句的组合
  - 总共包括4种类型子句，2种会影响算分，2种不会
- 相关性并不只是全文检索的专利，也适用于bool查询
  - 匹配的子句越多，相关性评分越高
  - 若多条查询子句被合并为一条复合查询语句，则每个查询子句计算出的评分会被合并到总的相关性评分中

| must     | 必须匹配，贡献算分                    |
| -------- | ------------------------------------- |
| should   | 选择性匹配，贡献算分                  |
| must_not | Filter Context 查询子句，必须不能匹配 |
| filter   | Filter context 必须匹配，不贡献算分   |

每个字段结合boost,可以影响算分的权重



bool算分过程

- 查询should语句的中的2个查询
- 加和2个查询的评分
- 乘以匹配语句的总数
- 除以所有语句的总数



### 单字符串多字段查询



Disjunction Max Query 复合查询

- 将任何与任一查询匹配的文档作为结果返回
- 采用字段上最匹配的评分，作为最终评分返回（最高那个）
- `dis_max`属性
  - 结合`tie_breaker`（0-1的浮点数）参数调整评分的规则



使用场景

- 最佳字段：best fields
  - 当字段之间相互竞争，又相互关联。评分来自最匹配字段
- 多数字段:most fields
  - 主字段抽取词干，加入同义词，已匹配更多的文档
  - 相同的文本，加入子字段，以提供更加精确的匹配
  - 其他字段作为匹配文档提高相关度的信息，匹配字段越多则越好
- 混合字段:Cross Fields
  - 需要在多个字段中确定信息，单个字段只能作为整体的一部分
  - 希望在任何这些列出的字段中找到尽可能多的词



Multi Match

~~~json
post blogs/_search
{
	"query":{
        "multi_match":{
            "type":"best_fields",
            "query":"pets",
            //在哪些字段查询
            "fields":["title","body"]，
            "tie_breaker":0.2,
            "minimum_should_match":"20%"
        }
    }
}
~~~



跨字段搜索

- 无法使用Operator
- 可以使用copy_to解决，但是需要额外的存储空间
- type为：cross_fields跨字段搜索解决
  - 可以使用operator操作符



~~~json
post blogs/_search
{
	"query":{
        "multi_match":{
            "type":"cross_fields",
            "query":"pets",
            //在哪些字段查询
            "fields":["title","body"]，
            "operator":"and"
        }
    }
}
~~~



### 结构化搜索

#### 结构化数据

- 日期、时间、数字、布尔这类的数据结构化
  - 有精确的格式，我们可对这些格式进行逻辑操作
  - 包括比较数字或事件范围，或判断2个值的大小
- 结构化的文本可以做精确匹配，或者部分匹配
  - ==Term查询==、Prefix前缀查询
  - Term查询针对多值（例如 数组)是采用包含而不是相等
  - 解决精确查询：增加一个`gener_count`字段进行计数，结合bool查询完成
- 结构化结果只有：是、否两个值
  - 根据场景需要，可以决定结构化搜索是否需要打分
  - `constant_score`属性，跳过算分



#### 结构化搜索

> 结构化查询并不关心文件的相关度或评分，我们得到的结果总是：要么存在于集合中，要么不存在

1. 精确值查找

   - 使用精确值查找时，会使用过滤器，过滤器非常重要，它的执行速度很快，因为不会去计算相关度
   - 而且过滤器很容易被缓冲
   - **constant_score**  + filter +term

2. 组合过滤器

   - 可能存在过滤多个值或字段的情况
   - 需要采用bool过滤器，它可以接受多个其他过滤器作为参数，并将这些过滤器结合成各式各样的布尔逻辑组合
   - bool+must、must_not、should 等

3. 查找多个精确值

   - constant_score + terms + 值数组

4. 范围

5. null

   







### 相关性和相关性算分

> 默认情况下，返回的结果是按照**相关性**进行排序的，即最相关的文档排在最前



> [!WARNING]
>
> 相关性测试（调试）总是放在最后（相关性测试很消耗资源，生产环境不建议开启的）



#### 相关性 Relevance

- 搜索的相关性算分，==描述了一个文档和查询语句匹配的程度==
- ES会对每个匹配查询条件的结果进行算分：`_score`表示
- 每个文档都有相关性评分，用一个正浮点数字段 `_score` 来表示 。 评分越高，相关性越高
- ==打分的本质是：排序==，需要把最符合用户需求的文档排在前面
  - 使用filter来过滤时，不存在评分，只会筛选出符合条件的文档
  - ES5以前采用TF/IDF算法、ES5.0之后，采用BM25



**评分的计算方式取决于查询类型**，不同的查询语句用于不同的目的



词频TF

- Term Frequency：检索词在一篇文档中出现的频率
  - 检索词出现的次数/文档的总字数
- 度量一条查询和结果文档相关性的简单算法
  - 简单的将搜索中的每一个词的TF进行相加
- Stop word（停用词）
  - 某些词(例如：“的”）在文档中出现了很多次，但是，对贡献相关度几乎没有用处，不应该考虑它们的TF



逆文档频率IDF

- DF：检索词在文档中出现的频率
- IDF：Inverse Document Frequency
  - 简单说=log(全部文档数/检索词出现过的文档总数)
- TF/IDF算法：本质上就是将TF求和，变成了加权求和
  - TF(搜索词1)*IDF(搜索词1)+2+3..
  - TF-IDF算法被公认为是==信息检索领域==最重要的发明
  - 现代的搜索引擎大多都基于TF-IDF基础的

![image-20250106161619647](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250106161621048.png)



BM25

和TF-IDF相比，当TF无限增加时，BM25算分会趋于一个数值



Boosting Relevance

- Boosting是控制相关度的一种手段
  - 索引、字段、查询子条件，上面都可以设置boost值
- 参数boost的含义
  - boost>1，打分的相关度相对性提升
  - 0<boost<1,打分的权重相对性降低
  - boost<0,贡献负分



#### 评分标准

> 调试一条复杂的查询语句时，想要理解_score如何计算的是非常困难的

Explain API 查看TF-IDF

- ES在每个查询语句中都有一个 explain 参数，将 `explain` 设为 `true` 就可以得到更详细的信息
- explain可以让返回结果添加一个_score评分的得来依据
- explain的输出结果代价是相当昂贵的，只能用作调试工具，不能用于生产环境

~~~json
GET /_search?explain 
{
   "query"   : { 
       "match" : {
           "tweet" : "honeymoon" 
       }
   }
}
~~~

每个入口都包含一个 `description` 、 `value` 、 `details` 字段，它分别告诉你计算的类型、计算结果和任何我们需要的计算细节

~~~json
"_explanation": { 
   "description": "weight(tweet:honeymoon in 0)
                  [PerFieldSimilarity], result of:",
   "value":       0.076713204,
   "details": [
      {
         "description": "fieldWeight in 0, product of:",
         "value":       0.076713204,
         "details": [
            {  
               "description": "tf(freq=1.0), with freq of:",
               "value":       1,
               "details": [
                  {
                     "description": "termFreq=1.0",
                     "value":       1
                  }
               ]
            },
            { 
               "description": "idf(docFreq=1, maxDocs=1)",
               "value":       0.30685282
            },
            { 
               "description": "fieldNorm(doc=0)",
               "value":        0.25,
            }
         ]
      }
   ]
}
~~~



#### Function Score Query

`function_score`属性查询，可以优化算分



可以在查询结束后，对每一个匹配的文档进行一系列的重新算分，根据新生成的分数进行排序



提供了几种默认的计算分值的函数

- Weight
  - 为每一个文档设置一个简单而不被规范化的权重
- Field Value Factor
  - 使用该数值来修改`_score`，例如将"热度"、"点赞数"考虑进来
  - 可以引入：modifier、factor来进一步平滑分数
- Random Score
  - 为每一个用户使用一个不同的，随机计算分结果
- 衰减函数
  - 以某个字段的值为标准，距离某个值越近，得分越高
- Script Scort
  - 自定义脚本完全控制所需逻辑







### Search Template

> 主要是解耦程序、解耦搜索DSL

大的团队合作中，开发工程师只负责程序逻辑部分，还有一部分负责ES的性能及效果

- 开发人员/搜索工程师/性能工程师



开发初期，虽然可以明确查询参数，但是往往不能最终定义查询的DSL的具体结构

- 通过Search Template定义一个Contract





### Index Alias

> 索引别名，可以实现零停机运维



### Suggester 搜索建议

现代的搜索引擎，一般会提供Suggest as you type的功能

帮助用户在搜索的过程中，进行==自动补全或者纠错==。通过协助用户输入更精准的关键词，提高后续搜索阶段文档匹配的程度



ES使用Suggester API实现

原理

- 将输入的文本分解为Token，然后在索引的字典里查找相似的Term并返回给用户
- 根据不同的使用场景，ES设计了4种类别的Suggesters
  - Term & Phrase Suggester
  - Complete & Context Suggester



#### Term Suggester

- Suggester是一种特殊类型的搜索，text里是调用时候提供的文本，通常来自于用户界面上用户输入的内容
- 用户输入的"lucen"是一个错误的拼写
- 会到指定的字段“body"上搜索，当无法搜索到结果时，返回建议的词



> 每个建议都包含了一个算分，相似性是通过Levenshtein Edit Distance算法实现的

核心思想：一个词改动多少字符就可以和另一个词一致。提供了许多可选参数来控制相似性的模糊程度





Suggestion的几种Mode

- Missing:如果索引中已经存在，就不提供建议
- Popular：推荐出现频率更加高的词
- Always：无论是否存在，都提供建议



#### Phrase Suggester

- 在Term Suggester上增加了一些额外的逻辑
- 支持更多的一些参数
  - Suggest mode:missing、popular、always
  - Max errors:最多可以拼错的terms数
  - Confidence：限制返回结果数，默认1



#### Completion Suggester

> 提供了自动完成（Auto Complete)的功能，用户每输入一个字符，就需要即时发送一个查询请求到后端查找匹配项

- 对性能比较苛刻
  - ES采用了不同的数据结构，并发采用倒排索引完成
  - 采用了将Analyze的数据编码成FST和索引一起存放。FST会被ES整个加载进内存，速度很快
- FST只能用于前缀查找



使用Completion suggester的步骤

1. 定义Mapping，使用completion type
2. 索引数据
3. 运行“suggest”查询，得到搜索建议





#### Context Suggester

> 实现上下文感知的搜索

可以定义2种类型的Context上下文

- Categroy：任意的字符串
- Geo：地理位置信息



实现Context Suggester的具体步骤

- 定义Mapping
- 索引数据，并为每个文档加入Context信息
- 结合Context进行Suggestion查询



精准度和召回率

- 精准度：Completion>Phrase>Term
- 召回率：Term>Phrase>Completion
- 性能：Completion>Phrase>Term



### 配置跨集群搜索

1. 配置集群
2. 分别在不同机器上写入数据
3. 搜索数据
   - 指定集群名字.index来搜索





### 分布式查询及其相关性算分

#### 分布式搜索的运行机制

- ES的搜索分为2个阶段
  - 第一个阶段：Query
  - 第二个阶段：Fetch



#### Query阶段

![image-20250107213824815](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107213826803.png)

例子：3个主分片、3个副本分片

- 用户发出搜索请求到ES节点。节点收到请求后，会以Coordinating节点的身份角色，在主、副分片（例子是6个）中随机选择分片（3个），发送查询请求

- 被选中的分片执行查询，进行排序。

  - 然后每个分片会返回from+size个排序后的文档id和排序值值给Coordinating node

    

#### Fetch阶段

- Coordinating node将从Query阶段，每个分片获取的排序后的文档id列表重新排序，选取from+size个文档id
- 以`multi get`请求的方式，到相应的分片获取详细的文档信息





#### Query  Then Fetch潜在的问题

- 性能问题

  - 每个分片上需要查询的文档个数：X=from+size
  - 最终协调者节点需要处理：分片数*X
  - 深度分页问题

- 相关性算分

  - 每个分片都基于自己的分片上的数据进行相关性算分
  - 这会导致打分偏离的情况，特别是数据量少的时
  - 相关性算分在分片之间是相互独立的，当文档总数很少的情况下，如果主分片>1,主分片越多，相关性算分越不准

- 解决算分不准问题？

  1. 数据量不大时，可以将主分片数设置为1
  2. 使用DFS Query Then Fetch

  - 搜索的URL中，指定参数`dfs_query_then_fetch`
  - 到每个分片把每个分片的词频和文档频率进行搜集，然后完整的进行一次相关性算分
  - 但是，耗费更多的cpu和内存，性能低下，==一般不建议使用==



### 排序及Doc values & Fielddata

- ES默认采用相关性算分对结果进行降序排序
- 支持对一个、多个字段排序
- 可通过设定`sorting`参数，自定义排序
- 如果不指定`_score`,那么算分为Null





#### 排序的过程

- 排序是针对==字段原始内容==进行的，倒排索引无法发挥作用
- 需要用到正排索引，通过文档Id和字段,快速得到字段原始内容
- ES有2种实现排序的方式
  - Fielddata
  - Doc Values(列式存储，对Text类型无效)
    - 默认开启的
    - 对text字段排序，需要将fielddata设置为true
    - 不建议，同时对text排序也没有啥意义

|          | Doc Values                   | Field data                                   |
| -------- | ---------------------------- | -------------------------------------------- |
| 何时创建 | 索引时，和倒排索引以前创建   | 搜索时候动态创建                             |
| 创建位置 | 磁盘文件                     | JVM Heap                                     |
| 优点     | 避免占用大量内存             | 索引速度快，不要占用额外的磁盘空间           |
| 缺点     | 索引速度慢，占用额外磁盘空间 | 文档过多时，动态创建开销大，占用过多JVM Heap |
| 默认值   | ES 2.x后                     | ES1.x及以前                                  |



关闭Doc Values

- 默认开启，可通过Mapping设置关闭
  - 增加索引的速度/减少磁盘占用空间
- 关闭后，如果要重新打开，需要重建索引
- 什么时候关闭？
  - ==很明确不需要做排序及聚合分析==



### 分页及遍历

ES默认根据相关性算分返回前10条数据

from:开始位置

Size:期望获取文档的总数



#### ES分布式系统中深度分页问题

- ES天生就是分布式的。查询信息时，数据分布在多个分片上，多台机器上，ES天生就需要满足排序的需要（按照相关性算分）
- 当查询：From=990,Size=10，需要查询的数据：from+size
  - 会在每个分片上先都获取1000个文档，然后，通过coordinating node整合所有结果。最后，再通过排序选择前1000个文档
  - 页数越深，占用内存越多
    - 为了避免深度分片带来的内存开销
    - ==ES有一个设定，默认限定到了10000个文档==









### ES原理初步认识

集群中，一个白正方形代表一个节点-Node，节点之间多个绿色小方块组成一个ES索引，一个ES索引本质是一个Lucene Index

一个索引下，分布在多个Node中的绿色方块为一个分片-Shard

即Shard=Lucene Index

![image-20220620211959189](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202120849.png)





Lucene是一个全文搜索库，ES建立在Lucene之上：

![image-20220620212355322](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202123427.png)





#### Lucene

在Lucene里面有很多的segment，我们可以把它们看成Lucene内部的mini-index

![image-20220620212718856](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202127950.png)



segment内部有很多数据结构

1. **Inverted index**(倒排索引)

   - 最为重要

   - ![image-20220620213016476](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202130604.png)

   - 主要包括两个部分

     - 有序的数据字段dictionary(包括单词term和它出现的频率)
     - 与单词term对应的postings(即存在这个单词的文件)

   - 当进行搜索时，首先将搜索的内容分解，然后再字典里找到对应的term，从而查找到与搜索相关的文件内容

   - ![image-20220620213310255](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202133361.png)

   - 查询 the fury

   - ![image-20220620213409957](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202134032.png)

     

2. Stored Fields

   - stored fields是一个简单的键值对，默认情况下ES会存储整个文件的Json source

   - 例如查找包含特定标题内容的文件时

   - ![image-20220620213753421](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202137520.png)

     

3. Document values

   - 上述两种结构无法解决排序、聚合等问题，所有提出了document values
   - 本质上是一个列式的存储，它高度优化了具有相同类型的数据的存储结构
   - ![image-20220620214013344](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202140445.png)

4. cache



搜索时，Lucene会搜索所有的segment，然后将每个segment的搜索结果返回，最后合并呈现给用户

Lucene的一些特性在这个过程中非常重要

1. segment是不可变的
   - delete：当删除发生时，Lucene做的只是将其标志位置为删除，但是文件还是会在它原来的地方，不会发生改变
   - update：所以对于更新来说，本质上它做的工作是：先删除，然后重新索引（Re-index
2. 随处可见的压缩
   - Lucene非常擅长压缩数据，基本上所有教科书上的压缩方式，都能在Lucene中找到
3. 缓存所有的数据
   - Lucene也会将所有的信息做缓存，这大大提高了它的查询效率



当ES搜索一个文件时，会为文件建立相应的缓存，并且会定期(每秒)刷新这些数据，然后这些文件就可以被搜索到

![image-20220620214547223](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202145321.png)

所有ES会将这些Segment合并，这个过程中segment最终会被删除掉，生成一个新的segment，这就是为什么添加文件可能会使索引所占空间变小，它会发生merge，从而可能会有更多的压缩

![image-20220620214739766](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202215396.png)



路由routing

每个节点都保留一份路由表，当请求到达任何一个节点时，ES都有能力将请求转发的期望的节点shard上进一步处理





#### ES整体结构

![image-20220620231634563](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202316667.png)

1. 一个ES集群模式下，由多个Node组成，每个节点就是ES的一个实例
2. 每个节点有多个分片，p0、p1是主分片，R0、R1是副本分片
3. 每个分片对应一个Lucene Index(底层索引文件)
4. Lucene Index是一个统称
   - 由多个segment组成(就是倒排索引)，每个segment存储着doc文档
   - commit point记录了索引segment的信息

#### Lucene索引结构

![image-20220620231950382](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202319532.png)

![image-20220620232007170](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202320315.png)

#### Lucene处理流程

![image-20220620231120127](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202311241.png)

创建索引过程

1. 准备待索引的原文档，数据来源可能是文件、数据库、网络等等
2. 对文档内容进行分词处理，形成一些列term
3. 索引组件对文档和term处理，形成字典和倒排表

搜索索引过程

1. 对查询语句进行分词处理，形成一系列term
2. 根据倒排索引表查找出包含term的文档，并进行合并形成文档结果集
3. 比对查询语句与各个文档相关性得分，按照得分高低返回















### ES原理-读取文档流程

> 第一阶段查询到匹配的docId，第二阶段再查询docId对应的完整文档，这种方式在ES中成为query_then_fetch



![image-20230408230008696](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304082300369.png)

1. 在初始查询阶段时，查询会广播到索引中每一个分片拷贝（主分片或者副本分片）。 每个分片在本地执行搜索并构建一个匹配文档的大小为 from + size 的优先队列。PS：在2. 搜索的时候是会查询Filesystem Cache的，但是有部分数据还在Memory Buffer，所以搜索是近实时的。

   

2. 每个分片返回各自优先队列中 所有文档的 ID 和排序值 给协调节点，它合并这些值到自己的优先队列中来产生一个全局排序后的结果列表。

   

3. 接下来就是 取回阶段，协调节点辨别出哪些文档需要被取回并向相关的分片提交多个 GET 请求。每个分片加载并丰富文档，如果有需要的话，接着返回文档给协调节点。一旦所有的文档都被取回了，协调节点返回结果给客户端。



ES的读

Elasticsearch中每个Shard都会有多个Replica，主要是为了保证数据可靠性，除此之外，还可以增加读能力，因为写的时候虽然要写大部分Replica Shard，但是查询的时候只需要查询Primary和Replica中的任何一个就可以了

![image-20230408230326431](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304082303526.png)



在上图中，该Shard有1个Primary和2个Replica Node，当查询的时候，从三个节点中根据Request中的preference参数选择一个节点查询。preference可以设置_local，_primary，_replica以及其他选项。如果选择了primary，则每次查询都是直接查询Primary，可以保证每次查询都是最新的。如果设置了其他参数，那么可能会查询到R1或者R2，这时候就有可能查询不到最新的数据



es查询是如何支持分布式的

![image-20230408230614469](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304082306614.png)



Elasticsearch中通过分区实现分布式，数据写入的时候根据_routing规则将数据写入某一个Shard中，这样就能将海量数据分布在多个Shard以及多台机器上，已达到分布式的目标。这样就导致了查询的时候，潜在数据会在当前index的所有的Shard中，**所以Elasticsearch查询的时候需要查询所有Shard，同一个Shard的Primary和Replica选择一个即可**，查询请求会分发给所有Shard，每个Shard中都是一个独立的查询引擎，比如需要返回Top 10的结果，那么每个Shard都会查询并且返回Top 10的结果，然后在Client Node里面会接收所有Shard的结果，然后通过优先级队列二次排序，选择出Top 10的结果返回给用户。

这里有一个问题就是请求膨胀，用户的一个搜索请求在Elasticsearch内部会变成Shard个请求，这里有个优化点，虽然是Shard个请求，但是这个Shard个数不一定要是当前Index中的Shard个数，只要是当前查询相关的Shard即可，这个需要基于业务和请求内容优化，通过这种方式可以优化请求膨胀数



ES中的查询分为两类：

1. Get请求：通过id查询特定Doc
2. Search请求：通过query查询匹配的Doc
   - 这个是核心功能



### Java如何于ES交互

1. 节点客户端
   - node client：节点客户端作为一个非数据节点加入到本地集群中
   - 换句话说：它本身不保存任何数据，但是它知道数据在集群的哪个节点中，并且可以把请求转发的正确的节点
2. 传输客户端
   - transport client：轻量级的传输客户端，可以将请求发送到远程集群
   - 它本身不加入集群，但是它可以将请求转发到集群中的一个节点上

