package Task2;

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
public class QAPair implements Comparable{

    @JsonProperty("question")
    private String question;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("score")
    private int score;

    @Override
    public int compareTo(Object o) {
        QAPair pair = null;
        if(o instanceof QAPair)
            pair = (QAPair) o;
        return Integer.compare(pair.getScore(), this.getScore());
    }
}
