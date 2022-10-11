## Spring Security

权限管理框架，可以做用户验证、权限管理等等

有了SpringBoot、Cloud后，Security比Shiro更具优势，也使用的更多

它提供了一组可以在Spring应用上下文中配置的Bean，充分利用了Spring IoC，DI（控制反转Inversion of Control ,DI:Dependency Injection 依赖注入）和AOP（面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作



### Spring Security架构

### 核心功能

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

#### 基本原理

Spring Security核心就是一组过滤器链，采用责任链模式，项目启动后将会自动配置，将这组过滤链，加入到原生web过滤链中





---

### Spring Security认证流程分析

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





##### 认证配置多数据源

多个数据源：指的是同一个系统中，用户数据来自不同的表，认证时，若第一张表没有找到用户，则在其他表中查找

分析：认证都要经过AuthenticationProvider，每一个Ap中都会配置UserDetailsService，而不同的UDS则可以代表不同的数据源，所以只需要配置多个AP，并且为不同AP配置不同的UserDetailsService即可



##### 添加登录验证码

登录时候使用验证码是常见的需求，但是SpringSecurity没有提供自动化配置方案，需要开发者自行定义

常用实现登录验证码的两种思路：

1. 自定义过滤器
2. 自定义认证逻辑（推荐）：也即自定义AuthehticationProvider

生成验证码：Google开源库工具kaptcha

----





### 过滤器链分析



##### 初始化流程分析

1. **ObjectPostProcessor**

它是SpringSecurity中使用频率最高的组件之一，它是一个后置处理器，也就是当一个对象创建成功后，如果还需要一些额外的事情补充，则可以通过ObjectPostProcessor来进行处理，这个接口默认只有一个postProceess方法，该方法用来完成对对象的二次处理

![image-20220928214342403](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209282143538.png)



Spring Security中采用了大量的Java配置，许多过滤器是直接new出来的，这些new出的对象不会自动注入到Spring容器中，所以采用这两个实现类来将对象注入到Spring容器中，Spring Securtiy默认采用第二种方式，该方式一个对象可以有多个后置处理器，它里面的集合默认只有一个对象就是AutowireBeanFactoryObjectPostProcessor

Spring Security中，可以灵活配置需要哪些SpringSecurity过滤器，选定过滤器后，**每一个过滤器都会有一个对应的配置器**，叫做xxxConfigurer，例如CorsConfigurer、CsrfConfigurer，过滤器都是在xxxConfigurer中new出来的，然后在postProcess方法中处理将这些过滤器注入到Spring容器中



2. **SecurityFilterChain**

   就是Spring Security中过滤器链对象

3. **SecurityBuilder**

   SpringSecurity中**所有**需要构建的对象都可以通过它来实现，默认的过滤器链、代理过滤器、AuthenticationManager等，都可以通过它来构建

   不同的SecurityBuilder会构建出不同的对象，例如AuthenticationManagerBuilder用于构建AuthenticationManager对象

4. WebSecurity

   是一在更大的层面取构建过滤器，一个HttpSecurity对象可以构建一个过滤器链，也就是一个DefaultSecurityFilterChain对象，而一个项目中可以存在多个HttpSecurity对象，也就可以构建多个DSFC过滤器链

   最终由它返回一个filterChainProxy对象，这个对象是最终构建出来的代理过滤器链，通过Spring提供的DelegatingFilterProxy将其嵌入到原生Web Filter中

5. FilterChainProxy

   SpringSecurity中的过滤器链中的最终执行，就是在FilterChainProxy中

   

##### SecurityConfigurer接口

核心方法init、configure，一个完成配置类的初始化操作，一个进行配置类的配置







#### 多个过滤器链

全局AuthenticationManager、局部AuthenticationManager

登录认证时，先调用局部，若登录不成功再调用全局，全局默认登录名为user，密码为随机uuid，全局配置可以自定义



Spring Security中可以同时存在多个过滤器链，一个WebSecurityConfigurerAdapter的实例就可以配置一条过滤器链

比如/bar/xxx 、/foo/xxx分别为这种请求构建过滤链，是/bar/xx走自己的过滤器链中进行处理



##### 静态资源过滤

实际项目中，并非所有的请求都要经过SpringSecurity过滤器，例如静态资源一般不需要经过过滤器链，用户如果访问这些静态资源，直接返回对应的资源即可

