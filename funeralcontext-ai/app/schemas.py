# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/schemas.py
# 역할 : 
# ========================================

from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

# Java의 ObituaryDataCreated 이벤트와 동일한 구조를 가진 Pydantic 모델
class ObituaryDataCreated(BaseModel):
    obituaryId: Optional[int] = None
    funeralInfoId: Optional[int] = None
    obituaryTemplateId: Optional[int] = None    
    obituaryFilePath: Optional[str] = None
    obituaryFileUrl: Optional[str] = None     
    obituaryStatus: Optional[str] = None
    obituaryCreatedAt: Optional[datetime] = None
    customerId: Optional[int] = None          
    customerName: Optional[str] = None        
    customerRrn: Optional[str] = None         
    customerPhone: Optional[str] = None       
    deceasedName: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedDate: Optional[datetime] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedReligion: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
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
    mortuaryInfo: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    templateKeyword: Optional[str] = None

class DeathReportDataCreated(BaseModel):
    deathReportId: Optional[int] = None
    funeralInfoId: Optional[int] = None
    deathReportTemplateId: Optional[int] = None
    deathReportFilePath: Optional[str] = None
    deathReportFileUrl: Optional[str] = None
    deathReportStatus: Optional[str] = None
    deathReportCreatedAt: Optional[datetime] = None
    customerId: Optional[int] = None
    customerName: Optional[str] = None
    customerRrn: Optional[str] = None
    customerPhone: Optional[str] = None
    deceasedName: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedDate: Optional[datetime] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedReligion: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
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
    mortuaryInfo: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    templateKeyword: Optional[str] = None

class ScheduleDataCreated(BaseModel):
    scheduleId: Optional[int] = None
    funeralInfoId: Optional[int] = None
    scheduleTemplateId: Optional[int] = None
    scheduleFilePath: Optional[str] = None
    scheduleFileUrl: Optional[str] = None
    scheduleStatus: Optional[str] = None
    scheduleCreatedAt: Optional[datetime] = None
    customerId: Optional[int] = None
    customerName: Optional[str] = None
    customerRrn: Optional[str] = None
    customerPhone: Optional[str] = None
    deceasedName: Optional[str] = None
    deceasedAge: Optional[int] = None
    deceasedDate: Optional[datetime] = None
    deceasedBirthOfDate: Optional[datetime] = None
    deceasedGender: Optional[str] = None
    deceasedNameHanja: Optional[str] = None
    deceasedRrn: Optional[str] = None
    deceasedReligion: Optional[str] = None
    deceasedRelationToHouseholdHead: Optional[str] = None
    reportRegistrationDate: Optional[datetime] = None
    reporterName: Optional[str] = None
    reporterRrn: Optional[str] = None
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
    mortuaryInfo: Optional[str] = None
    funeralHomeAddress: Optional[str] = None
    funeralDuration: Optional[str] = None
    processionDateTime: Optional[str] = None
    burialSiteInfo: Optional[str] = None
    chiefMourners: Optional[str] = None
    templateKeyword: Optional[str] = None