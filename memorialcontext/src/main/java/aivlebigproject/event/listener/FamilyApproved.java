package aivlebigproject.event.listener;

import aivlebigproject.event.publisher.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class FamilyApproved extends AbstractEvent {

    private Long id;
    private UUID memorialId;
}
