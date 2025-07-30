package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GeneratedMessage extends AbstractEvent {

    private Long messageId;
    private String comment;
    private Long serviceId1;
    private Long serviceId2;
    private Long customerId;
    private Date createMessageDate;

    public GeneratedMessage(RecommendMessage aggregate) {
        super(aggregate);
    }

    public GeneratedMessage() {
        super();
    }
}
//>>> DDD / Domain Event
