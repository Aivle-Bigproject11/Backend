# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/services/schedule.py
# 역할 : 장례 일정표 이미지 생성 (종교별 상세 내용 적용)
# ========================================

import io
import os
import base64
from openai import OpenAI
from PIL import Image, ImageDraw, ImageFont
import img2pdf # [추가] PNG를 PDF로 변환하기 위한 라이브러리
from datetime import datetime, timezone
from app.schemas import ScheduleDataCreated
from .azure_uploader import upload_to_blob # [주석] Azure 업로드 함수를 import 합니다.

# --- [추가] OpenAI 클라이언트 초기화 ---
# .env 파일에 OPENAI_API_KEY="sk-..." 형식으로 키를 저장해야 합니다.
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# =======================================================================
# [추가] GPT를 이용한 배경 이미지 생성 함수
# =======================================================================
def generate_and_upload_template(event_data: ScheduleDataCreated, blob_service_client, container_name: str) -> dict:
    """
    이벤트 데이터를 기반으로 프롬프트를 생성하고, GPT를 호출하여
    배경 이미지 데이터(bytes)를 반환합니다.
    """
    try:
        print("🤖 GPT 이미지 생성을 위한 프롬프트를 생성합니다...", flush=True)
        religion = event_data.deceasedReligion
        keyword = event_data.templateKeyword

        prompt = (
            f"연한 수채화 느낌의 장례식 일정표 세로형 배경 이미지를 그려줘. "
            f"중앙에는 텍스트를 넣을 수 있도록 넓은 여백을 두고, "
            f"'{keyword}' 키워드의 분위기에 맞춰 색조와 분위기를 변주하되 톤은 차분하고 정중하게 표현해줘. "
            f"너무 밝거나 채도가 높은 색은 피하고, 키워드에 맞는 은은한 색상 포인트를 사용해. "
            f"텍스트 가독성을 위해 질감과 패턴은 최소화하고, 불필요한 장식은 넣지 마. "
            f"텍스트·문장·알파벳은 절대 포함하지 마."
        )
        # prompt = (
        #     '{religion}' 종교의 상징성과 f"연한 수채화 느낌의 장례식 일정표 세로형 배경 이미지를 그려줘. "
        #     f"중앙에는 텍스트를 넣을 수 있도록 넓은 여백을 두고, "
        #     f"'{religion}' 종교의 상징성과 '{keyword}' 키워드의 분위기를 은은하게 반영해 차분하고 정중하게 표현해줘. "
        #     f"배경색은 너무 밝거나 채도가 높은 색을 피하고, 종교와 키워드에 맞게 색감을 자연스럽게 선택해. "
        #     f"텍스트 가독성을 위해 질감과 패턴은 최소화하고, 불필요한 장식이나 특정 종교를 직접적으로 나타내는 그림(예: 십자가, 불상)은 넣지 마. "
        #     f"오직 배경 그래픽만 포함된 단순하고 미니멀한 이미지여야 하며, 텍스트·문장·알파벳은 절대 포함하지 마."
        # )
        print(f"   - 생성된 프롬프트: {prompt}", flush=True)
        print("🎨 이미지 생성 중입니다...", flush=True)
        
        result = client.images.generate(
            model="gpt-image-1",
            prompt=prompt,
            size="1024x1536",  # 1024x1024`, `1536x1024` (landscape), `1024x1536` (portrait), 또는 `auto`
            quality="low" # high, medium, low
        )
        
        image_base64 = result.data[0].b64_json
        image_bytes = base64.b64decode(image_base64)
        print("🖼️ 이미지 생성 완료!", flush=True)
        
        # [주석] 생성된 원본 이미지를 Azure Blob에 업로드합니다.
        doc_id = event_data.scheduleId
        time_stamp = datetime.now(timezone.utc).strftime('%Y%m%d%H%M%S')
        template_blob_name = f"schedule-templates/template_{doc_id}_{time_stamp}.png"
        template_url = upload_to_blob(blob_service_client, container_name, template_blob_name, image_bytes)

        if template_url:
            # [주석] 성공 시, 프롬프트, 원본 이미지 URL, 원본 이미지 바이트를 반환합니다.
            return {
                "prompt": prompt,
                "templateImageUrl": template_url,
                "imageBytes": image_bytes
            }
        else:
            return None

    except Exception as e:
        print(f"❌ GPT 이미지 생성 중 오류 발생: {e}", flush=True)
        return None

