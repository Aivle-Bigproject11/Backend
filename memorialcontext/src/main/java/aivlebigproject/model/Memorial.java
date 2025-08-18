package aivlebigproject.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Memorials")
@Data
public class Memorial {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "memorial_id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID memorialId;

    @Column(name="customer_id", nullable=false)
    private Long customerId;

    @Column(name="profile_image_url")
    private String profileImageUrl;

    @Column(name="name",  nullable=false)
    private String name;

    @Column(name="age",  nullable=false)
    private Integer age;

    @Column(name = "birth_date",  nullable = false)
    private LocalDate birthDate;

    @Column(name = "deceased_date", nullable = false)
    private LocalDate deceasedDate;

    @Column(name="gender",  nullable=false)
    private String gender;

    @Column(name = "tribute", columnDefinition = "TEXT")
    private String tribute;

    @Column(name = "tribute_generated_at")
    private LocalDateTime tributeGeneratedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    private List<Long> familyList = new ArrayList<>();

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateTribute(String newTribute) {
        this.tribute = newTribute;
        this.tributeGeneratedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfileImage(String url) {
        this.profileImageUrl = url;
        this.updatedAt = LocalDateTime.now();
    }

    public void addFamily(Long familyId) {
        this.familyList.add(familyId);
    }


}

