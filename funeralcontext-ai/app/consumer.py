# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/consumer.py
# 역할 : Kafka 이벤트를 수신하여 각 서비스에 작업을 요청하고, 그 결과를 다시 Kafka로 전송
# ========================================

import json
from kafka import KafkaConsumer, KafkaProducer
from datetime import datetime, timezone
from pathlib import Path
from app.schemas import ObituaryDataCreated, DeathReportDataCreated, ScheduleDataCreated
from app.services.obituary import create_obituary_document
from app.services.deathreport import create_death_report_document
from app.services.schedule import create_schedule_document

################################
# Azure blob
import io
import os
from azure.storage.blob import BlobServiceClient # Azure Blob 라이브러리 import
from app.services.azure_uploader import upload_to_blob # 업로드 함수 import
AZURE_CONTAINER_NAME = "a071098container" # 업로드할 컨테이너 이름

# --- Azure Blob Storage 연결 ---
blob_service_client = None
try:
    connect_str = os.getenv('AZURE_STORAGE_CONNECTION_STRING')
    if not connect_str:
        print("⚠️ AZURE_STORAGE_CONNECTION_STRING 환경 변수가 설정되지 않았습니다.", flush=True)
    else:
        blob_service_client = BlobServiceClient.from_connection_string(connect_str)
        print("☁️ Azure Blob Storage에 성공적으로 연결되었습니다.", flush=True)
except Exception as e:
    print(f"❌ Azure Blob Storage 연결 실패: {e}", flush=True)

################################
KAFKA_BROKER_URL = "my-kafka:9092"
TOPIC_NAME = "aivlebigproject"

consumer = KafkaConsumer(
    TOPIC_NAME,
    bootstrap_servers=KAFKA_BROKER_URL,
    group_id="test-ai-group",
    value_deserializer=lambda m: json.loads(m.decode("utf-8")),
    auto_offset_reset="earliest"
)

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER_URL,
    value_serializer=lambda m: json.dumps(m).encode("utf-8")
)

def start_consumer():
    """
    Kafka Consumer를 시작하고 수신된 이벤트에 따라 각 서비스 모듈에 문서 생성을 위임한 뒤,
    그 결과를 받아 다시 이벤트를 발행합니다.
    """
    print("📡 Kafka Consumer 시작", flush=True)

    for message in consumer:
        event_type = message.value.get("eventType")
        print(f"\n📥 수신 이벤트: {event_type}", flush=True)
        
        if not blob_service_client:
            print("❌ Azure 클라이언트가 연결되지 않아 파일 처리를 건너뜁니다.")
            continue

        try:
            # 1. 부고 이미지 생성 이벤트 처리
            if event_type == "ObituaryDataCreated":
                event_data = ObituaryDataCreated(**message.value)
                doc_id = event_data.obituaryId
                print(f"  -> 부고 이미지 생성 작업 시작 (ID: {doc_id})")
                
                # [수정] obituary.py에 모든 작업을 위임하고 결과(dict)만 받습니다.
                result = create_obituary_document(event_data, blob_service_client, AZURE_CONTAINER_NAME)

                if result:
                    generated_event = {
                        "eventType": "ObituaryDocumentGenerated",
                        "obituaryId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "obituaryFileName": result["fileName"],
                        "obituaryFileUrl": result["fileUrl"],
                        "funeralHomeAddressUrl": result.get("funeralHomeAddressUrl"),
                        "obituaryStatus": "COMPLETED",
                        "obituaryCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"ObituaryDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'ObituaryDocumentGenerated' 이벤트 전송 완료")
                else:
                    print(f"❌ 부고 이미지 생성/업로드 실패", flush=True)

            # 2. 사망진단서 PDF 생성 이벤트 처리
            elif event_type == "DeathReportDataCreated":
                event_data = DeathReportDataCreated(**message.value)
                doc_id = event_data.deathReportId
                print(f"  -> 사망진단서 PDF 생성 작업 시작 (ID: {doc_id})")

                # [수정] deathreport.py에 모든 작업을 위임하고 결과(dict)만 받습니다.
                result = create_death_report_document(event_data, blob_service_client, AZURE_CONTAINER_NAME)
                
                if result:
                    generated_event = {
                        "eventType": "DeathReportDocumentGenerated",
                        "deathReportId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "deathReportFileName": result["fileName"],
                        "deathReportFileUrl": result["fileUrl"],
                        "deathReportStatus": "COMPLETED",
                        "deathReportCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"DeathReportDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'DeathReportDocumentGenerated' 이벤트 전송 완료")
                else:
                    print(f"❌ 사망진단서 PDF 생성/업로드 실패", flush=True)

            # 3. 장례일정표 이미지 생성 이벤트 처리
            elif event_type == "ScheduleDataCreated":
                event_data = ScheduleDataCreated(**message.value)
                doc_id = event_data.scheduleId
                print(f"  -> 장례일정표 이미지 생성 작업 시작 (ID: {doc_id})")

                # [수정] schedule.py에 모든 작업을 위임하고 결과(dict)만 받습니다.
                result = create_schedule_document(event_data, blob_service_client, AZURE_CONTAINER_NAME)

                if result:
                    generated_event = {
                        "eventType": "ScheduleDocumentGenerated",
                        "scheduleId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "scheduleDallePrompt": result["scheduleDallePrompt"],
                        "scheduleDalleTemplateImageUrl": result["scheduleDalleTemplateImageUrl"],
                        "scheduleFileName": result["scheduleFileName"],
                        "scheduleFileUrl": result["scheduleFileUrl"],
                        "scheduleStatus": "COMPLETED",
                        "scheduleCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"ScheduleDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'ScheduleDocumentGenerated' 이벤트 전송 완료")
                else:
                    print(f"❌ 장례일정표 이미지 생성/업로드 실패", flush=True)
        
        except Exception as e:
            print(f"❌ 이벤트 처리 중 최상위 오류 발생: {e}", flush=True)

if __name__ == "__main__":
    start_consumer()
