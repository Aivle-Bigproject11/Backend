package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import aivlebigproject.domain.repository.MemorialRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Memorial_table")
@Data
//<<< DDD / Aggregate Root
public class Memorial {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @ColumnDefault("random_uuid()")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID memorialId;

    private Long customerId;

    private String imageUrl;

    private String name;

    private Integer age;

    private LocalDate birthOfDate;

    private LocalDate deceasedDate;

    private String gender;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ElementCollection
    private List<Long> familyList;

    public static MemorialRepository repository() {
        MemorialRepository memorialRepository = MemorialcontextApplication.applicationContext.getBean(
            MemorialRepository.class
        );
        return memorialRepository;
    }


    public static void saveMemorial(
        FuneralInfoRegistered funeralInfoRegistered
    ) {
        Memorial memorial = new Memorial();
        memorial.setCustomerId(funeralInfoRegistered.getCustomerId());
        memorial.setName(funeralInfoRegistered.getDeceasedName());
        memorial.setAge(funeralInfoRegistered.getDeceasedAge());
        memorial.setBirthOfDate(funeralInfoRegistered.getDeceasedBirthOfDate());
        memorial.setDeceasedDate(funeralInfoRegistered.getDeceasedDate());
        memorial.setGender(funeralInfoRegistered.getDeceasedGender());
        repository().save(memorial);

    }


    public static void addFamily(FamilyApproved familyApproved) {

    }


}

