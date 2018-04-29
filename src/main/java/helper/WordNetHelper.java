package helper;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.ICachingDictionary;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.tartarus.snowball.ext.PorterStemmer;
import tokenizer.AdvanceWordTokenizer;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class WordNetHelper {

    private ICachingDictionary dictionary;

    public WordNetHelper(ICachingDictionary dictionary){
        this.dictionary = dictionary;
    }

    public enum GLOSS{
        SYNONYMS,
        HYPERNYMS,
        HYPONYMS,
        MERNOMYS,
        HOLONYMS
    }

    public ISynset getSynset(String word, POS tag){
        IIndexWord idWord = dictionary.getIndexWord(word, tag);
        if(idWord != null) {
            IWordID iWordID = idWord.getWordIDs().get(0);
            ISynset synset = dictionary.getWord(iWordID).getSynset();
            return synset;
        }
        return null;
    }

    public List<String> getSynomys(String word, POS tag){
        ISynset synset = getSynset(word, tag);
        List<String> synonmys = new LinkedList<>();
        if(synset != null){
            for(IWord w : synset.getWords()){
                synonmys.add(w.getLemma());
            }
        }
        return synonmys;
    }

    public List<String> getHypernyms(String word, POS tag){
        ISynset synset = getSynset(word, tag);
        if(synset != null) {
            List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
            List<String> hypernymsList = new LinkedList<>();
            hypernyms.forEach(sid -> {
                List<IWord> words = dictionary.getSynset(sid).getWords();
                words.forEach(w -> {
                    hypernymsList.add(w.getLemma());
                });
            });
            return hypernymsList;
        }
        return null;
    }

    public Map<String,List<String>> wordnetMapping(String word, POS tag) {
        Map<String, List<String>> wordnet = new LinkedHashMap<>();

        wordnet.put(GLOSS.SYNONYMS.name(), getSynomys(word, tag));
        wordnet.put(GLOSS.HYPERNYMS.name(), getHypernyms(word, tag));

        return wordnet;
    }

    public Map<String, Map<String, List<String>>> getWordToMeaningMap(Map<String, String> wordToTag) throws IOException {
        Map<String, Map<String, List<String>>> wordToMeaning = new LinkedHashMap<>();
        this.dictionary.open();
        wordToTag.forEach( (word, tag) -> {
            POS pos = POS.getPartOfSpeech(tag.charAt(0));
            if(pos != null)
                wordToMeaning.putIfAbsent(word, wordnetMapping(word,pos));
        });
        return wordToMeaning;
    }

    public static void main(String[] args) throws IOException {

        final PorterStemmer stemmer = new PorterStemmer();
        final StanfordCoreNLP pipeline = new StanfordCoreNLP("pipeline.properties");
        final LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        final URL url = new URL("file",null, "/Users/sivagurunathanvelayutham/Documents/FAQ/dict");
        final ICachingDictionary dictionary = new Dictionary(url);


        final ParseHelper parseHelper = new ParseHelper(pipeline, stemmer, lp);
        final WordNetHelper wordNetHelper = new WordNetHelper(dictionary);
        final AdvanceWordTokenizer advanceWordTokenizer = new AdvanceWordTokenizer(pipeline);
        List<String> tokens = advanceWordTokenizer.tokenize("This city needs a better class of criminals" +
                ", and here I am to :P");
        Map<String,String> posTagging = parseHelper.doPOSTagging(tokens);
        wordNetHelper.getWordToMeaningMap(posTagging);
    }

}
