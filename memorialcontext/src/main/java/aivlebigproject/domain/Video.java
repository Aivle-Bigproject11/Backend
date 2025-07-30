package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import aivlebigproject.domain.RequestedVideo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Video_table")
@Data
//<<< DDD / Aggregate Root
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long videoId;

    private String memorialId;

    private String videoTitle;

    private String videoUrl;

    private String keywords;

    private String status;

    private Date createdAt;

    private Date completedAt;

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
}
//>>> DDD / Aggregate Root
