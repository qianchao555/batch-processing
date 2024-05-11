### 测试框架

#### 架构
- 基于`SpringBoot` +`mock`+ `dbunit` + `testcontainer` + `excel导入数据集作为数据源`
- testcontainer: 用于启动数据库容器。测试完毕后关闭容器
- dbunit+excel: 用于数据准备,向数据库中插入数据
  - 配置表数据，sheet代表表名，第一行代表列名，第二行开始代表数据
- docker服务:本地、远程都可以。根据自身情况修改
  - 建议远程docker,这样团队公共使用，不用每个人都安装docker

