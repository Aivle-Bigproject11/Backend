package aivlebigproject.event.listener;

import lombok.Data;


@Data
public class VideoCreated {
    private Long videoId;
    private String videoUrl;
}