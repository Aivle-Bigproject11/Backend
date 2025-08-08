package aivlebigproject.event.listener;

import aivlebigproject.event.publisher.AbstractEvent;

import java.time.LocalDate;

import lombok.*;

@Data
@ToString
public class FuneralInfoRegistered extends AbstractEvent {

    private Long funeralInfoId;
    private Long customerId;
    private String customerName;//왜있지??
    private String deceasedName;//위에랑 두개가 똑같은 거 아닌가
    private Integer deceasedAge;
    private LocalDate deceasedDate;
    private LocalDate deceasedBirthOfDate;
    private String deceasedGender;
}
