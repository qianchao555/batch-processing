## Java面向对象-三大特性

### 封装

> 将数据和基于数据的操作封装起来，数据被保护在内部，尽可能的隐藏内部细节，对外只提供一些接口使其与外部发生联系

优点

1. 提高软件的可重用性
2. 减少耦合：可以独立开发、测试、优化



### 继承

> 继承实现了IS-A的关系
>
> 继承应该遵循里氏替换原则，子类对象必须能够替换掉所有父类对象



使用继承少不了的三个东西

1. 构造器
2. protected访问权限
3. 向上转型



继承的目的

1. 通过继承自父类，提高了代码的可重用性
2. 继承运行通过方法重写来促进多态性



继承的缺点

1. 增加了类之间的耦合度



Java不支持多重继承





### 多态

> 多态分为：编译时多态、运行时多态



#### 编译时多态

只要指的是：方法的重载



#### 运行时多态

指的是：程序中定义的对象引用，所指向的具体类型在运行期间才能确定



运行时多态的三个条件

1. 继承 或（实现接口）
2. 复写（重写）
3. 向上转型



## 基础类型的包装类型

基础类型都有对应的包装类型

~~~java
boolean 1个字节   =>Boolean
byte 1个字节  =》Byte
char 2个字节  =》Charater
short 2个字节 =》Short
int   4个字节 =》Integer
float 4个字节 =》Float
long  8个字节 =》Long
double 8个字节 =》Double
~~~



自动装箱、拆箱

~~~java
Integer a=2; //自动装箱
int x=a;//自动拆箱
~~~

自动装箱过程会调用valueOf()方法，因此，多个Integer实例使用自动装箱来创建值相同的对象，它们引用的是相同的对象

~~~java
Integer a= 123;
Integer b=123;
sout(a==b);   //true   因为a、b引用的是同一个对象
~~~





缓存池

> Java8中，Integer缓存池的大小默认是 -128 ~127

new Integer(123)和Integer.valueOf(123)的区别

1. new Integer(123)：每次都会创建一个新的对象
2. Integer.valueOf(123)：会先使用缓存池中的对象，多次调用会取得同一个对象的引用



如果值在缓冲池之外，则会创建一个新的对象

~~~java
Integer a=200;
Integer b=200;
sout(a==b);  //false   会创建不同的对象了
~~~



## 抽象类和接口

### 抽象类

> 抽象类用abstract关键字声明，抽象类一般包含抽象方法

抽象类和普通类最大的区别：抽象类不能被实例化



### 接口

> 接口是抽象类的延伸
>
> Java8之前，接口可以看成是一个完全的抽象类，不能有任何的方法实现
>
> Java8开始，接口可以有默认方法的实现，因为不支持默认方法的接口的维护成本太高了。Java8之前，一个接口想要添加新的方法，那么就需要修改所有实现了接口的类



抽象类和接口的区别

1. 接口的字段默认是：static和final修饰的，抽象类的字段没有这个限制
2. 一个类可以实现多个接口，但是只能单继承一个类
3. 接口的成员只能是Public的，抽象类的成员可以有多种访问权限



## 关键字

### Final

> 表示不可变变量，==必须在构造函数完成前，完成初始化==

1. 声明数据

   - 声明数据为常量，可以说编译时常量，也可以是运行时被初始化后不能被改变的常量
   - 对于基本类型：final是数值不变
   - 对于引用类型：final使引用不变，也就是不能引用其他对象，但是，引用的对象本身是可以修改的

   ~~~java
   public class Test{
       final int x=1;  //编译期常量
   	x=2; //编译错误
       
       int xyz=5; //实例变量，每个对象独立（但是多线程访问同一个对象的实例变量时，可能会引发线程不安全问题）
       static int xx=20;//所有对象共享（多线程访问同上，会出问题）
       
       static final int yy=30;//所有对象共享，且不可变
       
       //final修饰引用类型时，指向可变对象，多线程访问时可能是线程不安全的哟！！
       static final List<Integer> unsafeList=new ArrayList<>();
       static{
           unsafeList.add(1);
       }
   
   	Random r=new Random();
   	final int k=r.nextInt(); //非编译期常量，只是k的值在初始化后就无法更改
   
   	final A y=new A();
   	y.a=3;
       
       public static void main(String[] args){
           
       }
   }
   
   ~~~

2. 声明方法：方法不能被子类重写

   - private方法隐式被指定为final
   - final方法是可以被重载的

3. 声明类：类不能被继承

   - String类就是final类，final类中，所有的方法都是隐式final的

4. 修饰方法参数：无法在方法中更改参数引用所指向的对象

   - 这个特性主要用来像匿名内部类传递数据



### static

1. 静态变量：又称==类变量==，也就是说这个变量是属于类的
   - 类的所有实例都共享这个静态变量，静态变量在内存中只存在一份
   - 实例变量：每创建一个实例，就会产生一个实例变量，它与该实例同生共死
2. 静态方法
   - 静态方法在类加载的时候就存在了，它不依赖于任何实例，所以：静态方法必须要有实现，即不能说抽象的方法
   - 静态方法==只能访问所属类的静态字段和静态方法==，方法中不能有this、super关键字
3. 静态语句块
   - 在类初始化时，运行一次
4. 静态内部类
   - 非静态内部类：依赖于外部类的实例
   - 静态内部类：不需要依赖外部类的实例
5. 静态导包
6. ==初始化顺序==
   - 静态语句块与静态变量的初始化顺序取决于它们在代码中的顺序
   - 实例变量和普通语句块
   - 构造函数的初始化
   - 存在继承情况时
     - 父类（静态变量、静态块）
     - 子类（静态变量、静态块）
     - 父类（实例变量、普通语句块）
     - 父类构造函数
     - 子类（实例变量、普通语句块）
     - 子类构造函数
   - 类加载阶段
     - 静态变量、静态语句块按照顺序初始化
   - 对象创建
     - 实例变量、普通语句块按照顺序初始化
     - 构造器
     - 注意：**每次创建新对象时**，都会执行实例变量、实例语句块的初始化动作



### static final组合使用

> 表示定义一个类级别的不可变常量

1. 不可变性：分别讨论
   - 注意：
   - 值类型初始化后不可变
   - 引用类型：指的是引用不可变，但是引用的对象里面的值是可以变的
2. 初始化时机：类加载时初始化
   - 必须确保在类加载时初始化：直接赋值或者在静态块中赋值
3. 唯一性：所有实例共享同一份常量值



### final重排序问题

> 重排序：编译器和处理器为了优化性能，可能对指令的执行顺序进行调整，只要最终结果与程序顺序执行的结果一致
>
> 前提：保证单线程执行的正确性（as-if serial语义）
>
> 但是：在多线程环境下，重排序可能导致问题，因为其他线程可能看到不一致的执行顺序，从而导致数据竞争或不一致状态

