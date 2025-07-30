package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.util.*;
import lombok.*;

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
}
