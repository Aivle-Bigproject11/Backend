package aivlebigproject.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentResponse {
    private Long commentId;
    private UUID memorialId;
    private String name;
    private String relationship;
    private String content;
    private LocalDateTime createdAt;
}
