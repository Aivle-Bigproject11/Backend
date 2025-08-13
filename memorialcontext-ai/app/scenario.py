# ============================================================================
# 시나리오 생성
# ============================================================================
import json

from dotenv import load_dotenv
from openai import OpenAI

from app.config import SCENARIO_PROMPT, TEST_MODE


def generate_scenario(keywords, name, birth, death, photo_count):

    load_dotenv()
    openai_client = OpenAI()
    prompt = SCENARIO_PROMPT.format(
        keywords=keywords,
        name=name,
        birth=birth,
        death=death,
        photo_count=photo_count
    )

    response = openai_client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {"role": "system", "content": "당신은 감성적인 추모 영상 시나리오를 잘 작성하는 작가입니다."},
            {"role": "user", "content": prompt}
        ],
        temperature=0.9,
        max_tokens=1000
    )
    print(json.loads(response.choices[0].message.content))
    return json.loads(response.choices[0].message.content)

if __name__ == '__main__':
    keywords = "그리움, 아버지, 든든한 아버지"
    name = "이선균"
    birth = "1975-03-02"
    death = "2023-12-27"
    photo_count = 15
    scenario = generate_scenario(keywords, name, birth, death, photo_count+1)
    print("*" * 40)
    print("scenario")
    print(scenario)
