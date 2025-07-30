package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

@Data
@ToString
public class DeathPredictionEvent extends AbstractEvent {

    private LocalDate date;
    private String region;
    private Long previousYearDeaths; 

    public DeathPredictionEvent() {
        super();
    }

    public DeathPredictionEvent(LocalDate date, String region, Long previousYearDeaths) {
        super();
        this.date = date;
        this.region = region;
        this.previousYearDeaths = previousYearDeaths;
    }
}
