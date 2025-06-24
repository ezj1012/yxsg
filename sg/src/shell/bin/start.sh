#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

MAINCLASS=com.yxbear.sg.SgAppMain

PIDS=`ps -f | grep java | grep "$CONF_DIR" |awk '{print $2}'`

LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

LIB_JARS=$DEPLOY_DIR/lib/*
JAVA_MEM_OPTS=" -server -Xmx1024m -Xms1024m -XX:MaxMetaspaceSize=128m "


echo -e "Starting the application ...\c"

nohup java $JAVA_OPTS $JAVA_MEM_OPTS -classpath $CONF_DIR:$LIB_JARS $MAINCLASS >/dev/null 2>&1 &


echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
