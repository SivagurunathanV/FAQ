package Task3;

import action.Action;
import com.google.gson.Gson;
import dao.AdvTokenizedDataDao;
import dao.RawDataDao;
import helper.ParseHelper;
import model.AdvTokenizedData;
import model.RawData;
import tokenizer.Tokenizer;
import helper.WordNetHelper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class AdvanceTokenizeService implements Action<Response> {

    private Tokenizer tokenizer;
    private RawDataDao rawDataDao;
    private WordNetHelper wordNetHelper;
    private ParseHelper parseHelper;
    private AdvTokenizedDataDao advTokenizedDataDao;
    public static final int PAGE_SIZE = 100;


    public AdvanceTokenizeService(Tokenizer tokenizer, RawDataDao rawDataDao, WordNetHelper wordNetHelper,
                                  ParseHelper parseHelper, AdvTokenizedDataDao advTokenizedDataDao){
        this.tokenizer = tokenizer;
        this.rawDataDao = rawDataDao;
        this.wordNetHelper = wordNetHelper;
        this.parseHelper = parseHelper;
        this.advTokenizedDataDao = advTokenizedDataDao;
    }

    @Override
    public Response invoke() {
        List<AdvTokenizedData> advTokenizedDataList = new LinkedList<>();
        List<RawData> rawDataList = rawDataDao.findByAnswerType("Y");
        rawDataList.stream().forEach( rawData -> {
                    List<String> tokens = tokenizer.tokenize(rawData.getQuestion());
                    AdvTokenizedData advTokenizedData = new AdvTokenizedData();
                    try {
                        advTokenizedData.setQuestion(rawData.getQuestion());
                        advTokenizedData.setAnswer(rawData.getAnswer());
                        buildAdvTokenizedData(tokens, advTokenizedData);
                        advTokenizedDataList.add(advTokenizedData);
                        if (advTokenizedDataList.size() == 100){
                            advTokenizedDataDao.batchInsert(advTokenizedDataList);
                            advTokenizedDataList.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        if(advTokenizedDataList.size() > 0)
            advTokenizedDataDao.batchInsert(advTokenizedDataList);
        return Response.ok().build();
    }

    public AdvTokenizedData buildAdvTokenizedData(List<String> tokens, AdvTokenizedData advTokenizedData) throws IOException {
        Map<String, String> posTagging = parseHelper.doPOSTagging(tokens);
        advTokenizedData.setLemma(new Gson().toJson(tokens));
        advTokenizedData.setStem(
                new Gson().toJson(parseHelper.doStemming(tokens)));
        advTokenizedData.setPos(
                new Gson().toJson(posTagging));
        advTokenizedData.setTree(
                new Gson().toJson(parseHelper.doDependencyParser(tokens)));
        advTokenizedData.setWordnet_features(
                new Gson().toJson(wordNetHelper.getWordToMeaningMap(posTagging)));
        return advTokenizedData;
    }
}
