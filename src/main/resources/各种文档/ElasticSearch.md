# ElasticSearch



> ES 是一款非常强大的、基于 Lucene 的开源 **搜索及分析引擎**
>
> 它是一个实时的分布式搜索分析引擎，能让你以前所未有的速度和规模去探索你的数据

它被用作 ==全文索引、结构化搜索、分析==，以及这三个功能的组合

除了搜索，结合 Kibana、Logstash、Beats 开源产品、Elastic 技术栈（ELK)还被广泛运用在大数据近实时分析领域。包括：日志分析、指标监控、信息安全等等。它可以帮助我们探索海量结构化、非结构化数据，按需创建可视化报表，对监控数据设置报警阈值，通过机器学习，自动识别异常状况

ES 基于 Restful Web Api，使用 Java 开发的搜索引擎库，并作为 Apache 许可条款下的开放源码发布，是当前流行的企业级搜索引擎



应用场景

1. 开源搜索引擎，例如 Github 上搜索及高亮
2. 网上购物的商品推荐
3. 打车回家的时候帮助你定位附件的乘客或司机
4. ELK
5. 大数据分析
6. 等等





## 为什么不直接使用 Lucene

> ElasticSearch 是基于 Lucene 的



Lucene：**全文搜索引擎**

- Lucene 是当下最先进、高性能、全功能的搜索引擎库，但是 ==Lucene 仅仅只是一个库==
- Java 语言开发的，Hadoop 之父开发 Doug Cutting 开发的
- 只能基于 Java 语言开发，类库的接口学习难度高
- 原生不支持水平扩展



2004 年，ES 创始人 `Shay Banon` 基于 Lucene 开发了 Compass

2010 年，Shay Banon 重写了 Compass, 取名 ElasticSearch

- 支持分布式、可水平扩展

- 降低全文索引的学习曲线，可以被任何编程语言调用

   

![image-20250104205130113](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250104205132977.png)



Elasticsearch 也是使用 Java 编写的，它的内部使用 Lucene 做索引与搜索，但是它的目的是使全文检索变得简单，**通过隐藏 Lucene 的复杂性，取而代之的提供一套简单一致的 Restful Web API**

然而，ES 不仅仅是 Lucene，并且也不仅仅是一个全文搜索引擎，它可以被这样准确的描述：

1. 一个分布式的 ==实时文档存储==，每个字段都可以被索引与搜索
2. 一个分布式 ==实时分析搜索引擎==
3. 能胜任上百个服务器节点的扩展，并 **支持 PB 级别的结构化或非结构化数据**
   - PB 级别的数据存储容量：1024GB = 1TB，1024TB = 1PB





### lucene 实现全文索引流程

![image-20220623212318865](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206232123692.png)





## ES 主要功能与使用场景

主要功能

1. 海量数据的分布式存储以及集群管理
   - 实现服务与数据的高可用、水平扩展
2. 近实时搜索、性能卓越
   - 对结构化、全文、地理位置等数据类型的处理
3. 海量数据的近实时分析
   - 聚合功能



使用场景

1. 网站搜索、垂直搜索、代码搜索
2. 日志管理于分析、安全指标监控、应用性能监控、web 抓取舆情分析等等



## Elastic Stack 生态(ELK)

> Beats+ElasticSearch+Logstash+Kibana

下图展示了 ELK 生态以及基于 ELK 的场景

![image-20220619214741901](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192147011.png)





X-Pack 最初是收费的，在 2018 年的时候开源了，提供了几个订阅版本，base 版是免费使用的

 

![image-20220619214931561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192149659.png)





### Beats

> ES 对它的定义是轻量的数据采集器
>
> Beats 是一个面向 **轻量型采集器** 的平台，这些采集器可以从边缘机器向 Logstash、ElasticSearch 发送数据

它是由 Go 语言进行开发的，运行效率方面比较快。



从下图中可以看出，不同 Beats 的套件是针对不同的数据源。

1. Filebeat：用于监听日志数据，可以替代 logstash-input-file
2. packetbeat：用于监控网络流量
3. winlogbeat：用于搜索 windows 事件日志
4. 。。。

![image-20230407234355171](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304072343966.png)





### Logstash

> Logstash 是 **动态数据收集管道**，拥有可扩展的插件生态系统，支持从不同来源采集数据，转换数据，并将数据发送到不同的存储库中。其能够与 ElasticSearch 产生强大的协同作用，后被 Elastic 公司在 2013 年收购

2009 年诞生最初用于日志处理，2013 年被 ES 收购



它具有如下特性：

1. 几乎可以访问任何数据
2. 实时解析和转换数据
   - 例如：从 Ip 地址破译出地理坐标
3. 可扩展
   - 具有 200 多个插件（日志/数据库/Arcisigh/Netflow)
4. 可靠性、安全性
   - Logstash 会通过持久化队列来保证至少将运行中的事件送达一次
   - 同时将数据进行传输加密
5. 监控





主要组成部分：

1. Shipper—发送日志数据
2. Broker—收集数据，缺省内置 Redis
3. Indexer—数据写入

Logstash 提供了很多功能强大的滤网以满足你的各种应用场景。是一个 input | filter | output 的数据流



### ElasticSearch

ElasticSearch 对数据进行 **搜索、分析和存储**，它 **是基于 JSON 的分布式搜索和分析引擎**，专门为实现水平可扩展性、高可靠性和管理便捷性而设计的

它的实现原理主要分为以下几个步骤

1. 首先用户将数据提交到 ElasticSearch 数据库中
2. 再通过分词控制器将对应的语句分词
3. 将分词结果及其权重一并存入，以备用户在搜索数据时，根据权重将结果排名和打分，将返回结果呈现给用户



### Kibana

> Kibana = Kiwifruit+Banana, 奇异果+香蕉
>
> 最早是基于 Logstash 的工具，2013 年加入 ES



Kibana 实现 **数据可视化**，其作用就是在 ElasticSearch 中进行搜索并展示。Kibana 能够以图表的形式呈现数据，并且具有可扩展的用户界面，可以全方位的配置和管理 ElasticSearch

1. Kibana 可以提供各种可视化的图表
2. 可以通过机器学习的技术，对异常情况进行检测，用于提前发现可以问题



一个典型的日志系统包括

1. 收集：能够从多种数据源采集日志数据
2. 传输：能稳定的把日志数据解析过滤，并传输到存储系统
3. 存储：存储日志数据
4. 分析：支持 UI 分析
5. 告警：能够提供错误报告、监控机制





日志收集系统的演变

1. beats+es+kibana

   - beats 采集数据、存储在 ES 中、Kibana 进行展示
2. beats+logstash+es+kibana

   - Logstash 具有基于磁盘的自适应缓冲系统，该系统将吸收传入的吞吐量，从而减轻背压
3. beats+MQ+logstash+es+kibana

   - 加 MQ 的好处：降低对日志机器的影响

   - 如果有很多台机器需要做日志收集，那么让每台机器都向 Elasticsearch 持续写入数据，必然会对 Elasticsearch 造成压力，因此需要对数据进行缓冲，同时，这样的缓冲也可以一定程度的保护数据不丢失






## ES 架构

![image-20220623213234829](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206232132945.png)

1. Gateway 是 ES 用来存储索引的文件系统，支持多种类型
   - 默认使用本地文件系统：Local
2. Gateway 的上层是一个分布式的 lucene 框架。
3. Lucene 之上是 ES 的模块，包括：索引模块、搜索模块、映射解析模块等
4. ES 模块之上是 Discovery、Scripting 和第三方插件
   - Discovery 是 ES 的节点发现模块，不同机器上的 ES 节点要组成集群需要进行消息通信，集群内部需要选举 master 节点，这些工作都是由 Discovery 模块完成
   - 支持多种发现机制，如 Zen 、EC2、gce、Azure。
   - Scripting 用来支持在查询语句中插入 javascript、python 等脚本语言
   - scripting 模块负责解析这些脚本，使用脚本语句性能稍低
   - ES 也支持多种第三方插件。
5. 再上层是 ES 的传输模块和 JMX.传输模块支持多种传输协议，如 Thrift、memecached、http，默认使用 http。JMX 是 java 的管理框架，用来管理 ES 应用。
6. 最上层是 ES 提供给用户的接口，可以通过 RESTful 接口、Java API和 ES 集群进行交互







## ES 基础-相关名词

- Near Realtime(NRT) 
  - 近实时，数据提交索引后，立马就可以搜索到
- Cluster 集群
  - 一个集群由一个唯一的名字标识，默认为 elasticsearch。集群名称非常重要，**具有相同集群名的节点才会组成一个集群**，集群名称可以在配置文件中指定
- Node 节点
  - 存储集群的数据，参与集群的索引和搜索功能。
  - 每个节点也有自己的名称，默认在启动时会以一个随机的 UUID 的前七个字符作为节点的名字，可以为其指定任意的名字
  - 通过集群名在网络中发现同伴组成集群。一个节点也可是集群
- Index 索引
  - 一个索引是一个 **文档的集合**
  - 每个索引有唯一的名字，通过这个名字来操作它
  - 一个集群中可以有任意多个索引
- Type 类型
  - 指在一个索引中，可以索引不同类型的文档，如用户数据、博客数据
  - 从 6.0.0 版本起已废弃，一个索引中只存放一类数据
- Document 文档
  - 被索引的一条数据，索引的基本信息单元，以 JSON 格式来表示
  - 类似与关系型数据库中的一条记录
- 字段
  - 文档中的字段，对文档数据根据不同属性进行分类的标识
  - 相当于关系型数据库中一条记录的一个字段
- 映射
  - 是处理数据的方式和规则方面做一些限制
  - 如某个字段的数据类型、默认值、分析器、是否被索引等等，这些都是映射里面可以设置的
  - 其它就是处理 es 里面数据的一些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射，并且需要思考如何建立映射才能对性能更好
- 分片
  - 在创建一个索引时，可以指定分成多少个分片来存储
  - 每个分片也是一个功能完善且独立的'索引'，可以被放置在集群的任意节点上
- Replication
  - 备份，一个分片可以有多少个副本备份







## 文档

ES 是面向文档的，文档是所有可搜索数据的最小单位

- 日志文件中的每一个日志项，就是一个文档
- 一本电影的具体信息、一张唱片的信息，就是一个文档
- 一首歌、一篇 pdf 文档中的具体内容，就是一个文档
- 等等



文档会被序列化为 Json 格式，存储在 ES 中

- Json 文档格式灵活，不需要预定义格式
- 字段类型可以手动指定或者通过 ES 自动推算
  - 生产环境中，推荐使用手动 Mapping, 自动推算可能不准确



每个文档具有 Unique Id

- 可以自己指定 id、可以 ES 自己生成



### 文档的元数据

> 用于标注文档的相关信息

- _index：文档所在的索引名称
- _type：文档的所属类型，ES7 开始，只有是`_doc`
- _id：文档唯一 id
- _score：相关性打分
- _version：文档版本，若文档修改了，该字段会增加
- _source：文档的原始 Json 数据

~~~json
{
  "_index " : " test_logs2 ",
  "_type " : "_doc ",
  "_id " : " 1 ",
  "_version " : 1,
  "_seq_no " : 0,
  "_primary_term " : 1,
  "found" : true,
  "_source " : {
    "uid" : 1,
    "username" : "test"
  }
}
~~~



## 集群

Cerebro：ES 可视化集群工具

ES 天生就是分布式的，它知道如何通过管理多个节点来提高扩容性和可用性，这也意味着我们的应用无需关注这个问题



Master eligible Nodes

- 每个节点启动后，默认就是一个 Master eligible 节点
- eligible 节点可以参加选主流程，成为 Master
- 第一个节点启动的时候，它会将自己选举出 Master 节点
- 每个节点上都保存了集群的状态，只有 master 节点才能修改集群的状态信息



应对故障

> ES 可以应对节点故障，当主节点发生故障时，会立即选举出一个新的主节点



Master Node角色

- master节点是ES集群的管理者，负责维护集群的全局状态和协调操作
- 主要功能
  - 集群管理、协调
  - 节点管理
  - 分片管理
  - 安全性

Master eligilble

- 有资格成为Master节点的节点角色



Data Node角色

- 可以保存数据的节点，负责保存分片信息，在数据扩展上起到了至关重要的作用



Coordinating Node

- 负责接收 Client 的请求，将请求分发到合适的节点，最终把结果汇聚在一起
- 每个节点默认起到了 Coordinating Node 的职责



Hot& Warm Node

- 不同硬件配置的 Data Node，用来实现 Hot & Warm 架构，降低集群部署的成本



Machine Learning Node

- 负责跑机器学习的 Job，用来做异常检测



Tribe Node

- 5.3 开始采用 Cross Clustor Search, Tribe Node 连接到不同 ES 集群，并支持将这些集群当作一个单独的集群处理



节点启动时候，读取配置文件，知道自己承担什么样的角色 



当一个节点被选举成为 *主* 节点时， 它将负责管理集群范围内的所有变更，例如增加、删除索引，或者增加、删除节点等。 

而 **主节点并不需要涉及到文档级别的变更和搜索等操作**，所以当集群只拥有一个主节点的情况下，即使流量的增加它也不会成为瓶颈。 任何节点都可以成为主节点

作为用户，我们可以将请求发送到 *集群中的任何节点* ，包括主节点。 每个节点都知道任意文档所处的位置，并且能够将我们的请求直接转发到存储我们所需文档的节点。==无论我们将请求发送到哪个节点，它都能负责从各个包含我们所需文档的节点收集回数据==，并将最终结果返回給客户端。 Elasticsearch 对这一切的管理都是透明的



## 集群分布式及选主问题

- ES 分布式架构中，不同的集群通过名字来区分，默认名字是“elasticsearch”



节点

- 一个节点就是一个 ES 实例
  - 本质就是一个 Java 进程
  - 一台机器可以运行多个 ES 进程，但是生产一般一台机器一个 ES 实例
- 每个节点都有名字
- 每个节点在启动之后，会分配一个 UID，保存在 data 目录下



Coordinating Node

- 处理请求的节点，叫 Coordinating Node
  - 路由请求到正确的节点，例如：创建索引的请求，需要路由到 Master
- 所有的节点默认是 Coordinating Node



Data Node

- 保存数据的节点
  - 节点启动后，默认就是 Data Node
- 保存分片数据
  - 由于 Master Node 决定如何把分片分发到数据节点上
- 通过增加数据节点
  - 可以解决数据水平扩展和解决数据单点问题

Master Node

- 职责
  - 处理创建、删除等请求/决定分片被分配到哪个节点/负责索引的创建删除
  - 维护并更新 Cluster State
- 最佳实践
  - 部署要考虑单点问题
  - 为一个集群设置多个 master/每个节点只承担 Master 的单一角色







### 集群健康

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

status 字段

- Green
  - 所有主分片和副本分片都已分配并正常运行
- Yellow
  - 所有主分片运行正常，但不是所有的副本分片都正常运行
  - 影响：数据的高可用性降低
