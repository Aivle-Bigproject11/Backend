package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.domain.FuneralInfoRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "FuneralInfo_table")
@Data
//<<< DDD / Aggregate Root
public class FuneralInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @PostPersist
    public void onPostPersist() {
        FuneralInfoRegistered funeralInfoRegistered = new FuneralInfoRegistered(
            this
        );
        funeralInfoRegistered.publishAfterCommit();
    }

    public static FuneralInfoRepository repository() {
        FuneralInfoRepository funeralInfoRepository = FuneralcontextApplication.applicationContext.getBean(
            FuneralInfoRepository.class
        );
        return funeralInfoRepository;
    }

    //<<< Clean Arch / Port Method
    public void createObituary() {
        //implement business logic here:

        ObituaryCreationRequested obituaryCreationRequested = new ObituaryCreationRequested(
            this
        );
        obituaryCreationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void createDeathReport() {
        //implement business logic here:

        DeathReportCreationRequested deathReportCreationRequested = new DeathReportCreationRequested(
            this
        );
        deathReportCreationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void createSchedule() {
        //implement business logic here:

        ScheduleCreationRequested scheduleCreationRequested = new ScheduleCreationRequested(
            this
        );
        scheduleCreationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void createAllDocuments() {
        //implement business logic here:

        AllDocumentsCreationRequested allDocumentsCreationRequested = new AllDocumentsCreationRequested(
            this
        );
        allDocumentsCreationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void updateFuneralInfo() {
        //implement business logic here:

        FuneralInfoUpdated funeralInfoUpdated = new FuneralInfoUpdated(this);
        funeralInfoUpdated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
