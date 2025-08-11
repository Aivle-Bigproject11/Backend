// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/DeathReport.java
// 역할 : 
// ========================================

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        // 1. 새로운 DeathReport 객체 생성
        DeathReport deathReport = new DeathReport();

        // 2. 이벤트로 받은 모든 데이터를 새로 만든 DeathReport 객체에 채워넣기
        deathReport.setFuneralInfoId(deathReportCreationRequested.getFuneralInfoId());
        deathReport.setCustomerId(deathReportCreationRequested.getCustomerId());
        deathReport.setCustomerName(deathReportCreationRequested.getCustomerName());
        deathReport.setCustomerRrn(deathReportCreationRequested.getCustomerRrn());
        deathReport.setCustomerPhone(deathReportCreationRequested.getCustomerPhone());
        deathReport.setDeceasedName(deathReportCreationRequested.getDeceasedName());
        deathReport.setDeceasedNameHanja(deathReportCreationRequested.getDeceasedNameHanja());
        deathReport.setDeceasedRrn(deathReportCreationRequested.getDeceasedRrn());
        deathReport.setDeceasedAge(deathReportCreationRequested.getDeceasedAge());
        deathReport.setDeceasedDate(deathReportCreationRequested.getDeceasedDate());
        deathReport.setDeceasedBirthOfDate(deathReportCreationRequested.getDeceasedBirthOfDate());
        deathReport.setDeceasedGender(deathReportCreationRequested.getDeceasedGender());
        deathReport.setDeceasedReligion(deathReportCreationRequested.getDeceasedReligion());
        deathReport.setDeceasedRelationToHouseholdHead(deathReportCreationRequested.getDeceasedRelationToHouseholdHead());
        deathReport.setReportRegistrationDate(deathReportCreationRequested.getReportRegistrationDate());
        deathReport.setReporterName(deathReportCreationRequested.getReporterName());
        deathReport.setReporterRrn(deathReportCreationRequested.getReporterRrn());
        deathReport.setReporterRelationToDeceased(deathReportCreationRequested.getReporterRelationToDeceased());
        deathReport.setReporterAddress(deathReportCreationRequested.getReporterAddress());
        deathReport.setReporterPhone(deathReportCreationRequested.getReporterPhone());
        deathReport.setReporterEmail(deathReportCreationRequested.getReporterEmail());
        deathReport.setSubmitterName(deathReportCreationRequested.getSubmitterName());
        deathReport.setSubmitterRrn(deathReportCreationRequested.getSubmitterRrn());
        deathReport.setFuneralCompanyName(deathReportCreationRequested.getFuneralCompanyName());
        deathReport.setDirectorName(deathReportCreationRequested.getDirectorName());
        deathReport.setDirectorPhone(deathReportCreationRequested.getDirectorPhone());
        deathReport.setFuneralHomeName(deathReportCreationRequested.getFuneralHomeName());
        deathReport.setMortuaryInfo(deathReportCreationRequested.getMortuaryInfo());
        deathReport.setFuneralHomeAddress(deathReportCreationRequested.getFuneralHomeAddress());
        deathReport.setFuneralDuration(deathReportCreationRequested.getFuneralDuration());
        deathReport.setProcessionDateTime(deathReportCreationRequested.getProcessionDateTime());
        deathReport.setBurialSiteInfo(deathReportCreationRequested.getBurialSiteInfo());
        deathReport.setChiefMourners(deathReportCreationRequested.getChiefMourners());
        deathReport.setTemplateKeyword(deathReportCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        deathReport.setDeathReportStatus("PENDING");
        deathReport.setDeathReportCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 deathReportId가 자동으로 생성됨)
        repository().save(deathReport);

        // 4. DeathReport 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
        deathReportDataCreated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createDeathReportData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        // 1. 새로운 DeathReport 객체 생성
        DeathReport deathReport = new DeathReport();

        // 2. 이벤트로 받은 모든 데이터를 새로 만든 DeathReport 객체에 채워넣기
        deathReport.setFuneralInfoId(allDocumentsCreationRequested.getFuneralInfoId());
        deathReport.setCustomerId(allDocumentsCreationRequested.getCustomerId());
        deathReport.setCustomerName(allDocumentsCreationRequested.getCustomerName());
        deathReport.setCustomerRrn(allDocumentsCreationRequested.getCustomerRrn());
        deathReport.setCustomerPhone(allDocumentsCreationRequested.getCustomerPhone());
        deathReport.setDeceasedName(allDocumentsCreationRequested.getDeceasedName());
        deathReport.setDeceasedNameHanja(allDocumentsCreationRequested.getDeceasedNameHanja());
        deathReport.setDeceasedRrn(allDocumentsCreationRequested.getDeceasedRrn());
        deathReport.setDeceasedAge(allDocumentsCreationRequested.getDeceasedAge());
        deathReport.setDeceasedDate(allDocumentsCreationRequested.getDeceasedDate());
        deathReport.setDeceasedBirthOfDate(allDocumentsCreationRequested.getDeceasedBirthOfDate());
        deathReport.setDeceasedGender(allDocumentsCreationRequested.getDeceasedGender());
        deathReport.setDeceasedReligion(allDocumentsCreationRequested.getDeceasedReligion());
        deathReport.setDeceasedRelationToHouseholdHead(allDocumentsCreationRequested.getDeceasedRelationToHouseholdHead());
        deathReport.setReportRegistrationDate(allDocumentsCreationRequested.getReportRegistrationDate());
        deathReport.setReporterName(allDocumentsCreationRequested.getReporterName());
        deathReport.setReporterRrn(allDocumentsCreationRequested.getReporterRrn());
        deathReport.setReporterRelationToDeceased(allDocumentsCreationRequested.getReporterRelationToDeceased());
        deathReport.setReporterAddress(allDocumentsCreationRequested.getReporterAddress());
        deathReport.setReporterPhone(allDocumentsCreationRequested.getReporterPhone());
        deathReport.setReporterEmail(allDocumentsCreationRequested.getReporterEmail());
        deathReport.setSubmitterName(allDocumentsCreationRequested.getSubmitterName());
        deathReport.setSubmitterRrn(allDocumentsCreationRequested.getSubmitterRrn());
        deathReport.setFuneralCompanyName(allDocumentsCreationRequested.getFuneralCompanyName());
        deathReport.setDirectorName(allDocumentsCreationRequested.getDirectorName());
        deathReport.setDirectorPhone(allDocumentsCreationRequested.getDirectorPhone());
        deathReport.setFuneralHomeName(allDocumentsCreationRequested.getFuneralHomeName());
        deathReport.setMortuaryInfo(allDocumentsCreationRequested.getMortuaryInfo());
        deathReport.setFuneralHomeAddress(allDocumentsCreationRequested.getFuneralHomeAddress());
        deathReport.setFuneralDuration(allDocumentsCreationRequested.getFuneralDuration());
        deathReport.setProcessionDateTime(allDocumentsCreationRequested.getProcessionDateTime());
        deathReport.setBurialSiteInfo(allDocumentsCreationRequested.getBurialSiteInfo());
        deathReport.setChiefMourners(allDocumentsCreationRequested.getChiefMourners());
        deathReport.setTemplateKeyword(allDocumentsCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        deathReport.setDeathReportStatus("PENDING");
        deathReport.setDeathReportCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 deathReportId가 자동으로 생성됨)
        repository().save(deathReport);

        // 4. DeathReport 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(deathReport);
        deathReportDataCreated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateDeathReportData(
        DeathReportDocumentGenerated deathReportDocumentGenerated
    ) {
        // 1. 이벤트로 받은 deathReportId를 사용하여 DB에서 해당 DeathReport 데이터를 찾습니다.
        repository().findById(deathReportDocumentGenerated.getDeathReportId()).ifPresent(deathReport->{
            
            // 2. 찾은 데이터의 필드 값을 이벤트로 받은 정보로 업데이트합니다.
            deathReport.setDeathReportFilePath(deathReportDocumentGenerated.getDeathReportFilePath());
            deathReport.setDeathReportFileUrl(deathReportDocumentGenerated.getDeathReportFileUrl());
            deathReport.setDeathReportStatus(deathReportDocumentGenerated.getDeathReportStatus());
            
            // 3. 변경된 내용을 DB에 다시 저장합니다.
            repository().save(deathReport);

         });
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
