## KafKa

[参考文章](https://www.w3cschool.cn/apache_kafka/)

### 定义、简介

1. kafka是一个分布式的基于发布/订阅模式的消息队列，可以处理大量的数据，并使你能够将消息从一个端点传递到另一个端点
2. Kafka消息保留在磁盘上，并在集群内复制以防止数据丢失，Kafka构建在Zookeeper同步服务之上
3. 主要应用于大数据实时处理领域
3. 最新定义：分布式事件流平台，被数千家公司用于高性能数据管道、流分析、数据集成和关键认为应用

### 术语

#### 生产者、消费者

1. 消息的发送者叫Producer，消息的使用者/接收者是Consumer
2. 生产者将数据保存到Kafka集群中，消费者从中获取消息进行业务的处理
3. ![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733109884.png)

#### broker

1. broker    n:代理、中间人   v:协调、安排
2. Kafka集群中有很多台Server，其中每一台Server都可以存储消息，将每一台Server称为一个Kafka实例，也叫做broker
2. broker接受生产者发生的消息并存入磁盘，broker同时服务消费者拉取分区消息的请求，返回目前已经提交的信息

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

## 消息队列应用场景

常见消息队列：kafka，activeMq，RabbitMq，RocketMq

大数据场景主要采用kafka做为消息队列。在JavaEE开发中采用另外三种

1. 缓冲和消峰：有助于控制和优化数据流经过系统的速度，解决生产消息和消费消息的处理速度不一致请求
2. 解耦：允许独立的拓展或修改两边的处理过程，只要确保它们遵守同样的接口约束
3. 异步通信：允许把消息放入队列，但并不立即处理它，然后在需要的时候再去处理它们

---



### 消息队列的两种模式

#### 点对点模式

1. 在点对点的消息系统中，消息保留在队列中，一个或者多个消费者可以消耗队列中的消息，但是消息最多只能被一个消费者消费，一旦有一个消费者将其消费掉，消息就从该队列中消失
1. ![image-20220424221006967](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204242212370.png)

#### 发布/订阅模式

1. 一对多，消费者消费数据之后，消费者消费后，队列不会消除消息
2. 消息生产者（发布）将消息发布到topic中，同时又多个消息消费者（订阅）消费该消息
3. topic是一个队列
4. 这种模式下有两种消费方式
   - 消费者主动消费消息
   - topic主动推送给消费者
5. ![image-20220424221235289](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204242212437.png)

---



### Kafka基本原理

#### 分布式和分区

1. 我们说 kafka 是一个分布式消息系统，所谓的分布式，实际上我们已经大致了解。消息保存在 Topic 中，而为了能够实现大数据的存储，一个 topic 划分为多个分区，每个分区对应一个文件，可以分别存储到不同的机器上，以实现分布式的集群存储。另外，每个 partition 可以有一定的副本，备份到多台机器上，以提高可用性。
2. 一个 topic 对应的多个 partition ，分散存储到集群中的多个 broker 上，存储方式是一个 partition 对应一个文件，每个 broker 负责存储在自己机器上的 partition 中的消息读写

#### partition副本

1. kafka可以配置partition需要备份的个数(replicas)，每个partition将会被备份到多台机器上，以提高高可用性，备份的数量可以通过配置文件指定
2. 既然有副本，就涉及到对同一个文件的多个备份如何进行管理和调度。kafka 采取的方案是：每个 partition 选举一个 server 作为“leader”，由 leader 负责所有对该分区的读写，其他 server 作为 follower 只需要简单的与 leader 同步，保持跟进即可。如果原来的 leader 失效，会重新选举由其他的 follower 来成为新的 leader。如何选取：kafka所以zookeeper在broker中选出一个controller，用于partition分配和leader选举
2. 一个分区只能由一个消费者消费

#### 整体数据流

kafka总体数据流满足下图，下图基本上概括了整个kafka的基本原理

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733104250.jpg)

##### 数据生产过程

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733117119.png)

1. 对于生产者要写入的一条记录，可以指定四个参数：分别是topic、partition、key、value，其中topic和value(要写入的数据)是必须指定的
2. 对于一条记录，先对其进行序列化，然后按照topic和partition，放到对应的发送队列中。如果partition没有指定，那么情况会是这样的：
   - 指定了key，则按照key进行哈希，相同的key去一个partition
   - 没有指定key，Round-Robin来选partition
3. 生产者会和topic下所有partition leader保持socket连接，消息由生产者直接通过socket发送到broker。其中partition leader的位置(host:port)注册在zookeeper中，生产者作为zookeeper client，已经注册了watch用来监听partition leader的变更事件，因此可以准确的知道谁是当前的leader
4. 生产者端采用异步发送：将多条消息暂且在客户端buffer起来，并将他们批量的发送到broker。小数据I/O太多会拖慢整体的网络延迟，批量延迟发送提升了网络效率

