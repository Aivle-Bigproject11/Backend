// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/FuneralInfo.java
// 역할 : 
// ========================================

package aivlebigproject.domain;

import aivlebigproject.FuneralcontextApplication;
import aivlebigproject.infra.FuneralInfoAiValidator;
import aivlebigproject.domain.FuneralInfoRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import org.springframework.beans.BeanUtils;

import lombok.Data;

@Entity
@Table(name = "FuneralInfo_table")
@Data
//<<< DDD / Aggregate Root
public class FuneralInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funeralInfoId;
    private Long customerId;
    private String validationStatus;
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
    // [주석] updateFuneralInfo 메서드가 Command 객체를 파라미터로 받도록 변경합니다.
    public void updateFuneralInfo(UpdateFuneralInfoCommand command) {
        // [주석] Command 객체에 담겨온 데이터로 현재 Aggregate의 필드 값을 업데이트합니다.
        //       BeanUtils.copyProperties를 사용하면 이 과정을 자동화할 수 있습니다.
        BeanUtils.copyProperties(command, this);

        // [주석] 요청에 따라 validationStatus를 "VALIDATED"로 설정합니다.
        this.setValidationStatus("VALIDATED");

        // [주석] 업데이트가 완료되었다는 이벤트를 발행합니다.
        FuneralInfoUpdated funeralInfoUpdated = new FuneralInfoUpdated(this);
        funeralInfoUpdated.publishAfterCommit();
    }

    // [주석] 이 메서드는 이제 ObituaryController에서 직접 로직을 처리하므로 사용되지 않습니다.
    public void createObituary() {
        // No operation
    }

    // [주석] 이 메서드는 이제 ScheduleController에서 직접 로직을 처리하므로 사용되지 않습니다.
    public void createSchedule() {
        // No operation
    }

    // [주석] 이 메서드는 이제 DeathReportController에서 직접 로직을 처리하므로 사용되지 않습니다.
    public void createDeathReport() {
        // No operation
    }
}
//>>> DDD / Aggregate Root
