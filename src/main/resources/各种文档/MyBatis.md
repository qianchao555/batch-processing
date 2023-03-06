## MyBatis

### 什么是MyBatis

1. 是一个半ORM(对象关系映射)框架，它内部封装了JDBC，加载驱动、创建连接、创建statement等繁杂的过程，使得开发者开发时只需要关注如何编写sql语句，可以严格控制sql执行性能，灵活度高
2. MyBatis可以使用xml或者注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的jdbc代码和手动设置参数以及获取结果集

---

### JDBC

Java DataBase Connectivity ：是Java和数据库之间的一个桥梁，是一个规范。它由一组Java语言编写的类和接口组成。各种不同类型的数据库都有相应的实现

![image-20220329213239713](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202203292133603.png)



#### JDBC编程步骤

1. 装载相应数据库的Jdbc驱动并进行初始化

   - 导包/或者依赖

   - 初始化驱动

     ~~~java
     try {
         Class.forName("com.mysql.jdbc.Driver");		
     } catch (ClassNotFoundException e) { 				
         e.printStackTrace();
     }
     Class.forName是把这个类加载到JVM中，加载的时候，就会执行其中的静态初始化块，完成驱动的初始化的相关工作
     ~~~

     

2. 建立JDBC和数据库之间的Connection连接

   - Connection是与特定数据库连接会话的接口

     ~~~java
             /**
     	 * 取得数据库的连接
     	 * @return 一个数据库的连接
     	 */
     public static Connection getConnection(){
     		Connection conn = null;
     		 try {
     			 	//初始化驱动类com.mysql.jdbc.Driver
     	            Class.forName("com.mysql.jdbc.Driver");
     	            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/exam?characterEncoding=UTF-8","root", "admin");
     	            //该类就在 mysql-connector-java-5.0.8-bin.jar中,如果忘记了第一个步骤的导包，就会抛出ClassNotFoundException
     	        } catch (ClassNotFoundException e) { 				
     	            e.printStackTrace();
     	        }catch (SQLException e) {							
     	            e.printStackTrace();
     	        }
     		 return conn;
     	}
     ~~~

     

3. 创建Statement或者PreparedStatement接口，执行SQL

   - Statement

     1. Statement接口创建之后，可以执行SQL语句，完成对数据库的增删改查。其中 ，增删改只需要改变SQL语句的内容就能完成，然而查询略显复杂。在Statement中使用字符串拼接的方式，该方式存在句法复杂，容易犯错等缺点

     2. 字符串拼接方式的SQL语句是非常繁琐的，中间有很多的单引号和双引号的混用，极易出错

     3. ~~~java
        Statement s = conn.createStatement();
        // 准备sql语句
        // 注意： 字符串要用单引号'
        String sql = "insert into t_courses values(null,"+"'数学')";
        //在statement中使用字符串拼接的方式，这种方式存在诸多问题
        s.execute(sql);
        System.out.println("执行插入语句成功");
        ~~~

   - PreparedStatement

     1. 与Statement不同的是，需要根据sql语句创建PreparedStatement。除此之外，还能通过设置参数，指定相应的值，而不是使用字符串拼接

     2. ~~~java
          public void addCourse(String courseName){
              //该语句为每个 IN 参数保留一个问号（“？”）作为占位符
           	 String sql = "insert into t_course(course_name) values(?)";  
              Connection conn = null;			
              PreparedStatement pstmt = null;		
              try{
                  conn = DbUtil.getConnection();
                  pstmt = (PreparedStatement) conn.prepareStatement(sql);
                  //给占位符赋值
                  pstmt.setString(1, courseName); 
                  //执行
                  pstmt.executeUpdate();		
              }catch(SQLException e){
                  e.printStackTrace();
              }
              finally{
                 //关闭资源
              }
           	}
     
        
     
        	public void delCourse(int courseId){
        		String sql = "delete from t_course where course_id = ?";
        		Connection conn = null;
        		PreparedStatement pstmt = null;
        		try {
        			conn = DbUtil.getConnection();
        			pstmt = (PreparedStatement) conn.prepareStatement(sql);
        			pstmt.setInt(1, courseId);
        			pstmt.executeUpdate();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}finally{
        			 //关闭资源
        		}
        	}
        	   
        	       /**
        	 * 修改课程
        	 * @param courseId
        	 * @param courseName
        	 */
        	public void modifyCourse(int courseId,String courseName){
        		String sql = "update t_course set course_name =? where course_id=?";
        		Connection conn = null;
        		PreparedStatement pstmt = null;
        		try {
        			conn = DbUtil.getConnection();
        			pstmt = (PreparedStatement) conn.prepareStatement(sql);
        			//利用Preparedstatement的set方法给占位符赋值，参数索引是从1开始的
        	           pstmt.setString(1, courseName);  
        			pstmt.setInt(2, courseId);
        			pstmt.executeUpdate();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}finally{
        		}
        	}
        ~~~
      
        

