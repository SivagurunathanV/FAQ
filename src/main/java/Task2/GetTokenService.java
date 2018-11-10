package Task2;

import action.Action;
import dao.TokenizedDataDao;
import model.TokenizedData;

import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class GetTokenService implements Action<List<TokenizedData>> {

    private TokenizedDataDao tokenizedDataDao;

    public GetTokenService(TokenizedDataDao tokenizedDataDao){
       this.tokenizedDataDao = tokenizedDataDao;
    }

    @Override
    public List<TokenizedData> invoke() {
        return tokenizedDataDao.findAll();
    }

}
