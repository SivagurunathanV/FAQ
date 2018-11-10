package tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public interface Tokenizer {
    public List<String> tokenize(String sentence);
}
