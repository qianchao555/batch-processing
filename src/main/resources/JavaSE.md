## 集合容器

Java体系中容器主要包括Collection和Map两种，Collection存放对象的集合，Map存放k-v对的映射表

Collection主要包含List、Set、Queue

### List

#### ArrayList源码分析

针对：JDK8

ArrayList是顺序容器，即：元素存放的数据与放进去的顺序相同，运行放入null元素，底层通过数组实现。未实现同步机制，其余与Vector大致相同

创建ArrayList时，默认ArrayList对象中的elementData长度是空的{}，即：arr.length=0；size是(elementData中元素包含的个数)0，当第一次add的时候，elementData将会变成默认大小为10



##### ArrayList自动扩容机制

每当向数组中添加元素时，都要去检查添加后元素的个数是否会超出当前数组的长度，如果超出，数组将会进行扩容，以满足添加数据的需求

数组进行扩容时，会将老数组中的元素重新拷贝一份到新的数组中，每次数组容量的增长大约是其原容量的1.5倍(oldCapacity + (oldCapacity >> 1)) => (10 + 10>>1)  ==>10+5 =15

这种操作的代价是很高的，因此在实际使用时，我们应该尽量避免数组容量的扩张。当我们可预知要保存的元素的多少时，要在构造ArrayList实例时，就指定其容量，以避免数组扩容的发生

每次add的时候，都会在添加之前检查是否能存放，否则会进行自动扩容 grow()方法



##### ArrayList添加和删除的性能

时间复杂度O

添加：

1. 指定位置后面的元素会依次向右移动一个位置：O(n)
2. 直接在末尾添加：O(1)

删除：

1. 后面的元素后依次向左移动一个位置，将最后一个元素置null，好让垃圾收集器进行回收，remove方法不会让数组的长度缩减，只是将最后一个数组元素置空而已：O(n)

这就导致了在指定位置添加和删除的性能消耗大！



##### ArrayList Fail-Fast机制

使用iterator遍历可能会引发多线程异常

ArrayList采用了快速失败机制，通过记录modCount参数来实现，在面对并发修改时，迭代器很快就会完全失败，而不是冒着在将来某个不确定的时间发生任何不确定行为的风险



##### ArrayList序列化机制

transient Object[] elementData ，ArrayList元素数组是不会被序列化的

ArrayList自己实现了序列化与反序列化的方法

writeObject(ObjectOutputStream)：序列化

readObject(ObjectInputStream) ：反序列化

与直接序列化Object数组相比，这样做的优势：

1. elementData是一个Object数组，它通常会预留一些容量，等容量不足时再进行扩容
2. 有些空间(elementData[size] ~ elementData[elementData.length-1])实际上并没有存储元素(存的是null)

ArrayList的序列化机制：elementData定义为transient的优势，自己根据size序列化真实的元素，**只序列化实际存储的集合元素，而不是去实例化整个Object数组，从而节省空间和时间的消耗**


---



### HashMap

#### HashMap结构

内部有一个非常重要的属性Node，是HashMap的一个内部类，其实现了Map.Entry接口

其中Node中包含一个Node next，从而形成链表结构

#### HashMap容量

~~~java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
~~~

默认容量16=》数组大小为10

为什么初始值为16：为了降低hash碰撞的几率



#### HashMap负载因子

为什么需要负载因子：

其表示hash表中，元素的填满程度

负载因子=填入表中的元素个数/散列表的长度

负载因子越大，则填满的元素越多，空间利用率越高，但是发生hash冲突的概率就越大

负载因子越小，则填满的元素越少，空间浪费多，发生hash冲突的概率越低

所以需要在：空间利用率和冲突概率之间寻找一种平衡与折中

~~~java
static final float DEFAULT_LOAD_FACTOR = 0.75f;
~~~

HashMap的负载因子默认为0.75

为什么是0.75？

为了减少冲突发生的概率，当HashMap的数组长度达到一定的临界值时，就会触发扩容。扩容为原来的两倍（数组扩大为原来的两倍）然后重新计算每个元素在数组中的位置，然后再进行存储

根据负载因子和容量可以得到一个临界值：threshold=capacity*loadFactor

例如：16*0.75=12，则第13次put时候就会触发扩容机制

为什么是0.75：统计学里面的一个原理——泊松分布



#### HashMap的hash()算法

