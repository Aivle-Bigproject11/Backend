package aivlebigproject.event.publisher;

import java.time.LocalDate;
import java.util.*;

import aivlebigproject.model.Video;
import lombok.*;

//<<< DDD / Domain Event
@Getter @Setter
@ToString
public class VideoRequested extends AbstractEvent {

    private Long videoId;
    private UUID memorialId;
    private String name;
    private LocalDate birthDate;
    private LocalDate deceasedDate;
    private String keywords;
    private Integer photoCount;
    private List<String> imageUrls;

    public VideoRequested(Video aggregate) {
        super(aggregate);
    }

    public VideoRequested() {
        super();
    }
}
//>>> DDD / Domain Event