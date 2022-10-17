## Spring Security

权限管理框架，可以做用户验证、权限管理等等

有了SpringBoot、Cloud后，Security比Shiro更具优势，也使用的更多

它提供了一组可以在Spring应用上下文中配置的Bean，充分利用了Spring IoC，DI（控制反转Inversion of Control ,DI:Dependency Injection 依赖注入）和AOP（面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作



### 1、Spring Security架构

---



### 2、核心功能

认证和授权，Spring Security中，认证(Authentication)和授权(Authorization)是分开的

1. 认证：通俗说就是 需要登录 才能访问某个url资源

#### 认证

通俗来说就是我们常说的登录通过@EnableWebSecurity开启

SpringSecurity支持的认证方式：

- HTTP BASIC authentication headers：基于IETF RFC 标准。
- HTTP Digest authentication headers：基于IETF RFC 标准。
- HTTP X.509 client certificate exchange：基于IETF RFC 标准。
- LDAP：跨平台身份验证。
- Form-based authentication：基于表单的身份验证。
- Run-as authentication：用户用户临时以某一个身份登录。
- OpenID authentication：去中心化认证

除了这些常见的认证方式之外，一些比较冷门的认证方式，Spring Security 也提供了支持。

- Jasig Central Authentication Service：单点登录。
- Automatic "remember-me" authentication：记住我登录（允许一些非敏感操作）。
- Anonymous authentication：匿名登录。



认证可以单独使用，即不划分资源的级别，所有人只要登录都可以查看



用户的认证信息主要由Authentication的实现类来保存，不同的认证方式，都会对应一个不同的Authentication实例，Authentication实现类比较多：常用的有AbstractAuthenticationToken、RememberMeAuthenticationToken、UsernamePasswordAuthenticationToken、PreAuthenticatedAuthenticationToken等等



认证工作主要由：AuthenticationManager接口来负责，接口里面的authenticate()用来做认证，返回Authentication表示认证成功

AuthenticationManager最主要的实现类：ProviderManager

ProviderManager：管理重多的AuthenticationProvider实例

AuthenticationProvider：authencate()用于认证、supports()用于判断是否支持给的的Authentication类型

一次完整的认证流程中，可能同时存在多个AuthenticationProvider(例如：同时支持表单、短信登录)

ProviderManager具有一个可选的parent，若所有的AuthenticationProvider都认证失败，那么会调用parent进行认证，由他来收拾残局



#### 授权

无论采用上面哪一种认证方式，都不影响授权功能，SpringSecurity支持基于UPL的请求授权、支持方法访问授权、SpEL访问控制、域对象安全(ACL)、动态权限配置、支持RBAC权限模型等

授权的目的是可以把资源进行划分，例如公司有不同的资料，有普通级别和机密级别，只有公司高层才能看到机密级别的子类，而普通级别的资料大家都可以看到！ 那么授权就是允许你查看某个资源，当然，如果你没有权限，就拒绝你查看

分为：功能权限(只能用哪些功能)、数据权限(只能看到哪些数据)



安全这一块从来都有说不完的话题，一个简单的注册登录很好做，但是你要是考虑到各种各样的攻击，XSS、CSRF 等等，一个简单的注册登录也能做的很复杂。只要你用了 Spring Security，就能自动避免掉很多攻击了，因为 Spring Security 已经自动帮我们完成很多防护了。



认证完成后，就是授权了，Spring Security授权体系中，两个关键接口，他们都有众多的实现类

1. AccessDecisionManager
   - 是一个决策器，决定此次访问是否被允许访问。在AccessDecisionManager中会遍历AccessDecisionVoter，进而决定是否允许用户访问。
   - 二者关系类似于ProviderManager和AuthenticationProvider
2. AccessDecisionVoter
   - 是一个投票器，会检查用户是否具备应有的角色，进而投出赞成、反对、弃权票

Spring Security中，用户请求一个资源（通常是一个网络接口或一个Java方法），所需要的角色会被封装成ConfigAttribute对象，ConfigAttribute中有一个getAttribute()方法，返回String，就是角色的名称。一般来说角色名称都带有ROLE_ 前缀，投票器AccessDecisionVoter所做的就是比较用户所具备的角色和请求某个资源所需的ConfigAttribute之间的关系



#### 登录数据保存

1. SecurityContextHolder
2. 当前请求对象中获取

不使用Security这样的安全管理框架，大部分开发者会将登录用户数据保存在Session中，当然SpringSecurity也是这样做的，不过为了使用方便，Spring Security在此基础上进行了改进，其中最主要的就是**线程绑定ThreadLocal**

用户登录成功后，Security会将用户信息保存到SecurityContextHolder中，SecurityContextHolder中的数据保存默认是通过ThreadLocal来实现的，使用ThreadLocal创建的变量只能被当前线程访问，不能被其他线程访问和修改，也就是**用户数据和请求线程绑定在一起**

登录请求处理完成后，SpringSecurity会将SecurityContextHolder中的数据取出来，保存到Session中，同时将SecurityContextHolder清空



##### SecurityContextHolder

![image-20221011214124380](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/image-20221011214124380.png)



SecurityContextHolder中定义了三种不同的数据存储策略，实际上这是一种典型的策略模式：

1. MODE_threadlocal存放策略：这种模式是将SecurityCOntext存放在ThreadLocal中，也是默认的存储策略，但是若开启了子线程，在子线程中不能获取到用户数据，需要单独的进行处理
2. Mode_InheritableThreadLocal：这种存储模式适用于多线程环境，若希望在子线程中也能够获取到登录信息，那么可以使用这种存储模式。实现方式为：子线程创建的时候，会将父线程的数据复制到子线程中
3. Mode_Global：这种模式实际上是将数据保存在一个静态变量中，JavaWeb开发中，这种模式很少使用



对应上方的第二种方式：从请求头中获取Authentication

SpringMvc中，Controller的请求参数都是HttpServletRequest带来的

整合jwt后，可以直接写一个工具类：验证请求头中的token，解析得到用户信息

---

#### 用户定义

SpringSecurity支持多种用户定义方式，自定义用户其实就是使用UserDetailsService的不同实现类来提供用户数据，开发中一般都是自定义UserDetails不使用它提供的实现，同时将配置好的UserDetailsService配置给AuthenticationManagerBuilder，系统再将UserDetailsService提供给AuthenticationProvider使用

1. 基于内存InMemoryUserDetailsManager
2. 基于JdbcUserDetailsManager
3. Mybatis：实际开发中采用此种方式
4. 基于Spring Data JPA：类似于Mybatis

这四种方式都异曲同工，只是数据的存储方式不同而已，其他的执行流程都是一样的



---

### 3、Spring Security认证流程分析

#### 登录流程分析

三个基本组件AuthenticationManager、ProviderManager、AuthenticationProvider

接入认证功能的过滤器：AbstractAuthenticationProcessingFilter

#### AuthenticationManager接口

是一个认证管理器，定义了SpringSecurity过滤器要如何执行认证操作，认证成功后返回一个Authentication对象，该对象会被设置到SecurityContextHolder中。实际应用中使用最多的是ProviderManager实现类，Spring Security默认也是使用ProviderManager





#### AuthehticationProvider

SpringSecurity支持不同的认证方式，不同的认证方式对应不同的身份类型，AuthehticationProvider就是针对不同的身份类型执行具体的身份认证，身份认证就是在其authenticate(Authentication authentication)中完成的

例如：RememberMeAuthenticationProvider用来支持“记住我”类型的认证、UsernamePasswordAuthentication支持用户名/密码类型的登录认证

实际开发中，若采用jwt则需要自己实现AuthehticationProvider，实现核心的authenticate()、和supports()方法，来自定义认证逻辑



#### ProviderManager

ProviderManager管理各个AuthenticationProvider，不同的认证方式对应了不同的AuthenticationProvider，例如：用户名/密码认证、手机号码动态认证、RememberMe认证等等，所以一个完整的认证流程可能由多个AuthenticationProvider来提供，多个AuthenticationProvider将组成一个列表，这个列表由ProviderManager代理，在ProviderManager中遍历列表中的每一个AuthenticationProvider去执行身份认证，最终得到认证结果

![image-20220928204400095](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209282044337.png)





这些组件如何与登录关联？

##### AbstractAuthenticationProcessingFilter

SpringSecurity过滤器链中的一环，它可以用来处理任何提交给它的身份认证，如图：流程是一个通用的架构



![image-20220928205118309](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209282051415.png)



作为一个抽象类，如果使用了用户名/密码登录，那么它对应的实现类则实现类是UsernamePasswordAuthenticationFilter，构建出来的Authentication对象则是UsernamePasswordAuthenticationToken



下图为使用用户名/密码进行身份认证流程图：

![image-20221011225050664](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/image-20221011225050664.png)

认证流程：

1. 用户提交登录请求时，UsernamePasswordAuthenticationFilter会从当前请求HttpServletRequest中提取出用户名和密码，然后创建一个UsernamePasswordAuthenticationToken对象

2. 将该对象传入ProviderManager中进行具体的认证操作

