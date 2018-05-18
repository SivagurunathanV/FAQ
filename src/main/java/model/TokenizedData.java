package model;

import Task2.QAPair;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Entity
@Table(name = "tokenized_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class TokenizedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Long id;

    //@JsonProperty("qa_pair")
    private String question;

    private String answer;

    //@JsonProperty("tokens")
    private String tokens;

    //@JsonProperty("unigram_prob")
    private String prob;

}
