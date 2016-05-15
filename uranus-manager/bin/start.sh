#!/usr/bin/env bash
source /etc/profile
HOME=$(cd "$(dirname "$0")/../"; pwd)

jars=$(ls $HOME/|grep .jar)
libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
echo $jars
cd $HOME
echo $HOME/config:$jars:$libjar
nohup  java -cp $HOME/config:$libjar:$jars  com.ptb.uranus.manager.UranusManagerApplication  $* >> log/run.log 2>&1 &