3. 认证失败则SecurityContextHolder中相关信息将被清除，登录失败回调也会被调用

4. 认证成功则：进行登录信息的存储、Session并发处理、登录成功事件发布、登录成功方法回调等等

   

实际开发中：若采用jwt-token方式，则需要实现自己的Authentication对象，例如：JwtToken extends AbstractAuthenticationToken，并将自己的jwt过滤器放置在UsernamePasswordAuthenticationFilter之前





#### 认证配置多数据源

多个数据源：指的是同一个系统中，用户数据来自不同的表，认证时，若第一张表没有找到用户，则在其他表中查找

分析：认证都要经过AuthenticationProvider，每一个Ap中都会配置UserDetailsService，而不同的UDS则可以代表不同的数据源，所以只需要配置多个AP，并且为不同AP配置不同的UserDetailsService即可



#### 添加登录验证码

登录时候使用验证码是常见的需求，但是SpringSecurity没有提供自动化配置方案，需要开发者自行定义

常用实现登录验证码的两种思路：

1. 自定义过滤器（实现起来更容易些）：定义过滤器，将其放在UsernamePasswordAuthenticationFilter之前
2. 自定义认证逻辑：也即自定义AuthehticationProvider

生成验证码：Google开源库工具kaptcha

----





### 4、过滤器链分析



#### 基本原理

Spring Security核心就是过滤器，这些过滤器组成一个完整的过滤器链，采用责任链模式，项目启动后将会自动配置，将这组过滤链，加入到原生web过滤链中



#### Spring Security 初始化流程分析

1. **ObjectPostProcessor**

它是SpringSecurity中使用频率最高的组件之一，它是一个对象后置处理器，也就是当一个对象创建成功后，如果还需要一些额外的事情补充，则可以通过ObjectPostProcessor来进行处理，这个接口默认只有一个postProceess方法，该方法用来完成对对象的二次处理

![image-20220928214342403](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209282143538.png)



Spring Security中采用了大量的Java配置，许多过滤器是直接new出来的，这些new出的对象不会自动注入到Spring容器中，所以采用这两个实现类来将对象注入到Spring容器中，Spring Securtiy默认采用第二种方式，该方式一个对象可以有多个后置处理器，它里面的集合默认只有一个对象就是AutowireBeanFactoryObjectPostProcessor

Spring Security中，可以灵活配置需要哪些SpringSecurity过滤器，选定过滤器后，**每一个过滤器都会有一个对应的配置器**，叫做xxxConfigurer，例如CorsConfigurer、CsrfConfigurer，过滤器都是在xxxConfigurer中new出来的，然后在postProcess方法中处理将这些过滤器注入到Spring容器中



2. **SecurityFilterChain**

   就是Spring Security中过滤器链对象，里面包含过滤器集合

   

3. **SecurityBuilder**

   SpringSecurity中**所有**需要构建的对象都可以通过它来实现，默认的过滤器链、代理过滤器、AuthenticationManager等，都可以通过它来构建

   不同的SecurityBuilder会构建出不同的对象，例如AuthenticationManagerBuilder用于构建AuthenticationManager对象

   

4. HttpSecurity

   主要作用：构建一条过滤器链，反应在代码上，也就是构建一个DefaultSecurityFilterChain对象。一个DefaultSecurityFilterChain对象包含一个路径匹配器和多个Spring Security过滤器，HttpSecurity中通过收集各种各类的xxxConfigurer，将过滤器链对应的配置类收集起来，在后续的构建过程中，将这些xxxConfigurer构建为具体的Spring Security过滤器，同时添加到HttpSecurity的filters对象中

   

5. WebSecurity

   相对于HttpSecurity，它是一在更大的层面取构建过滤器，一个HttpSecurity对象可以构建一个过滤器链，也就是一个DefaultSecurityFilterChain对象，而一个项目中可以存在多个HttpSecurity对象，也就可以构建多个DSFC过滤器链。最终由它返回一个filterChainProxy对象

   

6. FilterChainProxy

   FilterChainProxy是最终构建出来的代理过滤器链，通过Spring提供的DelegatingFilterProxy，将FilterChainProxy对象嵌入到原生过滤器链（Web Filter）中，DelegatingFilterProxy作为一个代理对象，它不承载具体的业务

   ![image-20221012113502142](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210121136543.png)



##### SecurityConfigurer接口

核心方法init、configure，一个完成配置类的初始化操作，一个进行配置类的配置

WebSecurityConfigurer

是一个空接口，可以通过它来自定义WebSecurity，其只有一个实现类WebSecurityConfigurerAdapter，大多数情况下，开发者通过继承WebSecurityConfigurerAdapter来实现对WebSecurity的自定义配置

WebSecurityConfigurerAdapter



#### ObjectPostProcessor

所有的过滤器都由对应的配置类来负责创建，配置类将过滤器创建成功之后，会调用父类的postProcess方法。默认情况下，CompositeObjectPostProcessor对象中所维护的List集合中只有一个对象，那就是AutowireBeanFactoryObjectPostProcessor，调用该类的postProcess方法可以将对象注册到Spring容器

自定义ObjectPostProcessor，比较典型的场景是**动态权限配置**



#### 多种用户定义方式

自定义用户两种方式：

1. 重写configure(AuthenticationManagerBuilder)
2. 直接向Spring容器中注入UserDetailsService对象



Spring Security中存在两种类型的AuthenticationManager

全局AuthenticationManager、局部AuthenticationManager

登录认证时，先调用局部，若登录不成功再调用全局，全局默认登录名为user，密码为随机uuid，全局配置可以自定义



1. 全局的AuthenticationManager
   - 不用配置，系统会默认提供一个全局的AuthenticationManager对象
   - 也可以通过重写configure(AuthenticationManagerBuilder)方法进行全局配置
   - 默认的全局AuthenticationManager在配置时，会从Spring容器中找UserDetailsService实例，所以针对全局AuthenticationManager配置用户，只需要往Spring容器中注入一个UserDetailsService实例即可
2. 局部的AuthenticationManager

当用户进行验证时，首先通过局部的AuthenticationManager对象进行验证，若失败，则会调用其parent也就是全局的AuthenticationManager再次进行验证



#### AuthenticationManager

获取AuthenticationManager实例时，有两种不同的方式

1. 重写父类的authenticationManager
   - 获取的是全局的AuthenticationManager实例
2. 重写父类的authenticationManagerBean()
   - 获取的是局部的AuthenticationManager实例





#### 多个过滤器链

Spring Security中可以同时存在多个过滤器链，一个WebSecurityConfigurerAdapter的实例就可以配置一条过滤器链

比如/bar/xxx 、/foo/xxx分别为这种请求构建过滤链，是/bar/xx走自己的过滤器链中进行处理

设置了多个过滤器链，需要使用@Order注解来标记不同配置的优先级，数字越大优先级越低，会按照过滤器链的优先级从高到低，依次进行匹配





#### 静态资源过滤

实际项目中，并非所有的请求都要经过SpringSecurity过滤器，例如静态资源一般不需要经过过滤器链，用户如果访问这些静态资源，直接返回对应的资源即可

WebSecurity中有一个ignoredRequests变量，里面记录的就是所有需要被忽略的请求





#### 使用Json格式登录

SpringSecurity默认登录参数传递格式是k-v格式，也就是表单登录，实际项目中，一般使用Json格式来传递登录参数，这就需要自定义过滤器来实现

登录参数的提取是在UsernamePasswordAuthenticationFilter过滤器中完成的，若要使用JSON格式登录，只需要模仿该过滤器定义自己的过滤器，再将自定义的过滤器放到该过滤器之前即可







---

### 5、密码加密

实际项目中，密码等重要信息都会采用密文进行存储，若用明文存储会带来极高的安全风险。企业级应用中，密码不仅要加密还会采用加盐来保证密码的安全性

传统加密方式：将明文进行Hash运算(例如：SHA-256等等)得到密文进行存储

#### 彩虹表

是一个用于对加密Hash函数逆运算的表，**通常用于破解加密过的Hash字符串**，为了降低彩虹表对系统安全性的影响，人们又发明了**密码加盐**，即：**对明文加密后，再添加一个随机数(即盐)和密文再混合在一起进行加密**，这样即使明文相同，生成的加密字符串也是不同的，当然这个随机数也需要以明文方式和密文一起存放数据库的

用户登录：将输入的明文和存储再数据库中的盐一起进行Hash，再将运算结果和存储在数据库中的密文进行比较，进而确定用户的登录信息是否有效

但是：随着计算机硬件发展，每秒执行数十亿次Hash计算已经轻轻松松了，这意味着即使给密码加密加盐也不再安全

SpringSecurity采用了一种**自适应单向函数**来处理密码问题，这种函数在进行密码匹配时，会刻意占用大量系统资源(例如cup、内存等)，可以增加恶意用户攻击系统的难度

SpringSecurity中可以通过bcrypt、PBKDF2、scrypt等自适应单向函数进行加密

