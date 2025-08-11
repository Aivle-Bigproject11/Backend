package aivlebigproject.domain.dto;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class SavePreviewMessageCommand {
    private String message;

    private Long serviceId1;
    private Long serviceId2;

    private Long customerId;

    private String imageUrl1;
    private String imageUrl2;

    private String detailedUrl1;
    private String detailedUrl2;

//    private LocalDateTime createOfMessage;

    private FilterCriteria filterCriteria;
}