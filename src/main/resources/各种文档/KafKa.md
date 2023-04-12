## 消息队列及应用场景

消息：指的是在应用间传送的数据，消息可以非常简单，比如只包含文本字符串，也可以很复杂，比如包含嵌入的对象

消息队列：Message Queue简称MQ，是一种应用间的通信方式，是一种跨进程的通信机制，常用于上下游之间传递消息。消息发送后立即返回，由消息系统来确保信息的可靠传递

MQ是一种常见的上下游 "逻辑解耦+物理解耦"的消息通信，消息发送上游只需要依赖MQ，逻辑上和物理上都不用依赖其他服务



常见消息队列有：**kafka**，activeMq，RabbitMq，**RocketMq**(阿里开源mq框架)

大数据场景主要采用kafka做为消息队列。在JavaEE开发中采用另外三种，一般而言使用RocketMQ



### 消息队列使用场景

1. **缓冲和消峰**：有助于控制和优化数据流经过系统的速度，解决生产消息和消费消息的处理速度不一致请求
   - 比如上游的订单系统每秒产生5000个订单，把订单发到消息队列
   - 我下游订单系统设定一个消费频率，每秒只消费2000个订单，防止数据库宕机
2. **解耦**：允许独立的拓展或修改两边的处理过程，只要确保它们遵守同样的接口约束
3. **异步通信**：允许把消息放入队列，但并不立即处理它，然后在需要的时候再去处理它们。一般针对实时性要求不高的操作



### 使用消息队列需要注意的问题

1. 增加了系统的复杂性
   - 引入队列后，需要考虑消息会不会丢失、消息积压怎么办、队列满了怎么办、消息会不会重复消费、怎么保证消息顺序性等等一系列问题
2. 降低了系统的可用性
   - 系统之间依赖MQ，MQ万一挂了怎么办
3. 一致性问题
   - 多个系统依赖一个消息系统，如果部分系统成功消费，部分系统消费失败，可能导致数据一致性问题

---



## 消息系统的两种模式

#### 点对点模式

> 一个具体的消息只能有一个消费者消费



1. 在点对点的消息系统中，消息保留在队列中，一个或者多个消费者可以消费队列中的消息，但是消息最多只能被一个消费者消费，**一旦有一个消费者将其消费掉，消息就从该队列中消失**
1. ![image-20220424221006967](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204242212370.png)

#### 发布/订阅模式

1. 消息生产者将消息发布到消息队列中，同时有多个消息消费者（订阅）消费该消息，发布到消息队列的消息会被所有订阅者消费。消费者消费消息后，队列不会消除消息。kafka中可以通过配置使得过一段时间后自动删除以释放磁盘空间
4. 这种模式下有两种消费方式
   - 消费者主动消费消息  ：kafka采用此种方式poll
   - 消息队列主动推送给消费者
5. ![image-20220424221235289](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204242212437.png)

---



## Apache KafKa

项目中的版本：2.3.1，          2.8.x后可以移除zookeeper了

> Kafka是由Linkedin公司基于Scala和Java开发的分布式消息发布-订阅系统
>
> **Kafka最广为人知的是消息队列系统，但是，事实上kafka已然成为一个流行的分布式流处理平台**
>
> 特点：具有高吞吐、低延迟的特性，许多大数据处理系统例如：storm、spark、flink都能很好的集成



参考博客：https://blog.csdn.net/hellozpc/article/details/105680217

### 定义

1. kafka是一个**分布式的基于发布/订阅模式的消息系**统，可以处理大量的数据，并使你能够将消息从一个端点传递到另一个端点
2. Kafka消息保留在磁盘上，并在集群内复制以防止数据丢失，Kafka构建在Zookeeper同步服务之上
3. 主要应用于大数据实时处理领域
4. 最新定义：分布式事件流平台，被数千家公司用于高性能数据管道、流分析、数据集成和关键认为应用

Kafka支持低延迟消息传递，并在出现机器故障时提供对容错的保证。 它具有处理大量不同消费者的能力。**kafka非常快，执行速度大约为200万写/秒**。kafka将所有数据都保存到磁盘，意味着所有写都会进入操作系统（RAM)的页面缓存，这使得将数据从页面缓存传输到网络套接字非常有效



### Kafka通常的3个角色

1. 消息系统
2. 分布式流处理平台
   - Kafka 不仅能够与大多数[流式计算框架](https://blog.csdn.net/hellozpc/article/details/104444984)完美整合，并且自身也提供了一个完整的流式处理库，即kafka Streaming。kafka Streaming提供了类似[Flink](https://blog.csdn.net/hellozpc/article/details/104444984)中的窗口、聚合、变换、连接等功能
3. 存储系统
   - 通常kafka消息队列会把消息持久化到磁盘，防止消息丢失，保证消息可靠性。Kafka的消息持久化机制和多副本机制使其能够作为通用数据存储系统来使用。



### 体系结构

> Kafka体系结构中，通常包含多个生产者(Producer)、多个消费者(Consumer)、多个Broker(Kafka服务器)，以及一个Zookeeper集群
>
> Zookeeper是在Kafka中主要负责管理Kafka集群的元数据、控制器选举等操作的分布式协调器

![image-20220505161011733](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/image-20220505161011733.png)







### 术语

#### 生产者、消费者

1. 消息的发送者叫Producer，消息的使用者/接收者是Consumer
2. 生产者将数据发送到Kafka集群中，消费者从中获取消息进行业务的处理
3. ![image-20220504223114695](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205042231879.png)

#### broker

1. Kafka集群中有很多台Server，其中每一台Server都可以存储消息，将每一台Server称为一个Kafka实例，也叫做broker
2. broker接收生产者发送的消息并存入磁盘，broker同时服务消费者拉取消息的请求，返回目前已经提交的信息



主题和分区是Kafka中两个核心的概念！！！

#### Topic

> Kafka中，消息是以topic为单位进行归类的
>
> 生产者必须将消息发送到指定的topic，即发送到Kafka集群的每一条消息都必须指定一个主题，消费者消费消息也需要指定主题，即消费者负责订阅主题并进行消费

1. 一个topic里保存的是同一类消息，相当于对消息的分类。数据存储在主题中
2. 每个producer将消息发送到kafka中，都需要指明要存的topic是哪个，也就是指明这个消息属于哪一类



如果一个Topic在Kafka中只对应一个存储文件，那么海量数据场景下，这个文件所在机器的I/O将会成为这个主题的瓶颈，而分区正是为了解决这类问题

#### Partition分区

> Kafka中，一个Topic可以分为多个partition，每个分区可以分布式的存储在不同的机器上。一个特定的分区只属于一个topic。
>
> 同一个主题的不同分区包含的消息是不同的
>
> 在底层存储上，每一个分区对应一个可追加的log文件(append log)，消息在被追加到分区log文件时会分配一个特定的offset(偏移量)，offset是消息在分区中的唯一标识，**Kafka通过offset来保证消息在分区内的有序性。**offset不能跨越分区，即Kafka保证的是分区有序而不是全局有序



1. 每个topic至少包含一个分区
1. 每个topic都可以分成多个partition，每个partition在存储层面是append log文件
2. 任何发布到此partition的消息都会被直接追加到log文件的尾部
3. 为什么要进行分区：最根本原因是kafka基于文件进行存储，当文件内容大到一定程度时，很容易达到单个磁盘的上限，因此采用分区的方法，一个分区对应一个文件，这样就可以将数据分别存储到不同的broker上去。这样做也可以负载均衡，容纳更多的消费者



#### 偏移量Offset

1. 一个分区对应磁盘上的一个文件，而消息在文件中的位置就称为offset，offset是一个long型数字，它可以唯一标记一条消息
2. 由于kafka没有提供其他额外的索引机制来存储offset，文件只能顺序的读写，所以在kafka中几乎不允许对消息进行"随机读写"



#### Replica副本

> 在分区下，Kafka又引入了副本的概念。如果说分区实现了水平扩展，那么副本则是实现了纵向扩展，提升了容灾能力。同一分区中的不同副本保存的消息是相同的。
>
> 但是，在同一时刻，副本之间并非完全一致，因为同步存在延迟。**副本之间是一主多从的关系**

leader副本负责处理读写请求，fllower副本负责与leader副本进行消息同步。副本在不同的broker中，当leader副本出现故障时，从fllower副本中重新选举leader副本对外提供读写服务

Kafka通过多副本机制实现了故障的自动转移，当Kafka集群中某个Broker挂掉时，副本机制保证该节点的partition数据不丢失，仍能保证kafka可用



下图展示了一个多副本架构：4台Broker，3分区，3副本。生产者和消费者只和leader副本进行交互，follower副本只负责和leader进行消息同步。每个分区都存在不同的broker中，若每个broker单独部署一台机器的话，那么不同的partition以及副本在物理上便是隔离的

![image-20230314221326181](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303142213482.png)



可以认为topic 是逻辑上的概念，partition是物理上的概念，因为每个partition 都对应于一个.log文件存储在kafka 的log目录下，该log 文件中存储的就是producer 生产的数据。Producer 生产的数据会被不断追加到该log 文件末端，且每条数据都有自己的offset(偏移位)。消费者组中的每个消费者，每次消费完数据都会向kafka服务器提交offset，以便出错恢复时从上次的位置继续消费





---



## Kafka基本原理

#### 分布式和分区

1. 我们说 kafka 是一个分布式消息系统，所谓的分布式，实际上我们已经大致了解。消息保存在 Topic 中，而为了能够实现大数据的存储，一个 topic 划分为多个分区，每个分区对应一个文件，可以分别存储到不同的机器上，以实现分布式的集群存储。另外，每个 partition 可以有一定的副本，备份到多台机器上，以提高可用性。
2. 一个 topic 对应的多个 partition ，分散存储到集群中的多个 broker 上，存储方式是一个 partition 对应一个文件，每个 broker 负责存储在自己机器上的 partition 中的消息读写

#### partition副本

1. kafka可以配置partition需要备份的个数(replicas)，每个partition将会被备份到多台机器上，以提高高可用性，备份的数量可以通过配置文件指定
2. 既然有副本，就涉及到对同一个文件的多个备份如何进行管理和调度。kafka 采取的方案是：每个 partition 选举一个 server 作为“leader”，由 **leader分区负责所有对该分区的读写**，其他 server 作为 follower 只需要简单的与 leader 同步，保持跟进即可。如果原来的 leader 失效，会重新选举由其他的 follower 来成为新的 leader。如何选取：kafka所以zookeeper在broker中选出一个controller，用于partition分配和leader选举
2. 一个分区只能被一个消费者消费

#### 整体数据流

kafka总体数据流满足下图，下图基本上概括了整个kafka的基本原理

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733104250.jpg)



