import base64
import os

import runwayml
from dotenv import load_dotenv
from runwayml import RunwayML

from app.config import RUNWAY_PROMPT
from app.utils import resize_with_padding, download_url, download_video


def generate_image_to_video(memorial_path):
    load_dotenv()

    outro_path = os.path.join(memorial_path, "outro")
    outro_image = os.path.join(outro_path, "outro.jpeg")
    outro_image_resized = os.path.join(outro_path, "outro_resized.jpeg")

    # 이미지 리사이즈
    resized = resize_with_padding(outro_image)
    resized.save(outro_image_resized)

    client = RunwayML(
        api_key=os.environ.get("RUNWAYML_API_SECRET"),
    )

    with open(outro_image_resized, "rb") as f:
        # Read the file as a base64 encoded string
        base64_image = base64.b64encode(f.read()).decode("utf-8")
        # Format it as a data URI
        # We're using `image/png` here because the input is a PNG.
        data_uri = f"data:image/jpeg;base64,{base64_image}"

    try:
        task = client.image_to_video.create(
            model = 'gen4_turbo',
            prompt_image=data_uri,
            prompt_text=RUNWAY_PROMPT,
            ratio='1280:720',
            duration=5,
        ).wait_for_task_output()

        print('Task complete:', task)

        if task.output:
            video_url = task.output[0]
            output_path = os.path.join(outro_path, "runway_result.mp4")
            download_video(video_url, output_path)
            return output_path

    except runwayml.TaskFailedError as e:
        print('The video failed to generate.')
        print(e.task_details)


if __name__ == "__main__":
    memorial_path = os.path.join('../temp', 'test')
    generate_image_to_video(memorial_path)