4. 处理和显示结果

   - ~~~java
        /**
           	 * 查询
           
           	 * @return
           	 */
           	public List<Course> findCourseList(){
           		String sql = "select * from t_course order by course_id";
           		Connection conn = null;
           		PreparedStatement pstmt = null;
           		ResultSet rs = null;
           		//创建一个集合对象用来存放查询到的数据
           		List<Course> courseList = new ArrayList<>();
           		try {
           			conn = DbUtil.getConnection();
           			pstmt = (PreparedStatement) conn.prepareStatement(sql);
                 
                 //结果集
           			rs = (ResultSet) pstmt.executeQuery();
           			while (rs.next()){
           				int courseId = rs.getInt("course_id");
           				String courseName = rs.getString("course_name");
           				//每个记录对应一个对象
           				Course course = new Course();
           				course.setCourseId(courseId);
           				course.setCourseName(courseName);
           				//将对象放到集合中
           				courseList.add(course);
           			}
           		} catch (SQLException e) {
           			e.printStackTrace();
           		}finally{
           			//释放资源
           		}
           		return courseList;
           	}
     ~~~
   
   - PreparedStatement和Statement比较
   
     1. 两者都是用来执行SQL的
     2. PreparedStatement需要根据SQl语句来创建，它通过设置参数、指定相应的值，不像Statement使用字符串拼接方式
     3. PreparedStatement使用参数设置，可读性好、不易出错。相反Statement采用字符串拼接，容易出错及可读性和维护性差
     4. PreparedStatement具有预编译机制，性能比后者好
     5. 前者能防止SQL注入攻击

5. 释放资源

   - 在JDBC编码的过程中我们创建了Connection、ResultSet等资源，这些资源在使用完毕之后是一定要进行关闭的。关闭的过程中遵循从里到外的原则

### 总体架构设计

![image-20230306170448304](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202303061704172.png)



#### 接口层

和数据库交互的方式，Mybatis和数据库交互有2种方式

1. 使用传统的Mybatis提供的API
2. **使用Mapper接口**

使用Mapper接口中：将<mapper>节点抽象为一个接口，而这个接口中声明的方法跟<mapper>节点的<select |update|delete|insert>节点项对应，即节点的id值为Mapper接口中方法的名称



#### 数据处理层

数据处理层可以说是Mybatis的核心，它主要完成2个功能

1. 通过传入参数构建动态SQL语句
   - Mybatis通过传入的参数值，使用Ognl来动态地构造SQL语句，使得Mybatis有很强的灵活性和扩展性
   - 参数映射：指的是对于Java数据类型和JDBC数据类型之间的转换。其中，查询阶段将Java类型的数据转换成JDBC类型的数据，通过preparedStatement.setXXX来设值。另外一个是对resultset查询结果集的JdbcType数据转换从Java数据类型
2. SQL语句的执行以及封装查询结果集成List<E>
   - 动态SQL语句生成后，Mybatis将执行sql语句，并将可能返回的结果集转换成List<E>。
   - Mybatis对结果集的处理中，支持结果集一对多、多对一的转换，并且有2种支持，一个是嵌套语句的查询、一个是嵌套结果集的查询



#### 框架支持层

1. 事物管理机制
2. 连接池管理机制
3. 缓存机制
4. SQL语句的配置方式



#### 引导层

是配置和启动Mybatis配置信息的方式，支持xml配置文件和Java API方式





---

### MyBatis核心对象

![image-20220329220927022](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202203292209900.png)

MyBatis有三个基本要素

核心接口和类、Mybatis核心配置文件mybatis-config.xml、SQL映射文件mapper.xml

#### 核心接口和类

每个MyBatis应用程序都以一个SqlSessionFactory对象的实例为核心