- Red
  - 集群不健康
  - 部分或全部主分片未分配
  - 影响：数据丢失或不可以、集群功能部分或全部失效





### 添加索引

> 往 ES 添加数据时，需要用到索引，它是保存数据的地方，索引实际上是指向一个或多个物理分片的逻辑命名空间

即：一个索引中的数据，可能保存在多个分片上面。（和 kafka 里面 topic 的分片类似）



一个分片是一个底层的工作单元，它仅保存了全部数据中的一部分，一个分片是一个 Lucene 实例，并且一个分片本身就是一个完整的搜索引擎

我们的文档被存储和索引到分片内，**但是应用程序是直接与索引进行交互而不是与分片进行交互**



Elasticsearch 是利用分片将数据分发到集群内各处的。分片是数据的容器，文档保存在分片内，分片又被分配到集群内的各个节点里。 当你的集群规模扩大或者缩小时， Elasticsearch 会自动的在各节点中迁移分片，使得数据仍然均匀分布在集群里



一个分片可以是主分片或副本分片，索引内任意一个文档都归属于一个主分片，所以主分片的数量决定着索引能保存的最大数据量。技术上来说一个主分片最大能存储的文档数量为：Interger.Max_value-128



一个副本分片只是一个主分片的拷贝，副本分片是作为硬件故障时保障数据不丢失的冗余备份，**并且为搜索和返回文档等读操作提供服务**

==索引建立的时候就确定了主分片数，但是 **副本分片是可以随时修改** 的==



例如：创建一个索引，分配 3 个主分片和一个副本(每个主分片一个副本)

~~~sh
PUT /blogs
{
   "settings" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1
   }
}
~~~

因为现在是单节点集群，所以 3 个主分片都被分配到了 Node1

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



因为是单节点集群，所以副本并没有被分配到任何节点，即 unassiged_shards：3 这三个副本分片都没有分配到任何节点



### 添加故障转移

> 集群中只有一个节点运行时，意味着单节点故障问题

启动一个节点时候，将 cluster.name 配置为之前集群的名字，这个节点就会 **自动发现集群并加入到其中**



所有新添加的文档都将会保存在主分片上，然后被并行复制到对应的副本分片上。这样保证了既可以从主分片又可以从副本分片获得文档



### 水平扩容

分片是一个功能完整的搜索引擎，它拥有使用一个节点上的所有资源的能力。

主分片的数目在索引创建的时候就确定下来了，实际上，这个数目定义了这个索引能够存储的最大数据量（实际大小取决于你的数据、硬件和使用场景）。但是，读取操作可以同时被主分片和副本分片处理，所以当拥有越多的副本分片时，就意味着拥有更高的吞吐量

![image-20230410104200452](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304101042297.png)

`Node 1` 和 `Node 2` 上各有一个分片被迁移到了新的 `Node 3` 节点，现在每个节点上都拥有 2 个分片，而不是之前的 3 个。 这表示每个节点的硬件资源（CPU, RAM, I/O）将被更少的分片所共享，每个分片的性能将会得到提升



所以，水平扩容时候，可以 **动态调整副本分片的数目**，按需伸缩集群

但是，**如果只是在相同节点数目** 的集群上增加更多的副本分片并不能提高性能，因为每个分片从节点上获得的资源会变少。 你需要 **增加更多的硬件资源** 来提升吞吐量。同时，副本分片的增多会提高数据的冗余







## 分片

- 主分片，用以解决数据水平扩展问题
  - 通过主分片，可以将一个索引数据分布到集群内的不同节点上
  - 一个分片运行一个 Lucene 实例
  - 主分片数在索引创建时指定，后续不允许修改，除非 ReIndex
- 副本
  - 副本分片数，可以动态调整
  - 增加副本数，可以一定程度上提供访问的可用性（读取的吞吐增加）



分片数的设定

- 对于生产环境中分片的设定，需要提前做好容量规划
- 分片数量过小
  - 后续无法增加节点实现水平扩展
  - 单个分片的数据量太大，导致数据重新分配时耗时
- 分片数量过大
  - 7.0 开始，默认主分片设置为 1
  - 影响搜索结果的相关性打分，影响统计结果的准确性





### 分片的内部原理

- 什么是 ES 分片
  - ES 中最小的工作单元（是一个 Lucene 的 Index)
- 一些问题
  - 为什么 ES 的搜索是近实时的（1s 后被搜索到）？
  - ES 如何保证断电时数据不丢失？
  - 为什么删除文档，并不会立即释放空间？



#### 倒排索引的不可变性

- 倒排索引采用 Immutable Design, 一旦生成，不可更改
- 不可变性，带来的好处
  - 无需考虑并发写文件的问题，避免了锁机制带来的性能问题
  - 一旦读入内核的文件系统缓存，便留在那里。只要文件系统有足够的空间，部分请求就会直接请求内存，不会命中磁盘，提升了很大的性能
  - 缓存容易生成和维护，系统可以充分利用缓存
  - 倒排索引允许数据被压缩
- 不可变性，带来的挑战
  - ==如果需要让一个新的文档可以被索引，需要重建整个索引==



#### Lucene Index

- Lucene 中，==单个倒排索引文件被称为 Segment==，Segement 是自包含的、不可变更的。多个 Segment 汇聚在一起，就称为 Lucene 的 Index，其对应的就是 ES 中的一个 Shard 分片
- 当有新的文档写入时，会生成新的 Segment，==查询时，会同时查询所有的 Segments==，并对结果汇总。Lucene 中有一个文件 CommitPoint，用来记录所有的 Segements 信息
- 删除的文档信息，保存在 `.del` 文件中

![image-20250107210018237](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107210019887.png)



#### ES 中的 Refresh

![image-20250107210757851](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107210759162.png)

- ES 在写入文档的时候，会先将文档写入 Index Buffer 的内存空间
- Index buffer 中的文档写入 Segment 的过程，叫做 Refresh, Refresh 不执行 fsync 操作
  - 当文档写入 Segment 中后，这些文档就可以被搜索到了
- Refresh 频率
  - 默认是 1s 发生一次，可配置
  - ==这就是为什么 ES 被称为近实时搜索的原因==
- 如果系统有大量的数据写入，就会产生很多的 Segment
- Index buffer 被占满时，会触发 Refresh，默认大小是 JVM 的 10%



#### Transaction Log

![image-20250107212123830](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107212125835.png)

- Segement 写入磁盘的过程相对耗时，借助文件系统缓存，==Refresh 时，先将 Segment 写入缓存以开放查询==
- 为了保证数据不丢失，所以在 Index 文档时，同时写入 Transaction Log，高版本中，Transaction Log 是默认落盘，每个分片有一个 Transaction Log
- 在 ES Refresh 时，Index buffer 被清空，Transaction Log 不会清空
  - 这就是为什么 ES 断电后，数据不丢失，因为 Transaction Log 已经落盘了



#### ES 的 Flush

![image-20250107212626282](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107212627411.png)

- 调用 Refresh，Index buffer 清空并且 Refresh
- 调用 fsync(操作系统级别)，将缓存的 Segments 写入磁盘
- 清空 Transaction Log
- 何时触发 Flush（比较重）
  - 默认 30min，调用一次
  - Transaction Log 满（512MB)



#### ES 的 Segment Merge

ES 完成 Flush 后，Segments 会写入磁盘，随着时间的推移，Segment 会越来越多，所以会定期处理

ES 的 Merge 主要做 2 个事情

- Segment 很多，需要定期合并

  - 减少 Segments/删除已经删除的文档

- ES 和 Lucene 会自动进行 Merge 操作

  - ~~~
    post _index/_forcemerge
    ~~~











## 文档分布式存储

文档是存储在分片上

> 当索引(存储）一个文档的时候，文档会被存储到一个主分片中。ES 如何知道这个文档放到哪个分片？



- 文档会存储在具体的某个主分片和副本分片上
- 文档到分片的映射算法
  - 确保文档能均匀分布在所有分片上
  - 潜在的算法
    - 随机/Round Robin
    - 维护文档到分片的映射关系，当文档数据量大时，维护成本高
    - 实时计算，通过文档 id，自动计算需要映射到哪个分片上



### 路由一个文档到一个分片

ES 索引文档到分片的默认路由算法：

- shard = hash(_routing)%number_of_primary_shards
- hash 算法确保文档均匀分散到分片中



1. `_routing` 是一个可变值，默认为文档_id
   - 这个值，也可以设置为自定义的值
   - **通过这个参数我们可以自定义文档到分片的映射**。一个自定义的路由参数可以用来确保所有相关的文档——例如所有属于同一个用户的文档——都被存储到同一个分片中
2. `number_of_primary_shards`：主分片的数量
   - ES 中，设置好 Index Settings 后，主分片数量不能修改的原因就是这个，会导致路由算法错误
   - **这就解释了为什么我们要在创建索引的时候就确定好主分片的数量 并且永远不会改变这个数量：因为如果数量变化了，那么所有之前路由的值都会无效，文档也再也找不到了**





### 主分片与副本分片交互

假设有一个集群由 3 个节点组成。包含一个 blogs 索引，两个主分片，每个主分片两个副本分片。相同的主分片与副本分片不会放在同一个节点

![image-20220622163611805](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221636449.png)



我们可以发送请求到集群中的任一节点。 每个节点都有能力处理任意请求。 每个节点都知道集群中任一文档位置，所以可以直接将请求转发到需要的节点上。 

在下面的例子中，将所有的请求发送到 master `Node 1` ，我们将其称为 *协调节点(coordinating node)* 

当发送请求的时候， 为了扩展负载，更好的做法是轮询集群中所有的节点。

### 新建、索引、删除文档

![image-20220622164644609](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221646160.png)

这三个操作都是写操作，必须在主分片上面完成之后，才能被复制到相关的副本分片中

以下是在主副分片和任何副本分片上面 成功新建，索引和删除文档所需要的步骤顺序：

1. 客户端向 `Node 1` 发送新建、索引或者删除请求。
2. **节点使用文档的 `_id` 确定文档属于分片 0** 。因为分片 0 的主分片目前被分配在 `Node 3` 上，请求会被转发到 `Node 3`
3. `Node 3` 在主分片上面执行请求。如果成功了，它将请求并行转发到 `Node 1` 和 `Node 2` 的副本分片上。一旦所有的副本分片都报告成功, `Node 3` 将向协调节点报告成功，协调节点向客户端报告成功



### 取回一个文档

可以从主分片或者从其它任意副本分片检索文档

![image-20220622165813493](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221658278.png)

以下是从主分片或者副本分片检索文档的步骤顺序：

1. 客户端向 `Node 1` 发送获取请求。
2. 节点使用文档的 `_id` 来确定文档属于分片 `0` 。分片 `0` 的副本分片存在于所有的三个节点上。 在这种情况下，它将请求转发到 `Node 2` 。
3. `Node 2` 将文档返回给 `Node 1` ，然后将文档返回给客户端

在处理读取请求时，**协调结点在每次请求的时候都会通过轮询所有的副本分片** 来达到负载均衡

在文档被检索时，已经被索引的文档可能已经存在于主分片上但是还没有复制到副本分片。 在这种情况下，副本分片可能会报告文档不存在，但是主分片可能成功返回文档。 **一旦索引请求成功返回给用户，文档在主分片和副本分片都是可用的**（创建时，只有返回成功，证明主、副分片上面都有了）





### 更新一个文档

1. 请求发送到一个 Coordination node 节点
   - （所有节点默认都是 coordination node, 即都可以扮演协调者角色)
2. hash 算法算出这个请求需要路由到哪个 ==主分片==
3. 请求发送到节点分片上
4. ES 对文档的更新是，先删除
5. 在重新创建出这个数据
6. 返回给 Coordination node
7. 返回给用户

![image-20250107204258555](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107204300024.png)



### 删除一个文档

与更新类似

![image-20250107205116212](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107205117521.png)







### 多文档模式

`mget` 和 `bulk` API 的模式类似于单文档模式。区别在于协调节点知道每个文档存在于哪个分片中。 它将整个多文档请求分解成 *每个分片* 的多文档请求，并且将这些请求并行转发到每个参与节点。

协调节点一旦收到来自每个节点的应答，就将每个节点的响应收集整理成单个响应，返回给客户端

![image-20220622172209455](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221722764.png)



使用单个 `mget` 请求 **取回多个文档** 所需的步骤顺序：

1. 客户端向 `Node 1` 发送 `mget` 请求。
2. `Node 1` 为每个分片构建多文档获取请求，然后并行转发这些请求到托管在每个所需的主分片或者副本分片的节点上。一旦收到所有答复， `Node 1` 构建响应并将其返回给客户端。

可以对 `docs` 数组中每个文档设置 `routing` 参数



![image-20220622172244340](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221722708.png)

`bulk` API 按如下步骤顺序执行：

1. 客户端向 `Node 1` 发送 `bulk` 请求。
2. `Node 1` 为每个节点创建一个批量请求，并将这些请求并行转发到每个包含主分片的节点主机。
3. 主分片一个接一个按顺序执行每个操作。当每个操作成功时，主分片并行转发新文档（或删除）到副本分片，然后执行下一个操作。 一旦所有的副本分片报告所有操作成功，该节点将向协调节点报告成功，协调节点将这些响应收集整理并返回给客户端。

`bulk` API 还可以在整个批量请求的最顶层使用 `consistency` 参数，以及在每个请求中的元数据中使用 `routing` 参数





## 索引

> 索引是文档的容器，是一类文档的集合

- Index 是逻辑空间的概念
  - 每个索引都有自己 Mapping 定义，用于定义包含的文档的字段名和字段类型
- Shard 是物理空间的概念
  - 索引中的数据，分散存储在不同的 Shard 分片上



索引的 Mapping 与 Settings

- Mapping 定义文档字段的类型
- Settings 定义不同的数据分布



索引的不同语义

- 名词
  - 一个 ES 集群中，可以创建多个不同的索引
- 动词
  - 将一个文档保存到 ES 的过程，叫做索引
  - ES 中，创建一个倒排索引的过程
- 名词（抛开 ES 说）
  - 一个 B-Tree 索引、倒排索引等



以下语句会动态创建一个 customer 的索引 index

~~~sh
PUT /customer/_doc/1
{
  "name": "John Doe"
}
~~~

而这个索引实际上已经自动创建了它里面的字段(name)的类型，以下为它自动创建的映射 mapping

