package aivlebigprojectminseo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResponse {
    private Long videoId;
    private String status;
    private String message;
}