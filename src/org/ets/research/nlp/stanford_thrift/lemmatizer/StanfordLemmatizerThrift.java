package org.ets.research.nlp.stanford_thrift.lemmatizer;

import CoreNLP.TaggedToken;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.MorphaAnnotator;
import org.ets.research.nlp.stanford_thrift.general.CoreNLPThriftUtil;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delia on 17/02/15.
 */
public class StanfordLemmatizerThrift {
    
    private MorphaAnnotator ma;
    
    public StanfordLemmatizerThrift() {
        ma = new MorphaAnnotator();
        
    }
    
    public List<TaggedToken> lemmatize_pos_tagged_tokens(List<TaggedToken>
                                                              tokens) {

        Annotation annotation = CoreNLPThriftUtil.getAnnotationFromPosTokens
                (tokens);
        
        ma.annotate(annotation);

        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
        // the output
        List<TaggedToken> taggedSentence = new ArrayList<TaggedToken>();

        for (CoreMap sent : sentences) {
            for (CoreLabel token: sent.get(TokensAnnotation.class)) {
                String lemma = token.get(LemmaAnnotation.class);
                String word = token.get(TextAnnotation.class);
                
                TaggedToken tokenLemma = new TaggedToken();
                tokenLemma.tag = lemma;
                tokenLemma.token = word;
                taggedSentence.add(tokenLemma);
            }
        }
        
        return taggedSentence;
    }
}
