package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
@Entity
@Table(name = "adv_tokenized_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class AdvTokenizedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Long id;

    private String question;

    private String answer;

    private String lemma;

    private String stem;

    private String pos;

    private String tree;

    private String wordnet_features;

}
