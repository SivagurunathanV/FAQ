import config.FAQConfiguration;
import dao.AdvTokenizedDataDao;
import dao.RawDataDao;
import dao.TokenizedDataDao;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.ICachingDictionary;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import helper.ModelHelper;
import helper.ParseHelper;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import model.AdvTokenizedData;
import model.RawData;
import model.TokenizedData;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.tartarus.snowball.ext.PorterStemmer;
import resource.AdvanceTokenResource;
import resource.DataResource;
import resource.SimilarityResource;
import resource.TokenResource;
import tokenizer.AdvanceWordTokenizer;
import helper.WordNetHelper;
import tokenizer.WordTokenizer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.net.URL;
import java.util.EnumSet;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class FAQApplication extends Application<FAQConfiguration> {

    public static void main(String[] args) throws Exception {
        new FAQApplication().run(args);
    }

    private final HibernateBundle<FAQConfiguration> hibernateBundle = new HibernateBundle<FAQConfiguration>(
            RawData.class,
            TokenizedData.class,
            AdvTokenizedData.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(FAQConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<FAQConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(FAQConfiguration configuration, Environment environment) throws Exception {

        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders","X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods","OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // url mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final String filePath = configuration.getFilePath();
        final RawDataDao rawDataDao = new RawDataDao(hibernateBundle.getSessionFactory());
        final TokenizedDataDao tokenizedDataDao = new TokenizedDataDao(hibernateBundle.getSessionFactory());
        final AdvTokenizedDataDao advTokenizedDataDao = new AdvTokenizedDataDao(hibernateBundle.getSessionFactory());

        final PorterStemmer stemmer = new PorterStemmer();
        final StanfordCoreNLP pipeline = new StanfordCoreNLP("pipeline.properties");
        final LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        final URL url = new URL("file",null, "/Users/sivagurunathanvelayutham/Documents/FAQ/dict");
        final ICachingDictionary dictionary = new Dictionary(url);
        final AdvanceWordTokenizer advanceWordTokenizer = new AdvanceWordTokenizer(pipeline);

        final ParseHelper parseHelper = new ParseHelper(pipeline, stemmer, lp);
        final WordNetHelper wordNetHelper = new WordNetHelper(dictionary);
        final HttpClient client = HttpClientBuilder.create().build();
        final ModelHelper modelHelper = new ModelHelper(client);

        environment.jersey().register(new DataResource(filePath, rawDataDao));
        environment.jersey().register(new TokenResource(new WordTokenizer(), rawDataDao, tokenizedDataDao));
        environment.jersey().register(new AdvanceTokenResource(advanceWordTokenizer, rawDataDao,
                advTokenizedDataDao, parseHelper, wordNetHelper));
        environment.jersey().register(new SimilarityResource(modelHelper, advTokenizedDataDao,
                parseHelper, advanceWordTokenizer));
    }
}
