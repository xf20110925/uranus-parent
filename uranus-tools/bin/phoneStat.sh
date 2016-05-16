 #!/usr/bin/env bash
 source /etc/profile
 HOME=$(cd "$(dirname "$0")/../"; pwd)
 cd $HOME
 jars=$(ls $HOME/|grep .jar)
 libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
 echo $jars

 echo $HOME/config:$jars:$libjar
nohup   java -cp $HOME/config:$jars:$libjar  com.ptb.uranus.stat.phone.PhoneStatistics  $* >> $HOME/logs/run.log 2>&1 &