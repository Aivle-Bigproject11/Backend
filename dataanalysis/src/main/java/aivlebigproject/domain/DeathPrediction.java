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
    private YearMonth date; // <-- YearMonth 타입으로 변경
    @Id
    private String region;

    private Long Deaths;

    public DeathPrediction() {}

    public DeathPrediction(YearMonth date, String region, Long Deaths) { // <-- YearMonth로 변경
        this.date = date;
        this.region = region;
        this.Deaths = Deaths;
    }
}
