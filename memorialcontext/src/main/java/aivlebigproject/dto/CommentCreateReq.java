package aivlebigproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class CommentCreateReq {
    private String name;
    private UUID memorialId;
    private String relationship;
    private String content;
}
