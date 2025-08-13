from fastapi import FastAPI
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
from contextlib import asynccontextmanager
import asyncio, json, os, joblib, logging, pandas as pd
from deathPredict import train_and_save_models

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "my-kafka:9092")
KAFKA_TOPIC = "aivlebigproject"

class AppState:
    def __init__(self):
        self.producer = None
        self.consumer = None
        self.ai_models = None

app_state = AppState()


@asynccontextmanager
async def lifespan(app: FastAPI):
    try:
        app_state.producer = AIOKafkaProducer(bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS)
        app_state.consumer = AIOKafkaConsumer(
            KAFKA_TOPIC,
            bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
            group_id="fastapi-prediction-group",
            auto_offset_reset='latest'
        )
        await app_state.producer.start()
        await app_state.consumer.start()
        logger.info("✅ Kafka 연결 성공")

        model_file = 'regional_models.joblib'
        if os.path.exists(model_file):
            app_state.ai_models = joblib.load(model_file)
            logger.info("✅ AI 모델 로드 완료")
        else:
            app_state.ai_models = train_and_save_models('지역월별_사망자수_데이터.csv', model_file)
            logger.info("✅ AI 모델 학습 및 저장 완료")

        asyncio.create_task(kafka_consumer_loop())  # ✅ 자동 처리 루프 등록

        yield

    finally:
        await app_state.consumer.stop()
        await app_state.producer.stop()
        logger.info("✅ Kafka 연결 해제 완료")


app = FastAPI(lifespan=lifespan)


async def kafka_consumer_loop():
    logger.info("📡 Kafka 메시지 소비 루프 시작")
    while True:
        try:
            await process_one_kafka_message()
        except Exception as e:
            logger.error(f"🚨 메시지 처리 오류: {e}")
        await asyncio.sleep(0.1)


async def process_one_kafka_message():
    msg = await app_state.consumer.getone()
    logger.info(f"📨 메시지 수신: {msg.value.decode('utf-8')}")
    logger.info(f"📨 수신 헤더 원본: {msg.headers}")

    headers = dict(msg.headers)
    event_type = None
    for k, v in msg.headers:
        if k =='eventType':
            event_type = v.decode()
            break

    if event_type != "AiRequestEvent":
        logger.info(f"⏩ 무시된 이벤트: eventType={event_type}")
        return {"status": "ignored", "reason": f"eventType={event_type}"}

    payload = json.loads(msg.value.decode('utf-8'))
    date_str, region = payload.get("date"), payload.get("region")
    if not date_str:
        logger.warning("❌ 'date' 필드 없음")
        return

    start_date = pd.to_datetime(f"{date_str}-01").date()

    if not region or region == "전국":
        predictions = app_state.ai_models.predict_all_regions(start_date)
    else:
        predictions = app_state.ai_models.predict_next_months(region, start_date)


    for prediction in predictions:
        result_payload = {
            "eventType": "DeathPredictionEvent",  # ✅ payload에 명시
            "date": prediction["date"],
            "region": prediction["region"],
            "predictedDeaths": prediction["predictedDeaths"]
        }

        await app_state.producer.send_and_wait(
            KAFKA_TOPIC,
            json.dumps(result_payload).encode('utf-8'),
            headers=[
                ("contentType", b"application/json"),
                ("spring_json_header_types", b'{"eventType":"java.lang.String"}'),
                ("eventType", b"DeathPredictionEvent")  # ✅ 여기를 바꾼 것!
            ]
        )

        logger.info(f"✅ 예측 결과 발행: {result_payload}")


@app.get("/")
async def health_check():
    return {"status": "ok"}
