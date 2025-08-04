package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import java.time.YearMonth; // LocalDate -> YearMonth로 변경
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeathPredictionEvent extends AbstractEvent {

    private YearMonth date; // <-- YearMonth 타입으로 변경
    private String region;
    private Long predictedDeaths;
}