##### 数据生产过程

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733117119.png)

1. 对于生产者要写入的一条记录，可以指定四个参数：分别是topic、partition、key、value，其中topic和value(要写入的数据)是必须指定的
2. 对于一条记录，先对数据进行序列化为字节数组，然后按照topic和partition，放到对应的发送队列中。如果partition没有指定，那么情况会是这样的：
   - 指定了key，则按照key进行哈希，相同的key去一个partition
   - 没有指定key，Round-Robin来选partition
3. 生产者会和topic下所有partition leader保持socket连接，消息由生产者直接通过socket发送到broker。其中partition leader的位置(host:port)注册在zookeeper中，生产者作为zookeeper client，已经注册了watch用来监听partition leader的变更事件，因此可以准确的知道谁是当前的leader
4. 生产者端采用异步发送：将多条消息暂且在客户端buffer起来，并将他们批量的发送到broker。小数据I/O太多会拖慢整体的网络延迟，批量延迟发送提升了网络效率
4. 如果写入成功，就返回一个RecordMetaData对象，它包含了主题、分区信息、偏移量等等。如果写入失败，告知生产者重新发送消息，达到最大重试次数就抛出异常



##### 数据消费过程

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733117671.png)

1. 对于消息者，不是以单独的形式存在的，每一个消费者属于一个consumer group，一个group包含多个消费者
2. 注意**：订阅topic是以一个消费组来订阅的**，发送到topic的消息，只会被订阅此topic的每个group中的一个consumer消费
3. 如果所有消费者具有相同的group，那么就像是一个点对点的消息系统，如果每个消费者都具有不同的group，那么消息会广播给所有的消费者
4. 具体说来，这实际上是根据 partition 来分的，**topic的一个 Partition，只能被消费组里的一个消费者消费**，但是可以同时被多个消费组消费，消费组里的每个消费者是关联到一个 partition 的，因此有这样的说法：对于一个 topic,同一个 group 中不能有多于 partitions 个数的 consumer 同时消费,否则将意味着某些 consumer 将无法得到消息
5. 同一个消费组的两个消费者不会同时消费一个 partition
6. kafka采用了pull方式，即：在消费者和broker建立连接后，主动去pull消息，首先消费者可以根据自己的消费能力适时地去pull消息并处理，且可以控制消息消费的进度(offset)
7. 当消息被消费者接收之后，需要保持offset记录消费到哪里了，0.10版本后，kafka把这个offset的保持从zk中剥离，保存在一个名叫consumeroffsets topic的Topic中



---

## Kafka工作流程

Kafka 只是分为一个或多个分区的主题的集合。Kafka 分区是消息的线性有序序列，其中每个消息由它们的索引(称为偏移)来标识。Kafka 集群中的所有数据都是不相连的分区联合。 传入消息写在分区的末尾，消息由消费者顺序读取。 

Kafka 以快速，可靠，持久，容错和零停机的方式提供基于pub-sub 和队列的消息系统。 在这两种情况下，生产者只需将消息发送到主题，消费者可以根据自己的需要选择任何一种类型的消息传递系统

1. 发布-订阅消息的工作流程

   1. 生产者向主题topic发送消息

   2. kafka broker存储该特定主题配置的分区中的所有消息，它确保消息在分区之间平等共享。 如果生产者发送两个消息并且有两个分区，Kafka 将在第一分区中存储一个消息，在第二分区中存储第二消息

   3. 消费者订阅特定主题

   4. 一旦消费者订阅主题，kafka将向消费者提供主题的当前偏移，并且还将偏移保存在zookeeper中

   5. 消费者将定期(例如100ms)请求kafka新消息

   6. 一旦kafka收到来自生产者的消息，它将这些消息转发给消费者

   7. 消费者将收到消息进行处理

   8. 一旦消息被处理，消费者将向broker发送确认

   9. 一旦 Kafka 收到确认，它将偏移更改为新值，并在 Zookeeper 中更新它。 由于偏移在 Zookeeper 中维护，消费者可以正确地读取下一个消息

   10. 以上流程将重复，直到消费者停止请求。

   11. 消费者可以随时回退/跳到所需的主题偏移量，并阅读所有后续消息

       

2. 队列消息/用户组的工作流

在队列消息传递系统中，消费者不是单一的，具有相同组 ID 的一组消费者将订阅主题，消息在这一组消费者之间共享。

1. 生产者以固定间隔向某个主题发送消息
2. kafka存储  为该特定主题配置的分区中的所有消息
3. 单个消费者订阅特定主题，假设Topic-01为Group ID为Group-1
4. kafka以与发布-订阅消息相同的方式与消费者交互，直到新消费者以相同的组ID订阅相同主题Topic-01
5. 一旦新消费者到达，kafka将其操作切换到共享模式，并在两个消费者之间共享数据。此共享将继续，直到用户数达到为该特定主题配置的分区数
6. 一旦消费者的数量超过分区的数量，新消费者将不会接收任何进一步的消息，直到现有消费者取消订阅任何一个消费者。 出现这种情况是因为 Kafka 中的每个消费者将被分配至少一个分区，并且一旦所有分区被分配给现有消费者，新消费者将必须等待

### Zookeeper在Kafka中的作用

> Zookeeper是一个分布式协调框架。很好的将消息生产、存储、消费过程结合在一起。
>
> 典型的Kafka集群中，Kafka 服务器通过 Zookeeper 集群共享信息。Kafka通过Zk管理集群配置、选举leader，以及在消费者组发生变化是进行rebalance
>
> Kafka 在 Zookeeper 中存储基本元数据，例如关于主题，broker，消费者偏移(队列读取器)等的信息，控制器选举等操作的分布式协调器
>
> 不过，kafka2.8 之后可以不用配置zk了，kafka自己搞了一套实现



zk集群主要作用：

1. 存元数据：主题分区所有数据都存在zk中
2. 成员管理：指broker节点注册，注销以及属性变更
3. Controller选举：选举集群controller

https://blog.csdn.net/peng_2297731313/article/details/124099789

#### Broker注册

1. Broker是分布式部署并且相互独立，所以需要一个注册中心来对整个集群的Broker进行管理，所以采用Zk来专门记录Broker服务器列表的节点：/broker/ids
2. broker在zk中保存为一个临时节点，节点路径为：/brokers/ids/[0...N]
3. kafka采用了全局唯一的数字来指代Broker服务器，不同的Broker必须使用不同的brokerId进行注册，创建完节点后，每个Broker就会将自己的IP地址和端口等信息记录到该节点中去。其中，Broker创建的节点是临时节点，一旦Broker宕机，对应的临时节点会被自动删除。这样就能很方便的监控到Broker节点的变化，及时调整负载均衡等等

#### Topic注册

topic会被分为多个partition并分配到多个broker上，分区的信息以及broker的对应关系，都保存在zk中。ZK中，专门的节点来记录这些信息，节点路径为：/brokers/topics{topic_name}，每个topic都会在topics下建立独立的子节点，每个topic节点都会包含分区以及broker的对应信息。并且，topic创建的节点也是临时节点

#### 维护分区与消费者的关系



#### 记录消息消费的进度Offset

> offset存储的地方不止zookeeper，也可以存放在其他地方。比如：项目中就是先把offset存储在redis中，再手动确认ack

消费者对指定的消息分区进行消费的过程中，需要定时的将分区消息的消费进度offset记录到Zk中，以便在该消费者进行重启或其他消费者重新接管该消息分区的消息消费后，能从之前的进度开始继续进行消费

offset在zk中是由一个专门的节点进行记录的，节点路径为：/consumer/[group_id]/offsets/[topic]/[broker_id_partition_id]，节点内容就是offset的值



#### 消费者注册

新的消费者组注册到zookeeper中时，zookeeper会创建专用的节点来保存相关信息，其节点路径为 /consumers/{group_id}，其节点下有三个子节点，分别为[ids, owners, offsets]。

ids节点：记录该消费组中当前正在消费的消费者；

owners节点：记录该消费组消费的topic信息；

offsets节点：记录每个topic的每个分区的offset；

---





## kafka数据可靠性- ack机制

kafka采用ack机制保证数据可靠性，类似与TCP的三次握手四次挥手



### 生产者的可靠性

> 1. 存储层方面：前面提到的为每个partition设置副本，即leader和follower
> 2. 发送层面：生产者向broker发送消息后，broker上的leader会向生产者发送ack确认消息，如果生产者没有收到ack，将会触发重试机制





生产者的ISR机制

生产者把消息发送到服务器的leader后，leader将消息同步给自己所有的follower。默认情况下，kafka默认当所有follower都完成同步消息后，再给生产者返回ack。但是当其中的一个follower发生故障后，迟迟不能同步完成，此时leader就会一致等下去。这个问题该怎么办？kafka利用ISR机制解决这个问题。

![image-20230315210138770](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303152101882.png)





在kafka中，一个主题的**分区的所有的副本集合称之为AR**(Assigned Replicas)。AR=ISR+OSR

**leader节点维护了一个动态的集合，即in-sync replica set (ISR)**，指的是：和leader保持一定程度同步的follower集合(包括leader节点在内)。

**与leader副本同步滞后过多的副本组成OSR(Out-of-Sync Replicas)集合**