1. 首先获取SqlSessionFactoryBuilder对象，可以根据xml或类的实例构建该对象
2. 通过SqlSessionFactoryBuilder对象来获取SqlSessionFactory对象
3. 进而通过SqlSessionFactory获取SqlSession
4. SqlSession对象中完全包含了以数据库为背景的索引执行SQL操作的方法，用该实例可以直接执行已映射的SQL语句

##### SqlSessionFactory

1. 它是一个工厂接口，其任务是创建SqlSession

2. 有了它之后就可以通过SqlSession提供的openSession()来获取SqlSession实例

3. ~~~java
   public interface SqlSessionFactory {
       SqlSession openSession();
       SqlSession openSession(boolean autoCommit);
       SqlSession openSession(Connection connection);
       SqlSession openSession(TransactionIsolationLevel level);
       SqlSession openSession(ExecutorType execType);
       SqlSession openSession(ExecutorType execType, boolean autoCommit);
       SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);
       SqlSession openSession(ExecutorType execType, Connection connection);
       Configuration getConfiguration();
   }
   ~~~

SqlSessionFactory生命周期和作用域

1. 此对象一旦创建，就会在整个应用程序过程中存在。
2. 没有理由去销毁或再创建它，并且也不建议应用程序在运行中多次创建它
3. 因此，它的最佳作用域是Application，即随着应用程序的生命周期一直存在
4. 采用单例模式

##### SqlSession

SqlSession是用于执行持久化操作的对象，类似与JDBC中的Connection。它提供了面向数据库执行Sql命令所需的所有方法，可以通过SqlSession实例直接运行已映射的Sql语句

~~~java
void clearCache();
Configuration getConfiguration();
void rollback(boolean force);
void commit(boolean force);
int delete(String statement, Object parameter);
...
~~~

**主要用途**

1. 获取映射器：让映射器通过命名空间和方法名称找到对应的sql，并发给数据库，执行后返回结果
2. 直接通过命名空间+SQL id 的方式执行Sql，不需要获取映射器

**Sqlsession生命周期和作用域名**

Sqlsession对应一次数据库会话。由于数据库会话不是永久的，因此其生命周期也不是永久的，每次访问数据库时都需要创建Sqlsession对象

注意：每个线程都有自己的Sqlsession实例，Sqlsession实例不能共享，也不是线程安全的，因此其作用域范围是request作用域或方法体作用域内



### 映射器

http://c.biancheng.net/mybatis/mapper.html

映射器是Mybatis最重要的文件，文件中包含一组sql语句，这些语句称为映射语句或映射sql语句

映射器由Java接口和xml文件或注解共同组成，它的作用如下：

1. 定义参数类型
2. 配置缓存
3. 提供Sql语句和动态sql
4. 定义查询结果和POJO的映射关系

#### 映射器实现方式

1. 通过xml文件方式实现
   - 比如：在mybatis-config.xml文件中描述的xml文件，用来生成mapper
   - 接口(一般为dao层接口)+xml
2. 通过注解方式实现
   - 使用Configuration对象注册Mapper接口
   - 接口上使用注解，注入sql即可

#### MyBatis映射器主要元素

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







### Mybatis是否支持延迟加载?实现原理？

1. Mybatis仅仅支持association关联对象和collection关联集合对象的延迟加载，association指的是一对一，collection指的是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载：lazyLoadingEnabled=true / false
2. 延迟加载基本原理：使用Cglib创建目标对象的代理方法，点调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用



---

### Mybatis数据源DataSourec与连接池



#### DataSource分类

1. UnPooled：不使用连接池的数据源
2. Pooled：使用连接池的数据源
3. JNDI：使用JNDI实现的数据源



#### 数据源DataSource创建过程

Mybatis数据源DataSource对象的创建发生在Mybatis初始化过程中，Mybatis通过工厂模式来创建数据源的DataSource对象。抽象工厂DataSourceFactory

不同的类型具有不同的工厂

1. PooledDataSourceFacotry
2. Un
3. JndiDataSourceFactory

创建DataSource对象后，会将其放到Configuration对象内的Enviroment对象中，供以后使用



#### 什么时候创建Connection对象

当创建SqlSession对象去执行SQL语句时，这时候Mybatis才会去调用dataSource对象来创建java.sql.Connection对象，也就是说Connection对象的创建一直延迟到执行SQL语句的时候



#### 为什么使用连接池

创建一个java.sql.**Connection对象是一个耗时的过程**，可能创建一个Connection对象就需要200-300毫秒，这对于计算机来说是非常奢侈的了

