package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerDeleted extends AbstractEvent {

    private Long customerId;

    public CustomerDeleted(CustomerProfile aggregate) {
        super(aggregate);
        this.customerId = aggregate.getCustomerId();
    }

    public CustomerDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
