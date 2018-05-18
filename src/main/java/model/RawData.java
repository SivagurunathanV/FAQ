package model;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
@Entity
@Table(name = "raw_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class RawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Long id;

    private String questionType;

    private String asin;

    private String answerTime;

    private long unixTime;

    private String question;

    private String answerType;

    private String answer;

}
