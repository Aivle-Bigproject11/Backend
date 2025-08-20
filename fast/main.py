from fastapi import FastAPI
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
from contextlib import asynccontextmanager
import asyncio, json, os, joblib, logging, pandas as pd
# rf_model에서 필요한 함수들을 정확하게 import 합니다.
from rf_model import train_and_save_models, make_predictions

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
        data_file = './지역_월별_사망자수_데이터_최종.csv'


        app_state.ai_models = train_and_save_models(data_file, model_file)
        logger.info("✅ AI 모델 학습 및 저장 완료")

        asyncio.create_task(kafka_consumer_loop())
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
    logger.info(f"메시지 수신: {msg.value.decode('utf-8')}")

    headers = dict(msg.headers)
    event_type = None
    for k, v in msg.headers:
        if k == 'eventType':
            event_type = v.decode()
            break

    if event_type != "AiRequestEvent":
        logger.info(f"무시된 이벤트: eventType={event_type}")
        return

    payload = json.loads(msg.value.decode('utf-8'))
    date_str = payload.get("date")
    region = payload.get("region") # 📌 region 필드가 없어도 오류가 나지 않도록 get() 사용

    if not date_str:
        logger.warning("'date' 필드 없음. 메시지를 무시합니다.")
        return

    try:
        year, month = map(int, date_str.split('-'))
    except (ValueError, IndexError):
        logger.warning(f"잘못된 날짜 형식: {date_str}")
        return

    try:
        # region이 None이면 전체 지역에 대한 예측을 수행합니다.
        predictions = make_predictions(
            app_state.ai_models,
            app_state.ai_models['training_data'],
            year,
            list(range(1, 13)),
            region # 📌 region 값을 그대로 전달
        )
    except Exception as e:
        logger.error(f"🚨 예측 오류: {e}")
        return

    for prediction in predictions:
        result_payload = {
            "eventType": "DeathPredictionEvent",
            "date": prediction["date"],
            "region": prediction["regionName"],
            "predictedDeaths": prediction["predictedDeaths"],
            "growthRate": prediction["growthRate"],
            "previousYearDeaths": prediction["previousYearDeaths"]
        }
        if "regionalPercentage" in prediction:
            result_payload["regionalPercentage"] = prediction["regionalPercentage"]

        await app_state.producer.send_and_wait(
            KAFKA_TOPIC,
            json.dumps(result_payload).encode('utf-8'),
            headers=[
                ("contentType", b"application/json"),
                ("spring_json_header_types", b'{"eventType":"java.lang.String"}'),
                ("eventType", b"DeathPredictionEvent")
            ]
        )
        logger.info(f" 예측 결과 발행: {result_payload}")
        
@app.get("/")
async def health_check():
    return {"status": "ok"}