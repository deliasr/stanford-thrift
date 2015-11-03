package org.ets.research.nlp.stanford_thrift;
// Generated code
import CoreNLP.ParseTree;
import CoreNLP.StanfordCoreNLP;
import CoreNLP.TaggedToken;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class StanfordCoreNLPClient {

    public static void main(String [] args) throws Exception {

        String server = "";
        Integer port = 0;
        String inputFilename = "";

        if (args.length == 4) {
            server = args[0];
            port = Integer.parseInt(args[1]);
            inputFilename = args[2];
        }
        else {
            System.err.println("Usage: StanfordCoreNLPClient <server> <port> " +
                    "<inputfile>");
            System.exit(2);
        }

        try {
            TTransport transport;
            transport = new TSocket(server, port);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            StanfordCoreNLP.Client client = new StanfordCoreNLP.Client(protocol);

            perform(client, inputFilename);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(StanfordCoreNLP.Client client, String
            inputFilename) throws Exception
    {
        FileReader infile = null;

        try {
            infile = new FileReader(inputFilename);
            BufferedReader in = new BufferedReader(infile);
            while (in.ready()) {
                String sentence = in.readLine();

                // parse tree
                List<ParseTree> trees = client.parse_text(sentence, null);
                for (ParseTree tree : trees)
                {
                    System.out.println(tree.tree);
                }

                // partial tagged text
                List<TaggedToken> tokens = client.tag_partially_tagged_sentence(sentence, "_");

                for (TaggedToken tok : tokens)
                {
                    System.out.println(tok.getToken() + " " + tok.getTag());
                }
            }
            in.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
}