~~~json
{
  "mappings": {
    "_doc ": {
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
#config/elasticsearch.yml 的每个节点下添加配置
action.auto_create_index: false
~~~



### 索引库配置管理：settings

1. 通过 settings 可以修改索引的分片数和副本数
2. 零停机重新索引数据
   - es 在字段 mapping 建立后，就不能再次修改 mapping 的值了
   - 利用拷贝，将一个索引拷贝成另一个索引来实现



### 索引的格式

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
   - properties：由于 type 在后续版本会被去除掉，所以无需对 type 嵌套



### 文档 CRUD

- 创建索引
  - POST、PUT
    - Index 方式：先删除后写入，原有文档先删除，后写入的 version 会+1
    - Create 方式
- 删除索引
- 更新索引
  - Post
    - update api: version+1
- 查询文档
- Bulk Api
  - 一次 api 调用，对索引进行不同的操作
  - 并且可以多不同索引进行操作
  - 操作中，单条失败，不会影响其他操作
- 批量读取：mget Api
- 批量查询







## 倒排索引

倒排索引又叫反向索引，==是搜索引擎中，非常重要的数据结构==



#### 正向索引

用户发起查询时(假设查询为一个关键词)，搜索引擎会扫描索引库中的所有文档，找出所有包含关键词的文档，这样依次 **从文档中去查找是否含有关键词的方法叫做正向索引**

互联网上存在的网页(或文档)不计其数，这样遍历的索引结构效率低下，无法满足用户需求

正向索引结构：

文档 1 的 id——> 单词 1 的信息；单词 2 的信息；单词 n 的信息

文档 n 的 id——> 单词 1 的信息；单词 2 的信息；单词 n 的信息

![image-20220621214736369](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212147599.png)



#### 反向索引

为了提升效率，搜索引擎会把正向索引变为反向索引(倒排索引)，即把文档——> 单词的形式变为单词——> 文档 的形式

倒排索引结构：

单词 1→ 文档 1 的 ID；文档 2 的 ID；文档 3 的 ID…
单词 2→ 文档 1 的 ID；文档 4 的 ID；文档 7 的 ID…

![image-20220621214940479](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212149605.png)



#### 单词-文档矩阵

单词-文档矩阵是表达两者之间所具有的一种包含关系的概念模型

现有以下几个文档：

- D1：乔布斯去了中国。
- D2：苹果今年仍能占据大多数触摸屏产能。
- D3：苹果公司首席执行官史蒂夫·乔布斯宣布，iPad2 将于 3 月 11 日在美国上市。
- D4：乔布斯推动了世界，iPhone、iPad、iPad2，一款一款接连不断。
- D5：乔布斯吃了一个苹果

此时用户查询 "苹果 and (乔布斯 or ipad2)"：表示包含苹果，同时包含乔布斯或 ipad2

![image-20220621215355925](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212153021.png)

搜索引擎的索引其实就是实现 "单词-文档" 矩阵的具体数据结构。可以有不同的方式来实现上述模型，比如：倒排索引、签名文件、后缀树等等方式，但是倒排索引是实现单词到文档映射关系的最佳实现方式



#### 倒排索引

> 核心分成：单词词典、倒排列表

倒排索引是实现单词-文档矩阵的一种具体存储形式，通过倒排索引可以根据单词快速获取包含这个单词的文档列表

倒排索引主要由两部分组成：单词词典、倒排文档

1. 单词词典
   - 会记录所有文档的单词，记录单词-> 倒排列表的关联关系
   - 单词词典是由文档集合中出现过的所有单词构成的字符串集合，单词词典内每条索引项记载单词本身的一些信息以及指向倒排列表的指针
   - 单词词典一般比较大，可以通过 B+Tree 或哈希拉链法实现，以满足高性能的插入和查询
2. 倒排列表
   - posting List，记录了单词对应的文档集合，由倒排索引项组成
   - 倒排索引项
     - 文档 ID
     - 词频 TF term frequency：该单词在文档中出现的次数，用于相关性评分
     - 位置：单词在文档中分词的文章，用于语句搜索
     - 偏移：记录单词的开始结束位置，用于高亮显示
3. 倒排文件
   - Inverted file，所有单词的倒排列表往往是顺序的存储在磁盘的某个文件里，这个文件称为倒排文件，倒排文件是存储倒排索引的物理文件

![image-20220621221334545](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212213673.png)

![image-20250104231359229](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250104231400618.png)





==ES 的倒排索引==

- ES 的 Json 文档中的每个字段，都有自己的倒排索引
- 可以指定对某些字段不做索引
  - 优点：节省存储空间
  - 缺点：字符无法被搜索



#### 案例

文档中的数字代表文档的 ID，例如 Doc1 中的 1

- Doc1：乔布斯去了中国。
- Doc2：苹果今年仍能占据大多数触摸屏产能。
- Doc3：苹果公司首席执行官史蒂夫·乔布斯宣布，iPad2 将于 3 月 11 日在美国上市。
- Doc4：乔布斯推动了世界，iPhone、iPad、iPad2，一款一款接连不断。
- Doc5：乔布斯吃了一个苹果。

通过以上 5 个文件，建立简单的倒排索引

| 单词 ID(WordID) | 单词(Word) | 倒排列表(DocID) |
| -------------- | ---------- | --------------- |
| 1              | 乔布斯     | 1，3，4，5      |
| 2              | 苹果       | 2，3，5         |
| 3              | iPad2      | 3，4            |
| 4              | 宣布       | 3               |
| 5              | 了         | 1，4，5         |
| …              | …          | …               |

首先分词系统会把文档自动切分成单词序列，这样就由文档转换为单词序列构成的数据流，并对每个不同的单词赋予唯一的单词编号(wordId)，并且每个单词都有对应的含有该单词的文档列表即倒排列表

例如：单词 Id 1 对应的单词为乔布斯，单词乔布斯的倒排列表为{1，3，4，5}即文档 1、3、4、5 都包含单词乔布斯

下面是更复杂的包含更多信息的倒排索引

TF(term frequency)：单词在文档中出现的次数

pos：单词在文档中出现的位置

| 单词 ID(WordID) | 单词(Word) | 倒排列表(DocID; TF; <Pos>)                |
| -------------- | ---------- | --------------------------------------- |
| 1              | 乔布斯     | (1; 1; <1>),(3; 1; <6>),(4; 1; <1>),(5; 1; <1>) |
| 2              | 苹果       | (2; 1; <1>),(3; 1; <1>),(5; 1; <5>)           |
| 3              | iPad2      | (3; 1; <8>),(4; 1; <7>)                     |
| 4              | 宣布       | (3; 1; <7>)                               |
| 5              | 了         | (1; 1; <3>),(4; 1; <3>)(5; 1; <3>)            |
| …              | …          | …                                       |

比如单词“乔布斯”对应的倒排索引里的第一项(1; 1; <1>)意思是，文档 1 包含了“乔布斯”，并且在这个文档中只出现了 1 次，位置在第一个



## 分词

Analysis、Analyzer

- Analysis: 文本分析，是把全文转换为一系列单词(term/token)的过程，也叫分词
  - Analysis 是通过 Analyzer 实现的
  - 可以使用 ES 内置的分析器或自定义分析器
- 除了在数据写入时转换词条，匹配 Query 语句时，也需要用相同的分词器对使用同样的分词器对查询语句进行分析



Analyzer

分词器由专门处理分词的组件构成

- Character Filters：字符过滤器
  - 针对原始文本处理
  - **字符过滤器的任务** 是在分词前整理字符串，例如：去掉 html、将&转化为 and 等等
- Tokenizer：分词器
  - 按照规则切分为单词
- Token Filters：Token(分词、词条)过滤器
  - 将切分后的单词进行二次加工
  - 这个过程可能会改变词条(例如：小写化)、删除词条(例如：a、and、the 这些无用词)、增加词条



ES 内置分词器

==_analyze API 可以帮助我们分析如何分词的 ==

- Standard Analyzer：默认分词器
  - 按词分词，小写处理
- Simple Analyzer
  - 按照非字母切分，非字母的都去除
  - 小写处理
- Whitespace Analyzer
  - 按照空格进行切分分词
- Stop  Analyzer
  - 相比 Simple Analyzer 多了 Stop filter
  - 会把 the、a、is 等这些修饰词去除
- Keyword  Analyzer
  - 不分词，将输入当作一个结果 term 输出
- Pattern Analyzer
  - 通过正则表达式进行分词
  - 默认是\W+, 非字符的符号进行分隔
- Language Analyzer
  - 不同国家的语言提供了支持



中文分词的难点

- 中文句子，切分一个一个词（不是一个个字）
- 英文中，单词有自然的空格作为分隔
- 一句中文，在不同的上下文，有不同的理解，所以更难





什么时候会使用到分析器

1. 当我们索引一个文档，它的全文域被分析成词条，以用来创建倒排索引
2. 全文查询中，需要将查询字符串通过相同的分析过程，以保证我们搜索的词条格式与索引中的词条格式一致





### ES 安装中文分词器

ICU Analyzer、ik、Thulac、HanLP 等等中文分词器

> ES 自带的英文分词器太简单，对于中文的分词，需要安装一个叫做 **iK 分词器** 来解决对中文的分词问题



ik 分词器带有两个分词器

1. ik_max_word
   - 会将文本做 **最细粒度的拆分**，尽可能的拆分初词语句子
   - 例如：我爱我的都祖国
   - 结果：我、爱、我的、祖、国、祖国
2. ik_smart
   - 会 **做最粗粒度的拆分**，已被分出的词语不会再次被其他词语占有
   - 例如：中华人民共和国国歌
   - 结果：中华人民共和国、国歌





## Search API

- URL Search: 在 Url 中使用查询参数
- Request Body Search
  - 使用 ES 提供的基于 Json 格式的更完备的查询语句：Query Domain Specific Language，DSL



| 语法                   | 范围              |
| ---------------------- | ----------------- |
| /_search               | 集群中所以的索引  |
| /index1/_search        | index1            |
| /index1, index2/_search | index1、index2    |
| /index*/_search        | 以 index 开头的索引 |



~~~json
{
  "took": 0,  //花费的时间
  "timed_out": false,
  "_shards ": {
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
    "hits": [  //返回结果集，默认前 10 个文档
      {
        "_index ": " test-index-user ",
        "_id ": " xPZsMJQBcLXQ9h4VdKV3 ",
        "_score ": 1, //文档相关性评分
        "_source ": { //文档原始信息
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









## 查询

查询参数比较多，需要时在查文档即可

ES 的查询基于 JSON 风格的 DSL 来实现的，DSL: Domain Specific Language，即
领域特定语言；使用 Json 构造一个请求体



ES查询中，主要的顶层关键字及其作用如下

~~~json
{
  "query": {},           // 查询体
  "from": 0,            // 分页起始位置
  "size": 10,           // 返回文档数量
  "sort": [],           // 排序
  "aggs": {},           // 聚合
  "_source": [],        // 指定返回字段
  "highlight": {},      // 高亮设置
  "search_after": [],   // 深分页
  "scroll": "1m"        // 滚动查询
}
~~~





常用的查询类型包括

1. 查询所有：查询出所有的数据，一般测试用。例如：match_all
2. 全文检索：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如： match_query ; multi_match_query
3. 精确查询：根据精确词条值查找数据，一般是查找 keyword、数值、日前、boolean 等类型字段。例如：ids ；range; term
4. 复合查询
5. 地理查询



### 查询数据

~~~sh
GET /bank/_search
{
  "query": { 
      "match_all": {},

      //match 查询表达式, abc 与 def 是 oR 的条件
      "match":{
        "address": "abc def"
      }
      "query_string":{
      	"fields": [a, b]
      	"query": xxx
      },
      "simple_query_string":{
      	"fields": [a, b]
      	"query": xxx
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





### 脚本字段

> script_fields, 可以利用它做一些计算后，返回新的字段，使用时详细看







### 嵌套聚合

聚合条件的嵌套，例如计算每个州的平均结余，需要在 state 分组基础上，嵌套计算 avg(balance)

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



另外，可以对聚合结果排序

 对嵌套计算出的 avg(balance)，这里是 average_balance，进行排序

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





### 复合查询

> 在查询中，会有多种条件组合的查询，在 ES 中称为复合查询



在查询中会有多种条件组合的查询，在 ES 中叫做复合查询，常用的 5 种复合查询有：

1. bool query：布尔查询
2. boosting query：提升查询
3. constant_score：固定分数查询
4. dis_max：最佳匹配查询
5. function_score：函数查询



#### bool query

> 通过布尔逻辑将较小的查询组合成较大的查询

布尔查询特点

- 子查询可以任意顺序出现
- 可以嵌套多个查询，包括 boole query 嵌套查询
- 若bool查询种没有must条件，那么should中至少满足一条才会返回结果



bool 查询包含 4 种操作符

> 它们均是一种数组，数组里面是对应的判断条件

1. must：必须匹配，贡献算分
2. must_not：过滤子句，必须不能匹配，但不贡献算分
3. should：选择性匹配，至少满足一条，贡献算分
4. filter：过滤子句，必须匹配，不贡献算分

~~~json
{
    "query":{
        "bool":{
            "must":{
                "term":{
                    "user.name":"kim"
                }
            },
            "filter":{
                "term":{
                    "tag":"production"
                }
            }
        }
    }
}
~~~





#### boosting

> 与bool查询不同，boosting是降低显示的权重/优先级(即 score)

比如搜索逻辑是 name = 'apple' and type ='fruit'，对于只满足部分条件的数据，不是不显示，而是降低显示的优先级（即 score)

例如：

~~~json
#创建数据
POST /test-dsl-boosting/_bulk
{ "index": { "_id ": 1 }}
{ "content": "Apple Mac" }
{ "index": { "_id ": 2 }}
{ "content": "Apple Fruit" }
{ "index": { "_id ": 3 }}
{ "content": "Apple employee like Apple Pie and Apple Juice" }
~~~



~~~sh
#对 pie 进行降级
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

固定分数查询：查询某个条件时，固定的返回指定的 score，显然当不需要计算 score 时，只需要 filter 条件即可，因为 filter 忽略分数

例如：score 为 1.2

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

disjunction max query：分离最大化查询，指的是，将任何与任意查询匹配的文档作为结果返回，但只将 **最佳匹配的评分作为查询的评分结果返回** 。

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

函数查询：通过自定义函数的方式，来修改或替换文档的原始相关性评分 `_score`

`function_score` 属性查询，可以优化算分

可以在查询结束后，对每一个匹配的文档进行一系列的重新算分，根据新生成的分数进行排序



**适用场景**

- 基于业务逻辑调整文档的评分
  - 例如：搜索商品时，结合商品名称匹配度与销量进行排序
- 结合多个评分函数来优化搜索结果
- 实现个性化推荐或基于地理位置、时间等因素的排序



ES 的自定义函数：

1. script_score：使用自定义的脚本来完全控制分值计算逻辑。如果需要预定义函数之外的功能，可以根据需要通过脚本进行实现
2. weight：对每份文档使用一个简单的提升，且该提升不会被归约。当 weight 为 2 时，结果为 2 * _score
3. random_score：使用一致性随机分值计算来对每个用户采用不同的结果排序方式，对相同用户仍然使用相同的排序方式。
4. field_value_factor：使用文档中某个字段的值来改变_score，比如将受欢迎程度或者投票数量考虑在内
5. 衰减函数(decay function)：linear，exp，gauss





### 基于文本查询

基于文本查询，是极为常用的是对文本进行搜索，即==全文搜索==

![image-20230408204816731](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304082048447.png)





ES的DSL中Full Text Query内容是非常多的，主要分为3大类

- Match类型
  - match查询的本质：是对多个term的组合查询
  - 但是，它并不是简单的term查询，是基于term查询构建的更高级查询
  - 它支持全文搜索、模糊搜索、逻辑控制，适用于==大多数全文检索场景==
- query string类型
- intervals类型



**基于全文搜索的特点**

- 索引和搜索时，都会进行分词，查询字符串先传递到一个合适的分词器，然后生成一个供查询的词项列表
- 查询时
  - 先对输入的查询进行分词
  - 然后每个词逐项进行底层查询，最终将结果进行合并
  - 为每个文档生成一个算分，分数高的排在前面



#### Match查询

##### Match单个词

数据如下：

~~~sh
PUT /test-dsl-match
{ "settings": { "number_of_shards": 1 }} 

POST /test-dsl-match/_bulk
{ "index": { "_id ": 1 }}
{ "title": "The quick brown fox" }
{ "index": { "_id ": 2 }}
{ "title": "The quick brown fox jumps over the lazy dog" }
{ "index": { "_id ": 3 }}
{ "title": "The quick brown fox jumps over the quick dog" }
{ "index": { "_id ": 4 }}
{ "title": "Brown fox brown dog" }
~~~

Match 单个词

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



match 类型的单个词查询步骤：

1. 检查查询字段类型
2. 分析查询字符串
   - 将待查询字符串(例如QUICK!) 传入分析器中
   - 分析器给出输出项的结果是单个项 quick。因为只有一个单词项，所以 match 查询执行的是单个底层 term 查询
3. 查找匹配文档
   - 用 term 查询在倒排索引中查找 quick 然后获取一组包含该项的文档，本例的结果是文档：1、2 和 3 
4. 为每个文档评分
   - 用 term 查询计算每个文档相关度评分 _score ，这是一种将词频（term frequency）和反向文档频率（即词 quick 在所有文档的 title 字段中出现的频率），以及字段的长度（即字段越短相关度越高）相结合的计算方式





##### Match 多个词

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



match 多个词的本质

它在内部实际上先执行两次 term 查询，然后 **将两次查询的结果合并作为最终结果输出**。为了做到这点，它将两个 term 查询合并到了一个 bool 查询中，下面语句与Match多个词的查询结果是等同的

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

should：任意一个满足

match 有一个 operator 参数，默认是 or，所以对应的是 should

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



##### match的匹配精度

如果用户给定三个词：A、B、C，但是想要查找至少包含其中2个的文档，应该如何处理？

`minimun_shouled_match`最小匹配 参数，可以指定必须匹配的词项数，用来表示一个文档是否相关



`minimun_shouled_match`参数格式

1. 整数：指定必须匹配的should子句数量
2. 百分比：指定必须匹配的should子句百分比
3. 组合：整数与百分比组合使用



例如：minimun_should_match："75%"

- 当给定百分比时，其minimun_shouled_match会做合适的事情，以满足我们的需求
- 此时：A、B、C3个词语*0.75=2.25，向下取值的最小词语数量为：2
- 所以至少匹配2个词语的文档才会被查询返回



##### match_phrase

- 连续的term查询：Abc cde ff
  - 文档必须连续包含：Abc cde ff xx xx才会匹配

##### match_phrase_prefix

- 有序的，前缀查询法，匹配的是最后一个词的前缀
- Abc cde f(也能查询查上述文档)
- Abc cde bc f(不能查询到文档)







#### query_string 类型

此查询使用语法根据运算符（and or）来解析和拆分提供的查询字符串，然后，查询在返回匹配文档之前，独立分析每个拆分的文本

可以使用 `query_string` 查询来创建包含通配符、跨多个字段的搜索等复杂搜索。 

虽然用途广泛，但==查询很严格==，如果查询字符串包含任何无效语法，则会返回错误



#### Intervals 类型

> Intervals：时间间隔，本质上是将多个规则按照顺序匹配







### Term查询

> 一种极为常用的是 **对词项进行搜索**，官方称为 term level 查询
>
> (基于单词查询)



常见的Term-level查询类型

- term查询：精确匹配某个字段的值
- terms：匹配某个字段的多个值
- exists：检查某个字段是否在文档中
- prefix查询：匹配某个字段中的指定前缀开头的值
- wildcard：支持通配符的匹配
- regexp：支持正则表达式的匹配
- range：匹配某个字段的范围值
- fuzzy：模糊匹配
- 等

> 注意Term-level查询，并不是都是以为term关键字开始的，例如：prefix、exists等



**Term level查询特点**

- ES 中，Term 查询，对输入的搜索词 ==不做分词==。
  - 会将输入作为一个整体作为搜索条件，在倒排索引中查找精确的词项，并且使用相关度算分公式，给每个包含该词项的文档进行相关性算分后返回。例如："Apple Store" 的匹配
- 可以通过 `Constanc Score` 将一个查询转换为：==Filter, 避免算分==，并利用缓存，提供性能
- 想要精确查询：增加子字段 keyword，进行严格匹配



**与全文匹配的区别**

全文搜索

- 会对查询字符串进行分词
- 基于相关性评分返回结果
- 适用于`Text`类型的字段

Term-levle

- 查询不会对查询字符串进行分词
- 基于精确匹配返回结果
- 适用于`keyword`类型的字段



![image-20220620155926112](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201559600.png)

单个分词匹配 term

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



多个分词匹配 terms

按照单个分词匹配，它们之间是 or 关系

~~~sh
GET /test-dsl-term-level/_search
{
  "query": {
    "terms": {
      "programming_languages": ["php", "c++"]
    }
  }
}
~~~













## 聚合查询 Aggregation

Aggregation，SQL 中叫做 group by，ES 中叫 Aggregation ，即聚合运算

- ES 除了提供搜索功能外，提供了==针对 ES 数据统计分析功能==，就是：Aggregation
  - 实时性高
  - Hadoop(T+1)
- 通过聚合，可以得到一个数据的概念，==聚合是分析和总结全套的数据，而不是寻找单个文档==
- 高性能，只需要一条语句，就可以从 ES 得到分析结果



Kibana 的可视化报表中，很多功能都是 ES 的聚合功能实现的





ES 提供了几大类聚合方式

1. Bucket Aggretation: 桶聚合
   - 一些列满足特定条件的文档的集合
2. Metric Aggretation：指标聚合
   - 一些数学运算，可以对文档字段进行统计分析
3. Pipeline Aggretation：管道聚合
   - 对其他的聚合结果进行二次聚合
4. Matrix Aggretation：矩阵聚合
   - 支持对多个字段的操作，并提供一个结果矩阵



### Bucket & Metric

- Bucket 桶的概念类似于 SQL 中的分组，

- Metric 指标类似于 SQL 中的统计函数 count、sum、max 等

1. 桶 Buckets：满足特定条件的文档的集合
2. 指标 Metrics：对桶内的文档进行统计计算



==指标聚合与桶聚合大多数情况组合在一起使用==，桶聚合本质上是一种特殊的指标聚合，它的聚合指标就是数据的条数(count)



官网给出了==好几十种桶聚合==，但是肯定是不能一个一个去看的，所以要站在设计的角度上来分类理解，主要有以下三类：

1. 对流程的控制：多个管道Aggregation嵌套聚合
2. 特殊字段或文档关系的聚合支持
   - IP类型
   - Number类型
   - 地理位置
   - 文档关系
   - 日期
   - 等
3. 对特殊功能的聚合支持
   - 柱状图
   - 矩阵
   - 样本
   - 等等

![image-20220620163557154](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201635557.png)





#### 多个聚合

~~~sh
GET /test-agg-cars/_search
{
    "size" : 0,
    "aggs" : { 
    // agg 的名字
        "popular_colors" : { 
        //terms 桶的类型
            "terms" : { 
              "field" : "color.keyword"
            }
        },
        //agg 的名字
        "make_by" : { 
            "terms" : { 
              "field" : "make.keyword"
            }
        }
    }
}
~~~

![image-20220620165554791](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201655354.png)



#### 聚合的嵌套

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



#### 动态脚本的聚合

ES 支持一些基于脚本(生成运行时的字段)的复杂的动态聚合







### Metrics 聚合

> 指标聚合 Metrics Aggregation，类似于 sql 中的 avg()、count()、min()、max()

适用时候，查看文档即可



分类

1. 单值分析

   - avg、max、min、sum、value_count等
   - cardinality基数（去重）、weighted_avg等

2. 多值分析

   - stats型（stats统计）

   - 百分数型

   - 地理位置型

   - Top型

     



### Pipline聚合

> 管道聚合：让上一步的聚合结果成为下一个聚合的输入

管道机制的场景场景

1. Tomcat 管道机制
2. 责任链模式
   - 管道机制在设计模式上属于责任链模式
3. FilterChain
   - 常见的责任链模式是FilterChain
4. ElasticSearch 的管道机制
   - 让上一步的聚合结果，成为下一个聚合的输入



**Pipline聚合分类**

1. 第一个维度：管道聚合有很多不同 **类型**，每种类型都与其他聚合计算不同的信息，可以将这些类型分为两类

   - 父级：父级聚合的输出提供了一组管道聚合，它可以计算新的存储桶或新的聚合以添加到现有存储桶中
   - 兄弟：同级聚合的输出提供的管道聚合，并且能够计算与该同级聚合处于同一级别的新聚合

2. 第二个维度：根据功能设计的意图

   - 前置聚合可能是 Bucket 聚合，后置的可能是基于 Metric 聚合，那么它就可以成为一类管道

   - 进而引出了：`xxx bucket`
     - Bucket 聚合-> metric 聚合：bucket 聚合的结果，成为下一步 metric 聚合的输入

     - Average bucket

     - Min/Max/Sum bucket

     - Stats bucket

     - Extended stats bucket






### 聚合分析的原理及精准度问题



分布式系统的近似统计算法

![image-20250108143145040](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108143146652.png)



![image-20250108143354007](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108143355382.png)



![image-20250108143427699](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108143429289.png)











## Mapping

> GET /index/_mapping 查看 Mapping 关系

Mapping是ES中，==定义索引中字段类型和相关属性的核心机制==

- Mapping 类似关系型数据库中的 schema 字段定义

- 用于定义索引文档中每个字段的名称、数据类型、格式、分词方式等等

- 通过Mapping，ES可以高效地存储和检索数据

  



Mapping 会把 Json 文档映射成 Luncene 所需的扁平格式

一个 Mapping 属于一个索引的 Type

- 每个文档属于一个 Type
- 一个 Type 有一个 Mapping 定义
- ES7.0 开始，不需要再 Maping 中指定 type 信息了，只有一个 `_doc类型`



**字段属性**

每个字段可以设置多种属性

- index
  - 是否所有该字段
- analyzer
  - 为字段指定分词器，未指定则使用默认的
- format
  - 定义日期或数值的格式
- store
  - 是否单独存储字段值（默认不存储）
    - 这个存储是独立于`_source`的存储
- fields（子字段）
  - 多字段设置，==允许一个字段以多种方式索引==
- 等等



**字段的数据类型**

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



**Dynamic Mapping**

- 在写入文档的时候，如果这个文档不存在，会自动创建索引
- Dynamic mapping 机制，使得我们无需手动定义 Mapping
- ES 会自动根据文档信息，推算出字段的类型
- 但是，有时候会推算不对
  - 当类型设置不对时，会导致一些功能无法正常运行





**更改 Mapping 的字段类型**

> Mapping一旦创建，就不能直接修改已有字段的类型，但可以添加新字段

- 新增字段

  - Dynamic 为 Ture 时，一旦有新字段的文档写入，mapping 也会同时更新
  - Dynamic 为 False 时，Mapping 不会被更新，新增的字段也无法被索引，但是信息会出现在 `_source` 中
  - Dynamic 为 Strict 时，文档写入失败

- 已存在的字段

  - 一旦已经有数据写入，就不支持修改字段定义
    - Lucene（英：鲁森~）实现的倒排索引，一旦生成后，就不允许修改
    - 如果想要改变字段类型，必须使用 ReIndex Api, 重建索引

  
  

![image-20250105231108572](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250105231111069.png)







如何写？

- 参考 API 文档，纯手写
- 为了减少工作量、减少出错概率，可以参考以下步骤
  - 创建一个临时的 Index，写入一些样本数据
  - 通过访问 Mapping Api 获得临时文档的动态 Mapping 的定义
  - 修改这个定义，配置我们自己需要的索引定义
  - 删除临时索引



控制当前字段是否被索引

> index 属性，控制当前字段是否能被索引，默认 True
>
> 当不需要索引某些字段时，这个字段就不会被搜索到了，同时也会节省很大磁盘开销

Index Options: 索引配置级别

- docs: 记录 doc id
- freqs: 记录 doc id、term frequnencies
- positions: 记录 doc id、term frequnencies、term position
- offsets: 记录 doc id、term frequnencies、term position、charachter offsets

Text 类型默认记录 positions，其他类型默认 docs

记录内容越多，占用存储空间越大



Null_Value

> Lucene 是不能存储 null 值的，所以认为存在 null 值的域为空域

- 需要对 Null 值实现搜索
- 只有 keyword 类型支持设定 Null_value





## 模板

> Index Template，是一种用于自动为新索引预定义设置的机制

通过模板，可以统一管理索引的Mapping、Settings等配置，避免每次创建索引时重复定义



帮助我们设定 Mappings 和 Settings，并按照一定的规则，==自动匹配到新创建的索引上==
- 模板仅在一个索引被新创建时，才会产生作用，修改模板不会影响已经创建的索引
- 可以设定多个索引模板，这些设置会被 "merge" 在一起
- 可以指定 "order" 的数值，控制 "merging" 的过程



### 模板类型

1. 组件模板
2. 索引模板
   - 可以包含组件模板的集合，也可以直接指定设置，映射和别名

二者可以结合使用，以更灵活地管理索引的配置



### 组件模板

- 是一种可重用的配置块，用于配置mapping、settings、aliases
- ==它们不会直接应用于一组索引==
- 它可以被多个索引模板引用，从而实现配置的复用



**核心组件**

1. template：定义部分索引配置（settings、mappings等）
2. version：定义模板的版本



例如

1. 创建一个settings的组件模板
2. 创建一个mappings的组件模板
3. 创建一个索引模板
   - composed_of参数，引用这2个组件模板

**模板合并规则**

1. settings：后面的配置会覆盖前面的配置
2. mappings：字段会合并，冲突字段以后面的配置为准
3. aliases：别名会合并



### 索引模板核心组件

1. index_patterns

   - 定义模板的匹配规则
   - 支持通配符（*）和正则表达式
   - 例如：log-*匹配所有log-开始的索引

2. template

   - 定义模板的具体配置信息
   - 配置settings、mappings、aliases等

3. priority

   - 定义模板的优先级
   - 当多个模板匹配同一个索引时，优先级高的模板回覆盖优先级低的模板

   





### 模板索引的应用案例

1. 日志索引模板
2. 时间序列索引模板



### ES内置索引模板

ES 具有内置索引模板，每个索引模板的优先级为 100，适用于以下索引模式

1. logs-* -*
2. metrics-* -*
3. synthetics-* -*

在涉及内建索引模板时，要避免索引模式冲突















## Query&Filter 与多字符串多字段查询

ES 中，有 Query 和 Filter 两种不同的 Context

- Query Context：会进行相关性算分
- Filter Context：不需要算分，可以利用 Cache，获得更好的性能



符合查询 bool 查询

- 一个 bool 查询，是一个或多个查询子句的组合
  - 总共包括 4 种类型子句，2 种会影响算分，2 种不会
- 相关性并不只是全文检索的专利，也适用于 bool 查询
  - 匹配的子句越多，相关性评分越高
  - 若多条查询子句被合并为一条复合查询语句，则每个查询子句计算出的评分会被合并到总的相关性评分中

| must     | 必须匹配，贡献算分                    |
| -------- | ------------------------------------- |
| should   | 选择性匹配，贡献算分                  |
| must_not | Filter Context 查询子句，必须不能匹配 |
| filter   | Filter context 必须匹配，不贡献算分   |

每个字段结合 boost, 可以影响算分的权重



bool 算分过程

- 查询 should 语句的中的 2 个查询
- 加和 2 个查询的评分
- 乘以匹配语句的总数
- 除以所有语句的总数



## 单字符串多字段查询



Disjunction Max Query 复合查询

- 将任何与任一查询匹配的文档作为结果返回
- 采用字段上最匹配的评分，作为最终评分返回（最高那个）
- `dis_max` 属性
  - 结合 `tie_breaker`（0-1 的浮点数）参数调整评分的规则



使用场景

- 最佳字段：best fields
  - 当字段之间相互竞争，又相互关联。评分来自最匹配字段
- 多数字段: most fields
  - 主字段抽取词干，加入同义词，已匹配更多的文档
  - 相同的文本，加入子字段，以提供更加精确的匹配
  - 其他字段作为匹配文档提高相关度的信息，匹配字段越多则越好
- 混合字段: Cross Fields
  - 需要在多个字段中确定信息，单个字段只能作为整体的一部分
  - 希望在任何这些列出的字段中找到尽可能多的词



Multi Match

~~~json
post blogs/_search
{
	"query":{
        "multi_match":{
            "type": "best_fields",
            "query": "pets",
            //在哪些字段查询
            "fields": ["title", "body"]，
            "tie_breaker": 0.2,
            "minimum_should_match": "20%"
        }
    }
}
~~~



跨字段搜索

- 无法使用 Operator
- 可以使用 copy_to 解决，但是需要额外的存储空间
- type 为：cross_fields 跨字段搜索解决
  - 可以使用 operator 操作符



~~~json
post blogs/_search
{
	"query":{
        "multi_match":{
            "type": "cross_fields",
            "query": "pets",
            //在哪些字段查询
            "fields": ["title", "body"]，
            "operator": "and"
        }
    }
}
~~~



## 结构化搜索

> ES结构化搜索是值：对具有明确结构的数据进行精确匹配的搜索

与全文搜索不同，结构化搜索通常用于查询：==非文本字段==（例如：日期、数字、枚举等）

并且==结构化搜索不涉及分词与相关性评分==





### 结构化数据

- 日期、时间、数字、布尔这类的数据结构化
  - 有精确的格式，我们可对这些格式进行逻辑操作
  - 包括比较数字或事件范围，或判断 2 个值的大小
- 结构化的文本可以做精确匹配，或者部分匹配
  - ==Term 查询==、Prefix 前缀查询
  - Term 查询针对多值（例如 数组)是采用包含而不是相等
  - 解决精确查询：增加一个 `gener_count` 字段进行计数，结合 bool 查询完成
- 结构化结果只有：是、否两个值
  - 根据场景需要，可以决定结构化搜索是否需要打分
  - `constant_score` 属性，跳过算分



### 结构化搜索

> 结构化查询并不关心文件的相关度或评分，我们得到的结果总是：要么存在于集合中，要么不存在

1. 精确值查找

   - 使用精确值查找时，会使用过滤器，过滤器非常重要，它的执行速度很快，因为不会去计算相关度
   - 而且过滤器很容易被缓冲
   - **constant_score**  + filter +term

2. 组合过滤器

   - 可能存在过滤多个值或字段的情况
   - 需要采用 bool 过滤器，它可以接受多个其他过滤器作为参数，并将这些过滤器结合成各式各样的布尔逻辑组合
   - bool+must、must_not、should 等

3. 查找多个精确值

   - constant_score + terms + 值数组

4. 范围

5. null

   

结构化搜索的常见查询类型

- term、terms、range、existes、prefiex、wildcard、regexp，总的来说与Term-level类似





## 相关性和相关性算分

> 默认情况下，返回的结果是按照 **相关性** 进行排序的，即最相关的文档排在最前



> [!WARNING]
>
> 相关性测试（调试）总是放在最后（相关性测试很消耗资源，生产环境不建议开启的）



### 相关性

相关性Relevance：指搜索结果与用户查询的匹配程度

Relevance Score：用于量化这种匹配程度的数值

- 搜索的相关性算分，==描述了一个文档和查询语句匹配的程度==
- ES 会对每个匹配查询条件的结果进行算分：`_score` 表示
- 每个文档都有相关性评分，用一个正浮点数字段 `_score` 来表示 。 评分越高，相关性越高
- ==打分的本质是：排序==，需要把最符合用户需求的文档排在前面
  - 使用 filter 来过滤时，不存在评分，只会筛选出符合条件的文档
  - ES5 以前采用 TF/IDF 算法、ES5.0 之后，采用 BM25



**评分的计算方式取决于查询类型**，不同的查询语句用于不同的目的



==ES中，相关性基于以下因素：词频TF、逆文档频率IDF、字段长度==



词频 TF

> 查询词在文档中出现的次数越多，相关性越高

- Term Frequency：检索词在一篇文档中出现的频率
  - 检索词出现的次数/文档的总字数
- 度量一条查询和结果文档相关性的简单算法
  - 简单的将搜索中的每一个词的 TF 进行相加
- Stop word（停用词）
  - 某些词(例如：“的”）在文档中出现了很多次，但是，对贡献相关度几乎没有用处，不应该考虑它们的 TF



逆文档频率 IDF

> 查询词在整个索引中出现的频率越低，相关性越高

- DF：检索词在文档中出现的频率
- IDF：Inverse Document Frequency
  - 简单说 = log(全部文档数/检索词出现过的文档总数)
- TF/IDF 算法：本质上就是将 TF 求和，变成了加权求和
  - TF(搜索词 1)*IDF(搜索词 1)+2+3..
  - TF-IDF 算法被公认为是 ==信息检索领域== 最重要的发明
  - 现代的搜索引擎大多都基于 TF-IDF 基础的

![image-20250106161619647](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250106161621048.png)



BM25

和 TF-IDF 相比，当 TF 无限增加时，BM25 算分会趋于一个数值





字段长度

> 文档中字段的长度越短，相关性越高（因为更短的字段通常更集中地描述了内容）



### 评分标准

> 调试一条复杂的查询语句时，想要理解_score 如何计算的是非常困难的

Explain API 查看 TF-IDF

- ES 在每个查询语句中都有一个 explain 参数，将 `explain` 设为 `true` 就可以得到更详细的信息
- explain 可以让返回结果添加一个==_score 评分的得来依据==
- explain 的输出结果代价是相当昂贵的，只能用作调试工具，不能用于生产环境

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
"_explanation ": { 
   "description": " weight(tweet: honeymoon in 0)
                  [PerFieldSimilarity], result of:",
   "value":       0.076713204,
   "details": [
      {
         "description": "fieldWeight in 0, product of:",
         "value":       0.076713204,
         "details": [
            {  
               "description": "tf(freq = 1.0), with freq of:",
               "value":       1,
               "details": [
                  {
                     "description": "termFreq = 1.0",
                     "value":       1
                  }
               ]
            },
            { 
               "description": "idf(docFreq = 1, maxDocs = 1)",
               "value":       0.30685282
            },
            { 
               "description": "fieldNorm(doc = 0)",
               "value":        0.25,
            }
         ]
      }
   ]
}
~~~



### 影响相关性评分的因素

- 查询类型
  - 不同的查询类型会影响评分
- 字段权重
  - 可以通过`boost`参数调整字段的权重
  - Boosting Relevance
    - Boosting 是控制相关度的一种手段
      - 索引、字段、查询子条件，上面都可以设置 boost 值
    - 参数 boost 的含义
      - boost > 1，打分的相关度相对性提升
      - 0 < boost < 1, 打分的权重相对性降低
      - boost < 0, 贡献负分
- 自定义评分：function_score函数
- 文档长度
  - 较短的文档，通常得分更高
- 词频和逆文档频率
  - 词频越高，逆文档频率越低，得分越高
- 查询逻辑
  - and、or会影响评分







## Search Template

> 主要是解耦程序、解耦搜索 DSL

大的团队合作中，开发工程师只负责程序逻辑部分，还有一部分负责 ES 的性能及效果

- 开发人员/搜索工程师/性能工程师



开发初期，虽然可以明确查询参数，但是往往不能最终定义查询的 DSL 的具体结构

- 通过 Search Template 定义一个 Contract





## Index Alias

> 索引别名，可以实现零停机运维

Index alias：是一个==指向一个或多个索引的逻辑名称==

它允许通过别名来访问多个索引，或者在不影响客户端的情况下切换底层索引



例如：my_alias索引名指向2个索引

~~~json
{
    "actions":[
        {
            "add":{
                "index":"index1",
                "alias":"my_alias"
            }
        },
        {
            "add":{
                "index":"index2",
                "alias":"my_alias"
            }
        }
    ]
}
~~~



应用场景

1. 无缝切换索引
   - 将my_alias从`index_v1`切换到`index_v2`,客户端无需修改代码
2. 基于时间的索引
   - 对应日志或时间序列的数据，通常会按天、周、月等创建索引
   - 通过别名，可以方便的查询特定时间范围的数据
   - 例如：每天创建一个索引，别名指向新的索引即可
3. 多租户架构
   - 为每个租户创建一个索引，使用别名指向租户的索引
4. 读写分离
   - 为索引创建2个别名，一个读、一个写
   - 写入时使用：write_alias
   - 查询时使用：read_alias



## Suggester 搜索建议

现代的搜索引擎，一般会提供 Suggest as you type 的功能

Suggester帮助用户在搜索的过程中，进行 ==自动补全或者纠错==。通过协助用户输入更精准的关键词，提高后续搜索阶段文档匹配的程度



ES 使用 Suggester API 实现，原理如下：

- 将输入的文本分解为 Token，然后在索引的字典里查找相似的 Term 并返回给用户
- 根据不同的使用场景，ES 设计了 4 种类别的 Suggesters
  - Term & Phrase Suggester
  - Complete & Context Suggester



### Term Suggester

**用途：**基于编辑距离==提供拼写纠正建议==

**原理：**根据用户输入的词条，从索引中查找相似的词条

**适用场景：**拼写纠错、模糊匹配



例如：

- 用户输入的 text中："helo" 是一个错误的拼写
- ES会到指定的字段field：“title" 上搜索，当无法搜索到结果时，返回建议的词

~~~json
{
    "suggest":{
        "my_sugg":{
            "text":"helo",
            "term":{
                "field":"title"
            }
        }
    }
}
~~~



返回结果：

- > 每个建议都包含了一个算分，相似性是通过 Levenshtein Edit Distance 算法实现的，用于衡量2个字符串之间的相似度

- 核心思想：一个词改动多少字符就可以和另一个词一致。提供了许多可选参数来控制相似性的模糊程度

~~~json
{
  "suggest": {
    "my_suggestion": [
      {
        "text": "helo",
        "offset": 0,
        "length": 4,
        "options": [
          {
            "text": "hello",
            "score": 0.8,
            "freq": 10
          }
        ]
      }
    ]
  }
}
~~~



Suggestion 的几种 Mode

- Missing： 如果索输入词在索引中不存在时，提供建议，==默认模式==
- Popular：推荐出现频率更加高的词
- Always：无论是否存在，都提供建议
- Prefix：基于前缀匹配返回建议词
- 等等



### Phrase Suggester

Phrase(短语、词组、惯用语)

**用途：**提供==短语级别==的拼写纠正建议

**原理：**分析用户输入的短语，推荐更合适的完整短语

**适用场景：**短语拼写纠错





- 在 Term Suggester 上增加了一些额外的逻辑
- 支持更多的一些参数
  - Suggest mode: missing、popular、always
  - Max errors: 最多可以拼错的 terms 数
  - Confidence：限制返回结果数，默认 1



### Completion Suggester

> 提供了==自动补全==（Auto Complete)的功能，用户每输入一个字符，就需要即时发送一个查询请求到后端查找匹配项

**原理：**使用内存中的数据结构（FST,Finite State Transducer)快速匹配前缀

**适用场景：**搜索框自动补全



- 对性能比较苛刻
  - ES 采用了不同的数据结构，并发采用倒排索引完成
  - 采用了将 Analyze 的数据编码成 FST 和索引一起存放。FST 会被 ES 整个加载进内存，速度很快
- ==FST 只能用于前缀查找==



使用 Completion suggester 的步骤

1. 定义 Mapping，使用 completion type
2. 索引数据
3. 运行“suggest”查询，得到搜索建议





### Context Suggester

> 实现上下文感知的搜索，在自动补全的基础上，支持上下文过滤

**原理：**结合上下文信息，提供更精准的建议

**适用场景：**带上下文过滤的自动补全



可以定义 2 种类型的 Context 上下文

- Categroy：任意种类分类的字符串
- Geo：地理位置信息



实现 Context Suggester 的具体步骤

- 定义 Mapping
- 索引数据，并为每个文档加入 Context 信息
- 结合 Context 进行 Suggestion 查询



### 精准度和召回率

- 精准度：返回的建议中，有多少是用户真正想要的
  - 精准度=相关建议数量/总的建议数量
  - Completion > Phrase > Term

- 召回率：用户真正想要的建议中，有多少被返回
  - 召回率Recall = 相关建议数量/总的相关建议数量
  - Term > Phrase > Completion

- 性能：Completion > Phrase > Term



## 配置跨集群搜索

1. 配置集群
2. 分别在不同机器上写入数据
3. 搜索数据
   - 指定集群名字.index 来搜索





## 分布式查询及其相关性算分

分布式搜索的运行机制

- ES 的搜索分为 2 个阶段
  - 第一个阶段：Query
  - 第二个阶段：Fetch



### Query 阶段

![image-20250107213824815](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107213826803.png)

例子：3 个主分片、3 个副本分片，同时`my_index`分布在这3个分片上

1. 客户端向ES集群发送请求
2. 协调节点的身份角色接收请求
   - 请求首先到达协调节点Coordinating Node
3. 分发查询
   - 协调节点将查询分发到索引的所有相关分片（Primary或Replica Shard)
   - 通常会选择负载较低的分片（主分片或者副本分片任意选择一个）
     - 假设（可能）选择P0、P1、P2
4. 分片执行查询
   - 每个分片在本地执行查询，找到匹配的文档，并计算相关性算法
   - 每个分片会返回 from+size 个排序后的文档 id 和排序值给 Coordinating node
5. 返回结果给协调节点







### Fetch 阶段

- Coordinating node 将从 Query 阶段，每个分片获取的排序后的文档 id 列表重新排序，选取 from+size 个文档 id
- 以 `multi get` 请求的方式，到相应的分片获取详细的文档信息





### Query  Then Fetch 潜在的问题

1. 深度分页及性能问题

   - 每个分片上需要查询的文档个数：X = from+size

   - 最终协调者节点需要处理：分片数*X

   - 深度分页问题

2. 相关性算分不一致问题

   - 查询阶段，每个分片都基于自己的分片上的数据进行相关性算分

   - 这会导致打分偏离的情况，特别是数据量少的时

   - 相关性算分在分片之间是相互独立的，当文档总数很少的情况下，如果主分片 > 1, 主分片越多，相关性算分越不准
     - 数据量不大时，可以将主分片数设置为 1
     - 使用 DFS Query Then Fetch
       - 搜索的 URL 中，指定参数 `dfs_query_then_fetch`
       - 查询阶段，每个分片把所有分片的词频和逆文档频率进行搜集，然后在取回阶段，完整的进行一次相关性算分
       - 但是，耗费更多的 cpu 和内存，性能低下，==一般不建议使用==



## 排序及 Doc values & Fielddata

- ES 默认采用相关性算分对结果进行降序排序
- 支持对一个、多个字段排序
- 可通过设定 `sorting` 参数，自定义排序
- 如果不指定 `_score`, 那么算分为 Null



Doc Values和Fielddata是支持排序和聚合的两种数据结构

### ES排序的实现方式

> 排序的实现方式取决于字段的类型和存储结构（Doc Values、Fielddata

**排序基本原理**

- ES执行排序时，需要获取每个文档中指定字段的值
- 这些值加载到内存中，然后根据指定的排序规则进行排序
- 排序的效率取决于字段的类型及存储方式(Doc Values、Fielddata)



**排序的字段类型**

1. 数值类型
   - integer、float、日期等等，默认使用：Doc Values，排序效率高
2. 字符串类型
   - 如果是`keyword`类型，默认使用Doc Values
   - 如果是`text`类型，默认不支持排序（因为会被分词），除非启用fielddata属性来支持排序



### Doc Values

它以列式存储的方式，将字段值存储在磁盘上，适合处理大量数据

**工作原理**

- 列式存储
  - 将每个字段的值按列存储
  - 例如：对于price字段，所有文档的price值会存储在一起
- 磁盘存储
  - Doc Values的数据在磁盘上，只要需要时，才会加载到内存中
  - 减少了内存的占用，适合处理大规模数据

对于数值、日期、keyword字段，默认启用Doc Values排序



**缺点**

- 不支持分词文本字段
  - 对于`text`类型，默认不会启用doc_values，除非字段是`keyword`类型
- 磁盘I/O
  - 频繁排序和聚合操作可能会导致磁盘I/O增加



**启用禁用Doc Values**

- mapping时，可以指定字段的`doc_values`是否启用
- 关闭 Doc Values
  - 默认开启，可通过 Mapping 设置关闭
    - 增加索引的速度/减少磁盘占用空间
  - 关闭后，如果要重新打开，需要重建索引
  - 什么时候关闭？
    - ==很明确不需要做排序及聚合分析==



### Fielddata

- Fielddata是ES中，对于分词文本字段（text）进行排序和聚合的数据结构
- 与doc values不同，fielddata是存储在内存中
- 默认fielddata是禁用的，因为对内存消耗大



**工作原理**

- 内存存储
  - 将字段的值加载到JVM内存中
  - 对于分词字段，Fielddata会存储分词后的词项及其对应的文档
- 动态加载
  - 第一次需要排序或聚合时动态加载的
  - 加载后，数据会一直保留在内存中，直到段（segment)被合并或删除



在设计mapping时，要合理选择字段类型和Doc values或Fielddata

|          | Doc Values                   | Field data                                    |
| -------- | ---------------------------- | --------------------------------------------- |
| 何时创建 | 索引时，和倒排索引以前创建   | 搜索时候动态创建                              |
| 存储位置 | 磁盘文件                     | JVM Heap                                      |
| 适用字段 | 数值、日期、keyword          | 分词文本字段（text)                           |
| 优点     | 避免占用大量内存             | 索引速度快，不要占用额外的磁盘空间            |
| 缺点     | 索引速度慢，占用额外磁盘空间 | 文档过多时，动态创建开销大，占用过多 JVM Heap |
| 默认值   | ES 2.x 后                    | ES1.x 及以前                                  |





## 分页及遍历

ES 默认根据相关性算分返回前 10 条数据



### 基本分页

- from+size
- from：指定从第几条数据开始返回，默认0
- Size：期望获取返回文档的总数



**优点**

- 简单易用，适合小规模分页



**缺点**

- 深度分页性能问题
- 限制：ES默认from+size不能超过1w条，可调整，但是不建议



### 游标分页

> search_after，是一种基于游标的分页方式，适合深度分页

- 原理
  - 通过指定上一页最后一条数据的排序值（sort字段），获取下一页的数据
- 使用条件
  - 必须指定一个唯一的排序字段（例如`_id`或时间戳）
  - 需要与`sort`参数配合使用



**游标分页优点**

- 适合深度分页，性能由于from+size
- 不受1w条限制



**缺点**

- 需要维护排序值和游标状态
- 不支持跳页，只能顺序分页





### 滚动查询scorll（遍历）

Scroll Api是用于遍历大量数据的机制，适合一次性获取大量数据（例如：导出数据）。是一个==对结果进行遍历的操作==



**原理**

- 创建一个快照，在指定时间内报错所有状态不变，通过游标逐批获取数据。当有新的数据写入后，无法被查到
- 每次查询后，输入上一次的 scorll Id



**使用条件**

- 需要维护`scroll_id`
- 适合离线任务或数据导出



**缺点**

- 占用资源多，滚动上下文消耗内存
- 不适合实时查询



### 不同的搜索类型和使用场景

- Regular
  - 需要实时获取顶部部分文档（平时普通的 10 条记录）
- 遍历
  - 使用scorll，需要全部文档，例如：导出全部数据
- 实时遍历
  - search_after：适合实时性较高的遍历场景，不需要维护滚动上下文
- 分页
  - 普通小规模：选用 From 和 Size
  - 如需要深度分页：选用 Search After（考虑局限性）



### ES 分布式系统中深度分页问题

![image-20250107231751829](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250107231753417.png)

- ES 天生就是分布式的。查询信息时，数据分布在多个分片上，多台机器上，ES 天生就需要满足排序的需要（按照相关性算分）

- 当查询：From = 990, Size = 10，需要查询的数据：from+size

  - 会在每个分片上先都获取 1000 个文档，返回给协调节点

  - 协调节点

    - 整合所有分片的结果
    - 再通过排序选择前 1000 个文档，返回最前面的10条

  - 页数越深，占用内存越多
  
    - 为了避免深度分片带来的内存开销
    - ==ES 有一个设定，默认限定到了 10000 个文档==
  
    

深度分页替代方案

1. search_after
2. scorll api



## ES 中并发读写操作



### ES 采用乐观并发控制

- ES 中的文档是不可变更的。如果要更新一个文档，会将文档标记为删除，同时增加一个全新的文档，同时新文档的 version 字段加 1
- 内部版本控制
  - ES 早期采用的是：version
  - ES 新版中采用的是：`if_seq_no`+`if_primary_term`
- 使用外部版本（使用其他数据库作为主要数据存储）
  - `version`+`version_type` = external













## 对象及嵌套对象（Nested)

==ES 不善于处理关联关系==

- ES 采用反范式化设计
  - 反范式的好处：读取速度更快、无需联表、无需行锁
- ES 通常采用以下四种方式处理关联
  - 对象类型
  - 嵌套对象（Nested Object)
  - 父子关联关系（Parent、Child)
  - 应用端关联：程序端处理



Nested Data Type

- ES 中的一种类型，==允许对象数组中的对象被独立索引==
- 在内部，Nested 类型的文档，每个对象会单独保存在一个 Lucene 文档中，在查询时做 Join 处理



使用场景：查询为主，子文档偶尔更新



对象和 Nested 对象的局限性

- 更新时，需要重新索引整个对象，包括根对象和嵌套对象



文档的父子级关系

> ES 提供了类型关系型数据库中 Join 的实现方式，使用 Join 数据类型实现，可以通过维护 Parent/Child 的关系，从而分离 2 个文档

- 父文档和子文档是 2 个独立文档
- 更新父文档无需重新索引子文档。子文档被添加，更新或删除也不会父文档和其他子文档



适用场景：子文档更新频繁



## Reindex

一般以下几种情况，我们需要重建索引

- 索引的 Mappings 发生变更：字段类型更改、分词器、字典更新等
- 索引的 Settings 发生变更：索引的主分片数发生改变
- 集群内，集群间需要做数据迁移
- 数据转换
- 版本升级
- 等等

Reindex是ES 提供的内置的API，用于将一个索引中的数据复制到另一个索引中



支持的功能

1. 从源索引读取数据
2. 将数据写入目标索引中
3. 支持数据过滤、转换、脚本处理



- Update By Query: 在原有索引上重建
- Reindex: 在其他索引上重建索引
  - 先建一个新的索引，在将原有的链接上去
  - 如果涉及跨集群 Reindex，需要修改配置文件



基本用法

- source：原索引
- dest：目标索引

~~~json
POST _reindex
{
  "source": {
    "index": "source_index",
    "slices":5 //提高并行化
  },
  "dest": {
    "index": "target_index",
    "refresh":false //禁止刷新
  }
}
~~~



高级用法

- 过滤数据：reindex过程中只要满足条件的数据
- 数据转换：可通过script脚本进行数据转换
- 批量大小控制：每个批次复制多少数据
- 跨级群Reindex：可以从远程集群复制数据到本地集群



### Reindex性能优化

1. 并行化Reindex：通过指定`slices`参数，将Reindex操作并行化，提高性能
2. 禁止刷新
   - Reindex过程中，禁止目标索引的刷新（refresh），减少资源消耗
3. 使用任务管理API
   - Reindex操作可能花费很长时间，可以使用任务管理API监控和管理Reindex任务

### Reindex注意事项

- 数据一致性
  - Reindex过程中，源索引的数据可能发生变化，导致数据不一致
  - 可通过版本控制或快照机制确保数据一致性问题
- 性能影响
  - Reindex操作可能占用大量资源，影响集群性能
  - 建议低峰期执行Reindex操作，==并使用并发化和批量大小控制优化性能==
- 索引设置
  - 目标索引的mapping和分片设置，应该与源索引一致，否则可能导致数据丢失或性能问题



## Ingest Pipline

ES提供的一种数据预处理管道工具，==允许在文档被索引之前，对数据进行转换、丰富和过滤==，从而减少后续查询和分析的复杂度



![image-20250108171637255](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108171638479.png)

ES5.0 之后，引入了一种新的节点类型 Ingest Node。==默认配置下，每个节点都是 Ingest Node==

- 具有预处理数据的能力，可拦截 Index 或 Bulk Api 的请求
- 对数据进行转换，并重新返回给 Index 或 Bulk APi

无需 Logstash，就可以进行数据的预处理

- 例如：为某个字段设置默认值、重命名某个字段的字段名、对字段值进行 split 操作等等
- 支持设置 Painless 脚本，对数据进行更复杂的加工





Ingest Pipeline由一系列的处理器（Processor）组成，每个processer可以对文档进行特定的操作

![image-20250108172115439](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108172116975.png)



一些内置的 Processors

- Split Processor
  - 例如：将给定字段值分成一个数组
- set processor
  - 设置字段的值

- Remove/Rename
  - 例：移除一个重命名字段
- Append
  - 例: 为商品增加一个标签
- Covert
  - 转换字段的数据类型
  - 例: 从字符串转换为 float
- Data/Json
  - 例：日期格式转换、字符串-Json 格式转换
- Date Index Name
  - 例：通过该处理器的文档，分配到指定时间格式的索引中
- 等等，官网去看



**Ingest Node VS Logstash**

Ingest Node 与 Logstash 有一些功能上的重合

![image-20250108173426137](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108173431282.png)



## painless script

- ES5.0 后引入，专门为 ES 设计的脚本语言，扩展了 Java 语法

- 6.0 开始，ES 只支持 Painless脚本语言。Groovy、JavaScript、Python 都不再支持

- Painless 语法与Java和Groovy类似，支持所有 Java 的数据类型及 Java API 子集

- Painless Script 特性

  - 高性能/安全

  - 支持显示类型或动态定义类型

    

painless script 作用

- 对文档字段进行加工处理
  - 例如：更新、删除字段，处理数据聚合操作
  - scrip field：对返回的字段提前进行计算
  - Function Score：对文档的算分进行处理
- 在 Ingest node Pipeline 中执行脚本
- Reindex API、Update By Query 中，对数据进行处理

![image-20250108174138029](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108174139353.png)

脚本缓存

- ES 中脚本的编译开销相对较大
- ES 会将脚本编译后缓存在 cache 中
  - Inline Script 和 Stored scripts 都会被缓存
  - 默认缓存 100 个脚本
- 缓存参数
  - script.cache.max_size: 设置最大缓存数
  - script.cache.expire：设置缓存超时
  - script.max_compilation_rate：默认 5 分钟最多 75 次编译





# ES 数据建模

功能需求+性能需求



建模时候，通常要考虑以下四点：

- 字段类型
- 是否需要搜索及分词
- 是否需要聚合及排序
- 是否需要额外的存储



## 字段类型 text vs keywork

- Text
  - 适用于全文本字段，文本会被分词
  - 默认不支持聚合分析及排序，需要设置 fielddata 为 true 来支持
- Keyword
  - 用于 id、枚举及不需要分词的文本
    - 例如：电话号码、email、邮编、性别等
  - 适用于：filter(精确匹配)，sorting、aggregations
- 设置多字段类型
  - 默认会为文本设置 text 类型，并设置一个 keyword 的子字段
  - 处理人类语言时，通过增加“英文”、“拼音”、和“标准”分词器，提供搜索结构

## 字段类型: 结构化数据

- 数值类型
  - 尽量选择贴近的类型，例如可以用 byte 就不要用 long
- 枚举类型
  - 设置为 keyword, 即使是数字的枚举，也应该设置为 keyword，获得更好的性能
- 其他
  - 布尔/日期/地理信息等



## 是否需要-检索

- 如果不需要检索、排序、聚合分析
  - Enable 设置为 false
- 如果不需要检索
  - Index 设置为 false
- 对需要检索的字段，可以通过配置，设定存储粒度
  - Index options/Norms：不需要归一化数据时，可以关闭





## 是否需要-聚合和排序

- 如果不需要检索、排序、聚合分析
  - Enable 设置为 false
- 如不需要排序或聚合分析功能
  - Doc_values/fielddata 设置为 fasle
- 更新频繁，聚合查询频繁的字段，设置为 keyword
  - 推荐将 eager_global_ordinals 设置为 True



## 是否需要-额外的存储

- 是否需要专门的存储当前字段的数据
  - Store 设置 True，可以存储该字段的原始内容
  - 一般结合 `_source` 的 Enable 为 false 时使用
- Disable _source: 节约测评，适合于指标型数据
  - 一般建议先考虑增加压缩比
  - source disable 后，无法做 reindex, 无法 update



## 建模建议

### 如何处理关联关系

- Object: 优先考虑反范式设计
- Nested Object: 当数据包含多值对象，同时有查询需求时
- Parent/Child: 关联文档更新频繁时

> [!WARNING]
>
> ==Kibana 目前不支持== nested 类型和 Parent/Child 类型
>
> 如果需要使用 Kiban 进行数据分析，在数据建模时，需要对嵌套对象和父子关联类型做出取舍



### 避免过多字段

- 一个文档中，避免大量的字段
  - 不易维护
  - Mapping 信息保存在 cluster state 中，数据量过大，对集群性能会有影响（Cluster state 信息需要同步所有的节点）
  - 删除或修改数据需要 reindex
- 默认最大字段数是 1000，可以配置限定最大字段数
- 什么原因导致文档中会有成百上千的字段？
  - Dynamic(生产中，尽量不要开)
    - true: 未知字段会被自动加入
    - false: 新字段不会被索引，但是，会保存在 `_source` 中
    - strict: 新字段不会被索引，文档写入也会失败
  - Strict
    - 可控制字段级别





### 避免正则查询

- 正则，通配符查询，前缀查询属于 Term 查询，但是性能不好
- 特别是将通配符放在开头，会导致性能的灾难





### 避免空值引起聚合不准







# ES 集群身份认证和用户鉴权

- ES 默认安装后，不提供任何形式的安全防护
- 错误的配置信息导致公网可以访问 ES 集群
  - 例如：elasticsearch.yml 中，将 server.host 配置为 0.0.0.0



## 数据安全性的基本需求

- 身份认证
  - 鉴定用户是否合法
- 用户鉴权
  - 哪个用户可以访问哪些索引
- 传输加密
- 日志审计



一些免费的方案

- 设置 Nginx 反向代理
- 安装免费的 Security 插件
  - Search Guard
  - ReadOnly Rest
- X-Pack 的 Basic 版



## 身份认证

认证体系的几种类型

- 提供用户名和密码
- 提供密钥或 Kerberos 票据



Realms: X-Pack 中的认证服务

- 内置 Realms(免费)
  - File/Native: 用户名密码保存在 ES
- 外部 Realms（收费）
  - LDAP/Active Directory/PKI/SAML/Kerberos





## 用户鉴权

ES 中权限包括：索引级别、字段级、集群级别的不同操作

RBAC: Role Based Access Contrl, 定义一个角色，并分配一组权限

通过将角色分配给用户，使得用户拥有这些权限



ES 内置的用户和角色

- elastic: 超级用户
- kibana
- logstsh_system
- beats_system
- apm_system
- Remote_monitoring_user



管理员可以在 kibana 里面配置用户来限制



上述是通过身份认证及用户鉴权，对 rest APi 的一个保护





## 集群内部安全通信

比如别人可以启动一个 es 节点，加入你的集群，存在风险



- 加密数据
  - 避免数据抓包，敏感信息泄露
- 验证身份
  - 避免 Impostor Node
  - Data/Cluster State



1. 为节点签发 CA 证书来解决
2. 配置节点间通讯



## 集群与外部系统的安全通信

浏览器-> Kibana-> ES <-> Logstash

这些组件之间通信都是需要 Https 的







# 集群部署方式



## 节点类型

不同角色的节点: 默认这些角色都承担

- Master eligible/Data Node/Ingest Node/Coordinating Node/Machine Learning Node 等

- Master eligible：有资格成为Master节点的节点
  - 其参与集群的管理和协调工作，但它们并不一定是当前Master节点

eligible(符合条件的、合格的)



开发环境：一个节点可承担多个角色

生产环境中：

- 根据数据量，写入和查询的吞吐量，选择合适的部署方式
- ==建议设置单一角色的节点==



节点参数配置，通过参数配置不同的角色

![image-20250108223402048](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108223403335.png)





![image-20250108223302841](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250108223304243.png)



单一角色职责分离的好处

- 不同节点负责处理不同的事情
- master eligible node: 负责集群状态的管理
  - 那么可以使用低配置的 CPU、RAM 和磁盘
- Data Node：负责存储数据及处理客户端的请求
  - 使用高配置的 Cpu、Ram、磁盘等
- Ingest Node：负责处理数据
  - 使用高配 CPU、中的 RAM，低配的磁盘





Coordinating Only Node

- 生产环境中，建议为一些大的集群配置 Coordinating Only Nodes
- 扮演 Load Balancers, 降低 Master 和 Data Nodes 的负载
- 负载搜索结构的 Gather/Reduce
- 有时候无法预知客户端会发送怎样的请求
  - 大量占用内存的结合操作，一个深度聚合可能引发 OOM



Delicate Master Node

- 从高并发、避免脑裂角度出发
  - 一般生产配 3 台
  - 一个集群只有一台活跃的主节点
    - 负责分片管理，索引创建，集群管理等操作
- 如果和 Data Node 或 Coordinating Node 节点混合部署
  - 数据节点相对有比较大的内存占用
  - Coordinating 节点有时候可能会有开销很高的查询, 导致 OOM
  - 这些都是可能影响 Master Node，导致集群不稳定



## 部署方式

基本部署：增加节点，水平扩展

![image-20250109132222520](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109132223827.png)





![image-20250109132257571](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109132259063.png)



![image-20250109132328416](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109132330248.png)



![image-20250109132407125](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109132408388.png)





![image-20250109132424712](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109132425730.png)



# Hot & Warm 架构



## Hot & Warm Architecture

- Hot Warm Architecture
  - 数据通常不会有 update 操作；==适用于 Time based 索引数据==，同时数据量比较大的场景
  - 引入 Warm 节点，低配置大容量的机器存放老数据，以降低部署成本
- 两类数据节点，不同的硬件配置
  - Hot 节点：索引不断有新文档写入。通常使用 SSD 固态硬盘
  - Warm 节点：索引不存在新数据的写入，同时也不存在大量的数据查询，通常采用 HDD 机械硬盘

通过将数据从Hot节点迁移到Warm节点，可以显著的降低存储和计算成本，同时保持对历史数据的访问能力



## Hot Nodes

-  用于数据写入，Indexing 对 CPU 和 IO 的要求高，所以需要更高配置的机器
- ![image-20250109133410440](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109133412442.png)



## Warm Nodes

- 用于保存只读的索引，比较旧的数据，通常使用大容量的磁盘，通常是 Spinning Disks(SSD)

![image-20250109133509269](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109133511025.png)



**旧的数据移动到 Warm 节点**

可以使用索引生命中期管理ILM、Curator工具等

~~~
index.routing.allocation.require.my_node_type: warm
~~~





# 分片设计及管理



## 单个分片

- ES7.0 开始，新建一个索引时，默认只要一个分片
  - 单个分片，查询算分，聚合不准的问题都可以避免
- 单个索引，单个分片的时候，集群无法实现水平扩展
  - 即使增加新的节点，也无法实现水平扩展



## 2 个分片

集群增加一个节点后，ES 会自动进行分片的移动，也叫：Shard Rebalancing



## 如何设计分片数？

一定要和 kafka 分片区分开（kafka 主题分片指的是节点），ES 分片是索引的物理存储单元，一个分片就是一个 Lucene 索引



- 当分片数 > 节点数
  - 一旦集群中有新的数据节点加入，分片就会自动进行分配
  - 分片在重新分配时，系统不会有 downtime(不会中断服务)
- 多分片的好处
  - 一个索引如果分布在不同的节点，多个节点可以并行执行
  - 查询可以并行执行
  - 数据写入可以分散到多个机器

案例

- 5 个不同的日志，每天创建一个日志索引。每个日志索引创建 10 个主分片
- 数据保留 180 天
- 那么总的分片数量为：5x10x180 = 9000 个分片





## 分片过多带来的副作用

- Shard 分片是 ES 实现集群 水平扩展的最小单位
- 过多设置分片数会带来一些潜在问题
  - 每个分片是一个 Lucene 索引，会使用机器的资源，过多的分片会导致额外性能开销
  - 每次搜索的请求，需要从每个分片上获取数据
  - 分片的 Meta 信息由 Master 维护，过多，会导致管理的负担。经验值：控制分片总数在 10w 以内



## 如何确定主分片数

ES 给出了一些推荐

- 从存储的物理角度看
  - 日志类应用：单个分片不要大于 50GB
  - 搜索类应用：单个分片不要大于 20GB
- 为什么要控制分片存储大小
  - 提高 update 性能
  - Merge 时，减少所需的资源
  - 丢失节点时，具备更快的恢复速度/便于分片在集群内 Rebalanceing



## 如何确定副本分片数

- 副本是主分片的拷贝
  - 提供系统的可用性，防止数据丢失
  - 需要占用和主分片一样的资源
- 对性能的影响
  - 副本会降低数据的索引速度
    - 有几份副本，就会有几倍的 CPU 资源消耗在索引上
  - 会减缓对主分片的查询压力
    - 如果机器资源重复，提高副本数，可以提高整体查询的 QPS



# 如何对集群进行容量规划

> 容量规划：简单来说，一个集群需要为它准备多少台机器，创建多少个索引，每个索引需要设置怎样的分片



## 容量规划

- 一个集群总共需要多少个节点？一个索引需要设置几个分片？
  - 规划上需要保持一定的余量，当负载出现波动，节点出现丢失时，还能保证正常运行
- 考虑的一些因素
  - 机器的软硬件配置
  - 单条文档的尺寸/文档的总数据量/索引的总数据量（数据保留的时间）/副本分片数
  - 文档是如何写入的（Bulk 的尺寸）
  - 文档的复杂度，文档是如何进行读取的（怎样查询和聚合）



## 评估业务的性能需求

容量规划前，对业务进行评估

- 数据吞吐及性能需求
  - 数据写入的吞吐量，每秒要求写入多少数据？
  - 查询的吞吐量是怎样的？
  - 单挑查询可接受的最大返回时间？
- 了解你的数据
  - 数据的格式和数据的 Mappings
  - 实际的查询和聚合长的是什么样的



## 常见用例

- 搜索：固定大小的数据集
  - 搜索类的数据集增长相对比较缓慢
- 日志：基于时间序列的数据
  - 使用 ES 存放日志与性能指标。数据每天写入，增长速度较快
  - 结合 Warm Node 做数据的老化处理



## 硬件配置

- Data Node 尽可能使用 SSD
- 搜索性能要求高的场景，建议使用 SSD
  - 按照 1：10 的比例配置内存和硬盘
- 日志类和查询并发低的场景，可以考虑使用 HSD
  - 按照 1：50 的比列配置内存和硬盘
- 单节点数据建议控制在 2TB，最大不建议超过 5TB
  - 单个分片不要超过 20GB
- JVM 配置机器内存的一半，JVM 内存配置不建议超过 32G



# 监控 ES 集群

ES 提供了一些 API

创建监控 Dashobrd

- 可以开发 ES plugin，通过读取相关的监控 API，将数据发送到 ES
- 可以使用 kibana、Graffna 等创建 Dashboard
- 也可以开发 ES Exporter, 通过 Pormetheus 监控 ES 集群





# 诊断集群的潜在问题

ES 提供了 Support Diagnostics Tool，通过这个工具运行一些命令，收集一些指标，来分析问题

==问题分析定位的门槛较高==

阿里云：部署在阿里云，阿里提供了 EYOU 智能运维工具





# 优化集群写入性能

- 客户端
  - 采用多线程、批量写方式（bulk api)
  - 多线程：需要观察是否有 Http 429 返回，实现 Retry 及线程数量的自动调节
- 服务器端
  - 单个性能问题，往往是多个因素造成的
  - 先分解问题，在单个节点上进行调整并测试，尽可能压榨硬件资源，以达到最高吞吐量
    - 使用更好的硬件
    - 观察：cpu、I/O、线程切换/堆栈状况等



服务器端优化写入性能的一些手段

- 降低 I/O 操作
  - 使用 ES 自动生成的文档 Id/一些相关的 ES 配置，例如：Refresh Interval
- 降低 CPU 和存储开销
  - 较少不必要的分词/避免不必要的 doc_values/文档的字段尽量保证相同的顺序，可提供文档的压缩率
- 尽可能做到写入和分片的均衡负载，实现水平扩展
- 调整 Bulk 线程池和队列
  - 单个 bulk 请求体数据量不要太大，ES 建议 5~15Mb
  - 写入端的 bulk 请求超时需要足够长，建议 60s 以上
  - 写入端将数据轮询到不同节点上

> [!NOTE]
>
> 一切优化，都基于 ==高质量的数据建模==



高质量的建模

- 关闭无关的功能
  - 只需要聚合不需要搜索：Index 设置为 false
  - 不需要算分：norms 设置 false
  - 不要对字符串使用默认的 dynamic mapping
  - Index_options 控制在创建倒排索引时，哪些内容会被添加到倒排索引中
  - 关闭 `_source`, 减少 IO 操作（适合指标型数据）



![image-20250109172428921](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109172430093.png)



# 集群读性能优化



## 尽量 Denormalize 数据

反范式设计

- 尽量减少 Nested 类型数据：查询速度会 ==慢几倍==
- 尽量减少 Parent/Child 关系：查询速度会 ==慢几百倍==



## 数据建模

- 尽量将数据先行计算，在保持到 ES 中。尽量避免查询时使用 Script 计算
- 尽量使用 Filter Context，利用缓存机制，减少不必要的算分
- 结合 Profile，explian Api 分析慢查询，==持续优化数据模型==
  - 严禁使用 * 开头的通配符 Terms 查询，可能导致灾难性的性能



# 集群压力测试

压力测试步骤

- 测试计划
- 脚本开发
  - Jmeter、==Elastic Rally== 等
- 测试环境搭建
- 分析比较结果







# 段合并优化

Lucene 中，单个倒排索引文件被称为 Segment, 多个 Segment 汇总在一起，称为 Lucene 的 Index, 其对应就是 ES 的 Shard



Merge 优化

- ES 和 Lucene 会自动进行 Merge 操作，Merge 操作相对较重，需要优化，降低对系统的影响
- 优化点一：降低分段产生的数量/频率
  - 可以将 Refresh Interval 调整到分钟级别（同时要记得修改 index_buffer_size
  - 尽量避免文档的更新操作
- 优化点二：降低最大 Segment 大小，避免较大的分段继续参与 Merge(会导致 Segment 变多)



Force Merge

- 当 Index 不再有写入操作的时候，建议对其进行 Force merge
  - 提升查询速度/减少内存开销
- 最终分成多少个 Segment 合适？
  - 越少越好，最好可以 force merge 成一个
    - 但是，force merge 会占用大量的网络、IO、CPU
  - 如果不能在业务高峰期之前完成，就需要考虑增大最终的分段数量



# 缓存及 Breaker 限制内存使用

ES 缓存主要分为 3 大类

![image-20250109212041182](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109212042835.png)



## Node Query Cache

- 每个节点有一个 Node Query 缓存
  - 由该节点的所有 Shard 分片共享，只缓存 Filter context 相关内容
  - Cache 采用 LRU 算法
- 静态配置，需要设置在每个 Data Node 上



## Shard Request Cache

- 缓存每个分片上的查询结果
  - 只会缓存设置了 size = 0 的查询的对应的结果，不会缓存 hits, 但是会缓存 Aggregations 和 Suggestions
- Cache Key
  - LRU 算法，将整个 Json 查询串作为 Key，与 Json 对象的顺序相关
- 静态配置
  - 数据节点：indeces.request.cache.size: 1%





## Fielddata Cache

- 除了 Text 类型，默认都采用了 doc_values, 以节约内存
  - Aggregation 的 Global ordinals 也保持在 Fielddata cache 中
- Text 类型的字段需要打开 fielddata 才能对其进行聚合和排序
  - Text 经过分词，排序和聚合效果不佳，建议不要轻易使用
- 配置
  - Indices.fielddata.cache.size, 避免产生 GC(默认无限制)



## 缓存失效

- Node Query Cache
  - 保持的是 Segment 级别缓存命中的结果
  - Segment 被合并后，缓存失效
- Shard Reqeust Cache
  - 分片 Refresh 时，Shard Request Cache 会失效
  - 如果 Shard 对应的数据频繁发生变化，该缓存的效率会很差
- Fielddata Cache
  - Segment 合并后，缓存会失效







## Circuit Breaker

断路器，避免不合理操作引起 OOM, 每个断路器可以指定内存使用的限制

- Parent circuit breaker：设置所有的熔断器可以使用的内存的总量
- Fielddata：加载 fielddata 所需的内存
- Request：防止每个请求级数据结构超过一定的内存
- In flight：request 中的断路器 
- Accounting request：请求结束后不能释放的对象所占用的内存





# 索引管理





## 索引管理 API

- Open/Close Index
  - 索引关闭后无法进行读写，但是索引数据不会被删除
- Shrink Index
  - 可以将索引的 ==主分片数收缩== 到较小的值
  - 使用场景
    - 索引保存的数据量比较小，需要重新设定主分片数
    - 索引从 Hot 移动到 Warm 后，需要降低主分片数
  - 会使用和源索引相同的配置创建一个新的索引，仅仅降低主分片数
    - 限制：源分片数必须是目标分片数的倍数，如果源分片数是素数，目标分片数只能是 1
    - 如果文件系统支持硬链接，会将 Segments 硬链接到目标索引，索引性能好
  - 完成后，可以删除源索引
- Split Index
  - 可以 ==扩大主分片个数==
- Rollover Index
  - 类似 Log4J 记录日志的方式，索引尺寸或者时间超过一定值后，创建新的
  - 应用场景
    - 一个索引数据量过大，例如：时间序列索引
    - 一般结合 Index Lifecycle Management Policies 一起使用
- Rollup Index
  - 对数据进行处理后，重新写入，减少数据量
- 看官网，还有其他的







## 索引生命周期管理

> Index Lifecycle Management ,ILM，是一种自动化管理索引生命周期的机制

它允许用户根据索引的年龄、大小或其他条件，自动执行诸如：滚动更新、分片分配、快照备份、删除等操作



ILM是ES中管理时间序列数据的重要工具



索引生命周期常见的阶段

Hot-> Warm-> Cold-> Frozen>Delete



- Hot：索引还存在大量的读写操作
- Warm：索引不存在写操作，但是，有查询的需求
- Cold：数据不存在写操作，读操作也不多
- Frozen：数据不存在写造成，几乎不存在读操作，仅用于长期存档
- Delete：索引不再需要，可被安全删除



### ILM核心概念

1. 生命周期策略：Lifecycle Policy
   - 定义索引在不同阶段的行为和操作
   - 每个策略包含多个阶段，每个阶段可定义具体的操作
   - ==Kibana 里面可配置==、ES API可配置
2. 滚动更新
   - 当索引满足特定条件时，自动创建新索引
   - 常用于Hot阶段，适合时间序列数据
3. 索引别名
   - 通过别名指向当前活跃的索引，滚动更新时自动切换别名到新索引
4. 阶段转换条件
   - 定义索引何时从一个阶段进入下一个阶段
   - 条件可以基于：索引的年龄、文档数、大小等
5. 操作：Action
   - 每个阶段可以定义多个操作
   - Rollover：滚动更新索引
   - Force Merge：强制合并段
   - Shrink：缩小索引分片数
   - Delete
   - Allocate：调整分片分配
   - Freeze：冻结索引





ES Curator

- ES 官方推出的基于 Python 的命令行工具







# ES 原理初步认识

集群中，一个白正方形代表一个节点-Node，节点之间多个绿色小方块组成一个 ES 索引，一个 ES 索引本质是一个 Lucene Index

一个索引下，分布在多个 Node 中的绿色方块为一个分片-Shard

即 Shard = Lucene Index

![image-20220620211959189](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202120849.png)





Lucene 是一个全文搜索库，ES 建立在 Lucene 之上：

![image-20220620212355322](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202123427.png)





#### Lucene

在 Lucene 里面有很多的 segment，我们可以把它们看成 Lucene 内部的 mini-index

![image-20220620212718856](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202127950.png)



segment 内部有很多数据结构

1. **Inverted index**(倒排索引)

   - 最为重要

   - ![image-20220620213016476](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202130604.png)

   - 主要包括两个部分

     - 有序的数据字段 dictionary(包括单词 term 和它出现的频率)
     - 与单词 term 对应的 postings(即存在这个单词的文件)

   - 当进行搜索时，首先将搜索的内容分解，然后再字典里找到对应的 term，从而查找到与搜索相关的文件内容

   - ![image-20220620213310255](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202133361.png)

   - 查询 the fury

   - ![image-20220620213409957](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202134032.png)

     

2. Stored Fields

   - stored fields 是一个简单的键值对，默认情况下 ES 会存储整个文件的 Json source

   - 例如查找包含特定标题内容的文件时

   - ![image-20220620213753421](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202137520.png)

     

3. Document values

   - 上述两种结构无法解决排序、聚合等问题，所有提出了 document values
   - 本质上是一个列式的存储，它高度优化了具有相同类型的数据的存储结构
   - ![image-20220620214013344](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202140445.png)

4. cache



搜索时，Lucene 会搜索所有的 segment，然后将每个 segment 的搜索结果返回，最后合并呈现给用户

Lucene 的一些特性在这个过程中非常重要

1. segment 是不可变的
   - delete：当删除发生时，Lucene 做的只是将其标志位置为删除，但是文件还是会在它原来的地方，不会发生改变
   - update：所以对于更新来说，本质上它做的工作是：先删除，然后重新索引（Re-index
2. 随处可见的压缩
   - Lucene 非常擅长压缩数据，基本上所有教科书上的压缩方式，都能在 Lucene 中找到
3. 缓存所有的数据
   - Lucene 也会将所有的信息做缓存，这大大提高了它的查询效率



当 ES 搜索一个文件时，会为文件建立相应的缓存，并且会定期(每秒)刷新这些数据，然后这些文件就可以被搜索到

![image-20220620214547223](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202145321.png)

所有 ES 会将这些 Segment 合并，这个过程中 segment 最终会被删除掉，生成一个新的 segment，这就是为什么添加文件可能会使索引所占空间变小，它会发生 merge，从而可能会有更多的压缩

![image-20220620214739766](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202215396.png)



路由 routing

每个节点都保留一份路由表，当请求到达任何一个节点时，ES 都有能力将请求转发的期望的节点 shard 上进一步处理





#### ES 整体结构

![image-20220620231634563](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202316667.png)

1. 一个 ES 集群模式下，由多个 Node 组成，每个节点就是 ES 的一个实例
2. 每个节点有多个分片，p0、p1 是主分片，R0、R1 是副本分片
3. 每个分片对应一个 Lucene Index(底层索引文件)
4. Lucene Index 是一个统称
   - 由多个 segment 组成(就是倒排索引)，每个 segment 存储着 doc 文档
   - commit point 记录了索引 segment 的信息

#### Lucene 索引结构

![image-20220620231950382](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202319532.png)

![image-20220620232007170](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202320315.png)

#### Lucene 处理流程

![image-20220620231120127](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202311241.png)

创建索引过程

1. 准备待索引的原文档，数据来源可能是文件、数据库、网络等等
2. 对文档内容进行分词处理，形成一些列 term
3. 索引组件对文档和 term 处理，形成字典和倒排表

搜索索引过程

1. 对查询语句进行分词处理，形成一系列 term
2. 根据倒排索引表查找出包含 term 的文档，并进行合并形成文档结果集
3. 比对查询语句与各个文档相关性得分，按照得分高低返回







# Java 如何于 ES 交互

1. 节点客户端
   - node client：节点客户端作为一个非数据节点加入到本地集群中
   - 换句话说：它本身不保存任何数据，但是它知道数据在集群的哪个节点中，并且可以把请求转发的正确的节点
2. 传输客户端
   - transport client：轻量级的传输客户端，可以将请求发送到远程集群
   - 它本身不加入集群，但是它可以将请求转发到集群中的一个节点上







# Logstash

- ELT 工具/数据搜索集处理引擎，支持 200+插件

  ![image-20250109224520317](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250109224523150.png)





## Logstash consepts(概念)

- Pipline
  - 包含了 input-> filter-> output 三个阶段处理流程
  - 插件生命周期管理
  - 队列管理
- Logstash Event
  - 数据在 Logstash 内部流转时的具体表现形式
    - 数据在 Input 阶段被转换为 Event，在 Output 被转换完成目标格式的数据
  - Event 其实是一个 Java Object，在配置文件中，对 Event 的属性进行增删改查



## Logstash 架构

![image-20250110141821379](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250110141822597.png)



## Input Plugins

一个 Pipline 可以有多个 input 插件。可官网查看

- Stdin/File
- Beats/Log4j/ES/JDBC/Kafka/Mq/Redis/..
- JMX/Http/Websocket/Upd/Tcp
- Google Cloud Storage/Amazon S3...
- Github/Twitter



## Output Plugins

- 将 Event 发送到特定的目的地，是 Pipline 的最后一个阶段
- 常见的 Output Plugins，可官网查看
  - ES
  - Email/Pageduty(事件管理平台)
  - Influxdb/Kafka/MongoDB/Opentsdb/Zabbix
  - Http/Tcp/Websocket



## Codec Plugins

将原始数据 decode 成 Event、将 Event encode 成目标数据

- 内置 Codec Plugins，可官网查看
- Line/Multiline: 行数据转换
- Json/Avro/Cef
- Dots/Rubydebug



## Filter Plugins

处理 Event，一个 filter 里面可以包含多个 plugins

内置的 Filter plugins, 可官网查看

- Mutate: 操作 Event 的字段
- Metrics: Aggregate metrics
- Ruby: 执行 Ruby 代码



## Queue

一个 Logstash 是支持多个 Input 的，引入 Queue 防止消息丢失

![image-20250110144123202](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250110144124402.png)



- In Memory Queue
  - 进程 Crash（崩溃）、机器宕机，会导致数据丢失
- Persistent Queue
  - Queue.type.persisted(默认是 memory)
  - Logstash 即使宕机，数据也不会丢失，数据保证会被消费，可以替代 Kafka 等消息队列缓冲区的作用







# Beats

- 以搜集数据为主
- 支持与 Logstah 或 ES 集成
- 全品类采集器/轻量级/开箱即用/可插拔/可扩展/可视化



能收集的数据源

- Filebeat：日志文件
- Metricbeat：收集指标数据
- Packetbeat: 网络数据
- Winlogbeat: Windows 事件日志
- Auditbeat：审计数据
- Heartbeat：运行时间监控
- Functionbeat: 无需服务器的采集器
- 等



这些 bests 启动后，可以在 Kibana 的 Dashboard 里面查看分析



## Metricbeat

- 用来定期搜集操作系统、软件等的指标数据
- 收集后，指标存储到 ES，可通过 Kibana 进行实时的数据分析



Metric VS Logs

- Metric：可聚合的数据，定期搜索
- Log: 文本数据，可随机搜集



### Metricbeat 组成

- Moudule
  - 搜集的指标对象。例如：不同的操作系统、不同的数据库、不同的应用系统
  - Metricbeat 提供了许多内置的 Module, 亦可以自定义
- Metricset
  - 一个 Module 可以有多个 metricset
  - 具体的指标集合。以减少调用次数为原则进行划分
    - 不同的 metricset 可以设置不同的抓取时长







# Kibana 基本可视化组件介绍

- Discover: 探索数据，就是平时我们看日志那个界面
- Dashboard：用于数据可视化展示和交互式分析，其实是一组相关主题的组件
- Visualize: 数据可视化（将数据聚合以图表等形式查看）
- Canvas: 更加炫酷的方式，演绎你的数据，做一些大屏展示，可以针对像素级做一些高度定制。
- Maps







# APM 进行程序性能监控

ES 提供了 APM 功能，类似 Peromethus 普罗米修斯那种

可以监控类似 Java 程序、Python 程序等, 在 Kibana 中查看各种指标



# ES 平台机器学习功能

ES 机器学习功能，是 X-Pack 里面的收费功能

- 主要是针对 ==时序数据的异常检测和预测==
  - 异常检测：异常代表是不同的，但是，未必代表是坏的。定义异常需要一些指导标准，从哪些方面去看认为是异常
- ES 的机器学习使用的是：贝叶斯统计，一种概率计算方法





# ELK 对日志进行集中管理

日志搜集-> 格式化分析-> 检索与可视化-> 风险告警



## Filebeat

- 读取日志文件，Filebeat 不做数据的解析
  - 日志是非结构化的数据，需要进行处理后，以结构化的方式保存到 ES
- 保证数据至少被读取一次
- 处理多行数据，解析 JSON，简单的过滤



Filebeat 简单工作原理

![image-20250110174102588](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/20250110174103844.png)



Filebeat 执行流程

1. 定义数据采集：Prospector 配置
2. 建立数据模型：Index Template
3. 建立数据处理流程：Ingest Pipline
4. 存储并提供可视化分析：ES+Kibana, Logstash+ES+Kibana



























