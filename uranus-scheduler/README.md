uranus-scheduler
================

简介
---
调度任务处理

环境配置
----
1.centos 6+
2.java 8



1 配置统一的uranus.propertie和bus.propertie配置文件
2 建立MONGO的索引
db.schedule.ensureIndex({"obj.collectType":1,"nTime":1,"done":1,"priority":1})

启动
---
1.使用start.sh启动脚本
