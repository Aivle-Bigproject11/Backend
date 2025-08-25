import os, time, requests
from os.path import basename, join, abspath
from dotenv import load_dotenv
from openai import OpenAI

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
        url = None
        # 디버그 출력 (존재하는 URL 키 우선순위대로)
        for i, audio in enumerate(audio_data):
            candidate = audio.get('audioUrl')
            print(f"Track {i + 1}: {candidate}")
            if candidate:
                url = candidate  # 마지막 유효 URL을 사용 (또는 첫 번째를 쓰고 싶으면 break)
        if not url:
            print("No audioUrl found in response payload.")
            return None
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

    openai_client = OpenAI()
    prompt = SUNO_PROMPT.format(
        keywords=keywords
    )

    response = openai_client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {
                "role": "system",
                "content": (
                    "You are a music prompt optimizer for Suno AI.\n"
                    "Your job:\n"
                    "1. Translate all keywords into natural English.\n"
                    "2. Remove or replace sensitive words (e.g., 'miso').\n"
                    "3. Ensure the entire prompt is in English only.\n"
                    "4. Keep the length under 400 characters.\n"
                    "5. Return ONLY the final safe prompt string (no JSON, no explanation)."
                )
            },
            {"role": "user", "content": prompt}
        ],
        temperature=0.7,
        max_tokens=500
    )
    optimized_prompt = response.choices[0].message.content.strip()
    print(f"        Optimized prompt: {optimized_prompt}")
    payload = {
        "prompt": optimized_prompt,
        "customMode": False,
        "instrumental": True,
        "model": "V3_5",
        "callBackUrl": "https://your-server.com/callback"
    }

    # 1) 생성 요청
    resp = requests.post(f"{API_BASE}/generate", headers=headers, json=payload, timeout=30)
    j = resp.json()
    task_id = (j.get("data") or {}).get("taskId")
    if not task_id:
        raise RuntimeError(f"Generate 실패: status={resp.status_code}, body={j}")
    print(f"taskId: {task_id}")

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