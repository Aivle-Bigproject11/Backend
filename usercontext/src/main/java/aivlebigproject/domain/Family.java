package aivlebigproject.domain;

import aivlebigproject.UsercontextApplication;
import aivlebigproject.domain.FamilyRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.*;
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

    private UUID memorialId;

    private Date createdAt;

    private Date updatedAt;

    public void approveFamily(ApproveFamilyCommand approveFamilyCommand) {
        // 비즈니스 로직: 유가족의 상태를 'APPROVED'로 변경합니다.
        this.setStatus("APPROVED");

        // 유가족 승인 완료 이벤트를 발행합니다.
        FamilyApproved familyApproved = new FamilyApproved(this);
        familyApproved.publishAfterCommit();
    }

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