WebSecurity中有一个ignoredRequests变量，里面记录的就是所有需要被忽略的请求



##### 使用Json格式登录

SpringSecurity默认登录参数传递格式是k-v格式，也就是表单登录，实际项目中，一般使用Json格式来传递登录参数，这就需要自定义过滤器来实现

---

### 密码加密

实际项目中，密码等重要信息都会采用密文进行存储，若用明文存储会带来极高的安全风险。企业级应用中，密码不仅要加密还会采用加盐来保证密码的安全性

传统加密方式：将明文进行Hash运算(例如：SHA-256等等)得到密文进行存储

##### 彩虹表

是一个用于对加密Hash函数逆运算的表，通常用于破解加密过的Hash字符串，为了降低彩虹表对系统安全性的影响，人们又发明了密码加盐，即：对明文加密后，再添加一个随机数(即盐)和密文再混合在一起进行加密，这样即使明文相同，生成的加密字符串也是不同的，当然这个随机数也需要以明文方式和密文一起存放数据库的

用户登录：将输入的明文和存储再数据库中的盐一起进行Hash，再将运算结果和存储在数据库中的密文进行比较，进而确定用户的登录信息是否有效

但是：随着计算机硬件发展，每秒执行数十亿次Hash计算已经轻轻松松了，这意味着即使给密码加密加盐也不再安全

SpringSecurity采用了一种**自适应单向函数**来处理密码问题，这种函数在进行密码匹配时，会刻意占用大量系统资源(例如cup、内存等)，可以增加恶意用户攻击系统的难度

SpringSecurity中可以通过bcrypt、PBKDF2、scrypt等自适应单向函数进行加密

由于自适应单向函数刻意占用大量系统资源，因此每个登录认证请求会大大降低应用程序的性能，但是SpringSecurity没有采用任何措施来提高密码验证的速度，因为它正是通过这种方式来增强系统的安全性，所以开发者可以将用户名/密码这种长期凭证兑换为短期凭证，例如：会话、OAuth2令牌等，这样既可以快速验证用户凭证信息，又不会损失系统安全性。通常也是会采用token方式来登录验证



##### PasswordEncoder

常见实现类：BCryptPasswordEncoder、SCryptPasswordEncoder、PbkdfPasswordEncoder、Argon2PasswordEncoder

DelegatingPasswordEncoder：SpringSecrity5.0之后，默认的密码加密方案，它是一个代理类并非具体的密码方案，而是代理上面不同的密码加密方案



---

### RememberMe（下次自动登录）

记住我功能：并不是将用户名/密码用Cookie保存在浏览器中，这是一种服务器端的行为。

传统的登录方式是基于Session会话，一旦用户关闭浏览器重新打开，就要再次登录，这样过于繁琐。考虑这样一种机制：用户关闭后并重新打开浏览器之后，还能继续保存认证状态，就会方便很多，RememberMe就是为了解决这一需求而生的。即：成功认证一次之后在一定的时间内我可以不用再输入用户名和密码进行登录了，系统会自动给我登录



这种方式会存在安全隐患：会在响应头里面，将这个remember-me字符串携带给浏览器cookie，这样rememberMe泄露则恶意用户可以随意访问系统资源了

解决：通过持久化令牌以及二次校验来降低RememberMe所带来的安全风险

二次校验就是将系统中的资源分为敏感和不敏感的，若用户使用了RememberMe的方式登录，则在访问敏感资源的时候会自动跳转到登录页面，登录后才能访问这些敏感资源，这就是在一定程度上牺牲了用户体验，但是换取了系统的安全性





对应的过滤器：RememberMeAuthenticationFilter



---

### 会话管理

用户通过浏览器登录成功后，用户和系统之间就会保持一个会话(Session)，通过这个会话，系统可以确定出访问用户的身份，Spring Securtity中和会话相关的功能由SessionManagementFilter和SessionAuthticationStrategy接口来处理

 

##### 会话并发管理

会话并发管理：当前系统中，同一个用户可以同时创建多个会话，例如一台设备对应一个会话的话，简单理解就是为同一个用户可以同时在多台设备上进行登录，默认同一个用户没有设置多少设备登录没做限制，不过可以在SpringSecurity中进行配置

提供一个httpSessionEventPublisher实例

