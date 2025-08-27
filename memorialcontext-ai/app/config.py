# ============================================================================
# 설정 및 상수
# ============================================================================

# 테스트 모드 설정 (True: 미리 만든 파일 사용, False: API 사용)
TEST_MODE = False
TEST_MUSIC_FILE = "test_music.wav"  # 테스트용 음악 파일명
TEST_OUTRO_VIDEO = "runway_result.mp4"  # 테스트용 아웃트로 영상 파일명

# 비디오 설정
VIDEO_SIZE = (1280, 720)
FONT_PATH = "resources/fonts/ChosunNm.ttf"

# GPT 시나리오 생성 프롬프트 (효과 제거됨)
SCENARIO_PROMPT = """
You are a poetic video director creating a 2 to 3-minute memorial tribute video.

Input:
- Keywords: {keywords}
- Person name: {name}
- Birth year: {birth}
- Death year: {death}
- Number of photos: {photo_count}

**Goal**: The total number of scenes (including intro and outro) MUST be exactly {photo_count}, and the combined duration should be between **120 to 180 seconds**.

Structure:
1. Intro scene (8~10 sec): Show name, birth-death years, and a warm tribute line in Korean.
2. Main scenes: Create exactly ({photo_count} - 2) scenes. Each scene corresponds to one photo:
   - Write a 1–2 line emotional Korean subtitle (e.g., longing, warmth, gratitude)
   - Set a suitable duration (typically 8~10 sec per photo)
3. Outro scene (10 sec): Final farewell message.

Each scene should include:
- scene_id (integer)
- scene_type: "intro", "main", or "outro"
- subtitle (in Korean)
- duration (integer seconds)

**Important**: 
- Focus on heartfelt, emotional subtitles that honor the person's memory
- Keep the tone respectful and loving
- Make each subtitle unique and meaningful

Ensure the final sum of durations is between **120 and 180 seconds**.
Do not invent fictional details. Be heartfelt, poetic, and universally respectful.
Output only the JSON array without any markdown formatting.
"""

# RunwayML 프롬프트
RUNWAY_PROMPT = """
In this 10-second outro, let the person appear deeply peaceful, gently concluding their life journey.  
They blink softly once, as if taking a final calm breath, while a faint tender smile emerges on their face.  
Their chest and shoulders show the slightest hint of gentle breathing, evoking serenity and acceptance.  
With quiet grace, they slowly raise one hand for a final subtle wave—small and heartfelt, like a last farewell to loved ones.  
Do not move the background or the camera.  
As the gesture ends, their form softly fades into warm light and stillness, symbolizing eternal rest, gratitude, and a timeless goodbye.
"""

SUNO_PROMPT = """
You are a music prompt optimizer for Suno AI.

Given a base template:
    Create an emotional instrumental memorial tribute music.
    Themes: {keywords}.
    Slow tempo, cinematic, peaceful atmosphere.
    Instrumental only, no vocals.
    Around 3–4 minutes, evolving motifs, gentle ending.

Tasks:
1. Translate any non-English (Korean, etc.) keywords into natural English.
2. Replace sensitive words (e.g., "miso") with safe synonyms like "smile".
3. Ensure the entire prompt is written only in English.
4. If the prompt exceeds 400 characters, shorten and summarize it while keeping the emotional and memorial theme intact.
5. Return only the final safe prompt string, nothing else.
"""