def create_schedule_document(event_data: ScheduleDataCreated, blob_service_client, container_name: str) -> dict:

    try:
        # --- 1. GPT를 이용해 배경 이미지 생성 ---
        generation_result = generate_and_upload_template(event_data, blob_service_client, container_name)

        # 이미지 생성에 실패하면 함수를 종료합니다.
        if not generation_result:
            return None
        
        prompt_text = generation_result["prompt"]
        template_image_url = generation_result["templateImageUrl"]
        background_image_bytes = generation_result["imageBytes"]

        # --- 2. 생성된 이미지 위에 텍스트 작성 ---
        image = Image.open(io.BytesIO(background_image_bytes)).convert("RGBA")
        draw = ImageDraw.Draw(image)
        
        # 사용할 파일 경로
        # template_path = "resources/templates/template.png" 
        font_path = "resources/fonts/NanumGothic.ttf"
        
        # 폰트 객체 생성
        font_title = ImageFont.truetype(font_path, size=40)
        font_info = ImageFont.truetype(font_path, size=28)
        font_day_title = ImageFont.truetype(font_path, size=30)
        font_task = ImageFont.truetype(font_path, size=26)
        font_check = ImageFont.truetype(font_path, size=22)
        # [주석] 하단 폰트 크기가 본문보다 커서, 가독성을 위해 18로 조정했습니다.
        font_footer = ImageFont.truetype(font_path, size=18)
        text_color = (50, 50, 50)

        # image = Image.open(template_path).convert("RGBA")
        # draw = ImageDraw.Draw(image)

        # --- [주석] 발인 일시를 보기 좋은 형식으로 바꾸는 로직을 추가했습니다. ---
        procession_datetime_formatted = event_data.processionDateTime
        if event_data.processionDateTime:
            try:
                # ISO 8601 형식의 문자열("...T...Z")을 datetime 객체로 변환
                dt_obj = datetime.fromisoformat(event_data.processionDateTime.replace('Z', '+00:00'))
                # 원하는 형식의 문자열로 포맷팅
                procession_datetime_formatted = dt_obj.strftime('%Y년 %m월 %d일 %H시 %M분')
            except ValueError:
                # 형식이 맞지 않을 경우 원본 데이터를 그대로 사용
                procession_datetime_formatted = event_data.processionDateTime


        # --- 이미지에 텍스트 그리기 ---
        # 제목
        title_text = f"故 {event_data.deceasedName}님 장례 일정표"
        draw.text((image.width / 2, 200), title_text, font=font_title, fill=text_color, anchor="mt")

        # 기본 정보
        info_text = f"""
■ 장례기간 : {event_data.funeralDuration or ''}
■ 상주 : {event_data.chiefMourners or ''}
■ 빈소 : {event_data.funeralHomeName} {event_data.mortuaryInfo or ''}
■ 발인 : {procession_datetime_formatted or ''}
■ 장지 : {event_data.burialSiteInfo or ''}
        """.strip()
        draw.text((120, 280), info_text, font=font_info, fill=text_color, spacing=15)

        # --- 종교별 상세 일정 그리기 ---
        y_pos = 510
        religion = event_data.deceasedReligion or "무교"
        
        # 공통 스타일 변수
        task_indent = 150
        check_indent = 180
        line_indent = 100
        day_spacing = 30
        task_spacing = 50
        check_spacing = 40

        if religion == "개신교":
            # 1일차
            draw.text((120, y_pos), "[ 1일차 : 소천 및 위로예배 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 빈소 마련 및 조문객 맞이", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 부고 알림, 교회 장례 위원회 연락", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 위로예배", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 예배 공간 및 시간 확인, 목사님 맞이 준비", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 2일차
            draw.text((120, y_pos), "[ 2일차 : 입관예배 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 입관예배", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 고인의 유품(성경책, 찬송가) 준비", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 3일차
            draw.text((120, y_pos), "[ 3일차 : 발인예배 및 안치 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 발인예배 및 운구", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 장례식장 비용 정산, 운구 차량 확인", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 화장 및 장지로 이동", font=font_task, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 하관예배 및 안치", font=font_task, fill=text_color, anchor="lt")

        elif religion == "천주교":
            # 1일차
            draw.text((120, y_pos), "[ 1일차 : 위령기도 (연도) ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 빈소 준비 및 조문객 맞이", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 제대 준비(십자고상, 초, 향 등)", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 위령기도 (연도)", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 연도단(연령회)과 기도 시간 조율", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 2일차
            draw.text((120, y_pos), "[ 2일차 : 입관 예절 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 입관 예절", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 성수, 십자가 등 예절에 필요한 물품 확인", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 3일차
            draw.text((120, y_pos), "[ 3일차 : 장례 미사 및 사도예절 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 출관 예절 및 운구", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 운구 봉사자 확인, 장례 미사 시간 안내", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 화장(또는 매장) 후 장지로 이동", font=font_task, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 장례 미사 및 사도예절", font=font_task, fill=text_color, anchor="lt")

        elif religion == "불교":
            # 1일차
            draw.text((120, y_pos), "[ 1일차 : 다라니경 독송 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 빈소 마련 및 조문객 맞이", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 영단 준비(향, 초, 과일, 위패 등)", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 스님 독경 및 염불", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 독경 및 염불 시간 확인", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 2일차
            draw.text((120, y_pos), "[ 2일차 : 입관식 및 성복제 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 입관식 및 성복제", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 관, 수의 등 입관 용품 확인", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 3일차
            draw.text((120, y_pos), "[ 3일차 : 발인제 및 다비식 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 발인제 및 운구", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 노잣돈 준비, 운구 행렬 순서 확인", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 다비식(화장) 및 봉안식", font=font_task, fill=text_color, anchor="lt")

        else: # 무교 또는 기타
            # 1일차
            draw.text((120, y_pos), "[ 1일차 : 안치 및 조문 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 장례식장 안치 및 빈소 선택", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 부고 알림, 영정 사진 준비", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 2일차
            draw.text((120, y_pos), "[ 2일차 : 입관 및 성복 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 고인 입관 및 성복(상복 착용)", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 발인 준비 최종 점검 (운구차량, 서류 등)", font=font_check, fill=text_color, anchor="lt")
            y_pos += day_spacing
            draw.line([(line_indent, y_pos), (image.width - line_indent, y_pos)], fill=(220, 220, 220), width=1)
            y_pos += day_spacing
            # 3일차
            draw.text((120, y_pos), "[ 3일차 : 발인 및 장지 ]", font=font_day_title, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 발인 및 운구", font=font_task, fill=text_color, anchor="lt")
            y_pos += check_spacing
            draw.text((check_indent, y_pos), "□ 장례식장 비용 정산", font=font_check, fill=text_color, anchor="lt")
            y_pos += task_spacing
            draw.text((task_indent, y_pos), "- 화장 및 장지로 이동", font=font_task, fill=text_color, anchor="lt")

        # 하단 정보 텍스트 기입
        # [주석] 하단 텍스트가 잘리지 않도록 y 좌표를 조정했습니다.
        footer_text = f"장례 문의 : {event_data.directorName or ''} ({event_data.directorPhone or ''})"
        draw.text((120, image.height - 100), footer_text, font=font_footer, fill=text_color, anchor="lt")
        
        # --- [수정 시작] PNG 생성 후 PDF로 변환하여 업로드 ---
        
        # 3-1. 텍스트가 추가된 최종 이미지를 PNG 형식의 바이트 데이터로 변환
        final_img_byte_arr = io.BytesIO()
        image.save(final_img_byte_arr, format='PNG')
        png_bytes = final_img_byte_arr.getvalue()

        # 3-2. [추가] PNG 바이트 데이터를 PDF 바이트 데이터로 변환
        pdf_bytes = img2pdf.convert(png_bytes)
        
        # 3-3. [수정] Azure Blob에 PDF 데이터를 업로드
        doc_id = event_data.scheduleId
        time_stamp = datetime.now(timezone.utc).strftime('%Y%m%d%H%M%S')
        # [수정] 파일 확장자를 .pdf로 변경
        final_blob_name = f"schedules/schedule_{doc_id}_{time_stamp}.pdf"
        # [수정] 업로드할 데이터를 png_bytes 대신 pdf_bytes로 변경
        final_file_url = upload_to_blob(blob_service_client, container_name, final_blob_name, pdf_bytes)
        
        # --- [수정 완료] ---

        if final_file_url:
            # --- 4. 최종 결과값을 담은 딕셔너리 반환 ---
            return {
                "scheduleDallePrompt": prompt_text,
                "scheduleDalleTemplateImageUrl": template_image_url,
                "scheduleFileName": final_blob_name,
                "scheduleFileUrl": final_file_url
            }
        else:
            return None

    except Exception as e:
        print(f"❌ 최종 장례 일정표 생성 중 오류 발생: {e}", flush=True)
        return None
