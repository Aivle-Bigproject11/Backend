# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/deathreport.py
# 역할 : 사망신고서 PDF 문서 생성
# ========================================

import fitz
from datetime import datetime
from app.schemas import DeathReportDataCreated

def create_death_report_document(event_data: DeathReportDataCreated):
    """
    DeathReportDataCreated 이벤트 데이터를 받아 사망신고서 PDF를 생성하고,
    수정된 PDF 문서 객체를 반환합니다.
    """
    try:
        template_path = "resources/templates/blank_singo.pdf"
        font_path = "resources/fonts/NanumGothic.ttf"
        text_color = (0.2, 0.6, 0.9)

        coordinates = {
            "declaration_year": (123, 93), "declaration_month": (173, 93), "declaration_day": (213, 93),
            "deceasedName": (200, 112), "deceasedNameHanja": (200, 140),
            "deceasedRrn": (426, 127),
            "checkbox_deceased_male": (309, 136), "checkbox_deceased_female": (350, 136),
            "deceasedRelationToHouseholdHead": (460, 198),
            "deceasedRegisteredAddress": (165, 168), # [주석] 좌표는 존재하지만, 데이터 모델에 필드가 없어 사용 안 함
            "deceasedFinalAddress": (165, 198),      # [주석] 좌표는 존재하지만, 데이터 모델에 필드가 없어 사용 안 함
            "death_year": (162, 227), "death_month": (204, 227), "death_day": (231, 227),
            "death_hour": (258, 227), "death_minute": (286, 227),
            "funeralHomeAddress": (190, 255),
            "checkbox_death_hospital": (263, 277),
            "reporterName": (237, 370), "reporterRrn": (423, 372),
            "checkbox_reporter_qualification_1": (154, 393),
            "reporterRelationToDeceased": (393, 397),
            "reporterAddress": (148, 445), "reporterPhone": (369, 446),
            "reporterEmail": (468, 446),
            "submitterName": (195, 477), "submitterRrn": (421, 478),
        }

        doc = fitz.open(template_path)
        page = doc[0]

        with open(font_path, "rb") as fontfile:
            font_buffer = fontfile.read()
        page.insert_font(fontname="korean_font", fontbuffer=font_buffer)

        # [수정] data_to_fill 딕셔너리에서 스키마에 없는 필드 제거
        data_to_fill = {
            "deceasedName": event_data.deceasedName,
            "deceasedNameHanja": event_data.deceasedNameHanja,
            "deceasedRrn": event_data.deceasedRrn,
            "deceasedRelationToHouseholdHead": event_data.deceasedRelationToHouseholdHead,
            # "deceasedRegisteredAddress": event_data.deceasedRegisteredAddress, # ❌ 스키마에 없어 에러 유발, 제거
            # "deceasedFinalAddress": event_data.deceasedFinalAddress,           # ❌ 스키마에 없어 에러 유발, 제거
            "reporterName": event_data.reporterName,
            "reporterRrn": event_data.reporterRrn,
            "reporterRelationToDeceased": event_data.reporterRelationToDeceased,
            "reporterAddress": event_data.reporterAddress,
            "reporterPhone": event_data.reporterPhone,
            "reporterEmail": event_data.reporterEmail,
            "submitterName": event_data.submitterName,
            "submitterRrn": event_data.submitterRrn,
            "funeralHomeAddress": event_data.funeralHomeAddress
        }
        
        # 날짜/시간 데이터 처리
        if event_data.reportRegistrationDate:
            dt_obj = event_data.reportRegistrationDate
            if isinstance(dt_obj, str):
                dt_obj = datetime.fromisoformat(dt_obj.replace('Z', ''))
            data_to_fill["declaration_year"] = dt_obj.strftime("%Y")
            data_to_fill["declaration_month"] = dt_obj.strftime("%m")
            data_to_fill["declaration_day"] = dt_obj.strftime("%d")

        if event_data.deceasedDate:
            dt_obj = event_data.deceasedDate
            if isinstance(dt_obj, str):
                dt_obj = datetime.fromisoformat(dt_obj.replace('Z', ''))
            data_to_fill["death_year"] = dt_obj.strftime("%Y")
            data_to_fill["death_month"] = dt_obj.strftime("%m")
            data_to_fill["death_day"] = dt_obj.strftime("%d")
            data_to_fill["death_hour"] = dt_obj.strftime("%H")
            data_to_fill["death_minute"] = dt_obj.strftime("%M")
        
        # 텍스트 필드 채우기
        for key, coord in coordinates.items():
            if "checkbox" not in key and key in data_to_fill and data_to_fill[key] is not None:
                page.insert_text(coord, str(data_to_fill[key]), fontname="korean_font", fontsize=10, color=text_color)

        # 체크박스 처리
        if event_data.deceasedGender == "1":
            rect = fitz.Rect(coordinates["checkbox_deceased_male"], coordinates["checkbox_deceased_male"]) + (-4, -4, 4, 4)
            page.draw_line(rect.top_left, rect.bottom_right, color=text_color, width=1.5)
            page.draw_line(rect.top_right, rect.bottom_left, color=text_color, width=1.5)
        elif event_data.deceasedGender == "2":
            rect = fitz.Rect(coordinates["checkbox_deceased_female"], coordinates["checkbox_deceased_female"]) + (-4, -4, 4, 4)
            page.draw_line(rect.top_left, rect.bottom_right, color=text_color, width=1.5)
            page.draw_line(rect.top_right, rect.bottom_left, color=text_color, width=1.5)
        
        return doc

    except Exception as e:
        print(f"❌ 사망신고서 PDF 생성 중 오류 발생: {e}", flush=True)
        return None