因此AR=ISR+OSR。正常情况下所有的follower都应该和leader保持一定程度的同步，即AR=ISR



kafka何时给生产者返回ack相应：kafka根据用户配置的ack级别来确认何时返回客户端ack消息

**acks参数：用来指定分区中有多少副本收到这条消息后，生产者才能确认这条消息是写入成功的**，这直接影响kafka集群的吞吐量和消息可靠性



ack有3个可选值0、1、-1

1. ack=1：默认值1，可能出现消息丢失
   - producer发送消息后，只要**分区的leader成功写入消息后，就会收到服务器的ack成功响应**
   - 如果消息写入leader失败，比如leader挂了，正在重新选举，此时生产者客户端会收到错误相应，可以进行重发
   - 如果leader成功写入后，还没有来得及把数据同步到follower节点就挂了，这时候消息就丢失了。
   - ack=1是kafKa消**息可靠性和吞吐量之间的折中方案**，也是默认的配置
2. ack=0，可能出现消息丢失
   - 就是生产者不等待broker响应。这种情况模式下延迟最低，但是可能丢失数据。**适合高吞吐量、接受消息丢失的场景。**
3. ack=-1 或 ack=all：
   - producer**需要收到分区内所有副本的成功写入的通知才认为推送消息成功了**
   - 即：生产者在消息发送之后，需要等待ISR 中的所有副本都成功写入消息之后才能够收到来自服务端的ack响应
   - ack=-1可以达到最高的数据可靠性，但是，在所有follower成功写入后，leader发送ack之前，leader挂了，此时生产者认为发送失败了，此时重新发送数据给新的leader，造成数据重复发送



---



## kafka生产者

kafka producer发送消息可以采用同步或者异步的方式

核心类

KafkaProducer：生产者对象，用来发送数据

ProducerConfig：设置生产者的一系列配置参数

ProducerRecord：每条数据都要封装成一个ProducerRecord对象



### 生产者发送消息流程

1. 生产者生成消息后，首先会经过一个或多个拦截器链
   - producer->send(ProducerRecord)->[Interceptors拦截器可有可无]->
2. 当消息通过所有拦截器链后，会根据key、value的序列化配置进行序列化消息内容。Serializer序列化器(kafka自己实现的，没有Java的)
   - 生产者和消费者必须使用相同的k-v序列化方式
3. 序列化后，根据自定义的分区器后默认分区器，进行获取消息的所属分区
   - 一个分区会创建一个队列
4. 获取到消息所属的分区后，消息会被追加放到消息缓冲区中  
   - Java对象为：RecordAccumulator
   - 可配置大小为：buffer.memory



sender线程

Sender(读取数据)

batch.size：只有数据累积到batac.size后，sender才会发送数据这一批数据。默认16k

linger.ms：如果数据迟迟未到batch.size，sender等待linger.ms设置的时间后就会发送数据。单位是ms，默认是0ms表示没有延迟，项目中配置的1ms

broker收到消息后，给sender应答（ack机制）

sender成功后，清理队列里的消息，失败会去重试



### 异步发送

> Kafka的**Producer发送消息采用的是异步发送的方式**，主要涉及main线程、sender线程以及一个线程共享变量RecordAccumulatro
>
> main线程将消息发送给RecordAccumulator，sender线程不断从它那里拉取消息发送给kafka

![image-20230316162408650](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303161624276.png)



异步不带回调

~~~java
public class MyProducer1 {
    public static void main(String[] args) throws Exception{
      Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");
      props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
      props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 32);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
      props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32 * 1024 * 1024);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 20; i++) {
            producer.send(new ProducerRecord<String, String>("topic_test", Integer.toString(i), "value:" + i));
        }

        producer.close();
    }
}

~~~



### 带回调函数的异步发送

KafkaProducer#send(ProducerRecord<K,V>, Callback)

~~~java

