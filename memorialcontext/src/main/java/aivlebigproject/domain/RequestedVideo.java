package aivlebigproject.domain;

import java.time.LocalDateTime;
import java.util.*;

import aivlebigproject.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestedVideo extends AbstractEvent {

    private Long id;
    private String videoPath;
    private LocalDateTime createdAt;
    private List<String> imageUrls;

    public RequestedVideo(Video aggregate) {
        super(aggregate);
    }

    public RequestedVideo() {
        super();
    }
}
//>>> DDD / Domain Event
