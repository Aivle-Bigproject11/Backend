package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class FamilyApproved extends AbstractEvent {

    private Long id;
    private String memorialId;
}
