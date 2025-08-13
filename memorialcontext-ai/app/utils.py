# ============================================================================
# 유틸리티 함수
# ============================================================================

import base64
import os
from urllib.parse import urlparse

import requests
from PIL import Image

from app.config import VIDEO_SIZE


def resize_with_padding(image_path, outro_size=VIDEO_SIZE, bg_color=(0, 0, 0)):
    """이미지를 패딩과 함께 리사이즈"""
    img = Image.open(image_path)
    img.thumbnail(outro_size, Image.LANCZOS)

    new_img = Image.new("RGB", outro_size, bg_color)
    left = (outro_size[0] - img.width) // 2
    top = (outro_size[1] - img.height) // 2
    new_img.paste(img, (left, top))
    return new_img


def encode_image_as_data_uri(path):
    """이미지를 Base64 데이터 URI로 인코딩"""
    with open(path, "rb") as image_file:
        encoded = base64.b64encode(image_file.read()).decode("utf-8")
        return f"data:image/jpeg;base64,{encoded}"


def download_url(url, memorial_id, subdir, new_ext=".jpeg"):
    """URL에서 원본 파일명 그대로 가져오되, 확장자만 변경하여 저장"""
    # URL에서 파일명 추출
    original_filename = os.path.basename(urlparse(url).path)
    base_name, _ = os.path.splitext(original_filename)  # 확장자 제거

    # 새 확장자 적용
    filename = base_name + new_ext

    # 저장 디렉토리 생성
    save_dir = os.path.join("./temp", memorial_id, subdir)
    os.makedirs(save_dir, exist_ok=True)

    # 다운로드 & 저장
    response = requests.get(url, stream=True)
    response.raise_for_status()

    file_path = os.path.join(save_dir, filename)
    with open(file_path, "wb") as f:
        for chunk in response.iter_content(chunk_size=8192):
            if chunk:
                f.write(chunk)

    print(f"🎥 저장 완료: {file_path}")
    return file_path

def download_video(url: str, output_path: str, chunk_size: int = 1024 * 1024) -> str:
    os.makedirs(os.path.dirname(output_path), exist_ok=True)

    with requests.get(url, stream=True, timeout=120) as r:
        r.raise_for_status()
        with open(output_path, "wb") as f:
            for chunk in r.iter_content(chunk_size=chunk_size):
                if chunk:  # keep-alive 청크 제외
                    f.write(chunk)

    print(f"🎬 다운로드 완료: {output_path}")
    return output_path


def create_directories(*directories):
    """디렉토리들을 생성"""
    import os
    for directory in directories:
        os.makedirs(directory, exist_ok=True)


if __name__ == "__main__":
    memorial_id = "test"
    outro_image = "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/outro/outro.jpg"
    base_path = os.path.join("./temp/test")

    photo_urls = [
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/1.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/2.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/3.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/4.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/5.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/6.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/7.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/8.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/9.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/10.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/11.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/12.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/13.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/14.jpg",
        "https://aivles.blob.core.windows.net/memorial-content/0208b132-4020-400a-a78c-6d27c946933e/tribute-video/images/15.jpg"
    ]

    for url in photo_urls:
        download_url(url, memorial_id, "images")
    download_url(outro_image, memorial_id, "outro")
