# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/Dockerfile
# 역할 : 
# ========================================

# 1. 베이스 이미지 설정
# Python 3.9 버전의 가벼운(slim) 리눅스 환경에서 시작합니다.
FROM python:3.9-slim

# 📌 이 설정은 Python의 표준 출력(print)이 즉시 터미널로 flush되도록 만듭니다.
ENV PYTHONUNBUFFERED=1

# 2. 작업 디렉토리 설정
# 컨테이너 내부에 /app 이라는 폴더를 만들고, 앞으로의 모든 작업을 여기서 수행합니다.
WORKDIR /app

# 3. 라이브러리 설치
# 먼저 requirements.txt 파일만 복사하여 라이브러리를 설치합니다.
# (이렇게 하면, 코드만 수정될 경우 라이브러리를 다시 설치하지 않아 시간이 절약됩니다.)

# 시스템 패키지 설치 (Pillow 이미지 처리용 종속 포함)
RUN apt-get update && apt-get install -y \
    libjpeg-dev \
    zlib1g-dev \
 && apt-get clean

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 4. 소스 코드 복사
# 현재 폴더(test-ai)의 모든 파일을 컨테이너의 /app 폴더로 복사합니다.
COPY . .

RUN mkdir -p /app/storage

# 5. 포트 노출
# FastAPI 서버가 8000번 포트를 사용하므로, 컨테이너의 8000번 포트를 외부에 공개합니다.
EXPOSE 8000

# 6. 애플리케이션 실행
# 컨테이너가 시작될 때 uvicorn 서버를 실행하는 명령어를 지정합니다.
# "main:app"은 'main.py' 파일의 'app' 객체를 실행하라는 의미입니다.
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
