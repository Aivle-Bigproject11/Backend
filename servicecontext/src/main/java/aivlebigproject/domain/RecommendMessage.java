package aivlebigproject.domain;


import aivlebigproject.domain.dto.FilterCriteria;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendMessage {
    @Id @GeneratedValue
    private Long id;

    private Long customerId;
    private Long serviceId1;
    private Long serviceId2;

    @Column(columnDefinition = "TEXT")
    private String imageUrl1;

    @Column(columnDefinition = "TEXT")
    private String imageUrl2;

    @Column(columnDefinition = "TEXT")
    private String detailedUrl1;

    @Column(columnDefinition = "TEXT")
    private String detailedUrl2;

    private LocalDateTime createMessageDate;

    @Column(columnDefinition = "TEXT")
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
