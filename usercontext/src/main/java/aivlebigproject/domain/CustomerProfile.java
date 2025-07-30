package aivlebigproject.domain;

import aivlebigproject.UsercontextApplication;
import aivlebigproject.domain.CustomerDeleted;
import aivlebigproject.domain.CustomerRegistered;
import aivlebigproject.domain.CustomerUpdated;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CustomerProfile_table")
@Data
//<<< DDD / Aggregate Root
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ElementCollection
    private List<String> diseaseList;

    private String rrn;

    @PostPersist
    public void onPostPersist() {
        CustomerRegistered customerRegistered = new CustomerRegistered(this);
        customerRegistered.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        CustomerUpdated customerUpdated = new CustomerUpdated(this);
        customerUpdated.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        CustomerDeleted customerDeleted = new CustomerDeleted(this);
        customerDeleted.publishAfterCommit();
    }

    public static CustomerProfileRepository repository() {
        CustomerProfileRepository customerProfileRepository = UsercontextApplication.applicationContext.getBean(
            CustomerProfileRepository.class
        );
        return customerProfileRepository;
    }
}
//>>> DDD / Aggregate Root
