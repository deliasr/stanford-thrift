package org.ets.research.nlp.stanford_thrift.test;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by delia on 06/11/15.
 */
public class StanfordCoreNLPTest {

    public static void main(String [] args) throws Exception {

        String fileName = "";

        if (args.length == 1) {
            fileName = args[0];
        }
        else {
            System.err.println("Usage: StanfordCoreNLPTest <inputFile>");
            System.exit(2);
        }

        FileReader inFile;
        List<String> tweets = new ArrayList<String>();

        try {
            inFile = new FileReader(fileName);
            BufferedReader in = new BufferedReader(inFile);

            while (in.ready()) {
                String tweet = in.readLine();
                tweets.add(tweet);
            }

            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        int idx = 0;
        int i = 0;

        while (true) {

            System.out.println("tweet " + new Integer(i).toString());

            if (idx == tweets.size()) {
                idx = 0;
            }

            String text = tweets.get(idx);

            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            List<String> tweetList = new ArrayList<String>();

            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : document.get(CoreAnnotations.TokensAnnotation
                    .class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                tweetList.add(word);
            }

            System.out.println(tweetList);

            idx ++;
            i++;
        }

    }
}
