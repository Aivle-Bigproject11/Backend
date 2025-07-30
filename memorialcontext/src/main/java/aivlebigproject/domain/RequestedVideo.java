package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestedVideo extends AbstractEvent {

    private Long id;
    private String videoPath;
    private Date createdAt;

    public RequestedVideo(Video aggregate) {
        super(aggregate);
    }

    public RequestedVideo() {
        super();
    }
}
//>>> DDD / Domain Event