~~~java
// 示例：普通字段,多线程情况下，可能因重排序导致问题
class Example {
    int x;
    Example() {
        x = 42; // 可能被重排序到构造函数外
    }
}
//若对象引用在其他线程中可见时，x的写入还未完成，其他线程可能看到x=0（默认值）
~~~



#### final字段的重排序规则

> JSR-133(Java 5+)，==为final字段引入了特殊规则==，确保其初始化时在多线程的安全性

- 构造函数内的写操作，不会被重排序到构造函数外
  - 确保对象引用对其他线程可见时，final字段已经正确初始化
- 禁止读取未初始化的final字段
  - 其他线程读取final字段时，会看到构造函数初始化的值，而非默认值（0或null）
- 内存屏障插入
  - JVM在final字段初始化后，插入`StoreStore`屏障，确构造函数退出前，所有final字段的写入，对其他线程可见

#### 构造函数逃逸问题

- 尽管final字段有上述保证，但是，如果在构造函数中泄漏`this引用`（如将对象赋值给静态变量、启动线程等等）
- 那么，其他线程可能访问到未完全初始化的对象，导致final字段的可见性问题

~~~java
public class FinalFieldExample {
    final int x;
    static FinalFieldExample instance;

    public FinalFieldExample() {
        x = 42;
        instance = this; // 构造函数中泄漏this引用
    }

    public static void main(String[] args) {
        new FinalFieldExample();
        // 其他线程可能通过instance访问到x=0
    }
}
~~~



final字段在初始化时的重排序规则是怎样的？

- 根据Java内存模型修订，final字段的写入和对象的引用，在构造函数中的初始化顺序是有规则顺序的
- 如果对象的引用在构造函数完成之前，能被其他线程看到，那么final字段的值，可能无法正确可见
  - 因此，JMM保证在构造函数正确完成之后，final字段的初始化值，对其他线程可见，并且不会发生重排序问题
- 具体说就是：==当一个对象包含final字段时，构造函数中的final字段的初始化写操作，不会被重排序到构造函数之外==
  - 但是，如果在构造函数中，存在逃逸，比如：将this赋值给一个静态变量。那么即使说final字段也会出现问题





## 对象的拷贝

参考文章：https://blog.csdn.net/baiye_xing/article/details/71788741



### 浅拷贝

