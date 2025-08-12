package aivlebigproject.model;


import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "Photos")
@Data
//<<< DDD / Aggregate Root
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(name="memorial_id", columnDefinition = "BINARY(16)",  nullable = false)
    private UUID memorialId;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        uploadedAt = LocalDateTime.now();
    }

}
//>>> DDD / Aggregate Root
