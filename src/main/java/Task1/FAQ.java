package Task1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FAQ {

    @JsonProperty("questionType")
    private String questionType;

    @JsonProperty("asin")
    private String asin;

    @JsonProperty("answerTime")
    private String answerTime;

    @JsonProperty("unixTime")
    private long unixTime;

    @JsonProperty("question")
    private String question;

    @JsonProperty("answerType")
    private String answerType;

    @JsonProperty("answer")
    private String answer;
}
