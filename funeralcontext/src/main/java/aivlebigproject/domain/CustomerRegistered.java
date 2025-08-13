package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class CustomerRegistered extends AbstractEvent {

    private Long customerId;
    private String name;
    private Integer age;
    private List<String> disease;
    private String phone;
    private String job;
    private String address;
    private String gender;
    private Date birthDate;
    private Boolean hasChildren;
    private Boolean isMarried;
    private String rrn;
}
