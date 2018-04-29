package Task4;

import action.Action;
import helper.ModelHelper;
import helper.ParseHelper;
import output_model.FormData;
import output_model.ModelResponse;
import tokenizer.AdvanceWordTokenizer;

import java.io.IOException;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class SimilarityService implements Action<ModelResponse> {

    private ModelHelper modelHelper;
    private FormData formData;
    private AdvanceWordTokenizer advanceWordTokenizer;
    private ParseHelper parseHelper;

    public SimilarityService(ModelHelper modelHelper, AdvanceWordTokenizer advanceWordTokenizer,
                             ParseHelper parseHelper){
        this.modelHelper = modelHelper;
        this.advanceWordTokenizer = advanceWordTokenizer;
        this.parseHelper = parseHelper;
    }

    public SimilarityService withWords(FormData formData){
        this.formData = formData;
        List<String> tokens = advanceWordTokenizer.tokenize(formData.getWords());
        formData.setWords(String.join(",",parseHelper.doStemming(tokens)));
        return this;
    }

    @Override
    public ModelResponse invoke(){
        try {
            return modelHelper.sendPost(this.formData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
