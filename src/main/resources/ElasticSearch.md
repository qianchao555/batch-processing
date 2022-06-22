## ElasticSearch

ES是一款非常强大的、基于Lucene的开源**搜索以及分析引擎**，它是一个实时的分布式搜索分析引擎，能让你以前所未有的速度和规模去去探索你的数据

除了搜索，结合Kibana、Logstash、Beats开源产品、Elastic技术栈（ELK)还被广泛运用在大数据近实时分析领域。包括：日志分析、指标监控、信息安全等等。它可以帮助我们探索海量结构化、非结构化数据，按需创建可视化报表，对监控数据设置报警阈值，通过机器学习，自动识别异常状况

ES基于Restful Web Api，使用Java开发的搜索引擎库，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎

### 为什么不直接使用Lucene

Lucene：**全文搜索引擎**

Lucene是当下最先进、高性能、全功能的搜索引擎库，但是 Lucene 仅仅只是一个库。为了充分发挥其功能，你需要使用 Java 并将 Lucene 直接集成到应用程序中。 更糟糕的是，您可能需要获得信息检索学位才能了解其工作原理。Lucene 非常 复杂

Elasticsearch 也是使用 Java 编写的，它的内部使用 Lucene 做索引与搜索，但是它的目的是使全文检索变得简单，**通过隐藏 Lucene 的复杂性，取而代之的提供一套简单一致的 Restful API**



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

一个集群由一个唯一的名字标识，默认为elasticsearch。集群名称非常重要，具有相同集群名的节点才会组成一个集群，集群名称可以在配置文件中指定

#### Node节点

存储集群的数据，参与集群的索引和搜索功能。像集群有名字，节点也有自己的名称，默认在启动时会以一个随机的UUID的前七个字符作为节点的名字，你可以为其指定任意的名字。通过集群名在网络中发现同伴组成集群。一个节点也可是集群

#### Index索引

一个索引是一个文档的集合（等同于solr中的集合）。每个索引有唯一的名字，通过这个名字来操作它。一个集群中可以有任意多个索引

#### Type类型

指在一个索引中，可以索引不同类型的文档，如用户数据、博客数据。从6.0.0 版本起已废弃，一个索引中只存放一类数据

#### Document文档

被索引的一条数据，索引的基本信息单元，以JSON格式来表示

#### Shard分片

在创建一个索引时可以指定分成多少个分片来存储。每个分片本身也是一个功能完善且独立的“索引”，可以被放置在集群的任意节点上

#### Replication备份

一个分片可以有多个备份（副本）

![image-20220619214242668](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192143891.png)



---

### Elastic Stack生态(ELK)

Beats+ElasticSearch+Logstash+Kibana

下图展示了ELK生态以及基于ELK的场景(深绿色部分)

![image-20220619214741901](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192147011.png)



![image-20220619214931561](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206192149659.png)



#### Beats

Beats是一个面向**轻量型采集器**的平台，这些采集器可以从边缘机器向Logstash、ElasticSearch发送数据，它是由Go语言进行开发的，运行效率方面比较快。从下图中可以看出，不同Beats的套件是针对不同的数据源。

#### Logstash

Logstash是**动态数据收集管道**，拥有可扩展的插件生态系统，支持从不同来源采集数据，转换数据，并将数据发送到不同的存储库中。其能够与ElasticSearch产生强大的协同作用，后被Elastic公司在2013年收购

它具有如下特性：

1. 实时解析和转换数据
2. 可扩展，具有200多个插件
3. 可靠性、安全性。Logstash会通过持久化队列来保证至少将运行中的事件送达一次，同时将数据进行传输加密
4. 监控

#### ElasticSearch

ElasticSearch对数据进行**搜索、分析和存储**，其是基于JSON的分布式搜索和分析引擎，专门为实现水平可扩展性、高可靠性和管理便捷性而设计的

它的实现原理主要分为以下几个步骤

1. 首先用户将数据提交到ElasticSearch数据库中
2. 再通过分词控制器将对应的语句分词
3. 将分词结果及其权重一并存入，以备用户在搜索数据时，根据权重将结果排名和打分，将返回结果呈现给用户

#### Kibana

Kibana实现**数据可视化**，其作用就是在ElasticSearch中进行展示。Kibana能够以图表的形式呈现数据，并且具有可扩展的用户界面，可以全方位的配置和管理ElasticSearch



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

### 索引

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



#### 索引的格式

在请求体里面传入设置或类型映射

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

一旦索引被关闭，那么这个索引只能显示元数据信息，不能进行读写操作

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

### ES查询-复合查询

ES的查询基于JSON风格的DSL来实现的，DSL:Domain Specific Language

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

**match多个词的本质**

它在内部实际上先执行两次term查询，然后将两次查询的结果合并作为最终结果输出。为了做到这点，它将两个term查询合并到了一个bool查询中

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

使用具有严格语法的解析器提供的查找字符串返回文档

此查询使用一种语法根据运算符（and or）来解析和拆分提供的查询字符串，然后，查询在返回匹配文档之前，独立分析每个拆分文本

