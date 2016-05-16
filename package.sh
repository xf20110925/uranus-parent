#!/usr/bin/env bash
rm ./dist/
HOME=$(cd "$(dirname "$0")/"; pwd)
cd $HOME
rm -rf ./dist
sh uranus-manager/script/release.sh
sh uranus-server/script/release.sh
sh uranus-scheduler/script/release.sh
sh uranus-tools/script/release.sh
sh uranus-asistant/script/release.sh
cp -rf doc ./dist/

