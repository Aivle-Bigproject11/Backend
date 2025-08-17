package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeathPredictionEvent extends AbstractEvent {
    private String date;
    private String region;
    private Long predictedDeaths;
    private Double growthRate; // 📌 Double로 변경
    private Double regionalPercentage; // 📌 Double로 변경
    private Long previousYearDeaths;
}