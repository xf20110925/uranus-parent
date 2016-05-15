uranus-server
========================

简介
--------------------------

1. 接收uranus-cli的命令,并爬取相关数据后,并返回结果
2. 内部采用总线协议 http://192.168.5.23/wiki/index.php/PTB总线通讯协议



运行需求
-------------------------

1. centos6+
2. java 8+
3. phantomjs


安装手册
-------------------------

    0. 进行系统前置安装(安装phantomjs) 

    1. 初始化数据库
        mongorestore -h [mongodbHost] -d uranus config/data/uranus 

    执行 bin/install.sh

**config目录下的文件,首次安装如需修改,请将example后缀取掉使用

1. 配置爬虫助手操作接口,修改config/下的配置文件crawler.properties,mongodb.properties
        
2. 启动程序
        bin/start.sh