由于自适应单向函数刻意占用大量系统资源，因此每个登录认证请求会大大降低应用程序的性能，但是SpringSecurity没有采用任何措施来提高密码验证的速度，因为它正是通过这种方式来增强系统的安全性，所以开发者可以将用户名/密码这种长期凭证兑换为短期凭证，例如：会话、OAuth2令牌等，这样既可以快速验证用户凭证信息，又不会损失系统安全性。通常也是会采用token方式来登录验证



#### PasswordEncoder

常见实现类：BCryptPasswordEncoder、SCryptPasswordEncoder、PbkdfPasswordEncoder、Argon2PasswordEncoder

DelegatingPasswordEncoder：SpringSecrity5.0之后，默认的密码加密方案，它是一个代理类并非具体的密码方案，而是代理上面不同的密码加密方案



---

### 6、RememberMe（下次自动登录）

记住我功能：并不是将用户名/密码用Cookie保存在浏览器中，这是一种服务器端的行为。

传统的登录方式是基于Session会话，一旦用户关闭浏览器重新打开，就要再次登录，这样过于繁琐。考虑这样一种机制：用户关闭后并重新打开浏览器之后，还能继续保存认证状态，就会方便很多，RememberMe就是为了解决这一需求而生的。即：成功认证一次之后在一定的时间内我可以不用再输入用户名和密码进行登录了，系统会自动给我登录

例如：一些购物网站、博客等，发现网站即使关闭浏览器后，重写打开浏览器访问该网站，登录状态依然有效，这个功能的实现就是RememberMe



这种方式会存在安全隐患：会在响应头里面，将这个remember-me字符串携带给浏览器cookie，这样rememberMe泄露则恶意用户可以随意访问系统资源了

解决：通过持久化令牌以及二次校验来降低RememberMe所带来的安全风险

#### 持久化令牌

使用持久化令牌实现RememberMe的体验和使用普通令牌的登录体验是一样的，不同的是服务端所做的事情变了

持久化令牌在普通令牌的基础上，新增了series和token两个校验参数，当用户使用用户名/密码方式登录时，series才会自动更新；一旦有了新的会话，token会重新生成。所有，如果令牌被盗用一旦对方基于RememberMe登录成功后，就会生成新的token，你自己的登录令牌会失效，这样就能及时发现帐户泄漏并作出处理



#### 二次校验

二次校验就是将系统中的资源分为敏感和不敏感的，若用户使用了RememberMe的方式登录，则在访问敏感资源的时候会自动跳转到登录页面，登录后才能访问这些敏感资源，这就是在一定程度上牺牲了用户体验，但是换取了系统的安全性

例如：

1. /hello接口：认证后才能访问，无论通过什么认证方式
2. /hello2接口：认证才能访问，但是必须通过用户名/密码的方式认证
3. /hello3接口：认证后才能访问，当时必须通过RememberMe方式认证

#### 原理分析

具体使用时候再去学习

对应的过滤器：RememberMeAuthenticationFilter



---

### 7、会话管理

用户通过浏览器登录成功后，用户和系统之间就会保持一个会话(Session)，通过这个会话，系统可以确定出访问用户的身份，Spring Securtity中和会话相关的功能由SessionManagementFilter和SessionAuthticationStrategy接口来处理，过滤器委托该接口对会话进行处理，典型用法：防止会话固定攻击、配置会话并发数等等

####  会话

当浏览器调用登录接口登录成功后，服务端和浏览器之间便建立了一个会话（Session)，浏览器每次发送请求时都会携带一个SessionId，服务端则根据这个SessionId来判断用户身份

当浏览器关闭后，服务端的Session不会自动销毁，需要开发者手动在服务端调用Session的销毁方法或者等Session过期时间到了之后自动销毁

SpringSecurity中，与HttpSession相关的功能由SessionManagementFilter和SessionAuthenticationStrategy接口处理，SessionManagementFilter过滤器将Session相关操作委托给SessionAuthenticationStrategy接口去完成



#### 会话并发管理

会话并发管理：当前系统中，同一个用户可以同时创建多个会话，例如一台设备对应一个会话的话，简单理解就是为同一个用户可以同时在多台设备上进行登录，默认同一个用户没有设置多少设备登录没做限制，不过可以在SpringSecurity中进行配置

提供一个httpSessionEventPublisher实例，

配置： sessionManagement().maximumSessions(n)



#### Session共享

针对有状态登录、集群环境下会话管理方案

session共享：将不同服务的会话统一放在一个地方，所有的服务共享一个会话。一般使用一些k-v数据库存储Session，常见方案是使用Redis存储，Session共享方案由于其简便性与稳定性，目前使用较多

目前使用较多方案：Spring-session，利用spring-session可以方便地失效Session的管理

![image-20221013215545159](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210132208223.png)

##### 有状态登录

即：服务端需要记录每次会话的客户端信息，从而识别客户端身份

典型设计：Tomcat的Session

例如：用户登录后，把用户的信息保存在服务端的Session中，并且给用户一个Cookie值，记录对应的Session，下次请求，用户携带cookie(浏览器自动完成)请求，服务端能识别到对应的Session，从而找到用户的信息

缺陷：服务端保存大量数据，增加服务端压力；服务端保存用户状态，不支持集群化部署

##### 无状态登录

1. 客户端登录到服务端认证
2. 服务端返回token
3. 客户端每次携带token访问资源
4. 服务端校验token，判断是否有权访问





---

### 8、HttpFirewall

是Spring Security提供的Http防火墙，它可以用于拒绝潜在的危险请求或包装这些请求进而控制其行为，通过HttpFirewall可以对各种非法请求提前进行拦截并处理，降低损失

代码层面：HttpFirewall被注入到FilterChainProxy中，并在Spring Security过滤链执行前被触发

实际项目中：一般不使用

---

### 9、漏洞保护

SpringSecurity优势之一：为各种可能存在的漏洞提供了保护机制，这些保护机制默认是开启的，即：不用开发者考虑太多的事情，就可以开发出一套安全的权限管理系统

#### CSRF攻击与防御

CSRF：Cross-Site Request Forgery **跨站请求伪造**，也称为“一键式攻击”，通常缩写为CSRF、XSRF

CSRF是一种**挟持用户在当前已经登录的浏览器上发送恶意请求的攻击行为**，CSRF是利用网站对用户网页浏览器的信任

SpringSecurity默认是开启了csrf的防护的，但是一般都会禁用（见下令牌同步模式），因为启用后的规则很多



![image-20220929214316572](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292143184.png)



##### CSRF防御

csrf攻击的根源在于：浏览器默认的身份验证机制(自动携带当前网站的Cookie信息)，这种机制可以保证请求是来自某个浏览器的，但是不能保证该请求是用户授权发送的

Spring提供了两种机制来防御CSRF攻击

1. 令牌同步模式，目前主流的csrf攻击防御方案
2. 在Cookie上指的SameSite属性

这两种方式，前提都是请求方法的幂等性，即：Http请求中的Get、Head、Options、Trace方法不应该改变应用的状态

###### 令牌同步模式

目前主流的csrf攻击预防方案

每个Http请求中，除了默认自动携带的Cookie参数之外，再提供一个额外安全的字符串称为Csrf令牌，这个令牌由服务端生成，生成后在HttpSession中保存一份

当前端请求到达后，请求携带CSRF令牌信息和服务端保存的令牌进行对比，若令牌不一致则拒绝该Http请求

SpringSecurity中，默认不会对Get、Head、Options、Trance请求进行csrf令牌校验

post请求时候，开启csrf防御，请求中会包含一些隐藏域（_csrf），所以一般会禁用csrf防御功能

![image-20221013222830443](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210132228585.png)



#### HTTP响应头处理

Http响应头中的许多属性都可以用来提高Web安全

![image-20220929215829796](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292158902.png)

前三个与缓存相关

这些响应头都是在HeaderWriterFilter中添加的，默认情况下，该过滤器会添加到Spring Security过滤链中，HeaderWriterFilter是通过HeadersConfigurer进行配置的

##### 缓存控制

1. Cache-Control：no-cache、no-store、max-age=0、must-revalidate
   - 请求头和响应头都支持该字段
2. Pragma：no-cache
   - 作用：类似于Cache-Control：no-cache   兼容HTTP/1.0客户端
3. Expires：0
   - 指定一个日期，即：在指定日期之后，缓存过期。若日期值为0，则表示缓存过期

从上面解释可以看到SpringSecurity默认不做任何缓存。但是这个是**针对SpringSecurity过滤器的请求**，如果请求本身没有经过SpringSecurtiy过滤器，那么该缓存的还是会缓存

若请求经过SpringSecurity过滤器，但是又希望开启缓存功能，那么需要关闭SpringSecurity中关于缓存的默认配置：.cacheControl().disable()，这样SpringSecurity就不会配置Cache-Control、Pragma、Expires这三个缓存相关的相应头了

##### X-Content-Type_Options

得先了解MIME嗅探

