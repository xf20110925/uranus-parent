uranus-server
========================

简介
--------------------------

执行一些与uranus-server相关的统计任务


运行需求
-------------------------

1. centos6+
2. java 8+
3. phantomjs


安装手册
-------------------------

1. 打开Jar包,配置jdbc.propertie,mongodb.propertie等等
2. 配置总线 bus.propertis 中的zk和KAFKA地址
3. java -cp *.jar com.ptb.uranus.tools.Tool

使用防范
------------------------
1. 微信媒体导入
        
        #需配置mongodb.properties 地址为存储媒体信息的数据库
        执行 bin/tool.sh -x [file]   //其中file[txt格式]中输入微信ID(不是昵称),每行输入一个ID
   
2. 微博媒体导入

        #需配置mongodb.properties 地址为存储媒体信息的数据库
        执行 bin/tool.sh -b [file]   //其中file[txt格式]中输入微博ID(不是昵称,是一个10位数字),每行输入一个ID,

3. 手机统计脚本
        
        #需配置bus.properties,地址为kafka有配置,需配置zk和brokerid等
        #需配置jdbc.properties 地址为手机状态统计结果的数据库
        执行bin/phoneStat.sh



















