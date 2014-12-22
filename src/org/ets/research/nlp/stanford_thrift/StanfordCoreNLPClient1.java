package org.ets.research.nlp.stanford_thrift;
// Generated code
import CoreNLP.ParseTree;
import CoreNLP.StanfordCoreNLP;
import CoreNLP.TaggedToken;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.ets.research.nlp.stanford_thrift.general.CoreNLPThriftUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StanfordCoreNLPClient1 {

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
            while (in.ready()) {
                String sentence = in.readLine();

                // parse tree
                List<ParseTree> trees = client.parse_text(sentence, null);
                for (ParseTree tree : trees)
                {
                    System.out.println(tree.tree);
                }

                List<TaggedWord> pTaggedSentence = CoreNLPThriftUtil
                        .getListOfTaggedWordsFromTaggedSentence
                                (sentence, "_");

                String taggerModel =
                        "stanford-corenlp-3.5.0-models/" +
                                "edu/stanford/nlp/models/pos-tagger/english" +
                                "-left3words/english-left3words-distsim.tagger";

                MaxentTagger tagger = new MaxentTagger(taggerModel);
                List<TaggedWord> outputFromTagger = tagger.tagSentence(pTaggedSentence,
                        true);
                List<TaggedToken> taggedSentence = new ArrayList<TaggedToken>();
                for (TaggedWord tw : outputFromTagger)
                {
                    TaggedToken token = new TaggedToken();
                    token.tag = tw.tag();
                    token.token = tw.word();
                    taggedSentence.add(token);
                    System.out.println(token);
                }

                /*
                // partial tagged text
                List<TaggedToken> tokens = client.tag_partially_tagged_tokenized_sentence(sentence, "_");

                for (TaggedToken tok : tokens)
                {
                    System.out.println(tok.getToken() + " " + tok.getTag());
                }

                */
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
