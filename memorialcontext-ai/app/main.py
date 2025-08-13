# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/main.py
# 역할 : 
# ========================================

import threading
import uvicorn
from fastapi import FastAPI
from app.consumer import start_consumer # consumer.py에서 함수를 가져옵니다.

print("✅ main.py 파일 로드 완료", flush=True)

# FastAPI 애플리케이션 생성
app = FastAPI()

# FastAPI 앱이 시작될 때 실행되는 이벤트 핸들러
@app.on_event("startup")
async def startup_event():
    threading.Thread(target=start_consumer, daemon=True).start()
    print("✅ Kafka consumer 백그라운드에서 실행됨", flush=True)

# 서비스 상태 확인용 API
@app.get("/")
def health_check():
    return {"status": "AI Service is running"}

# Docker 환경에서는 CMD 명령어로 실행되므로, 이 부분은 직접 실행 시에만 사용됩니다.
if __name__ == "__main__":
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000)


