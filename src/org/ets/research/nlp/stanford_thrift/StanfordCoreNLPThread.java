package org.ets.research.nlp.stanford_thrift;

import CoreNLP.StanfordCoreNLP;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class StanfordCoreNLPThread implements Runnable
{
    final Logger logger = LoggerFactory.getLogger(StanfordCoreNLPThread.class);

    private StanfordCoreNLP.Processor processor;
    private Integer port;

    @SuppressWarnings("rawtypes")
    public StanfordCoreNLPThread(String configFile, Integer portNum) throws Exception {
        this.processor = new StanfordCoreNLP.Processor(new StanfordCoreNLPHandler(configFile));
        port = portNum;
    }

    public void run()
    {
        int THREAD_POOL_SIZE = 10;
        try
        {
            // Initialize the transport socket
            TServerTransport serverTransport = new TServerSocket(port);
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
            args.maxWorkerThreads(THREAD_POOL_SIZE);
            args.processor(this.processor);
            args.executorService(new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE));
            TServer server = new TThreadPoolServer(args);

            // From https://github.com/m1ch1/mapkeeper/blob/eb798bb94090c7366abc6b13142bf91e4ed5993b/stubjava/StubServer.java#L93
                /*TNonblockingServerTransport trans = new TNonblockingServerSocket(port);
                TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(trans);
                args.transportFactory(new TFramedTransport.Factory());
                args.protocolFactory(new TBinaryProtocol.Factory());
                args.processor(processor);
                args.selectorThreads(4);
                args.workerThreads(32);
                TServer server = new TThreadedSelectorServer(args);*/

            System.out.println("The CoreNLP server is running...");
            server.serve();
        }
        catch (Exception ex)
        {
            logger.error("server thread error", ex);
        }
    }
}
