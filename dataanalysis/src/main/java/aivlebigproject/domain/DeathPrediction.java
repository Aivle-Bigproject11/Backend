package aivlebigproject.domain;

import java.time.YearMonth; // LocalDate -> YearMonth로 변경
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DeathPrediction_table")
@Data
@IdClass(DeathPredictionId.class)
public class DeathPrediction {

    @Id
    private String date;; 
    @Id
    private String region;

    private Long Deaths;

    public DeathPrediction() {}

    public DeathPrediction(String date, String region, Long Deaths) { 
        this.date = date;
        this.region = region;
        this.Deaths = Deaths;
    }
}