若仅仅创建一个Connection连接就需要那么大代价，那么在web应用中，用户的每一个请求就操作一次数据库，那么当有10000人在线用户，并发操作的话，仅仅创建Connection对象就需要10000*250ms=2500s=41分钟！！！！

分析：创建一个Connection对象过程，在底层相当于和数据库建立的通信连接，在建立通信连接的过程消耗了这么多时间，往往是建立连接后，就执行一个简单的sql语句，然后就要抛弃掉，这是非常大的资源浪费

解决方案：

对于需要频繁地跟数据库交互的应用程序，可以在创建了Connection对象，并操作完数据库后，可以不释放掉资源，而是将它放到内存中，当下次需要操作数据库时，可以直接从内存中取出Connection对象，不需要再创建了，这样就极大地节省了创建Connection对象的资源消耗。

由于内存也是有限和宝贵的，这就要求我们对内存中的Connection对象怎么有效地维护提出了很高的要求。

我们将在内存中存放Connection对象的容器称之为连接池（Connection Pool）



#### PooledDataSource原理

Mybatis将连接池中的PooledConnection分为两种状态：空闲(idle)、活动(active)状态。这两种状态的PooledConnection对象分别存放到了两个List集合中

**idleConnections**:表示当前闲置的没有被使用的PooledConnection集合，调用PooledDataSource的getConnection()方法时，会优先从此集合中取PooledConnection对象。当用完后，在放回到此集合中

**activeConnections**:表示当前正在被使用的PooledConnection集合，调用PooledDataSource的getConnection()方法时，会优先从idleConnections集合中取PooledConnection对象,如果没有，则看此集合是否已满，如果未满，PooledDataSource会创建出一个PooledConnection，添加到此集合中，并返回



使用连接池，使用了Connection对象后，如何放回连接池？

代理模型解决：为真正的Connection对象创建一个代理对象，代理对象所有的方法都是调用真正的Connection对象的方法实现，当代理对象执行close()方法时，要特殊处理，不调用真正Connection对象的close()方法，而是将Connection对象添加到连接池中



MyBatis的PooledDataSource的PoolState内部维护的对象是PooledConnection类型的对象，而PooledConnection则是对真正的数据库连接java.sql.Connection实例对象的包裹器。

PooledConnection对象内持有一个真正的数据库连接java.sql.Connection实例对象和一个java.sql.Connection的代理



---

### Mybatis的一级二级缓存



#### 一级缓存

对于SqlSession级别的缓存称为一级缓存

![image-20221020162658425](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210201627715.png)

1. SqlSession对象只是一个门面，真正干活的是Executor执行器，Mybatis将缓存和对缓存相关的操作封装在Cache接口中，SqlSession、Executor、Cache之间的关系图为：

![image-20221020162958732](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210201630530.png)





![image-20221020163339872](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210201633898.png)



SqlSession级别的一级缓存实际上就是使用PerpetualCache维护的，底层就是一个普通的HashMap来存放的。

Cache最核心的实现其实就是一个Map，将本次查询使用的特征值作为key，将查询结果作为value存储到Map中。现在最核心的问题出现了：怎样来确定一次查询的特征值？换句话说就是：怎样判断某两次查询是完全相同的查询？也可以这样说：如何确定Cache中的key值？

MyBatis认为，对于两次查询，如果以下条件都完全一样，那么就认为它们是完全相同的两次查询：

- 传入的 statementId
- 查询时要求的结果集中的结果范围 （结果的范围通过rowBounds.offset和rowBounds.limit表示）
- 这次查询所产生的最终要传递给JDBC java.sql.Preparedstatement的Sql语句字符串（boundSql.getSql() ）
- 传递给java.sql.Statement要设置的参数值

**现在分别解释上述四个条件**：

- 传入的statementId，对于MyBatis而言，你要使用它，必须需要一个statementId，它代表着你将执行什么样的Sql；
- MyBatis自身提供的分页功能是通过RowBounds来实现的，它通过rowBounds.offset和rowBounds.limit来过滤查询出来的结果集，这种分页功能是基于查询结果的再过滤，而不是进行数据库的物理分页；
- 由于MyBatis底层还是依赖于JDBC实现的，那么，对于两次完全一模一样的查询，MyBatis要保证对于底层JDBC而言，也是完全一致的查询才行。而对于JDBC而言，两次查询，只要传入给JDBC的SQL语句完全一致，传入的参数也完全一致，就认为是两次查询是完全一致的。
- 上述的第3个条件正是要求保证传递给JDBC的SQL语句完全一致；第4条则是保证传递给JDBC的参数也完全一致；即3、4两条MyBatis最本质的要求就是：调用JDBC的时候，传入的SQL语句要完全相同，传递给JDBC的参数值也要完全相同。