~~~java
static final int hash(Object key) {
     int h;
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
~~~

逻辑：先获得key的hashCode值h，再让h和h右移16位做异或操作

实际上就是把一个数的低16位 与 高16位做异或运算。目的是减少hash冲突

#### Hash算法

hash其实就是一个函数，该函数实现的就是一种算法，通过一系列的算法来得到一个hash值，通过hash算法得到的hash值存放与hash表中

#### HashCode

1. hashCode就是通过hash函数得到的，hashcode就是在hash表中有对应的位置
2. 每个对象都有hashcode，对象的hashcode是通过对象的内部地址(也就是物理地址)转换成一个整数，然后该整数通过hash函数的算法就得到了hashcode(不同jvm的实现不同）。
3. 比如：hash表中有hashcode为1、hashcode为2、3、4、5、6、7、8等这样八个位置，有一个对象A，假如A的物理地址转换为一个整数17，就通过hash算法(假如直接取余算法)，17%8=1，那么A的hashcode就为1，则A就在hash表中1的位置

##### 为什么使用hashcode

1. 为了查找的快捷性，HashCode是用来在散列存储结构中确定对象的存储地址的
2. hashmap之所以快，就是因为使用了散列表，根据key的hashcode值，生成数组下标以得到对应的对象

##### equals

默认对象的equals，比较的是内存地址值，除开String对象外，因为它重写了此方法

将对象存入散列集合的流程

1. 判断对象是否与集合中任意一个对象的hashcode是否相等
   - 不相等，则加入对象
   - 相等，则判断对象与任意一个对象的equals方法是否相等
2. equlas判断情况
   - 不相等，则加入对象
   - 相等，则放弃加入

#### 解决hash冲突的方法

1. 开放定制法
   - 线性探查法
   - 平方探测法
   - 伪随机探测法
   
2. 再hash法

3. 链地址法
   - HashMap采用了此种方法
   - 将冲突位置的元素构造成链表，在添加数据的时候如果发生冲突就放在这个位置的链表上
   
   ![image-20220421230927994](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javase_img/202204212310674.png)
   
4. 建立一个公共溢出区



#### HashMap的数组+链表/红黑树问题

##### 为什么引入链表

HashMap底层是数组，当put操作时候，会进行hash计算，判断这个对象属于数组的那个位置。当多个对象值出现在同一个数组位置上面的时候，就会有hash冲突，从而引入了链表来对其进行链式的存放

##### 为什么引入红黑树

当链表长度大于8时，遍历查询效率慢，故而引入红黑树

##### hashmap为什么一开始不就使用红黑树

因为红黑树相对于链表维护成本大，红黑树在插入新数据之后，可能会通过左旋、右旋、变色来保持平衡，造成维护成本过高，故链路较短时，不适合用红黑树

##### HashMap的底层数组取值的时候，为什么不用取模，而是&

因为&比取模运算的性能更快

~~~java
tab[i=(n-1) & hash]
~~~



#### 数组的长度为什么是2的n次幂

https://www.itqiankun.com/article/hashmap-basic-question

1. 为了减少hash冲突，让数据均匀分布。一般使用hashCode % size ，这个可以达到最大的平均分配。而(n-1) & hash，当n满足2的n次幂时候，满足公式：（n-1) & hash ==hash%n
2. &速度快，比取模运算%将近快10倍左右
3. 能保证索引值 肯定在capacity中，不会出现数组越界

原因分析

因为HashMap的底层数据结构为：数组+链表，所有hashMap每次保存的数据都是分散保存在数组的各个index位置而为了存取效率达到最高，要求hashMap每次保存的时候尽量平均分散到数组的各个位置，这样可以避免每次存取都要遍历链表造成额外的时间成本开销

所以就通过hashMap对数组长度取余运算（hashCode%size），这样可以达到最大的平均分配；但是hashMap的作者想到了如果通过位运算来计算取余的话效率会比10进制的取余运算来的快，因为计算机的底层运算都是转化位二进制运算的

所以为了位运算的取余效果能达到10进制的取模效果，作者推算出了如果容量为2的N次方的话，那么hash&(length-1) == hash%length

总结：既要达到最大可能的平均分配HashMap的value在tab的各个index，又要用二进制来实现存取效率，所有要求HashMap的容量必须为2的n次幂

如果不考虑性能问题，我们就可以不设置数组的长度是2的n次幂，此时也没有必要 h&(len-1)，而换成 h%length

**如果指定数组长度不为2的n次幂，就破环了数组长度是2的n次幂这个规则了吗？**

~~~java
//不会，因为hashmap里面的tableSizeFor方法做了处理，保证n永远为2的n次幂
static final int tableSizeFor(int cap) {
    //cap-1后，n的二进制最右一位肯定和cap的最右一位不同，即一个为0，一个为1，例如cap=17（00010001），n=cap-1=16（00010000）
    int n = cap - 1;
    //n = (00010000 | 00001000) = 00011000
    n |= n >>> 1;
    //n = (00011000 | 00000110) = 00011110
    n |= n >>> 2;
    //n = (00011110 | 00000001) = 00011111
    n |= n >>> 4;
    //n = (00011111 | 00000000) = 00011111
    n |= n >>> 8;
    //n = (00011111 | 00000000) = 00011111
    n |= n >>> 16;
    //n = 00011111 = 31
    //n = 31 + 1 = 32, 即最终的cap = 32 = 2 的 (n=5)次方
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
~~~







#### HashMap扩容原理

当map中的Entry的数量大于等于threshold=loadFactor*capacity的时候，且新建的Entry刚好落在一个非空的桶上(数组[i])上，此时触发扩容机制，将其容量扩大为原来的两倍

#### put过程

1. 对key的hashCode()做hash运算，计算出index
2. 如果没有hash碰撞，则调用newNode()来创建Node放入tab中
3. 碰撞了，则以链表的形式存在tab中。当碰撞导致链表长度过长就会将链表转为红黑树
4. 如果某节点已经存在，则新的值替换为旧的值(保证key的唯一性)
5. 如果tab快满了(阈值=loadFacotr*currentCapacity)，则会进行扩容(resize)

#### 为什么转为红黑树



#### HashMap线程不安全问题

产生的原因：

1. Jdk7
   - 扩容造成死循环
   - 扩容造成数据丢失
2. Jdk8
   - 数据覆盖问题
   - 线程A会把线程B插入的数据给**覆盖**，发生线程不安全

如何解决

1. Collections.synchronizedMap(map)，包装成同步Map，原理：在HashMap的所以方法上synchronized
2. ConcurrentHashMap





## 