public class MyProducer2 {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 32);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32 * 1024 * 1024);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("topic_test", Integer.toString(i), "value:" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (null == exception) {
                        System.out.println("send success->" + metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }
}
~~~

回调函数在producer收到ack时异步调用，该方法有两个参数：RecordMetadata、Exception

这两个参数是互斥关系，即如果exception为null，则发送消息成功，此时RecordMetadata必定不为null。消息发送异常时，RecordMetadata为null，而exception不为null。消息发送失败会自动重试，不需在回调函数中手动重试。重试次数由参数retries设定

KafkaProducer设定参数retries，如果发送消息到broker时抛出异常，且是允许重试的异常，那么就会最大重试retries参数指定的次数



### 同步发送

指一条**消息发送后会阻塞当前线程，直到返回ack消息**

producer的send方法返回对象是Future类型，因此可以通过调用**Future对象的get()方法触发同步等待**

~~~java
producer.send(new ProducerRecord<String, String>("topic_test", Integer.toString(i), "value:" + i)).get();
~~~





### 生产者分区

> 分区提高了topic的处理性能，提高了topic的并发性。kafka生产者发送的数据封装成ProducerRecord对象，该对象中有partition属性，每条ProducerRecord会被发送到特定的partition中
>
> **消息写入到哪个分区是由生产者决定的**，在发送消息时，可以指定分区，否则用默认分区器计算。因为分区可能会调整，通常，不会指定固定的分区，而是依靠分区计算器来分派到具体的分区



**分区的好处**

1. 便于合理使用存储资源，每个分区在一个Broker上存储，可以把海量的数据按照分区切割成一块一块数据存储在多台Broker上。合理控制分区的任务，可以实现负载均衡的效果
2. 提高并行度，生产者可以以分区为单位发送数据，消费者可以以分区为单位进行消费数据

**（默认？）分区原则**

1. 构造器中指明partiton时，直接将指定值作为partition值
2. 没有指明partition值但是有key的情况，将key的hash值与topic的partition数进行取余得到partiton值
3. 既没有指定partition值，也没有指定key，则：第一次调用时随机生成一个整数，后面每次调用在这个整数上自增，并与这个topic的partition数取模得到partition值，即采用round-robin算法（轮询各分区发送）

#### 生产者发送消息的分区策略

1. 默认的分区器DefaultPartitioner

2. 自定义分区器，每次计算出不同的分区

   - 实现Partitioner接口

   - 重写核心方法

   - ~~~java
     public int partition(String topic，Object key，byte[] keyBytes，Object value，byte[] valueBytes，Cluster cluster) {
         //获取数据
         String msgValue=value.toString();
         int partition;
         if(msgValue.contains("qc")){
             partition=0;
         }else{
             partition=1;
         }
         return partition;
     }
     ~~~

   - 关联自定义分区器



### 生产者如何提高吞吐量

通过修改参数配置：缓冲区大小

RecordAccumulator:缓冲区大小，修改为64m



### 生产者数据可靠性

应答机制ack

1. ack=0：生产者发送过来的数据，broker不需要落盘应答
   - 数据可靠性：丢数
2. ack=1：生产者发送过来的数据，需要leader收到数据后应答
   - 数据可靠性：丢数
   - leader应答完后，还没有开始同步副本follower，leader挂了
3. ack=-1/all：生产者发送过来的数据，leader和ISR队列(follower)里面的所有节点收齐数据后应答
   - 问题：leader收到数据，所有follower开始同步数据，但有follower挂了，迟迟不能与leader进行同步，怎么解决?
   - Leader维护了一个动态的ISR：和leader保持同步的follower+leader集合(leader:broker0，isr：follower0，1，2)
   - 如果follower长时间未向leader发送通信请求或同步数据，则将该follower提出ISR，该时间阈值由replica.lag.time.max.ms参数设定，默认30s
   - 数据可靠性：如果分区数副本设置为1个，或者isr里应答的最小副本数量设置为1，则和ack=1是一样的

数据完全可靠条件=ack级别设置为-1 +分区副本数大于等于2 + ISR里应答的最小副本数量大于等于2

ack=1：一般用于传输普通日志，允许丢失个别数据

ack=-1：一般用于传输和钱相关的数据，对可靠性要求高的场景



### 生产者的ExactlyOnce

exactlyOnce：精确的一次、正好一次

kafka能保证精准一次性吗？

> 在交易业务里，对于精准一次性要求是比较高的，我们绝不能丢失交易数据，也不应该发起重复交易，这就是ExactlyOnce的定义



#### 消息传送机制

kafka支持3中消息投递语义，通常使用At least once模型

1. At most once：最多一次，消息可能会丢失，但不会重复

   - ack=0，可以保证每天消息只会发送一次，不会重复

2. At least once：最少一次，消息不会丢失，可能会重复

   - ack=-1，可以保证生产者和broker之间不丢失数据，但是可能会重复

3. **Exactly once：只且一次，消息不丢失不重复，只且消费一次**

   - **ack=-1，消息可能重复，所以只需要在at least once基础上保证幂等性即可**
   - **开启kafka幂等性，enable.idompotence=true**

   

#### kafka如何保证幂等性

> 对于每一个生产者，会分配一个唯一的Pid，在发送到同一个broker的消息会附带一个sequence number。sequence number是单调自增的，broker会对<pid,partitionId,Seq num>做一个缓存，当具有相同主键的消息提交时，kafka只会持久化一条。
>
> 但是，Pid会随着生产者的重启而分配一个新的，并且不同的partition对应的partitionId也不相同，所以kafka的幂等性无法保证跨分区、跨会话
>
> 所以，kafka幂等性只能保证单分区会话内不重复



### Producer数据重复写入性

大多出现在ack=-1情况下

例如：producer发送消息到broker，broker里的leader、follower已经落盘，准备回应producer的时候，突然这个leader挂了，ack没有发送出去，producer没有收到确认消息。这时候，会重新选举出一个leader，由于上一次leader未应答，所以，producer会重新发送消息到新的leader，这就造成了数据重复写入



#### 数据重复写入-解决方法

1. kafka自带方法：ack=-1 + 幂等性 + 事务
   - kafka幂等性：见上面的解释。指的producer无论向borker发送多少次重复数据，broker端只会持久化一条，保证了不重复
   - 默认是开启幂等性的，即：enable.idempotence=true  
   - kafka事务：支持多分区，数据有唯一id，和所有分区比较，如果存在则不发送消息到broker。生产环境下使用少，容易造成数据积压。
   - 开启事务，必须开启幂等性（没有看这里）
2. 出现重复不可怕，怎么解决是关键。即：去重
   - **可以利用Redis(BloomFilter)去重(公司项目就是这样搞得)**
   - 手段：分组、按照id开窗只取第一个值



### Producer的数据一致性

> 这里的数据一致性值得是：无论是老leader还是新选举的leader，消费者读到的都是一样的数据
>
> kafka利用了一个高水位机制解决：High water mark (木桶原理)

![image-20230316164035982](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303161640383.png)

![image-20230316164712302](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303161647509.png)



1. follower故障
   - follower发生故障后，会被踢出ISR。等待其回复后，follower读取本地记录的删除HW，并将log文件中高于HW的部分截取掉，从HW处开始向leader同步
   - 等该follower的LEO大于等于该partition（leader）的HW，即follower追上leader之后，就可以重新加入ISR了
2. leader故障
   - 会从ISR中选取出一个新的leader，为了保证多副本之间的数据一致性，其余的follower会先将各自的log文件中高于HW的部分截取掉，然后从新的leader同步数据





### Producer数据有序性

> 生产者生产的消息是有序的，为了保证有序性，生产者采用了双端队列，保证最新消息发送失败也能最先发出

有序性分为全局有序和部分有序



#### 部分有序

1. 生产者将消息发送到指定的同一个partition分区
2. kafka中每个分区中的消息在写入时都是有序的。生产者在发送消息时，可以指定需要保证顺序的消息发送到同一个分区中。这样消费者消费时，消息就是有序的

但是，存在多分区时，分区与分区间的消息是无序的

![image-20220505230242650](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205052302719.png)

#### 全局有序

如果要保证消息的全局有序，则只能由一个生产者往topic发送消息，并且一个topic内部只能有一个分区，消费者也必须是单线程消费这个队列，这样的消息就是全局有序的

**缺点：只能被一个consumer 消费，降低了性能，不适合高并发场景**





### Producer数据乱序

kafka1.x后，启用幂等后，kafka服务端会缓存producer发来的最近5个request的元数据，所以，无论如何都可以保证最近5个request的数据是有序的

开启了幂等性且缓存的请求个数小于5个，会在服务端重写排序



### 生产者拦截器

ProducerInterceptor

**kafka 生产者会在消息序列化和计算分区之前调用拦截器**的onSend方法，我们可以在此方法中进行消息发送前的业务定制

自己实现这个拦截器即可



---





## kafka broker

Zookeeper中存储的kafka信息，比较多

1. /kafka/brokers/ids：记录有哪些服务器   [0,1,2]
2. /kafka/brokers/topics/first/partitions/0/state {"leader":1,"isr":[1,0,2]}  记录谁是leader，有哪些服务器可用
3. /kafka/controller {brokerid:0}  :辅助选举leader



### kafka 分区副本

副本作用：提高数据可靠性

默认一个副本，生成环境一般配置为2个；太多副本会增加磁盘存储空间，增加网络上的数据传输，降低了效率

kafka中副本分为：leader和follower

kafka生产者只会把数据发往leader，然后follower找leader进行同步数据

kafka分区中的所有副本集合：AR assigned repllicas

AR=ISR+OSR

ISR：表示与Leader保持同步的**Follower集合**。如果Follower长时间未向Leader发送通信请求或同步数据，则Follower将被踢出ISR，该时间阈值默认30s，由replica.lag.time.max.ms设定。leader发生故障后，会从ISR中选举新的leader

OSR：表示Follower与Leader副本同步时延迟过多的副本



### kafka集群Leader选举流程

> Kafka集群中有一个或者多个broker，其中有一个broker会被选举为控制器(Kafka Controller)，也可以看作为（broker的leader，即controller leader）。它负责管理整个集群中所有分区和福本的状态
>
> 还有一种leader是分区leader

- 当某个分区的leader出现故障时，由控制器负责为该分区选举出新的leader
- 当检测到某个分区的ISR集合发生变化时，由控制器负责通知所有broker更新其元数据信息
- 当使用kafka-topic.sh脚本为某个Topic增加分区数量时，由控制器负责让新分区被其他节点感知到



#### Controller leader (broker leader)选举机制

kafka集群启动时，会自动选举一台broker作为controller来管理整个集群。

选举过程是：**每个broker都尝试在zk上创建一个/controller的临时节点，zk会保证有且仅有一个broker能创建/controller节点成功。这个broker就是集群的总控器controller**



具备controller身份的broker比其他普通的broker多一份职责

1. 监听broker的相关变化
2. 监听topic的相关变化
3. 从zk中读取当前所有topic、partition、broker的相关信息并进行响应的管理
4. 更新集群的元数据信息，同步到其他普通的broker节点中



#### 分区follower副本选举leader机制

controller感知到分区leader所在的broker挂了(controller监听了很多zk节点，可以感知到broker存活)之后，会根据设置来判断怎么选举leader

1. 参数unclean.leader.election.enable=false的前提下
   - controller会从ISR列表中挑选第一个broker作为leader(第一个broker最先放进ISR列表，可能是同步数据最多的副本)
2. 参数unclean.leader.election.enable为true
   - 代表ISR列表里面所有副本都挂了的时候，可以在ISR列表以外的副本中选举leader
   - 这种设置可以提高可用性，但是，选举出的新leader可能数据少很多







### 分区副本分配规则

假如kafka broker3个节点，设置kafka分区数大于服务器台数，kafka底层如何分配存储副本？





### Broker文件存储、消息格式

topic是一个逻辑上的概率，partition是物理上的概念，**每一个partition对应于一个log文件**，该log文件中存储的就是Producer生产的数据。

Producer生产的数据会不断追加到该log文件末端。是一个虚拟概念。

为了防止log过大导致数据定位效率低下，kafka采取了**分片和索引机制**，将每个partition分为多个segment。每个segment包括：.index文件，.log文件，.timeindex文件等。

这些文件位于一个文件夹下，该文件夹命名规则：topic名称+分区序号

例如：有一个名为firstTopic包含3个分区，那么在kafka的数据目录下就有3个文件夹/tmp/kafka-log/firstTopic01    02   03



一个topic可分为多个分区，一个分区可分为多个segment

segment默认1G，里面包含：.log文件 .index偏移量索引文件 .timeindex时间戳索引文件等等



log文件：记录消息的

索引文件：用来保存消息的索引

index和log文件以当前segment的第一条消息的offset命名



好处：采用分片和分段的策略，避免了数据量多大时，数据文件无限扩张带来的隐患，更有助于消息文件的维护以及被消费的消息的处理



注意：

1. index为稀疏索引，大约每往log写入4k，会往index文件写入一条索引
2. index文件中保存的offset为相对offset，这样能确保offset的值所占空间不会过大，因此能将offset的值控制在固定大小



索引文件与日志文件的关系：

![image-20230319150251003](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202303191502668.png)

如何在log中(分区中)定位到offset=xxx的记录？

1. 根据目标offset定位segment段中的索引(index)文件
   - 由于索引文件命名是以上一个文件的最后一个offset 进行命名的
   - 所以，使用二分查找算法能够根据offset 快速定位到指定的索引文件
2. 找到索引文件后，根据offset进行定位，找到**索引文件中匹配范围的偏移量position(位置)**
   - kafka 采用稀疏索引的方式来提高查找性能
3. 得到位置之后，再到对应的log文件中，从position处开始查找offset对应的消息，将每条消息的offset与目标offset进行比较，直到找到消息

![image-20220426212527332](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204262125861.png)





### Broker文件清除策略与压缩策略

**kafka中默认的日志保存时间为7天**，可以通过参数进行调整

kafka提供的日志清理策略有delete和compact

1. delete：日志删除，将过期数据删除
   - log.cleanup.policy=delete，所有数据启用删除策略
   - 基于时间：默认打开，以segment中所有记录中的最大时间戳作为该文件的时间戳
   - 基于文件大小：默认关闭，超过设置的日志总大小，则删除最早的segment
2. compact：日志压缩
   - 对应相同key的不同value值，只保留最后一个版本



### 高效读写数据（明天还要看一下）

kafka读写为什么那么快？

#### 1. kafka本身是分布式集群，利用分区技术，实现并行处理



#### 2. 顺序写磁盘

kafka的消息是保存在磁盘上的，那么磁盘的读写效率尤为重要。磁盘的读写速度的快慢取决于是顺序读写或随机读写。

- kafka的producer生产数据，要写入log文件中，写的过程一直追加到文件末端，为顺序写。其顺序写能达到600M/s
- 顺序读写省去了大量的磁头寻址时间

这样设计的缺陷是：没有办法直接删除数据。但是，由于磁盘有限，不可能保存所有数据，实际上作为消息系统 Kafka 也没必要保存所有数据，需要删除旧的数据。又由于顺序写入的原因，所以 Kafka 采用各种删除策略删除数据的时候，并非通过使用“读 - 写”模式去修改文件，而是将 Partition 分为多个 Segment，每个 Segment 对应一个物理文件，**通过删除整个文件的方式去删除 Partition 内的数据**。这种方式清除旧数据的方式，也避免了对文件的随机写操作



#### 3. 页缓存和零拷贝技术

##### 零拷贝

Kafka 中存在大量的网络数据持久化到磁盘（Producer 到 Broker）和磁盘文件通过网络发送（Broker 到 Consumer）的过程。这一过程的性能直接影响 Kafka 的整体吞吐量。

操作系统的核心是内核，独立于普通的应用程序，可以访问受保护的内存空间，也有访问底层硬件设备的权限。

为了避免用户进程直接操作内核，保证内核安全，操作系统将虚拟内存划分为两部分，一部分是内核空间（Kernel-space），一部分是用户空间（User-space）。

传统的 Linux 系统中，标准的 I/O 接口（例如read，write）都是基于数据拷贝操作的，即 I/O 操作会导致数据在内核地址空间的缓冲区和用户地址空间的缓冲区之间进行拷贝，所以标准 I/O 也被称作缓存 I/O。这样做的好处是，如果所请求的数据已经存放在内核的高速缓冲存储器中，那么就可以减少实际的 I/O 操作，但坏处就是数据拷贝的过程，会导致 CPU 开销。

##### PageCache页缓存



1. 页缓存+零拷贝技术
   - 零拷贝：kafka的数据加工处理操作交由kafka生产者和kafka消费者处理，kafka broker应用层不关心存储的数据，所以不用走应用层，传输效率高
   - PageCache页缓存：kafka重度依赖底层操作系统提高的pagecache功能。当上层有写操作时，操作系统只是将数据写入pagecache，当读操作发生时，先从pagecache查找，如果找不到，再去磁盘中读取。
   - ![image-20220427205334342](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272053956.png)



---









## kafka消费者



### 消费者客户端API

核心类：

KafkaConsumer：消费者对象，用来消费数据

ConsumerConfig：设置消费者的一系列配置参数

kafkaConsumer.poll(Duration)：消费者客户端核心方法，即从服务器拉取消息



### kafka消费方式

#### push方式

broker集群推送消息给消费者

kafka没有采用这种方式，因为：这种方式是由broker决定消息发送速率，很难适应消费者的消费速率



#### pull方式

> kafka  consumer采用从broker中主动拉取数据。kafka采用这种方式，根据consumer的消费能力以适当的速率消费消息

![image-20220427205659931](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272057175.png)

缺点：如果kafka没有数据，那么消费者可能会陷入循环中，一直返回空的数据。针对这点不足，kafka的消费者可以在消费数据时，传入一个时长参数timeout，如果当前没有数据可供消费，consumer则会等待一段时间后再返回



### 消费者工作流程

![image-20220427210633676](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272106921.png)

offset放在zookeeper 的consumer节点中

offset为什么移除zookeeper？

如果所有消费者都把offset存在zk中，则所有消费者需要于zk进行大量的交换，会导致网络数据传输频繁，所以存放在系统主题中，方便管理



### 消费者组原理

消费组(Consumer Group)是Kafka的消费理念中一种特有的概念，每个消费者都属于一个消费组。生产者的消息发布到主题后，只会被投递给订阅该主题的每个消费组中的一个消费者。消费者组内每个消费者负责消费不同分区的数据，**一个分区只能由一个消费者组内的消费者消费**；所有的消费者都有一个与之对应的消费者组，即消费者组是逻辑上的一个订阅者。**消费者组之间互不影响，多个不同的消费者组可以同时订阅一个Topic**，此时消息会同时被每个消费者组中一个消费者消费。



实际生产中，一般分区数和消费者数保持相等，如果这个主题的消费者数大于主题的分区数，那么多出来的消费者将消费不到数据，只能浪费系统资源。



Consumer Grop：由多个consumer组成，形成一个消费者组

1. 消费者组内每个消费者负责消费不同分区的数据，一个主题的分区只能由一个组内的消费者消费
2. 消费者组之间互不影响

![image-20220427211607003](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272116428.png)





![image-20220427211716358](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272117602.png)



### 消费者组初始化流程

![image-20220427215200765](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272152197.png)





### 消费者组消费流程

![image-20220427215650700](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272156995.png)





### 独立消费者-消费主题

例子：一个独立消费者，消费first主题。其中first主题有3个分区（每一个分区都在一台broker上）

~~~java
//0、配置
	连接kafka、反序列化方式、消费者组id
//1、创建消费者
     
//2、订阅主题
        kafkaconsumer.subscribe("first")
//3、消费数据
        while(true){
            if(flag==true){
                break;
            }
     ConsumerRecords<String,String>cr=kafkaconsumer.poll();
        }
~~~



### 独立消费者-消费分区

例如：消费first主题的0号分区

~~~java
//0、配置
	连接kafka、反序列化方式、消费者组id
//1、创建消费者
     kafkaconsumer=
        new KafkaConsumer<String,String>(properties);
//2、订阅主题对应的分区
	Collection<TopicPartition>  topicPartition;
	topicPartition.add(new TopicPartition("first",0));
     kafkaconsumer.assign(topicPartition)
//3、消费分区的数据
        while(true){
            if(flag==true){
                break;
            }
  ConsumerRecords<String,String>cr=kafkaconsumer.poll(xxx);
        }
~~~





### 消费者组 消费

例如：同一个主题的分区数据，只能由一个消费者组中的一个消费

多个消费者，配置在一个组里面



### 消费者分区的分配以及再平衡（rebalance)

> kafka再平衡机制：在再平衡过程中，消费者无法从kafka消费消息，这对kafka的TPS影响是非常大的，这段时间内kafka基本处于不可用状态，所有，应该尽量避免再平衡发生



到底由哪个消费者来消费哪个分区？

**消费者不用选择分区，消费组会帮消费者指定分区**



![image-20220427225649493](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272256859.png)



#### Range分区及再平衡

> 再平衡默认策略：Range



当执行rebalance的时候，消费者组中所有消费者实例都会停止对消息的消费，直到rebalance完成。因此，要尽可能地避免rebalance

range时候的再平衡：有消费者挂了，它的任务交给其他consumer

![image-20220427230004999](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272300268.png)



![image-20220427230018304](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204272300542.png)

Range范围分区的弊端：如上图，随着topic的数量增多，那么C0消费者会出现多消费n个分区，导致部分消费者过载



#### RoundRobin分区及再平衡 

轮询分区策略

轮询分区分为以下2种情况：

1. 同一消费组内所有消费者订阅的topic都是相同的
2. 同一消费者组内的消费者订阅的消息有不相同的

![image-20220428201322929](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282013691.png)



#### Sticky 以及再平衡

黏性分区：在执行一次新的分配之前，考虑上一次分配的结果，尽量少的调整分配的变动，可以节省大量的开销

是kafka 0.11x版本开始引入的分配策略，首先会尽量均衡的放置分区到消费者上面，在出现同一消费者组内消费者出现问题的时候，会尽量保持原有分配的分区不变化



#### 什么情况下会发生rebalance

1. 消费者组中的消费者实例发生变化
2. 订阅的主题发生变化
3. 主题的分区发生变化



#### 避免再平衡

要完全避免再平衡那是不可能的，但是，消费者故障是最为常见的引发再平衡的地方，所以要尽可能的避免消费者故障

首先，若消费者真的挂掉了那也是没有办法的。实际场景中，kafka可能错误的认为一个正常的消费者挂掉了，我们要解决的是避免这种情况出现

哪些情况会出现kafka错误判断消费者挂掉的情况：

> 分布式系统中，通常是通过心跳来维持分布式系统的连接的，kafka也是采用心跳机制来判断是否和Kafka broker连接

1. 由于网络原因没有收到心跳，不知道对方是因为负载过重没来得及反应心跳或网络阻塞，所以一般会约定一个时间，超时会判断对方挂掉了
2. kafka消费场景中
3. session.timeout.ms：group coordinator检测consumer发生崩溃所需的时间。一个consumer group里面的某个consumer挂掉了，最长需要 session.timeout.ms 秒检测出来。它指定了一个阈值—10秒，在这个阈值内如果group coordinator未收到consumer的任何消息（指心跳），那coordinator就认为consumer挂了
   - 项目中消费者配置的是：150000ms==>150s   ==>2分30秒
4. heartbeat.interval.ms：每个consumer 都会根据 heartbeat.interval.ms 参数指定的时间周期性地向group coordinator发送 hearbeat，group coordinator会给各个consumer响应，若发生了 rebalance，各个consumer收到的响应中会包含 REBALANCE_IN_PROGRESS 标识，这样各个consumer就知道已经发生了rebalance，同时 group coordinator也知道了各个consumer的存活情况。
5. max.poll.interval.ms：如果consumer两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，会将其踢出消费组，将分区分配给别的consumer消费 ，触发rebalance 
   - 项目中消费者配置的是：300000ms==>300s   ==>5分钟



所以避免设置好这几个参数，能很好的防止误判，导致再平衡







#### 心跳机制

每个分区会绑定一个消费者，消费者和topic之间有一个心跳机制。kafka的心跳是consumer和broker之间的健康检查，只有当broker coordinator(协调者)正常时，consumer才会发送心跳。也就是说，消费者和broker之间保存着长连接，只有心跳正常时，才会进行消费



---

### 位移offset

对于Kafka 中的分区而言，它的每条消息都有唯一的offset ，用来表示消息在分区中对应的位置

对于消费者而言， 它也有一个offset 的概念，消费者使用offset 来表示消费到分区中某个消息所在的位置

**通常把消费者位移存储起来的动作称为提交**，消费者在消费完消息之后，需要执行消费位移的提交

### 消费者位移提交方式

![image-20220428201925433](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282019003.png)



_consumer_offsets主题里面采用k-v的方式存储数据。key是group.id+topic+分区号，v是当前offset的值，每隔一段时间，kafka内部会对这个topic进行compact，也就是每个group.id+topic+分区号就保留最新数据

在配置文件 config/consumer.properties中添加配置：exclude.internal.topics=false   默认为true，表示不能消费系统主题。为了能查看该系统主题数据，需要将该参数设置为false



消费过程中会产生offset存放在这个系统主题中

![image-20220428203023561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282030761.png)



#### 自动提交offset

为了使用户专注于自己的业务逻辑，kafka提供了自动提交offset的功能

自动位移提交的动作是在poll()方法里面完成的，每次向服务端发起拉取请求之前会检查是否可以进行位移提交，如果可以那么就会提交上一次轮询的位移。

**如果在下一次自动提交消费位移之前，消费者宕机了，那么又得从上一次位移提交的地方开始消费，导致了重复消费**

自动提交offset的相关参数：

1. enable.auto.commit：是否开启自动提交offset功能，默认true
2. auto.commit.interval.ms：自动提交offset的时间间隔，**默认5s**

~~~java
/**
 * 自动提交offset
 */
public class MyConsumer1 {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");
        //group.id相同的属于同一个消费者组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_test");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //自动提交offset,每1s提交一次
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList("topic_test"));
        //消费者启动死循环不断消费
        while (true) {
            //一旦拉取到数据就返回，否则最多等待duration设定的时间
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(), record.partition(),
                        record.offset(), record.key(), record.value());
            });
        }
    }
}

