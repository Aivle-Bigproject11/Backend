# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/schemas.py
# 역할 :
# ========================================

from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

# Java의 ObituaryDataCreated 이벤트와 동일한 구조를 가진 Pydantic 모델
class VideoRequest(BaseModel):
    videoId: Optional[int] = None
    memorialId: Optional[str] = None
    name: Optional[str] = None
    birthDate: Optional[str] = None
    deceasedDate: Optional[str] = None
    keywords: Optional[str] = None
    photoCount: Optional[str] = None
    imageUrls: Optional[list] = None
    outroImageUrl: Optional[str] = None

class VideoCreate(BaseModel):
    videoId: Optional[int] = None
    videoUrl: Optional[str] = None
