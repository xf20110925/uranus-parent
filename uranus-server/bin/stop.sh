#!/bin/bash

URANUS_HOME=$(cd "$(dirname "$0")/../"; pwd)
cd ${URANUS_HOME}
jar_name=`ls | grep .jar`
ps -ef|grep $jar_name |grep -v grep |awk '{print $2}'|head -n 1 |xargs kill -9
killall phantomjs
