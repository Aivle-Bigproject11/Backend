package aivlebigproject.domain;

import aivlebigproject.infra.AbstractEvent;

import java.util.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CustomerRegistered extends AbstractEvent {

    private Long customerId;
    private Long userId;
    private String name;
    private String phone;
    private List<String> diseaseList;
    private Integer age;
    private String job;
    private String address;
    private String gender;
    private Boolean hasChildren;
    private Boolean isMarried;

    private Date birthDate;
}