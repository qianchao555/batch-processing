1. JDK动态代理只能针对实现了接口的类生成代理
2. Cglib是针对类 实现代理，主要是对指定的类生成一个子类，覆盖其中的方法。因为是采用继承的方式，所以 该类或者方法不要声明为final,final可以阻止继承和多态
3. CGLib底层采用ASM字节码生成框架，使用字节码技术生成代理类，比使用Java反射效率要高



Spring Aop框架对Aop代理类处理原则：

~~~java
return (AopProxy)(!targetClass.isInterface() && !Proxy.isProxyClass(targetClass) ? new ObjenesisCglibAopProxy(config) : new JdkDynamicAopProxy(config))
~~~

- 如果目标对象的实现类实现了接口Spring AOP 将会采用 JDK 动态代理来生成 AOP 代理类；
- 如果目标对象的实现类没有实现接口，Spring AOP 将会采用 CGLIB 来生成 AOP 代理类
- 这个选择过程对开发者完全透明、开发者也无需关心

