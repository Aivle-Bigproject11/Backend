package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class FuneralInfoRegistered extends AbstractEvent {

    private Long funeralInfoId;
    private Long customerId;
    private String customerName;
    private String deceasedName;
    private Integer deceasedAge;
    private Date deceasedDate;
    private Date deceasedBirthOfDate;
    private String deceasedGender;

    public FuneralInfoRegistered(FuneralInfo aggregate) {
        super(aggregate);
    }

    public FuneralInfoRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
