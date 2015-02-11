package org.ets.research.nlp.stanford_thrift;

import CoreNLP.StanfordCoreNLP;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by delia on 11/02/15.
 */
public class StanfordCoreNLPClientTokenizer {
    public static void main(String [] args) throws Exception {

        String server = "";
        Integer port = 0;
        String inputFilename = "";
        String configFilePath = "";

        if (args.length == 4) {
            server = args[0];
            port = Integer.parseInt(args[1]);
            inputFilename = args[2];
            configFilePath = args[3];
        }
        else {
            System.err.println("Usage: StanfordCoreNLPClient <server> <port> " +
                    "<inputfile> <configPath>");
            System.exit(2);
        }

        try {
            TTransport transport;
            transport = new TSocket(server, port);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            StanfordCoreNLP.Client client = new StanfordCoreNLP.Client(protocol);

            perform(client, inputFilename, configFilePath);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(StanfordCoreNLP.Client client, String
            inputFilename, String configFilePath) throws Exception
    {
        FileReader infile = null;

        try {
            infile = new FileReader(inputFilename);
            BufferedReader in = new BufferedReader(infile);
            int i = 0;
            while (in.ready()) {
                String sentence = in.readLine();
                
                List<List<String>> tokens = client.tokenize_text(sentence);
                System.out.print(String.format("Tweet %d: ", i));
                for (List<String> toks : tokens)
                {
                    for (String s : toks) {
                        System.out.print(s + " ");
                    }                    
                }
                System.out.println();
                i++;

            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
