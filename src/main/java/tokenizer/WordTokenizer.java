package tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class WordTokenizer implements Tokenizer {

    public static void main(String[] args) {
        String sentence = "Are these containers BPA free?";
        Reader reader = new StringReader(sentence);
        List<String> wordList = new LinkedList<>();

        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(reader,
                new CoreLabelTokenFactory(), "");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            System.out.println(label);
        }
    }

    @Override
    public List<String> tokenize(String sentence) {
        Reader reader = new StringReader(sentence);
        List<String> stringList = new LinkedList<>();
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(reader,
                new CoreLabelTokenFactory(), "");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            stringList.add(label.toString());
        }
        stringList.replaceAll(String::toLowerCase);
        return stringList;
    }
}
