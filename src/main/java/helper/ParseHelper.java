package helper;

import com.google.gson.Gson;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import org.json.XML;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class ParseHelper {

    PorterStemmer stemmer;
    StanfordCoreNLP pipeline;
    LexicalizedParser lp;

    public ParseHelper(StanfordCoreNLP pipeline, PorterStemmer stemmer, LexicalizedParser lp){
        this.pipeline = pipeline;
        this.stemmer = stemmer;
        this.lp = lp;
    }

    public List<String> doStemming(List<String> tokens){
        List<String> stemList = new LinkedList<>();
        tokens.stream().forEach(word -> {
            stemmer.setCurrent(word);
            stemmer.stem();
            stemList.add(stemmer.getCurrent());
        });
        return stemList;
    }

    public Map<String, String> doPOSTagging(List<String> tokens){
        String wordStr = tokens.stream().map(word -> word.toString()).collect(Collectors.joining(" "));
        Annotation annotation = new Annotation(wordStr);
        pipeline.annotate(annotation);
        Map<String, String> map = new LinkedHashMap<>();
        for(CoreLabel label : annotation.get(CoreAnnotations.TokensAnnotation.class)){
            String word = label.get(CoreAnnotations.TextAnnotation.class);
            String pos = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            map.putIfAbsent(word, pos);
        }
        return map;
    }

    public String doDependencyParser(List<String> tokens) throws IOException {
        Map<String, String> map = doPOSTagging(tokens);
        List<TaggedWord> words = new LinkedList<>();
        map.forEach((word, tag) -> {
            words.add(new TaggedWord(word, tag));
        });
        Tree tree = lp.parse(words);
        try(PrintWriter pw = new PrintWriter("temp_tree_op")) {
            tree.indentedXMLPrint(pw, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        try(BufferedReader bf = new BufferedReader(new FileReader("temp_tree_op"))) {
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        }
        String treeJson = XML.toJSONObject(sb.toString()).toString();
//        System.out.println(treeJson);
        Files.deleteIfExists(Paths.get("temp_tree_op"));
        return treeJson;
    }

}
