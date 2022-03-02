## MyBatis

### 什么是MyBatis

1. 是一个半ORM(对象关系映射)框架，它内部封装了JDBC，加载驱动、创建连接、创建statement等繁杂的过程，使得开发者开发时只需要关注如何编写sql语句，可以严格控制sql执行性能，灵活度高
2. MyBatis可以使用xml或者注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的jdbc代码和手动设置参数以及获取结果集

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

































