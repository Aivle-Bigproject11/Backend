package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import java.util.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FamilyApproved extends AbstractEvent {

    private Long id;
    private UUID memorialId;


    public FamilyApproved(Family aggregate) {
        super();
        this.id = aggregate.getId();
        this.memorialId = aggregate.getMemorialId();
    }

    public FamilyApproved() {
        super();
    }
}  
//>>> DDD / Domain Event
