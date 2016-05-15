uranus-asistant
===================================
简介
----------------------------------
该项目为uranus提供附加数据的管理,管理内容包括但不限于账户上下文管理,代理服务器管理

* 目前支持微信账户登陆
* 可以用于微信上下文信息的URL地址及COOKIE API
* 帐户池内异常报警功能(数量不够)

安装需求
----------------------------------
1. phantomjs安装

        1.1 安装库
            #将phantomjs放到/usr/bin/下
        1.2 安装字体
            #在centos中执行：
            yum install bitmap-fonts bitmap-fonts-cjk 
            #在ubuntu中执行：
            sudo apt-get install xfonts-wqy
        1.3 修改系统编码为中文
            #/etc/sysconfig/i18n 中将 LANG更改为
            LANG="zh_CN.UTF-8"
        
2. TOMCAT安装

        1.1 需求TOMCAT 8

3. redis安装
        版本>2.0
        如是centos的话,yum install redis即可

程序安装
-----------------------------------
1.  安装步骤

        1. 解压安装包
        2. 关闭TOMCAT
        3. killall phantomjs
        3. 移除ROOT下内容
        4. 将WAR包拷贝到ROOT下
        5. 执行jar -xvf *.war
        
2. 配置文件配置

        配置war包的class下的redis.propertis 
        配置war包的bus.properties 为KAFKA zk和Kafka broker id
        WEB-INFO/js/ 中的phoneSchdularUrl 的手机调度地址为uranus-asistant的地址




