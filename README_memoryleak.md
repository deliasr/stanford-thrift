README Memory Leak
==================

1. To build everything 

```Python
./gradlew
```

The `.class` files will be under `build/classes/main/org/ets/research/nlp
/stanford_thrift`

2. To test Stanford CoreNLP

```Python
StanfordCoreNLPTest tweets.1000
```

3. To test Stanford CoreNLP + Thrift

a) start server: 

```Python
scripts/start_server.sh 12345 2G scripts/config
```

Usage: `start_server.sh <port> <heapsize> <config file>`

b) start StanfordCoreNLPTokenizerTest: 

```Python
StanfordCoreNLPTokenizerTest localhost 12345 tweets.1000 scripts/config
```

Usage: `StanfordCoreNLPTokenizerTest <server> <port> <input file> <config file>`