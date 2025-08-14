package aivlebigproject.domain;

import lombok.Data;
import java.util.UUID;

@Data
public class ApproveFamilyCommand {
    private UUID memorialId;
}