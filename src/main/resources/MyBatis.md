## MyBatis

### 什么是MyBatis

1. 是一个半ORM(对象关系映射)框架，它内部封装了JDBC，加载驱动、创建连接、创建statement等繁杂的过程，使得开发者开发时只需要关注如何编写sql语句，可以严格控制sql执行性能，灵活度高
2. MyBatis可以使用xml或者注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的jdbc代码和手动设置参数以及获取结果集

---



### # {} 和 ${}的区别

${}：是字符串替换，#{}：是预处理

1. Mybatis在处理${}时，会把它直接替换成变量的值

2. 处理#{}时，会对sql语句进行预处理，将sql中的#{}替换成 ？ 号，调用PreparedStatement的set方法来赋值，所有使用#{}可以有效的防止SQL注入，提高系统的安全性

3. ~~~mysql
   select * from stu where id=#{id}
   
   #近似的jdbc代码
   String selectStu="select * from stu where id=?"
   PreparedStatement ps=conn.prepareStatement(selectSut);
   ps.setInt(1,id);
   ~~~

---



### Dao接口里的方法参数不同时，方法能重载吗

1. Mapper接口(Dao接口)里的方法是不能重载的，因为使用的是权限定名+方法名来进行寻找策略的

2. 接口的全限定名，就是映射文件(xxx.xml)中namespace的值

3. 接口的方法名，就是映射文件中Mapper的Statement的id值

4. 接口方法的参数，就是传递给sql的参数

5. 当调用接口方法时，接口权限定名+方法名 拼接作为key，可以唯一定位一个MapperStatement。在Mybatis中，每一个Sql标签都会被解析为一个MapperStatement对象。例如：

6. ~~~mysql
   <select id="selectLike">
   	select * from stu where name='zhangsan'
   </select>
   ~~~

---



### Mybatis分页

#### Mybatis如何分页

mybatis使用RowBounds对象进行分页，它是针对ResultSet结果集执行的内存分页，而非物理分页。可以在sql内直接书写带有物理分页参数了完成分页功能，也可以使用分页插件来完成物理分页

#### Mybatis分页插件原理

基本原理是：使用mybatis提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的sql，然后重写sql，根据dialect方言，添加对应的物理分页语句和物理分页参数

---



### Mybatis是否支持延迟加载?实现原理？

1. Mybatis仅仅支持association关联对象和collection关联集合对象的延迟加载，association指的是一对一，collection指的是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载：lazyLoadingEnabled=true / false
2. 延迟加载基本原理：使用Cglib创建目标对象的代理方法，点调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用

---



### Mybatis的一级二级缓存

#### 一级缓存

1. 默认情况下，mybatis只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。这就是Mybatis的一级缓存，其作用域是SqlSession

2. 一级缓存运行过程：执行sql语句时，首次从数据库取的数据被存储在一段高速缓存中，今后执行这条语句的时候就会从高速缓存中读取结果。但是一旦执行新增或更新或删除操作，缓存就会被清除

3. ~~~xml
   <!--
   flushCache默认为false,表示不会去清空本地缓存和二级缓存
   -->
   <select id="selectAll"   flushCache="true" >
   	select * from stu
   </select>
   ~~~

4. 

5. 一级缓存没有过期时间，只有生命周期，mybatis在开启一个数据库会话时，会创建一个新的SqlSession对象，SqlSession对象中会有一个Executor对象，Executor对象中持有一个PerpetualCache对象（HashMap本地缓存），如果SqlSession调用了close()则会释放掉一级缓存PerpetualCache对象，一级缓存将不可用。在这个SqlSession中执行了update、delete、insert操作，都会清空PerpetualCache对象里面的数据，但是该对象可以继续使用

#### 二级缓存

##### mybatis二级缓存出现的原因

1. 一级缓存中不同的SqlSession进行相同SQL查询的时候，是查询两次数据库，显然是一种浪费，既然sql相同那就没有必要再次查库了，直接利用缓存数据即可，这种思想就是Mybatis二级缓存的初衷

2. Spring整合Mybatis后，每次查询后都要进行关闭SqlSession（同一个线程未开启事务的情况下每次查询会关闭旧的sqlSession而创建新的SqlSession。开启事务情况下，spring使用ThreadLocal获取当前资源绑定同一个SqlSession，此时一级缓存是有效的），关闭之后，数据被清空。所有Mybatis和Spring整合之后，一级缓存就没有意义了。如果开启二级缓存，关闭SqlSession后，会把该SqlSession一级缓存中的数据添加到mapper namespace的二级缓存中。这样，缓存在SqlSession关闭之后依然存在

3. mybatis默认只启动了本地的会话缓存，即：一级缓存。要启用全局的二级缓存，需要在xml文件中 添加一行：<cache/>  或者全局缓存配置

   ~~~xml
   <!--全局配置方式  mybatis-config.xml 文件中配置-->
   <settings>
   	<setting name="cacheEnabled" value="true"/>
   </settings>
   
   
   <!--单个namespance里面配置  
   useCache是否开启二级缓存
   flushCache二级缓存是否清除
   -->
   <cache/>
   <select id="selectAll" useCache="true">
   	select * from stu 
   </select>
   ~~~

   

二级缓存是Mapper级别的缓存，多个SqlSession去操作同一个Mapper的sql语句，多个SqlSession可以公用二级缓存，二级缓存是跨SqlSession的

二级缓存与一级缓存其机制相同，默认也是采用 PerpetualCache，HashMap 存储，不同在于其存储作用域为Mapper(namespace)，即：二级缓存的作用域针对namespace，在单个namespace配置二级缓存就只针对当前这个映射文件才配置二级缓存

对于缓存数据更新机制，当某一个作用域的进行了delete、update、insert后，默认都会导致本地缓存和二级缓存被清空

二级缓存有过期时间，并不是k-v的过期时间，而是这个cache的过期时间，即flushInterval，意味着清空整个缓存，每次存取数据的时候，都会检测cache时间，默认一个小时，超过这个时间就整个缓存清空一下

---



### MyBatis如何将sql执行结果封装为目标对象并返回的

#### 使用标签

使用标签逐一定义数据库列名和对象属性名之间的映射关系

#### 使用sql列的别名功能

1. 使用sql列的别名功能，将列的别名书写为对象属性名
2. 有了列名与属性名的映射关系后，Mybatis通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的

---



### MyBatis动态sql

Mybatis动态sql可以在Xml映射文件内以标签的形式编写动态sql，执行原理是根据表达式的值完成逻辑判断，并且动态拼接sql功能

sql标签有：trim、where、set、foreach、if、choose、when、otherwise、bind

---



### Mybatis映射文件Xml标签中，有哪些常用标签

1. select、insert、update、delete
2. resultMap、parameterMap
3. sql、include
4. <sql>为一个sql片段，通过include标签来进行引入



### 模糊查询如何写？

~~~java
String val="qc%";
list<Stu> stuList=mapper.selectLike(val);

//xml文件中编写
<select id="selectLike">
    select * from stu where name like #{val}
<select>
    
    
2、sql里面拼接
    name like '%' || #{var} || '%'
~~~



