X-Content-Type_Options响应头相当于一个提示标志，被服务器用来提示客户端一定要遵循在Content-Type中对MIME类型的设定，而不能对其进行修改。换言之，就是服务端告诉服务端其对于MIME类型的设置没有任何问题

X-Content-Type_Options：nosiniff



##### Strict-Transport-Security

用来指定当前客户端只能通过Https服务服务端，而不能通过Http访问

Strict-Transport-Security：max-age=3152600；includeSumDomains

1. max-age：浏览器收到这个请求后的多少秒的时间内，凡是访问这个域名下的请求都使用https请求

2. includeSubDomains：可选项，若被指定则表示第一条规则也适用于子域名

   

##### X-Frame-Options

该响应头用来告诉浏览器是否允许一个页面在<from>、<ifram>、<embed>或<objec>中展现，通过该响应头可以确保网站没有被嵌入到其他站点里面，进而避免发送单击劫持

**单击劫持**：一种视觉上的欺骗手段。攻击者将被劫持的网页放在一个iframe标签中，设置该iframe标签透明不可见，然后将该iframe标签覆盖在另一个页面上，最后诱使用户在该页面上进行操作，通过调整iframe页面的位置，可以诱使用户恰好单击在iframe页面的一些功能性按钮上

- deny：表示该页面不允许在frame中展示，即便在相同域名的页面中嵌套也不允许
- sameorigin：该页面可以在相同域名页面的frame中展示
- allow-from uri：表示该页面可以在指定来源的frame中展示

SpringSecurity中默认取值是Deny

~~~shell
X-Frame-Options：DENY
~~~



##### X-XSS-Protection

X-XSS-Protection响应头告诉浏览器，当检测到跨站脚本攻击（XSS）时，浏览器将停止加载页面

1. 0 表示禁止XSS过滤
2. 1表示启用XSS过滤(浏览器默认的)，若检测到跨站脚本攻击，浏览器将清除页面（删除不安全的部分）
3. 1;mode=block 表示启用XSS过滤。如果检测到攻击，浏览器不清除页面，而是阻止页面加载
4. 1;report=<reporting-URI> 表示启用过滤XSS，若检测到跨站脚本攻击，浏览器清除页面，并使用CSP report-uri指令的功能发送违规报告(Chrome支持)

XSS攻击：跨站脚本攻击，是一种安全漏洞，攻击者利用这种漏洞在网站注入恶意的javascript代码，而浏览器无法区分出这是恶意的javascript代码还是正常的javascript代码。当被攻击者登录网站时，就会自动运行这些恶意代码，攻击者可以利用这些恶意代码去窃取Cookie、监听用户行为以及修改DOM结构等



SpringSecurity中设置的X-XSS-Protection响应头为：

```shell
X-XSS-Protection： 1; mode=block
```





#### HTTP通信安全

三方面入手

1. 使用Https代替Http
   - Https访问端口是8443
2. Strict-Transport-Security配置
3. 代理服务器配置



---



### 10、HTTP认证



Http提供了一个用于认证和权限控制的通用方式，这种认证方式通过Http请求头来提供认证信息，而不是通过表单登录

例如熟知的Http Basic authentication，另一种更为安全的是http digest authentication

Spring Security对这两种认证方式都提供了支持



##### Http Basic authentication

**Http基本认证**，这种认证方式中：将用户的登录名/密码经过Base64编码后，放在请求头的Authorization字段中，从而完成用户身份的认证

WWW-Authenticate响应头：定义了用何种验证方式取完成身份认证

最简单、常见的是使用Http基本认证(Basic)，还有Bearer(OAuth2.0认证)、Digest(Http摘要认证)等取值

实际应用中，这种认证方式很少使用，主要还是安全问题，http基本认证没有对传输的凭证信息进行加密，仅仅是进行了Base64编码，这就造成了很大的安全隐患，所以Http基本认证一般是结合Https一起使用，同时一旦Http基本认证成功后，除非用户关闭浏览器或者清空浏览器缓存，否则没有办法退出登录



##### Http Digest authentication

http摘要认证，和Http基本认证的认证流程基本一种，不同的是每次传递的参数有所差异

Spring Security中为Http摘要认证提供了相应的AuthenticationEntryPoint和Filter，但是需要手动配置

Http摘要认证使用的也不多。。。



实际项目中这两个都不用，还是老老实实用JWT吧





---

### 11、跨域问题

跨域是一个非常常见的需求，Spring框架中对跨域有好几种方案，引入Spring Security后跨域方案又增加了

#### CORS

Cross-Origin Resource Sharing，是W3C制定的一种跨域资源共享技术标准，其目的就是为了解决前端的跨域请求

Options方法请求：预检请求，目的是查看服务端是否支持即将发起的跨域请求，如果服务端允许，才能发起实际的Http请求。在预检请求的返回中，服务端也可以通知客户端是否需要携带身份认证等等

若服务端支持跨域请求则返回响应头中将包含：Access-Control-Allow-Origin：http://xxxx

Access-Control-Allow-Origin：告诉浏览器可以访问该资源的域



#### Spring处理跨域方案

1. @CrossOrigin
   - 可以标注在方法上、Controller上
2. addCorsMappings
   - @CrossOrigin需要添加在不同的Controller上，所以需要一种全局的配置方法，就是重写WebMvcConfigurer的addCorsMappings方法来实现全局配置
   - 这两种都是在CrosInterceptor拦截器中实现的
3. **CorsFilter**：使用多
   - 是Spring Web提供的一个处理跨域的过滤器，可以通过这个过滤器处理跨域
   - 整体项目:则推荐使用这个
4. 选一种来处理跨域即可



#### Spring Security处理跨域方案

项目使用SpringSecurity后Spring提供的第1、2两种方案处理跨域方式会失效，第3种CorsFilter方案会不会失效则要看过滤器的优先级，若过滤器优先级高于SpringSecurity过滤器则有效，否则会失效



**Filter、DispatcherServlet、Interceptor执行顺序**如下：

![image-20220929232805913](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292328029.png)

使用@CrossOrigin注解或addCorsMappings方法配置跨域，最终都是在CorsInterceptor中对跨域进行校验的，要进入CrosIntercepter拦截器，首先要经过SpringSecurtiy过滤器链，而在SpringSecurity过滤器链时，由于预检请求没有携带认证信息，就会被拦截下来

如果使用CrosFilter配置跨域，则只要配置的过滤器优先级高于SpringSecurity优先级，即：在SpringSecurity过滤器之前执行跨域请求校验，那么就没有问题。若配置的过滤器优先级低于SpringSecurity，则会被SpringSecurity过滤器拦截下来



##### 特殊处理Options请求

引入SpringSeucrity之后，要想继续通过@CrossOrigin或addCrosMappings方法配置跨域，那么需要通过给Options请求单独放行，来解决预检请求被拦截的问题



1. configure(HttpSecurity)中配置：
   - 不过：这种直接在configure中直接指定所有的Options请求直接通过的方案，既不安全也不优雅，实际开发中很少使用，了解即可

~~~java
 //指定所有的Options请求直接通过
antMatchers(HttpMethod.Options).permitAll()  
~~~

2. 继续使用CorsFilter

   - 只需要将CorsFilter的优先级设置高于SpringSecurity过滤器优先级
   - ![image-20221014140031747](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210141400956.png)
   - FilterRegistrationBean实现过滤器、能够实现过滤器之间的优先级
   - order：值越低，优先级越高
   - 

   - ![image-20221014140321150](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210141403730.png)
   - 
   - 只要将CorsFilter的优先级高于FilterChainProxy即可
   - ![image-20221014141645556](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210141416856.png)
   - SpringSecurity过滤器的优先级，从SecurityProperties中获取的，该对象默认过滤器优先级为-100，所以，开发者配置CorsFilter过滤器只需要小于-100即可

   



**专业解决方案**

SpringSecurity中提供了更加专业的方式来解决预检请求所面临的问题

跨域问题的处理：根据开发者提供的CorsConfigurationSource对象构建出CorsFilter，并将CorsFilter放置于认证过滤器之前，所以能处理跨域问题

这是第四种构建CorsFilter的方式

核心思路：将处理跨域的过滤器放在Spring Securtity认证过滤器之前即可



**实际项目推荐：第三种，即CrosFilter**

---

### 12、Spring Security异常处理

Security种主要分为**认证异常**处理、**权限异常**处理，除此之外的异常则抛出，交由Spring去处理

AuthenticationException：认证异常

AccessDeniedException：权限异常



Spring Security异常处理主要由ExceptionTranslationFilter过滤器中完成

---



### 13、权限管理



认证和授权是Spring Security的两大核心功能，授权就是我们日常说的权限管理

两大核心功能间有很好的解耦和关系，无论使用哪种认证方式，都不影响权限管理功能的使用

Spring Security中对于**RBAC、ACL等不同权限模型都有很好的支持**



#### 什么是权限管理

认证就是确认用户身份，也就是我们常说的登录

授权则是**根据系统提前设置好的规则，给用户分配可以访问某一资源的权限**，用户根据自己所具有的权限，去执行相应的操作

