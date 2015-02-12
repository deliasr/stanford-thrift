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


package org.ets.research.nlp.stanford_thrift.tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StanfordTokenizerThrift 
{
	public StanfordTokenizerThrift()
	{
	}
	
	public String untokenizeSentence(List<String> sentenceTokens)
	{
		return PTBTokenizer.ptb2Text(sentenceTokens);
	}
	
	public List<List<String>> tokenizeText(String arbitraryText)
	{
		List<List<String>> tokenizedSentences = new ArrayList<List<String>>();
		
    	DocumentPreprocessor preprocess = new DocumentPreprocessor(new StringReader(arbitraryText));
    	Iterator<List<HasWord>> foundSentences = preprocess.iterator();
        int i = 0;
    	while (foundSentences.hasNext())
    	{
            System.out.println(String.format("sentence %d", i));
            i++;
    		List<HasWord> tokenizedSentence = foundSentences.next();
    		List<String> tokenizedSentenceAsListOfStrings = new ArrayList<String>();
    		for (HasWord w : tokenizedSentence)
    		{
    			tokenizedSentenceAsListOfStrings.add(w.word());
    		}
    		tokenizedSentences.add(tokenizedSentenceAsListOfStrings);
    	}
    	
    	return tokenizedSentences;
	}

    public List<List<String>> tokenizeTextUsingTokenizer(String arbitraryText)
    {

        TokenizerAnnotator ta = new TokenizerAnnotator();

        Tokenizer<CoreLabel> tokenizer = ta.getTokenizer(new StringReader
                (arbitraryText));
        
        List<CoreLabel> tokens = tokenizer.tokenize();
        
        List<List<String>> tokenizedSentences = new ArrayList<List<String>>();

        List<String> tokensList = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            String word = token.get(TextAnnotation.class);
            tokensList.add(word);
        }
        tokenizedSentences.add(tokensList);
        
        return tokenizedSentences;
    }
}
