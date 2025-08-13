package aivlebigproject.domain;

import aivlebigproject.UsercontextApplication;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CustomerProfile_table")
@Data
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    @Column(unique = true)
    private String loginId; 
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private String job;
    private String address;
    private String gender;
    private Date birthDate;
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
}
//>>> DDD / Aggregate Root
