# 计院楼后台系统

### 该系统现在已经实现了计院楼公众号后台的以下功能
- 查询回复
- 新成员注册
- 管理员邀请新成员加入系统
- 管理员修改成员信息
- 超级管理员添加或者删除管理员
- 超级管理员添加或者删除表
- 任意公众号后台接入
- 弹幕互动系统

### 使用的技术
- JSP
- SQL
- PYTHON

### 开发环境
##### 服务端
- Java 1.8.0
- Python3
- Tomcat8
- Pycharm
- Eclipse Mars2
- Mysql 5.7.20
- Navicat 2015

##### 客户端弹幕软件
- python 3.6
- pyqt5
- 其他若干包，详见import

### 运行环境
##### 服务端环境
- Ubuntu 17.10
- Java 1.8.0
- Tomcat8
- Mysql 5.7.20

##### 客户端弹幕软件
- Windows，添加至杀毒软件白名单

### 项目文件说明
- 文件中不包含Eclipse的项目编译生成的中间文件
- 文件中包含需要用到的4个库，请记得自行BuildPath
- 文件中未包含包含数据库用户名密码、邮箱用户名密码、服务器信息的`src/pub/Configure.java`，需要请自行编写
- 文件中只包含弹幕客户端的可执行程序，并没有服务端war包

### 目录简要说明
- `bilibili_client`客户端弹幕程序Python代码
- `src`服务端各个模块代码
- `WebContent`服务端网页
- `*.jar`各种库的包

<meta http-equiv="refresh" content="1">