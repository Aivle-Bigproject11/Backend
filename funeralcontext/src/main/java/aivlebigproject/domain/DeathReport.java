package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.domain.DeathReportDataCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DeathReport_table")
@Data
//<<< DDD / Aggregate Root
public class DeathReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deathReportId;

    private Long funeralInfoId;

    private Long deathReportTemplateId;

    private String deathReportFilePath;

    private String deathReportFileUrl;

    private String deathReportStatus;

    private Date deathReportCreatedAt;

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

    public static DeathReportRepository repository() {
        DeathReportRepository deathReportRepository = FuneralcontextApplication.applicationContext.getBean(
            DeathReportRepository.class
        );
        return deathReportRepository;
    }

    //<<< Clean Arch / Port Method
    public static void createDeathReportData(
        DeathReportCreationRequested deathReportCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        DeathReport deathReport = new DeathReport();
        repository().save(deathReport);

        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
        deathReportDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(deathReportCreationRequested.get???()).ifPresent(deathReport->{
            
            deathReport // do something
            repository().save(deathReport);

            DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
            deathReportDataCreated.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createDeathReportData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        DeathReport deathReport = new DeathReport();
        repository().save(deathReport);

        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
        deathReportDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(allDocumentsCreationRequested.get???()).ifPresent(deathReport->{
            
            deathReport // do something
            repository().save(deathReport);

            DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
            deathReportDataCreated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
