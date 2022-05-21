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



### Mybatis分页

Mybatis分页是基于内存的分页，即先查询出所有记录，再按起始位置和页面容量取出结果

这里指的是JVM内存

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



### Mybatis如何扩展一个mybatis插件

































