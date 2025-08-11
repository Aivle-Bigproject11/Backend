package aivlebigproject.model;

import aivlebigproject.event.publisher.VideoRequested;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Videos")
@Data
//<<< DDD / Aggregate Root
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(name="memorial_id", columnDefinition = "BINARY(16)")
    private UUID memorialId;

    @Column(name="video_title")
    private String videoTitle;

    @Column(name="video_url")
    private String videoUrl;

    @Column(name="keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name="status")
    private String status;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="completed_at")
    private LocalDateTime completedAt;

//    @PostPersist
//    public void onPostPersist() {
//        VideoRequested videoRequested = new VideoRequested(this);
//        videoRequested.publishAfterCommit();
//    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        status = "REQUESTED";
    }
}
//>>> DDD / Aggregate Root
