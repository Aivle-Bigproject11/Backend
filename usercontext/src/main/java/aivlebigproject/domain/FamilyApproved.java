package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class FamilyApproved extends AbstractEvent {

    private Long id;
    private String memorialId;

    public FamilyApproved(Family aggregate) {
        super(aggregate);
    }

    public FamilyApproved() {
        super();
    }
}
//>>> DDD / Domain Event
