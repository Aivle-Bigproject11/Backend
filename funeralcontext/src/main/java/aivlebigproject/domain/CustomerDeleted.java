package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class CustomerDeleted extends AbstractEvent {

    private Long customerId;
    private String name;
    private Integer age;
    private String phone;
    private String job;
    private String address;
    private String gender;
    private Date birthOfDate;
    private Boolean hasChildren;
    private Boolean isMarried;
    private String rrn;
}