一个优秀的认证+授权系统可以为我们的应用系统提供强有力的安全保障功能



#### SpringSecurity权限管理策略

主要有两种类型：

1. 基于过滤器的权限管理：FilterSecurityInterceptor

   - 主要用来拦截Http请求，拦截下来后，**根据Http请求地址进行权限校验**

2. 基于AOP的权限管理：MethodSecrityInterceptor

   - 主要用来处理方法级别的权限问题
   - 当调用某一个方法时候，通过Aop将操作拦截下来，然后判断用户是否具备相关的权限，具备则允许方法调用

   

#### 核心概念

##### 角色与权限

Security中，**用户登录成功后，用户信息保存在Authentication对象中**，该对象有一个getAuthorities方法，用来返回当前对象所具备的权限信息，也即：已经授予当前登录用户的权限

getAuthorities()返回值为Collection<? extends GrantedAuthority>，即：集合中存放的是GrantedAuthority的子类

无论用户的认证方式采用何种方式例如：用户/密码、RememberMe、还是其他的CAS、OAuth2等认证方式，最终用户的权限信息都可以通过getAuthorities获取

**设计层面讲**，角色和权限是完全不同的东西：权限就是一些具体的操作，例如针对员工数据的读权限、针对员工数据的写权限；角色是某些权限的集合，例如管理员角色、普通用户角色

例如：某个用户是管理员角色，管理员角色拥有的权限集合肯定比普通用户的权限集合更丰富

**代码层次讲**，角色和权限并没有太大的不同，特别是Spring Security中，角色和权限的处理方式基本是一样的，唯一的区别是会自动给角色添加一个ROLE_前缀，而权限则不会自动添加任何前缀



对于Authentication#getAuthorities方法返回值，则要根据情况来对待：

1. 权限系统设计简单：用户=>权限=>资源，那么getAuthorities方法的含义**就是返回用户的权限**

2. 权限系统设计复杂写：用户=>角色=>权限=>资源的系统中（用户关联角色、角色关联权限、权限关联资源），此时，可以将getAuthorities方法返回值**当作权限来理解**。SpringSecurity没有提供相关的角色类，因此这个时候需要自定义角色类

   - 系统中同时存在角色和权限，可以使用GrantedAuthority的实现类SimpleGrantedAuthority来表示一个权限，在该实现类中，可以将权限描述为一个字符串。例如：Read_Employee、Write_Employee。据此，定义角色类如下：角色继承自GrantedAuthority，一个角色对应多个权限

   - ~~~java
     @Data
     public class Role implements GrantedAuthority{
         private String name;
         private List<SimpleGrantedAuthority> allowedOperations=new ArrayList<>();
         
         @Override
         public String getAuthority(){
             return name;
         }
     }
     ~~~

   - 定义用户类的时候，**将角色转为权限**。 这里就体现为：代码层面-角色和权限差别不大

   - ~~~java
     public class User implements UserDetails{
         private List<Role> roles=new ArrayList<>();
         
         @Override
         public Collection<? extends GrantedAuthority> getAuthorities(){
             List<SimpleGrantedAuthority> authorities=new ArrayList<>();
             for(Role role:roles){
                 authorities.addAll(role.getAllowedOperations());
             }
             return 
                 authorities.stream().distinct().collect(Collectors.toList());
         }
     }
     ~~~

     

##### 角色继承

角色继承：指的是角色存在一个上下级的关系，例如：role_amdin继承自role_user，那么role_admin就自动具备role_user的所有权限

Hierarchy：层次结构、等级制度、统治集团

SpringSecurity中通过RoleHierarchy接口对角色继承提供支持，该接口中有一个getReachableGrantedAuthorities方法，返回**用户真正可触达的权限**。例如：role_admin继承自role_user，role_user继承自role_guest。那么role_admin的权限包含了这两个用户能访问的资源。该方法就是根据当前用户所具有的角色，从角色层级映射结构中解析成用户真正可触达的权限



##### 两种处理器

基于过滤器和基于Aop的权限管理，都涉及一个前置处理器和后置处理器

基于过滤器的权限管理中：请求首先到达FilterSecurityInterceptor，在其执行过程中，前置处理器会判断当前请求的用户是否具备相应的权限，若具备，则请求继续往下走，到达目标方法并执行完毕。在响应时，又会经过该过滤器的后置处理器去完成一些其他收尾工作。基于过滤器的权限管理中，后置处理器一般不工作

![image-20221017112040612](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210171120362.png)



基于方法（Aop)的权限管理：目标方法的调用会被MethodSecurityInterceptor拦截下来，实现原理就是Aop机制。当目标方法的调用被该拦截器拦截之后，在其invoke方法中，会首先执行前置处理器判断当前用户是否具备调用该目标方法所需的权限，若具备，则请求继续往下走执行目标方法。目标方法执行完毕后，在invoke方法中，再去执行后置处理器对目标方法的返回结果进行过滤或者鉴权，然后在invoke方法中将处理后的结果返回

![image-20221017112113052](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210171121408.png)



##### 前置处理器

###### 投票器AccessDecisionVoter

是SpringSecurity中权限管理中的一个组件，作用是**针对是否允许某一个操作进行投票**。当请求的url地址或当方法的调用被aop拦截下来，都会调用投票器对当前操作进行投票，以便决定是否允许当前操作

具体投票方法：根据用户所具有的权限以及当前请求需要的权限进行投票

SpringSecurity中，提供了多个投票器的实现，在具体使用中，可以单独使用一个，也可以多个一起使用。开发者还可以自定义投票器，需要注意的是：投票器的投票结果并非最终结果(通过、拒绝)，最终结果还要看决策器（AccessDecisionManager)

###### 决策器AccessDecisionManager

决策器由AccessDecisionManager负责，它会同时管理多个投票器，由它来调用投票器进行投票，然后根据投票结果做出相应的决策。所以它也称为**决策管理器**

一个AccessDecisionManager对应多个投票器，那么多个投票器针对同一个请求可能会给出多个不同的结果，那么这就要看决策器的决策结果了

1. AffirmativeBased：一票通过机制，即只要有一个投票器通过就可以访问（默认机制）
2. UnanimousBased：一票否决机制，即只要有一个投票器反对就不可以访问
3. ConsensusBased：少数服从多数机制

这是SpringSecurity提供的三个决策器，若无法满足我们的需求，那么我们可以自定义决策器，通过继承AbstractAccessDecisionManager来实现自己的决策器

**这就是前置处理器的大致逻辑，主要是投票器和决策器，无论是基于url地址还是基于方法的权限管理，都是在前置处理器中通过AccessDecisionManager调用AccessDecisionVoter进行投票，然后进而做出相应的决策**



##### 后置处理器

后置处理器一般只在基于方法的权限控制中会用到，当目标方法执行完毕后，通过后置处理器可以**对目标方法的返回值进行权限校验或过滤**



##### 权限元数据

###### ConfigAttribute

投票器的具体投票方法vote中，受保护的对象**所需要的权限**存储在一个Collection\<ConfigAttribute>集合中

ConfigAttribute用来存放与安全系统相关的属性，也就是系统关于权限的配置

```java
public interface ConfigAttribute extends Serializable{
    String getAttribute();
}
```

该接口只有一个getAttribute方法，返回具体的权限字符串，而GrantedAuthority中则是通过getAuthority方法返回用户所具有的权限，两者返回值都是字符串

ConfigAttribute有不同的实现类，例如：

1. WebExpressionConfigAttribute
   - 若用户是基于url来控制权限并且支持SpEL，那么默认配置的权限控制表达式会封装为该对象
2. SecurityConfig
   - 若用户使用了@Secured注解来控制权限，那么配置的权限就会被封装为SecurityConfig对象
3. Jsr250SecurityConfig
4. PreInvocationExpressionAttritute
5. PostInvocationExpressionAttritute

针对不同的配置方式，配置数据会以不同的ConfigAttribute对象存储



###### SecurityMetadataSource

投票器在投票时，需要两方面的权限：当前用户具备哪些权限；当前访问的url或方法需要哪些权限才能访问；投票器所做的就是对这两种权限进行比较

用户所具备的权限保存在Authentication中

当前访问的url或方法需要的权限：SecurityMetadataSource有关

作用：提供受保护对象所需要的权限。例如：用户访问一个url地址，该url地址需要哪些权限才能访问？这个就是由SecurityMetadataSource提供的

![image-20221017143221572](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210171432401.png)

默认url权限控制会将其请求地址和所需权限封装为一个map

FilterInvacationSecurityMetadataSource用于处理基于Url权限管理控制相关

例如：SpringSecurity中，若直接在configure(HttpSecurity)方法中配置url请求地址拦截

这段请求和权限之间的映射关系，会经过DefaultFilterInvocationSecurityMetadataSource的子类ExpressionBasedFilterInvocationSecurityMetadataSource进行处理，并将映射关系保存到requestMap中

~~~java
http.authorizaRequests()
.antMatchers("/admin/**").hasRole("admin")
.antMatchers("/user/**").access("hasRole('user')")
~~~

