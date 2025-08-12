package aivlebigproject.model;


import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Comments")
@Data
//<<< DDD / Aggregate Root
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name="memorial_id", columnDefinition = "BINARY(16)", nullable=false)
    private UUID memorialId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="relationship", nullable = false)
    private String relationship;

    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
//>>> DDD / Aggregate Root
