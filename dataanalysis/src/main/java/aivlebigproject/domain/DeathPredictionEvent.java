package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
// import java.time.YearMonth; // 이 라인은 삭제합니다.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeathPredictionEvent extends AbstractEvent {

    private String date; // YearMonth -> String으로 변경
    private String region;
    private Long predictedDeaths;
}