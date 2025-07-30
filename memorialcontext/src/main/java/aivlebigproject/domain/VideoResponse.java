package aivlebigproject.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResponse {
    private Long id;
    private String status;
    private String message;
}