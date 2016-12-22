 #!/usr/bin/env bash
 source /etc/profile
 HOME=$(cd "$(dirname "$0")/../"; pwd)
 CONFIG_FILE_HOME=/opt/ptbconf/uranus-scheduler
 cd $HOME
 jars=$(ls $HOME/|grep .jar)
 libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
 echo $jars

 echo $CONFIG_FILE_HOME:$jars:$libjar
 java -cp $CONFIG_FILE_HOME:$libjar:$jars  com.ptb.uranus.scheduler.TimeCollectSchduler $* >>$HOME/logs/run.log 2>&1
