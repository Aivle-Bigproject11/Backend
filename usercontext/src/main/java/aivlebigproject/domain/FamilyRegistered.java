package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class FamilyRegistered extends AbstractEvent {

    private Long id;
    private String loginId;
    private String loginPassword;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String memorialId;
    private Date createdAt;
    private Date updatedAt;

    public FamilyRegistered(Family aggregate) {
        super(aggregate);
    }

    public FamilyRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
