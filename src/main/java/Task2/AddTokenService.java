package Task2;

import action.Action;
import com.google.gson.Gson;
import dao.RawDataDao;
import dao.TokenizedDataDao;
import model.RawData;
import model.TokenizedData;
import output_model.TokenizeQAModel;
import tokenizer.Tokenizer;

import java.io.Reader;
import java.io.StringReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class AddTokenService implements Action {
    private TreeMap<QAPair, List<String>> tokenizePairMap;
    private Tokenizer tokenizer;
    private List<QAPair> qaPairList;
    private RawDataDao rawDataDao;
    private List<TokenizedData> tokenizedDataList;
    private TokenizedDataDao tokenizedDataDao;

    public AddTokenService(Tokenizer tokenizer, RawDataDao rawDataDao, TokenizedDataDao tokenizedDataDao){
        this.tokenizer = tokenizer;
        this.tokenizePairMap = new TreeMap<>(new SortQuestion());
        this.rawDataDao = rawDataDao;
        this.qaPairList = new LinkedList<>();
        this.tokenizedDataList = new LinkedList<>();
        this.tokenizedDataDao = tokenizedDataDao;
    }

    @Override
    public Object invoke() {
        List<TokenizeQAModel> tokenizeQAModelList = new LinkedList<>();
        List<RawData> rawData = rawDataDao.findAll();

        rawData.stream().forEach(data -> {
            QAPair qaPair = new QAPair();
            qaPair.setQuestion(data.getQuestion());
            qaPair.setAnswer(data.getAnswer());
            qaPairList.add(qaPair);
        });

        qaPairList.stream().forEach( pair -> {
            List<String> tokenizeWords = tokenizer.tokenize(pair.getQuestion());
            tokenizePairMap.putIfAbsent(pair, tokenizeWords);
        });

        tokenizePairMap.forEach((k,v) -> {
            TokenizeQAModel model = new TokenizeQAModel();
            model.setQaPair(k);
            model.setTokens(v.stream().map(Object::toString).collect(Collectors.joining(",")));
            model.setMap(BaggingWord.unigramProbability(v));
            tokenizeQAModelList.add(model);
        });

        Gson gson = new Gson();

        tokenizeQAModelList.stream().forEach(model -> {
            TokenizedData tokenizedData = new TokenizedData();
            tokenizedData.setQuestion(model.getQaPair().getQuestion());
            tokenizedData.setAnswer(model.getQaPair().getAnswer());
            tokenizedData.setTokens(model.getTokens());
            tokenizedData.setProb(gson.toJson(model.getMap()));
            tokenizedDataList.add(tokenizedData);
        });

        tokenizedDataDao.batchInsert(tokenizedDataList);
        return null;
    }

    public class SortQuestion implements Comparator<QAPair> {
        @Override
        public int compare(QAPair o1, QAPair o2) {
            return o1.getQuestion().compareTo(o2.getQuestion());
        }
    }
}
