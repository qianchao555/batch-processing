### Java8

Java8的好处：把函数式编程中一些最好的想法融入到了Java语法中



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



1. lambda是一个匿名函数，理解成是一段**可以传递的代码**（将函数像数据一样传递）
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

1. 消费型接口

   - ~~~java
     Consumer<T> 
     	void accept(T t);
     ~~~

2. 供给型接口

   - ~~~java
     Supplier<T>
         T get();
     ~~~

3. 函数接口

   - ~~~java
     Function<T,R>
     	R apply(T t);
     ~~~

4. 判断型接口/断言型

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



#### Stream

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

		1. fork/join框架   java7
		2. parallel并行流   底层fork/join





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

---







#### Optional

1. Optional\<T>是一个容器类，代表一个值存在或不存在
3. 可以避免空指针

---

#### 接口中默认方法

用default修饰的方法，并且带有方法体；实现接口的类里面可以覆写/不覆写默认方法



1. default修饰
2. 可以实现默认方法
3. 默认方法可以有多个
4. 接口中可以包含默认方法，还可以有静态方法

类优先、接口冲突



作用：支持库设计师，让他们能够写出更容易改进的接口；而且，真正需要编写默认方法的程序员很少很少

#### 新时间API







