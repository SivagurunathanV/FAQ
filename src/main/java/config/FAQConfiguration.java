package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Getter
@Setter
public class FAQConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String filePath;

    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory(){
        return dataSourceFactory;
    }

}
