# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/obituary.py
# 역할 : 부고 이미지 생성
# ========================================

from PIL import Image, ImageDraw, ImageFont
from datetime import datetime
from app.schemas import ObituaryDataCreated

def create_obituary_document(event_data: ObituaryDataCreated):
    """
    ObituaryDataCreated 이벤트 데이터를 받아 부고장 이미지를 생성하고,
    생성된 이미지 객체를 반환합니다.
    """
    try:
        # 사용할 파일 경로 (경로 문제는 없는지 실행 환경에서 확인 필요)
        template_path = "resources/templates/template.png"
        font_path = "resources/fonts/NanumGothic.ttf"
        
        # 폰트 객체 생성
        font_title = ImageFont.truetype(font_path, size=40)
        font_main = ImageFont.truetype(font_path, size=26)
        font_list = ImageFont.truetype(font_path, size=24)
        font_mourner = ImageFont.truetype(font_path, size=28)
        
        # --- 텍스트 내용 가공 ---

        # [수정] 발인 날짜에서 요일 계산 시, 더 안전한 예외 처리 적용
        procession_date_with_day = event_data.processionDateTime
        if event_data.processionDateTime:
            try:
                # processionDateTime이 "YYYY년MM월DD일 HH시MM분" 같은 형식이라고 가정
                date_str = event_data.processionDateTime.split(' ')[0]
                dt_obj = datetime.strptime(date_str, "%Y년%m월%d일")
                weekdays = ['월', '화', '수', '목', '금', '토', '일']
                day_of_week = weekdays[dt_obj.weekday()]
                procession_date_with_day = event_data.processionDateTime.replace("일", f"일({day_of_week})")
            except (ValueError, IndexError) as e:
                # [수정] Bare except 대신 구체적인 예외를 잡고 로그를 남깁니다.
                print(f"⚠️ 발인 날짜 파싱 오류: {e}. 원본 데이터 '{event_data.processionDateTime}'를 그대로 사용합니다.", flush=True)
                # procession_date_with_day는 이미 원본 값으로 설정되어 있으므로 별도 처리는 불필요

        # 상주 목록 텍스트 만들기 (줄바꿈 처리)
        mourners = event_data.chiefMourners.replace(", ", "\n") if event_data.chiefMourners else ""

        # --- 이미지에 텍스트 쓰기 ---
        image = Image.open(template_path).convert("RGBA")
        draw = ImageDraw.Draw(image)
        text_color = (50, 50, 50)
        
        # 좌표 정의
        coordinates = {
            "title": (image.width / 2, 150),
            "intro": (image.width / 2, 250),
            "details_list": (170, 400),
            "mourner_title": (image.width / 2, 600),
            "mourner_list": (image.width / 2, 650),
            "closing": (image.width / 2, 800),
            "signature": (image.width / 2, 900)
        }

        # [개선] 텍스트 정렬 기준을 "ma"에서 "mm"으로 변경하여 수직/수평 중앙 정렬 정확도 향상
        # 제목
        draw.text(coordinates['title'], "【 부고 】", font=font_title, fill=text_color, anchor="mm")

        # [수정] 사망일(deceasedDate)이 문자열일 경우를 대비해 파싱 로직 추가
        deceased_date_str = "정보 없음"
        if event_data.deceasedDate:
            if isinstance(event_data.deceasedDate, str):
                dt_obj = datetime.fromisoformat(event_data.deceasedDate.replace('Z', ''))
            else:
                dt_obj = event_data.deceasedDate
            deceased_date_str = dt_obj.strftime("%Y년 %m월 %d일")

        # 서문
        intro_text = f"故 {event_data.deceasedName}님께서 향년 {event_data.deceasedAge}세를 일기로\n{deceased_date_str} 별세하셨기에 삼가 알려드립니다."
        draw.text(coordinates['intro'], intro_text, font=font_main, fill=text_color, spacing=10, align="center", anchor="mm")

        # 핵심 정보 목록 (좌측 정렬이므로 anchor 미사용)
        details_text = f"""
▪ 고인 : 故 {event_data.deceasedName}
▪ 빈소 : {event_data.mortuaryInfo}
▪ 발인 : {procession_date_with_day}
▪ 장지 : {event_data.burialSiteInfo}
        """
        draw.text(coordinates['details_list'], details_text, font=font_list, fill=text_color, spacing=12)

        # 상주 제목 및 목록
        draw.text(coordinates['mourner_title'], "【 상주 】", font=font_title, fill=text_color, anchor="mm")
        draw.text(coordinates['mourner_list'], mourners, font=font_mourner, fill=text_color, spacing=10, align="center", anchor="mm")

        # 맺음말
        closing_text = "경황이 없어 일일이 연락드리지 못함을\n널리 혜량하여 주시기 바랍니다."
        draw.text(coordinates['closing'], closing_text, font=font_main, fill=text_color, spacing=10, align="center", anchor="mm")

        # 서명 및 연락처
        signature_text = f"- {event_data.reporterRelationToDeceased} {event_data.reporterName} 올림 -\n연락처 : {event_data.reporterPhone}"
        draw.text(coordinates['signature'], signature_text, font=font_list, fill=text_color, spacing=10, align="center", anchor="mm")

        return image

    except Exception as e:
        print(f"❌ 부고 이미지 생성 중 오류 발생: {e}", flush=True)
        return None