// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/Schedule.java
// 역할 : 
// ========================================

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        // 1. 새로운 Schedule 객체 생성
        Schedule schedule = new Schedule();

        // 2. 이벤트로 받은 모든 데이터를 새로 만든 Schedule 객체에 채워넣기
        schedule.setFuneralInfoId(scheduleCreationRequested.getFuneralInfoId());
        schedule.setCustomerId(scheduleCreationRequested.getCustomerId());
        schedule.setCustomerName(scheduleCreationRequested.getCustomerName());
        schedule.setCustomerRrn(scheduleCreationRequested.getCustomerRrn());
        schedule.setCustomerPhone(scheduleCreationRequested.getCustomerPhone());
        schedule.setDeceasedName(scheduleCreationRequested.getDeceasedName());
        schedule.setDeceasedNameHanja(scheduleCreationRequested.getDeceasedNameHanja());
        schedule.setDeceasedRrn(scheduleCreationRequested.getDeceasedRrn());
        schedule.setDeceasedAge(scheduleCreationRequested.getDeceasedAge());
        schedule.setDeceasedDate(scheduleCreationRequested.getDeceasedDate());
        schedule.setDeceasedBirthOfDate(scheduleCreationRequested.getDeceasedBirthOfDate());
        schedule.setDeceasedGender(scheduleCreationRequested.getDeceasedGender());
        schedule.setDeceasedReligion(scheduleCreationRequested.getDeceasedReligion());
        schedule.setDeceasedRelationToHouseholdHead(scheduleCreationRequested.getDeceasedRelationToHouseholdHead());
        schedule.setReportRegistrationDate(scheduleCreationRequested.getReportRegistrationDate());
        schedule.setReporterName(scheduleCreationRequested.getReporterName());
        schedule.setReporterRrn(scheduleCreationRequested.getReporterRrn());
        schedule.setReporterRelationToDeceased(scheduleCreationRequested.getReporterRelationToDeceased());
        schedule.setReporterAddress(scheduleCreationRequested.getReporterAddress());
        schedule.setReporterPhone(scheduleCreationRequested.getReporterPhone());
        schedule.setReporterEmail(scheduleCreationRequested.getReporterEmail());
        schedule.setSubmitterName(scheduleCreationRequested.getSubmitterName());
        schedule.setSubmitterRrn(scheduleCreationRequested.getSubmitterRrn());
        schedule.setFuneralCompanyName(scheduleCreationRequested.getFuneralCompanyName());
        schedule.setDirectorName(scheduleCreationRequested.getDirectorName());
        schedule.setDirectorPhone(scheduleCreationRequested.getDirectorPhone());
        schedule.setFuneralHomeName(scheduleCreationRequested.getFuneralHomeName());
        schedule.setMortuaryInfo(scheduleCreationRequested.getMortuaryInfo());
        schedule.setFuneralHomeAddress(scheduleCreationRequested.getFuneralHomeAddress());
        schedule.setFuneralDuration(scheduleCreationRequested.getFuneralDuration());
        schedule.setProcessionDateTime(scheduleCreationRequested.getProcessionDateTime());
        schedule.setBurialSiteInfo(scheduleCreationRequested.getBurialSiteInfo());
        schedule.setChiefMourners(scheduleCreationRequested.getChiefMourners());
        schedule.setTemplateKeyword(scheduleCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        schedule.setScheduleStatus("PENDING");
        schedule.setScheduleCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 scheduleId가 자동으로 생성됨)
        repository().save(schedule);

        // 4. Schedule 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
        scheduleDataCreated.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createScheduleData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        // 1. 새로운 Schedule 객체 생성
        Schedule schedule = new Schedule();

        // 2. 이벤트로 받은 모든 데이터를 새로 만든 Schedule 객체에 채워넣기
        schedule.setFuneralInfoId(allDocumentsCreationRequested.getFuneralInfoId());
        schedule.setCustomerId(allDocumentsCreationRequested.getCustomerId());
        schedule.setCustomerName(allDocumentsCreationRequested.getCustomerName());
        schedule.setCustomerRrn(allDocumentsCreationRequested.getCustomerRrn());
        schedule.setCustomerPhone(allDocumentsCreationRequested.getCustomerPhone());
        schedule.setDeceasedName(allDocumentsCreationRequested.getDeceasedName());
        schedule.setDeceasedNameHanja(allDocumentsCreationRequested.getDeceasedNameHanja());
        schedule.setDeceasedRrn(allDocumentsCreationRequested.getDeceasedRrn());
        schedule.setDeceasedAge(allDocumentsCreationRequested.getDeceasedAge());
        schedule.setDeceasedDate(allDocumentsCreationRequested.getDeceasedDate());
        schedule.setDeceasedBirthOfDate(allDocumentsCreationRequested.getDeceasedBirthOfDate());
        schedule.setDeceasedGender(allDocumentsCreationRequested.getDeceasedGender());
        schedule.setDeceasedReligion(allDocumentsCreationRequested.getDeceasedReligion());
        schedule.setDeceasedRelationToHouseholdHead(allDocumentsCreationRequested.getDeceasedRelationToHouseholdHead());
        schedule.setReportRegistrationDate(allDocumentsCreationRequested.getReportRegistrationDate());
        schedule.setReporterName(allDocumentsCreationRequested.getReporterName());
        schedule.setReporterRrn(allDocumentsCreationRequested.getReporterRrn());
        schedule.setReporterRelationToDeceased(allDocumentsCreationRequested.getReporterRelationToDeceased());
        schedule.setReporterAddress(allDocumentsCreationRequested.getReporterAddress());
        schedule.setReporterPhone(allDocumentsCreationRequested.getReporterPhone());
        schedule.setReporterEmail(allDocumentsCreationRequested.getReporterEmail());
        schedule.setSubmitterName(allDocumentsCreationRequested.getSubmitterName());
        schedule.setSubmitterRrn(allDocumentsCreationRequested.getSubmitterRrn());
        schedule.setFuneralCompanyName(allDocumentsCreationRequested.getFuneralCompanyName());
        schedule.setDirectorName(allDocumentsCreationRequested.getDirectorName());
        schedule.setDirectorPhone(allDocumentsCreationRequested.getDirectorPhone());
        schedule.setFuneralHomeName(allDocumentsCreationRequested.getFuneralHomeName());
        schedule.setMortuaryInfo(allDocumentsCreationRequested.getMortuaryInfo());
        schedule.setFuneralHomeAddress(allDocumentsCreationRequested.getFuneralHomeAddress());
        schedule.setFuneralDuration(allDocumentsCreationRequested.getFuneralDuration());
        schedule.setProcessionDateTime(allDocumentsCreationRequested.getProcessionDateTime());
        schedule.setBurialSiteInfo(allDocumentsCreationRequested.getBurialSiteInfo());
        schedule.setChiefMourners(allDocumentsCreationRequested.getChiefMourners());
        schedule.setTemplateKeyword(allDocumentsCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        schedule.setScheduleStatus("PENDING");
        schedule.setScheduleCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 scheduleId가 자동으로 생성됨)
        repository().save(schedule);

        // 4. Schedule 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(schedule);
        scheduleDataCreated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateScheduleData(
        ScheduleDocumentGenerated scheduleDocumentGenerated
    ) {
        // 1. 이벤트로 받은 scheduleId를 사용하여 DB에서 해당 Schedule 데이터를 찾습니다.
        repository().findById(scheduleDocumentGenerated.getScheduleId()).ifPresent(schedule->{
            
            // 2. 찾은 데이터의 필드 값을 이벤트로 받은 정보로 업데이트합니다.
            schedule.setScheduleFilePath(scheduleDocumentGenerated.getScheduleFilePath());
            schedule.setScheduleFileUrl(scheduleDocumentGenerated.getScheduleFileUrl());
            schedule.setScheduleStatus(scheduleDocumentGenerated.getScheduleStatus());
            
            // 3. 변경된 내용을 DB에 다시 저장합니다.
            repository().save(schedule);

         });
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
