package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.domain.ObituaryDataCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Obituary_table")
@Data
//<<< DDD / Aggregate Root
public class Obituary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long obituaryId;

    private Long funeralInfoId;

    private Long obituaryTemplateId;

    private String obituaryFilePath;

    private String obituaryFileUrl;

    private String obituaryStatus;

    private Date obituaryCreatedAt;

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

    public static ObituaryRepository repository() {
        ObituaryRepository obituaryRepository = FuneralcontextApplication.applicationContext.getBean(
            ObituaryRepository.class
        );
        return obituaryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void createObituaryData(
        ObituaryCreationRequested obituaryCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Obituary obituary = new Obituary();
        repository().save(obituary);

        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
        obituaryDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(obituaryCreationRequested.get???()).ifPresent(obituary->{
            
            obituary // do something
            repository().save(obituary);

            ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
            obituaryDataCreated.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createObituaryData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Obituary obituary = new Obituary();
        repository().save(obituary);

        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
        obituaryDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(allDocumentsCreationRequested.get???()).ifPresent(obituary->{
            
            obituary // do something
            repository().save(obituary);

            ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
            obituaryDataCreated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
