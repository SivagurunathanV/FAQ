package resource;

import Task1.DatasourceService;
import Task1.FAQ;
import com.codahale.metrics.annotation.Timed;
import dao.RawDataDao;
import io.dropwizard.hibernate.UnitOfWork;
import model.RawData;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {

    private RawDataDao rawDataDao;

    private String path;

    private DatasourceService datasourceService;

    public DataResource(String path, RawDataDao rawDataDao){
        this.path = path;
        this.rawDataDao = rawDataDao;
        this.datasourceService  = new DatasourceService();
    }

    @Path("/sample_data")
    @GET
    @Timed
    @UnitOfWork
    public List<RawData> getSampleData(){
        return rawDataDao.findAll();
    }

    @Path("/bulk_insert")
    @PUT
    @UnitOfWork
    public void insertData(){
        List<RawData> rawDataList = datasourceService.prepareData(path);
        rawDataDao.batchInsert(rawDataList);
    }



}
