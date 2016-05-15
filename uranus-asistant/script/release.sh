#!/usr/bin/env bash
OUTPUT_DIR=`pwd`
OUTPUT_DIR=$OUTPUT_DIR/dist
mkdir $OUTPUT_DIR
HOME=$(cd "$(dirname "$0")/../"; pwd)
cd $HOME
publishTime=`date "+%Y%m%d%H%M%S"`
PNAME=`ls ${HOME}/target|grep '.*\.war$'`
PNAME=${PNAME%.*}-${publishTime}
echo "发布包名为：${PNAME}"
DistDir=$HOME/target/dist/${PNAME}
mkdir -p $DistDir
cp target/*.*ar ${DistDir}
cp -rf ${HOME}/bin ${DistDir}
cp -rf ${HOME}/config ${DistDir}
cp -rf ${HOME}/README.md ${DistDir}
cp -rf ${HOME}/target/libs ${DistDir}
cd $HOME/target/dist/
chmod -R 755   ${PNAME}/
tar czvf ${PNAME}.tar.gz ${PNAME}
cp ${PNAME}.tar.gz  $OUTPUT_DIR
rm  -rf ${PNAME}/
echo "打包成功"