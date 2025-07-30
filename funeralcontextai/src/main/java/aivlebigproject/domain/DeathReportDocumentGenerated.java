package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DeathReportDocumentGenerated extends AbstractEvent {

    private Long id;

    public DeathReportDocumentGenerated(FuneralPython aggregate) {
        super(aggregate);
    }

    public DeathReportDocumentGenerated() {
        super();
    }
}
//>>> DDD / Domain Event
