@echo off & setlocal enabledelayedexpansion

chcp 65001 >nul

cd ..
set LIB_JARS=.\lib\*
java -Xms1024m -Xmx1024m -XX:MetaspaceSize=64m  -Dfile.encoding=UTF-8 -classpath .\conf;%LIB_JARS% com.yxbear.sg.SgAppMain 