> 拷贝对象和原始对象引用同一个对象（错误的理解）
>
> 浅拷贝：被复制对象的所有变量都含有与原来的对象相同的值，而所有的对其他对象的引用仍然指向原来的对象。即对象的[浅拷贝](https://so.csdn.net/so/search?q=浅拷贝&spm=1001.2101.3001.7020)会对“主”对象进行拷贝，但不会复制主对象里面的对象。"里面的对象“会在原来的对象和它的副本之间共享。
>
> 简而言之：浅拷贝仅仅复制主对象，而不复制主对象所引用的对象（只是复制了这个对象的地址）



实现浅拷贝

1. 被复制的类需要实现Cloneable接口
2. 重写clone方法

Person p1=new Person();

Person p2=(Person) p1.clone();    //会产生一个新对象，p2指向了一个新的对象，只不过新对象里面的值是p1对象是相同的



### 深拷贝

> 无论被拷贝对象是值类型还是引用类型，在内存中都会生成一个新的对象
>
> 当被拷贝对象和它所引用的对象一起拷贝时即发生深拷贝，即：它自己会产生一个新对象、它引用的对象还会产生一个新对象



有两种方式实现深拷贝

1. 实现Cloneable接口
2. 实现Serializable接口





## 集合容器

Java体系中容器主要包括Collection和Map两种，Collection存放对象的集合，Map存放k-v对的映射表

Collection主要包含List、Set、Queue

### List

#### ArrayList源码分析

针对：JDK8

ArrayList是顺序容器，即：元素存放的数据与放进去的顺序相同，允许放入null元素，底层通过数组实现。未实现同步机制，其余与Vector大致相同

创建ArrayList时，默认ArrayList对象中的elementData长度是空的{}，即：arr.length=0；size是(elementData中元素包含的个数)0，当第一次add的时候，elementData将会变成默认大小为10

##### 底层数据结构

~~~java
transient Object [] elementObject；
private int size;
~~~



##### ArrayList自动扩容机制

每当向数组中添加元素时，都要去检查添加后元素的个数是否会超出当前数组的长度，如果超出，数组将会进行扩容，以满足添加数据的需求

数组进行扩容时，会将老数组中的元素重新拷贝一份到新的数组中，每次数组容量的增长大约是其原容量的1.5倍(oldCapacity + (oldCapacity >> 1)) => (10 + 10>>1)  ==>10+5 =15

这种操作的代价是很高的，因此在实际使用时，我们应该尽量避免数组容量的扩张。当我们可预知要保存的元素的多少时，要在构造ArrayList实例时，就指定其容量，以避免数组扩容的发生

每次add的时候，都会在添加之前检查是否能存放，否则会进行自动扩容 grow()方法

0->10->15->22



##### ArrayList添加和删除的性能

时间复杂度O

添加：

1. 指定位置后面的元素会依次向右移动一个位置：O(n)
2. 直接在末尾添加：O(1)

删除：

1. 后面的元素后依次向左移动一个位置，将最后一个元素置null，好让垃圾收集器进行回收，**remove方法不会让数组的长度缩减**，只是将最后一个数组元素置空而已：O(n)

这就导致了在指定位置添加和删除的性能消耗大！



##### ArrayList排序、判断对象是否相等

判断ArrayList里面的对象，需要重写该对象的equals、hashcode方法

排序操作：

1. 采用集合工具类中提供的方法进行排序：Collections.sort方法，若存放的是自定义对象时：该对象需要实现Compareable接口，并实现compareTo(T o)，按照我们自定义的规则进行排序
2. 使用比较器Comparator
3. Java8：Stream流，arrayList.stream().sorted(Student::age).collection(Collectors.toList);



##### ArrayList Fail-Fast机制

使用ArrayList遍历的时候可能会引发多线程异常

ArrayList采用了快速失败机制，通过记录modCount参数来实现，在面对并发修改时，迭代器很快就会完全失败，而不是冒着在将来某个不确定的时间发生任何不确定行为的风险

使用迭代器remove单线程不会报并发修改错误，因为迭代器remove后，会重新设置expectedModCount=等于外部的modCount变量，从而不会触发快速失败机制(比较modCount)

使用迭代器提供的remove方法时，实际上还是调用的集合的remove方法删除元素，但是在删除元素之后会将expectedModCount重新置为modCount的值，即让这两个值变得想等了，因此单线程下使用迭代器的remove方法删除元素是不会触发快速失败机制的

##### fail-safe 安全失败机制

1. 使用Iterator迭代器的删除方法
2. copyOnWriteArrayList

安全失败，是相对于快速失败而言的，快速失败时立即抛出异常，安全失败则是不会抛出异常，失败的“很安全”，但是，也是属于一个“失败”的操作

采用安全失败机制的集合容器，实际上在遍历时不是直接在原来的集合内容上访问的，而是先复制原有集合内容，在拷贝的集合上进行遍历。这种机制也被称为“**写时复制**”（Copy-On-Write，简称**COW**），很多实现都使用了该机制，比如Redis的快照功能，Redis写快照的时候，就用到了Linux底层的Copy-On-Write技术

原理：迭代时是对原集合的拷贝进行遍历，所以在遍历过程中对原集合所作的修改并不能被迭代器检测到，所以不会触发ConcurrentModificationException

实现例子：CopyOnWriteArrayList






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

#### LinkedList分析

LinkedList实现了List接口和Deque双端队列接口，即：可以将它看作一个顺序容器，又可以看作一个队列，同时又可以看作一个栈

但需要使用栈或队列的时候，可以考虑使用LinkedList，但是首先ArrayDeque，它比LinkedList(当作栈或队列)有着更好的性能

LinkedList没有实现同步，多线程访问时，可以采用Collections.synchronizedList()方法对其进行包装

##### 底层数据结构

双向链表，双向链表的每个节点用内部类Node表示。LinkedList通过first和last引用，分别执行链表的第一个和最后一个元素。没有哑元(头节点前的一个空节点)节点，当链表为空的时候first和last都指向null

~~~java
transient int size = 0;

transient Node<E> first;

transient Node<E> last;

/**
* Node是一个私有内部类
*/
private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

~~~



1. 底层实现了**双向链表**和双端队列的特点
2. 双向链表的每一个节点用内部类Node表示
3. 添加和删除效率高 直接改变指向
4. 查找速度慢 链表需要一直挨着去查找
5. 添加是从尾部添加元素
   - 逻辑：把最后一个节点last取出来=》L(Node)
   - 当前节点pre指向L、next指向null
   - last=newNode

---

### Stack & Queue

使用栈时，Stack已经不推荐使用了，而是使用更高效的ArrayDeque；Queue是一个接口，当需要使用队列时也首选ArrayDeque，其次才是LinkedList

Queue继承自Collection接口

DeQue ：Double ended queue，双端队列，Deque继承自Queue接口，由于它是双端的，所以可以对队列的头和尾进行操作

双端队列，既可以当作栈使用，也可以当作队列使用

当Deque当作FIFO的队列时，元素从尾部添加，从头部删除

ArrayDeque与LinkedList是Deque的两个通用实现，官方推荐使用ArrayDeque



#### ArrayDeque

底层通过数组实现，为了满足可以同时在数组两端插入或删除元素的需求，该数组所以设计为循环的，即：循环数组；也就是说循环数组的任何一点都可能被看作起点或者终点

ArrayDeque是非线程安全的，其次，该容器不允许放null元素



数组也会进行扩容，扩容为原理的两倍

---



#### PriorityQueue

优先队列，作用：能保证每次取出的元素都是队列中权值最小的

元素大小的判定可以通过元素本身的自然顺序，也可以通过构造时传入比较器来做判定

PriorityQueue实现了Queue，不允许放入null元素

~~~java
	/**默认容量为11*/
	private static final int DEFAULT_INITIAL_CAPACITY = 11;

	/**队列容器*/
    transient Object[] queue;

	/**队列包含的元素长度（个数）*/
    private int size = 0;

	/**比较器,为null使用自然排序*/
    private final Comparator<? super E> comparator;

.......
~~~



##### 实现原理

通过堆实现，具体来说就是：通过二叉树实现的小顶堆，通过数组来作为PriorityQueue的底层实现

小顶堆：任意一个非叶子节点的权值，都不大于其左右子节点的权值



小顶堆、大顶堆应用场景：求10亿个数的前1000大、小  （TopN问题）



二叉堆可以用数组表示：父子节点下表关系为

leftNode=parentNo*2+1

rightNode=parentNo*2+2

parentNode=(nodeNo-1)/2



新加入的元素可能会破坏小顶堆的性质，因此需要做必要的调整

调整过程：从k指定的位置开始，将x逐层与当前节点的paren进行比较并交换，直到满足x>=queue[parent]为止，这里都比较可以是自然顺序，也可以是依靠比较器的顺序



删除

删除队首元素，会改变队列的结构，要去维护小顶堆的性质去做必要的调整

删除指定元素，也会去维护



---

### Set

#### HashSet

HashSet是对HashMap的简单包装，即：HashSet底层是HashMap实现

HashSet只用map的键存储数据，map的所有值为Object PRESENT=new Object();

可以存放null，但是只能有一个

HashSet查找效率为O(1)：根据hash值直接定位



#### LinkedHashSet

具有HashSet的查找效率，其内部使用双向链表来维护元素的插入顺序

对LinkedHashMap包装了一层而已



#### TreeSet

基于红黑树实现，支持有序性操作，查找效率为O(logn)

同理：TreeSet是对TreeMap做的一层封装

---

### Map

#### HashMap

https://www.developers.pub/wiki/1002310/582

1. HashMap允许k-v都为null，但是只能有一个元素为null
2. 容器不保证元素的顺序

根据对冲突的处理方式不同，哈希表有两种实现方式

1. 开放地址法
2. 链地址法（Java采用此种方式）：数组加链表的结合，在每一个数组元素上都链上一个链表结构，当元素hash后，得到数组下标，然后把元素放在对应的链表后边

hashcode()：方法决定对象被放到数组的哪个位置(bucket)

equals()：当多个对象的哈希值冲突时，equals决定了这些对象是否是同一个对象

所以，若要将自定义的对象放入到HashMap或HashSet中，需要重写这两个方法



##### HashMap结构

Java8：数组+链表+红黑树

内部有一个非常重要的属性Node，是HashMap的一个内部类，其实现了Map.Entry接口

其中Node中包含一个Node next，从而形成链表结构

##### HashMap容量

~~~java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
~~~

默认容量16=》数组大小为10

为什么初始值为16：为了降低hash碰撞的几率



##### HashMap负载因子

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



##### HashMap的hash()算法

~~~java
static final int hash(Object key) {
     int h;
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
~~~

逻辑：先获得key的hashCode值h，再让h和h右移16位做异或操作

实际上就是把一个数的低16位 与 高16位做异或运算。目的是减少hash冲突

##### Hash算法

hash其实就是一个函数，该函数实现的就是一种算法，通过一系列的算法来得到一个hash值，通过hash算法得到的hash值存放与hash表中

##### HashCode

1. hashCode就是通过hash函数得到的，hashcode就是在hash表中有对应的位置
2. 每个对象都有hashcode，对象的hashcode是通过对象的内部地址(也就是物理地址)转换成一个整数，然后该整数通过hash函数的算法就得到了hashcode(不同jvm的实现不同）。
3. 比如：hash表中有hashcode为1、hashcode为2、3、4、5、6、7、8等这样八个位置，有一个对象A，假如A的物理地址转换为一个整数17，就通过hash算法(假如直接取余算法)，17%8=1，那么A的hashcode就为1，则A就在hash表中1的位置

为什么使用hashcode

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

##### 解决hash冲突的方法

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



##### HashMap的数组+链表/红黑树问题

根据数组元素中，第一个节点数据类型是Node还是TreeNode来判断该位置下是链表还是红黑树

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



##### 数组的长度为什么是2的n次幂

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







##### HashMap扩容原理

resize方法，数组是无法扩容的，所有采用一种新的数组代替已有的小容量数组

当map中的Entry的数量大于等于threshold=loadFactor*capacity的时候，且新建的Entry刚好落在一个非空的桶上(数组[i])上，此时触发扩容机制，将其容量扩大为原来的两倍

##### put过程

1. 对key的hashCode()做hash运算，计算出index
2. 如果没有hash碰撞，则调用newNode()来创建Node放入tab中
3. 碰撞了，则以链表的形式存在tab中。当碰撞导致链表长度过长就会将链表转为红黑树
4. 如果某节点已经存在，则新的值替换为旧的值(保证key的唯一性)
5. 如果tab快满了(阈值=loadFacotr*currentCapacity)，则会进行扩容(resize)

##### 为什么转为红黑树



##### HashMap线程不安全问题

产生的原因：

1. Jdk7
   - 扩容造成死循环
   - 扩容造成数据丢失
2. Jdk8
   - 数据覆盖问题
   - 线程A会把线程B插入的数据给**覆盖**，发生线程不安全
   - 在put时，如果没有hash碰撞时，插入元素时候，多个线程操作，可能会造成覆盖

如何解决

1. Collections.synchronizedMap(map)，包装成同步Map，原理：在HashMap的所以方法上synchronized
2. ConcurrentHashMap







#### LinkedHashMap

使用双向链表来维护元素顺序，顺序为插入顺序或最近最少使用顺序

在HashMap的基础上，采用双向链表的形式，将所有entry连接起来，这样就保证了元素的迭代顺序与插入顺序是相同的

LinkedHashMap经典用法：

实现采用FIFO替换策略的缓存



#### TreeMap

基于红黑树实现

红黑树是一种近似于平衡的二叉树，它能够保证任何一个节点左右子树的高度差不会超过二者中较低那个的一倍

红黑树是满足以下条件的二叉查找树

1. 每个节点要么是红色，要么是黑色。
2. 根节点必须是黑色
3. 红色节点不能连续(也即是，红色节点的孩子和父亲都不能是红色)。
4. 对于每个节点，从该点至`null`(树尾端)的任何路径，都含有相同个数的黑色节点



当树的结构发生改变时，需要对其进行调整，以满足红黑树的约束条件，红黑树调整分为两类：颜色调整，结构调整：结构调整包含两个基本操作左旋和右旋

左旋：将x的右子树绕x逆时针旋转，使得右子树成为x的父亲，同时修改相关节点的引用。旋转之后，二叉查找树的属性仍然满足



右旋：将x的左子树围绕x顺时针旋转，使得x的左子树成为x的父亲，同时修改相关节点的引用



#### WeakHashMap

没有WeakHashSet，不过可以通过包装WeakHashMap来包装一个自己的WeakHashSet

特殊的Map，weakHashMap里面的entry可能会被GC自动删除，即使没有调用remove或clear方法

使用场景：需要缓存的场景

弱引用



---



## 反射

https://cn-sec.com/archives/880047.html

JAVA反射机制是：在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制

Java通过反射机制可以操作字节码文件

### 获取Class的方式

1. Class.forName("完整类名")
2. 对象.getClass()
3. 类型.class
4. 类加载器 xxxClassLoader.loadClass()传入路径获取
   - ClassLoader.getSystemClassLoader().loadClass("cn.qianchao.MyObject")
   - 通过类加载器获取Class对象不会进行初始化，意味着不进行包括初始化等一系列步骤，静态代码块和静态对象也不会得到执行

### 通过反射实例化对象

对象.newInstance()

newInstance()方法内部实际上是调用了无参构造方法，必须保证无参构造函数存在时才可以调用，否则会抛出异常：java.lang.InstantiationException

### Constructor

反映的是Class对象所表示的类的构造方法

### Field

Field提供有关类或接口的单个字段的信息、以及对这个字段的动态访问权限

反射的字段可能是一个类(静态)字段或实例字段

### Method

Method类提供关于类或接口上某个方法、以及如何访问该方法的信息，所反映的方法可能是类方法或实例方法（包含抽象方法）



### 反射机制执行流程

底层稍微知道就行了，不用太关注，我达不到那个层次。。。

#### 反射获取类实例

并不是Java实现的，而是交给JVM来搞的

主要是通过ClassLoader，然后调用native原生方法，获取信息

#### 反射获取方法



### 反射工具包

reflectutils 



Spring Ioc ->反射

Jdbc -> 反射动态加载数据库驱动程序

Aop基于->动态代理->反射实现

Java注解对象的获取



































## 泛型

JDK1.5引入了泛型，泛型提供了编译时类型安全检查机制，该机制允许程序员在编译时检测到非法的类型

泛型的本质是：参数化类型，即给类型指定一个参数，在使用时再指定此参数具体的值，这个类型就可以在使用时决定了

参数化类型：就是**将原来的具体的类型参数化**，然后在使用/调用时传入具体的类型(类型实参)



### 为什么使用泛型

1. 保证了类型的安全性

   - 没有泛型之前，从集合中读取到的每一个对象都需要进行类型转换，若不小心插入了错误的类型对象，在运行时的转换处理就会出错

   - ~~~java
     public static void noGeneric() {
         //没有泛型
     	ArrayList names = new ArrayList();
     	names.add("搜索时");
     	names.add(123); //编译正常
         for(int i = 0; i< arrayList.size();i++){
             String item = (String)arrayList.get(i);
             Log.d("泛型测试","item = " + item);
     	}
         //上述会抛出异常
         //java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
         
         
         //为了解决上述问题(在编译阶段就可以解决),泛型应运而生
         
         
         //有泛型之后，编译器会在编译阶段帮我们发现类似的问题
         ArrayList<String> names = new ArrayList<>();
     	names.add("搜索时");
     	names.add(123); //编译不通过
     }
     ~~~

2. 消除强制转换

   - ~~~java
     List list = new ArrayList();
     list.add("hello");
     //需要强制转换
     String s = (String) list.get(0);
     
     
     List<String> list2 = new ArrayList<String>();
     list2.add("hello");
     String s = list.get(0); //不用需要强制转换
     
     ~~~

     

3. 避免了不必要的装箱、拆箱操作，提高程序的性能

4. 提高了代码的重用性



### 特性

泛型只在编译阶段有效，编译过程中，正确检验泛型结果后，会将泛型的相关信息擦除，并且在对象进入和离开方法的边界处添加类型检测和类型转换的方法

即：泛型信息不会进入到运行阶段，泛型类型在逻辑上可以看成是多个不同的类型，实际上都是相同的基本类型



### 泛型的使用：类型、接口、方法

#### 泛型类

~~~java
public class GenericClass<a,b,c>{}

public class GenericClass<T>{
    
}
~~~



参数类型使用规范

T：任意类型 type
E：集合中元素的类型 element
K：key-value形式 key
V： key-value形式 value

#### 泛型接口

~~~java
public interface GenericInterface<T>{}


//实现泛型接口，未传入泛型参数时
//需要将泛型的声明也一起加到类中，否则编译器会报：UnKnow class
public FruitGenerator<T> implements GenericInterface<T>{}

//实现泛型接口，传入泛型实参时
public class FruitGenerator implements GenericInterface<String> {}
~~~



#### 泛型方法

在调用方法的时候指明泛型的具体类型

~~~java
public class GenericTest<T>{
    private T key;
    
    //泛型方法   public 和    返回值   直接的<T> 声明为泛型方法
    public <T> T genericMethod(Class<T> tClass){
            T instance = tClass.newInstance();
            return instance;
    }
    
    //泛型方法
    public <T> void test(String name){}
    //泛型方法
    public <T> void test(T t){}
    
    //泛型方法中的可变参数
    public <T> void argsMethod(A ...args){}
    

    //非泛型方法,只不过返回值是在声明泛型类已经声明过的泛型
    //所以这个方法中才可以继续使用T这个泛型
    public T getKey(){
        return key;
    }
    
}
~~~



### 泛型数组

Java的泛型数组，在初始化时，数组类型不能是具体的泛型类型，只能是通配符形式

因为：具体类型会导致可存入任意类型对象，在取出时会发生类型转换异常，会与泛型的设计思想冲突，而通配符本来就需要自己强转，符合预期

~~~java
List<?> [] list=new ArrayList<?>[10];

List<String> [] ls =new ArrayList[10];  //这样也行，不过会报警告
~~~





### 泛型通配符

\<?> ：无边界通配符，类型参数可以是任意类型

\<? extends  E>  ：固定上边界的通配符，E就是该泛型的上边界，?表示继承自E或实现了E接口的类型

\<? super E> ：固定下边的界通配符，E就是该泛型的下边界，？表示E的父类类型

多个限制：&     例如：<T extends Student & Man>



### 类型擦除

JVM泛型的擦除机制





~~~java
// 通过反射添加其它类型的元素
public class Test {

    public static void main(String[] args)   {
        ArrayList<Integer> list = new ArrayList<Integer>();
         //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer
        list.add(1); 
        
        //反射插入String类型的数据
        list.getClass().getMethod("add", Object.class).invoke(list, "asd");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
~~~

1. 无限制类型擦除
   - ![image-20221026223244650](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javase_img/202210262233351.png)
   - 类定义在类型参数没有任何限制时，类型擦除中直接被替换为Object
   - 即：\<T> \<?>的类型参数被擦除为Object
2. 有限制类型擦除
   - ![image-20221026223459104](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javase_img/202210262234296.png)
   - 当类定义中的类型参数存在限制（上下界）时，在类型擦除中替换为类型参数的上界或者下界，比如形如`<T extends Number>`和`<? extends Number>`的类型参数被替换为`Number`，`<? super Number>`被替换为Object
3. 擦除方法定义中的类型参数
   - ![image-20221026223651412](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javase_img/202210262236521.png)



### 如何理解编译期间检查

Java编译器会**编译之前**先检查代码中泛型的类型，然后进行类型擦除，再进行编译

**类型检查是针对引用**的，谁是一个引用，用这个引用调用泛型方法，就会对这个引用调用的方法进行类型检查，而无关它真正引用的对象

~~~java
//因为类型检查就是编译时完成的，new ArrayList()只是在内存中开辟了一个存储空间，可以存储任何类型对象，而真正涉及类型检查的是它的引用，因为我们是使用它引用list1来调用它的方法，比如说调用add方法，所以list1引用能完成泛型类型的检查。而引用list2没有使用泛型，所以不行
public class Test {  

    public static void main(String[] args) {  

        ArrayList<String> list1 = new ArrayList<>();  
        list1.add("1"); //编译通过  
        list1.add(1); //编译错误  
        String str1 = list1.get(0); //返回类型就是String  

        ArrayList list2 = new ArrayList<String>();  
        
        list2.add("1"); //编译通过  
        list2.add(1); //编译通过  
        Object object = list2.get(0); //返回类型就是Object  

        new ArrayList<String>().add("11"); //编译通过  
        new ArrayList<String>().add(22); //编译错误  

        //返回类型就是String  
        String str2 = new ArrayList<String>().get(0); 
    }  
} 
~~~





### 为什么不能将基本类型作为泛型类型

例如：有 ArrayList\<Integer>   而没有 ArrayList\<int>

因为类型擦除后，ArrayList的原始类型变为Object，但是Object不能存int，所有只能引用Integer的值

注意ArrayList\<Object>  list 中： list.add(123)      这个 123是经过自动装箱为Integer的



### 如何理解泛型类型不能实例化

~~~java
T t=new T();  //编译不过
~~~

不能实例化泛型类型的本质：还是类型擦除

Java编译期间没法确定泛型参数化类型，也就找不到对应的字节码文件，自然不能进行实例化。

此外，由于T被擦除为Object，new T()则变成了new Object()，失去了本意

如果确实需要实例化一个泛型应该通过反射实现

~~~java
static <T> T newTClass(Class <T> clazz){
    T obj =clazz.newInstance();
	return obj;
}
~~~



### 理解泛型类中的静态方法和静态变量

泛型类中的静态方法和静态变量，不可用使用泛型类所声明的泛型类型参数

原因：泛型类中，泛型参数的实例化是在定义对象的时候指定的，而静态变量和静态方法不需要使用对象来调用，此时对象都没有创建，不能确定这个泛型参数是何种类型，所以是错误的

~~~java
public class Test2<T> {    
    //编译错误    
    public static T one;   
    
    //编译错误  
    public static  T show(T one){   
        return null;    
    }    
}
~~~



但是，静态的泛型方法却是可以的

因为：泛型方法中所以的`<T>`**是自己在方法中定义的T，而不是泛型类的T**

~~~java
public class Test2<T> {    

    public static <T >T show(T one){ //这是正确的    
        return null;    
    }    
}
~~~



### 如何获取泛型参数

既然类型都被擦除了，那么应该如何获取泛型参数类型？ 可以通过反射获取泛型

java.lang.reflect.Type是Java种所以类型的公共高级接口，代表了Java中的所有类型

Type体系中类型包括：

1. 数组类型：GenericArrayType
2. 参数化类型：ParameterType
3. 类型变量：TypeVariable
4. 通配符类型：WildcardType
5. 原始类型：Class
6. 基本类型：Class

这些类型都实现了Type接口



### 泛型开发中的应用

项目中

自己实现的一个抽象service

abstract class AbstractService\<T> implements BaseService<T>

BaseService\<T> extends PrincialService\<T> ，CriteriaService\<T>，CommonCriteriaService\<T>



主要思想：顶层Service接口定义相关增删改查和一些额外的方法，AbstractService结合抽象BaseMapper\<T>实现数据库操作，具体Service覆写具体的Mapper实现自己的数据库的一系列操作

---

## 注解

注解是附加在代码中的一些元信息，**用于一些工具在编译、运行时进行解析和使用**

起到说明、配置的功能。注解不会也不能影响代码的实际逻辑，仅仅起到辅助性的作用



### 元注解

用于定义注解的注解

@Retention：标注注解被保留使用的阶段

- Source：源文件保留
- Class：编译期保留，默认值
- Runtime：运行期保留，可通过反射去获取注解信息

@Target：标明注解的使用范围，字段上？方法上？类上？包上？

@Inherited：标明注解是否可继承

@Documented：标明是否生成Javadoc文档







### Java8提供的新注解

重复注解：@Repeatable

什么是重复注解：运行在同一声明类型多次使用同一个注解

Java8之前：由另一个注解来存储重复注解，在使用时候，用存储注解来扩展重复注解

~~~java
public @interface Authority {
     String role();
}

public @interface Authorities {
    Authority[] value();
}

public class RepeatAnnotationUseOldVersion {

    @Authorities({@Authority(role="Admin"),@Authority(role="Manager")})
    public void doSomeThing(){
    }
}

~~~





Java8里面：不同的地方是，创建重复注解Authority时，加上@Repeatable,指向存储注解Authorities，在使用时候，直接可以重复使用Authority注解。从上面例子看出，java 8里面做法更适合常规的思维，可读性强一点

~~~java
@Repeatable(Authorities.class)
public @interface Authority {
     String role();
}

public @interface Authorities {
    Authority[] value();
}

public class RepeatAnnotationUseNewVersion {
    @Authority(role="Admin")
    @Authority(role="Manager")
    public void doSomeThing(){ }
}

~~~



@Native

使用@Native注解修饰成员变量，表示该变量可用被本地代码引用，常常被代码生成工具使用

了解即可



### 注解支持继承？

不能使用extends来继承莫格@Interface，但是注解在编译后，编译器会自动继承自Annotation接口



区别于注解的继承

被注解的子类 继承父类注解可以使用@Inherited：如果某个类使用了被@Inherited修饰的Annotation，则其子类将自动具有该注解





### 注解与反射接口

定义注解后，如何获取注解中的内容呢？

反射包java.lang.reflect下的AnnotatedElement接口提供这些方法，不过：只有注解被定义为RUNTIME后，该注解才能是运行时可见，当class文件被装载时被保存在class文件中的Annotation才会被虚拟机读取

**自定义注解后，处理注解的时候，就需要用到这个注解反射接口来操作**



### 自定义注解

根据自己的需求定义注解，并可用元注解对自定义注解进行注解，并且可用处理这个自定义注解



自定义注解重要，重要的是注解处理器，如果编写注解处理器！

自定义注解使用场景，项目中用过吗？

一般自定义注解，结合Aop使用：原因：通过注解+Aop最终目的是实现模块的解耦



自己写过（见过的哈哈哈）吗？：面试要讲，而且还有讲清除

1. 注解+Aop实现Log的添加
2. 注解+Aop+redis 实现：幂等性接口（防止重复提交）
3. 测试架构中：注解(指定excel位置)，解析将excel中的数据，注入到容器里面的数据库中
4. 等等等等







### 注解实现原理



https://blog.csdn.net/qq_20009015/article/details/106038023

https://www.race604.com/annotation-processing/

了解即可





### 注解到底是个什么东西：类、接口、抽象类

~~~java
public @interface MyTest{}
~~~



反编译查看字节码得到：

Java编译器认为**注解是一个接口**，一个继承自Annotation的接口，里面的每一个属性，其实就是接口的一个抽象方法



注解是接口，那么何时实例化、怎么实例化？

调用getDeclaredAnnotations()时候返回一个代理$Proxy对象，这个是使用JDK动态代理创建的，使用Proxy的newProxyInstance()，传入接口和InvocationHandler的一个实例(也就是AnnotationInvocationHandler)，最后返回一个代理实例



最终总结：
注解@interface 是一个实现了Annotation接口的 接口， 然后在调用getDeclaredAnnotations()方法的时候，返回一个代理$Proxy对象，这个是使用jdk动态代理创建，使用Proxy的newProxyInstance方法时候，传入接口 和InvocationHandler的一个实例(也就是 AnotationInvocationHandler ) ，最后返回一个代理实例

期间，在创建代理对象之前，解析注解时候 从该注解类的常量池中取出注解的信息，包括之前写到注解中的参数，然后将这些信息在创建 AnnotationInvocationHandler时候 ，传入进去 作为构造函数的参数

当调用该代理实例的获取值的方法时，就会调用执行AnotationInvocationHandler里面的逻辑，将之前存入的注解信息 取出来



---

## SPI机制

Service Provider Interface



是JDK内置的一种**服务发现机制**，这个东西一般而言针对厂商或插件，可以用来启用框架扩展和替换组件，主要被框架的开发人员使用

比如：java.sql.Driver接口，不同的厂商可以针对同一接口做出不同的实现，例如MySQL、Oracle、Postgresql等等都有自己的实现，而**Java SPI机制可以为某个接口寻找不同的服务实现**

Java SPI机制主要思想：是将装配的控制权转移到程序之外，核心思想就是**解耦**

![image-20221028164004656](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/javase_img/202210281641562.png)

**当服务的提供者(实现者)提供了一种接口的实现之后，需要在classpath下的META-INF/services目录里创建一个以服务接口命名的文件，这个文件里面的内容就是这个接口的具体的实现类**

当其他程序需要这个服务的时候，就可以通过查找这个jar包的META-INF/services中的配置文件，配置文件中有接口的具体实现类名，可以根据这个类名进行加载实例化，就可以使用改服务了

JDK中查找服务的实现的工具类为：**java.util.ServiceLoader**





### SPI在SpringBoot等框架中的应用

Jdbc：

1. 硬编码方式：Class.forName()加载驱动，通过Meta-INF/services/java.sql.Driver文件来指定实现类的方式来暴露驱动提供者
2. DriverManager封装
   - DriverManager是Java实现的，用来获取数据库连接
   - 底层封装了Class.forName()

Common-Loggin：

是常用的日志库门面

使用SPI服务发现机制，发现LogFactory的具体实现



SpringBoot：自动装配机制

springBoot的自动装配过程中，最终会加载META-INF/spring.factories文件，而加载过程是由SpringFactoriesLoader加载的

从Classpath下的每个jar包搜索META-INF/spring.factories配置文件，然后将会解析Properties文件，找到指定名称的配置后返回



### SPI机制-插件

最具SPI机制的应该属于插件开发

例如：Eclipse的插件思想



### SPI机制的缺陷

1. 不能按需加载，每次都是去遍历所有的实现
2. 获取某个类的实现类的方式不够灵活，只能通过Iterator形式获取，不能根据某个参数来获取对应的实现类
3. 多线程并发使用ServiceLoader是不安全的





## Java8

Java8的好处，核心：把函数式编程中一些最好的想法融入到了Java语法中

主要涉及到的新特性：

1. 函数式编程：lambda
2. Stream流
3. Optional
4. 默认方法
5. LocalDate



### 函数式编程

> 面向对象编程是对数据进行抽象，函数式编程是对行为进行抽象

核心思想：使用不可变值和函数，函数对一个值进行处理，映射成另一个值



#### Lambda

lambda和方法引用，简明理解：**将代码或方法作为参数传递**

lambda表达式可以被赋值给一个变量，或传递给一个接受函数式接口作为参数的方法  ！！！！！

~~~java
1: Runnble r =()->sout("hello world");

2: process(r);
2: process(()->sout("hello world"))

public void process(Runnable r){
    r.run();
}
~~~



1. **lambda在Java中是一个匿名函数**，理解成是一段**可以传递的代码**（将函数像数据一样传递）
2. 更紧凑的代码风格，提高阅读性



##### 语法

1. 无参，无返回值
   - （）->sysout("hello lambda");
2. 一个参数，无返回值
   - （x)->sout("x");
3. 多个参数，有返回值
   - (x,y)->{return x+y}
4. 多个参数，有返回值，但是只有一行语句   大括号和return都可以不用写
   - (x,y)-> x+y;
5. 参数列表数据类型可以不用写，JVM编译器能通过上下文推断出



##### 在哪里使用Lambda表达式

1. 函数式接口
2. 函数描述符(lambda表达式的签名)

例如:

| 函数式接口           | 函数描述符  | 原始类型特化                     |
| -------------------- | ----------- | -------------------------------- |
| Predicate\<T>        | T->boolean  | IntPredicate、LongPredicate..... |
| Consumer\<T>         | T->void     | IntConsumer....                  |
| BiConsumer<T,U>      | (T,U)->void |                                  |
| Function\<T，R>      | T->R        |                                  |
| BiFunction\<T，U，R> | (T，U)->R   |                                  |



##### 四大核心函数式接口

函数式接口：**只定义一个抽象方法的接口**，即使接口中含有默认方法，它仍然是一个函数式接口

1. 消费型接口：有参数，无返回值的抽象方法

   - ~~~java
     Consumer<T> 
     	void accept(T t);
     ~~~

2. 供给型接口：无参数，有返回值

   - ~~~java
     Supplier<T>
         T get();
     ~~~

3. 函数接口：有参数，有返回值

   - ~~~java
     Function<T,R>
     	R apply(T t);
     ~~~

4. 判断型接口/断言型：有参数，但是返回值是boolean类型

   - ~~~java
     Predicate<T>
     	boolean test(T t);
     ~~~



其他常见函数式接口

Runnable、Callable、Comparator、EventListener等等



##### 函数式接口能做什么

Lambda表达式允许直接**以内联的形式为函数式接口的抽象方法提供实现**，并把整个表达式作为函数式接口的实例



##### 编译器对Lambda的检查

lambda可以为函数式接口生成一个实例。然而，Lambda表达式本身并不包含它在实现哪个函数式接口的信息。为了全面了解Lambda表达式，你应该知道Lambda的实际类型是什么

###### Lambda实际类型

Lambda的类型是**从使用Lambda的上下文推断出来的**，上下文中Lambda表达式需要的类型称为目标类型



###### 类型检查过程：

~~~java
//调用
List<Apple> filterWeightList=filter(appleList,(Apple a)->a.getWeight>100);

//定义
filter(List<Apple> appleList,Predicate<Apple> p){}

~~~

1. Lambda上下文是什么？
   - 看filter方法
   - 上下文：变量赋值上下文、方法调用上下文(参数和返回值)、类型转换上下文
   - 可以从上面的上下文中获得响应的目标类型
2. filter(List\<Apple> appleList，Predicate\<Apple> p)
   - 得出目标类型：Predicate\<Apple>
3. Predicate\<Apple>的抽象方法是什么？
   - boolean test(Apple apple);
   - 接受一个Apple返回boolean
   - 函数描述符 即：Apple->boolean
4. filter的任何实际参数都必须匹配这个函数描述符要求



###### 类型推断

~~~java
//参数a 没有显示的类型， Java编译器会通过类型推断出它是Apple类型
List<Apple> filterList =
    filter(appleList,a->"green".equals(a.getColor())); 

~~~



##### Lambda实质

Lambda本质是对象引用，事实上Java8的Lambda表达式最初的实现原型是内部类，但是JVM从长期来看，希望能够支持更复杂的lambda表达式，所以采用Java8以及之后采用了一种更复杂的技术，而不是硬编码为内部类



JVM玩的儿的还是字节码

invokenamic

---

#### 方法引用



##### 作用

可以**重复使用现有的方法定义**，并像Lambda表达式一样传递它们

可以被看作是仅仅调用特定方法的Lambda的一种快捷写法



1. 若Lambda体中的内容已经有方法实现了，那么可以使用方法引用
2. 可以理解为：Lambda表达式的另外一种表现形式

##### 三种语法格式

1. 对象：：实例方法名
   - (arg)->expr.instanceMethod(arg)    => expr::instanceMethod
2. 类：：静态方法名
   - 注意事项
   - 调用类的静态方法
   - Lambda体中，调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数参数列表和返回值类型保持一致
3. 类：：实例方法名
   - 使用规则
   - 第一个参数是实例方法的调用者，第二个参数是实例方法的参数时 例如：String::equals
   - (arg0，a)->arg0.instanceMethod(a)    arg0是ClassName类型的    则=>ClassName::instanceMethod



例如：

~~~java
Apple::getWeight   没有括号，因为实际上并没有调用这个方法
  
(Apple a)->a.getWeight()的快捷写法
~~~





---

#### 构造器引用

对于一个现有的构造函数，可以利用它的名称和关键字new来创建它的一个方法引用

1. ClassName::new 
2. 功能：创建对象的意思

~~~java
//不带参数的构造函数
Supplier<Apple> c1= Apple::new;
Apple a1= c1.get();

//等价于
Supplier<Apple> c1 =()->new Apple();    //构建Lambda表达式
Apple a1=c1.get();					  //调用get方法会产生一个Apple实例


//带参数的构造函数
Function<Integer,Apple> c2= Apple::new;
Apple a2=c2.apply(100);

Function<Integer,Apple> c2=(Integer i)->new Apple(i);
Apple a2=c2.apply(100);
~~~



如果需要一个具有多个参数的构造函数怎么实现？

~~~java
//因为没有提供这样的函数，所以需要自定义一个 多参数的函数式接口
public interface TriFunction<T,U,V,R>{
    R apply(T t,U u,R r);
}

TriFunction<Integer,Integer,Integer,Apple> appleFactory=Apple::new;
~~~



---

#### 数组引用

Type::new

---



#### 函数式编程范式基石

1. 没有共享的可变数据
2. 将方法或函数(即:代码)传递给其他方法的能力



---



### Stream

流：**从支持数据处理操作的源 生成的元素序列** 。程序可以从输入流中一个一个读取数据项，然后以同样的方式将数据项写入输出流。一个程序的输出流可能是另一个程序的输入流

注意：从有序集合生成流时，会保留原有的顺序

可以看作：比较**花哨的遍历数据集的高级迭代器**



##### 流操作两个重要特点

1. 流水线：很多流操作本身会返回一个流，这样多个操作就可以链接起来，形成一个大的流水线
   - 流水线的操作可以看作是对数据源进行数据库式查询
2. 内部迭代



##### 行为参数化

用行为参数化把代码传递给方法

Stream API就是构建在通过**传递代码**使操作行为实现参数化的思想上





1. 对集合、数组等数据进行操作
2. 一系列流水线式的中间操作、数据源并不会改变，会产生一个新的流
3. 集合讲的是数据，流讲的是计算操作
4. Stream不会存储元素、不会改变源对象
5. 操作是延迟执行的
   - 触发终止操作后才会去执行一系列中间操作



##### 并行流

并行流是把一个内容分成多个数据块，并用不同的线程分别处理每个数据块的流



并行流变为顺序流：

~~~java
stream.parallel()
    .filter(...)
    .sequential()
    .xxx
~~~



```java
	1. fork/join框架   java7
	2. parallel并行流   底层fork/join
```



问题：并行流使用的线程从哪里来的？有多少个线程？整个过程怎么定义的？

1. 并行流内部默认才有了Java7给出的方案：Fork/Join框架的ForkJoinPool
2. 默认的线程数量是：自己处理器的数量



并行化的代价：

1. 需要对流做递归划分，把每个子流的归纳操作分配到不同的线程，然后把这些操作合并为一个值。但是在多个内核之间移动数据的代价可能比并行执行工作的实际长，这种情况就不适合了
2. 因此：在使用并行流加速代码之前，必须确保使得的对，否则就会出现常见的陷阱



###### 正确使用并行流

错用并行流产生错误的首要原因：使用的算法改变了某些共享状态



###### 分支/合并框架

分支/合并框架的目的：以递归的方式将可以并行的任务拆分为更小的任务，然后将每个子任务的结果合并起来生成整体结果

它是ExecutorService接口的一个实现，它把子任务分配给线程池(ForkJoinPool)中的工作线程



##### Stream与Collection

Collection处理元素需要使用for-each这样的循环来迭代处理元素，这种方式称为**外部迭代**，外部迭代需要进行显示地取出每个项目再加以处理

- Collection是数据结构，主要是为了**存储和访问数据**

Stream的数据处理是在库内部进行的，这种思想称为**内部迭代**，内部迭代时，项目可以透明的处理或用更优化的顺序进行处理。这相对于外部迭代来讲这些优化是很困难的

- Stream主要目的是**对数据的计算**
- Stream利用内部迭代把迭代代替我们做了，但是，只有我们定义了能够隐藏迭代的操作列表，才会进行迭代。例如：filter、map操作



**Java8引入流的理由(似乎)：**Streams库的内部迭代可以自动选择一种合适你硬件的数据表示和并行实现。与此相反，一旦通过写for-each而选择了外部迭代，那你基本上就要自己管理所有的并行问题了（自己管理实际上意味着“某个良辰吉日我们会把它并行化”或“开始了关于任务和synchronized的漫长而艰苦的斗争”）。Java 8需要一个类似于Collection却没有迭代器的接口，于是就有了Stream！



##### 流操作

分为2大类

1. 中间操作：filter、map、limit可以连成一条流水线
2. 终端操作：collect触发流水线执行，并关闭它

可以连接起来的流操作称为中间操作，关闭流的操作称为终端操作



##### 使用流的好处

1. 代码以声明式方式编写：说明想要完成什么，而不是说明如何实现一个操作
2. 通过基础操作的链接完成复杂数据的处理
3. 可并行-性能更好



##### 创建流方式

1. 由值创建
2. 数组创建
3. 文件生成流
4. 函数生成流：创建无限流，一般会搭配limit使用来限制流，避免出现无穷个数
   - 用给定的函数，按需创建值
   - Stream.iterate
   - Stream.generate









### Optional

1. Optional\<T>是一个容器类，代表一个值存在或不存在
2. 可以避免空指针







### 接口中默认方法

参考文章：https://pdai.tech/md/java/java8/java8-default.html

用default修饰的方法，并且带有方法体；实现接口的类里面可以覆写/不覆写默认方法

1. default修饰
2. 可以实现默认方法
3. 默认方法可以有多个
4. 接口中可以包含默认方法，还可以有静态方法



为什么会有默认方法？

对于接口的修改，所有实现了该接口的类都要进行修改，所有搞了一个默认方法，来解决此类问题



接口的多重继承的冲突问题？

1. 声明在类里面的方法优先于任何默认方法
2. 否则，优先选区路径最短的



### 新时间API

LocalDate





