package resource;

import Task4.SimilarFAQService;
import Task4.SimilarityService;
import com.codahale.metrics.annotation.Timed;
import dao.AdvTokenizedDataDao;
import helper.ModelHelper;
import helper.ParseHelper;
import io.dropwizard.hibernate.UnitOfWork;
import model.AdvTokenizedData;
import output_model.FormData;
import output_model.ModelResponse;
import output_model.SuggestionsFAQResponse;
import tokenizer.AdvanceWordTokenizer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
@Path("/similarity_service")
public class SimilarityResource {
    private SimilarityService similarityService;
    private SimilarFAQService similarFAQService;

    public SimilarityResource(ModelHelper modelHelper, AdvTokenizedDataDao advTokenizedDataDao,
                              ParseHelper parseHelper,AdvanceWordTokenizer advanceWordTokenizer){
        this.similarityService = new SimilarityService(modelHelper,advanceWordTokenizer, parseHelper);
        this.similarFAQService = new SimilarFAQService(modelHelper,advTokenizedDataDao, parseHelper, advanceWordTokenizer);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelResponse findSimilarity(FormData formData){
        return similarityService.withWords(formData).invoke();
    }

    @Path("/faq")
    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SuggestionsFAQResponse> findSimilarFAQ(FormData formData){
        return similarFAQService.withParameters(formData).invoke();
    }
}
