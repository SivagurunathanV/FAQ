package output_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
@Data
@NoArgsConstructor
public class FormData {
    @JsonProperty("words")
    String words;
}
