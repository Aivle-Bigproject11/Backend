package aivlebigproject.domain;

import aivlebigproject.DataanalysisApplication;
import aivlebigproject.domain.DeathPredictedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DeathPrediction_table")
@Data
//<<< DDD / Aggregate Root
public class DeathPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long date;

    private String region;

    private Long predictedDeath;

    @PostPersist
    public void onPostPersist() {
        DeathPredictedEvent deathPredictedEvent = new DeathPredictedEvent(this);
        deathPredictedEvent.publishAfterCommit();
    }

    public static DeathPredictionRepository repository() {
        DeathPredictionRepository deathPredictionRepository = DataanalysisApplication.applicationContext.getBean(
            DeathPredictionRepository.class
        );
        return deathPredictionRepository;
    }
}
//>>> DDD / Aggregate Root
