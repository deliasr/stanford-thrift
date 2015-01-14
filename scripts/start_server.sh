#!/bin/zsh

MAINDIR=$(dirname $0:A)/../
CLASSDIR=$(dirname $0:A)/../target/classes/

if [ $# -eq 3 ]; then
 PORT=$1
 HEAPSIZE=$2
 CONFIG=$3
 java -cp $CLASSPATH:$MAINDIR/"*":$CLASSDIR -Xmx$HEAPSIZE -XX:-UseGCOverheadLimit org.ets.research.nlp.stanford_thrift.StanfordCoreNLPServer $PORT $CONFIG
# java -cp $CLASSPATH:$MAINDIR/"*":$MAINDIR/out-java/production/stanford
# -thrift -Xmx$HEAPSIZE -XX:-UseGCOverheadLimit org.ets.research.nlp.stanford_thrift.StanfordCoreNLPServer $PORT $CONFIG
 echo $MAINDIR
else
 echo "Usage: $(basename $0) <port> <heapsize> <config file>"
 echo "See scripts/ for an example config file."
fi


