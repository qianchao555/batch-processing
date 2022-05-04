## 消息队列及应用场景

消息队列：Message Queue简称MQ，是一种跨进程的通信机制，常用于上下游之间传递消息

MQ是一种常见的上下游 "逻辑解耦+物理解耦"的消息通信分为，消息发送上游只需要依赖MQ，逻辑上和物理上都不用依赖其他服务

常见消息队列：kafka，activeMq，RabbitMq，RocketMq

大数据场景主要采用kafka做为消息队列。在JavaEE开发中采用另外三种

使用场景：

1. **缓冲和消峰**：有助于控制和优化数据流经过系统的速度，解决生产消息和消费消息的处理速度不一致请求
2. **解耦**：允许独立的拓展或修改两边的处理过程，只要确保它们遵守同样的接口约束
3. **异步通信**：允许把消息放入队列，但并不立即处理它，然后在需要的时候再去处理它们

---



## 消息系统的两种模式

#### 点对点模式

1. 在点对点的消息系统中，消息保留在队列中，一个或者多个消费者可以消费队列中的消息，但是消息最多只能被一个消费者消费，一旦有一个消费者将其消费掉，消息就从该队列中消失
1. ![image-20220424221006967](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204242212370.png)

#### 发布/订阅模式

1. 消息生产者（发布）将消息发布到消息队列中，同时有多个消息消费者（订阅）消费该消息
2. 一对多，消费者消费数据之后，消费者消费后，队列不会消除消息
4. 这种模式下有两种消费方式
   - 消费者主动消费消息  ：kafka采用此种方式poll
   - 消息队列主动推送给消费者
5. ![image-20220424221235289](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204242212437.png)

---



## Apache KafKa

[参考文章](https://www.w3cschool.cn/apache_kafka/)

### 定义、简介

1. kafka是一个分布式的基于发布/订阅模式的消息系统，可以处理大量的数据，并使你能够将消息从一个端点传递到另一个端点
2. Kafka消息保留在磁盘上，并在集群内复制以防止数据丢失，Kafka构建在Zookeeper同步服务之上
3. 主要应用于大数据实时处理领域
4. 最新定义：分布式事件流平台，被数千家公司用于高性能数据管道、流分析、数据集成和关键认为应用

Kafka支持低延迟消息传递，并在出现机器故障时提供对容错的保证。 它具有处理大量不同消费者的能力。kafka非常快，执行速度大约为200万写/秒。kafka将所有数据都保存到磁盘，意味着所有写都会进入操作系统（RAM)的页面缓存，这使得将数据从页面缓存传输到网络套接字非常有效

### 术语

#### 生产者、消费者

1. 消息的发送者叫Producer，消息的使用者/接收者是Consumer
2. 生产者将数据发送到Kafka集群中，消费者从中获取消息进行业务的处理
3. ![image-20220504223114695](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205042231879.png)

#### broker

1. broker    n:代理、中间人   v:协调、安排
2. Kafka集群中有很多台Server，其中每一台Server都可以存储消息，将每一台Server称为一个Kafka实例，也叫做broker
3. broker接受生产者发生的消息并存入磁盘，broker同时服务消费者拉取分区消息的请求，返回目前已经提交的信息

#### Topic

1. topic：一个topic里保存的是同一类消息，相当于对消息的分类。数据存储在主题中
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



## Kafka基本原理

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





#### RoundRobin分区及再平衡 

![image-20220428201322929](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282013691.png)



#### Sticky 以及再平衡

黏性分区：可以理解为分配的结果代意黏性的。即在执行一次新的分配之前，考虑上一次分配的结果，尽量少的调整分配的变动，可以节省大量的开销

是kafka 0.11x版本开始引入的分配策略，首先会尽量均衡的放置分区到消费者上面，在出现同一消费者组内消费者出现问题的时候，会尽量保持原有分配的分区不变化

## Offset

![image-20220428201925433](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282019003.png)



_consumer_offsets主题里面采用k-v的方式存储数据。key是group.id+topic+分区号，v是当前offset的值，每隔一段时间，kafka内部会对这个topic进行compact，也就是每个group.id+topic+分区号就保留最新数据

在配置文件 config/consumer.properties中添加配置：exclude.internal.topics=false   默认为true，表示不能消费系统主题。为了能查看该系统主题数据，需要将该参数设置为false







消费过程中会产生offset存放在这个系统主题中

![image-20220428203023561](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282030761.png)



#### 自动提交offset

为了使用户专注于自己的业务逻辑，kafka提供了自动提交offset的功能

自动提交offset的相关参数：

1. enable.auto.commit：是否开启自动提交offset功能，默认true
2. auto.commit.interval.ms：自动提交offset的时间间隔，默认5s

##### 自动提交offset实现原理

![image-20220428203358834](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282033218.png)



#### 手动提交offset

虽然自动提交offset方便，但是是基于时间提交的，开发人员难以把握offset提交的时机，因此可以通过手动提交offset

