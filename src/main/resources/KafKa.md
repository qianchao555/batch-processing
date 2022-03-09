## KafKa

[参考文章](https://www.w3cschool.cn/apache_kafka/)

### 定义、简介

1. kafka是一个分布式的基于发布/订阅模式的消息队列，可以处理大量的数据，并使你能够将消息从一个端点传递到另一个端点
2. Kafka消息保留在磁盘上，并在集群内复制以防止数据丢失，Kafka构建在Zookeeper同步服务之上
3. 主要应用于大数据实时处理领域

### 术语

#### 生产者、消费者

1. 消息的发送者叫Producer，消息的使用者/接收者是Consumer
2. 生产者将数据保存到Kafka集群中，消费者从中获取消息进行业务的处理
3. ![img](https://atts.w3cschool.cn/attachments/day_200916/202009161733109884.png)

#### broker

1. broker    n:代理、中间人   v:协调、安排
2. Kafka集群中有很多台Server，其中每一台Server都可以存储消息，将每一台Server称为一个Kafka实例，也叫做broker

#### Topic

1. topic(主题)：一个topic里保存的是同一类消息，相当于对消息的分类
2. 每个producer将消息发送到kafka中，都需要指明要存的topic是哪个，也就是指明这个消息属于哪一类

#### Partition

partition：分区

1. 每个topic都可以分成多个partition，每个partition在存储层面是append log文件
2. 任何发布到此partition的消息都会被直接追加到log文件的尾部
3. 为什么要进行分区：最根本原因是kafka基于文件进行存储，当文件内容大到一定程度时，很容易达到单个磁盘的上限，因此采用分区的方法，一个分区对应一个文件，这样就可以将数据分别存储到不同的server上去。这样做也可以负载均衡，容纳更多的消费者

#### 偏移量Offset

1. 一个分区对应磁盘上的一个文件，而消息在文件中的位置就称为offset，offset是一个long型数字，它可以唯一标记一条消息
2. 由于kafka没有提供其他额外的索引机制来存储offset，文件只能顺序的读写，所以在kafka中几乎不允许对消息进行"随机读写"

 综上，我们总结一下 Kafka 的几个要点:

- kafka 是一个基于发布-订阅的分布式消息系统（消息队列）
- Kafka 面向大数据，消息保存在主题中，而每个 topic 有分为多个分区
- kafka 的消息数据保存在磁盘，每个 partition 对应磁盘上的一个文件，消息写入就是简单的文件追加，文件可以在集群内复制备份以防丢失
- 即使消息被消费，kafka 也不会立即删除该消息，可以通过配置使得过一段时间后自动删除以释放磁盘空间
- kafka依赖分布式协调服务Zookeeper，适合离线/在线信息的消费，与 storm 和 spark 等实时流式数据分析常常结合使用

---



### 消息队列的两种模式

#### 点对点模式

1. 在点对点的消息系统中，消息保留在队列中，一个或者多个消费者可以消耗队列中的消息，但是消息最多只能被一个消费者消费，一旦有一个消费者将其消费掉，消息就从该队列中消失

#### 发布/订阅模式

1. 一对多，消费者消费数据之后，不会消除消息
2. 消息生产者（发布）将消息发布到topic中，同时又多个消息消费者（订阅）消费该消息
3. topic是一个队列
4. 这种模式下有两种消费方式
   - 消费者主动消费消息
   - topic主动推送给消费者

---



### Kafka基本原理

#### 分布式和分区

1. 我们说 kafka 是一个分布式消息系统，所谓的分布式，实际上我们已经大致了解。消息保存在 Topic 中，而为了能够实现大数据的存储，一个 topic 划分为多个分区，每个分区对应一个文件，可以分别存储到不同的机器上，以实现分布式的集群存储。另外，每个 partition 可以有一定的副本，备份到多台机器上，以提高可用性。
2. 一个 topic 对应的多个 partition ，分散存储到集群中的多个 broker 上，存储方式是一个 partition 对应一个文件，每个 broker 负责存储在自己机器上的 partition 中的消息读写

#### partition副本

1. kafka可以配置partition需要备份的个数(replicas)，每个partition将会被备份到多台机器上，以提高高可用性，备份的数量可以通过配置文件指定
2. 既然有副本，就涉及到对同一个文件的多个备份如何进行管理和调度。kafka 采取的方案是：每个 partition 选举一个 server 作为“leader”，由 leader 负责所有对该分区的读写，其他 server 作为 follower 只需要简单的与 leader 同步，保持跟进即可。如果原来的 leader 失效，会重新选举由其他的 follower 来成为新的 leader。如何选取：kafka所以zookeeper在broker中选出一个controller，用于partition分配和leader选举

#### 整体数据流

kafka总体数据流满足下图，下图基本上概括了整个kafka的基本原理

![img](https://atts.w3cschool.cn/attachments/day_200916/202009161733104250.jpg)

##### 数据生产过程

![img](https://atts.w3cschool.cn/attachments/day_200916/202009161733117119.png)

1. 对于生产者要写入的一条记录，可以指定四个参数：分别是topic、partition、key、value，其中topic和value(要写入的数据)是必须指定的
2. 对于一条记录，先对其进行序列化，然后按照topic和partition，放到对应的发送队列中。如果partition没有指定，那么情况会是这样的：
   - 指定了key，则按照key进行哈希，相同的key去一个partition
   - 没有指定key，Round-Robin来选partition
3. 生产者会和topic下所有partition leader保持socket连接，消息由生产者直接通过socket发送到broker。其中partition leader的位置(host:port)注册在zookeeper中，生产者作为zookeeper client，已经注册了watch用来监听partition leader的变更事件，因此可以准确的知道谁是当前的leader
4. 生产者端采用异步发送：将多条消息暂且在客户端buffer起来，并将他们批量的发送到broker。小数据I/O太多会拖慢整体的网络延迟，批量延迟发送提升了网络效率

##### 数据消费过程

![img](https://atts.w3cschool.cn/attachments/day_200916/202009161733117671.png)

1. 对于消息者，不是以单独的形式存在的，每一个消费者属于一个consumer group，一个group包含多个消费者
2. 注意：订阅topic是以一个消费组来订阅的，发送到topic的消息，只会被订阅此topic的每个group中的一个consumer消费
3. 如果所有消费者具有相同的group，那么就像是一个点对点的消息系统，如果每个消费者都具有不同的group，那么消息会广播给所有的消费者
4. 具体说来，这实际上是根据 partition 来分的，一个 Partition，只能被消费组里的一个消费者消费，但是可以同时被多个消费组消费，消费组里的每个消费者是关联到一个 partition 的，因此有这样的说法：对于一个 topic,同一个 group 中不能有多于 partitions 个数的 consumer 同时消费,否则将意味着某些 consumer 将无法得到消息
5. 同一个消费组的两个消费者不会同时消费一个 partition
6. kafka采用了pull方式，即：在消费者和broker建立连接后，主动去pull消息，首先消费者可以根据自己的消费能力适时地去pull消息并处理，且可以控制消息消费的进度(offset)
7. 当消息被消费者接收之后，需要保持offset记录消费到哪里了，0.10版本后，kafka把这个offset的保持从zk中剥离，保存在一个名叫consumeroffsets topic的Topic中

#### 消息传送机制

kafka支持3中消息投递语义，通常使用At least once模型

1. At most once：最多一次，消息可能会丢失，但不会重复
2. At least once：最少一次，消息不会丢失，可能会重复
3. Exactly once：只且一次，消息不丢失不重复，只且消费一次

---



## Kafka集群架构

---

## Kafka工作流程

Kafka 只是分为一个或多个分区的主题的集合。Kafka 分区是消息的线性有序序列，其中每个消息由它们的索引(称为偏移)来标识。Kafka 集群中的所有数据都是不相连的分区联合。 传入消息写在分区的末尾，消息由消费者顺序读取。 通过将消息复制到不同的代理提供持久性。

Kafka 以快速，可靠，持久，容错和零停机的方式提供基于pub-sub 和队列的消息系统。 在这两种情况下，生产者只需将消息发送到主题，消费者可以根据自己的需要选择任何一种类型的消息传递系统

### 发布-订阅消息的工作流程

pub-sub消息的工作流程：

1. 生产者向主题topic发送消息
2. 

### 队列消息/用户组的工作流

### Zookeeper的作用





















































