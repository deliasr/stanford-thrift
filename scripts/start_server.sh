#!/bin/sh

MAINJAR=$(dirname $0:A)/../build/libs/stanford-corenlp-wrapper*

if [ $# -eq 3 ]; then
 PORT=$1
 HEAPSIZE=$2
 CONFIG=$3
 java -cp $MAINJAR -Xmx$HEAPSIZE -XX:-UseGCOverheadLimit org.ets.research.nlp.stanford_thrift.StanfordCoreNLPServer $PORT $CONFIG
# java -cp $CLASSPATH:$MAINDIR/"*":$MAINDIR/out-java/production/stanford
# -thrift -Xmx$HEAPSIZE -XX:-UseGCOverheadLimit org.ets.research.nlp.stanford_thrift.StanfordCoreNLPServer $PORT $CONFIG
else
 echo "Usage: $(basename $0) <port> <heapsize> <config file>"
 echo "See scripts/ for an example config file."
fi


