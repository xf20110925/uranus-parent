#!/usr/bin/env bash
#/bin/bash

source /etc/profile
URANUS_HOME=$(cd "$(dirname "$0")/../"; pwd)
CONFIG_FILE_HOME=/opt/ptbconf/uranus-server
cd $URANUS_HOME

#debug_model="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=9999"
main_class="com.ptb.uranus.server.Entry"
lib_dir="libs/"

allclasspath=`ls $lib_dir | grep .jar | awk '{jar=jar"'"$lib_dir"'"$1":"} END {print jar}'`

jars=$(ls |grep .jar)
echo $jars

TOPIC=uranus-server_v2_wx_s
if [ ! -d logs/${TOPIC} ]; then
    mkdir logs/${TOPIC}
fi
nohup java -cp $CONFIG_FILE_HOME:$allclasspath$jars -Dtopic=${TOPIC} ${main_class} \
--listenTopics=${TOPIC} \
--spider.worker.num=5 \
--kafka.consumer.num=1 >> logs/${TOPIC}/run.log 2>&1 &

TOPIC=uranus-server_v2_wx_d
if [ ! -d logs/${TOPIC} ]; then
    mkdir logs/${TOPIC}
fi
nohup java -cp $CONFIG_FILE_HOME:$allclasspath$jars -Dtopic=${TOPIC} ${main_class} \
--listenTopics=${TOPIC} \
--spider.worker.num=3 \
--kafka.consumer.num=1 >> logs/${TOPIC}/run.log 2>&1 &

TOPIC=uranus-server_v2_wb
if [ ! -d logs/${TOPIC} ]; then
    mkdir logs/${TOPIC}
fi
java -cp $CONFIG_FILE_HOME:$allclasspath$jars -Dtopic=${TOPIC} ${main_class} \
--listenTopics=${TOPIC} \
--spider.worker.num=8 \
--kafka.consumer.num=1 >> logs/${TOPIC}/run.log  2>&1

#TOPIC=uranus-server_v2
#if [ ! -d logs/${TOPIC} ]; then
#    mkdir logs/${TOPIC}
#fi
#nohup java -cp $config_dir:$allclasspath$jars -Dtopic=${TOPIC} ${main_class} \
#--listenTopics=${TOPIC} \
#--spider.worker.num=4 \
#--kafka.consumer.num=1 >> logs/${TOPIC}/run.log  2>&1 &