sessionManagement().maximumSessions(n)



---

### HttpFirewall

是Spring Security提供的Http防火墙，它可以用于拒绝潜在的危险请求或包装这些请求进而控制其行为，通过HttpFirewall可以对各种非法请求提前进行拦截并处理，降低损失

代码层面：HttpFirewall被注入到FilterChainProxy中，并在Spring Security过滤链执行前被触发

---

### 漏洞保护



##### CSRF攻击与防御

CSRF：Cross-Site Request Forgery 跨站请求伪造，也称为“一键式攻击”，通常缩写为CSRF、XSRF

CSRF是一种挟持用户在当前已经登录的浏览器上发送恶意请求的攻击行为，CSRF是利用网站对用户网页浏览器的新任

SpringSecurity默认是开启了csrf的防护的



![image-20220929214316572](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292143184.png)



##### CSRF防御

csrf攻击的根源在于：浏览器默认的身份验证机制(自动携带当前网站的Cookie信息)，这种机制可以保证请求是来自某个浏览器的，但是不能保证该请求是用户授权发送的

Spring提供了两种机制来防御CSRF攻击

1. 令牌同步模式，目前主流的csrf攻击防御方案
2. 在Cookie上指的SameSite属性

这两种方式，前提都是请求方法的幂等性，即：Http请求中的Get、Head、Options、Trace方法不应该改变应用的状态

###### 令牌同步模式

每个Http请求中，除了默认自动携带的Cookie参数之外，再提供一个额外安全的字符串称为Csrf令牌，这个令牌由服务端生成，生成后在HttpSession中保存一份

当请求到达后，请求携带CSRF令牌信息和服务端保存的令牌进行对比，若令牌不一致则拒绝该Http请求



##### HTTP响应头处理

Http响应头中的许多属性都可以用来提高Web安全

![image-20220929215829796](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292158902.png)



这些响应头都是在HeaderWriterFilter中添加的，默认情况下，该过滤器会添加到Spring Security过滤链中



##### HTTP通信安全

三方面入手

1. 使用Https代替Http
   - Https访问端口是8443
2. Strict-Transport-Security配置
3. 代理服务器配置



---

### HTTP认证

Http提供了一个用于认证和权限控制的通用方式，这种认证方式通过Http请求头来提供认证信息，而不是通过表单登录

例如熟知的Http Basic authentication，另一种更为安全的是http digest authentication

Spring Security对这两种认证方式都提供了支持



##### Http Basic authentication

Http基本认证，这种认证方式中：将用户的登录名/密码经过Base64编码后，放在请求头的Authorization字段中，从而完成用户身份的认证

WWW-Authenticate响应头：定义了用何种验证方式取完成身份认证

最简单、常见的是使用Http基本认证(Basic)，还有Bearer(OAuth2.0认证)、Digest(Http摘要认证)等取值

实际应用中，这种认证方式很少使用，主要还是安全问题，http基本认证没有对传输的凭证信息进行加密，仅仅是进行了Base64编码，这就造成了很大的安全隐患，所以Http基本认证一般是结合Https一起使用，同时一旦Http基本认证成功后，除非用户关闭浏览器或者清空浏览器缓存，否则没有办法退出登录



##### Http Digest authentication

http摘要认证，和Http基本认证的认证流程基本一种，不同的是每次传递的参数有所差异

Spring Security中为Http摘要认证提供了相应的AuthenticationEntryPoint和Filter，但是需要手动配置

Http摘要认证使用的也不多。。。

还是老老实实用JWT把

---

### 跨域问题

跨域是一个非常常见的需求，Spring框架中对跨域有好几种方案，引入Spring Security后跨域方案又增加了

##### CORS

Cross-Origin Resource Sharing，是W3C制定的一种跨域资源共享技术标准，其目的就是为了解决前端的跨域请求

Options方法请求：预检请求，目的是查看服务端是否支持即将发起的跨域请求，如果服务端允许，才能发起实际的Http请求。在预检请求的返回中，服务端也可以通知客户端是否需要携带身份认证等等

若服务端支持跨域请求则返回响应头中将包含：Access-Control-Allow-Origin：http://xxxx



##### Spring处理跨域方案

1. @CrossOrigin
   - 可以标注在方法上、Controller上