##### 数据消费过程

![img](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202009161733117671.png)

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

## Kafka工作流程

Kafka 只是分为一个或多个分区的主题的集合。Kafka 分区是消息的线性有序序列，其中每个消息由它们的索引(称为偏移)来标识。Kafka 集群中的所有数据都是不相连的分区联合。 传入消息写在分区的末尾，消息由消费者顺序读取。 通过将消息复制到不同的代理提供持久性。

Kafka 以快速，可靠，持久，容错和零停机的方式提供基于pub-sub 和队列的消息系统。 在这两种情况下，生产者只需将消息发送到主题，消费者可以根据自己的需要选择任何一种类型的消息传递系统

### 发布-订阅消息的工作流程

pub-sub消息的工作流程：

1. 生产者向主题topic发送消息
2. kafka代理存储该特定主题配置的分区中的所有消息，它确保消息在分区之间平等共享。 如果生产者发送两个消息并且有两个分区，Kafka 将在第一分区中存储一个消息，在第二分区中存储第二消息。
3. 消费者订阅特定主题
4. 一旦消费者订阅主题，kafka将向消费者提供主题的当前偏移，并且还将偏移保存在zookeeper中
5. 消费者将定期(例如100ms)请求kafka新消息
6. 一旦kafka收到来自生产者的消息，它将这些消息转发给消费者
7. 消费者将收到消息进行处理
8. 一旦消息被处理，消费者将向kafka代理发送确认
9. 一旦 Kafka 收到确认，它将偏移更改为新值，并在 Zookeeper 中更新它。 由于偏移在 Zookeeper 中维护，消费者可以正确地读取下一封邮件，即使在服务器暴力期间
10. 以上流程将重复，直到消费者停止请求。
11. 消费者可以随时回退/跳到所需的主题偏移量，并阅读所有后续消息

### 队列消息/用户组的工作流

在队列消息传递系统而不是单个消费者中，具有相同组 ID 的一组消费者将订阅主题。 简单来说，订阅具有相同 Group ID 的主题的消费者被认为是单个组，并且消息在它们之间共享。

1. 生产者以固定间隔向某个主题发送消息
2. kafka存储  为该特定主题配置的分区中的所有消息
3. 单个消费者订阅特定主题，假设Topic-01为Group ID为Group-1
4. kafka以与发布-订阅消息相同的方式与消费者交互，直到新消费者以相同的组ID订阅相同主题Topic-01
5. 一旦新消费者到达，kafka将其操作切换到共享模式，并在两个消费者之间共享数据。此共享将继续，直到用户数达到为该特定主题配置的分区数
6. 一旦消费者的数量超过分区的数量，新消费者将不会接收任何进一步的消息，直到现有消费者取消订阅任何一个消费者。 出现这种情况是因为 Kafka 中的每个消费者将被分配至少一个分区，并且一旦所有分区被分配给现有消费者，新消费者将必须等待

### Zookeeper的作用

Apache Kafka 的一个关键依赖是 Apache Zookeeper，它是一个分布式配置和同步服务。Zookeeper 是 Kafka 代理和消费者之间的协调接口。Kafka 服务器通过 Zookeeper 集群共享信息。Kafka 在 Zookeeper 中存储基本元数据，例如关于主题，代理，消费者偏移(队列读取器)等的信息。

由于所有关键信息存储在 Zookeeper 中，并且它通常在其整体上复制此数据，因此Kafka代理/ Zookeeper 的故障不会影响 Kafka 集群的状态。Kafka 将恢复状态，一旦 Zookeeper 重新启动。 这为Kafka带来了零停机时间。Kafka 代理之间的领导者选举也通过使用 Zookeeper 在领导者失败的情况下完成。



kafka2.8 之后可以不要配置zk了



---



## Kafka ack机制

kafka的ack机制：

1. producer发送消息到leader收到消息之后发送ack
2. leader和follower之间同步完数据会发送ack

这直接影响kafka集群的吞吐量和消息可靠性

ack有3个可选值0、1、-1

1. 0：就是producer发送一次就不再发送了，不管是否发送成功
2. 1：默认值1，producer只要收到一个分区副本成功写入的通知就认为推送消息成功了。这个分区副本必须是leader副本，只有leader副本成功写入了，producer才会认为消息发送成功
3. -1（all)：producer只要收到分区内所有副本的成功写入的通知才认为推送消息成功了

ack=1的情况下，为什么消息也会丢失？

ack=1的情况下，producer只要收到分区leader成功写入的通知就会认为消息发送成功了。但是如果leader成功写入后，还没有来得及把数据同步到follower节点就挂了，这时候消息就丢失了



