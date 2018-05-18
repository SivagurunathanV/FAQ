package output_model;

import Task2.QAPair;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Getter
@Setter
public class TokenizeQAModel {
    @JsonProperty("qa_pair")
    private QAPair qaPair;

    @JsonProperty("tokens")
    private String tokens;

    @JsonProperty("unigram_prob")
    private HashMap<String, Double> map;

}