综上所述,CacheKey由以下条件决定：**statementId + rowBounds + 传递给JDBC的SQL + 传递给JDBC的参数值**；

##### CacheKey对象创建

对于每次的查询请求，Executor都会根据传递的参数信息以及动态生成的SQL语句，将上面的条件根据一定的计算规则，创建一个对应的CacheKey对象。

我们知道创建CacheKey的目的，就两个：

- 根据CacheKey作为key,去Cache缓存中查找缓存结果；
- 如果查找缓存命中失败，则通过此CacheKey作为key，将从数据库查询到的结果作为value，组成key,value对存储到Cache缓存中；



##### 一级缓存的生命周期

MyBatis在开启一个数据库会话时，会创建一个新的SqlSession对象，SqlSession对象中会有一个新的Executor对象，Executor对象中持有一个新的PerpetualCache对象；当会话结束时，SqlSession对象及其内部的Executor对象还有PerpetualCache对象也一并释放掉

**一级缓存是一个粗粒度的缓存，没有更新缓存和缓存过期的概念**

1. 默认情况下，mybatis只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。这就是Mybatis的一级缓存，其作用域是SqlSession

2. 一级缓存运行过程：MyBatis一次会话: 一个SqlSession对象中创建一个本地缓存(local cache)，对于每一次查询，都会尝试根据查询的条件去本地缓存中查找是否在缓存中，如果在缓存中，就直接从缓存中取出，然后返回给用户；否则，从数据库读取数据，将查询结果存入缓存并返回给用户

2. 但是一旦执行新增或更新或删除操作，缓存就会被清除

3. ~~~xml
   <!--
   flushCache默认为false,表示不会去清空本地缓存和二级缓存
   -->
   <select id="selectAll"   flushCache="true" >
   	select * from stu
   </select>
   ~~~

4. 

6. 一级缓存没有过期时间，只有生命周期，mybatis在开启一个数据库会话时，会创建一个新的SqlSession对象，SqlSession对象中会有一个Executor对象，Executor对象中持有一个PerpetualCache对象（HashMap本地缓存），如果SqlSession调用了close()则会释放掉一级缓存PerpetualCache对象，一级缓存将不可用。在这个SqlSession中执行了update、delete、insert操作，都会清空PerpetualCache对象里面的数据，但是该对象可以继续使用



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



Mybatis缓存机制整体设计以及二级缓存的工作模式：

![image-20221019225030299](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210192250591.png)



##### Mybaits缓存机制

当开启一个会话时，一个SqlSession对象会使用一个Excutor对象来完成会话操作，Mybatis的二级缓存就是在这个Excutor对象上做文章

如果开启了二级缓存，那么MyBatis在为SqlSession创建Excutor对象的时候，会对Excutor加一个装饰者：CacheExcutor，这时SqlSession使用Excutor对象来完成操作

CachingExecutor对于查询请求，会判断该查询请求在Application级别的二级缓存中是否有缓存结果，若有则直接返回缓存结果，没有则会交给真正的Executor对象来完成查询操作，之后CachingExecutor会将真正Executor返回的查询结果放置到缓存中，然后再返回给用户

![image-20221019225140940](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210192251045.png)

CachingExecutor是Executor的装饰者，以增强Executor的功能，使其具有缓存查询的功能。（采用Mybatis插件功能对Executor进行增强）

##### 二级缓存的划分

Mybatis并不是简单的对整个Application就有一个Cache缓存对象，它将缓存划分的更细，即：Mapper级别 。即：每一个Mapper都可以拥有一个Cache对象，也可以多个Mapper共用一个Cache缓存对象

1. 为每一个Mapper分配一个Cache缓存对象（使用\<cache>节点配置）
   - Mybatis将Application级别的二级缓存细分到Mapper级别，即：对于每一个Mapper.xml，如果在其中使用了\<cache>节点，则会为这个Mapper创建一个Cache缓存对象
   - ![image-20221019231215583](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210192312703.png)