但是，**实际开发中，url地址和访问它所需要的权限是保存在数据库中的，所以此时需要自定义类实现FilterInvocationSecurityMetadataSource接口，然后重写里面的getAttributes()，在该方法中，根据当前请求的url地址去数据库查询其所需要的权限，然后将查询结果封装为相应的ConfigAttribute集合返回即可**



MethodSecurityMetadataSource用于处理基于方法的权限管理控制相关

其实现类较多，例如

1. PrePostAnnotationSecurityMetadataSource
   - @PreAuthorize、@PreFilter、等注解，将由其负责提供
2. SecuredAnnotationSecurityMetadataSource
3. 等等

总之：不同的权限拦截方式，都对应一个SecurityMetadataSource实现类，请求的url或方法需要什么权限，调用SecurityMetadataSource#getAttributes()就可以获取到



##### 权限表达式

SpringSecurity 3.0 开始引入了SpEL表达式进行权限配置，在请求的url或方法上，通过SpEL来配置需要的权限

SpringSecurity内置了很多表达式，基本上能满足需求。若内置表达式无法满足项目需求，那么可以自定义表达式。

![image-20221017144012061](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210171440125.png)



SecurityExpressOperations接口定义了基本的权限表达式

基本实现类SecurityExpressionRoot中定义的表达式既可以在url权限管理中使用，也可以在基于方法的权限管理中使用

在SpringSecurity中使用基于Url地址管理的权限管理，使用的是WebSecurityExpressionRoot，其继承自SecurityExpressionRoot，并添加了hasIpAddress方法，用来判断请求的ip地址是否满足要求



其他几个对象，使用时候看一下即可，比较简单



---

#### 基于Url地址的权限管理



基于Url地址的权限关联主要是通过**过滤器FilterSecurityInterceptor**来实现

若开发者配置了基于Url地址的权限管理，那么该过滤器会被自动添加到SpringSecurity过滤器链中，在过滤器链中拦截下请求，然后分析当前用户是否具备请求所需要的权限

Filter将请求拦截下来后，交给AccessDecisionManager进行处理，AccessDecisionManager则会调用投票器进行投票，然后对投票结果进行决策，最终决定请求是否通过

SpringSecurity会为hasRole表达式自动添加ROLE_ 前缀，所以用户信息从数据库中读取的时候，需要注意ROLE_ 前缀的问题



##### 角色继承

若需要配置角色继承，则只需要提供一个RoleHierarchy实例即可

~~~java
//role_amdin继承role_user  
@Bean
RoleHierarchy roleHierarchy(){
    RoleHierarchyImpl h=new RoleHierarchyImpl();
    h.setHierarchy("role_admin > role_user");
    return h;
}
~~~



##### 自定义表达式

自定义PermissionExpress类，并注册到Spring容器中，然后在里面定义相应的方法。最后在SecurityConfig中添加相应的匹配规则



权限管理系统复杂：复杂的是设计！！，技术相对来说更加容易

##### 基于Url权限管理控制-原理剖析

AbstractSecurityInterceptor

FilterSecurityInterceptor

AbstractInterceptUrlConfigurer





##### 动态管理权限规则

通过代码来配置Url拦截规则和请求url所需要的权限 比较死板、不灵活，若要调整某个url所需要的权限还得修改代码

所以，动态管理权限规则可以解决这样问题：**将Url拦截规则和访问Url所需要的权限都保存在数据库中**，这样，在不改变代码的情况下，只需要修改数据库中的数据，就可以对权限进行调整

简单设计：用户、角色、资源

1. menu表：相当于权限表（资源表），用于保存访问规则（理解为菜单也是可以的，因为点击某个菜单也是相当于进行资源访问）
   - /admin/xxx、角色(标识该项资源所需要的角色)
   - /user/xx
   - ....
2. role角色表：定义保存系统中的角色
   - role_admin、系统管理员
   - role_user、普通用户
   - ...
3. user用户表：使用系统的用户的信息
   - admin、123密码
   - user、345
   - ....
4. 用户-角色关联表
5. 角色-权限(资源)关联表

复杂些设计：用户、角色、权限、资源





#### 基于方法的权限管理

用的较少，一般用于特殊处理，比如某一个方法，只能由具有admin角色的用户才能访问



主要通过AOP实现，在Spring Security中通过MethodSecurityInterceptor来提供相关实现

不同于FilterSecurityInterceptor只是在请求之前进行前置处理，MethodSecurityInterceptor除了前置处理外，还可以进行后置处理，即：前置处理在请求之前判断是否具备相应的权限，后置处理则是在对方法执行完后，进行二次过滤



实现方式：目前，SpringBoot中，基于方法的权限管理，主要通过@EnableGlobalMethodSecurity注解开启权限注解的使用



---

### 权限模型



权限管理是一个非常复杂繁琐的工作，为了能开发出高效的易于维护的权限管理系统，需要一个“指导思想”，这是指导思想就是**权限模型**



#### DAC

Discretionary access control：自主访问控制

是一种访问控制模型，这种访问控制模型中，系统会根据被操作对象的权限控制列表中的信息，来决定当前用户能够对其进行哪些操作，用户可以将其具备的权限直接或者间接授予其他用户，这也是其称为自主访问控制的原因

#### MAC

Mandatory access control：强制访问控制，也叫非自主访问控制



#### ABAC（未来）

Attribute-Base Access Control：基于属性的访问控制，有时也称为PBAC或CBAC，也被称为下一代权限管理模型

基于属性的访问控制中一般包含四类属性：用户属性、环境属性、操作属性、资源属性，通过动态计算一个或一组属性是否满足某一条件来进行授权



在企业级开发中，目前最为流行的权限管理模型当属**RBAC**，除了RBAC外还有一个ACL权限模型，Spring Security中对ACL也提供了相关的依赖



#### ACL（次要）

Access control list：访问控制列表，是一种比较古老的权限控制模型，它是一种面向资源的访问控制模型，在ACL中，我们所做的所有权限配置都是针对资源的

ACL核心思路：将某个对象的某种权限授予某个用户或角色，它们之间的关系是多对多，即一个用户/角色可以具备某个对象的多种权限，某个对象的权限也可以被多个用户/角色持有

ACL是一种粒度非常细的权限控制，它可以精确到某一个资源的某一个权限，细到每一个资源的CRUD，这些权限数据都记录在数据库中，这带来的另一个问题就是需要维护的权限数据量非常大，特别是对于一些大型系统而言，大量的数据需要维护，可能会造成系统性能下降。不过对于一些简单的小型系统使用ACL还是可以的没有任何问题



#### RBAC（重要）

Role-Based access control：基于角色的访问控制，是一种以角色为基础的访问控制，它是一种较新且广为使用的权限控制模型，这种机制不是直接给用户赋予权限，而是将权限赋予角色

RBAC权限模型中，将用户按照角色进行归类，通过用户的角色来确定用户对某项资源是否具备操作权限

RBAC简化了用户与权限的管理，它将用户与角色管理、角色与权限管理、权限与资源管理，这种模式使得用户的授权管理变得非常简单和易于维护

RBAC权限模型三原则：

1. 最小权限：给角色配置的权限，是其能完成任务所需的最小权限集合
2. 职责分离：通过相互独立互斥的角色来共同完成任务
3. 数据抽象：通过权限的抽象来体现，RBAC支持的数据抽象程度与RBAC的实现细节有关

RBAC的应用非常广泛：常规的企业级应用、医疗、国防等等领域



##### RBAC权限模型分类

有四种不同的分类RBAC0~3

RBAC0

是最简单的用户、角色、权限模型，也是RBAC权限模型中最为核心的一部分，其他几种模型都是在此基础上建立的，该模型中，一个用户可以具备多个角色，一个角色可以具备多个权限，最终用户所具备的权限是用户所具备的角色的权限的并集

RBAC0 权限模型图：

![image-20221001230511120](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210012305760.png)



RBAC1

在RBAC0模型上引入了角色继承，让角色有了上下级关系，通过前面的角色继承实现方式完成

![image-20221001230705676](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210012307791.png)



RBAC2

该模型是在RBAC0继承上进行了扩展，引入了静态指责分类和动态指责分类，要理解职责分离，得先理解角色互斥

角色互斥：实际项目中，有一些角色是互斥的、对立的例如：财务这个角色，一般不能和其他角色兼任，否则会出现自己报账自己审批，通过职责分离则可以解决这一问题

静态职责分离 即Static Separation of Duty，SSD：在权限配置阶段就做限制。例如：同一用户不能授予互斥的角色，用户只能有有限个角色，用户过的高级权限之前要有低级权限

动态职责分离 即Dynamic Static Separation of Duty，DSD：在运行阶段进行限制。例如：运行时同一个用户下 5个角色只能同时有2个角色激活等等

![image-20221001231333982](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210012313079.png)



RBAC3

是RBAC1和RBAC2的合体

![image-20221001231547886](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210012315012.png)



若这四种模型依然不能满足项目的要求，则开发者可以在此基础上进行扩展，例如：用户分组、角色分组等，实际项目中用户分组、角色分组使用也比较多

