package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CustomerUpdated extends AbstractEvent {

    private Long customerId;
    private String phone;
    private String name;
    private Integer age;
    private String job;
    private String address;
    private String gender;
    private Boolean hasChildren;
    private Boolean isMarried;
    private List<String> diseaseList;
    private Date birthOfDate;
    private String rrn;
    private Date birthOfDate;
    private String rrn;

    public CustomerUpdated(CustomerProfile aggregate) {
        super(aggregate);
    }

    public CustomerUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
