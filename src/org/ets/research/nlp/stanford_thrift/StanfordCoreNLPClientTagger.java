package org.ets.research.nlp.stanford_thrift;
// Generated code
import CoreNLP.StanfordCoreNLP;
import CoreNLP.TaggedToken;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.ArrayList;
import java.util.List;

public class StanfordCoreNLPClientTagger {

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

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(StanfordCoreNLP.Client client) throws Exception
    {
        try {

            List<TaggedToken> tokens = new ArrayList<TaggedToken>();

            tokens.add(new TaggedToken("NNP", "Diane"));
            tokens.add(new TaggedToken(null, "lives"));
            tokens.add(new TaggedToken(null, "in"));
            tokens.add(new TaggedToken(null, "New"));
            tokens.add(new TaggedToken(null, "Jersey"));

            List<TaggedToken> pos_tokens =
                    client.tag_partially_tagged_tokens(tokens);

            for (TaggedToken t : pos_tokens) {
                System.out.println(t.getToken() + " " + t.getTag());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
