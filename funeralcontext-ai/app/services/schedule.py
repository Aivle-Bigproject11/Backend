# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/schedule.py
# 역할 : 장례 일정표 이미지 생성
# ========================================

from PIL import Image, ImageDraw, ImageFont
import qrcode
from app.schemas import ScheduleDataCreated

# [추가] 설정 파일 등에서 관리하는 것을 권장하는 외부 URL
BASE_URL = "https://www.your-real-service.com" 

def create_schedule_document(event_data: ScheduleDataCreated):
    """
    ScheduleDataCreated 이벤트 데이터를 받아 장례 일정표 이미지를 생성하고,
    생성된 이미지 객체를 반환합니다.
    """
    try:
        # 사용할 파일 경로
        template_path = "resources/templates/template.png" 
        font_path = "resources/fonts/NanumGothic.ttf"
        
        # 폰트 객체 생성
        font_title = ImageFont.truetype(font_path, size=40)
        font_info = ImageFont.truetype(font_path, size=28)
        font_day_title = ImageFont.truetype(font_path, size=30)
        font_task = ImageFont.truetype(font_path, size=26)
        font_check = ImageFont.truetype(font_path, size=22)
        font_footer = ImageFont.truetype(font_path, size=18)
        text_color = (50, 50, 50)

        image = Image.open(template_path).convert("RGBA")
        draw = ImageDraw.Draw(image)

        # --- 이미지에 텍스트 그리기 ---
        # 제목
        title_text = f"故 {event_data.deceasedName}님 장례 일정표"
        draw.text((image.width / 2, 80), title_text, font=font_title, fill=text_color, anchor="mt")

        # 기본 정보
        info_text = f"""
■ 장례기간 : {event_data.funeralDuration or '3일장'}
■ 빈      소 : {event_data.mortuaryInfo or '정보 없음'}
■ 발      인 : {event_data.processionDateTime or '정보 없음'}
■ 장      지 : {event_data.burialSiteInfo or '정보 없음'}
        """
        # [수정] 여러 줄 텍스트에서는 anchor 옵션을 지원하지 않으므로 제거합니다.
        draw.text((120, 160), info_text, font=font_info, fill=text_color, spacing=15)

        # 상세 일정 (고정된 예시)
        y_pos = 350
        
        # --- 1일차 ---
        draw.text((120, y_pos), "[ 1일차 : 소천 및 안치 ]", font=font_day_title, fill=text_color, anchor="lt")
        y_pos += 50
        draw.text((150, y_pos), "- 장례식장 안치 및 빈소 선택", font=font_task, fill=text_color, anchor="lt")
        y_pos += 40
        draw.text((150, y_pos), "- 부고 알림 및 조문객 맞이", font=font_task, fill=text_color, anchor="lt")
        y_pos += 70

        # --- 2일차 ---
        draw.text((120, y_pos), "[ 2일차 : 입관 및 성복 ]", font=font_day_title, fill=text_color, anchor="lt")
        y_pos += 50
        draw.text((150, y_pos), "- 고인 입관 및 성복(상복 착용)", font=font_task, fill=text_color, anchor="lt")
        y_pos += 40
        draw.text((150, y_pos), "- 장례 용품 확인 및 조문객 맞이", font=font_task, fill=text_color, anchor="lt")
        y_pos += 70

        # --- 3일차 ---
        draw.text((120, y_pos), "[ 3일차 : 발인 및 장지 ]", font=font_day_title, fill=text_color, anchor="lt")
        y_pos += 50
        draw.text((150, y_pos), "- 발인제 및 운구", font=font_task, fill=text_color, anchor="lt")
        y_pos += 40
        draw.text((150, y_pos), "- 화장 및 장지로 이동", font=font_task, fill=text_color, anchor="lt")

        # QR코드 생성 및 삽입
        qr_data = f"{BASE_URL}/funeral-infos/{event_data.funeralInfoId}"
        qr = qrcode.QRCode(
            version=1,
            error_correction=qrcode.constants.ERROR_CORRECT_L,
            box_size=4,
            border=2,
        )
        qr.add_data(qr_data)
        qr.make(fit=True)
        qr_img = qr.make_image(fill_color="black", back_color="white").resize((120, 120))

        qr_position = (image.width - qr_img.width - 80, image.height - qr_img.height - 80)
        image.paste(qr_img, qr_position)
        draw.text((qr_position[0] + qr_img.width / 2, qr_position[1] + qr_img.height + 10), 
                  "상세정보 (QR)", font=font_check, fill=text_color, anchor="mt")

        # 하단 정보 텍스트 기입
        footer_text = f"총괄 장례지도사: {event_data.directorName or ''} ({event_data.directorPhone or ''})"
        draw.text((120, image.height - 80), footer_text, font=font_footer, fill=text_color, anchor="lt")
        
        return image

    except Exception as e:
        print(f"❌ 장례 일정표 이미지 생성 중 오류 발생: {e}", flush=True)
        return None