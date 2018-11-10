package tokenizer;

import com.google.common.io.CharStreams;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.ICachingDictionary;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.json.XML;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class AdvanceWordTokenizer implements Tokenizer {

    private static final CharArraySet STOP_WORD_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    private StanfordCoreNLP pipeline;

    public AdvanceWordTokenizer(StanfordCoreNLP pipeline){
        this.pipeline = pipeline;
    }

    @Override
    public List<String> tokenize(String sentence) {
        sentence = sentence.replaceAll("\\W"," ");

        Annotation annotation = new Annotation(sentence);
        pipeline.annotate(annotation);

        List<CoreLabel> tokensLabels = annotation.get(CoreAnnotations.TokensAnnotation.class);
        Set<String> tokens = new LinkedHashSet<>();
        tokensLabels.stream().forEach(tokensLabel -> tokens.add(tokensLabel.get(CoreAnnotations.LemmaAnnotation.class)));
        tokens.removeAll(STOP_WORD_SET);
        return tokens.stream().collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
//        AdvanceWordTokenizer advanceWordTokenizer = new AdvanceWordTokenizer();
//        String sentence = "Can you please tell me for each gallon size how many pounds of dog food they approximately hold?";
//        Reader reader = new StringReader(sentence);
//        List<String> list = advanceWordTokenizer.tokenize(reader);
//        System.out.println(advanceWordTokenizer.doStemming(list));
//        Map map = advanceWordTokenizer.doPOSTagging(list);
//        advanceWordTokenizer.doDependencyParser(map);
        URL url = new URL("file",null, "/Users/sivagurunathanvelayutham/Documents/FAQ/dict");
        ICachingDictionary dictionary = new Dictionary(url);
        dictionary.open();

        IIndexWord idWord = dictionary.getIndexWord("dog", POS.NOUN);
        IWordID word = idWord.getWordIDs().get(0);
        System.out.println(dictionary.getWord(word).getSynset().getGloss());
    }


}
