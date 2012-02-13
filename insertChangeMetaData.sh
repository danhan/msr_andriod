#!/bin/bash

# PROGNAME=$(basename $0)

myDir=$(readlink -f $0 | xargs dirname)
pushd .


#cd ${myDir}/../

#source ./bin/envrc

USAGE="USAGE: "

if [ -z "$1" ]; then
	echo "$USAGE"
fi

if [ ! -f "${JAVA_HOME}/bin/java" ]; then
	echo "JAVA_HOME not found."
	exit -1
fi


MYLIB=${PWD}/bin/msr.jar
MYSQLLIB=${PWD}/lib/mysql-connector-java-5.0.8-bin.jar


#echo ${MYLIB}

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "insert data for change message"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

${JAVA_HOME}/bin/java -Xmx1500m -classpath ${MYLIB}:${MYSQLLIB} android.optimization.SAXChangeMeta

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "insert data for change message end"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

