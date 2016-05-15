 #!/usr/bin/env bash
 source /etc/profile
 HOME=$(cd "$(dirname "$0")/../"; pwd)

 jars=$(ls $HOME/|grep .jar)
 libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
 echo $jars
 cd $HOME
 echo $HOME/config:$jars:$libjar
 java -cp $HOME/config:$jars:$libjar  com.ptb.uranus.tools.Tools $*
