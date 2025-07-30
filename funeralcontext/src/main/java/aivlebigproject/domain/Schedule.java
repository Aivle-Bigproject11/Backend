package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.domain.ScheduleDataCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Schedule_table")
@Data
//<<< DDD / Aggregate Root
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scheduleId;

    private Long funeralInfoId;

    private Long scheduleTemplateId;

    private String scheduleFilePath;

    private String scheduleFileUrl;

    private String scheduleStatus;

    private Date scheduleCreatedAt;

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

    public static ScheduleRepository repository() {
        ScheduleRepository scheduleRepository = FuneralcontextApplication.applicationContext.getBean(
            ScheduleRepository.class
        );
        return scheduleRepository;
    }

    //<<< Clean Arch / Port Method
    public static void createScheduleData(
        ScheduleCreationRequested scheduleCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Schedule schedule = new Schedule();
        repository().save(schedule);

        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
        scheduleDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(scheduleCreationRequested.get???()).ifPresent(schedule->{
            
            schedule // do something
            repository().save(schedule);

            ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
            scheduleDataCreated.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createScheduleData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Schedule schedule = new Schedule();
        repository().save(schedule);

        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
        scheduleDataCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(allDocumentsCreationRequested.get???()).ifPresent(schedule->{
            
            schedule // do something
            repository().save(schedule);

            ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
            scheduleDataCreated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
