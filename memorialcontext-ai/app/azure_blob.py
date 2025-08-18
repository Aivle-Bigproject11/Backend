# ============================================================================
# Azure Blob Storage 간단 업로드
# ============================================================================

import os
import uuid
from datetime import datetime
from azure.storage.blob import BlobServiceClient, ContentSettings
from dotenv import load_dotenv

load_dotenv()


def upload_video_to_blob(local_file_path, memorialId=None, videoId=None):
    """비디오 파일을 Azure Blob Storage에 업로드"""
    try:
        # Azure Storage 설정
        connection_string = os.getenv('AZURE_STORAGE_CONNECTION_STRING')
        container_name = os.getenv('AZURE_CONTAINER_NAME', 'memorial-video')
        account_name = os.getenv('AZURE_STORAGE_ACCOUNT_NAME')

        if not connection_string:
            return {'success': False, 'error': 'AZURE_STORAGE_CONNECTION_STRING not found'}

        if not os.path.exists(local_file_path):
            return {'success': False, 'error': f'File not found: {local_file_path}'}

        # Blob Service Client 생성
        service = BlobServiceClient.from_connection_string(connection_string)

        blob_name = f"{memorialId}/tribute-video/{videoId}.mp4"

        print(f"📤 Blob Storage 업로드 시작...")
        print(f"   파일: {local_file_path}")
        print(f"   Blob: {blob_name}")

        # 컨테이너가 없으면 생성
        try:
            container_client = service.get_container_client(container_name)
            if not container_client.exists():
                container_client.create_container()
                print(f"✅ 컨테이너 생성됨: {container_name}")
        except Exception as e:
            print(f"⚠️  컨테이너 확인/생성 실패: {e}")

        # 파일 업로드
        blob_client = service.get_blob_client(container=container_name, blob=blob_name)

        with open(local_file_path, "rb") as data:
            blob_client.upload_blob(
                data = data
            )

        # 결과 생성
        blob_url = f"https://{account_name}.blob.core.windows.net/{container_name}/{blob_name}"
        file_size = os.path.getsize(local_file_path) / (1024 * 1024)  # MB

        result = {
            'success': True,
            'blob_name': blob_name,
            'blob_url': blob_url,
            'file_size_mb': round(file_size, 2),
            'container_name': container_name
        }

        print(f"✅ Blob Storage 업로드 완료!")
        print(f"🌐 공개 URL: {result['blob_url']}")
        print(f"📊 파일 크기: {result['file_size_mb']}MB")

        return result['blob_url']

    except Exception as e:
        print(f"❌ Blob Storage 업로드 실패: {e}")
        return {'success': False, 'error': str(e)}


def test_blob_upload():
    """Blob Storage 업로드 테스트"""
    print("🧪 Azure Blob Storage 업로드 테스트")
    print("=" * 50)

    # 테스트 파일 경로
    test_file = "../temp/test/output/memorial_video.mp4"

    if not os.path.exists(test_file):
        print(f"❌ 테스트 파일이 없습니다: {test_file}")
        print("💡 먼저 비디오를 생성하거나 테스트 파일을 준비해주세요.")
        return False

    # 업로드 테스트
    result = upload_video_to_blob(test_file, memorialId="memorial", videoId="1")

    if result['success']:
        print(f"\n🎉 테스트 성공!")
        return True
    else:
        print(f"\n❌ 테스트 실패: {result['error']}")
        print("\n💡 확인사항:")
        print("   1. AZURE_STORAGE_CONNECTION_STRING이 .env에 설정되어 있는가?")
        print("   2. AZURE_STORAGE_ACCOUNT_NAME이 .env에 설정되어 있는가?")
        print("   3. Azure Storage Account가 정상인가?")
        print("   4. azure-storage-blob 패키지가 설치되어 있는가?")
        return False


if __name__ == "__main__":
    test_blob_upload()