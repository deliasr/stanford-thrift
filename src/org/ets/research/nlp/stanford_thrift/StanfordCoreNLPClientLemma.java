package ets.research.nlp.stanford_thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delia on 18/02/15.
 */
public class StanfordCoreNLPClientLemma {

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
            CoreNLP.StanfordCoreNLP.Client client = new CoreNLP.StanfordCoreNLP.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(CoreNLP.StanfordCoreNLP.Client client) throws Exception
    {
        try {

            List<CoreNLP.TaggedToken> tokens = new ArrayList<CoreNLP.TaggedToken>();

            tokens.add(new CoreNLP.TaggedToken("NNP", "Societe"));
            tokens.add(new CoreNLP.TaggedToken("NNP", "Generale"));
            tokens.add(new CoreNLP.TaggedToken("VBZ", "acquires"));
            tokens.add(new CoreNLP.TaggedToken("NN", "stake"));

            //tokens.add(new TaggedToken("VB", "talks"));

            List<CoreNLP.TaggedToken> lemmas =
                    client.lemmatize_pos_tagged_tokens(tokens);

            for (CoreNLP.TaggedToken l : lemmas) {
                System.out.println(l.getToken() + " " + l.getTag());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
