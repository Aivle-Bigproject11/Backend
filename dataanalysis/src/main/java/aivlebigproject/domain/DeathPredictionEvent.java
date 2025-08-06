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
}