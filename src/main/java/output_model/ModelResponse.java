package output_model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
@Data
@NoArgsConstructor
public class ModelResponse {
    private List<List<String>> words;
}
