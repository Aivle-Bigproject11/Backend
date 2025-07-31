# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/consumer.py
# 역할 : 
# ========================================

import json
from kafka import KafkaConsumer, KafkaProducer
from datetime import datetime, timezone
from pathlib import Path
from app.schemas import ObituaryDataCreated, DeathReportDataCreated, ScheduleDataCreated
from app.services.obituary import create_obituary_document # 함수 이름 변경
from app.services.deathreport import create_death_report_document # 함수 이름 변경
from app.services.schedule import create_schedule_document # schedule 함수 import

KAFKA_BROKER_URL = "kafka:9092"
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
    Kafka Consumer를 시작하고 수신된 이벤트 유형에 따라 
    개별적으로 문서 생성 및 관련 이벤트를 발행합니다.
    """
    print("📡 Kafka Consumer 시작", flush=True)

    for message in consumer:
        event_type = message.value.get("eventType")
        print(f"\n📥 수신 이벤트: {event_type}", flush=True)

        try:
            # 1. 부고 이미지 생성 이벤트 처리
            if event_type == "ObituaryDataCreated":
                event_data = ObituaryDataCreated(**message.value)
                doc_id = event_data.obituaryId
                print(f"  -> 부고 이미지 생성 작업 시작 (ID: {doc_id})")
                
                # 이미지 생성 함수 호출
                generated_doc = create_obituary_document(event_data)

                if generated_doc:
                    output_filename = f"obituary_{doc_id}.png"
                    output_path = f"/app/storage/{output_filename}"
                    file_url = f"http://localhost:8088/api/files/{output_filename}"
                    
                    # 이미지 파일 저장
                    generated_doc.save(output_path)
                    print(f"  📄 파일 생성 완료: {output_path}", flush=True)

                    # 완료 이벤트 생성 및 전송
                    generated_event = {
                        "eventType": "ObituaryDocumentGenerated",
                        "obituaryId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "obituaryFilePath": output_path,
                        "obituaryFileUrl": file_url,
                        "obituaryStatus": "COMPLETED",
                        "obituaryCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"ObituaryDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'ObituaryDocumentGenerated' 이벤트 전송 완료", flush=True)
                else:
                    print(f"❌ 부고 이미지 생성 실패", flush=True)

            # 2. 사망진단서 PDF 생성 이벤트 처리
            elif event_type == "DeathReportDataCreated":
                event_data = DeathReportDataCreated(**message.value)
                doc_id = event_data.deathReportId
                print(f"  -> 사망진단서 PDF 생성 작업 시작 (ID: {doc_id})")

                # PDF 생성 함수 호출
                generated_doc = create_death_report_document(event_data)
                
                if generated_doc:
                    output_filename = f"death_report_{doc_id}.pdf"
                    output_path = f"/app/storage/{output_filename}"
                    file_url = f"http://localhost:8088/api/files/{output_filename}"
                    
                    # PDF 파일 저장
                    generated_doc.save(output_path)
                    generated_doc.close() # PDF 파일은 닫아주는 것이 좋습니다.
                    print(f"  📄 파일 생성 완료: {output_path}", flush=True)

                    # 완료 이벤트 생성 및 전송
                    generated_event = {
                        "eventType": "DeathReportDocumentGenerated",
                        "deathReportId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "deathReportFilePath": output_path,
                        "deathReportFileUrl": file_url,
                        "deathReportStatus": "COMPLETED",
                        "deathReportCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"DeathReportDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'DeathReportDocumentGenerated' 이벤트 전송 완료", flush=True)
                else:
                    print(f"❌ 사망진단서 PDF 생성 실패", flush=True)

            # 3. 장례일정표 이미지 생성 이벤트 처리
            elif event_type == "ScheduleDataCreated":
                event_data = ScheduleDataCreated(**message.value)
                doc_id = event_data.scheduleId
                print(f"  -> 장례일정표 이미지 생성 작업 시작 (ID: {doc_id})")

                # 이미지 생성 함수 호출
                generated_doc = create_schedule_document(event_data)

                if generated_doc:
                    output_filename = f"schedule_{doc_id}.png"
                    output_path = f"/app/storage/{output_filename}"
                    file_url = f"http://localhost:8088/api/files/{output_filename}"

                    # 이미지 파일 저장
                    generated_doc.save(output_path)
                    print(f"  📄 파일 생성 완료: {output_path}", flush=True)

                    # 완료 이벤트 생성 및 전송
                    generated_event = {
                        "eventType": "ScheduleDocumentGenerated",
                        "scheduleId": doc_id,
                        "funeralInfoId": event_data.funeralInfoId,
                        "scheduleFilePath": output_path,
                        "scheduleFileUrl": file_url,
                        "scheduleStatus": "COMPLETED",
                        "scheduleCreatedAt": datetime.now(timezone.utc).isoformat()
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"ScheduleDocumentGenerated")])
                    producer.flush()
                    print(f"  📤 'ScheduleDocumentGenerated' 이벤트 전송 완료", flush=True)
                else:
                    print(f"❌ 장례일정표 이미지 생성 실패", flush=True)
        
        except Exception as e:
            print(f"❌ 이벤트 처리 중 최상위 오류 발생: {e}", flush=True)
