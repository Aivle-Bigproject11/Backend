# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/consumer.py
# 역할 : Kafka 이벤트를 수신하여 각 서비스에 작업을 요청하고, 그 결과를 다시 Kafka로 전송
# ========================================

import json
import asyncio # [추가] 비동기 처리를 위한 asyncio 라이브러리
# [수정] 동기 라이브러리 대신 비동기 AIOKafka 라이브러리를 import 합니다.
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
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

# [수정] start_consumer 함수를 비동기(async) 함수로 변경합니다.
async def start_consumer():
    """
    AIOKafka Consumer를 비동기적으로 시작하고 메시지를 처리합니다.
    """
    print("📡 비동기 Kafka Consumer 시작", flush=True)

    # [수정] AIOKafkaConsumer와 AIOKafkaProducer를 초기화합니다.
    consumer = AIOKafkaConsumer(
        TOPIC_NAME,
        bootstrap_servers=KAFKA_BROKER_URL,
        group_id="test-ai-group-async",
        auto_offset_reset="latest", # latest로 변경하여 재시작 시 과거 메시지를 읽지 않도록 합니다.
        value_deserializer=lambda m: json.loads(m.decode("utf-8"))
    )
    producer = AIOKafkaProducer(bootstrap_servers=KAFKA_BROKER_URL)

    # [추가] 비동기적으로 Kafka 클라이언트를 시작합니다.
    await consumer.start()
    await producer.start()

    try:
        # [수정] 'for message in consumer:' 대신 'async for message in consumer:'를 사용합니다.
        async for message in consumer:
            event_type = message.value.get("eventType")
            print(f"\n📥 수신 이벤트: {event_type}", flush=True)
            
            if not blob_service_client:
                print("❌ Azure 클라이언트가 연결되지 않아 파일 처리를 건너뜁니다.")
                continue

            try:
                # [핵심] 이미지/PDF 생성은 CPU를 많이 사용하는 동기 작업이므로,
                # 비동기 이벤트 루프를 막지 않도록 별도의 스레드에서 실행합니다.
                loop = asyncio.get_running_loop()
                result = None

                # 1. 부고 이미지 생성 이벤트 처리
                if event_type == "ObituaryDataCreated":
                    event_data = ObituaryDataCreated(**message.value)
                    doc_id = event_data.obituaryId
                    print(f"  -> 부고 이미지 생성 작업 시작 (ID: {doc_id})")
                    
                    result = await loop.run_in_executor(
                        None, create_obituary_document, event_data, blob_service_client, AZURE_CONTAINER_NAME
                    )

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
                        # [수정] producer.send().flush() 대신 await producer.send_and_wait()를 사용합니다.
                        await producer.send_and_wait(TOPIC_NAME, json.dumps(generated_event).encode("utf-8"), headers=[("type", b"ObituaryDocumentGenerated")])
                        print(f"  📤 'ObituaryDocumentGenerated' 이벤트 전송 완료")
                    else:
                        print(f"❌ 부고 이미지 생성/업로드 실패", flush=True)

                # 2. 사망진단서 PDF 생성 이벤트 처리 (부고장과 동일한 패턴으로 수정)
                elif event_type == "DeathReportDataCreated":
                    event_data = DeathReportDataCreated(**message.value)
                    doc_id = event_data.deathReportId
                    print(f"  -> 사망진단서 PDF 생성 작업 시작 (ID: {doc_id})")
                    
                    result = await loop.run_in_executor(
                        None, create_death_report_document, event_data, blob_service_client, AZURE_CONTAINER_NAME
                    )
                    
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
                        await producer.send_and_wait(TOPIC_NAME, json.dumps(generated_event).encode("utf-8"), headers=[("type", b"DeathReportDocumentGenerated")])
                        print(f"  📤 'DeathReportDocumentGenerated' 이벤트 전송 완료")
                    else:
                        print(f"❌ 사망진단서 PDF 생성/업로드 실패", flush=True)

                # 3. 장례일정표 이미지 생성 이벤트 처리 (부고장과 동일한 패턴으로 수정)
                elif event_type == "ScheduleDataCreated":
                    event_data = ScheduleDataCreated(**message.value)
                    doc_id = event_data.scheduleId
                    print(f"  -> 장례일정표 이미지 생성 작업 시작 (ID: {doc_id})")
                    
                    result = await loop.run_in_executor(
                        None, create_schedule_document, event_data, blob_service_client, AZURE_CONTAINER_NAME
                    )

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
                        await producer.send_and_wait(TOPIC_NAME, json.dumps(generated_event).encode("utf-8"), headers=[("type", b"ScheduleDocumentGenerated")])
                        print(f"  📤 'ScheduleDocumentGenerated' 이벤트 전송 완료")
                    else:
                        print(f"❌ 장례일정표 이미지 생성/업로드 실패", flush=True)
            
            except Exception as e:
                print(f"❌ 이벤트 처리 중 최상위 오류 발생: {e}", flush=True)
    finally:
        # [추가] 서비스 종료 시 Kafka 클라이언트를 안전하게 중지합니다.
        await consumer.stop()
        await producer.stop()
        print("📡 비동기 Kafka Consumer 종료", flush=True)
