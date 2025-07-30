package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DeathPredictedEvent extends AbstractEvent {

    private Long date;
    private String region;
    private Long predictedDeath;

    public DeathPredictedEvent(DeathPrediction aggregate) {
        super(aggregate);
    }

    public DeathPredictedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