2. 多个Mapper共用一个Cache缓存对象（使用\<cache-ref>节点配置）
   - 想让多个Mapper公用一个Cache的话，可以使用\<cache-ref namespace="">节点，来指定这个Mapper使用那一个Mapper的缓存
   - ![image-20221019231237319](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210192312414.png)





##### 使用二级缓存必须具备的条件

**Mybatis对于二级缓存的支持粒度很细，它会指定某一条查询语句是否使用二级缓存**

虽然在Mapper.xml中配置了\<cache>节点，并且也为此Mapper分配了Cache对象，但是：这并不代表我们使用Mapper中定义的查询语句查到的结果都会放置到Cache对象中，我们**必须指定Mapper中的某条查询语句是否支持查询**。例如：需要在\<select>标签节点中配置useCache="true"，Mapper才会对此查询语句支持缓存特性，否则，对此select查询不会经过Cache缓存

~~~xml
<select id="selectById" resulteType="int" useCache="true">
    select  * from A 
</select>
~~~

总之，想要某条select查询语句支持二级缓存，需要保证

1. 开启Mybatis二级缓存总开关：cacheEnabled=true
2. 开启Mapper分开关：在Mapper中配置\<cache> 或\<cache-ref>节点，并且有效
3. \<select>节点配置参数：useCache=true

##### mybatis整合SpringBoot

1. mybatis整合Springboot后，直接在Mapper.xml中添加 \<cache> .... \</cache> 表示开启二级缓存

2. Mapper.xml里面 的select查询都具有缓存功能

3. 可以针对单个\<select id="xxx"   useCache="false">，取消单个查询的二级缓存

   



##### 一、二级缓存的使用顺序

如果你的MyBatis使用了二级缓存，并且你的Mapper和select语句也配置使用了二级缓存，那么在执行select查询的时候，MyBatis会先从二级缓存中取输入，其次才是一级缓存，即MyBatis查询数据的顺序是：**二级缓存 —> 一级缓存 —> 数据库**





##### 二级缓存实现的选择

MyBatis对二级缓存的设计非常灵活，它自己内部实现了一系列的Cache缓存实现类，并提供了各种缓存刷新策略如LRU，FIFO等等；

另外，MyBatis还允许用户自定义Cache接口实现，用户是需要实现Cache接口，然后将Cache实现类配置在`<cache type="">`节点的type属性上即可；

除此之外，MyBatis还支持跟第三方内存缓存库如Memecached的集成

总之有三种选择：

1. Mybatis自生提供的二级缓存机制实现
2. 用户自定义Cache接口实现
3. 整个第三方内存缓存库集成



##### 自身提供的二级缓存的实现

自身提供了丰富的二级缓存实现，它拥有一系列的Cache接口装饰者，可以满足各种对缓存操作和更新的策略

![image-20221019233302708](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210192333825.png)

对每一个Cache而言，都有一个容量限制，Mybatis提供了多种策略来对Cache缓存的容量进行控制，以及对Cache中的数据进行刷新和置换

刷新和置换策略：

1. LRU：最近最少未使用算法，即如果缓存中容量满了，会将最近最少使用的缓存记录删除，然后添加新的记录
2. FIFO：先进先出，若缓存中容量满了，马么将最先进入缓存中的数据清除
3. Scheduled：时间间隔清空算法，会以指定的某一个时间间隔清空缓存中的数据



##### 二级缓存中的问题



现有AMapper.xml中定义了对数据库表 ATable 的CRUD操作，BMapper定义了对数据库表BTable的CRUD操作；

假设 MyBatis 的二级缓存开启，并且 AMapper 中使用了二级缓存，AMapper对应的二级缓存为ACache；

除此之外，AMapper 中还定义了一个跟BTable有关的查询语句，类似如下所述：

```xml
<select id="selectATableWithJoin" resultMap="BaseResultMap" useCache="true">  
      select * from ATable left join BTable on ....  
</select>
```



执行以下操作：

- 执行AMapper中的"selectATableWithJoin" 操作，此时会将查询到的结果放置到AMapper对应的二级缓存ACache中；
- 执行BMapper中对BTable的更新操作(update、delete、insert)后，BTable的数据更新；
- 再执行1完全相同的查询，这时候会直接从AMapper二级缓存ACache中取值，将ACache中的值直接返回；

好，**问题就出现在第3步**上：

