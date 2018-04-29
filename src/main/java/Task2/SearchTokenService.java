package Task2;

import action.Action;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.TokenizedDataDao;
import model.TokenizedData;
import tokenizer.Tokenizer;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class SearchTokenService implements Action<List<QAPair>> {

    private TokenizedDataDao tokenizedDataDao;
    private Tokenizer tokenizer;
    private String question;
    private TreeMap<String, Double> expectedProbMap;

    public SearchTokenService(TokenizedDataDao tokenizedDataDao, Tokenizer tokenizer){
        this.tokenizedDataDao = tokenizedDataDao;
        this.tokenizer = tokenizer;
    }

    public SearchTokenService withQuestion(String question){
        this.question = question.trim();
        return this;
    }

    @Override
    public List<QAPair> invoke() {
        PriorityQueue<QAPair> qaPairPQ = new PriorityQueue<>();
        List<String> tokens = tokenizer.tokenize(this.question);
        HashMap<String, Double> actualProbMap = BaggingWord.unigramProbability(tokens);
        List<TokenizedData> tokenizedDataList = tokenizedDataDao.findAll();

        Type type = new TypeToken<TreeMap<String, Double>>(){}.getType();
        Gson gson = new Gson();

        Set<String> actualSet = actualProbMap.keySet();

        tokenizedDataList.stream().forEach(model -> {
            expectedProbMap = gson.fromJson(model.getProb(),type);
            Set<String> expWordSet = expectedProbMap.keySet();
            expWordSet.retainAll(actualSet);
            QAPair pair = new QAPair();
            pair.setQuestion(model.getQuestion());
            pair.setAnswer(model.getAnswer());
            pair.setScore(expWordSet.size());
            qaPairPQ.add(pair);
            qaPairPQ.stream().limit(11);
        });

        if(qaPairPQ.size() != 0)
            return qaPairPQ
                    .stream()
                    .limit(11)
                    .sorted((x,y) -> y.getScore()-x.getScore())
                    .collect(Collectors.toList());
        return null;
    }
}
