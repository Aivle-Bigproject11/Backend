package aivlebigproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class MemorialProfileResponse {
    private UUID memorialId;
    private String photoUrl;
}
