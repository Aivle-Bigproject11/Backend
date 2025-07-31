package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;

import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class UpdatedMessage extends AbstractEvent {

    private Long messageId;
    private String comment;
    private Long serviceId1;
    private Long serviceId2;
    private Long customerId;
    private Date createMessageDate;

    public UpdatedMessage(RecommendMessage aggregate) {
        super(aggregate);
    }

    public UpdatedMessage() {
        super();
    }
}
//>>> DDD / Domain Event