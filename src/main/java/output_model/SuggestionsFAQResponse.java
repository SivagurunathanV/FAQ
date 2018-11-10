package output_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestionsFAQResponse {

    private String question;

    private String answer;

    private String wordnet_features;

}
