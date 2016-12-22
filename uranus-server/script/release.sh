#!/usr/bin/env bash
OUTPUT_DIR=`pwd`
OUTPUT_DIR=$OUTPUT_DIR/dist
mkdir $OUTPUT_DIR
HOME=$(cd "$(dirname "$0")/../"; pwd)
cd $HOME

JARNAME=`ls ${HOME}/target|grep -v 'sources\..*ar'|grep '.*\..*ar$'`
echo "输出包为"$JARNAME
PNAME="uranus-server"
echo "发布包名为：${PNAME}"
DistDir=$HOME/target/dist/${PNAME}
mkdir -p $DistDir
cp -rf ${HOME}/${JARNAME} ${DistDir}/uranus-server.jar
cp -rf ${HOME}/script/Dockerfile ${DistDir}/Dockerfile
cp -rf ${HOME}/bin ${DistDir}
cp -rf ${HOME}/config ${DistDir}
cp -rf ${HOME}/README.md ${DistDir}
cp -rf ${HOME}/target/libs ${DistDir}
mkdir ${DistDir}/logs
cd $HOME/target/dist/
chmod -R 744   ${PNAME}/
tar czvf ${PNAME}.tar.gz ${PNAME}
cp ${PNAME}.tar.gz  $OUTPUT_DIR
rm  -rf ${PNAME}/
echo "打包成功"