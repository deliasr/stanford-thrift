package org.ets.research.nlp.stanford_thrift.test;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.ets.research.nlp.stanford_thrift.tokenizer.StanfordTokenizerThrift;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by delia on 09/11/15.
 */
public class StanfordCoreNLPTokenizerTest {
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

            List<String> input = readFile(inputFilename);
            perform(client, input);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }

        tokenize();
    }

    private static void tokenize()
    {
        StanfordTokenizerThrift st = new StanfordTokenizerThrift();
        List<List<String>> tokens = st.tokenizeTextUsingTokenizer("RT " +
                "@kvngmerius_: fake eyelashes = " +
                "iWeave by Apple, Inc. #❤️❤️❤️\uD83D\uDE2D❤️❤️❤️ @Puss_n_gold");

        for (List<String> toks : tokens)
        {
            for (String s : toks) {
                System.out.print(s + "| ");
            }
        }
        System.out.println();

    }

    private static List<String> readFile(String inputFilename) {

        FileReader infile = null;
        List<String> tweets = new ArrayList<String>();

        try {
            infile = new FileReader(inputFilename);
            BufferedReader in = new BufferedReader(infile);
            while (in.ready()) {
                String sentence = in.readLine();
                tweets.add(sentence);

            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tweets;
    }

    private static void perform(StanfordCoreNLP.Client client, List<String>
            input) throws Exception
    {

        int idx = 0;
        int all = 0;

        while (true) {

            if (idx == input.size()) {
                idx = 0;
            }

            String sentence = input.get(idx);

            try {
                List<List<String>> tokens = client.tokenize_text(sentence);
                System.out.println(String.format("Tweet %d: %s", all,
                        sentence));
                System.out.print("Tokens:");
                for (List<String> toks : tokens)
                {
                    for (String s : toks) {
                        System.out.print(s + ", ");
                    }
                }
                System.out.println();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            idx++;
            all++;
        }
    }
}