![image-20220428205244851](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282052076.png)

两种方式

1. commitSync：同步提交，必须等待offset提交完毕，再去消费下一批数据
2. commitAsync：异步提交，发送完offset请求后，就开始消费下一批数据了

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

将时间 转换为 offset



#### 漏消费和重复消费

![image-20220428215121984](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282151370.png)



如何做到既不漏消费也不重复消费？ 解决：消费者事务

#### 消费者事务

![image-20220428215447661](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282154017.png)



## 数据积压

消费者如何提高吞吐量 问题

![image-20220428215818999](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282158353.png)





## kafka监控

kafka-Eagle框架可以监控

kafka-eagle.org   最新名字：efak



## kafka-Kraft模式

已经可以不需要Zookeeper了，但是是兼容的

kafka2.8x后

![image-20220428221103795](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282211144.png)





## kafka集成springboot



![image-20220428221642033](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202204282216342.png)



 

~~~java
//1、配置文件中 配置kafka的相关信息

//生产者
@Autowired
KafkaTemplate<String,String> kafka;

kafka.send();//参数具体地方，具体修改
//发送到了kafka broker上

~~~



~~~java
//配置文件中 配置相关信息

//消费者  需要指定为配置类
@Configuration
public class KafkaConsumer{
    @KafkaListener(topics="xxx")
    public void consumerTopics(String msg){
        //msg就是 消息的内容
    }
}

    
    

~~~



## kafka调优



### 硬件配置

场景：100万日活，每人每天100条日志，每天总共日志的数量为100*100万

处理日志速度：总量/（24*3600s)   =1150条/s

一条日志大小：正常为0.5-2k   按照1k进行计算

1150*1k/s  =  大于等于 1m/s 

高峰值：（中午小高峰、晚上8-12高峰期）* 平均值的20倍  =20m/s

#### 购买多少台kafka服务器？

服务器台数=2*（生产者峰值生产速率 * 副本数量 / 100） +1

​				=2* （20m/s *2 /100)+1  =3

#### 磁盘选择

kafka按照顺序读写

机械和固态硬盘的 顺序读写速度差不多

随机读写 选择固态硬盘

1亿条*1k   =100g

100g*2个副本  * 保存3天  / 0.7的磁盘容量 不要装满了   ==1T

#### 内存选择

kafka 内存= kafka堆内存（kafka内部配置） +页缓存（服务器的内存）

堆内存：10-15g

页缓存：segment(1g)   分区数Leader(假如10个分区)  *  1g  *  25%  =1g



### kafka生产者



相关的参数调整



### kafka Broker调优











---



## 面试

### 消息队列连环炮

#### 项目中怎么用消息队列的（我们公司用的kafka)

job处理、上下游传递数据

---



#### 为什么这些场景中，系统要用到消息队列

回答：什么业务场景、不用的影响、用之后的好处

公司kafka：异步场景

主要应用消息队列的场景：解耦、异步、削峰

##### 解耦

A系统发送个数据到BCD三个系统，接口调用发送，那如果E系统也要这个数据呢？那如果C系统现在不需要了呢？现在A系统又要发送第二种数据了呢？A系统负责人濒临崩溃中。。。再来点更加崩溃的事儿，A系统要时时刻刻考虑BCDE四个系统如果挂了咋办？我要不要重发？我要不要把消息存起来？

不用mq的场景：

![image-20220503150927971](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031509395.png)

面试技巧：需要去考虑一下你负责的系统中是否有类似的场景，就是一个系统或者一个模块，调用了多个系统或者模块，互相之间的调用很复杂，维护起来很麻烦。但是其实这个调用是不需要直接同步调用接口的，如果用MQ给他异步化解耦，也是可以的，你就需要去考虑在你的项目里，是不是可以运用这个MQ去进行系统的解耦。在简历中体现出来这块东西，用MQ作解耦

 使用mq之后：

![image-20220503151953898](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031519126.png)



##### 异步

不用mq的同步高延时请求场景

![image-20220503152644584](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031526803.png)

A系统接收一个请求，需要在自己本地写库，还需要在BCD三个系统写库，自己本地写库要3ms，BCD三个系统分别写库要300ms、450ms、200ms。最终请求总延时是3 + 300 + 450 + 200 = 953ms，接近1s，用户感觉搞个什么东西，太慢了



使用mq后进行异步化之后：

![image-20220503160632232](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031606430.png)



##### 削峰

没有mq的时候，高峰期系统被打死场景：

![image-20220503161848712](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031618932.png)

每天0点到11点，A系统风平浪静，每秒并发请求数量就100个。结果每次一到11点~1点，每秒并发请求数量突然会暴增到1万条。但是系统最大的处理能力就只能是每秒钟处理1000个请求啊。。。尴尬了，系统会死

使用mq后：

![image-20220503163046599](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031630784.png)

---



#### 如果用到消息队列，那么他的优点和缺点是什么

优点：上面的解耦、异步、削峰

缺点：

