package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ObituaryDocumentGenerated extends AbstractEvent {

    private Long id;

    public ObituaryDocumentGenerated(FuneralPython aggregate) {
        super(aggregate);
    }

    public ObituaryDocumentGenerated() {
        super();
    }
}
//>>> DDD / Domain Event