~~~



##### 自动提交offset实现原理

![image-20220428203358834](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282033218.png)



#### 手动提交offset

虽然自动提交offset方便，但是是基于时间提交的，开发人员难以把握offset提交的时机，因此可以通过手动提交offset。

**开发中，一般都是消费消费后，进行手动提交！！**

![image-20220428205244851](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282052076.png)



##### 手动提交offset的两种方式

1. commitSync：同步提交
   - 同步提交，会阻塞消费线程，直到当前批次offset提交成功
2. commitAsync：异步提交
   - 不会阻塞消费线程
   - 发送完offset请求后，就开始消费下一批数据了
   - 异步提交，可以带回调函数，线程不会阻塞

相同点：都会将本次提交的一批数据最高的偏移量提交

不同点：

1. 同步提交会阻塞当前线程，一直到提交成功，并且会自动失败重试。但由于不可控因素，也会出现提交失败
2. 异步提交没有失败重试机制，故有可能提交失败





#### 指定offset消费

当kafka没有初始偏移量(消费者组第一次消费)或服务器上不再存在当前偏移量时(例如该数据已被删除)该怎么办？

1. earliest：自动将偏移量重置为最早的偏移量，--from-beginning
2. latest：默认值，自动将偏移量重置为最新偏移量
3. none：如果未找到消费者组的先前偏移量，则向消费者抛出异常



