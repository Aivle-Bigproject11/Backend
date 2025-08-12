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
    private String deathReportFileName;
    private String deathReportFileUrl;
    private String deathReportStatus;
    private Date deathReportCreatedAt;

    private Long customerId;
    private String deceasedName;
    private String deceasedNameHanja;
    private String deceasedRrn;
    private Integer deceasedAge;
    private Date deceasedBirthOfDate;
    private String deceasedGender;
    private Date deceasedDate;
    private String deceasedReligion;
    private String deceasedRegisteredAddress;
    private String deceasedAddress;
    private String deceasedRelationToHouseholdHead;
    private Date reportRegistrationDate;
    private String deathLocation;
    private String deathLocationType;
    private String deathLocationEtc;
    private String deathReportEtc;
    private String reporterName;
    private String reporterRrn;
    private String reporterQualification;
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
    private String funeralHomeAddress;
    private String funeralHomeAddressUrl;
    private String funeralDuration;
    private String mortuaryInfo;
    private String processionDateTime;
    private String burialSiteInfo;
    private String chiefMourners;
    private String chiefMournersContact;
    private String chiefMournerAccountHolder;
    private String chiefMournerBankName;
    private String chiefMournerAccountNumber;
    private String templateKeyword;

    public static DeathReportRepository repository() {
        DeathReportRepository deathReportRepository = FuneralcontextApplication.applicationContext.getBean(
            DeathReportRepository.class
        );
        return deathReportRepository;
    }

    //<<< Clean Arch / Port Method
    public static void updateDeathReportData(
        DeathReportDocumentGenerated deathReportDocumentGenerated
    ) {
        // 1. 이벤트로 받은 deathReportId를 사용하여 DB에서 해당 DeathReport 데이터를 찾습니다.
        repository().findById(deathReportDocumentGenerated.getDeathReportId()).ifPresent(deathReport->{
            
            // 2. 찾은 데이터의 필드 값을 이벤트로 받은 정보로 업데이트합니다.
            deathReport.setDeathReportFileName(deathReportDocumentGenerated.getDeathReportFileName()); // [수정] FilePath -> FileName
            deathReport.setDeathReportFileUrl(deathReportDocumentGenerated.getDeathReportFileUrl());
            deathReport.setDeathReportStatus(deathReportDocumentGenerated.getDeathReportStatus());
            
            // 3. 변경된 내용을 DB에 다시 저장합니다.
            repository().save(deathReport);

        });
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
