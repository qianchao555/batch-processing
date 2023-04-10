## ElasticSearch

> ES是一款非常强大的、基于Lucene的开源**搜索及分析引擎**，它是一个实时的分布式搜索分析引擎，能让你以前所未有的速度和规模去探索你的数据

它被用作全文索引、结构化搜索、分析，以及这三个功能的组合

除了搜索，结合Kibana、Logstash、Beats开源产品、Elastic技术栈（ELK)还被广泛运用在大数据近实时分析领域。包括：日志分析、指标监控、信息安全等等。它可以帮助我们探索海量结构化、非结构化数据，按需创建可视化报表，对监控数据设置报警阈值，通过机器学习，自动识别异常状况

ES基于Restful Web Api，使用Java开发的搜索引擎库，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎



### 为什么不直接使用Lucene

> ElasticSearch是基于Lucene的



Lucene：**全文搜索引擎**

Lucene是当下最先进、高性能、全功能的搜索引擎库，但是 Lucene 仅仅只是一个库。为了充分发挥其功能，你需要使用 Java 并将 Lucene 直接集成到应用程序中。 更糟糕的是，您可能需要获得信息检索学位才能了解其工作原理。Lucene 非常复杂



Elasticsearch 也是使用 Java 编写的，它的内部使用 Lucene 做索引与搜索，但是它的目的是使全文检索变得简单，**通过隐藏 Lucene 的复杂性，取而代之的提供一套简单一致的 Restful Web API**

然而，ES不仅仅是Lucene，并且也不仅仅是一个全文搜索引擎，它可以被这样准确的描述：

1. 一个分布式的实时文档存储，每个字段都可以被索引于搜索
2. 一个分布式实时分析搜索引擎
3. 能胜任上百个服务器节点的扩展，并**支持PB级别的结构化或非结构化数据**
   - PB级别的数据存储容量：1024GB=1TB，1024TB=1PB



#### lucene实现全文索引流程

![image-20220623212318865](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206232123692.png)

---

### ES主要功能与使用场景

主要功能

1. 海量数据的分布式存储以及集群管理，达到了服务于数据的高可用以及水平扩展
2. 近实时搜索、性能卓越。对结构化、全文、地理位置等数据类型的处理
3. 海量数据的近实时分析(聚合功能)

使用场景

1. 网站搜索、垂直搜索、代码搜索
2. 日志管理于分析、安全指标监控、应用性能监控、web抓取舆情分析等等



---

### ES基础概念

#### Near Realtime(NRT) 

近实时，数据提交索引后，立马就可以搜索到



#### Cluster集群

一个集群由一个唯一的名字标识，默认为elasticsearch。集群名称非常重要，**具有相同集群名的节点才会组成一个集群**，集群名称可以在配置文件中指定



#### Node节点

存储集群的数据，参与集群的索引和搜索功能。

集群有名字，节点也有自己的名称，默认在启动时会以一个随机的UUID的前七个字符作为节点的名字，你可以为其指定任意的名字。通过集群名在网络中发现同伴组成集群。一个节点也可是集群



#### Index索引

一个索引是一个**文档的集合**。每个索引有唯一的名字，通过这个名字来操作它。一个集群中可以有任意多个索引



#### Type类型

指在一个索引中，可以索引不同类型的文档，如用户数据、博客数据。从6.0.0 版本起已废弃，一个索引中只存放一类数据







#### Document文档

> 被索引的一条数据，索引的基本信息单元，以JSON格式来表示
>
> 类似与关系型数据库中的一条记录

##### 文档的元数据

- _index：文档所在的索引名称
- _type：文档的所属类型，ES7开始，只有一个那就是 _doc
- _id：文档唯一id
- _score：相关性打分
- _version：文档版本，若文档修改了，该字段会增加
- _source：文档的原始Json数据

~~~shell
#一个普通的es文档
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



#### 字段

> 文档中的字段，对文档数据根据不同属性进行分类的标识
>
> 相当于关系型数据库中一条记录的一个字段





#### 映射

是处理数据的方式和规则方面做一些限制，如某个字段的数据类型、默认值、分析器、是否被索引等等，这些都是映射里面可以设置的，其它就是处理es里面数据的一些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射，并且需要思考如何建立映射才能对性能更好





#### Shard分片

在创建一个索引时可以指定分成多少个分片来存储。

每个分片本身也是一个功能完善且独立的“索引”，可以被放置在集群的任意节点上



#### Replication备份

一个分片可以有多个副本





![image-20220619214242668](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192143891.png)







---

### Elastic Stack生态(ELK)

