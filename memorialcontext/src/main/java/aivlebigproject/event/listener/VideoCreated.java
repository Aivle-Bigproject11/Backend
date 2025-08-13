package aivlebigproject.event.listener;

import aivlebigproject.event.publisher.AbstractEvent;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class VideoCreated extends AbstractEvent {
    private Long videoId;
    private String videoUrl;
}