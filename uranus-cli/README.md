uranus-cli
===================================
简介
----------------------------------

该工程为对由gaia提供指定内容爬去的外部调用接口服务

目前功能
----------------------------------
1. 通过微信文章的URL,爬去其文章静态信息(标题,作者,文章内容,发布时间),文章动态信息(阅读数,点赞数)的API调用
2. 通过微信ID或微信文章URL或微信的BIZ—ID 爬去微信媒体的静态信息(头像地址,二维码,微信ID,微信昵称)
3. 通过微信BIZ-ID或微信ID爬去某个微信的最新文章


安装需求
----------------------------------
1. JAVA 8
2. MAVEN

使用方法
-----------------------------------
1. 在maven的POM文件中引入文件依赖(要确定配置好了MAVEN私服),其中VERSION部分请咨询开发者,要最近代码

        <dependency>
            <groupId>com.ptb.uranus</groupId>
            <artifactId>uranus-cli</artifactId>
            <version>1.0-SNAPSHOT</version>  
        </dependency>
        
2. 配置配置文件

        2.1. 在你的resources目录下加入总线的配置文件

        2.2. bus.propertis //具体配置方法可参考发布包中的bus-default.propertis,必备项为

        2.3. bootstrap.servers=kafka1:9092,kafka2:9092 //KAFKA broker在地址

        2.4. zookeeper.connect=zk1:2181,zk2:2181 //KAFKA所用ZK的地址配置
    

3. 使用程序的DAEMON
           
           //实例化客户端
           UranusClient uranusClient = new UranusClient("recvTopicName", new ResponseEventListenerAdapter() {

           @Override
           public void wxArticleStaticResponseEvent(Message<WxArticleResponse> resp, Bus bus) {
               this.handleMessage(resp, bus);
           }

           @Override
           public void wxArticleDyanmicResponseEvent(Message<WxArticleResponse> resp, Bus bus) {
               this.handleMessage(resp, bus);
           }

           @Override
           public void wxArticleMediaEvent(Message<WxArticleResponse> resp, Bus bus) {
               this.handleMessage(resp, bus);
           }

           @Override
           public void wxMediaStaticEvent(Message<WxMediaResponse> resp, Bus bus) {
               this.handleMessage(resp, bus);
           }

           @Override
           public void wxMediaRecentArticleEvent(Message<WxMediaResponse> resp, Bus bus) {
               this.handleMessage(resp, bus);
           }
           }, 3);


           //发送一个请求
           uranusClient.sendFetchWxArticleStaticCommand(new WxArticleRequest("http://mp.weixin.qq.com/s?__biz=Mjk2NzMwNTY2MA==&mid=403620277&idx=1&sn=b8df1903948f46ff425b2c24012c531a&3rd=MzA3MDU4NTYzMw==&scene=6#rd", "2222",MessageLevel.UserLevel));
           //当不需要使用此CLIENT ,记得关闭他
           uranusClient.close(); 
结束
===========================