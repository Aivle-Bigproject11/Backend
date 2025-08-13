package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerDeleted extends AbstractEvent {

    private String loginId;

    public CustomerDeleted(CustomerProfile aggregate) {
        super(aggregate);
        this.loginId = aggregate.getLoginId();
    }

    public CustomerDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
