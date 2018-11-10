import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class ESClient {
    public static void main(String[] args) throws IOException {
//        TransportClient client1 = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("host1"),9300));

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost",9200,"http")));

        String json = "{\n" +
                "  \"settings\": {\n" +
                "    \"analysis\": {\n" +
                "      \"filter\": {\n" +
                "        \"english_stop\": {\n" +
                "          \"type\":       \"stop\",\n" +
                "          \"stopwords\":  \"_english_\" \n" +
                "        },\n" +
                "        \"english_keywords\": {\n" +
                "          \"type\":       \"keyword_marker\",\n" +
                "          \"keywords\":   [\"example\"] \n" +
                "        },\n" +
                "        \"english_stemmer\": {\n" +
                "          \"type\":       \"stemmer\",\n" +
                "          \"language\":   \"english\"\n" +
                "        },\n" +
                "        \"english_possessive_stemmer\": {\n" +
                "          \"type\":       \"stemmer\",\n" +
                "          \"language\":   \"possessive_english\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"my_analyzer\": {\n" +
                "        \"english\": {\n" +
                "          \"tokenizer\":  \"standard\",\n" +
                "          \"filter\": [\n" +
                "            \"english_possessive_stemmer\",\n" +
                "            \"lowercase\",\n" +
                "            \"english_stop\",\n" +
                "            \"english_keywords\",\n" +
                "            \"english_stemmer\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "\n";

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        Response response = client.getLowLevelClient().performRequest("PUT","/twitter", Collections.emptyMap(),entity);
        System.out.println(response.getStatusLine().getStatusCode());

        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user","siva")
                .field("post_date", new Date())
                .field("message","using api es1")
                .endObject();

        RestStatus response1 = client.index( new IndexRequest("twitter","tweet_test").source(builder)).status();
        System.out.println(response1.getStatus());
        client.close();
    }
}
