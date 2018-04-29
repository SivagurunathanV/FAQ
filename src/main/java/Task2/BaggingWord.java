package Task2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class BaggingWord {

    public static HashMap<String, Double> unigramProbability(List<String> words){
        HashMap<String, Double> map = new HashMap<>();
        words.forEach(word -> {
            map.merge(word, 1.0, Double::sum);
        });

        int size = map.size();
        map.forEach((k,v) -> {
            map.put(k,v/size);
        });
        return map;
    }
}