2. addCorsMappings
   - @CrossOrigin需要添加在不同的Controller上，所以需要一种全局的配置方法，就是重写WebMvcConfigurer的addCorsMappings方法来实现全局配置
   - 这两种都是在CrosInterceptor拦截器中实现的
3. CorsFilter
   - 是Spring Web提供的一个处理跨域的过滤器，可以通过这个过滤器处理跨域
4. 选一种来处理跨域即可



##### Spring Security处理跨域方案

项目使用SpringSecurity后Spring提供的第1、2两种方案处理跨域方式会失效，第3种方案会不会失效则要看过滤器的优先级，若过滤器优先级高于SpringSecurity过滤器则有效，否则会失效

![image-20220929232805913](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202209292328029.png)

Filter、DispatcherServlet、Interceptor执行顺序

**专业解决方案**

SpringSecurity跨域问题的处理：根据开发者提供的CorsConfigurationSource对象构建出CorsFilter，并将CorsFilter放置于认证过滤器之前，所以能处理跨域问题

核心思路：将处理跨域的过滤器放在Spring Securtity认证过滤器之前即可



---

### Spring Security异常处理

Security种主要分为认证异常处理、权限异常处理，除此之外的异常则抛出，交由Spring去处理

AuthenticationException：认证异常

AccessDeniedException：权限异常



Spring Security异常处理主要由ExceptionTranslationFilter过滤器中完成





---

### 权限管理

认证和授权是Spring Security的两大核心功能，授权就是我们日常说的权限管理

两大核心功能间有很好的解耦和关系，无论使用哪种认证方式，都不影响权限管理功能的使用

Spring Security中对于RBAC、ACL等不同权限模型都有很好的支持



##### 什么是权限管理

认证就是确认用户身份，也就是我们常说的登录

授权则是**根据系统提前设置好的规则，给用户分配可以访问某一资源的权限**，用户根据自己所具有的权限，去执行相应的操作

一个优秀的认证+授权系统可以为我们的应用系统提供强有力的安全保障功能



##### Spring Security 权限管理策略

主要有两种类型：

1. 基于过滤器的权限管理

   - 主要用来拦截Http请求，拦截下来后，根据Http请求地址进行权限校验

2. 基于AOP的权限管理

   - 主要用来处理方法级别的权限问题
   - 当调用某一个方法时候，通过Aop将操作拦截下来，然后判断用户是否具备相关的权限，具备则允许方法调用

   

##### 核心概念

###### 角色与权限

Security中，用户登录成功后，用户信息保存在Authentication对象中，该对象有一个getAuthorities方法，用来返回当前对象所具备的权限信息，也即：已经授予当前登录用户的权限

无论用户的认证方式采用何种方式例如：用户/密码、RememberMe、还是其他的CAS、OAuth2等认证方式，最终用户的权限信息都可以通过getAuthorities获取

设计层面讲，角色和权限是完全不同的东西：权限就是一些具体的操作，例如针对员工数据的读权限、针对员工数据的写权限；角色是某些权限的集合，例如管理员角色、普通用户角色

例如：某个用户是管理员角色，管理员角色拥有的权限集合肯定比普通用户的权限集合更丰富

代码层次讲，角色和权限并没有太大的不同，特别是Spring Security中，角色和权限的处理方式基本是一样的，唯一的区别是会自动给角色添加一个ROLE_前缀，而权限则不会自动添加任何前缀



权限系统设计简单：用户=>权限=>资源

权限系统设计复杂写：用户=>角色=>权限=>资源的系统中（用户关联角色、角色关联权限、权限关联资源）



###### 角色继承

角色继承：指的是角色存在一个上下级的关系，例如：role_amdin继承自role_user，那么role_admin就自动具备role_user的所有权限



---

##### 基于Url地址的权限关联

基于Url地址的权限关联主要是通过过滤器FilterSecurityInterceptor来实现

若开发者配置了基于Url地址的权限管理，那么该过滤器会被自动添加到SpringSecurity过滤器链中，在过滤器链中拦截下请求，然后分析当前用户是否具备请求所需要的权限

Filter将请求拦截下来后，交给AccessDecisionManager进行处理，AccessDecisionManager则会调用投票器进行投票，然后对投票结果进行决策，最终决定请求是否通过





权限管理系统复杂：复杂的是设计！！，技术相对来说更加容易



###### 动态管理权限规则