## kafka数据重复写入、消费问题



### 重复写入

大多出现在ack=-1情况下

例如：producer发送消息到broker，broker里的leader、follower已经落盘，准备回应producer的时候，突然这个leader挂了，ack没有发送出去，producer没有收到确认消息。这时候，会重新选举出一个leader，producer会重新发送消息到新的leader，这就造成了数据重复写入

### 重复消费

大多出现在ack=0或者1的情况下

例如：某个消费者因为消费过慢、网络原因、无法消费等情况，触发rebalanced，此时数据会重新发到一个新的consumer的消费，这时候就出现重复消费

**如何解决**







## kafka命令行



## kafka生产者

### 生产者消息发送流程

将外部数据发送到kafka

producer->send(ProducerRecord)->[Interceptors拦截器可有可无]->

Serializer序列化器(kafka自己实现的，没有Java的)->Partitioner分区器(一个分区会创建一个队列)



sender线程

Sender(读取数据)

batch.size：只有数据累积到batac.size后，sender才会发送数据。默认16k

linger.ms：如果数据迟迟未到batch.size，sender等待linger.ms设置的时间后就会发送数据。单位是ms，默认是0ms表示没有延迟

kafka集群收到消息后，给sender应答（ack机制）

sender成功后，清理队列里的消息，失败会去重试



### 异步发送

外部的数据发送到队列里面  采用异步发送

生产者.send()

### 带回调函数的异步发送

生产者.send(ProducerRecord，Callback)



### 同步发送

生成者.send()，外部数据需要等待队列里面的数据处理完后，才能发送

send().get()





### 生产者分区

分区的好处

1. 便于合理使用存储资源，每个分区在一个Broker上存储，可以把海量的数据按照分区切割成一块一块数据存储在多台Broker上。合理控制分区的任务，可以实现负载均衡的效果
2. 提高并行度，生产者可以以分区为单位发送数据，消费者可以以分区为单位进行消费数据

#### 生产者发送消息的分区策略

1. 默认的分区器DefaultPartitioner

2. 自定义分区器

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

RecordAccumulator:缓冲区大小，修改为64m

### 数据可靠性

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

### 数据重复性

ack=-1会导致

场景：follower同步完成后，leder应答时突然挂掉，此时就需要选举新的leader

由于上一次leader未应答，所有producer会发送数据到 新的leader，此时出现数据重复



幂等性 和事务

kafka幂等性：指的producer无论向borker发送多少次重复数据，broker端只会持久化一条，保证了不重复

重复数据判断标准：具有<pid,partition,Seqnumber>相同主键的消息提交时，broker只会持久化一条。其中pid是kafka每次重启都会分配一个新的，partition表示分区号，sequence number是单调自增的

所以幂等性只能保证：单分区会话内不重复

enable.idempotence=true  默认开启幂等性



事务：开启事务，必须开启幂等性



---

### 数据有序

多分区时，分区与分区间无序



### 数据乱序

kafka1.x后，启用幂等后，kafka服务端会缓存producer发来的最近5个request的元数据，所以，无论如何都可以保证最近5个request的数据是有序的

开启了幂等性且缓存的请求个数小于5个，会在服务端重写排序



## kafka broker

Zookeeper中存储的kafka信息，比较多

1. /kafka/brokers/ids：记录有哪些服务器   [0,1,2]
2. /kafka/brokers/topics/first/partitions/0/state {"leader":1,"isr":[1,0,2]}  记录谁是leader，有哪些服务器可用
3. /kafka/controller {brokerid:0}  :辅助选举leader



### kafka副本

副本作用：提高数据可靠性

默认一个副本，生成环境一般配置为2个；太多副本会增加磁盘存储空间，增加网络上的数据传输，降低了效率

kafka中副本分为：leader和follower

kafka生产者只会把数据发往leader，然后follower找leader进行同步数据

kafka分区中的所有副本统称为：AR assigned repllicas

AR=ISR+OSR

ISR：表示和Leader保持同步的**Follower集合**。如果Follower长时间未向Leader发送通信请求或同步数据，则Follower将被踢出ISR，该时间阈值默认30s，由replica.lag.time.max.ms设定。leader发生故障后，会从ISR中选举新的leader

OSR：表示Follower与Leader副本同步时延迟过多的副本



### Leader选举流程

1. 每台broker启动后，会在zk中注册
2. zk的controller：谁先注册，谁说了算
3. broker与zookeeper里面都有controller节点
4. 由选举出来的controller（broker里面的）监听brokers节点变化
5. controller决定leader选举
   - 选举规则：在isr队列中存活为前提，按照AR中排在前面的优先
   - 例如：ar[1,0,2]，isr[1,0,2]，那么leader就会按照1，0，2的顺序轮询
   - AR:kafka分区中的所有副本统称
