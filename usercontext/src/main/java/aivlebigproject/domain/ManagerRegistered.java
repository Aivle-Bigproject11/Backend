package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ManagerRegistered extends AbstractEvent {

    private Long id;

    public ManagerRegistered(Manager aggregate) {
        super(aggregate);
    }

    public ManagerRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
