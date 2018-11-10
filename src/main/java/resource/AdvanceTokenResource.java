package resource;

import Task3.AdvanceTokenizeService;
import Task3.GetAdvanceTokenService;
import com.codahale.metrics.annotation.Timed;
import dao.AdvTokenizedDataDao;
import dao.RawDataDao;
import helper.ParseHelper;
import io.dropwizard.hibernate.UnitOfWork;
import model.AdvTokenizedData;
import tokenizer.Tokenizer;
import helper.WordNetHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Path("/advTokenized/data")
public class AdvanceTokenResource {

    private AdvanceTokenizeService advanceTokenizeService;
    private GetAdvanceTokenService getAdvanceTokenService;

    public AdvanceTokenResource(Tokenizer tokenizer, RawDataDao rawDataDao, AdvTokenizedDataDao advTokenizedDataDao,
                                ParseHelper parseHelper, WordNetHelper wordNetHelper){
        this.advanceTokenizeService = new AdvanceTokenizeService(tokenizer, rawDataDao,
                wordNetHelper, parseHelper, advTokenizedDataDao);
        this.getAdvanceTokenService = new GetAdvanceTokenService(advTokenizedDataDao);
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addAdvanceTokens(){
        return this.advanceTokenizeService.invoke();
    }

    @GET
    @UnitOfWork
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdvTokenizedData> getAdvanceTokens(@QueryParam("start") int start, @QueryParam("end") int end){
        return this.getAdvanceTokenService
                .withParameters(start,end)
                .invoke(); // TODO change reponse object as different
    }
}