> Beats+ElasticSearch+Logstash+Kibana

下图展示了ELK生态以及基于ELK的场景(深绿色部分)

![image-20220619214741901](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192147011.png)



![image-20220619214931561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192149659.png)



#### Beats

> Beats是一个面向**轻量型采集器**的平台，这些采集器可以从边缘机器向Logstash、ElasticSearch发送数据
>
> 它是由Go语言进行开发的，运行效率方面比较快。

从下图中可以看出，不同Beats的套件是针对不同的数据源。

1. Filebeat：用于监听日志数据，可以替代logstash-input-file
2. packetbeat：用于监控网络流量
3. winlogbeat：用于搜索windows事件日志
4. 。。。

![image-20230407234355171](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202304072343966.png)





#### Logstash

> Logstash是**动态数据收集管道**，拥有可扩展的插件生态系统，支持从不同来源采集数据，转换数据，并将数据发送到不同的存储库中。其能够与ElasticSearch产生强大的协同作用，后被Elastic公司在2013年收购

它具有如下特性：

1. 几乎可以访问任何数据
1. 实时解析和转换数据
2. 可扩展，具有200多个插件
3. 可靠性、安全性。Logstash会通过持久化队列来保证至少将运行中的事件送达一次，同时将数据进行传输加密
4. 监控

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

Kibana实现**数据可视化**，其作用就是在ElasticSearch中进行搜索并展示。Kibana能够以图表的形式呈现数据，并且具有可扩展的用户界面，可以全方位的配置和管理ElasticSearch

1. Kibana可以提供各种可视化的图表
2. 可以通过机器学习的技术，对异常情况进行检测，用于提前发现可以问题



日志收集系统的演变

1. beats+es+kibana
2. beats+logstash+es+kibana
3. beats+MQ+logstash+es+kibana





---

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



---

### ES入门：查询和聚合基础使用

利用kibana的dev tool进行学习测试

#### 查询数据

match_all表示查询所有数据，sort即按照申明字段排序

~~~sh
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ]
}
#-------------
bank:表示索引index
~~~



#### 分页查询

from+size

~~~sh
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ],
  "from": 10,
  "size": 10
}
~~~

#### 指定字段查询：match

~~~sh
GET /bank/_search
{
  "query": { "match": { "address": "mill lane" } }
}
#------------
因为ES底层是按照 分词索引的，所以上面查询结果是address包含mill lane 或mill 或lane的数据
~~~

#### 多条件查询bool

~~~sh
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}
#-------------
must、should、must_not、filter都是bool查询的子句

query与filter区别：
query 上下文的条件是用来给文档打分的，匹配越好 _score 越高；filter 的条件只产生两种结果：符合与不符合，后者被过滤掉
~~~



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









---

### 索引管理

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
        "properties": { ... any properties ... }
    }
}
~~~

1. settings：设置分片、副本等等信息
2. mappings：字段映射、类型等等
   - properties：由于type在后续版本会被去除掉，所以无需对type嵌套

#### 创建索引

下面都采用kibana的devtool学习索引的管理操作

例如创建一个user索引：test_index_users，其中包含三个属性：name、age、remarks；存储在一个分片、一个副本上

~~~sh
PUT /test-index-users
{
  "settings": {
		"number_of_shards": 1,
		"number_of_replicas": 1
	},
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "age": {
        "type": "long"
      },
      "remarks": {
        "type": "text"
      }
    }
  }
}
~~~



##### 插入数据

~~~sh
POST /test-index-users/_doc
{
	"name":"test name",
	"age":"18",
	"remarks":"hello world"
}
~~~



#### 修改索引

例如：修改副本数量为0

~~~sh
PUT /test-index-users/_settings
{
  "settings": {
    "number_of_replicas": 0
  }
}
~~~

#### 打开/关闭索引

一旦索引被关闭，那么这个**索引只能显示元数据信息**，不能进行读写操作

~~~sh
Post /test-index-users/_close
~~~

~~~sh
Post /test-index-users/_open
~~~

#### 删除索引

~~~sh
Delete /test-index-users
~~~

#### Kibana管理索引

![image-20220619233508493](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192335644.png)









---

### 索引模板

> Index Template

后续继续学习，这次暂且了解

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







---

### ES查询-DSL(复合查询)

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

### ES查询-全文搜索

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

### DSL查询-Term

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





---

### 聚合查询Bucket

除了查询之外，最常用的就是聚合了。

ES提供了三种聚合方式：桶聚合、指标聚合、管道聚合

ES中桶的概念类似于SQL中的分组，而指标类似于count、sum、max等统计方法，进而引入了桶、指标的概念

