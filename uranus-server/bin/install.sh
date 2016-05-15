#!/usr/bin/env bash
source /etc/profile
URANUS_HOME=$(cd "$(dirname "$0")/../"; pwd)
cp $URANUS_HOME/bin/phantomjs /usr/bin/
yum install bitmap-fonts bitmap-fonts-cjk libicu