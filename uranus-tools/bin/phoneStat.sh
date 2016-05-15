#!/usr/bin/env bash
source /etc/profile
URANUS_ASISTANT_HOME=$(cd "$(dirname "$0")/../"; pwd)

jars=$(ls $URANUS_ASISTANT_HOME/|grep .jar)
cd $URANUS_ASISTANT_HOM
nohup java -cp $URANUS_ASISTANT_HOME/config:$jars  com.ptb.uranus.stat.phone.PhoneStatistics >> log/run.log 2>&1 &

 #!/usr/bin/env bash
 source /etc/profile
 HOME=$(cd "$(dirname "$0")/../"; pwd)

 jars=$(ls $HOME/|grep .jar)
 libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
 echo $jars
 cd $HOME
 echo $HOME/config:$jars:$libjar
nohup   java -cp $HOME/config:$jars:$libjar  com.ptb.uranus.stat.phone.PhoneStatistics  $* >> log/run.log 2>&1 &