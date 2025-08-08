package aivlebigproject.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class PhotoUploadResponse {
    private Long photoId;
    private UUID memorialId;
    private String title;
    private String description;
    private String photoUrl;
    private LocalDateTime uploadedAt;

}