![image-20220503164555603](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205031645871.png)

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

![image-20220503205204699](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205032052899.png)



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

![image-20220503214357393](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205032143663.png)

kafka有个offset的概念，就是每个消息写进去，都有一个offset，代表他的序号，然后consumer消费了数据之后，每隔一段时间，会把自己消费过的消息的offset提交一下，代表我已经消费过了，下次我要是重启啥的，你就让我继续从上次消费到的offset来继续消费吧。

 但是凡事总有意外，比如有时候重启系统，看你怎么重启了，如果碰到点着急的，直接kill进程了，再重启。这会导致consumer有些消息处理了，但是没来得及提交offset，尴尬了。重启之后，少数消息会再次消费一次。

 其实重复消费不可怕，可怕的是你没考虑到重复消费之后，怎么保证幂等性。

举个例子：假设你有个系统，消费一条往数据库里插入一条，要是你一个消息重复两次，你不就插入了两条，这数据不就错了？但是你要是消费到第二次的时候，自己判断一下已经消费过了，直接扔了，不就保留了一条数据？一条数据重复出现两次，数据库里就只有一条数据，这就保证了系统的幂等性

 

如何解决mq重复消费的幂等性？

如何保证MQ的消费是幂等性的，需要结合具体的业务来看

其实还是得结合业务来思考，这里给几个思路：

1. 比如你拿个数据要写库，你先根据主键查一下，如果这数据都有了，你就别插入了，update一下好吧
2. 比如你是写redis，那没问题了，反正每次都是set，天然幂等性
3. 比如你不是上面两个场景，那做的稍微复杂一点，你需要让生产者发送每条数据的时候，里面加一个全局唯一的id，类似订单id之类的东西，然后你这里消费到了之后，先根据这个id去比如redis里查一下，之前消费过吗？如果没有消费过，你就处理，然后这个id写redis。如果消费过了，那你就别处理了，保证别重复处理相同的消息即可。

还有比如基于数据库的唯一键来保证重复数据不会重复插入多条，我们之前线上系统就有这个问题，就是拿到数据的时候，每次重启可能会有重复，因为kafka消费者还没来得及提交offset，重复数据拿到了以后我们插入的时候，因为有唯一键约束了，所以重复数据只会插入报错，不会导致数据库中出现脏数据

![image-20220503215808037](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205032158231.png)

---



#### 消息队列里面的数据不见了？

即：如何保证消息的可靠性传输、如何处理消息丢失的问题

kafka而言：

1. 消费者端弄丢了数据
   - 唯一可能丢失数据的情况是：消费到了消息，消费者也自动提交了offset，kafka认为这个消息已经消费了，但是此时消费者还没有处理这个消息就挂了，导致数据丢失
   - 解决方案：关闭kafka的自动提交offset，消费者处理完消息后进行手动提交，从而保证消息不丢失。但是有可能导致重复消费，因为可能消费者在手动提交offset的时候宕机，还没有提交offset。后面肯定会重复消费一次，消费者保证幂等性就可以了
   - 还有一种情况：就是kafka消费者消费到了数据之后是写到一个内存的queue里先缓冲一下，结果有的时候，你刚把消息写入内存queue，然后消费者会自动提交offset。然后此时我们重启了系统，就会导致内存queue里还没来得及处理的数据就丢失了
2. kafka丢失了数据
   - 比较常见的场景：某个leader收到数据后，还没有进行数据同步，此时leader挂了，导致新选举的follower变为leader，但是此时数据并没有同步过来导致数据丢失
   - ![image-20220504165124796](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205041651988.png)
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



![image-20220504201135687](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205042011857.png)



kafka写入一个partition中的数据一定是有顺序的

kafka如何保证顺序传输？

一个topic，一个partition，一个consumer，内部单线程消费，写N个内存queue，然后N个线程分别消费一个内存queue即可

![image-20220504202608347](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205042026706.png)

---



#### 如何保证消息队列的延时以及过期失效问题？rabbitmq的暂时不了解

#### 消息队列满了后怎么处理？大量消息持续积压几小时，怎么解决？

问题本质：消费端出问题了



1. 先修复consumer的问题，确保其恢复消费速度，然后将现有cnosumer都停掉
2. 新建一个topic，partition是原来的10倍，临时建立好原先10倍或者20倍的queue数量
3. 然后写一个临时的分发数据的consumer程序，这个程序部署上去消费积压的数据，消费之后不做耗时的处理，直接均匀轮询写入临时建立好的10倍数量的queue
4. 接着临时征用10倍的机器来部署consumer，每一批consumer消费一个临时queue的数据
5. 这种做法相当于是临时将queue资源和consumer资源扩大10倍，以正常的10倍速度来消费数据
6. 等快速消费完积压数据之后，得恢复原先部署架构，重新用原先的consumer机器来消费消息

![image-20220504204021818](https://gitee.com/qianchao_repo/pic-typora/raw/master/kafka_img/202205042040994.png)

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











































