package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CustomerDeleted extends AbstractEvent {

    private Long customerId;
    private String name;
    private String rrn;
    private Integer age;
    private String phone;
    private String job;
    private String address;
    private String gender;
    private Date birthOfDate;
    private Boolean hasChildren;
    private Boolean isMarried;
    private List<String> diseaseList;
    private List<String> diseaseList;

    public CustomerDeleted(CustomerProfile aggregate) {
        super(aggregate);
    }

    public CustomerDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
