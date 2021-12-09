### Java8

#### Lambda

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

##### 四大核心函数式接口

1. 消费型接口

   - ~~~java
     Consumer<T> 
     	void accept(T t);
     ~~~

2. 供给型接口

   - ~~~java
     Supplier(T) 
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

---

#### 方法引用



---



#### Stream

#### Optional









