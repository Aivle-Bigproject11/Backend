package aivlebigproject.domain;

import lombok.Data;
import java.util.UUID;

@Data
public class ApproveFamilyCommand {
    private Long familyId; // 승인할 유가족의 ID 추가
    private UUID memorialId;
}