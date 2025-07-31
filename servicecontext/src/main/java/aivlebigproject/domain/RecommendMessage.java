package aivlebigproject.domain;


import aivlebigproject.domain.dto.FilterCriteria;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class RecommendMessage {

    @Id @GeneratedValue
    private Long id;

    private Long customerId;
    private Long serviceId1;
    private Long serviceId2;
    private Date createMessageDate;

    private String message;

    private String ageGroup;
    private String gender;
    private String disease;
    private String family;

    public RecommendMessage(String msg, FilterCriteria criteria) {
        this.message = msg;
        this.ageGroup = criteria.getAgeGroup();
        this.gender = criteria.getGender();
        this.disease = criteria.getDisease();
        this.family = criteria.getFamily();
    }
}

//>>> DDD / Aggregate Root
