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
public class ObituaryDataCreated extends AbstractEvent {

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

    public ObituaryDataCreated() {
        super();
    }

    // [주석] 이 생성자는 더 이상 직접 사용되지 않거나, Python의 응답을 받을 때만 사용됩니다.
    public ObituaryDataCreated(Obituary aggregate) {
        super(aggregate);
    }

    // [추가] FuneralInfo와 Obituary 객체를 모두 받아 이벤트를 만드는 새로운 생성자입니다.
    public ObituaryDataCreated(FuneralInfo funeralInfo, Obituary obituary) {
        // [주석] 먼저 FuneralInfo의 모든 데이터를 이 이벤트 객체로 복사합니다.
        BeanUtils.copyProperties(funeralInfo, this);

        // [주석] 그 다음, Obituary 객체에서 필요한 고유 정보들을 가져와 설정합니다.
        this.setObituaryId(obituary.getObituaryId());
        this.setObituaryStatus(obituary.getObituaryStatus());
        this.setObituaryCreatedAt(obituary.getObituaryCreatedAt());
        // ... (필요시 obituaryTemplateId 등 다른 값도 설정)
    }
}
//>>> DDD / Domain Event