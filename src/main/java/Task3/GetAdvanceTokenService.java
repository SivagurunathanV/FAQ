package Task3;

import action.Action;
import dao.AdvTokenizedDataDao;
import model.AdvTokenizedData;

import javax.xml.ws.Response;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class GetAdvanceTokenService implements Action<List<AdvTokenizedData>> {
    private AdvTokenizedDataDao advTokenizedDataDao;
    private int start;
    private int end;

    public GetAdvanceTokenService(AdvTokenizedDataDao advTokenizedDataDao){
        this.advTokenizedDataDao = advTokenizedDataDao;
    }

    public GetAdvanceTokenService withParameters(int start, int end){
        this.start = start;
        this.end = end;
        return this;
    }

    @Override
    public List<AdvTokenizedData> invoke() {
        return advTokenizedDataDao.findAllWithPagination(start,end);
    }
}
