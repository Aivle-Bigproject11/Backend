from fastapi import FastAPI
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
import asyncio
import json
import logging
from contextlib import asynccontextmanager
from deathPredict import train_and_save_models, RegionalMonthlyDeathModels
import joblib
import os
import pandas as pd

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
KAFKA_TOPIC = "aivlebigproject"


class AppState:
    def __init__(self):
        self.producer = None
        self.consumer = None
        self.ai_models = None

app_state = AppState()


@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("### Fast API 애플리케이션 시작: 리소스 초기화")
    try:
        # Kafka 설정
        app_state.producer = AIOKafkaProducer(bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS)
        app_state.consumer = AIOKafkaConsumer(
            KAFKA_TOPIC,
            bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
            group_id="fastapi-prediction-group",
            auto_offset_reset='earliest'
        )
        await app_state.producer.start()
        await app_state.consumer.start()
        logger.info("### Kafka Producer 및 Consumer 연결 성공")

        # AI 모델 로드 또는 학습
        model_filename = 'regional_models.joblib'
        if os.path.exists(model_filename):
            app_state.ai_models = joblib.load(model_filename)
            logger.info("### 기존 AI 모델 파일 로드 성공")
        else:
            app_state.ai_models = train_and_save_models('지역월별_사망자수_데이터.csv', model_filename)
            logger.info("### AI 모델 신규 학습 및 저장 완료")

        # ✅ Kafka Consumer Listener 시작
        asyncio.create_task(kafka_consumer_listener())
        logger.info("🔥 Kafka Consumer 백그라운드 태스크 시작됨")

    except Exception as e:
        logger.error(f"### 리소스 초기화 중 오류 발생: {e}")
        raise RuntimeError("FastAPI 초기화 실패")

    yield

    logger.info("### Fast API 애플리케이션 종료: 리소스 정리")
    if app_state.producer:
        await app_state.producer.stop()
    if app_state.consumer:
        await app_state.consumer.stop()
    logger.info("### Kafka Producer 및 Consumer 연결 해제 완료")


app = FastAPI(lifespan=lifespan)


async def kafka_consumer_listener():
    """Kafka 메시지를 수신하고 처리하는 백그라운드 태스크"""
    try:
        async for msg in app_state.consumer:
            logger.info(f"### Kafka 메시지 수신: topic={msg.topic}, partition={msg.partition}, value={msg.value.decode('utf-8')}")

            try:
                payload = json.loads(msg.value.decode('utf-8'))

                date_str = payload.get('date')
                region = payload.get('region')

                if not date_str:
                    logger.warning("### 필수 필드(date)가 누락되었습니다. 메시지 무시.")
                    continue

                start_date = pd.to_datetime(f"{date_str}-01").date()

                if not region or region == "전국":
                    logger.info(f"### 예측 요청 처리: 전국 전체, 시작일: {date_str}")
                    predictions = app_state.ai_models.predict_all_regions(start_date)
                else:
                    logger.info(f"### 예측 요청 처리: region={region}, 시작일: {date_str}")
                    predictions = app_state.ai_models.predict_next_months(region, start_date)

                for prediction in predictions:
                    event_payload = {
                        "eventType": "DeathPredictionEvent",
                        "date": prediction["date"],
                        "region": prediction["region"],
                        "predictedDeaths": prediction["predictedDeaths"]
                    }

                    event_json = json.dumps(event_payload).encode('utf-8')

                    await app_state.producer.send_and_wait(
                        KAFKA_TOPIC,
                        event_json,
                        headers=[('eventType', b'DeathPredictionEvent')]
                    )
                    logger.info(f"### 예측 결과 Kafka 발행 성공. 토픽: {KAFKA_TOPIC}, 이벤트: {event_payload}")

            except json.JSONDecodeError:
                logger.error("### JSON 디코딩 오류 발생.")
            except ValueError as ve:
                logger.error(f"### 예측 처리 중 오류 발생: {ve}")
            except Exception as e:
                logger.error(f"### 예측 또는 발행 중 예상치 못한 오류 발생: {e}")

    except asyncio.CancelledError:
        logger.info("### Kafka Consumer 백그라운드 태스크 종료.")
    finally:
        await app_state.consumer.stop()
        logger.info("### Kafka Consumer 연결 해제.")


@app.get("/")
async def health_check():
    return {"status": "ok", "message": "FastAPI is running and ready to process Kafka messages."}