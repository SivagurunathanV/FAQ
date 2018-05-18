package Task4;

import Task3.AdvanceTokenizeService;
import action.Action;
import dao.AdvTokenizedDataDao;
import helper.ModelHelper;
import helper.ParseHelper;
import model.AdvTokenizedData;
import output_model.FormData;
import output_model.ModelResponse;
import output_model.SuggestionsFAQResponse;
import tokenizer.AdvanceWordTokenizer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class SimilarFAQService implements Action<List<SuggestionsFAQResponse>> {

    private ModelHelper modelHelper;
    private FormData formData;
    private AdvTokenizedDataDao advTokenizedDataDao;
    private ParseHelper parseHelper;
    private AdvanceWordTokenizer advanceWordTokenizer;

    public SimilarFAQService(ModelHelper modelHelper, AdvTokenizedDataDao advTokenizedDataDao, ParseHelper parseHelper,
                             AdvanceWordTokenizer advanceWordTokenizer){
        this.modelHelper = modelHelper;
        this.advTokenizedDataDao = advTokenizedDataDao;
        this.parseHelper = parseHelper;
        this.advanceWordTokenizer = advanceWordTokenizer;
    }

    public SimilarFAQService withParameters(FormData formData){
        this.formData = formData;
        List<String> tokens = advanceWordTokenizer.tokenize(formData.getWords());
        formData.setWords(String.join(",",parseHelper.doStemming(tokens)));
        return this;
    }

    @Override
    public List<SuggestionsFAQResponse> invoke() {
        List<SuggestionsFAQResponse> result = new LinkedList<>();
        List<AdvTokenizedData> advTokenizedDataList = new LinkedList<>();
        try {
            ModelResponse suggestionList = modelHelper.sendPost(formData);
            List<List<String>> pairSuggestion = suggestionList.getWords();
            pairSuggestion.stream().forEach( rowMatrix -> {
                StringBuffer stringBuffer = new StringBuffer();
                rowMatrix.forEach(colMatrix -> {
                    advTokenizedDataList.addAll(advTokenizedDataDao
                            .findQuestionByWordnetFeatures(colMatrix));
//                    stringBuffer.append(colMatrix + " ");
                });
                advTokenizedDataList.stream().forEach( advTokenizedData -> {
                    SuggestionsFAQResponse response = new SuggestionsFAQResponse();
                    response.setQuestion(advTokenizedData.getQuestion());
                    response.setAnswer(advTokenizedData.getAnswer());
                    response.setWordnet_features(advTokenizedData.getWordnet_features());
                    result.add(response);
                });
                advTokenizedDataList.clear();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
