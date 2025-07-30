package aivlebigproject.domain;

import aivlebigproject.UsercontextApplication;
import aivlebigproject.domain.FamilyRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Family_table")
@Data
//<<< DDD / Aggregate Root
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String loginId;

    private String loginPassword;

    private String name;

    private String email;

    private String phone;

    private String status;

    private String memorialId;

    private Date createdAt;

    private Date updatedAt;

    @PostPersist
    public void onPostPersist() {
        FamilyRegistered familyRegistered = new FamilyRegistered(this);
        familyRegistered.publishAfterCommit();
    }

    public static FamilyRepository repository() {
        FamilyRepository familyRepository = UsercontextApplication.applicationContext.getBean(
            FamilyRepository.class
        );
        return familyRepository;
    }

    //<<< Clean Arch / Port Method
    public void approveFamily(ApproveFamilyCommand approveFamilyCommand) {
        //implement business logic here:

        FamilyApproved familyApproved = new FamilyApproved(this);
        familyApproved.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
