package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AllDocumentsCreationRequested extends AbstractEvent {

    private Long funeralInfoId;
    private Long customerId;
    private String customerName;
    private String customerRrn;
    private String customerPhone;
    private String deceasedName;
    private Integer deceasedAge;
    private Date deceasedDate;
    private Date deceasedBirthOfDate;
    private String deceasedGender;
    private String deceasedNameHanja;
    private String deceasedRrn;
    private String deceasedReligion;
    private String deceasedRelationToHouseholdHead;
    private Date reportRegistrationDate;
    private String reporterName;
    private String reporterRrn;
    private String reporterRelationToDeceased;
    private String reporterAddress;
    private String reporterPhone;
    private String reporterEmail;
    private String submitterName;
    private String submitterRrn;
    private String funeralCompanyName;
    private String directorName;
    private String directorPhone;
    private String funeralHomeName;
    private String mortuaryInfo;
    private String funeralHomeAddress;
    private String funeralDuration;
    private String processionDateTime;
    private String burialSiteInfo;
    private String chiefMourners;
    private String templateKeyword;

    public AllDocumentsCreationRequested(FuneralInfo aggregate) {
        super(aggregate);
    }

    public AllDocumentsCreationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
