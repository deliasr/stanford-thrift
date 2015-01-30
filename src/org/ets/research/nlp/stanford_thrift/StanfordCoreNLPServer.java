package org.ets.research.nlp.stanford_thrift;
/*
  Apache Thrift Server for Stanford CoreNLP (stanford-thrift)
  Copyright (C) 2013 Diane M. Napolitano, Educational Testing Service
  
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation, version 2
  of the License.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/


import CoreNLP.StanfordCoreNLP;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Generated code

public class StanfordCoreNLPServer
{
    final static Logger logger = LoggerFactory.getLogger(StanfordCoreNLPServer.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws InterruptedException {

        if (args.length != 2)
        {
            System.err.println("Usage: StanfordCoreNLPServer <port> <config file>");
            System.exit(2);
            return;
        }

        final Integer portNum = Integer.parseInt(args[0]);
        String configFile = args[1];

        try {
            final StanfordCoreNLP.Processor processor = new StanfordCoreNLP.Processor(new StanfordCoreNLPHandler(configFile));
            Runnable server = new Runnable() {
                public void run() {
                    try {
                        TServerTransport serverTransport = new TServerSocket(portNum);
                        TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

                        logger.info("The CoreNLP server is running...");
                        server.serve();
                    } catch (Exception ex) {
                        logger.error("general error", ex);
                    }
                }
            };
            new Thread(server).start();
        } catch (Exception ex) {
            logger.error("general error", ex);
        }

        logger.info("The CoreNLP server is shutting down...");
    }
}
