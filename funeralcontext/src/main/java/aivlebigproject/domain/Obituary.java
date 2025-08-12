// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/Obituary.java
// 역할 : 
// ========================================

package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.domain.ObituaryDataCreated;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
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
    private String obituaryFileName;
    private String obituaryFileUrl;
    private String obituaryStatus;
    private Date obituaryCreatedAt;

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

    public static ObituaryRepository repository() {
        ObituaryRepository obituaryRepository = FuneralcontextApplication.applicationContext.getBean(
            ObituaryRepository.class
        );
        return obituaryRepository;
    }
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateObituaryData(
        ObituaryDocumentGenerated obituaryDocumentGenerated
    ) {
        repository().findById(obituaryDocumentGenerated.getObituaryId()).ifPresent(obituary->{
            // ... (다른 set 메소드들은 그대로)
            obituary.setObituaryFileName(obituaryDocumentGenerated.getObituaryFileName());
            obituary.setObituaryFileUrl(obituaryDocumentGenerated.getObituaryFileUrl());
            obituary.setObituaryStatus(obituaryDocumentGenerated.getObituaryStatus());
            obituary.setFuneralHomeAddressUrl(obituaryDocumentGenerated.getFuneralHomeAddressUrl());
            
            // 👇 [핵심] DTO에 이미 Date 객체로 변환되어 있으므로, 파싱 없이 바로 저장합니다.
            if (obituaryDocumentGenerated.getObituaryCreatedAt() != null) {
                obituary.setObituaryCreatedAt(obituaryDocumentGenerated.getObituaryCreatedAt());
            }
            
            repository().save(obituary);
        });
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
