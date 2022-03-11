## Docker学习与总结

### Docker是什么

1. **Docker** 使用 `Google` 公司推出的 [Go 语言](https://golang.google.cn) 进行开发实现，基于 `Linux` 内核的 [cgroup](https://zh.wikipedia.org/wiki/Cgroups)，[namespace](https://en.wikipedia.org/wiki/Linux_namespaces)，以及 [OverlayFS](https://docs.docker.com/storage/storagedriver/overlayfs-driver/) 类的 [Union FS](https://en.wikipedia.org/wiki/Union_mount) 等技术，对进程进行封装隔离，属于 [操作系统层面的虚拟化技术](https://en.wikipedia.org/wiki/Operating-system-level_virtualization)。由于隔离的进程独立于宿主和其它的隔离的进程，因此也称其为容器。
2. **Docker** 在容器的基础上，进行了进一步的封装，从文件系统、网络互联到进程隔离等等，极大的简化了容器的创建和维护。使得 `Docker` 技术比虚拟机技术更为轻便、快捷。
3. **Docker** 和传统虚拟化方式的不同之处。传统虚拟机技术是虚拟出一套硬件后，在其上运行一个完整操作系统，在该系统上再运行所需应用进程；而容器内的应用进程直接运行于宿主的内核，容器内没有自己的内核，而且也没有进行硬件虚拟。因此容器要比传统虚拟机更为轻便

### 基本概念

#### 镜像

1. 操作系统分为 **内核** 和 **用户空间**。对于 `Linux` 而言，内核启动后，会挂载 `root`文件系统为其提供用户空间支持
2. **Docker 镜像**（Image），就相当于是一个 `root` 文件系统。比如官方镜像 `ubuntu:18.04` 就包含了完整的一套 Ubuntu 18.04 最小系统的 `root` 文件系统
3. **Docker 镜像** 是一个特殊的文件系统，除了提供容器运行时所需的程序、库、资源、配置等文件外，还包含了一些为运行时准备的一些配置参数（如匿名卷、环境变量、用户等）。镜像 **不包含** 任何动态数据，其内容在构建之后也不会被改变
4. 分层存储
   - 因为镜像包含操作系统完整的 `root` 文件系统，其体积往往是庞大的，因此在 Docker 设计时，就充分利用 [Union FS](https://en.wikipedia.org/wiki/Union_mount) 的技术，将其设计为分层存储的架构。所以严格来说，镜像并非是像一个 `ISO` 那样的打包文件，镜像只是一个虚拟的概念，其实际体现并非由一个文件组成，而是**由一组文件系统组成，或者说，由多层文件系统联合组成**。
   - 镜像构建时，会一层层构建，前一层是后一层的基础。每一层构建完就不会再发生改变，后一层上的任何改变只发生在自己这一层。比如，删除前一层文件的操作，实际不是真的删除前一层的文件，而是仅在当前层标记为该文件已删除。在最终容器运行的时候，虽然不会看到这个文件，但是实际上该文件会一直跟随镜像。因此，在构建镜像的时候，需要额外小心，每一层尽量只包含该层需要添加的东西，任何额外的东西应该在该层构建结束前清理掉。
   - 分层存储的特征还使得镜像的复用、定制变的更为容易。甚至可以用之前构建好的镜像作为基础层，然后进一步添加新的层，以定制自己所需的内容，构建新的镜像。

#### 容器

1. 镜像和容器的关系
   - 镜像（`Image`）和容器（`Container`）的关系，就像是面向对象程序设计中的 `类` 和 `实例` 一样，镜像是静态的定义，容器是镜像运行时的实体。容器可以被创建、启动、停止、删除、暂停等
2. 容器的实质是进程，但与直接在宿主执行的进程不同，容器进程运行于属于自己的独立的 [命名空间](https://en.wikipedia.org/wiki/Linux_namespaces)。因此容器可以拥有自己的 `root` 文件系统、自己的网络配置、自己的进程空间，甚至自己的用户 ID 空间。容器内的进程是运行在一个隔离的环境里，使用起来，就好像是在一个独立于宿主的系统下操作一样。这种特性使得容器封装的应用比直接在宿主运行更加安全。也因为这种隔离的特性，很多人初学 Docker 时常常会混淆容器和虚拟机
3. 每一个容器运行时，是以镜像为基础层，在其上创建一个当前容器的存储层，我们可以称这个为容器运行时读写而准备的存储层为 **容器存储层**
4. 容器存储层的生存周期和容器一样，容器消亡时，容器存储层也随之消亡。因此，任何保存于容器存储层的信息都会随容器删除而丢失
5. 容器不应该向其存储层内写入任何数据，容器存储层要保持无状态化。所有的文件写入操作，都应该使用 [数据卷（Volume）]()、或者 [绑定宿主目录]()，在这些位置的读写会跳过容器存储层，直接对宿主（或网络存储）发生读写，其性能和稳定性更高。
6. 数据卷的生存周期独立于容器，容器消亡，数据卷不会消亡。因此，使用数据卷后，容器删除或者重新运行之后，数据却不会丢失

#### 仓库

1. 镜像构建完成后，可以很容易的在当前宿主机上运行，但是，如果需要在其它服务器上使用这个镜像，我们就需要一个集中的存储、分发镜像的服务，[Docker Registry]() 就是这样的服务
2. 一个 **Docker Registry** 中可以包含多个 **仓库**（`Repository`）；每个仓库可以包含多个 **标签**（`Tag`）；每个标签对应一个镜像；通常，一个仓库会包含同一个软件不同版本的镜像，而标签就常用于对应该软件的各个版本。我们可以通过 `<仓库名>:<标签>` 的格式来指定具体是这个软件哪个版本的镜像。如果不给出标签，将以 `latest` 作为默认标签
3. 例子：以 [Ubuntu 镜像](https://hub.docker.com/_/ubuntu) 为例，`ubuntu` 是仓库的名字，其内包含有不同的版本标签，如，`16.04`, `18.04`。我们可以通过 `ubuntu:16.04`，或者 `ubuntu:18.04` 来具体指定所需哪个版本的镜像。如果忽略了标签，比如 `ubuntu`，那将视为 `ubuntu:latest`
4. 仓库名经常以 **两段式路径**形式出现，比如 `jwilder/nginx-proxy`，前者往往意味着 Docker Registry 多用户环境下的用户名，后者则往往是对应的软件名。但这并非绝对，取决于所使用的具体 Docker Registry 的软件或服务

##### Docker Registry 公开服务

1. Docker Registry 公开服务是开放给用户使用、允许用户管理镜像的 Registry 服务。一般这类公开服务允许用户免费上传、下载公开的镜像，并可能提供收费服务供用户管理私有镜像
2. 最常使用的 Registry 公开服务是官方的 [Docker Hub](https://hub.docker.com)，这也是默认的 Registry，并拥有大量的高质量的 [官方镜像](https://hub.docker.com/search?q=&type=image&image_filter=official)。除此以外，还有 Red Hat 的 [Quay.io](https://quay.io/repository/)；Google 的 [Google Container Registry](https://cloud.google.com/container-registry/)，[Kubernetes](https://kubernetes.io) 的镜像使用的就是这个服务；代码托管平台 [GitHub](https://github.com) 推出的 [ghcr.io](https://docs.github.com/cn/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
3. 在国内访问这些服务可能会比较慢。国内的一些云服务商提供了针对 Docker Hub 的镜像服务（`Registry Mirror`），这些镜像服务被称为 **加速器**。常见的有 [阿里云加速器](https://www.aliyun.com/product/acr?source=5176.11533457&userCode=8lx5zmtu)、[DaoCloud 加速器](https://www.daocloud.io/mirror#accelerator-doc) 等。使用加速器会直接从国内的地址下载 Docker Hub 的镜像，比直接从 Docker Hub 下载速度会提高很多
4. 国内也有一些云服务商提供类似于 Docker Hub 的公开服务。比如 [网易云镜像服务](https://c.163.com/hub#/m/library/)、[DaoCloud 镜像市场](https://hub.daocloud.io)、[阿里云镜像库](https://www.aliyun.com/product/acr?source=5176.11533457&userCode=8lx5zmtu) 等

##### 私有Docker Registry

1. 除了使用公开服务外，用户还可以在本地搭建私有 Docker Registry。Docker 官方提供了 [Docker Registry](https://hub.docker.com/_/registry/) 镜像，可以直接使用做为私有 Registry 服务

### 安装Docker

1. 参照我的下一篇单独提取出来的文章
2. https://blog.csdn.net/qq_41884536/article/details/122645209?spm=1001.2014.3001.5501

### 使用镜像

Docker运行容器前需要本地存在对应的容器，如果本地不存在该镜像，Docker会从镜像仓库下载该镜像

#### 获取镜像

1. 从Docker镜像仓库获取镜像的命令：docker pull

   ~~~shell
   $ docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]
   ~~~

2. docker pull --help查看详细格式

   ~~~shell
   $ docker pull ubuntu:18.04
   18.04: Pulling from library/ubuntu
   92dc2a97ff99: Pull complete
   be13a9d27eb8: Pull complete
   c8299583700a: Pull complete
   Digest: sha256:4bc3ae6596938cb0d9e5ac51a1152ec9dcac2a1c50829c74abd9c4361e321b26
   Status: Downloaded newer image for ubuntu:18.04
   docker.io/library/ubuntu:18.04
   ~~~

3. 上述镜像完整名为：docker.io/library/ubuntu:18.04

##### 运行

有了镜像之后，可以以这个镜像为基础启动并运行一个容器

1. ~~~shell
   $ docker run -it --rm ubuntu:18.04 bash
   
   root@e7009c6ce357:/# cat /etc/os-release
   NAME="Ubuntu"
   VERSION="18.04.1 LTS (Bionic Beaver)"
   ID=ubuntu
   ID_LIKE=debian
   PRETTY_NAME="Ubuntu 18.04.1 LTS"
   VERSION_ID="18.04"
   HOME_URL="https://www.ubuntu.com/"
   SUPPORT_URL="https://help.ubuntu.com/"
   BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
   PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
   VERSION_CODENAME=bionic
   UBUNTU_CODENAME=bionic
   ~~~

2. docker run：运行容器的命令

   - -it：-i是交互式操作，-t是终端
   - --rm：容器退出之后随之将其删除。默认情况下退出容器并不会自动删除，除非手动docker rm
   - ubuntu:18.04 ：指用这个镜像为基础来启动容器
   - bash：放在镜像名后面的命令，这里希望有一个交互式shell，所以是bash
   - --------以下为shell命令
   - cat /etc/os-release ：查看当前系统版本的命令

#### 列出镜像

docker image ls

~~~shell
$ docker image ls
REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
redis                latest              5f515359c7f8        5 days ago          183 MB
nginx                latest              05a60462f8ba        5 days ago          181 MB
mongo                3.2                 fe9198c04d62        5 days ago          342 MB
<none>               <none>              00285df0df87        5 days ago          342 MB
ubuntu               18.04               329ed837d508        3 days ago          63.3MB
ubuntu               bionic              329ed837d508        3 days ago          63.3MB
~~~



1. 列表包含了 `仓库名`、`标签`、`镜像 ID`、`创建时间` 以及 `所占用的空间`。

   **镜像 ID** 则是镜像的唯一标识，一个镜像可以对应多个 **标签**。因此，在上面的例子中，我们可以看到 `ubuntu:18.04` 和 `ubuntu:bionic` 拥有相同的 ID，因为它们对应的是同一个镜像

2. docker system df：查看镜像、容器、数据卷所占用的空间

##### 虚悬镜像

1. 在上面的仓库中，可以看到镜像既没有仓库没标签，均为<none>

2. 这是由于这个镜像与新的镜像名同名，就镜像名被取消，这一类称为虚悬镜像

3. ~~~shell
   $ docker image ls -f dangling=true
   REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
   <none>              <none>              00285df0df87        5 days ago          342 MB
   ~~~

4. 这一类镜像是可以随意删除的 docker image prune

##### 中间层镜像

1. docker image ls：只会列出顶层镜像
2. docker image ls -a：可以查看包括中间层镜像在内的所有镜像
3. 删除那些依赖它们的镜像后，这些中间层依赖镜像也会被连带删除

##### 列出部分镜像

1. docker image ls ubuntu：根据仓库名列出镜像
2. docker image ls ubuntu:18.04 ：列出特定的某个镜像

#### 删除本地镜像

使用docker image rm 命令删除

~~~shell
$ docker image rm [选项] <镜像1> [<镜像2> ...]
~~~

以下关于Dockerfile的介绍参见[文章链接](https://blog.csdn.net/qq_41884536/article/details/122709159)

#### 利用commit理解镜像构成

不建议采用这种方式构建镜像

#### 使用Dockerfile定制镜像

镜像的定制，实际上就是定制每一层所添加的配置、文件。

如果把每一层修改、安装、构建、操作的命令写入一个脚本文件，利用脚本来构建、定制镜像，那么镜像构建透明性、体积性的问题都会解决。这个脚本就是Dockerfile。

Dockerfile文件包含一条条的指令，每一条指令构建一层，因此每一条指令的内容，就是描述该层应当如何构建。

~~~shell
--例如：
$ mkdir mynginx
$ cd mynginx 
$ vim Dockerfile
~~~

~~~shell
FROM nginx
RUN echo 'hello,Docker!'
~~~

##### FORM指定基础镜像

定制镜像，就是以一个镜像为基础，在其上进行定制。Dockerfile中，FROM 必须是第一条指令

##### RUN执行命令

Run指令是用来执行命令行的命令。

shell格式：RUN<命令>

exec格式：RUN["可执行文件"，"参数一"，"参数二"]

##### 构建镜像

docker build 命令进行镜像构建

格式：docker build [选项] <上下文路径/URL/->

例如：docker build -t  nginx:v3  .     

  -t 给镜像打一个Tag

 这里的 '.' 表示上下文路径

（当前路径下Dockerfile文件所在的位置 ，这个理解不准确） 

~~~shell
$ docker build -t nginx:v3 .
Sending build context to Docker daemon 2.048 kB
Step 1 : FROM nginx
 ---> e43d811ce2f4
Step 2 : RUN echo '<h1>Hello, Docker!</h1>' > /usr/share/nginx/html/index.html
 ---> Running in 9cdc27646c7b
 ---> 44aa4490ce2c
Removing intermediate container 9cdc27646c7b
Successfully built 44aa4490ce2c
~~~

##### 镜像构建上下文

首先我们要理解 `docker build` 的工作原理。Docker 在运行时分为 Docker 引擎（也就是服务端守护进程）和客户端工具。Docker 的引擎提供了一组 REST API，被称为 [Docker Remote API](https://docs.docker.com/develop/sdk/)，而如 `docker` 命令这样的客户端工具，则是通过这组 API 与 Docker 引擎交互，从而完成各种功能。因此，虽然表面上我们好像是在本机执行各种 `docker` 功能，但实际上，一切都是使用的远程调用形式在服务端（Docker 引擎）完成。

并非所有定制都会通过 `RUN` 指令完成，经常会需要将一些本地文件复制进镜像，比如通过 `COPY` 指令、`ADD` 指令等。而 `docker build` 命令构建镜像，其实并非在本地构建，而是在服务端，也就是 Docker 引擎中构建的。那么在这种客户端/服务端的架构中，如何才能让服务端获得本地文件呢？

这就引入了上下文的概念。当构建的时候，用户会指定构建镜像上下文的路径，`docker build` 命令得知这个路径后，会将路径下的所有内容打包，然后上传给 Docker 引擎。这样 Docker 引擎收到这个上下文包后，展开就会获得构建镜像所需的一切文件

~~~shell
Sending build context to Docker daemon 2.048 kB
#这就是在上传上下文至Docker引擎
~~~

docker build 还支持从url等方式构建

##### git repo进行构建

这行命令指定了构建所需的 Git repo，并且指定分支为 `master`，构建目录为 `/amd64/hello-world/`，然后 Docker 就会自己去 `git clone` 这个项目、切换到指定分支、并进入到指定目录后开始构建。

~~~shell
# $env:DOCKER_BUILDKIT=0
# export DOCKER_BUILDKIT=0

$ docker build -t hello-world https://github.com/docker-library/hello-world.git#master:amd64/hello-world

Step 1/3 : FROM scratch
 --->
Step 2/3 : COPY hello /
 ---> ac779757d46e
Step 3/3 : CMD ["/hello"]
 ---> Running in d2a513a760ed
Removing intermediate container d2a513a760ed
 ---> 038ad4142d2b
Successfully built 038ad4142d2b
~~~

##### 给定tar包构建

如果所给出的 URL 不是个 Git repo，而是个 `tar` 压缩包，那么 Docker 引擎会下载这个包，并自动解压缩，以其作为上下文，开始构建。

~~~shell
$ docker build http://server/context.tar.gz
~~~

#### Dockerfile指令详解

##### COPY复制文件

格式：

COPY [--chown=<user>:<group>] <源路径>....<目标路径>

COPY[--chown=<user>:<group>] ["<源路径>",..."<目标路径>"]

~~~shell
COPY package.json /usr/src/app/
#COPY指令将从上下文目录中<源路径>的文件或目录复制到新的一层镜像内的<目标路径>位置

#源路径可以包含多个，甚至可以是通配符
COPY hom* /mydir/
COPY hom?.txt /mydir/
~~~

<目标路径>可以是容器内的绝对路径，也可以是相对于工作目录的相对路径(工作目录可以通过WORKDIR指令指定)，目标路径不需要事先创建，如果目录不存在会在复制文件前先行创建缺失的目录

##### ADD更高级的复制文件

ADD指令和COPY的格式和性质基本一致，但是在copy基础上增加了一些功能。

尽可能的使用COPY，使用ADD的场合为自动解压等

##### CMD容器启动命令

CMD指令的格式和RUN相似

1. shell格式：cmd <命令>
2. exec格式：cmd ["可执行文件"，"参数1"，"参数2".。]
3. 参数列表格式：cmd["参数1"，"参数2"...]。在指定了ENTRYPOINT指令后，用cmd指定具体的参数

CMD指令就是用于指定默认的容器主进程的启动命令

##### ENTRYPOINT入口点

目的和CMD一样，都是在指定容器启动程序及参数，指定了entrypoint后，cmd的含义就发生了改变，不再是直接的运行其命令，而是将CMD的内容作为参数传给entrypoint指令。

##### ENV设置环境变量

格式：

1. ENV<key> <value>     中间有一个空格
2. ENV<key1>=<value1>  <key2>=<value2>

##### ARG构建参数

##### VOLUME定义匿名卷

##### EXPOSE暴露端口

EXPOSE<端口1> [<端口2>...]

expose指令是声明容器运行时提供服务的端口，这只是一个声明，在容器运行时并不会因为这个声明应用就会开启这个端口的服务。Expose仅仅是声明打算使用什么端口，并不会自动在宿主进行端口映射。

docker run -p 这里会自动映射Expose的端口

docker run -p <宿主端口>:<容器端口>  这个是映射宿主端口和容器端口，这里指定了端口，则不会映射Expose的端口

##### WORKDIR指定工作目录

WORKDIR <工作目录路径>



##### USER指定当前用户

##### HEALTHCHECK健康检查

##### ONBUILD

##### LABEL为镜像添加元数据

##### SHELL指令

#### Dockerfile多阶段构建

#### 构建多种系统架构支持的Docker镜像

#### 实现原理

---

### 操作容器

容器是独立运行的一个或一组应用，以及他们的运行态环境。

虚拟机可以理解为模拟运行的一整套操作系统和跑在上面应用。

#### 启动

1. 基于镜像新建一个容器并启动
2. 将在终止状态的容器重新启动

##### 新建并启动

主要命令为docker run

1. ~~~shell
   $ docker run ubuntu:18.04 /bin/echo 'Hello world'
   Hello world
   ~~~

2. 以上命令启动之后，终止容器

3. ~~~shell
   $ docker run -t -i ubuntu:18.04 /bin/bash
   root@af8bae53bdd3:/#
   ~~~

4. 以上命令启动容器后，会启动一个bash终端，允许用户进行交互。-t参数让Docker分配一个伪终端，并绑定到容器的标准输入上，-i参数让容器的标准输入保持打开，但是退出终端后，容器随着终止。

5. 利用docker run来创建容器时，Docker在后台允许的操作包括：

   - 检查本地是否存在指定的镜像，没有就从registry下载
   - 利用镜像创建并启动一个容器
   - 分配一个文件系统，并在只读的镜像层外面挂载一层可读可写
   - 从宿主主机配置的网桥接口中桥接一个虚拟接口到容器中
   - 从地址池配置一个ip地址给容器
   - 执行用户指定的应用程序
   - 执行完毕后容器被终止 

##### 启动已终止容器

1. docker container start 将一个已经终止(exited)的容器启动允许

#### 守护态运行

大多数情况都需要Docker在后台运行，而不是直接把执行命令的结果输出在当前宿主主机下，可以通过-d参数来使得容器在后台运行

1. ~~~shell
   $ docker run ubuntu:18.04 /bin/sh -c "while true; do echo hello world; sleep 1; done"
   hello world
   hello world
   hello world
   hello world
   ~~~

2. ~~~shell
   $ docker run -d ubuntu:18.04 /bin/sh -c "while true; do echo hello world; sleep 1; done"
   77b2dc01fe0f3f1265df143181e7b9af5e05279a884f4776ee75350ea9d8017a
   ~~~

3. 可见带有-d参数的容器并不会把结果输出到当前宿主主机上面

#### 终止

docker container stop终止一个运行中的容器

docker container restart 命令会将一个运行态的容器终止，然后再重新启动它

#### 进入容器

启动容器时，加入了-d参数，容器会进入后台，当需要进入容器进行操作时，可以采用docker attach 或者 docker exec 命令

1. 采用docker attach进入容器，当退出标准输入输出后，会导致该容器终止

2. ~~~shell
   $ docker run -dit ubuntu
   243c32535da7d142fb0e6df616a3c3ada0b8ab417937c853a9e1c251f499f550
   
   $ docker container ls
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   243c32535da7        ubuntu:latest       "/bin/bash"         18 seconds ago      Up 17 seconds                           nostalgic_hypatia
   
   $ docker attach 243c
   root@243c32535da7:/#
   ~~~

3. 采用exec命令进入容器，从这个标准输入输出exit后，不会终止容器的停止

4. ~~~shell
   $ docker run -dit ubuntu
   69d137adef7a8a689cbcb059e94da5489d3cddd240ff675c640c8d96e84fe1f6
   
   $ docker container ls
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   69d137adef7a        ubuntu:latest       "/bin/bash"         18 seconds ago      Up 17 seconds                           zealous_swirles
   
   $ docker exec -i 69d1 bash
   ls
   bin
   boot
   dev
   ...
   
   $ docker exec -it 69d1 bash
   root@69d137adef7a:/#
   ~~~

#### 导出和导入

1. 导出容器，docker export <容器id>

2. ~~~shell
   $ docker container ls -a
   CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
   7691a814370e  ubuntu:18.04  "/bin/bash" 36 hours ago Exited (0)  21 hours ago                       test
   $ docker export 7691a814370e > ubuntu.tar
   
   ~~~

3. 导入容器快照 docker import  从容器快照文件中再导入为镜像

4. ~~~shell
   $ cat ubuntu.tar | docker import - test/ubuntu:v1.0
   $ docker image ls
   REPOSITORY TAG  IMAGE ID CREATED  VIRTUAL SIZE
   test/ubuntu  v1.0    9d37a6082e97   About a minute ago   171.3 MB
   ~~~

5. 也可以通过指定 URL 或者某个目录来导入

   ~~~shell
   $ docker import http://example.com/exampleimage.tgz example/imagerepo
   ~~~

#### 删除

docker container rm 来删除一个处于终止状态的容器

docker container rm -f参数   可以删除一个处于运行中的容器 ，Doker会发送sigkill信号给容器

清理所有处于终止状态的容器

docker container prune 



------------------------------------以上对应CSDN博客上第一章节-----------------



### 访问仓库

Repository是集中存放镜像的地方

注册服务器Registry，实际上Registry是管理仓库的具体服务器，每个服务器可以有多个仓库，而每个仓库下面有多个镜像。

仓库可以被认为是一个具体的项目或目录，例如对于docker.io/ubuntu来说，docker.io是注册服务器地址，ubuntu是仓库名，但是大部分时候并不严格区分

#### Docker Hub

Docker官方维护的一个公共仓库，里面包含了超过2650000个镜像，大部分需求都可以通过Docker Hub直接下载镜像来实现

##### 拉取镜像

1. 可以通过docker search 命令来查找官方仓库中的镜像
2. 通过docker pull命令来将镜像下载到本地

##### 推送镜像

1. 用户可以登录Docker Hub后通过docker push命令将自己的镜像推送到doucker hub

2. 例如：qqcc/ubuntu:18.04这个镜像，表示自己的镜像

3. ~~~shell
   $ docker tag ubuntu:18.04 qqcc/ubuntu:18.04
   
   $ docker image ls
   
   REPOSITORY  TAG  IMAGE ID  CREATED  SIZE
   ubuntu     18.04  275d79972a86  6 days ago 94.6MB
   qqcc/ubuntu 18.04 275d79972a86  6 days ago 94.6MB
   
   $ docker push qqcc/ubuntu:18.04
   
   $ docker search qqcc/ubuntu:18.04
   
   NAME DESCRIPTION  STARS OFFICIAL AUTOMATED
   username/ubuntu
   ~~~

##### 自动构建

此功能收费。。。。

#### 私有仓库

通过docker-registry工具，可以构建私有的镜像仓库

##### 安装运行docker-registry

使用官方registry镜像来启动私有仓库，默认情况下，仓库被创建在容器的/var/lib/registry目录下

~~~shell
$ docker run -d -p 5000:5000 --restart=always --name registry registry
~~~

可以通过-v参数来指定将镜像文件存放在容器本地的指定路径

~~~shell
$ docker run -d \
    -p 5000:5000 \
    -v /opt/data/registry:/var/lib/registry \
    registry
~~~

##### 在私有仓库上次、搜索、下载镜像

创建好私有仓库之后，就可以使用 `docker tag` 来标记一个镜像，然后推送它到仓库。例如私有仓库地址为 `127.0.0.1:5000`

~~~shell
$ docker image ls
REPOSITORY                        TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
ubuntu                            latest              ba5877dc9bec        6 weeks ago         192.7 MB
~~~

使用 docker tag  将 `ubuntu:latest` 这个镜像标记为 127.0.0.1:5000/ubuntu:latest

格式为 `docker tag IMAGE[:TAG] [REGISTRY_HOST[:REGISTRY_PORT]/]REPOSITORY[:TAG]`

~~~shell
$ docker tag ubuntu:latest 127.0.0.1:5000/ubuntu:latest
$ docker image ls
REPOSITORY  TAG IMAGE ID  CREATED   VIRTUAL SIZE
ubuntu  latest  ba5877dc9bec 6 weeks ago  192.7 MB
127.0.0.1:5000/ubuntu:latest  latest          ba5877dc9bec        6 weeks ago         192.7 MB
~~~

docker pushh上次标记的镜像

~~~shell
$ docker push 127.0.0.1:5000/ubuntu:latest
The push refers to repository [127.0.0.1:5000/ubuntu]
373a30c24545: Pushed
a9148f5200b0: Pushed
cdd3de0940ab: Pushed
fc56279bbb33: Pushed
b38367233d37: Pushed
2aebd096e0e2: Pushed
latest: digest: sha256:fe4277621f10b5026266932ddf760f5a756d2facd505a94d2da12f4f52f71f5a size: 1568
~~~

用 `curl` 查看仓库中的镜像

~~~shell
$ curl 127.0.0.1:5000/v2/_catalog
{"repositories":["ubuntu"]}
~~~

先删除本地已有镜像，再尝试从私有仓库中下载这个镜像

~~~shell
$ docker image rm 127.0.0.1:5000/ubuntu:latest

$ docker pull 127.0.0.1:5000/ubuntu:latest
Pulling repository 127.0.0.1:5000/ubuntu:latest
ba5877dc9bec: Download complete
511136ea3c5a: Download complete
9bad880da3d2: Download complete
25f11f5fb0cb: Download complete
ebc34468f71d: Download complete
2318d26665ef: Download complete

$ docker image ls
REPOSITORY                         TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
127.0.0.1:5000/ubuntu:latest       latest              ba5877dc9bec        6 weeks ago         192.7 MB
~~~

#### 私有仓库高级配置

暂未写

#### Nexus3

暂未写



### 数据管理

Docker内部以及容器之间管理数据，容器中管理数据主要有两种方式：数据卷（Volumes）、挂载主机目录（Bind mounts)

#### 数据卷

数据卷是一个可供一个或多个容器使用的特殊目录，主要特性：

数据卷可以在容器之间共享和重用

对数据卷的修改会立马生效

对数据卷的更新，不用影响镜像

数据卷默认会一直存在，即使容器被删除

提醒：数据卷的使用，类似与Linux下对目录或文件进行mount（挂载），镜像中被指定为挂载点的目录中的文件会复制到数据卷中（仅数据卷为空时会复制）

##### 创建数据卷

1. docker volume create my-volume
2. 查看所有的数据卷：docker volume ls
3. 查看指定数据卷的信息：docker volum inspect my-volume

##### 启动一个挂载数据卷的容器

使用--mount参数标记将数据卷挂载到容器里，在一次docker run中可以挂载多个数据卷。

以下为通过nginx:alpine镜像来创建一个名为web的容器，并加载一个数据卷到容器的/usr/share/nginx/html目录

~~~shell
$ docker run -d -P \
    --name web \
    # -v my-vol:/usr/share/nginx/html \
    --mount source=my-vol,target=/usr/share/nginx/html \
    nginx:alpine
~~~

查看数据卷的具体信息：

1. 查看web容器的信息：docker inspect web

2. 数据卷在Mounts下面

3. ~~~shell
   "Mounts": [
       {
           "Type": "volume",
           "Name": "my-volume",
           "Source": "/var/lib/docker/volumes/my-volume/_data",
           "Destination": "/usr/share/nginx/html",
           "Driver": "local",
           "Mode": "",
           "RW": true,
           "Propagation": ""
       }
   ],
   ~~~

##### 删除数据卷

docker volume rm my-volume

数据卷是设计来持久化数据的，它的生命周期独立于容器，Doker不会在容器被删除后自动删除数据卷，如果需要在删除容器时，同时删除数据卷，使用命令：docker rm -v my-volume

#### 挂载主机目录

##### 挂载一个主机目录作为数据卷

~~~shell
$ docker run -d -P \
    --name web \
    # -v /src/webapp:/usr/share/nginx/html \
    --mount type=bind,source=/src/webapp,target=/usr/share/nginx/html \
    nginx:alpine
~~~

上述命令加载主机/src/webapp目录到容器的/usr/share/nginx/html目录，再次查看数据卷具体信息：docker inspect web。此时挂载主机目录的配置信息如下：

~~~shell
"Mounts": [
    {
        "Type": "bind",
        "Source": "/src/webapp",
        "Destination": "/usr/share/nginx/html",
        "Mode": "",
        "RW": true,
        "Propagation": "rprivate"
    }
],
~~~

##### 挂载一个本地主机文件作为数据卷

`--mount` 标记也可以从主机挂载单个文件到容器中

~~~shell
$ docker run --rm -it \
   # -v $HOME/.bash_history:/root/.bash_history \
   --mount type=bind,source=$HOME/.bash_history,target=/root/.bash_history \
   ubuntu:18.04 \
   bash

root@2affd44b4667:/# history
1  ls
2  diskutil list
~~~





-------------第二部分完结------------第三部分-----------------------

----------第四部分为镜像dockerfile----------------------

---以下为第五部分----

### 使用网络

Docker允许通过外部访问容器或容器互联的方式来提供网络服务。

#### 外部访问容器

容器中可以运行一些网络应用，要让外部也可以访问这些应用，可以通过-p或者-P参数来指定端口映射

使用-P标记时，Docker会随机映射一个端口到内部容器开发的网络端口

~~~shell
$ docker run -d -P nginx:alpine

$ docker container ls -l
CONTAINER ID IMAGE COMMAND  CREATED  STATUS  PORTS NAMES
fae320d08268    nginx:alpine   "/docker-entrypoint.…"   24 seconds ago      Up 20 seconds       0.0.0.0:32768->80/tcp   bold_mcnulty


#可以看出32768映射到了容器的80端口，此时访问本机的32768端口即可访问容器内Nginx的默认界面
~~~

使用-p时，可以指定要映射的端口，并且在一个指定端口上只可以绑定一个容器

格式：ip:hostPort:containerPort 、ip::containerPort 、hostPort:containerPort

##### 映射所有接口地址

~~~shell
$ docker run -d -p 80:80 nginx:alpine

#hostPort:containerPort格式
#本地的80端口映射到容器的80端口，此时默认会绑定本地所有接口上的所有地址
~~~

##### 映射到指定地址的指定端口

~~~shell
$ docker run -d -p 127.0.0.1:80:80 nginx:alpine

#ip:hostPort:continerPort格式
#指定映射使用一个特定地址，例如localhost
~~~

##### 映射到指定地址的任意端口

~~~shell
$ docker run -d -p 127.0.0.1::80 nginx:alpine

#ip::containerPort
#绑定localhost的任意端口到容器的80端口


$ docker run -d -p 127.0.0.1:80:80/udp nginx:alpine
#使用udp标记来指定udp端口
~~~

#### 容器互联

建议将容器加入自定义的Docker网络来连接多个容器，而不是采用--link参数

##### 新建网络

~~~shell
$ docker network create -d bridge my-net

#-d参数指定Docker网络类型，有brige、overlay
#overlay使用swarm 模式 ，后续swarm集群介绍
~~~

##### 连接容器

~~~shell
$ docker run -it --rm --name busybox1 --network my-net busybox sh

#运行一个容器并连接到新建的my-net网络
~~~

~~~shell
$ docker run -it --rm --name busybox2 --network my-net busybox sh

#运行一个容器并连接到新建的my-net网络
~~~

~~~shell
$ docker container ls

CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
b47060aca56b        busybox             "sh"                11 minutes ago      Up 11 minutes                           busybox2
8720575823ec        busybox             "sh"                16 minutes ago      Up 16 minutes                           busybox1

#打开新的终端并查看容器信息


#busybox1容器中输入ping命令
/ # ping busybox2
PING busybox2 (172.19.0.3): 56 data bytes
64 bytes from 172.19.0.3: seq=0 ttl=64 time=0.072 ms
64 bytes from 172.19.0.3: seq=1 ttl=64 time=0.118 ms

#此时可以看出，busybox1和2以及建立了互联关系
~~~

##### Docker Compose

后续单独章节介绍

#### 配置DNS

Docker采用虚拟文件来挂载容器的3个相关配置文件

在容器中使用mount命令可以看到挂载信息：

~~~shell
$ mount
/dev/disk/by-uuid/1fec...ebdf on /etc/hostname type ext4 ...
/dev/disk/by-uuid/1fec...ebdf on /etc/hosts type ext4 ...
tmpfs on /etc/resolv.conf type tmpfs ...
~~~

这种机制可以让宿主主机DNS信息发生更新后，所有Docker容器的DNS配置通过/ect/resolv.conf文件立刻得到更新

配置全部容器的DNS可以在/etc/docker/daemon.json文件中设置

~~~shell
{
  "dns" : [
    "114.114.114.114",
    "8.8.8.8"
  ]
}
~~~

### 高级网络配置

后续深入理解后添加上

### Docker  Compose

Docker Compose是Docker官方编排项目之一，负责快速的部署分布式应用，实现对Docker容器集群的快速编排

Compose定位是：定义和运行对个Docker容器的应用

它允许用户通过一个单独的docker-compose.yml文件来定义一组相关联的应用容器作为一个项目

两个重要概念

1. 服务：一个应用的容器，实际上可以包含若干运行相同镜像的容器实例
2. 项目：由一个关联的应用容器组成的一个完整业务单元，在docker-compose.yml文件中定义

Compose默认管理的对象是项目，通过子命令对项目中的一组容器进行便捷的生命周期管理

#### 安装

##### 二进制包

从官方下载[Git hub Release](https://github.com/docker/compose/releases)编译好的二进制文件即可

~~~shell
$ sudo curl -L https://github.com/docker/compose/releases/download/1.27.4/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

# 国内用户可以使用以下方式加快下载
$ sudo curl -L https://download.fastgit.org/docker/compose/releases/download/1.27.4/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

$ sudo chmod +x /usr/local/bin/docker-compose
~~~

#### 卸载

~~~shell
$ sudo rm /usr/local/bin/docker-compose

#删除二进制文件即可
~~~

#### 使用

##### 术语

1. 服务：一个应用容器，实际上可以运行多个相同镜像的实例
2. 项目：由一组关联的应用容器组成的一个完整业务单元

一个项目可以由多个服务(容器)关联而成，Compose面向项目进行管理

#### 命令

docker-compose [command] --help

1. 基本格式
   docker-compose [-f=<arg>...]  [options]   [Command] [Args...]

2. 命令选项
   -f，--file File 指定使用的Compose模板文件，默认为docker-compose.yml。可以指定多次
   -p，--project-name Name  指定项目名称，默认将使用所在目录名称作为项目名
   --verbose输出更多调试信息
   -v,--version 打印版本并退出

3. 命令使用说明

   build：docker-compose build [options] [Service...]

   构建(重新构建)项目中的服务容器

   服务容器一旦构建后，会带上一个标记名

   可以随时在项目目录下运行docker-compose build 来重新构建服务

   选项包括：

   - --force-rm 删除构建过程中的临时容器
   - --no-cache 构建镜像过程中不使用cache
   - --pull 始终尝试通过pull来获取更新版本的镜像

   comfig：验证Compose文件格式是否正确

   down：停止up命令所启动的容器，并移除网络

   exec：进入指定的容器

   help：获取帮助

   images：列出Compose文件中包含的镜像

   kill：格式docker-compose kill [options] [Service...]

   - 通过发送SigKill信号来强制停止服务容器

   - 支持通过-s参数来指定发送的信号

   - ~~~shell
     $ docker-compose kill -s SIGINT
     ~~~

   logs：格式 docker-compose logs [options] [Service...]

   - 查看服务容器的输出

   pause：docker-compose [options] [Service...]

   - 暂停一个服务容器

   port：docker-compose port [options] Service Private_Port

   - 打印某个容器端口所映射的公共端口
   - --protocol=proto 指定端口协议，tcp(默认)或udp
   - --index=index 如果同一服务存在多个容器，指定命令对象容器的序号(默认为1)

   ps：docker-compose ps [options] [Service]

   - 列出项目中目前的所以容器
   - -q 只打印容器的ID信息

   push：推送服务依赖的镜像到Docker镜像仓库

   restart：docker-compose restart [options] [Service...]

   - 重启项目中的服务
   - -t /--timeout Timeout 指定重启前停止容器的超时(默认为10s)

   rm：docker-compose rm [options] [Service...]

   - 删除所有(停止状态的)服务容器，推荐使用docker-compose stop 命令来停止容器
   - -f，--force 强制直接删除，包括非停止状态的容器，尽量不要使用该选项
   - -v 删除容器所挂载的数据卷

   run：docker-compose run [options] [-p Port ...] [-e Key=Value ...] Service [Command] [Args...]

   - 在指定服务上执行一个命令

   - ~~~shell
     $ docker-compose run ubuntu ping docker.com
     #将会启动一个ubuntu服务容器，并执行ping命令
     
     #默认情况下，如果存在关联，则所有关联的服务将会自动被启动，除非这些服务已经在运行中，如果不希望自动启动关联的容器，可以使用--no-deps选项
     $ docker-compose run --no-deps ubuntu ping docker.com
     ~~~

   - 选项：

   - -d：后台运行容器

   - --name Name ：为容器指定一个名字

   - --entrypoint Cmd 覆盖默认的容器启动指令

   - `-e KEY=VAL` 设置环境变量值，可多次使用选项来设置多个环境变量。

   - -u, --user=""` 指定运行容器的用户名或者 uid。` 

   - --no-deps` 不自动启动关联的服务容器。` 

   - `--rm` 运行命令后自动删除容器，`d` 模式下将忽略。

   - -p, --publish=[] 映射容器端口到本地主机。

   - --service-ports 配置服务端口并映射到本地主机。

   - -T 不分配伪 tty，意味着依赖 tty 的指令将无法运行

   scale：docker-compose scale [options] [Service=num...]

   - 设置指定服务运行的容器个数

   - ~~~shell
     $ docker-compose scale web=3 db=2
     #将启动3个容器运行web服务，2个容器运行db服务
     ~~~

   start：docker-compose start [Service...]  启动已经存在的服务容器

   stop：docker-compose start [Service...]  

   - 停止已经处于运行状态的容器，但不删除它。通过 `docker-compose start` 可以再次启动这些容器。

   选项：

   - -t, --timeout TIMEOUT  停止容器时候的超时（默认为 10 秒）。

   up：docker-compose up [options] [Service...]

   - 该功能十分强大，它将尝试自动完成包括：构建镜像、(重新)创建服务、启动服务并关联服务相关容器的一系列操作
   - 大部分时候都可以通过该命令来启动一个项目
   - 默认docker-compose up启动的容器都在前台，Ctrl-c 停止命令时，所有容器都将会停止
   - docker-compose up -d 将会在后台启动并运行所有容器，生产环境推荐使用



#### Compose模板文件

模板文件是compose的核心，默认模板文件名为docker-compose.yml

~~~shell
version: "3"

services:
  webapp:
    image: examples/web
    ports:
      - "80:80"
    volumes:
      - "/data"
     
#每个服务都必须通过image指令或build指令(需要dockerfile)等来自动构建生成镜像

#如果使用 build 指令，在 Dockerfile 中设置的选项(例如：CMD, EXPOSE, VOLUME, ENV 等) 将会自动被获取，无需在 docker-compose.yml 中重复设置
~~~

1. build：指定Dockerfile所在文件夹的路径，compose将会利用它自动构建这个镜像，然后使用镜像

   ~~~shell
   version: '3'
   services:
     webapp:
       build: ./dir
   ~~~

   也可以使用context指令指定Dockerfile所在文件夹的路径，dockerfile指令指定Dockerfile文件名，arg指令指定构建镜像时的变量

   ~~~shell
   version: '3'
   services:
     webapp:
       build:
         context: ./dir
         dockerfile: Dockerfile-alternate
         args:
           buildno: 1
   ~~~

2. 更多命令参加[官方网站](https://docs.docker.com/compose/compose-file/)



-------------------------------------------------------单独提取出来6节-----------------------------------------------------------------------



### Swarm mode

swarm mode内置K-V存储功能，提供了众多新特性，比如：具有容错能力的去中心化设计、内置服务发现、负载均衡、路由网格、动态伸缩、滚动更新、安全传输等。使得 Docker 原生的 `Swarm` 集群具备与 Mesos、Kubernetes 竞争的实力

swarm是使用swarmKit构建的Docker引擎内置的集群管理和编排工具

#### 节点

运行Docker的主机可以主动初始化一个Swarm集器或者加入一个已存在的swarm集群，这样这个运行Docker的主机就成为一个swarm集群的节点。

节点分为管理节点和工作节点

1. 管理节点：用于swarm集群的管理，docker swarm命令基本只能在管理节点执行(节点退出集群命令 docker swarm leave 可以在工作节点执行)。一个swarm集群可以有多个管理节点，但只有一个管理节点可以成为leader，leader通过raft协议实现

2. 工作节点：任务执行节点，管理节点将服务下发至工作节点执行，管理节点默认也可以作为工作节点，也可以通过配置让服务只运行在管理节点

3. 集群中管理节点于工作节点的关系

   ![image-20220130092459196](https://gitee.com/qianchao_repo/pic-typora/raw/master/img/image-20220130092459196.png)

#### 服务和任务

任务(Task)是swarm中最小的调度单位，目前来说就是一个单一的容器

服务(Services)指一组任务的集合，服务定义了任务的属性，服务有两种模式

1. replicated Services：按照一定规则在各个工作节点上运行指定个数的任务
2. global Services：每个工作节点上运行一个任务
3. 这两种模式通过docker swarm create --mode参数指定

容器、任务、服务的关系

![image-20220130092903098](https://gitee.com/qianchao_repo/pic-typora/raw/master/img/image-20220130092903098.png)



#### 创建Swarm集群

例子：创建一个管理节点，两个工作节点的swram集群

##### 初始化集群

~~~shell
#在已经安装好 Docker 的主机上执行如下命令
$ docker swarm init --advertise-addr 192.168.99.100
Swarm initialized: current node (dxn1zf6l61qsb1josjja83ngz) is now a manager.

To add a worker to this swarm, run the following command:

    docker swarm join \
    --token SWMTKN-1-49nj1cmql0jkz5s954yi3oex3nedyz0fb0xx14ie39trti4wxv-8vxv8rssmk743ojnwacrr2e7c \
    192.168.99.100:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.


#docker swarm init命令的节点自动成为管理节点
#如果你的 Docker 主机有多个网卡，拥有多个 IP，必须使用 --advertise-addr 指定 IP
~~~

##### 增加工作节点

~~~shell
#在其他两个 Docker 主机中分别执行如下命令，创建工作节点并加入到集群中
$ docker swarm join \
    --token SWMTKN-1-49nj1cmql0jkz5s954yi3oex3nedyz0fb0xx14ie39trti4wxv-8vxv8rssmk743ojnwacrr2e7c \
    192.168.99.100:2377

This node joined a swarm as a worker.
~~~

##### 查看集群

此时已经创建好了一个用于一个管理节点两个工作节点的swarm集群

~~~shell
$ docker node ls
ID                           HOSTNAME  STATUS  AVAILABILITY  MANAGER STATUS
03g1y59jwfg7cf99w4lt0f662    worker2   Ready   Active
9j68exjopxe7wfl6yuxml7a7j    worker1   Ready   Active
dxn1zf6l61qsb1josjja83ngz *  manager   Ready   Active        Leader
~~~



#### 部署服务

使用docker service命令来管理swarm集群中的服务，该命令只能在管理节点运行

##### 新建服务

~~~shell
#在swarm集群中运行一个名为nginx的服务
$ docker service create --replicas 3 -p 80:80 --name nginx nginx:1.13.7-alpine

#使用浏览器，输入任意节点 IP ，即可看到 nginx 默认页面
~~~

##### 查看服务

~~~shell
#docker service ls 查看当前集群运行的服务
$ docker service ls
ID               NAME       MODE        REPLICAS         IMAGE                 PORTS
kc57xffvhul5     nginx    replicated      3/3        nginx:1.13.7-alpine      *:80->80/tcp

#docker service ps查看某个服务的详情
$ docker service ps nginx 

#查看某个服务的日志 
$ docker service logs nginx 
~~~



##### 服务伸缩

~~~shell
#docker service scale 对一个服务运行的容器数量进行伸缩，当业务处于高峰期时，需要扩展服务运行的容器数量
$ docker service scale nginx=5

#业务平稳时，需要减少服务运行的容器数量
$docker service scale nginx=2
~~~

##### 删除服务

~~~shell
$ docker service rm nginx
~~~



#### 使用compose文件

swarm集群中也可以使用compose文件来配置、启动多个服务

docker service create 一次只能部署一个服务，使用docker-compose.yml文件可以一次启动多个关联的服务

~~~shell
#swarm集群中部署wordpress
version: "3"

services:
  wordpress:
    image: wordpress
    ports:
      - 80:80
    networks:
      - overlay
    environment:
      WORDPRESS_DB_HOST: db:3306
      WORDPRESS_DB_USER: wordpress
      WORDPRESS_DB_PASSWORD: wordpress
    deploy:
      mode: replicated
      replicas: 3

  db:
    image: mysql
    networks:
       - overlay
    volumes:
      - db-data:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: somewordpress
      MYSQL_DATABASE: wordpress
      MYSQL_USER: wordpress
      MYSQL_PASSWORD: wordpress
    deploy:
      placement:
        constraints: [node.role == manager]
#visualizer提供一个可视化页面
  visualizer:
    image: dockersamples/visualizer:stable
    ports:
      - "8080:8080"
    stop_grace_period: 1m30s
    volumes:
      - "/var/run/docker.sock:/var/run/docker.sock"
    deploy:
      placement:
        constraints: [node.role == manager]

volumes:
  db-data:
networks:
  overlay:
~~~

##### 部署服务

在swram中使用docker-compose.yml文件可以使用docker stack命令

~~~shell
#部署服务使用 docker stack deploy，其中 -c 参数指定 compose 文件名
$ docker stack deploy -c docker-compose.yml wordpress
~~~

##### 查看服务

~~~shell
$ docker stack ls
NAME                SERVICES
wordpress           3
~~~

##### 移除服务

~~~shell
$ docker stack down wordpress
Removing service wordpress_db
Removing service wordpress_visualizer
Removing service wordpress_wordpress
Removing network wordpress_overlay
Removing network wordpress_default


#该命令不会移除服务所使用的 数据卷，如果你想移除数据卷请使用 docker volume rm
~~~



#### 管理配置信息

在动态的、大规模的分布式集群上，管理和分发配置文件也是很重要的工作。传统的配置文件分发方式（如配置文件放入镜像中，设置环境变量，volume 动态挂载等）都降低了镜像的通用性。

Docker 新增了 `docker config` 子命令来管理集群中的配置信息，以后你无需将配置文件放入镜像或挂载到容器中就可实现对服务的配置。

注意：`config` 仅能在 Swarm 集群中使用。

以在 Swarm 集群中部署 `redis` 服务为例

##### 创建Config

~~~shell
#新建redis.cof文件
port 6380
~~~

~~~shell
#使用docker config create 命令创建config
$docker config create redis.conf redis.conf
~~~

##### 查看config

~~~shell
$ docker config ls

ID                          NAME                CREATED             UPDATED
yod8fx8iiqtoo84jgwadp86yk   redis.conf          4 seconds ago       4 seconds ago
~~~



##### 创建redis服务

~~~shell
$ docker service create \
     --name redis \
     # --config source=redis.conf,target=/etc/redis.conf \
     --config redis.conf \
     -p 6379:6380 \
     redis:latest \
     redis-server /redis.conf
~~~



#### 滚动升级

docker service update命令，使用 `nginx:1.13.7-alpine` 镜像部署了一个名为 `nginx` 的服务。现在我们想要将 `NGINX` 版本升级到 `1.13.12`

~~~shell
$ docker service update \
    --image nginx:1.13.12-alpine \
    nginx
    
#一样的 上面\是到末尾的换行符而已，使用--image 选项更新了服务的镜像
$ docker service update --image nginx:1.13.12-alpine  nginx
~~~

#### 服务回退

假设我们发现 nginx 服务的镜像升级到 `nginx:1.13.12-alpine` 出现了一些问题，我们可以使用命令一键回退

~~~shell
$ docker service rollback nginx
~~~

~~~shell
$ docker service ps nginx
#结果的输出详细记录了服务的部署、滚动升级、回退的过程
ID   NAME   IMAGE   NODE    DESIRED STATE    CURRENT STATE                         ERROR      PORTS
rt677gop9d4x  nginx.1  nginx:1.13.7-alpine   VM-20-83-debian     Running   Running about a minute ago
d9pw13v59d00  \_ nginx.1  nginx:1.13.12-alpine  VM-20-83-debian   Shutdown   Shutdown 2 minutes ago
i7ynkbg6ybq5  \_ nginx.1  nginx:1.13.7-alpine   VM-20-83-debian    Shutdown  Shutdown 2 minutes ago
~~~







































