#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

MAINCLASS=com.yxbear.sg.SgAppMain

PARAMTER=""

PIDS=`ps -f | grep java | grep "$CONF_DIR" |awk '{print $2}'`


LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=$DEPLOY_DIR/lib/*

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.util.Arrays.useLegacyMergeSort=true"

JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2048m -Xms2048m -Xmn512m -XX:MaxMetaspaceSize=256m  -XX:+UseG1GC  -XX:+DisableExplicitGC -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods "
else
    JAVA_MEM_OPTS=" -server -Xmx2048m -Xms2048m -XX:MaxMetaspaceSize=256m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi


echo -e "Starting the application ...\c"

nohup java $JAVA_OPTS $JAVA_MEM_OPTS -classpath $CONF_DIR:$LIB_JARS $MAINCLASS $PARAMTER >/dev/null 2>&1 &


echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
