package aivlebigproject.domain;


import aivlebigproject.infra.AbstractEvent;

import java.util.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

//<<< DDD / Domain Event
@Data
@ToString
public class CustomerRegistered extends AbstractEvent {

    private Long customerId;
    private String phone;
    private List<String> diseaseList;
    private Integer age;
    private String job;
    private String address;
    private String gender;
    private String email;
    private Boolean hasChildren;
    private Boolean isMarried;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    public CustomerRegistered(CustomerProfile aggregate) {
        super(aggregate);
    }

    public CustomerRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
