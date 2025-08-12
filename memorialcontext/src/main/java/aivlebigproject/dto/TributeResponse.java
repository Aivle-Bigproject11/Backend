package aivlebigproject.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TributeResponse {
    private UUID memorialId;
    private String tribute;
    private LocalDateTime tributeGeneratedAt;
}