#### 按照指定时间消费

遇到消费的数据异常，想重新按照指定时间消费

> 项目采用补偿方式：指定消费某个topic的指定partition key value







#### 可以自定义offset的存储位置

项目中，手动维护kafka偏移量，是将offset存在redis中的



 在Kafka中，offset默认存储在broker的内置Topic中，我们可以自定义存储位置。比如为了保证消费和提交偏移量同时成功或失败，我们可以利用数据库事务来实现，把offset存储在Mysql即可。下面的例子仅为示例代码，其中getOffset和commitOffset方法可以根据所选的offset存储系统(比如mysql)自行实现

~~~java
/**
 * 自定义offset提交
 * 在Kafka中，offset默认存储在broker的内置Topic中，我们可以自定义存储位置
 * 比如为了保证消费和提交偏移量同时成功或失败，我们可以利用数据库事务来实现，把offset存储在Mysql即可
 * 下面的例子仅为示例代码，其中getOffset和commitOffset方法可以根据所选的offset存储系统(比如mysql)自行实现
 */
public class MyConsumer4 {
    public static Map<TopicPartition, Long> currentOffset = new HashMap<>();

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");
        //group.id相同的属于同一个消费者组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_test");
        //关闭自动提交offset,手动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList("topic_test"), new ConsumerRebalanceListener() {
            //该方法会在Rebalanced之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                commitOffset(currentOffset);
            }

            //该方法会在Rebalanced之后调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                currentOffset.clear();
                for (TopicPartition partition : partitions) {
                    //定位到每个分区最近提交的offset位置继续消费
                    kafkaConsumer.seek(partition, getOffset(partition));
                }
            }
        });

        //消费者启动死循环不断消费
        while (true) {
            //一旦拉取到数据就返回，否则最多等待duration设定的时间
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(), record.partition(),
                        record.offset(), record.key(), record.value());
                currentOffset.put(new TopicPartition(record.topic(), record.partition()), record.offset());
            });

            //提交offset
            commitOffset(currentOffset);
        }
    }

    /**
     * 获取某分区最新的offset
     *
     * @param partition
     * @return
     */
    private static long getOffset(TopicPartition partition) {
        return 0;
    }

    /**
     * 提交该消费者所有分区的offset
     *
     * @param currentOffset
     */
    private static void commitOffset(Map<TopicPartition, Long> currentOffset) {
    }
}

~~~





### 消费者拦截器

kafka consumer在poll()方法返回之前，会先调用拦截器的onConsume()方法，可以在此方法里预先对消息进行定制化操作。kafka consumer在提交完消费位移之后，会调用拦截器的onCommit()方法



自定义拦截器，实现ConsumerInterceptor，并在props里面配置即可





### 漏消费和重复消费

![image-20220428215121984](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282151370.png)



如何做到既不漏消费也不重复消费？ 解决：消费者事务



### 消费者事务

kafka的消费者事务和数据库的ACID不是同一类东西

kafka事务指的是：一系列的生产者生产消息，和消费者提交偏移量的操作在一个事务中，或者说是一个原子操作，生产消息和提交偏移量同时成功和失败

![image-20220428215447661](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282154017.png)

简单的思路：

1. 利用consumer api的seek方法可以指定offset进行消费，在启动消费者时查询数据库中记录的offset信息，如果是第一次启动，那么数据库中将没有offset信息，需要进行消费的元数据插入，然后从offset=0开始消费
2. 关系型数据库具备事务的特性，当数据入库时，同时也将offset信息更新，借用关系型数据库事务的特性保证数据入库和修改offset记录这两个操作是在同一个事务中进行
3. 使用ConsumerRebalanceListener来完成在分配分区时和Relalance时作出相应的处理逻辑

---



### 消费者 数据积压

消费者如何提高吞吐量 问题

![image-20220428215818999](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282158353.png)













## kafka监控

kafka-Eagle框架可以监控

kafka-eagle.org   最新名字：efak



## kafka-Kraft模式

已经可以不需要Zookeeper了，但是是兼容的

kafka2.8x后

![image-20220428221103795](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202204282211144.png)









## kafka调优



### 硬件配置

场景：100万日活，每人每天100条日志，每天总共日志的数量为100*100万   ==1亿

处理日志速度：总量/（24*3600s)   =1150条/s

一条日志大小：正常为0.5-2k   按照1k进行计算

1150*1k/s  =  大于等于 1m/s 

高峰值：（中午小高峰、晚上8-12高峰期）* 平均值的20倍  =20m/s

#### 购买多少台kafka服务器？

服务器台数=2*（生产者峰值生产速率 * 副本数量 / 100） +1

​				=2* （20m/s *2 /100)+1  =3

#### 磁盘选择

kafka按照顺序读写

​	机械和固态硬盘的 顺序读写速度差不多

随机读写 选择固态硬盘

1亿条*1k   =100g

100g*2个副本  * 保存3天  / 0.7的磁盘容量 不要装满了   ==1T

#### 内存选择

kafka 内存= kafka堆内存（kafka内部配置） +页缓存（服务器的内存）

堆内存：10-15g

页缓存：segment(1g)   分区数Leader(假如10个分区)  *  1g  *  25%  =1g





---



## 面试

### 消息队列连环炮

#### 项目中怎么用消息队列的（我们公司用的kafka)

上下游传递数据(利用kafka实现了一套消息发布/订阅机制)、日志发送kafka然后采用ELK那一套来对日志进行处理、前后端异步消息



---



#### 为什么这些场景中，系统要用到消息队列

回答：什么业务场景、不用的影响、用之后的好处

公司kafka：异步场景，上游数据发送到kafka里面，下游监听kafka然后获取数据

主要应用消息队列的场景：解耦、异步、削峰

##### 解耦

A系统发送个数据到BCD三个系统，接口调用发送，那如果E系统也要这个数据呢？那如果C系统现在不需要了呢？现在A系统又要发送第二种数据了呢？A系统负责人濒临崩溃中。。。再来点更加崩溃的事儿，A系统要时时刻刻考虑BCDE四个系统如果挂了咋办？我要不要重发？我要不要把消息存起来？

不用mq的场景：

![image-20220503150927971](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031509395.png)

面试技巧：需要去考虑一下你负责的系统中是否有类似的场景，就是一个系统或者一个模块，调用了多个系统或者模块，互相之间的调用很复杂，维护起来很麻烦。**但是其实这个调用是不需要直接同步调用接口的，如果用MQ给他异步化解耦，也是可以的**，你就需要去考虑在你的项目里，是不是可以运用这个MQ去进行系统的解耦。在简历中体现出来这块东西，用MQ作解耦

 使用mq之后：

![image-20220503151953898](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031519126.png)



##### 异步

不用mq的同步高延时请求场景

![image-20220503152644584](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031526803.png)

A系统接收一个请求，需要在自己本地写库，还需要在BCD三个系统写库，自己本地写库要3ms，BCD三个系统分别写库要300ms、450ms、200ms。最终请求总延时是3 + 300 + 450 + 200 = 953ms，接近1s，用户感觉搞个什么东西，太慢了



使用mq后进行异步化之后：

![image-20220503160632232](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031606430.png)



##### 削峰

没有mq的时候，高峰期系统被打死场景：

![image-20220503161848712](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031618932.png)

每天0点到11点，A系统风平浪静，每秒并发请求数量就100个。结果每次一到11点~1点，每秒并发请求数量突然会暴增到1万条。但是系统最大的处理能力就只能是每秒钟处理1000个请求啊。。。尴尬了，系统会死

使用mq后：

![image-20220503163046599](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031630784.png)

---



#### 如果用到消息队列，那么他的优点和缺点是什么

优点：上面的解耦、异步、削峰

缺点：

![image-20220503164555603](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205031645871.png)

