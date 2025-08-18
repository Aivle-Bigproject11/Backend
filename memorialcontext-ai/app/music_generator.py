import os, time, requests
from os.path import basename, join, abspath
from dotenv import load_dotenv

from app.config import SUNO_PROMPT



def check_task_status(task_id, api_key):
    url = f"https://api.sunoapi.org/api/v1/generate/record-info?taskId={task_id}"
    headers = {"Authorization": f"Bearer {api_key}"}

    response = requests.get(url, headers=headers)
    result = response.json()
    print(result)

    status = result.get('data', {}).get('status')
    if status in ('PENDING', 'GENERATING', 'TEXT_SUCCESS', 'FIRST_SUCCESS'):
        # 아직 최종 오디오 URL이 준비되지 않은 중간 상태
        print(f"Still generating... ({status})")
        return None

    if status == 'SUCCESS':
        print("Generation complete!")
        resp = result.get('data', {}).get('response', {})
        audio_data = resp.get('sunoData') or resp.get('data') or []
        # 디버그 출력 (존재하는 URL 키 우선순위대로)
        for i, audio in enumerate(audio_data):
            url = (
                audio.get('audioUrl')
            )
            print(f"Track {i + 1}: {url}")
        return url

    print(f"Generation failed: {status}")
    return None

def generate_music_file(base_path: str, keywords: str, poll=30, timeout=600) -> str:
    load_dotenv()
    API_BASE = "https://api.sunoapi.org/api/v1"
    API_KEY = os.getenv("SUNO_API_KEY")
    if not API_KEY:
        raise RuntimeError("SUNO_API_KEY가 설정되어 있지 않습니다 (.env 확인).")

    music_dir = join(base_path, "music")
    os.makedirs(music_dir, exist_ok=True)

    headers = {"Authorization": f"Bearer {API_KEY}", "Content-Type": "application/json"}
    prompt = SUNO_PROMPT.format(
            keywords=keywords
        )
    payload = {
        "prompt": prompt,
        "customMode": False,
        "instrumental": True,
        "model": "V3_5",
        "callBackUrl": "https://your-server.com/callback"
    }

    # 1) 생성 요청
    resp = requests.post(f"{API_BASE}/generate", headers=headers, json=payload, timeout=30)
    j = resp.json()
    task_id = (j.get("data") or {}).get("taskId")
    print("taskId: " + task_id)
    if not task_id:
        raise RuntimeError(f"Generate 실패: status={resp.status_code}, body={j}")

    # 2) 폴링
    audio_url = None
    while True:
        audio_url = check_task_status(task_id, API_KEY)
        if audio_url:
            print("audio_url : ",audio_url)
            break
        time.sleep(30)
    # 3) 다운로드
    target = os.path.join(base_path, "music", "memorial_music.mp3")

    with requests.get(audio_url, stream=True, timeout=120) as r:
        r.raise_for_status()
        with open(target, "wb") as f:
            for chunk in r.iter_content(1024 * 1024):
                if chunk:
                    f.write(chunk)
    return target

if __name__ == "__main__":
    base_path = os.path.join("./temp", "test")
    keywords = "그리움, 아버지, 항상 든든한 아버지"
    generate_music_file(base_path, keywords)