package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import aivlebigproject.domain.repository.VideoRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Video_table")
@Data
//<<< DDD / Aggregate Root
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID memorialId;

    private String videoTitle;

    private String videoUrl;

    private String keywords;

    private String status;

    private LocalDateTime createdAt=LocalDateTime.now();

    private LocalDateTime completedAt;

    @PostPersist
    public void onPostPersist() {
        RequestedVideo requestedVideo = new RequestedVideo(this);
        requestedVideo.publishAfterCommit();
    }

    public static VideoRepository repository() {
        VideoRepository videoRepository = MemorialcontextApplication.applicationContext.getBean(
            VideoRepository.class
        );
        return videoRepository;
    }

    public static void saveVideo(VideoCreated videoCreated) {
        repository().findById(videoCreated.getId()).ifPresent(video -> {
            video.setVideoUrl(videoCreated.getVideoUrl());
            video.setStatus("COMPLETED");
            video.setCompletedAt(LocalDateTime.now());
            repository().save(video);
        });
    }
}
//>>> DDD / Aggregate Root
