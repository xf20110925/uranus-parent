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

1. 配置爬虫助手操作接口,修改config/下的配置文件crawler.properties的地址配置

        
2. 配置总线参数,修改发布jar包内的bus.propertis

        #必配项
        bootstrap.servers=kafka1:9092,kafka2:9092 //KAFKA broker在地址
        zookeeper.connect=zk1:2181,zk2:2181 //KAFKA所用ZK的地址配置
        
        详细配置参数为可参考
        http://kafka.apache.org/documentation.html#producerconfigs
3. 更改启动参数
    
        更改bin/start.sh 内的启动参数
        nohup java -cp $URANUS_HOME/config:$URANUS_HOME/uranus-server-1.0.0-SNAPSHOT.jar  org.springframework.boot.loader.JarLauncher \
        --listenTopics=uranus-server_wx_s,uranus-server_s_wx_d, uranus-server_wb, uranus-server \
        --spider.worker.num=3 \
        --kafka.consumer.num=1 >>/dev/null 2>&1 &
        其中listenTopics为监听的topic名称，分别为微信静态数据，微信动态数据，微博数据，新闻类网站数据
        spider.worker.num 为执行爬去任务的总线程数
        kafka.consumber.num 监听的每个TOPIC的线程数
        
3. 启动程序
        bin/start.sh
4. 关闭程序

        bin/stop.sh
    


