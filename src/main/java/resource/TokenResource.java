package resource;

import Task2.AddTokenService;
import Task2.GetTokenService;
import Task2.QAPair;
import Task2.SearchTokenService;
import com.codahale.metrics.annotation.Timed;
import dao.RawDataDao;
import dao.TokenizedDataDao;
import io.dropwizard.hibernate.UnitOfWork;
import model.TokenizedData;
import tokenizer.Tokenizer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Path("/tokenized/data")
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {

    private GetTokenService getTokenService;
    private AddTokenService addTokenService;
    private SearchTokenService searchTokenService;

    public TokenResource(Tokenizer tokenizer,RawDataDao rawDataDao, TokenizedDataDao tokenizedDataDao){
        this.getTokenService = new GetTokenService(tokenizedDataDao);
        this.addTokenService = new AddTokenService(tokenizer,rawDataDao,tokenizedDataDao);
        this.searchTokenService = new SearchTokenService(tokenizedDataDao, tokenizer);
    }

    @PUT
    @UnitOfWork
    @Timed
    public Object addTokenizedData(){
        return this.addTokenService.invoke();
    }

    @GET
    @UnitOfWork
    @Timed
    public List<TokenizedData> getTokenizeData(){
      return getTokenService.invoke();
    }

    @Path("/match")
    @GET
    @UnitOfWork
    @Timed
    public List<QAPair> searchQuestion(@QueryParam("question") String question){
        return searchTokenService.withQuestion(question).invoke();
    }


}
