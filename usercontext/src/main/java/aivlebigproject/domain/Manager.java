package aivlebigproject.domain;

import aivlebigproject.UsercontextApplication;
import aivlebigproject.domain.ManagerRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Manager_table")
@Data
//<<< DDD / Aggregate Root
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String loginId;

    private String loginPassword;

    private String name;

    @Embedded
    private Email email;

    private String phone;

    @PostPersist
    public void onPostPersist() {
        ManagerRegistered managerRegistered = new ManagerRegistered(this);
        managerRegistered.publishAfterCommit();
    }

    public static ManagerRepository repository() {
        ManagerRepository managerRepository = UsercontextApplication.applicationContext.getBean(
            ManagerRepository.class
        );
        return managerRepository;
    }
}
//>>> DDD / Aggregate Root
