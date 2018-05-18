package Task1;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.hibernate.UnitOfWork;
import model.RawData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class DatasourceService {

    private List<FAQ> faqList = new LinkedList<>();
    private List<RawData> rawDataList = new LinkedList<>();

    public List<FAQ> extractData(String path){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        try(BufferedReader bf = new BufferedReader(new FileReader(path))){
            String line;
            while((line = bf.readLine()) != null){
                FAQ faq = mapper.readValue(line, FAQ.class);
                faqList.add(faq);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return faqList;
    }

    public List<RawData> prepareData(String path){
        extractData(path);
        faqList.stream().forEach(faq -> {
            RawData rawData = new RawData();
            rawData.setQuestionType(faq.getQuestionType());
            rawData.setQuestion(faq.getQuestion());
            rawData.setAsin(faq.getAsin());
            rawData.setAnswerTime(faq.getAnswerTime());
            rawData.setUnixTime(faq.getUnixTime());
            rawData.setQuestion(faq.getQuestion());
            rawData.setAnswerType(faq.getAnswerType());
            rawData.setAnswer(faq.getAnswer());
            rawDataList.add(rawData);
        });
        return rawDataList;
    }
}
