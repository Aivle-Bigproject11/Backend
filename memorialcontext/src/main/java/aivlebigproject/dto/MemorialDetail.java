package aivlebigproject.dto;

import aivlebigproject.model.Comment;
import aivlebigproject.model.Photo;
import aivlebigproject.model.Video;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class MemorialDetail {
    private UUID memorialId;
    private String profileImageUrl;
    private String deceasedName;
    private Integer deceasedAge;
    private String gender;
    private LocalDate birthDate;
    private LocalDate deceasedDate;
    private String tribute;
    private LocalDateTime createdAt;

    private List<Photo> photos;
    private List<Video> videos;
    private List<Comment> comments;
}
