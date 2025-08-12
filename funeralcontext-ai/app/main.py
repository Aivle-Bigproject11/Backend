# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/main.py
# 역할 : 
# ========================================

# import threading
import asyncio # [수정] threading 대신 asyncio를 import 합니다.
import uvicorn
from fastapi import FastAPI
from app.consumer import start_consumer # 이제 이 함수는 비동기(async) 함수입니다.

print("✅ main.py 파일 로드 완료", flush=True)

# FastAPI 애플리케이션 생성
app = FastAPI()

# FastAPI 앱이 시작될 때 실행되는 이벤트 핸들러
@app.on_event("startup")
async def startup_event():
    # [수정] threading.Thread 대신, asyncio.create_task를 사용하여
    # 비동기 함수인 start_consumer를 FastAPI의 이벤트 루프 안에서
    # 백그라운드 작업으로 안전하게 실행합니다.
    asyncio.create_task(start_consumer())
    print("✅ Kafka consumer 백그라운드 작업으로 등록됨", flush=True)

# 서비스 상태 확인용 API
@app.get("/")
def health_check():
    return {"status": "AI Service is running"}

# Docker 환경에서는 CMD 명령어로 실행되므로, 이 부분은 직접 실행 시에만 사용됩니다.
if __name__ == "__main__":
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000)


