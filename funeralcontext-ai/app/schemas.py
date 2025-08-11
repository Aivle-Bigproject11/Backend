# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/schemas.py
# 역할 : 
# ========================================

from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

# Java의 ObituaryDataCreated 이벤트와 동일한 구조를 가진 Pydantic 모델
class ObituaryDataCreated(BaseModel):
    # Obituary 고유 정보
    obituaryId: Optional[int] = None
    obituaryTemplateId: Optional[int] = None
    obituaryFileName: Optional[str] = None  # FilePath -> FileName으로 변경
    obituaryFileUrl: Optional[str] = None
    obituaryStatus: Optional[str] = None
    obituaryCreatedAt: Optional[datetime] = None

    # FuneralInfo 공통 정보
    funeralInfoId: Optional[int] = None
    customerId: Optional[int] = None
    deceasedName: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedDate: Optional[datetime] = None
    deceasedReligion: Optional[str] = None
    deceasedRegisteredAddress: Optional[str] = None
    deceasedAddress: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    deathLocation: Optional[str] = None
    deathLocationType: Optional[str] = None
    deathLocationEtc: Optional[str] = None
    deathReportEtc: Optional[str] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
    reporterQualification: Optional[str] = None
    reporterRelationToDeceased: Optional[str] = None
    reporterAddress: Optional[str] = None
    reporterPhone: Optional[str] = None
    reporterEmail: Optional[str] = None
    submitterName: Optional[str] = None
    submitterRrn: Optional[str] = None
    funeralCompanyName: Optional[str] = None
    directorName: Optional[str] = None
    directorPhone: Optional[str] = None
    funeralHomeName: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralHomeAddressUrl: Optional[str] = None
    mortuaryInfo: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    chiefMournersContact: Optional[str] = None
    chiefMournerAccountHolder: Optional[str] = None
    chiefMournerBankName: Optional[str] = None
    chiefMournerAccountNumber: Optional[str] = None
    templateKeyword: Optional[str] = None

class DeathReportDataCreated(BaseModel):
    # DeathReport 고유 정보
    deathReportId: Optional[int] = None
    deathReportTemplateId: Optional[int] = None
    deathReportFileName: Optional[str] = None
    deathReportFileUrl: Optional[str] = None
    deathReportStatus: Optional[str] = None
    deathReportCreatedAt: Optional[datetime] = None

    # FuneralInfo 공통 정보
    funeralInfoId: Optional[int] = None
    customerId: Optional[int] = None
    deceasedName: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedDate: Optional[datetime] = None
    deceasedReligion: Optional[str] = None
    deceasedRegisteredAddress: Optional[str] = None
    deceasedAddress: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    deathLocation: Optional[str] = None
    deathLocationType: Optional[str] = None
    deathLocationEtc: Optional[str] = None
    deathReportEtc: Optional[str] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
    reporterQualification: Optional[str] = None
    reporterRelationToDeceased: Optional[str] = None
    reporterAddress: Optional[str] = None
    reporterPhone: Optional[str] = None
    reporterEmail: Optional[str] = None
    submitterName: Optional[str] = None
    submitterRrn: Optional[str] = None
    funeralCompanyName: Optional[str] = None
    directorName: Optional[str] = None
    directorPhone: Optional[str] = None
    funeralHomeName: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralHomeAddressUrl: Optional[str] = None
    mortuaryInfo: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    chiefMournersContact: Optional[str] = None
    chiefMournerAccountHolder: Optional[str] = None
    chiefMournerBankName: Optional[str] = None
    chiefMournerAccountNumber: Optional[str] = None
    templateKeyword: Optional[str] = None

class ScheduleDataCreated(BaseModel):
    # Schedule 고유 정보
    scheduleId: Optional[int] = None
    scheduleTemplateId: Optional[int] = None
    scheduleDallePrompt: Optional[str] = None
    scheduleDalleTemplateImageUrl: Optional[str] = None
    scheduleFileName: Optional[str] = None
    scheduleFileUrl: Optional[str] = None
    scheduleStatus: Optional[str] = None
    scheduleCreatedAt: Optional[datetime] = None

    # FuneralInfo 공통 정보
    funeralInfoId: Optional[int] = None
    customerId: Optional[int] = None
    deceasedName: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedDate: Optional[datetime] = None
    deceasedReligion: Optional[str] = None
    deceasedRegisteredAddress: Optional[str] = None
    deceasedAddress: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    deathLocation: Optional[str] = None
    deathLocationType: Optional[str] = None
    deathLocationEtc: Optional[str] = None
    deathReportEtc: Optional[str] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
    reporterQualification: Optional[str] = None
    reporterRelationToDeceased: Optional[str] = None
    reporterAddress: Optional[str] = None
    reporterPhone: Optional[str] = None
    reporterEmail: Optional[str] = None
    submitterName: Optional[str] = None
    submitterRrn: Optional[str] = None
    funeralCompanyName: Optional[str] = None
    directorName: Optional[str] = None
    directorPhone: Optional[str] = None
    funeralHomeName: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralHomeAddressUrl: Optional[str] = None
    mortuaryInfo: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    chiefMournersContact: Optional[str] = None
    chiefMournerAccountHolder: Optional[str] = None
    chiefMournerBankName: Optional[str] = None
    chiefMournerAccountNumber: Optional[str] = None
    templateKeyword: Optional[str] = None