1. 系统可用性降低：系统引入的外部依赖越多，越容易挂掉，本来你就是A系统调用BCD三个系统的接口就好了，现在ABCD四个系统好好的，没啥问题，但是加入的MQ万一MQ挂了咋整？MQ挂了，整套系统崩溃了，你不就完了么。
2. 系统复杂性提高：硬生生加个MQ进来，你怎么保证消息没有重复消费？怎么处理消息丢失的情况？怎么保证消息传递的顺序性？头大头大，问题一大堆，痛苦不已
3. 一致性问题：A系统处理完了直接返回成功了，用户以为你这个请求就成功了；但是问题是，要是BCD三个系统那里，BD两个系统写库成功了，结果C系统写库失败了，咋整？你这数据就不一致了

 所以消息队列实际是一种非常复杂的架构，你引入它有很多好处，但是也得针对它带来的坏处做各种额外的技术方案和架构来规避掉，最好之后，你会发现，妈呀，系统复杂度提升了一个数量级，也许是复杂了10倍。但是关键时刻，用，还是得用的。。。

---



#### kafka、activemq、rabbitmq、rocketmq各有什么区别

activemq：一般的业务系统要引入MQ，最早大家都用ActiveMQ，但是现在确实大家用的不多了，没经过大规模吞吐量场景的验证，社区也不是很活跃，所以大家还是算了吧，不推荐用这个

rabbitmq：后来大家开始用RabbitMQ，但是确实erlang语言阻止了大量的java工程师去深入研究和掌控他，对公司而言，几乎处于不可控的状态，但是其是开源的，有比较稳定的支持，活跃度也高；

推荐下面两个：

rocketmq：阿里的。中小型公司，技术实力较为一般，技术挑战不是特别高，用RabbitMQ是不错的选择；大型公司，基础架构研发实力较强，用RocketMQ是很好的选择

kafka：如果是大数据领域的实时计算、日志采集等场景，用Kafka是业内标准的，绝对没问题，社区活跃度很高，绝对不会黄，何况几乎是全世界这个领域的事实性规范



考验面试者的宽度和深度

| 特性                    | ActiveMQ                                                     | RabbitMQ                                                     | RocketMQ                                                     | Kafka                                                        |
| ----------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 单机吞吐量              | 万级，吞吐量比RocketMQ和Kafka要低了一个数量级                | 万级，吞吐量比RocketMQ和Kafka要低了一个数量级                | 10万级，RocketMQ也是可以支撑高吞吐的一种MQ                   | 10万级别，这是kafka最大的优点，就是吞吐量高。     一般配合大数据类的系统来进行实时数据计算、日志采集等场景 |
| topic数量对吞吐量的影响 |                                                              |                                                              | topic可以达到几百，几千个的级别，吞吐量会有较小幅度的下降     这是RocketMQ的一大优势，在同等机器下，可以支撑大量的topic | topic从几十个到几百个的时候，吞吐量会大幅度下降     所以在同等机器下，kafka尽量保证topic数量不要过多。如果要支撑大规模topic，需要增加更多的机器资源 |
| 时效性                  | ms级                                                         | 微秒级，这是rabbitmq的一大特点，延迟是最低的                 | ms级                                                         | 延迟在ms级以内                                               |
| 可用性                  | 高，基于主从架构实现高可用性                                 | 高，基于主从架构实现高可用性                                 | 非常高，分布式架构                                           | 非常高，kafka是分布式的，一个数据多个副本，少数机器宕机，不会丢失数据，不会导致不可用 |
| 消息可靠性              | 有较低的概率丢失数据                                         |                                                              | 经过参数优化配置，可以做到0丢失                              | 经过参数优化配置，消息可以做到0丢失                          |
| 功能支持                | MQ领域的功能极其完备                                         | 基于erlang开发，所以并发能力很强，性能极其好，延时很低       | MQ功能较为完善，还是分布式的，扩展性好                       | 功能较为简单，主要支持简单的MQ功能，在大数据领域的实时计算以及日志采集被大规模使用，是事实上的标准 |
| 优劣势总结              | 非常成熟，功能强大，在业内大量的公司以及项目中都有应用     偶尔会有较低概率丢失消息     而且现在社区以及国内应用都越来越少，官方社区现在对ActiveMQ 5.x维护越来越少，几个月才发布一个版本     而且确实主要是基于解耦和异步来用的，较少在大规模吞吐的场景中使用 | erlang语言开发，性能极其好，延时很低；     吞吐量到万级，MQ功能比较完备     而且开源提供的管理界面非常棒，用起来很好用     社区相对比较活跃，几乎每个月都发布几个版本分     在国内一些互联网公司近几年用rabbitmq也比较多一些     但是问题也是显而易见的，RabbitMQ确实吞吐量会低一些，这是因为他做的实现机制比较重。     而且erlang开发，国内有几个公司有实力做erlang源码级别的研究和定制？如果说你没这个实力的话，确实偶尔会有一些问题，你很难去看懂源码，你公司对这个东西的掌控很弱，基本职能依赖于开源社区的快速维护和修复bug。     而且rabbitmq集群动态扩展会很麻烦，不过这个我觉得还好。其实主要是erlang语言本身带来的问题。很难读源码，很难定制和掌控。 | 接口简单易用，而且毕竟在阿里大规模应用过，有阿里品牌保障     日处理消息上百亿之多，可以做到大规模吞吐，性能也非常好，分布式扩展也很方便，社区维护还可以，可靠性和可用性都是ok的，还可以支撑大规模的topic数量，支持复杂MQ业务场景     而且一个很大的优势在于，阿里出品都是java系的，我们可以自己阅读源码，定制自己公司的MQ，可以掌控     社区活跃度相对较为一般，不过也还可以，文档相对来说简单一些，然后接口这块不是按照标准JMS规范走的有些系统要迁移需要修改大量代码     还有就是阿里出台的技术，你得做好这个技术万一被抛弃，社区黄掉的风险，那如果你们公司有技术实力我觉得用RocketMQ挺好的 | kafka的特点其实很明显，就是仅仅提供较少的核心功能，但是提供超高的吞吐量，ms级的延迟，极高的可用性以及可靠性，而且分布式可以任意扩展     同时kafka最好是支撑较少的topic数量即可，保证其超高吞吐量     而且kafka唯一的一点劣势是有可能消息重复消费，那么对数据准确性会造成极其轻微的影响，在大数据领域中以及日志采集中，这点轻微影响可以忽略     这个特性天然适合大数据实时计算以及日志收集 |



---



#### 如何保证消息队列高可用？

mq挂了之后，可能会导致系统崩溃

mq的高可用是必问题目

这个问题这么问是很好的，因为不能问你kafka的高可用性怎么保证啊？ActiveMQ的高可用性怎么保证啊？一个面试官要是这么问就显得很没水平，人家可能用的就是RabbitMQ，没用过Kafka，你上来问人家kafka干什么？这不是摆明了刁难人么

面试者：用过哪个消息队列，就回答哪个



##### RabbitMq的高可用性

Rabbitmq不是分布式的，有三种模式：单机、普通集群、镜像集群模式

1. 单机 
2. 普通集群模式
3. 镜像集群模式

##### Kafka的高可用性

kafka是分布式架构的mq

![image-20220503205204699](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205032052899.png)



kafka一个最基本的架构认识：多个broker组成，每个broker是一个节点；你创建一个topic，这个topic可以划分为多个partition，每个partition可以存在于不同的broker上，每个partition就放一部分数据。

这就是天然的分布式消息队列，就是说一个topic的数据，是分散放在多个机器上的，每个机器就放一部分数据。

 实际上rabbitmq之类的，并不是分布式消息队列，他就是传统的消息队列，只不过提供了一些集群、HA的机制而已，因为无论怎么玩儿，rabbitmq一个queue的数据都是放在一个节点里的，镜像集群下，也是每个节点都放这个queue的完整数据。

 

kafka 0.8以前，是没有HA(高可用)机制的，就是任何一个broker宕机了，那个broker上的partition就废了，没法写也没法读，没有什么高可用性可言。

 kafka 0.8以后，提供了HA机制，就是replica副本机制。每个partition的数据都会同步到其他机器上，形成自己的多个replica副本。然后所有replica会选举一个leader出来，那么生产和消费都跟这个leader打交道，然后其他replica就是follower。写的时候，leader会负责把数据同步到所有follower上去，读的时候就直接读leader上数据即可。只能读写leader？很简单，要是你可以随意读写每个follower，那么就要care数据一致性的问题，系统复杂度太高，很容易出问题。kafka会均匀的将一个partition的所有replica分布在不同的机器上，这样才可以提高容错性。

 

这么搞，就有所谓的高可用性了，因为如果某个broker宕机了，没事儿，那个broker上面的partition在其他机器上都有副本的，如果这上面有某个partition的leader，那么此时会重新选举一个新的leader出来，大家继续读写那个新的leader即可。这就有所谓的高可用性了。

 写数据的时候，生产者就写leader，然后leader将数据落地写本地磁盘，接着其他follower自己主动从leader来pull数据。一旦所有follower同步好数据了，就会发送ack给leader，leader收到所有follower的ack之后，就会返回写成功的消息给生产者。（当然，这只是其中一种模式，还可以适当调整这个行为）

 消费的时候，只会从leader去读，但是只有一个消息已经被所有follower都同步成功返回ack的时候，这个消息才会被消费者读到。

---



#### 如何保证消息不被重复消费？(如何保证消费的时候的幂等)

![image-20220503214357393](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205032143663.png)

kafka有个offset的概念，就是每个消息写进去，都有一个offset，代表他的序号，然后consumer消费了数据之后，每隔一段时间，会把自己消费过的消息的offset提交一下，代表我已经消费过了，下次我要是重启啥的，你就让我继续从上次消费到的offset来继续消费吧。

 但是凡事总有意外，比如有时候重启系统，看你怎么重启了，如果碰到点着急的，直接kill进程了，再重启。这会导致consumer有些消息处理了，但是没来得及提交offset，尴尬了。重启之后，少数消息会再次消费一次。



 **其实重复消费不可怕，可怕的是你没考虑到重复消费之后，怎么保证幂等性。**

