package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.net.HostAndPort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class EsConfiguration {

    @JsonProperty
    @NotNull
    private List<HostAndPort> servers = Collections.emptyList();


    @JsonProperty
    @NotEmpty
    private String clusterName = "elastic";

}