6. controller将节点信息上传到zk
7. 其他controller从zk同步相关信息
8. 假设broker中的leader挂了，controller监听到节点的变化，从zk获取isr，选举新的leader
9. 更新leader以及Isr



### Follower故障处理

LEO：log end offset 每个副本的最后一个offset，其实就是最小的offset+1

HW：high watermark 所有副本中最小的LEO









### 分区副本分配

假如kafka服务器只有4个节点，设置kafka分区数大于服务器台数，kafka底层如何分配存储副本？



### Broker文件存储

Topic

topic是一个逻辑上的概率，partition是物理上的概念，每一个partition对应于一个log文件，该log文件中存储的就是Producer生产的数据。Producer生产的数据会不断追加到该log文件末端。是一个虚拟概念。为了防止log过大导致数据定位效率低下哎，kafka采取了分片和索引机制，将每个partition分为多个segment。每个segment包括：.index文件，.log文件，.timeindex文件等。这些文件位于一个文件夹下，该文件夹命名规则：topic名称+分区序号，例如：first-0

一个topic可分为多个分区，一个分区可分为多个segment

segment默认1G，里面包含：.log文件 .index偏移量索引文件 .timeindex时间戳索引文件等等

默认7天会删除里面的数据

index和log文件以当前segment的第一条消息的offset命名

注意：

1. index为稀疏索引，大约每往log写入4k，会往index文件写入一条索引
2. index文件中保存的offset为相对offset，这样能确保offset的值所占空间不会过大，因此能将offset的值控制在固定大小

如何在log中定位到offset=xxx的Record?

1. 根据目标offset定位segment文件
2. 找到小于等于目标offset的最大offset对应的索引项

![image-20220426212527332](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204262125861.png)





### Broker文件清理策略

kafka中默认的日志保存时间为7天，可以通过参数进行调整

kafka提供的日志清理策略有delete和compact

1. delete：日志删除，将过期数据删除
   - log.cleanup.policy=delete，所有数据启用删除策略
   - 基于时间：默认打开，以segment中所有记录中的最大时间戳作为该文件的时间戳
   - 基于文件大小：默认关闭，超过设置的日志总大小，则删除最早的segment
2. compact：日志压缩
   - 对应相同key的不同value值，只保留最后一个版本



### 高效读写数据

1. kafka本身是分布式集群，可以采用分区技术，并行度高
2. 读数据采用稀疏索引，可以快速定位要消费的数据
3. 顺序写磁盘
   - kafka的producer生产数据，要写入log文件中，写的过程一直追加到文件末端，为顺序写
   - 顺序写能达到600M/s
4. 页缓存+零拷贝技术
   - 零拷贝：kafka的数据加工处理操作交由kafka生产者和kafka消费者处理，kafka broker应用层不关心存储的数据，所以不用走应用层，传输效率高
   - PageCache页缓存：kafka重度依赖底层操作系统提高的pagecache功能。当上层有写操作时，操作系统只是将数据写入pagecache，当读操作发生时，先从pagecache查找，如果找不到，再去磁盘中读取。
   - ![image-20220427205334342](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272053956.png)



## kafka消费者



### kafka消费方式

#### pull方式

consumer采用从broker中主动拉取数据。kafka采用这种方式

![image-20220427205659931](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272057175.png)



缺点：如果kafka没有数据，那么消费者可能会陷入循环中，一直返回空的数据

#### push方式

kafka没有采用这种方式

因为：由broker决定消息发送速率，很难适应消费者的消费速率



### 消费者工作流程

![image-20220427210633676](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272106921.png)

offset放在zookeeper 的consumer节点中

offset为什么移除zookeeper？

如果所有消费者都把offset存在zk中，则所有消费者需要于zk进行大量的交换，会导致网络数据传输频繁，所以存放在系统主题中，方便管理



### 消费者组原理

Consumer Grop：由多个consumer组成，形成一个消费者组

1. 消费者组内每个消费者负责消费不同分区的数据，一个主题的分区只能由一个组内的消费者消费
2. 消费者组之间互不影响

![image-20220427211607003](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272116428.png)





![image-20220427211716358](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272117602.png)



### 消费者组初始化流程

![image-20220427215200765](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272152197.png)





### 消费者组消费流程

![image-20220427215650700](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272156995.png)





### 独立消费者  消费一个主题

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



### 独立消费者  消费分区

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



### 分区的分配以及再平衡



![image-20220427225649493](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272256859.png)



#### Range分区及再平衡

range时候的再平衡：有消费者挂了，它的任务交给其他consumer

![image-20220427230004999](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272300268.png)



![image-20220427230018304](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204272300542.png)











