package helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import output_model.FormData;
import output_model.ModelResponse;

import java.io.IOException;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class ModelHelper {

    private HttpClient client;
    public ModelHelper(HttpClient client){
        this.client = client;
    }

    public ModelResponse sendPost(FormData formData) throws IOException {
        String url = "http://localhost:5000/most_similar";
        HttpPost post = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(new Gson().toJson(formData));
        System.out.println(new Gson().toJson(formData));
        post.setEntity(stringEntity);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(post);
        System.out.println(response.getEntity());
        String str = EntityUtils.toString(response.getEntity(), "UTF-8").replaceAll("\t|\n|\r|\\s","");
        ModelResponse modelResponse = new ObjectMapper().readValue(str, ModelResponse.class);
        return modelResponse;
    }

    public static void main(String[] args) throws IOException {
//        ModelHelper modelHelper = new ModelHelper();
//        modelHelper.sendPost();
    }
}