1. 桶Buckets：满足特定条件的文档的集合
2. 指标Metrics：对桶内的文档进行统计计算

指标聚合与桶聚合大多数情况组合在一起使用，桶聚合本质上是一种特殊的指标聚合，它的聚合指标就是数据的条数(count)

Bucket Aggregations：桶 聚合

官网给出了好几十种桶聚合，但是肯定是不能一个一个去看的，所以要站在设计的角度上来分类理解，主要有以下三类：

![image-20220620163557154](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206201635557.png)



#### 标准的聚合

~~~sh
GET /test-agg-cars/_search
{
    "size" : 0,
    "aggs" : { 
        "popular_colors" : { 
            "terms" : { 
              "field" : "color.keyword"
            }
        }
    }
}
#--------------
聚合操作被置于顶层参数aggs下
popular_colors：聚合的名称
terms：桶的类型
~~~



#### 多个聚合

~~~sh
GET /test-agg-cars/_search
{
    "size" : 0,
    "aggs" : { 
        "popular_colors" : { 
            "terms" : { 
              "field" : "color.keyword"
            }
        },
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



---

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





---

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





#### ES分析器

分析过程

1. 将一块文本分成适合于倒排索引的独立词条
2. 将这些词条统一化为标准格式，以提高它们的可搜索性

分析器执行上面的工作



分析器实际上是将三个功能封装到了一个包里来完成这些工作：

1. 字符过滤器
   - 字符串按照顺序通过每个字符过滤器
   - **字符过滤器的任务**是在分词前整理字符串，例如：去掉html、将&转化为and等等
2. 分词器
   - 其次，字符串被分词器分为单个的词条
   - 一个简单的分词器遇到空格或标点的时候，可能会将文本拆分成词条
3. Token(分词、词条)过滤器
   - 最后，词条按照顺序通过tokent(分词)过滤器
   - 这个过程可能会改变词条(例如：小写化)、删除词条(例如：a、and、the这些无用词)、增加词条

ES提供了开箱即用的字符过滤器、分词器和tokent过滤器，这些可以组合起来形成自定义的分析器用于不同的目的



##### 内置分析器

> ES附带了可以直接适用的预包装的分析器，列举几个重要的分析器，看看它们的差异

```sh
#字符串
"Set the shape to semi-transparent by calling set_trans(5)"
```

1. 标准分析器

   ES默认的分析器，它根据Unicode定义的**单词边界**划分文本，删除大部分标点，最后将词条小写

   ~~~shell
   #分析结果词条为
   set, the, shape, to, semi, transparent, by, calling, set_trans, 5
   ~~~

2. 简单分析器

   - 在任何不是字母的地方分隔文本，将词条小写

   - ~~~shell
     #分析结果为：
     set, the, shape, to, semi, transparent, by, calling, set, trans
     ~~~

3. 空格分析器

   - 在空格的地方分隔文本

   - ~~~shell
     Set, the, shape, to, semi-transparent, by, calling, set_trans(5)
     ~~~

4. 语言分析器

   - 可**用于多种语言**，它们可以考虑指定语言的特点。例如：英语分析器附带了一组英语无用词(例如and the，它们对相对性没有影响)，它们会被删除

   - ~~~shell
     #英语分词器产生的结果
     set, shape, semi, transpar, call, set_tran, 5
     ~~~



##### 什么时候适用分析器

> 当我们索引一个文档，它的全文域被分析成词条，以用来创建倒排索引

全文查询中，需要将查询字符串通过相同的分析过程，以保证我们搜索的词条格式与索引中的词条格式一致



全文查询中，理解每个域是如何正确定义的，因此它们可以做正确的事

1. 当查询一个全文域时，会对查询字符串应用相同的分析器，以产生正确的搜索词条列表
2. 当查询一个精确值时，不会分析查询字符串，而是直接搜索指定的精确值



### ES原理—索引文档流程分析

> ES中，最重要的原理是：文档的索引（创建）、文档的读取

文档是ES中可搜索的最小单位，文档由一个或多个字段，**ES文档，类似与关系型数据库中的一行记录**







### ES原理-读取文档流程分析

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







### 倒排索引

倒排索引又叫反向索引

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

倒排索引是实现单词-文档矩阵的一种具体存储形式，通过倒排索引可以根据单词快速获取包含这个单词的文档列表

倒排索引主要由两部分组成：单词词典、倒排文档

1. 单词词典
   - 搜索引擎的索引单位是单词，单词词典是由文档集合中出现过的所有单词构成的字符串集合，单词词典内每条索引项记载单词本身的一些信息以及指向倒排列表的指针
2. 倒排列表
   - postingList，倒排列表记载了出现过某个单词的所有文档列表以及单词在该文档中出现的位置信息，每条记录称为一个倒排项，根据倒排列表即可获知哪些文档包含某个单词
3. 倒排文件
   - Inverted file，所有单词的倒排列表往往是顺序的存储在磁盘的某个文件里，这个文件称为倒排文件，倒排文件是存储倒排索引的物理文件

![image-20220621221334545](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206212213673.png)

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



### 

### Elastic.co上的文档讲解的ES

---

### 集群

ES天生就是分布式的，它知道如何通过管理多个节点来提高扩容性和可用性，这也意味着我们的应用无需关注这个问题

当一个节点被选举成为 *主* 节点时， 它将负责管理集群范围内的所有变更，例如增加、删除索引，或者增加、删除节点等。 而**主节点并不需要涉及到文档级别的变更和搜索等操作**，所以当集群只拥有一个主节点的情况下，即使流量的增加它也不会成为瓶颈。 任何节点都可以成为主节点

作为用户，我们可以将请求发送到 *集群中的任何节点* ，包括主节点。 每个节点都知道任意文档所处的位置，并且能够将我们的请求直接转发到存储我们所需文档的节点。 无论我们将请求发送到哪个节点，它都能负责从各个包含我们所需文档的节点收集回数据，并将最终结果返回給客户端。 Elasticsearch 对这一切的管理都是透明的

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





#### 应对故障

> ES可以应对节点故障，当主节点发生故障时，会立即选举出一个新的主节点，并且将副本分片提升为主分片









---

### 数据输入与输出

一个 *对象* 是基于特定语言的内存的数据结构。为了通过网络发送或者存储它，我们需要将它表示成某种标准的格式。 [JSON](http://en.wikipedia.org/wiki/Json) 是一种以人可读的文本表示对象的方法。 它已经变成 NoSQL 世界交换数据的事实标准。当一个对象被序列化成为 JSON，它被称为一个 *JSON 文档* 

**ES是分布式的文档存储**。它能存储和检索复杂的数据结构—序列化成为JSON文档—以 *实时* 的方式。 换句话说，一旦一个文档被存储在 ES中，它就是可以被集群中的任意节点检索到。

在 Elasticsearch 中， **每个字段的所有数据 都是 *默认被索引的*** 。 即**每个字段都有为了快速检索设置的专用倒排索引**。而且，不像其他多数的数据库，它能在 *同一个查询中* 使用所有这些倒排索引，并以惊人的速度返回结果。



#### 文档

在大多数应用中，多数实体或对象可以被序列化为包含键值对的 JSON 对象

ES中，文档有着特殊的定义，它是指最顶层或根对象，这个根对象被序列化为Json并存储到ES中，指定了唯一ID

#### 文档元数据

一个文档不仅仅包含它的数据，也包含元数据——有关文档的信息。**三个必须的元数据元素**为：_index、 _type、 _id

![image-20220622141959281](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221420454.png)



##### _index

> 文档存放在哪里，ES中，数据被存储和索引在分片中，而一个索引仅仅是逻辑上的命名空间，这个命名空间由一个或多个分片组合在一起



##### _type

>  这个文档表示的对象类别，但是，从7.0开始_type类型只有一个： _ doc

它运行在索引中对数据进行逻辑分区，例如：一个products索引中，有kitchen 、electronics等等类别



##### _id

> 文档唯一标识

当它与_index、 _type组合就可以确定ES中的一个文档。当创建一个新的文档时，要么自己提供id，要么ES自动生成

如何在ES中确定一个文档：index、id、type组合起来定位





#### 索引文档（将文档保存到ES中）

1. 索引作为名词时，代表在 ES集群中，可以创建很多不同的索引。
2. 索引作为动词，代表保存一个文档到 ES。就是在 ES创建一个倒排索引的意思
3. 通过index API，文档可以被索引(将文档保存到ES中)

##### 使用自定义id进行索引文档

~~~json
//格式
PUT /{index}/{type}/{id}
{
  "field": "value",
  ...
}

PUT /website/blog/123
{
  "title": "My first blog entry",
  "text":  "Just trying this out...",
  "date":  "2014/01/01"
}
//ES的响应
{
   "_index":    "website",
   "_type":     "blog",
   "_id":       "123",
   "_version":  1,
   "created":   true
}
~~~

该响应表明文档已经成功创建，该索引包括 `_index` 、 `_type` 和 `_id` 元数据， 以及一个新元素： `_version` 

在 ES中每个文档都有一个版本号。当每次对文档进行修改时（包括删除）， `_version` 的值会递增



##### Autogeneraing Ids

若索引没有自定义id，那么ES会帮我们自动生成ID

~~~json
//采用post
POST /website/blog/
{
  "title": "My second blog entry",
  "text":  "Still trying this out...",
  "date":  "2014/01/01"
}
//响应
{
   "_index":    "website",
   "_type":     "blog",
   "_id":       "AVFgSgVHUP18jI2wRx0w",
   "_version":  1,
   "created":   true
}
~~~

自动生成的 ID 是基于 Base64 编码且长度为20个字符的 GUID 字符串。 这些 GUID 字符串由可修改的 FlakeID 模式生成，这种模式允许多个节点并行生成唯一 ID ，且互相之间的冲突概率几乎为零。

#### 取回一个文档（查询ES中的文档）

~~~json
GET /index/type/id
GET /index/type/_source
~~~







#### 查询文档是否存在

> 检查一个文档是否存在，可以使用HEAD方式代替GET方法，Head没有返回体，只返回一个HTTP请求头

~~~java
HEAD /xxIndex
~~~





#### 更新文档

> ES中的文档是不可改变的，不能修改它们。
>
> 如果想要更新现有的文档，需要重建索引或进行替换

~~~json
PUT /website/blog/123
{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}

{
  "_index" :   "website",
  "_type" :    "blog",
  "_id" :      "123",
  "_version" : 2,
  "created":   false 
}
~~~

响应体中，**ES改变了_version字段的值为2，created标识为false，是因为相同的索引、类型、Id的文档已经存在**。

但是，在内部ES 仅仅是将旧文档标记为已删除，并增加一个全新的文档。 尽管你不能再对旧版本的文档进行访问，但它**并不会立即消失**。当继续索引更多的数据，ES会在后台清理这些已删除文档。

更新文档，实际上，ES内部，是按照以下过程执行的

1. 从旧文档构建JSON
2. 更改该JSON
3. 删除旧文档
4. 索引一个新文档





#### 创建新文档

如何确保我们创建的文档是一个新的，而不是覆盖现有的？

答案：使用ES的自动生成唯一id，因为ES中，一个文档是通过_index、 _type、 _id一起组合来标识的

~~~json
POST /indexName/type(_doc){

}
~~~







#### 删除文档

~~~json
DELETE /index/type/id
~~~

如果没有找到文档，_version也会增加



#### 处理冲突

Elasticsearch 是分布式的。当文档创建、更新或删除时， 新版本的文档必须复制到集群中的其他节点。Elasticsearch 也是异步和并发的，这意味着这些复制请求被并行发送，并且到达目的地时也许 *顺序是乱的* 。 Elasticsearch 需要一种方法确保文档的旧版本不会覆盖新的版本

**ES采用乐观并发控制，即上述的version字段**



#### 文档部分更新

1. update时候，接收文档的一部分作为doc的参数，并且将他们进行合并。对象合并到一起，覆盖现有字段，增加新的字段

2. 使用脚本来部分更新文档

   - 脚本可以用来改变_source的内容，它在更新脚本中称为：ctx. _source

   - ~~~json
     post /website/blog/1/_update
     {
         "script":"ctx.source.views+=1"
     }
     ~~~





#### 取回多个文档

将多个请求合并成一个，避免单独处理每个请求花费的网络延时和开销。 

**如果你需要从 ES检索很多文档，那么使用 *multi-get* 或者 `mget` API 来将这些检索请求放在一个请求中**，将比逐个文档请求更快地检索到全部文档

`mget` API 要求有一个 `docs` 数组作为参数，每个元素包含需要检索文档的元数据， 包括 `_index` 、 `_type` 和 `_id` 。如果你想检索一个或者多个特定的字段，那么你可以通过 `_source` 参数来指定这些字段的名字



#### 代价较小的批量操作

> bluk API运行在单个步骤中进行多次create、index、update、delete请求
>
> 比如：需要索引一个数据流比如日志事件，它可以排队和索引数百或千批次

~~~json
{action:{metadata}}\n
{request body   }\n
{action:{metadata}}\n
{request body   }\n
.....
~~~



bluk与其他的请求格式稍有不同

1. 每行一定要以换行符结尾，包括最后一行也需要换行符
2. 这些行不能包含未转义的换行符，因为它们将会对解析造成干扰。



action/metadata行：指定哪一个文档做什么操作，以下是必选项之一：

1. create：如果文档不存在，那么就创建它
2. index：创建一个新文档或替换一个现有的文档
3. update：部分更新一个文档
4. delete：删除一个文档



metadata：用于指定被索引、创建、更新、删除文档的_index 、 _type、 _id（不过，id不说必须的）。即文档的元数据的三要素

~~~json
{"delete":{"_index":"website","_type":"blog","_id":"123"}}
~~~

request body行：由文档的_source本身组成

~~~json
{ "create":  { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "My first blog post" }

若不指定id，将会自动生成一个ID
~~~



为了把所有的操作组合在一起，一个完整的bulk可能会是类似这样的

注意：

1. delete没有请求体
2. 最后一行的换行符不要忘记

~~~json
POST /_bulk
{ "delete": { "_index": "website", "_type": "blog", "_id": "123" }} 
{ "create": { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "My first blog post" }
{ "index":  { "_index": "website", "_type": "blog" }}
{ "title":    "My second blog post" }
{ "update": { "_index": "website", "_type": "blog", "_id": "123", "_retry_on_conflict" : 3} }
{ "doc" : {"title" : "My updated blog post"} } 

~~~



##### 批量请求多大算大？

整个批量请求都需要由接收到请求的节点加载到内存中，因此该请求越大，其他请求所能获得的内存就越少。 批量请求的大小有一个最佳值，大于这个值，性能将不再提升，甚至会下降。 但是最佳值不是一个固定的值。它完全取决于硬件、文档的大小和复杂度、索引和搜索的负载的整体情况。

这个最佳点很容易找到：通过批量索引典型文档，并不断增加批量大小进行尝试。 当性能开始下降，那么你的批量大小就太大了。一个好的办法是开始时将 1,000 到 5,000 个文档作为一个批次, 如果你的文档非常大，那么就减少批量的文档个数

密切关注你的批量请求的物理大小往往非常有用，一千个 1KB 的文档是完全不同于一千个 1MB 文档所占的物理大小。

 **一个好的批量大小在开始处理后所占用的物理大小约为 5-15 MB**







---

### 分布式文档存储



#### 路由一个文档到一个分片

> 当索引一个文档的时候，文档会被存储到一个主分片中。ES如何知道这个文档放到哪个分片？



公式为：shard=hash(routing)%number_of_primary_shards

1. routing是一个可变值，默认为文档_id，也可以设置为自定义的值
2. number_of_primary_shards：主分片的数量
3. hash后取余，这个余数就是（0-主分片数量-1）之间的数，即文档所在分片的位置

解释：默认根据文档_id做hash运算，得到一个hash值，再利用这个值对主分片数取模，便得到该存放到哪个分片上面

**这就解释了为什么我们要在创建索引的时候就确定好主分片的数量 并且永远不会改变这个数量：因为如果数量变化了，那么所有之前路由的值都会无效，文档也再也找不到了**



所有的文档 API（ `get` 、 `index` 、 `delete` 、 `bulk` 、 `update` 以及 `mget` ）都接受一个叫做 `routing` 的路由参数 ，**通过这个参数我们可以自定义文档到分片的映射**。一个自定义的路由参数可以用来确保所有相关的文档——例如所有属于同一个用户的文档——都被存储到同一个分片中



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





#### 局部更新一个文档

update Api结合了读取和写入模式

![image-20220622171636315](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206221716854.png)



部分更新一个文档的步骤：

1. 客户端向 `Node 1` 发送更新请求。
2. 它将请求转发到主分片所在的 `Node 3` 。
3. `Node 3` 从主分片检索文档，修改 `_source` 字段中的 JSON ，并且尝试重新索引主分片的文档。 如果文档已经被另一个进程修改，它会重试步骤 3 ，超过 `retry_on_conflict` 次后放弃。
4. 如果 `Node 3` 成功地更新文档，它将新版本的文档并行转发到 `Node 1` 和 `Node 2` 上的副本分片，重新建立索引。 一旦所有副本分片都返回成功， `Node 3` 向协调节点也返回成功，协调节点向客户端返回成功

**基于文档的复制**

当主分片把更改转发到副本分片时， 它不会转发更新请求。 相反，它转发完整文档的新版本。请记住，这些更改将会异步转发到副本分片，并且不能保证它们以发送它们相同的顺序到达。 如果Elasticsearch仅转发更改请求，则可能以错误的顺序应用更改，导致得到损坏的文档。







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





---

### 搜索-最基本的工具

> ES真正强大之处在于，可以从无规律的数据中，找出有意义的信息—从大数据->到大信息

为了能被搜索到，会为文档添加索引(indexes)，这也是为什么使用结构化的JSON文档，而不是无结构的二进制数据

文档中的**每个字段都将被索引**，并且可以被查询



为了充分挖掘ES的能力，需要掌握三个重要的概念

1. 映射：描述数据再每个字段内如何存储
2. 分析：全文是如何处理的，使之可以被搜索到
3. 领域特定查询语言：DSL，ES中强大而灵活的查询语言



#### 空搜索

> 搜索API的最基础形式，没有指定任务查询的空搜索，它返回集群中索引索引下的索引文档

~~~json
GET /_search
~~~

返回结果中最重要的是hits(hits翻译：采样数)

~~~json
{
   "hits" : {
      "total" :       14,     //匹配到的文档数
      "hits" : [		      //hit数组：查询结果的前十个文档
        {
          "_index":   "us",
          "_type":    "tweet",
          "_id":      "7",
          "_score":   1,
          "_source": {
             "date":    "2014-09-17",
             "name":    "John Smith",
             "tweet":   "The Query DSL is really powerful and flexible",
             "user_id": 2
          }
       },
        ... 9 RESULTS REMOVED ...
      ],
      "max_score" :   1     //与查询所匹配文档的_score的最大值
   },
   "took" :           4,    //花费的实际
   "_shards" : {            //查询中，参与分片的总数，以及这些分片成功了多少，失败了多少个
      "failed" :      0,
      "successful" :  10,
      "total" :       10
   },
   "timed_out" :      false
}
~~~



#### 分页

1. size：应该返回的结果数量，默认10
2. from：应该跳过的初始结果数量，默认0

例如：GET /_search?size=5&from=0

但是出现深度分页的情况时候，这是有问题的







---

### 映射与分析

为了促进全文域的查询，ES首先会分析文档，根据分析结果，创建倒排索引

#### 倒排索引

> 倒排索引结构，适用于快速的全文搜索
>
> 一个倒排索引由：文档中所有不重复的**词列表构成**，对于每一个词，有一个包含它的文档列表

我们只能搜索在索引中出现的词条，索引索引文本和查询字符串必须标准化为相同的格式



#### 分析和分析器

分析包含两个过程

1. 将一块文本分词适合于倒排索引的独立词条
2. 将这些词条统一化为标准格式，以提高它们的可搜索性

分析器，执行上面的工作



#### 映射

为了能够将时间域视为时间，数字域视为数字，字符串域视为全文或精确值字符串， Elasticsearch 需要知道每个域中数据的类型。这个信息包含在映射中

索引中每个文档都有类型，每种类型都有自己的映射，映射定义了类型中的域，每个域的数据类型，以及Elasticsearch如何处理这些域。映射也用于配置与类型有关的元数据

#### ES简单核心域类型

- 字符串: `string`
- 整数 : `byte`, `short`, `integer`, `long`
- 浮点数: `float`, `double`
- 布尔型: `boolean`
- 日期: `date`

索引一个包含新域的文档时（之前未曾出现）， Elasticsearch 会使用 [*动态映射*](https://www.elastic.co/guide/cn/elasticsearch/guide/current/dynamic-mapping.html) ，通过JSON中基本数据类型，尝试猜测域类型

| **JSON type**                  | **域 type** |
| ------------------------------ | ----------- |
| 布尔型: `true` 或者 `false`    | `boolean`   |
| 整数: `123`                    | `long`      |
| 浮点数: `123.45`               | `double`    |
| 字符串，有效日期: `2014-09-15` | `date`      |
| 字符串: `foo bar`              | `string`    |



#### ES复杂核心域类型

> 除了简单标量数据类型外，Json还要null、数组、对象，这些ES都是支持的

##### 多值域

以数组的形式索引

~~~json
{"tag":["search","nosql"]}
~~~

数组中所有的值必须是形同数据类型的，不能将日期和字符串混在一起



##### 空域

> Lucene是不能存储null值的，所以认为存在null值的域为空域

空域，是不会被索引的，以下三种域认为是空的

~~~json
"null_val":null,
"empty_arr":[],
"arr_with_null_val":[null]
~~~



##### 多层级结构

> 对象内部还会嵌入一个对象

~~~json
{
    "tweet":            "Elasticsearch is very flexible",
    "user": {
        "id":           "@johnsmith",
        "gender":       "male",
        "name": {
            "full":     "John Smith",
            "first":    "John",
            "last":     "Smith"
        }
    }
}
~~~



##### 内部对象的映射

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



##### 内部对象是如何索引的

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



##### 内部对象数组

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





#### 查看映射

例如：查看gb索引的映射

~~~json
GET /index/_mapping
GET /gb/_mapping/
~~~

ES根据索引的文档，为域(属性)动态生成映射

~~~json
{
   "gb": {
      "mappings": {
         "tweet": {
            "properties": {
               "date": {
                  "type": "date",
                  "format": "strict_date_optional_time||epoch_millis"
               },
               "name": {
                  "type": "string"
               },
               "tweet": {
                  "type": "string"
               },
               "user_id": {
                  "type": "long"
               }
            }
         }
      }
   }
}
~~~



#### 自定义映射

自定义映射允许你执行下面的操作：

- 全文字符串域和精确值字符串域的区别
- 使用特定语言分析器
- 优化域以适应部分匹配
- 指定自定义数据格式
- 还有更多

域最重要的属性是 `type` 。对于不是 `string` 的域，一般只需要设置 `type`

默认， `string` 类型域会被认为包含全文。就是说，它们的值在索引前，会通过一个分析器，针对于这个域的查询在搜索前也会经过一个分析器



string域映射有两个重要属性：index、analyzer(啊了来zer)

1. index属性：用于控制怎样索引字符串
   - analyzed：首先分析字符串，然后索引它。它是默认值
   - not_analyzed：索引这个域，它能被搜索，但索引的是精确值，不会对它进行分析
   - no：不索引这个域，所以这个域不能被搜索到
2. analyzer属性：指定字符串域在索引和搜索时使用的分析器
   - 默认：ES的标准分析器，standard分析器
   - 也可以指定其他的分析器
   - 当然，还可以自定义分析器



#### 更新映射

> 不能修改已经存在的域映射。一个域映射已经存在，那么该域的数据很可能已经索引了，如果去试图修改，索引的数据可能出错，不能被正常的搜索
>
> 我们可以更新一个映射来添加一个新域

先删除索引，在创建一个新的索引







---

### 排序与相关性

> 默认情况下，返回的结果是按照**相关性**进行排序的，即最相关的文档排在最前



#### 排序

为了按照相关性来排序，需要将相关性表示为一个数值。在 Elasticsearch 中， **相关性得分**由一个浮点数进行表示，并在搜索结果中通过 `_score` 参数返回， 默认排序是 `_score` 降序



#### 相关性

每个文档都有相关性评分，用一个正浮点数字段 `_score` 来表示 。 `_score` 的评分越高，相关性越高

ES中的相似度算法定义为检索词频、反向文档频率、TF/IDF

1. 检索词频
   - 检索词在该字段出现的频率，出现频率越高，相关性也越高。 字段中出现过 5 次要比只出现过 1 次的相关性高
2. 反向文档频率
   - 每个检索词在索引中出现的频率，频率越高，相关性越低。检索词出现在多数文档中会比出现在少数文档中的权重更低
3. TF/IDF
   - 字段的长度是多少，长度越长，相关性越低。 检索词出现在一个短的 title 要比同样的词出现在一个长的 content 字段权重更大

##### 评分标准

ES在 每个查询语句中都有一个 explain 参数，将 `explain` 设为 `true` 就可以得到更详细的信息。explain可以让返回结果添加一个_score评分的得来依据。explain的输出结果代价是相当昂贵的，只能用作调试工具，不能用于生产环境

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



### ES安装分词器

> ES自带的英文分词器太简单，对于中文的分词，需要安装一个叫做**iK分词器**来解决对中文的分词问题

安装步骤，具体搜索即可！

ik分词器带有两个分词器

1. ik_max_word
   - 会将文本做**最细粒度的拆分**，尽可能的拆分初词语句子
   - 例如：我爱我的都祖国
   - 结果：我、爱、我的、祖、国、祖国
2. ik_smart
   - 会**做最粗粒度的拆分**，已被分出的词语不会再次被其他词语占有
   - 例如：中华人民共和国国歌
   - 结果：中华人民共和国、国歌



### ES安装sql插件

> 对于一些复杂的查询，可以利用sql语句来实现ES的查询

ES6.3之后，ES自带了sql插件，可以直接通过sql语句的方式，来实现ES的数据查询



1. 可以通过kibana里面的rest风格进行操作
2. 进入es里面进行sql查询操作
3. 通过JDBC，利用Java来操作



### Java如何于ES交互

1. 节点客户端
   - node client：节点客户端作为一个非数据节点加入到本地集群中
   - 换句话说：它本身不保存任何数据，但是它知道数据在集群的哪个节点中，并且可以把请求转发的正确的节点
2. 传输客户端
   - transport client：轻量级的传输客户端，可以将请求发送到远程集群
   - 它本身不加入集群，但是它可以将请求转发到集群中的一个节点上