举个例子：假设你有个系统，消费一条往数据库里插入一条，要是你一个消息重复两次，你不就插入了两条，这数据不就错了？但是你要是消费到第二次的时候，自己判断一下已经消费过了，直接扔了，不就保留了一条数据？一条数据重复出现两次，数据库里就只有一条数据，这就保证了系统的幂等性

 

**如何解决mq重复消费的幂等性？**

如何保证MQ的消费是幂等性的，需要结合具体的业务来看

其实还是得结合业务来思考，这里给几个思路：

1. 比如你拿着数据要写库，你先根据主键查一下，如果这数据都有了，你就别插入了，update一下好吧
2. **比如你是拿着数据写redis，那没问题了，反正每次都是set，天然幂等性**
   - 项目中就是：先把topic对应的partition和offset写入一个带分数的set中，再去手动Ack
   - 从而保证了重复消费！！！！！
3. **比如你不是上面两个场景，那做的稍微复杂一点，你需要让生产者发送每条数据的时候，里面加一个全局唯一的id，类似订单id之类的东西，然后你这里消费到了之后，先根据这个id去比如redis里查一下，之前消费过吗？如果没有消费过，你就处理，然后这个id写redis。如果消费过了，那你就别处理了，保证别重复处理相同的消息即可**。
4. 第三点好像项目中是这样做的！！！明天看一下！！！



还有比如基于数据库的唯一键来保证重复数据不会重复插入多条，我们之前线上系统就有这个问题，就是拿到数据的时候，每次重启可能会有重复，因为kafka消费者还没来得及提交offset，重复数据拿到了以后我们插入的时候，因为有唯一键约束了，所以重复数据只会插入报错，不会导致数据库中出现脏数据

![image-20220503215808037](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205032158231.png)

---



#### 消息队列如何保证消息的可靠性传输、如何处理消息丢失的问题

kafka而言：

1. **消费者端**弄丢了数据
   - **唯一可能丢失数据的情况是：消费到了消息，消费者也自动提交了offset，kafka认为这个消息已经消费了，但是此时消费者还没有处理这个消息就挂了，导致数据丢失**
   - 解决方案：**关闭kafka的自动提交offset，消费者处理完消息后进行手动提交，从而保证消息不丢失。**但是有可能导致重复消费，因为可能消费者在手动提交offset的时候宕机，还没有提交offset。后面肯定会重复消费一次，消费者保证幂等性就可以了！！！！！
   - 还有一种情况：就是kafka消费者消费到了数据之后是写到一个内存的queue里先缓冲一下，结果有的时候，你刚把消息写入内存queue，然后消费者会自动提交offset。然后此时我们重启了系统，就会导致内存queue里还没来得及处理的数据就丢失了
2. kafka broker丢失了数据
   - 比较常见的场景：某个leader收到数据后，还没有进行数据同步，此时leader挂了，导致新选举的follower变为leader，但是此时数据并没有同步过来导致数据丢失
   - ![image-20220504165124796](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205041651988.png)
   - 解决方案：4个参数的设置
     1. 这个topic设置replication.factor参数，必须大于1。也就是每个partition必须拥有2个副本以上
     2. 服务端设置：min.insync.replicas参数，必须大于1。这个是要求一个leader至少感知到有至少一个follower还跟自己保持联系，没掉队，这样才能确保leader挂了还有一个follower
     3. 设置ack=all，这个是要求每条数据，必须是写入所有replica之后，才能认为是写成功了
     4. 生产者端设置：retries=max(是一个很大很大的值，可以理解为无限次重试的意思)，这个是要求一旦写入失败，进行重试写入
   - 这样配置之后，至少在kafka broker端就可以保证在leader所在broker发生故障，进行leader切换时，数据不会丢失
3. 生产者会不会丢失数据
   - ack=all，一定不会丢失
   - 因为leader收到消息后，所有的follower都同步了消息后才认为本次写成功了，如果不满足这个条件，那么生产者会进行重试，可以重试无限次



---



#### 如何保证消息的顺序传输？

顺序错乱的场景：

kafka：一个topic，一个partition，一个consumer，内部多线程导致顺序混乱



![image-20220504201135687](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205042011857.png)



kafka写入一个partition中的数据一定是有顺序的

kafka如何保证顺序传输？

一个topic，一个partition，一个consumer，内部单线程消费，写N个内存queue，然后N个线程分别消费一个内存queue即可

![image-20220504202608347](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205042026706.png)

---



#### 如何保证消息队列的延时以及过期失效问题？

生产者将消息发送到消息队列后，并不希望立马投递这条消息，而是推迟到某个时间点之后将消息投递给消费者进行消费

https://zhuanlan.zhihu.com/p/339459783



#### 消息队列满了后怎么处理？大量消息持续积压几小时，怎么解决？

> 1. 消息积压问题追溯
> 2. 开发消息积压的告警程序
> 3. 性能优化



问题本质：消费端出问题了，大部分情况下都是消费端问题

> 若这种性能倒挂的问题只是暂时的，那问题不大，只要消费端的性能恢复之后，超过发送端的性能，那积压的消息是可以逐渐被消化掉的。
>
> 若消费速度一直比生产速度慢，久而久之，系统就会异常。 - 要么，MQ存储被填满无法提供服务 - 要么消息丢失



1. **先修复consumer的问题，确保其恢复消费速度**，然后将现有cnosumer都停掉
2. 新建一个topic，partition是原来的10倍，临时建立好原先10倍或者20倍的queue数量
3. 然后写一个临时的分发数据的consumer程序，这个程序部署上去消费积压的数据，消费之后不做耗时的处理，直接均匀轮询写入临时建立好的10倍数量的queue
4. 接着临时征用10倍的机器来部署consumer，每一批consumer消费一个临时queue的数据
5. 这种做法相当于是临时将queue资源和consumer资源扩大10倍，以正常的10倍速度来消费数据
6. 等快速消费完积压数据之后，得恢复原先部署架构，重新用原先的consumer机器来消费消息

![image-20220504204021818](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205042040994.png)

---



#### 如果自己实现一个消息队列，应该如何设计架构？

开放性的题目

1. 考虑mq支持可伸缩性，就是需要的时候快速扩容
   - 设计个分布式的系统
   - kafka的设计理念，broker -> topic -> partition，每个partition放一个机器，就存一部分数据。如果现在资源不够了，简单啊，给topic增加partition，然后做数据迁移，增加机器，不就可以存放更多数据，提供更高的吞吐量了
2. 考虑mq的数据是否需要落盘
   - 顺序写，这样就没有磁盘随机读写的寻址开销，磁盘顺序读写的性能是很高的，这就是kafka的思路
3. 考虑mq的可用性
   - kafka的高可用保障机制。多副本 -> leader & follower -> broker挂了重新选举leader即可对外服务
4. 能不能支持数据的0丢失等待
   - kafka数据零丢失方案

这些都可以参考kafka的实现







---



## KafkaTemplate

生产用的

项目中可以注入多个KafkaTemplate实例，例如配置k-v序列化方式不同时，采用泛型依赖注入。 

底层依赖于DefaultKafkaProducerFactory，采用工厂方式生成Producer

~~~java
@Autowired
KafkaTemplate<String,String> kafkaTemplate1;

@Autowired
KafkaTemplate<byte[],byte[]> kafkaTemplate2;
~~~

kafkaTemplate底层实现：send方法发送消息给kafka，底层都会调用doSend方法，消息发送完会进行callback回调并关闭生产者，刷新缓存

底层用Sender线程来跑的





## @KafkaListener

消费用的

spring-kafka项目进行了集成kafka

kafkaListener原理：

Spring初始化的过程中，会执行所有实现了BeanPostProcessor接口的postProcessBeforeInitialization、postProcessAfterInitializationr方法

每个注解了KafkaListener的方法都会在KafkaListenerAnnotationBeanPostProcessor中，随之spring的启动，创建相应的kafkaMessageListenerContainer

KafkaMessageListenerContainer中创建了一个线程，这个线程new一个KafkaConsumer对象并执行poll方法进行消费



公司配置实现

containerFactory 监听容器工厂，当监听时需要区分单数据还是多数据消费时，需要配置containerFactory属性

公司实现了批量消费

![image-20220507222340054](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/kafka_img/202205072231174.png)



~~~java
  @Bean
    public KafkaListenerContainerFactory<?>xxxxxxBatchFactory() {
        ConcurrentKafkalistenerContainerFactory<String, String > factory = new ConcurrentKafkalistenerContainerFactory<>();
        //自定义的一个工厂
        factory.setConsumerFactory(xxxxConsumerFactory());
        //创建多少个KafkaListenerContainer实例，一个实例分配一个分区进行消费
        //若设置为1则代表这个实例消费topic的所有分区  
        factory.setConcurrency(Integer.parseInt(env.getProperty("spring.kafka.template.concurrency","1")));
        
        factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置

        //设置提交偏移量的方式
ConsumerConfig.MAX_POLL_RECORDS_CONFI6factory.getContainerProperties().setAckMode(AckMode.MANUAL);//MANUAL_IMMEDIATE);

        return factory;

    }


public ConsumerFactory<Object,Object> xxxxConsumerFactory(){
    return new DefaultKafkaConsumerFactory<xxxConsumerConfig>;
}
~~~



~~~java
    public Map<String, Object> xxxConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.ENABLEAUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, env.getProperty("spring.kafka.max.poll.interval.ms", "300000"));
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, env.getProperty("spring.kafka.request.timeout.ms", "150000"));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, env.getProperty(
                "spring.kafka.auto.commit.interval.ms",
                "10000"));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, env.getProperty("spring.kafka.session.timeout.ms", "150000"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, env.getProperty("spring.kafka.max.poll.records", "1000"));//每一批数量
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("spring.kafka.auto.offset.reset", "earliest"));
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, env.getProperty("spring.fetch.min.bytes", "10000000"));//10M
        KafkaSaalUtils.saalConfig(props, env);
        return props;
    }
~~~





