可以使用`query_string`查询来创建包含通配符、跨多个字段的搜索等复杂搜索。 虽然用途广泛，但查询很严格，如果查询字符串包含任何无效语法，则会返回错误

#### Intervals类型

Intervals：时间间隔，本质上是将多个规则按照顺序匹配



---

### ES查询-Term

DSL查询另一种极为常用的是对词项进行搜索，官方称为term level查询

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

### ES聚合-聚合查询Bucket

除了查询之外，最常用的就是聚合了。ES提供了三种聚合方式：桶聚合、指标聚合、管道聚合

ES中桶的概念类似于SQL中的分组，而指标类似于count、sum、max等统计方法

Buckets：满足特定条件的文档的集合

Metrics：指标，对桶内的文档进行统计计算

指标聚合与桶聚合大多数情况组合在一起使用，桶聚合本质上是一种特殊的指标聚合，它的聚合指标就是数据的条数(count)

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

ES支持一些基于脚本(生成运行时的字段)的复杂的动态聚合



---

### ES聚合-Metrics

指标聚合Metrics Aggregation





---

### ES聚合-Pipline

管道聚合：让上一步的聚合结果成为下一个聚合的输入，这就是管道

管道聚合有很多不同**类型**，每种类型都与其他聚合计算不同的信息，但是可以将这些类型分为两类

1. 父级：父级聚合的输出提供了一组管道聚合，它可以计算新的存储桶或新的聚合以添加到现有存储桶中
2. 兄弟：同级聚合的输出提供的管道聚合，并且能够计算与该同级聚合处于同一级别的新聚合

前置聚合可能是Bucket聚合，后置的可能是基于Metric聚合，那么它就可以成为一类管道，进而引出了：`xxx bucket`

- Bucket聚合->metric聚合：bucket聚合的结果，成为下一步metric聚合的输入
  - Average bucket
  - Min bucket
  - Max bucket
  - Sum bucket
  - Stats bucket
  - Extended stats bucket



---

### ES原理初步认识

集群中，一个白正方形代表一个节点-Node，节点之间多个绿色小方块组成一个ES索引，一个ES索引本质是一个Lucene Index

一个索引下，分布在多个Node中的绿色方块为一个分片-Shard

即Shard=Lucene Index

![image-20220620211959189](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202120849.png)

![image-20220620212355322](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202123427.png)

Lucene是一个全文搜索库，ES建立在Lucene之上



#### Lucene

在Lucene里面有很多的segment，我们可以把它们看成Lucene内部的mini-index

![image-20220620212718856](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206202127950.png)



segment内部有很多数据结构

1. **Inverted index**

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

1. 一个ES集群模式下，有多个Node组成，每个节点就是ES的一个实例
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

分析器实际上是将三个功能封装到了一起

1. 字符过滤器
   - 字符串按照顺序通过每个字符过滤器
   - 字符过滤器的任务是在分词前整理字符串
2. 分词器
   - 字符串被分词器分为单个的词条
3. Token过滤器
   - 词条按照顺序通过tokent过滤器
   - 这个过程可能会改变词条(例如：小写化)、删除词条(例如：a、and、the这些无用词)

ES提供了开箱即用的字符过滤器、分词器和tokent过滤器，这些可以组合起来形成自定义的分析器用于不同的目的

#### 内置分析器

```sh
#字符串
"Set the shape to semi-transparent by calling set_trans(5)"
```

##### 标准分析器

ES默认的分析器，它根据Unicode定义的**单词边界**划分文本，删除大部分标点，最后将词条小写

~~~sh
#分析结果词条为
set, the, shape, to, semi, transparent, by, calling, set_trans, 5
~~~

##### 简单分析器

在任何不是字母的地方分隔文本，将词条小写

~~~sh
#分析结果为：
set, the, shape, to, semi, transparent, by, calling, set, trans
~~~

##### 空格分析器

在空格的地方分隔文本

~~~sh
Set, the, shape, to, semi-transparent, by, calling, set_trans(5)
~~~

##### 语言分析器

可用于多种语言，它们可以考虑指定语言的特点。例如：英语分析器附带了一组英语无用词(例如and the，它们对相对性没有影响)，它们会被删除

英语分词器产生的结果

~~~sh
set, shape, semi, transpar, call, set_tran, 5
~~~





---

### ES原理-索引文档流程

ES中最重要的就是文档的索引和文档的读取

#### 文档索引步骤

##### 单个文档查询所需的步骤

以下是从主分片或副本分片检索文档步骤

![image-20220621150314799](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/es_img/202206211503663.png)

1. 客户端向Node1发送获取请求
2. Node使用文档的_id确定文档属于分片0，分片0的副本分片存在于Node3上，请求会被转发到node3，因为分片0的主分片被分配在node3上
3. node3在主分片上面执行请求，如果成功了，它将请求并行转发到node1和node2的副本上，一旦所以的副本分片都报告成功，node3将向协调节点报告成功，协调节点向客户端报告成功





---

### 倒排索引





















