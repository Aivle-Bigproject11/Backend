// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/DeathReportDataCreated.java
// ========================================

package aivlebigproject.domain;

import aivlebigproject.domain.*;
import aivlebigproject.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;
import org.springframework.beans.BeanUtils; // [주석] BeanUtils를 사용하기 위해 import 합니다.

//<<< DDD / Domain Event
@Data
@ToString
public class DeathReportDataCreated extends AbstractEvent {

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

    public DeathReportDataCreated() {
        super();
    }

    public DeathReportDataCreated(DeathReport aggregate) {
        super(aggregate);
    }

    // [추가] FuneralInfo와 DeathReport 객체를 모두 받아 이벤트를 만드는 새로운 생성자입니다.
    public DeathReportDataCreated(FuneralInfo funeralInfo, DeathReport deathReport) {
        // [주석] 먼저 FuneralInfo의 모든 데이터를 이 이벤트 객체로 복사합니다.
        BeanUtils.copyProperties(funeralInfo, this);

        // [주석] 그 다음, DeathReport 객체에서 필요한 고유 정보들을 가져와 설정합니다.
        this.setDeathReportId(deathReport.getDeathReportId());
        this.setDeathReportStatus(deathReport.getDeathReportStatus());
        this.setDeathReportCreatedAt(deathReport.getDeathReportCreatedAt());
    }
}
//>>> DDD / Domain Event
