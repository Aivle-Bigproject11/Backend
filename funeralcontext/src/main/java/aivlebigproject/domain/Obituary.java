// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/Obituary.java
// 역할 : 
// ========================================

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        // 1. 새로운 Obituary 객체 생성
        Obituary obituary = new Obituary();

        // 2. 이벤트로 받은 데이터를 새로 만든 Obituary 객체에 채워넣기
        obituary.setFuneralInfoId(obituaryCreationRequested.getFuneralInfoId());
        obituary.setCustomerId(obituaryCreationRequested.getCustomerId());
        obituary.setCustomerName(obituaryCreationRequested.getCustomerName());
        obituary.setCustomerRrn(obituaryCreationRequested.getCustomerRrn());
        obituary.setCustomerPhone(obituaryCreationRequested.getCustomerPhone());
        obituary.setDeceasedName(obituaryCreationRequested.getDeceasedName());
        obituary.setDeceasedNameHanja(obituaryCreationRequested.getDeceasedNameHanja());
        obituary.setDeceasedRrn(obituaryCreationRequested.getDeceasedRrn());
        obituary.setDeceasedAge(obituaryCreationRequested.getDeceasedAge());
        obituary.setDeceasedDate(obituaryCreationRequested.getDeceasedDate());
        obituary.setDeceasedBirthOfDate(obituaryCreationRequested.getDeceasedBirthOfDate());
        obituary.setDeceasedGender(obituaryCreationRequested.getDeceasedGender());
        obituary.setDeceasedReligion(obituaryCreationRequested.getDeceasedReligion());
        obituary.setDeceasedRelationToHouseholdHead(obituaryCreationRequested.getDeceasedRelationToHouseholdHead());
        obituary.setReportRegistrationDate(obituaryCreationRequested.getReportRegistrationDate());
        obituary.setReporterName(obituaryCreationRequested.getReporterName());
        obituary.setReporterRrn(obituaryCreationRequested.getReporterRrn());
        obituary.setReporterRelationToDeceased(obituaryCreationRequested.getReporterRelationToDeceased());
        obituary.setReporterAddress(obituaryCreationRequested.getReporterAddress());
        obituary.setReporterPhone(obituaryCreationRequested.getReporterPhone());
        obituary.setReporterEmail(obituaryCreationRequested.getReporterEmail());
        obituary.setSubmitterName(obituaryCreationRequested.getSubmitterName());
        obituary.setSubmitterRrn(obituaryCreationRequested.getSubmitterRrn());
        obituary.setFuneralCompanyName(obituaryCreationRequested.getFuneralCompanyName());
        obituary.setDirectorName(obituaryCreationRequested.getDirectorName());
        obituary.setDirectorPhone(obituaryCreationRequested.getDirectorPhone());
        obituary.setFuneralHomeName(obituaryCreationRequested.getFuneralHomeName());
        obituary.setMortuaryInfo(obituaryCreationRequested.getMortuaryInfo());
        obituary.setFuneralHomeAddress(obituaryCreationRequested.getFuneralHomeAddress());
        obituary.setFuneralDuration(obituaryCreationRequested.getFuneralDuration());
        obituary.setProcessionDateTime(obituaryCreationRequested.getProcessionDateTime());
        obituary.setBurialSiteInfo(obituaryCreationRequested.getBurialSiteInfo());
        obituary.setChiefMourners(obituaryCreationRequested.getChiefMourners());
        obituary.setTemplateKeyword(obituaryCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        obituary.setObituaryStatus("PENDING");
        obituary.setObituaryCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 obituaryId가 자동으로 생성됨)
        repository().save(obituary);

        // 4. Obituary 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
        obituaryDataCreated.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void createObituaryData(
        AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        // 1. 새로운 Obituary 객체 생성
        Obituary obituary = new Obituary();

        // 2. 이벤트로 받은 데이터를 새로 만든 Obituary 객체에 채워넣기
        obituary.setFuneralInfoId(allDocumentsCreationRequested.getFuneralInfoId());
        obituary.setCustomerId(allDocumentsCreationRequested.getCustomerId());
        obituary.setCustomerName(allDocumentsCreationRequested.getCustomerName());
        obituary.setCustomerRrn(allDocumentsCreationRequested.getCustomerRrn());
        obituary.setCustomerPhone(allDocumentsCreationRequested.getCustomerPhone());
        obituary.setDeceasedName(allDocumentsCreationRequested.getDeceasedName());
        obituary.setDeceasedNameHanja(allDocumentsCreationRequested.getDeceasedNameHanja());
        obituary.setDeceasedRrn(allDocumentsCreationRequested.getDeceasedRrn());
        obituary.setDeceasedAge(allDocumentsCreationRequested.getDeceasedAge());
        obituary.setDeceasedDate(allDocumentsCreationRequested.getDeceasedDate());
        obituary.setDeceasedBirthOfDate(allDocumentsCreationRequested.getDeceasedBirthOfDate());
        obituary.setDeceasedGender(allDocumentsCreationRequested.getDeceasedGender());
        obituary.setDeceasedReligion(allDocumentsCreationRequested.getDeceasedReligion());
        obituary.setDeceasedRelationToHouseholdHead(allDocumentsCreationRequested.getDeceasedRelationToHouseholdHead());
        obituary.setReportRegistrationDate(allDocumentsCreationRequested.getReportRegistrationDate());
        obituary.setReporterName(allDocumentsCreationRequested.getReporterName());
        obituary.setReporterRrn(allDocumentsCreationRequested.getReporterRrn());
        obituary.setReporterRelationToDeceased(allDocumentsCreationRequested.getReporterRelationToDeceased());
        obituary.setReporterAddress(allDocumentsCreationRequested.getReporterAddress());
        obituary.setReporterPhone(allDocumentsCreationRequested.getReporterPhone());
        obituary.setReporterEmail(allDocumentsCreationRequested.getReporterEmail());
        obituary.setSubmitterName(allDocumentsCreationRequested.getSubmitterName());
        obituary.setSubmitterRrn(allDocumentsCreationRequested.getSubmitterRrn());
        obituary.setFuneralCompanyName(allDocumentsCreationRequested.getFuneralCompanyName());
        obituary.setDirectorName(allDocumentsCreationRequested.getDirectorName());
        obituary.setDirectorPhone(allDocumentsCreationRequested.getDirectorPhone());
        obituary.setFuneralHomeName(allDocumentsCreationRequested.getFuneralHomeName());
        obituary.setMortuaryInfo(allDocumentsCreationRequested.getMortuaryInfo());
        obituary.setFuneralHomeAddress(allDocumentsCreationRequested.getFuneralHomeAddress());
        obituary.setFuneralDuration(allDocumentsCreationRequested.getFuneralDuration());
        obituary.setProcessionDateTime(allDocumentsCreationRequested.getProcessionDateTime());
        obituary.setBurialSiteInfo(allDocumentsCreationRequested.getBurialSiteInfo());
        obituary.setChiefMourners(allDocumentsCreationRequested.getChiefMourners());
        obituary.setTemplateKeyword(allDocumentsCreationRequested.getTemplateKeyword());

        // 초기 상태 및 생성일자 설정
        obituary.setObituaryStatus("PENDING");
        obituary.setObituaryCreatedAt(new Date());
        
        // 3. Repository에 저장 (이때 obituaryId가 자동으로 생성됨)
        repository().save(obituary);

        // 4. Obituary 데이터 생성이 완료되었다는 새로운 이벤트 발행 (Python으로 전송)
        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
        obituaryDataCreated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateObituaryData(
        ObituaryDocumentGenerated obituaryDocumentGenerated
    ) {
        // 1. 이벤트로 받은 obituaryId를 사용하여 DB에서 해당 Obituary 데이터를 찾습니다.
        repository().findById(obituaryDocumentGenerated.getObituaryId()).ifPresent(obituary->{
            
            // 2. 찾은 데이터의 필드 값을 이벤트로 받은 정보로 업데이트합니다.
            obituary.setObituaryFilePath(obituaryDocumentGenerated.getObituaryFilePath());
            obituary.setObituaryFileUrl(obituaryDocumentGenerated.getObituaryFileUrl());
            obituary.setObituaryStatus(obituaryDocumentGenerated.getObituaryStatus());
            
            // 3. 변경된 내용을 DB에 다시 저장합니다.
            repository().save(obituary);

         });
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