---







**Spring Security和Spring Security OAuth2关系**

1. Spring Security是一个框架，提供了认证和授权
2. OAuth2只是一个协议，需要具体的实现
3. spring security oauth2：spring security框架中内置了oauth2，可以直接使用，当然其他框架也有实现了oauth2的

---



## OAuth2

Open Authority

https://blog.csdn.net/u012702547/article/details/105699777

一个验证授权的**开放标准协议**，所有人都能基于这个标准实现自己的OAuth，OAuth基于Https以及APIs，Service应用使用access token来进行身份验证

OAuth主要有OAuth1.0a和OAuth2.0两个版本，二者完全不同，而且不兼容。2.0是目前广泛使用的版本

#### OAuth2.0

目前流行的**授权机制**，是一个非常重要的**认证协议**，用来授权第三方应用获取用户数据，利用第三方系统获得资源。该标准允许用户让第三方应用访问该用户在某一网站上存储的私密资源，并且这个过程中无须将用户名和密码提供给第三方应用。通过token可以实现这一功能，每一个令牌授权一个特定的网站，在特定的时间段内允许访问特定的资源

常用于第三方登录，例如：微信、微博、Github授权登录等等

例如：用户想通过微信登录微博，此时微博就是一个第三方应用，微博需要访问用户存在在微信服务器上的一些用户基本信息，此时就需要得到用户的信息。如果用户把自己的微信用户名/密码告诉微博，那么微博就能访问用户存储在微信服务器上的所有数据，并且用户只有修改密码才能收回授权，这种授权方式安全隐患很大，使用OAuth就能很好的解决这一问题

OAuth2关注客户端开发者的简易型，同时为Web应用、桌面应用、移动设备等提供专门的认证流程

Spring Security对OAuth2协议提供了相应的支持，可以在Spring Security中非常方便的使用OAuth2协议，这也是Spring Security的魅力之一

#### Spring Social

是一个遵循OAuth协议的框架，暂不去了解



#### 为什么要有OAuth

在OAuth之前，使用用户名、密码进行身份验证，这种形式不安全。OAuth的出现就是为了解决访问资源的安全性以及灵活性。OAuth使得第三方应用对资源的访问更加安全

#### OAuth2.0中四个角色

OAuth的流程中，主要有以下四个角色

1. Client：第三方应用，想要访问用户的客户端，它使用OAuth来获取访问权限
2. Resouce Owner：用户拥有资源服务器上面的数据
3. Resouce Server：资源服务器，能够通过http请求进行访问，在访问时需要OAuth访问令牌。受保护资源需要验证收到的令牌，并决定是否响应以及如何响应请求
4. Authorization Server：授权服务器，一个HTTP服务器，OAuth的主要引擎。授权服务器对资源拥有者和客户端进行身份认证，让资源拥有者向客户端授权，为客户端颁发令牌。

例如：假设你使用了一个照片云存储服务和一个云打印服务，并且想使用云打印服务来打印存放在云存储服务上的照片。很幸运，这两个服务能够使用API 来通信。这很好，但两个服务由不同的公司提供，这意味着你在云存储服务上的账户和在云打印服务上的账户没有关联。使用OAuth 可以解决这个问题：授权云打印服务访问照片，但并不需要将存储服务上的账户密码交给它。
在这上面这一段中：
客户端 ： 云打印服务
资源拥有者：你
资源服务器，授权服务器：照片云存储服务

![image-20220319190248139](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203191902399.png)





#### OAuth2.0四种授权模式

OAuth2.0对于如何颁发令牌的细节，规定的很细，一共分为四种授权类型即：四种颁发令牌的方式，适用于不同的场景

##### 授权码模式

常见的第三方平台登录功能基本都是使用这种模式，是最安全并且使用最广泛的一种模式

授权码模式中分为授权服务器和资源服务器，授权服务器用来分派Token、拿着Token则可以去资源服务器获取资源，这两个服务器可以分开也可以合并



##### 简化模式

简化模式是：不需要客户端服务器参与，直接在浏览器中向授权服务器申请令牌，一般如果网站是纯静态页面可以采用这种方式

##### 密码模式

密码模式是用户把用户名密码直接告诉客户端，客户端使用说这些信息向授权服务器申请令牌（token）。这需要用户对客户端高度信任，**例如客户端应用和服务提供商就是同一家公司，我们自己做前后端分离登录就可以采用这种模式**

密码模式在SpringCloud项目中广泛应用

密码模式有一个前提就是你高度信任第三方应用，举个不恰当的例子：如果我要在 www.javaboy.org 这个网站上接入微信登录，我使用了密码模式，那你就要在 www.javaboy.org 这个网站去输入微信的用户名密码，这肯定是不靠谱的，所以密码模式需要你非常信任第三方应用

密码模式的流程：

密码式的流程比较简单：

假如www.javabay.org这个网站要接入微信授权登录

首先 www.javaboy.org 会发送一个 post 请求，类似下面这样的：

```
https://wx.qq.com/oauth/authorize?response_type=password&client_id=javaboy&username=江南一点雨&password=123
```

response_type 的值这里是 password，表示密码式，另外多了用户名/密码参数，没有重定向的 redirect_uri ，因为这里不需要重定向。

微信校验过用户名/密码之后，直接在 HTTP 响应中把 令牌 返回给客户端。



##### 客户端模式

客户端模式是指客户端使用自己的名义而不是用户的名义向服务提供者申请授权，严格来说，客户端模式并不能算作 OAuth 协议要解决的问题的一种解决方案，但是，对于开发者而言，在一些前后端分离应用或者为移动端提供的认证授权服务器上使用这种模式还是非常方便的

---



#### OAuth2.0令牌存放

##### 令牌存在哪里

![image-20220319202813566](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203192028725.png)

1. InMemoryTokenStore 内存中，也是系统默认的，不推荐
2. jdbcTokenStore 保持到数据库中
3. JwtTokenStore 这个其实不是存储，因为使用了 jwt 之后，在生成的 jwt 中就有用户的所有信息，服务端不需要保存，这也是无状态登录
4. RedisTokenStore，这个就是将 access_token 存到 redis 中
   - access_token 这个 key 在 Redis 中的有效期就是授权码的有效期。正是因为 Redis 中的这种过期机制，让它在存储 access_token 时具有天然的优势
5. JwkTokenStore，将 access_token 保存到 JSON Web Key

**掌握2、3、4**



#### OAuth2.0结合JWT

https://mp.weixin.qq.com/s/xEIWTduDqQuGL7lfiP735w

传统的通过 session 来记录用户认证信息的方式我们可以理解为这是一种有状态登录，而 JWT 则代表了一种无状态登录

#####  什么是有状态

有状态服务，即服务端需要记录每次会话的客户端信息，从而识别客户端身份，根据用户身份进行请求的处理，典型的设计如 Tomcat 中的 Session。例如登录：用户登录后，我们把用户的信息保存在服务端 session 中，并且给用户一个 cookie 值，记录对应的 session，然后下次请求，用户携带 cookie 值来（这一步有浏览器自动完成），我们就能识别到对应 session，从而找到用户的信息。这种方式目前来看最方便，但是也有一些缺陷，如下：

- 服务端保存大量数据，增加服务端压力
- 服务端保存用户状态，不支持集群化部署

##### 什么是无状态

微服务集群中的每个服务，对外提供的都使用 RESTful 风格的接口。而 RESTful 风格的一个最重要的规范就是：服务的无状态性，即：

- 服务端不保存任何客户端请求者信息
- 客户端的每次请求必须具备自描述信息，通过这些信息识别客户端身份

那么这种无状态性有哪些好处呢？

- 客户端请求不依赖服务端的信息，多次请求不需要必须访问到同一台服务器
- 服务端的集群和状态对客户端透明
- 服务端可以任意的迁移和伸缩（可以方便的进行集群化部署）
- 减小服务端存储压力

#####  如何实现无状态

无状态登录的流程：

- 首先客户端发送账户名/密码到服务端进行认证
- 认证通过后，服务端将用户信息加密并且编码成一个 token，返回给客户端
- 以后客户端每次发送请求，都需要携带认证的 token
- 服务端对客户端发送来的 token 进行解密，判断是否有效，并且获取用户登录信息



授权服务器颁发令牌后，客户端拿着令牌去请求资源服务器，资源服务器回去校验令牌的真伪。与JWT结合，实际上令牌就不要存储了（无状态登录，服务端不需要保存信息），因为用户的所有信息都在jwt里面



---

### SpringSecurity OAuth2

Spring Security对OAuth2提供了很好的支持，使得在Spring Security中使用OAuth2非常方便，在对OAuth2的落地支持方案中比较混乱，例如：Spring Security OAuth、SpringCloud Security、SpringBoot1.5x开始都提供了对OAuth2的实现，以至于非常混乱

Spring Security5开始，统一了OAuth2的支持

一般来说，当项目中使用OAuth2时，都是开发客户端，授权服务器和资源服务器都是外部提供的。例如在自己开发的一个网站上集成Github.com第三方登录，只需要开发自己的客户端即可，认证服务器和授权服务器都是Github提供的