通过代码来配置Url拦截规则和请求url所需要的权限 比较死板、不灵活，若要调整某个url所需要的权限还得修改代码

所以，动态管理权限规则可以解决这样问题：将Url拦截规则和访问Url所需要的权限都保存在数据库中，这样，在不改变代码的情况下，只需要修改数据库中的数据，就可以对权限进行调整

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





##### 基于方法的权限管理

主要通过AOP实现，在Spring Security中通过MethodSecurityInterceptor来提供相关实现

不同于FilterSecurityInterceptor只是在请求之前进行前置处理，MethodSecurityInterceptor除了前置处理外，还可以进行后置处理，即：前置处理在请求之前判断是否具备相应的权限，后置处理则是在对方法执行完后，进行二次过滤



实现方式：主要通过@EnableGlobalMethodSecurity注解开启权限注解的使用



---

##### 权限模型

权限管理是一个非常复杂繁琐的工作，为了能开发出高效的易于维护的权限管理系统，需要一个“指导思想”，这是指导思想就是**权限模型**



##### DAC

Discretionary access control：自主访问控制

是一种访问控制模型，这种访问控制模型中，系统会根据被操作对象的权限控制列表中的信息，来决定当前用户能够对其进行哪些操作，用户可以将其具备的权限直接或者间接授予其他用户，这也是其称为自主访问控制的原因

##### MAC

Mandatory access control：强制访问控制，也叫非自主访问控制



##### ABAC

Attribute-Base Access Control：基于属性的访问控制，有时也称为PBAC或CBAC

基于属性的访问控制中一般包含四类属性：用户属性、环境属性、操作属性、资源属性，通过动态计算一个或一组属性是否满足某一条件来进行授权



在企业级开发中，目前最为流行的权限管理模型当属**RBAC**，除了RBAC外还有一个ACL权限模型，Spring Security中对ACL也提供了相关的依赖



##### ACL

Access control list：访问控制列表，是一种比较古老的权限控制模型，它是一种面向资源的访问控制模型，在ACL中，我们所做的所以权限配置都是针对资源的

ACL核心思路：将某个对象的某种权限授予某个用户或角色，它们之间的关系是多对多，即一个用户/角色可以具备某个对象的多种权限，某个对象的权限也可以被多个用户/角色持有

ACL是一种粒度非常细的权限控制，它可以精确到某一个资源的某一个权限，细到每一个资源的CRUD，这些权限数据都记录在数据库中，这带来的另一个问题就是需要维护的权限数据量非常大，特别是对于一些大型系统而言，大量的数据需要维护，可能会造成系统性能下降。不过对于一些简单的小型系统使用ACL还是可以的没有任何问题



##### RBAC

Role-Based access control：基于角色的访问控制，是一种以角色为基础的访问控制，它是一种较新且广为使用的权限控制模型，这种机制部署给用户赋予权限，而是将权限赋予角色

RBAC权限模型中，将用户按照角色进行归类，通过用户的角色来确定用户对某项资源是否具备操作权限

RBAC简化了用户与权限的管理，它将用户与角色管理、角色与权限管理、权限与资源管理，这种模式使得用户的授权管理变得非常简单和易于维护

RBAC权限模型三原则：

1. 最小权限：给角色配置的权限，是其能完成任务所需的最小权限集合
2. 职责分离：通过相互独立互斥的角色来共同完成任务
3. 数据抽象：通过权限的抽象来体现，RBAC支持的数据抽象程度与RBAC的实现细节有关

RBAC的应用非常广泛：常规的企业级应用、医疗、国防等等领域



##### RBAC权限模型分类

有四种不同的分类

RBAC0

是最简单的用户、角色、权限模型，也是RBAC权限模型中最为核心的一部分，其他几种模型都是在此基础上建立的，该模型中，一个用户可以具备多个角色，一个角色可以具备多个权限，最终用户所具备的权限是用户所具备的角色的权限的并集

RBAC0 权限模型图：

![image-20221001230511120](https://pic-typora-qc.oss-cn-chengdu.aliyuncs.com/springsecurity_img/202210012305760.png)



RBAC1

在RBAC0模型上引入了角色继承，让角色有了上下级关系

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



若这四种模型依然不能满足项目的要求，则开发者可以在此基础上进行扩展，例如：用户分组、角色分组等