# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/consumer.py
# 역할 : Kafka 이벤트를 수신하여 각 서비스에 작업을 요청하고, 그 결과를 다시 Kafka로 전송
# ========================================

import json
import time
from datetime import datetime

from kafka import KafkaConsumer, KafkaProducer

from app.config import TEST_MODE
from app.image_converter import generate_image_to_video
from app.music_generator import generate_music_file
from app.schemas import VideoRequest


################################
import os

from app.utils import download_url
from app.video_creator import MemorialVideoCreator


################################
KAFKA_BROKER_URL = "my-kafka:9092"
KAFKA_GROUP_ID = "aivlebigproject"
TOPIC_NAME = "aivlebigproject"

consumer = KafkaConsumer(
    TOPIC_NAME,
    bootstrap_servers=KAFKA_BROKER_URL,
    group_id=KAFKA_GROUP_ID,
    value_deserializer=lambda m: json.loads(m.decode("utf-8")),
    auto_offset_reset="latest"
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

        try:
            # 1. 영상 생성 이벤트 들었을 때
            if event_type == "VideoRequested":
                event_data = VideoRequest(**message.value)
                video_id = event_data.videoId
                memorial_id = event_data.memorialId
                name = event_data.name
                birth = event_data.birthDate
                death = event_data.deathDate
                keywords = event_data.keywords
                photo_urls = event_data.imageUrls
                outro_url = event_data.outroImageUrl
                print("이벤트 데이터")
                print(event_data)

                # base_path는 현재 기준
                if TEST_MODE:
                    base_path = os.path.join("../temp/test")
                else:
                    base_path = os.path.join("../temp", memorial_id)
                os.makedirs(base_path, exist_ok=True)

                # 이미지 다운로드(테스트모드가 아니라면)
                if not TEST_MODE:
                    for photo_url in enumerate(photo_urls):
                        download_url(photo_url, memorial_id, "images")
                    download_url(outro_url, memorial_id, "outro")

                # 음악 생성
                if not TEST_MODE:
                    generate_music_file(base_path, keywords)  # 실행하면 api써서 음악 생성됨.

                # runway
                if not TEST_MODE:
                    generate_image_to_video(base_path)

                # 추모영상 생성
                creator = MemorialVideoCreator(base_path, memorialId=memorial_id, videoId=video_id)

                blob_url = creator.create_video(keywords, name, birth, death, len(photo_urls))

                if blob_url:
                    generated_event = {
                        "eventType": "VideoCreated",
                        "videoId": int(video_id),
                        "videoUrl": blob_url,
                        "timestamp": (datetime.now().timestamp() * 1000)
                    }
                    producer.send(TOPIC_NAME, value=generated_event, headers=[("type", b"VideoCreated")])
                    producer.flush()
                    print(f"  📤 'VideoCreated' 이벤트 전송 완료")
                else:
                    print(f"❌ 추모영상 실패", flush=True)





        
        except Exception as e:
            print(f"❌ 이벤트 처리 중 최상위 오류 발생: {e}", flush=True)

if __name__ == "__main__":
    start_consumer()