由于AMapper的“selectATableWithJoin” 对应的SQL语句需要和BTable进行join查找，而在第 2 步BTable的数据已经更新了，但是第 3 步查询的值是第 1 步的缓存值，已经极有可能跟真实数据库结果不一样，即ACache中缓存数据过期了！

总结来看，就是：

对于某些使用了 join连接的查询，如果其关联的表数据发生了更新，join连接的查询由于先前缓存的原因，导致查询结果和真实数据不同步；

从MyBatis的角度来看，这个问题可以这样表述：

**对于某些表执行了更新(update、delete、insert)操作后，如何去清空跟这些表有关联的查询语句所造成的缓存**



MyBatis二级缓存的一个重要特点：松散的Cache缓存管理和维护

![image-20221020161902039](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210201620163.png)



解决上述问题方案：**对于某些表执行了更新(update、delete、insert)操作后，去清空跟这些指定的表有关联的查询语句所造成的缓存**; 这样，就是以很细的粒度管理MyBatis内部的缓存，使得缓存的使用率和准确率都能大大地提升



---

### MyBatis如何将sql执行结果封装为目标对象并返回的



#### 使用标签

使用标签逐一定义数据库列名和对象属性名之间的映射关系

#### 使用sql列的别名功能

1. 使用sql列的别名功能，将列的别名书写为对象属性名
2. 有了列名与属性名的映射关系后，Mybatis通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的

---



### MyBatis动态sql

Mybatis动态sql可以在Xml映射文件内以标签的形式编写动态sql，**执行原理是根据表达式的值完成逻辑判断，并且动态拼接sql功能**

sql标签有：trim、where、set、foreach、if、choose、when、otherwise、bind



#### 动态sql原理

关于动态SQL的接口和类：

SqlNode接口：简单理解就是xml中的每一个标签，例如sql中的update、trim、if等等

SqlSouce：Sql源接口，代表从xml文件或注解映射的sql内容。主要用于构建BoundSql

BoundSql类：封装Mybatis最终产生的sql类，包括sql语句、参数、参数源数据等等参数

XNode：一个Dom API中的Node接口的扩展类

BaseBuilder接口以及实现类：这些Builder用于构造sql







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



### Mybatis插件机制

mybatis提供了一个插件的功能，**虽然叫插件，但是它的实质是拦截器的功能**

Mybatis插件存在的目的：相当于JavaWeb中的拦截器，可拦截要操作的四大对象，包装对象额外添加内容，使得Mybatis的灵活性更强

Mybatis支持用插件对四大核心对象进行拦截，对mybatis来说插件就是拦截器，用来增强核心对象的功能，增强功能本质上是借助第层的动态代理实现的，即：Mybatis的四大对象都是代理对象

#### 四大对象

Executor(update,query,flushStatements,commit,rollback,close,isClosed,getTransaction)：执行增删改查操作，**拦截执行器的方法**

ParameterHandler(getParameterObject,setParameters)：处理SQL的参数对象，**拦截参数的处理**

ResultSetHandler(handleResultSets,handleOutputParameters)：处理SQL的返回结果集，**拦截结果集的处理**

StatementHandler(prepare,parameterize,batch,update,query)：处理sql语句预编译，用于执行sql语句。**拦截sql语法构建的处理**





#### 插件原理

Mybatis采用责任链模式，通过动态代理组织多个拦截器(插件)，通过这些拦截器可以改变mybatis的默认行为(例如sql重写之类的)

1. Mybatis的插件借助**责任链模式**进行拦截的处理
2. 使用动态代理对目标对象进行包装，达到拦截的目的
3. 作用于Mybatis的作用域对象之上



#### 代理链的生成

Mybatis支持对Executor、StatementHandler、ParameterHandler和ResultSetHandler进行拦截，也就是说会对这4种对象进行代理。通过查看Configuration类的源代码我们可以看到，每次都对目标对象进行代理链的生成

创建动态代理的时候，是按照插件配置顺序创建层层代理对象，执行目标方法是按照逆向顺序执行





#### MappedStatement

MappedStatement维护了一条<select | update | deltete | insert> 节点的疯子

例如：转换为Java类就是一个MappedStatement

~~~xml
<select id="selectDataNum" resulteType="int">
    select * from user where id=1
</select>
~~~



##### SqlSource

负责根据用户传递的parameterObject，动态生成sql语句，将信息封装到BoundSql对象中

##### BoundSql

