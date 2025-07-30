package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;

import java.time.LocalDate;
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
    private LocalDate deceasedDate;
    private LocalDate deceasedBirthOfDate;
    private String deceasedGender;
}