---

承接上OAuth2，token默认是明文不安全，采用JWT进行加密更安全

### JWT：Json Web Tokens

Jwt其实是一种广泛使用的token，他通过数字签名的方式，以JSON为载体，在不同的服务终端之间安全的传输信息

是一种 JSON 风格的轻量级的授权和身份认证**规范**，可实现无状态、分布式的 Web 应用授权。作为一种规范，并没有与某一种语言绑定在一起，开发者可以使用任何语言来实现JWT，Java中的JWT相关开源库也比较多，例如：jjwt、nimbus-jose-jwt



#### 常见应用场景

1. 授权认证，用户登录后，后续每个请求都将包含jwt，系统每次处理用户请求前，都要先进行jwt安全校验，通过校验后才能访问资源
2. 单点登录



#### JWT数据格式

JWT包含三部分数据：Header、Payload、Signature

由三部分组成，这三部分使用 "."号 隔开，会对头部进行Base64编码方式编码，例如：eyJhbGc6IkpXVCJ9.eyJpc3MiOiJCIsImVzg5NTU0NDUiLCJuYW1lnVlfQ.SwyHTf8AqKYMAJc

1. header：包含token的类型和加密算法

   - ```json
     { 
       "alg": "HS256",	 //签名使用的加密算法（第三部分的signature）：通常是HMAC SHA256
        "typ": "JWT"      //声明类型：JWT
     } 
     ```

   - 常见加密算法：MD5、SHA、HAMC

     - MD5：message-digest algorithm 5 信息-摘要算法，广泛用于文件校验
     - SHA：Secure Hash Algorithm 安全散列算法，数字签名等密码学应用中重要的工具，安全性高于MD5
     - HMAC：Hash Message Authentication Code 散列消息鉴别码，基于密钥的Hash算法的认证协议。用公开函数和密钥产生一个固定长度的值作为认证标识，用这个标识鉴别消息的完整性。常用于接口签名验证

   - 我们会对头部进行 Base64Url 编码（可解码），得到第一部分数据

2. payload：载荷，就是存放有效信息的地方  （payload其实就是完整的Claims，每一项表示一个claim），Claims是对实体及额外数据的描述，例如对用户身份、权限的描述。默认情况下JWT是未加密的

   - iss (issuer)：表示jwt签发者
   - exp (expiration time)：表示token过期时间
   - sub (subject)：主题、即jwt所面向的用户
   - aud (audience)：受众、即接受jwt的一方
   - nbf (Not Before)：生效时间、即定义在什么时间之前，该jwt都是不可用的
   - iat (Issued At)：jwt签发时间
   - jti (JWT ID)：jwt唯一身份认证编号，主要用来做一次性token，从而避免重放攻击
   - 这部分也会采用 Base64Url 编码，得到第二部分数据。

3. signature：签名，是对上两部分数据签名，通过指定的算法生成Hash，以确保数据不会被篡改，是整个数据的认证信息。

   - 签名生成依赖于散列或加解密算法，例如：SHA256、512等，也就是通过SHA256对于编码后的`Header`和`Claims`（payload）字符串进行一次散列计算

   - ```java
     //伪代码
     
     // 不进行编码
     HMACSHA256(
       base64UrlEncode(header) + "." +
       base64UrlEncode(payload),
       256 bit secret key
     )
     
     // 进行编码
     base64UrlEncode(
         HMACSHA256(
            base64UrlEncode(header) + "." +
            base64UrlEncode(payload)
            [256 bit secret key])
     )
         
     ```

   - 一般根据前两步的数据，再加上服务的密钥 secret（密钥保存在服务端，不能泄露给客户端）。这个密钥通过 Header 中配置的加密算法生成，得到的最终的token。用于验证整个数据完整和可靠性。

https://www.cnblogs.com/throwable/p/14419015.html这个讲的还行

项目：https://www.jianshu.com/p/e88d3f8151db

#### JWT交互流程

![image-20220319204234978](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/img/202203192042146.png)

1. 应用程序或客户端向授权服务器请求授权
2. 获取到授权后，授权服务器会向应用程序返回访问令牌
3. 应用程序使用访问令牌来访问受保护资源（如API）

因为 JWT 签发的 token 中已经包含了用户的身份信息，并且每次请求都会携带，这样服务就无需保存用户信息，甚至无需去数据库查询，这样就符合了 RESTful 的无状态规范

#### JWT存在的问题

JWT 也不是天衣无缝，由客户端维护登录状态带来的一些问题在这里依然存在，举例如下：

1. 续签问题，这是被很多人诟病的问题之一，传统的 cookie+session 的方案天然的支持续签，但是 jwt 由于服务端不保存用户状态，因此很难完美解决续签问题，如果引入 redis，虽然可以解决问题，但是 jwt 也变得不伦不类了。
2. 注销问题，由于服务端不再保存用户信息，所以一般可以通过修改 secret 来实现注销，服务端 secret 修改后，已经颁发的未过期的 token 就会认证失败，进而实现注销，不过毕竟没有传统的注销方便。
3. 密码重置，密码重置后，原本的 token 依然可以访问系统，这时候也需要强制修改 secret。
4. 基于第 2 点和第 3 点，一般建议不同用户取不同 secret(密钥)
5. 默认JWT是不加密的，一般生成令牌后，可以对该令牌再次进行加密(项目中是利用令牌的后16为来加解密)

#### Java-jwt

是Java推荐的JWT实现库而已，通过maven导入即可使用

~~~java
//产生加密token
String token = JWT.create()
  .withExpiresAt(newDate(System.currentTimeMillis()))  //设置过期时间
  .withAudience("user1") //设置接受方信息，一般时登录用户
  .sign(Algorithm.HMAC256("111111"));  //使用HMAC算法，111111作为密钥加密

//解密Token获取负载信息并验证token是否有效
String userId = JWT.decode(token).getAudience().get(0);
Assertions.assertEquals("user1", userId);
JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("111111")).build();
jwtVerifier.verify(token);


~~~



微服务中使用Spring Security+Jwt中认证流程：

![image-20221002221511891](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210022215077.png)



用户在提交登录信息后，服务器校验数据后将通过密钥的方式来生成一个字符串token返回给客户端，客户端在之后的请求会把token放在header里，在请求到达服务器后，服务器会检验和解密token，如果token被篡改或者失效将会拒绝请求，如果有效则服务器可以获得用户的相关信息并执行请求内容，最后将结果返回。
 在微服务架构下,通常有单独一个服务Auth去管理相关认证，为了安全不会直接让用户访问某个服务，会开放一个入口服务作为网关gateway，只允许外网网关，所有请求首先访问gateway，有gateway将请求路由到各个服务。

客户端请求网关后，增加一个filter,此filter来过滤请求，网关会根据路径过滤请求，是登录获取token操作的路径则直接放行，请求直接到达auth服务进行登录操作，之后进行JWT私钥加密生成token返回给客户端；是其他请求将会进行token私钥解密校验，如果token被篡改或者失效则直接拒绝访问并返回错误信息，如果验证成功经过路由到达请求服务，请求服务响应并返回数据



**如何实现登录、刷新、注销等？**
 登录比较简单，在验证身份信息后可以使用工具包例如jjwt根据用户信息生成token并设置有效时长，最后将token返回给客户端存储即可，客户端只需要每次访问时将token加在请求头里即可,然后在zuul增加一个filter,此filter来过滤请求，如果是登录获取token则放行，其他的话用公钥解密验证token是否有效。
 如果要实现刷新，则需要在生成token时生成一个refreshKey，在登录时和token一并返回给客户端，然后由客户端保存定时使用refreshKey和token来刷新获取新的有效时长的token,这个refreshKey可自定义生成，为了安全起见，服务器可能需要缓存refreshKey，可使用redis来进行存储，每次刷新token都将生成新的refreshKey和token，服务器需要将老refreshKey替换，客户端保存新的token和refreshKey来进行之后的访问和刷新。
 如果要实现注销，并使得旧的token即便在有效期内也不能通过验证，则需要修改登录、刷新、和优化zuul的filter。首先在登录时生成token和refreshKey后，需要将token也进行缓存，如果通过redis进行缓存可以直接放一个Set下，此Set存储所有未过期的token。其次，在刷新时在这个Set中删除旧的token并放入新的。最后对zuulFilter进行优化，在解密时先从redis里存放token的Set查找此token是否存在（redis的Set有提供方法），如果没有则直接拒绝，如果有再进行下一步解密验证有效时长，验证有效时长是为了防止刷新机制失效、没有刷新机制、网络异常强行退出等事件出现，在这种情况下旧的token没有被删除，导致了旧的token一直可以访问（如果只验证是否token是否在缓存中）。在注销时只需要删除redis中Set的token记录就好，最后写个定时器去定时删除redis中Set里面过时的token,原因也是刷新机制失效、没有刷新机制、网络异常强行退出等事件出现导致旧的token没有被删除

---

---