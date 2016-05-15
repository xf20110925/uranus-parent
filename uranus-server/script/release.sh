#!/usr/bin/env bash
HOME=$(cd "$(dirname "$0")/../"; pwd)
cd $HOME
publishTime=`date "+%Y%m%d%H%M%S"`
PNAME=`ls ./target|grep '.*\.jar$'`
PNAME=${PNAME%.*}-${publishTime}
echo "发布包名为：${PNAME}"
DistDir=dist/${PNAME}
mkdir -p $DistDir
cp target/*.jar ${DistDir}
cp -rf ./bin ${DistDir}
cp -rf ./config ${DistDir}
cp -rf ./README.md ${DistDir}
cp -rf ./target/libs ${DistDir}
mkdir ${DistDir}/logs
cd dist/
chmod -R 755   ${PNAME}/
tar czvf ${PNAME}.tar.gz ${PNAME}
rm  -rf ${PNAME}/
echo "打包成功"