表示动态生成的SQL语句、以及相应的参数信息

当调用SqlSource的getBoundSql方法，传入的就是parameterMappings相对应的参数,最终生成BoundSql对象,有了BoundSql就可以执行sql语句了





Mybatis封装的JDBC

SqlSession ->MapperProxy(被代理对象的方法的访问都会落实到代理者的invoke方法)-> MapperMethod   (此时mapper对象与sqlsession就真正的关联起来了)->Executor->StatementHandler->prepareStatement（ps.execute()去执行）



---



### Mybatis分页-插件分页

Mybatis的分页功能很弱，是基于内存的分页，即先查询出所有记录，再按偏移量和limit取出结果，在大数据量的情况下这样的分页是没有用的

这里指的是JVM内存



#### Mybatis如何分页

mybatis使用RowBounds对象进行分页，它是针对ResultSet结果集执行的内存分页，而非物理分页。可以在sql内直接书写带有物理分页参数了完成分页功能，也可以使用分页插件来完成物理分页

1. RowBounds：内存分页，不太实用
2. PageHelper分页插件，自定义分页插件，进行物理分页
3. Mybatis-plus分页组件：
4. Spring-data分页：

还可以自定义Page，组合Mybatis-plus的分页信息、还可以将Mybatis-plus的分页信息 封装后 转换为spring-data的分页



#### Mybatis分页插件原理

基本原理是：使用mybatis提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的sql，然后重写sql，根据dialect方言，添加对应的物理分页语句和物理分页参数



-------------------------



## Mybatis-Plus

### 特性

1. 无侵入：只做增强不做改变
2. 强大的curd：内置通用Mapper、通用Service
   - 可以不使用Mybatis-plus提供的 IService
   - 可以自己定义顶层Service，结合mapper，完成Service的框架封装
3. 支持Lambda形式调用：通过lambda表达式，方便编写各类查询条件，无需担心字段写错
4. 支持主键自动生成
5. 分页插件支持多种数据库：MySQL、Oracle、Postgresql、HSQL、MariaDB等等
6. 内置性能分析插件：可输出sql语句以及其执行时间，建议在开发、测试时启用该功能



### 核心功能

#### 代码生成器

#### CRUD接口

#### 条件构造器

QueryWrapper

LambdaQueryWrapper

#### 主键策略

#### 自定义ID生成器





### Mybatis-plus自定义DefaultSqlInjector，扩展BaseMapper

目的：

MyBats-Plus在一开始就给大家提供了很多通用的方法，在DefaultSqlInjector这个类中，在MethodList这个集合当中包含的都是通用方法类，如果想要使用自定义通用方法，也需要添加到这个集合当中



自定义自己的通用方法可以实现接口 ISqlInjector 也可以继承抽象类 AbstractSqlInjector 注入通用方法 SQL 语句 然后继承 BaseMapper 添加自定义方法，全局配置 sqlInjector 注入 MP 会自动将类所有方法注入到 mybatis 容器中



-----

## SpringData

Spring Data是Spring的一个子项目，用于简化数据库访问，支持NoSQL和关系型数据库。主要目标是：使数据库的访问变得方便快捷

支持NoSQL：

1. MongoDB，文档数据库
2. Redis，k-v数据库
3. Hbase，列值数据库
4. Neo4j，图像数据库

支持关系型数据库

1. JDBC
2. JPA



### JDBC与JPA

区别：

1. 不同标准：jdbc是数据库统一接口标准，jpa是orm框架统一接口标准
2. 用法：jdbc更注重数据库，orm更注重java代码，但是实际上jpa实现的框架底层还是用jdbc去和数据库打交道

### ORM框架

Mybatis：半orm框架，不是依照JPA规范，它需要写一些sql

Hibernate：是JPA的一种实现

Spring data JPA：对JPA规范的再次抽象，底层使用的是Hibernate

Spring data JDBC：jdbcTemplate模板数据库简化对数据库的操作，相比传统JDBC而言省去了，数据库驱动，连接等无关配置，只需要写sql，设置参数



![image-20221021142127464](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/mybatis_img/202210211421235.png)



### JPA

Java Persistence API：Java对象持久化的API

JPA本质上是一种ORM规范，不是ORM框架，因为JPA未提供ORM实现，它只是制定了一些规范，提供了一些API接口，但具体实现则由服务厂商来提供实现。JPA是为了让面向对象设置的，为了不写sql而设置的



