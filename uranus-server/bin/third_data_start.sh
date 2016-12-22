 #!/usr/bin/env bash
 source /etc/profile
 HOME=$(cd "$(dirname "$0")/../"; pwd)
 CONFIG_FILE_HOME=/opt/ptbconf/uranus-server
 cd $HOME
 jars=$(ls $HOME/|grep .jar)
 libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
 echo $jars

 echo $CONFIG_FILE_HOME:$jars:$libjar
 java -cp $CONFIG_FILE_HOME:$libjar:$jars  -Dtopic=third com.ptb.uranus.server.third.version2.ThirdEntryV2 $* >>$HOME/logs/run.log 2>&1