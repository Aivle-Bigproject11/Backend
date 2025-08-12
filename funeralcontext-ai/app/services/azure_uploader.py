# ========================================
# FILENAME: aivlebigproject/funeralcontext-ai/services/azure_uploader.py
# 역할 : Kafka 이벤트 수신 및 AI 문서 생성 요청 처리
# ========================================
import os
import mimetypes # 파일의 Content-Type을 추측하기 위해 import
from azure.storage.blob import BlobServiceClient, ContentSettings # ContentSettings import

def upload_to_blob(blob_service_client: BlobServiceClient, container_name: str, blob_name: str, file_data: bytes) -> str:
    """
    파일 데이터(bytes)를 Azure Blob에 업로드하고 해당 URL을 반환합니다.

    :param blob_service_client: 연결된 BlobServiceClient 객체
    :param container_name: 업로드할 컨테이너 이름
    :param blob_name: Blob에 저장될 파일 이름
    :param file_data: 업로드할 파일의 바이트 데이터
    :return: 업로드된 파일의 URL
    """
    try:
        blob_client = blob_service_client.get_blob_client(container=container_name, blob=blob_name)
        
        # 1. 파일 이름으로 Content-Type 추측
        content_type, _ = mimetypes.guess_type(blob_name)
        if content_type is None:
            content_type = 'application/octet-stream' # 추측 실패 시 기본값

        # 2. Content-Type 설정과 함께 업로드
        blob_client.upload_blob(
            file_data, 
            overwrite=True, 
            content_settings=ContentSettings(content_type=content_type) # <-- 이 부분 추가
        )
        
        print(f"  ☁️  '{blob_name}' Azure Blob에 업로드 성공 (Content-Type: {content_type}).")
        
        return blob_client.url
    except Exception as e:
        print(f"❌ Azure Blob 업로드 실패: {e}")
        return None

# --- 이 파일을 직접 실행하여 테스트할 때 사용하는 코드 ---
if __name__ == '__main__':
    from dotenv import load_dotenv
    
    print("Azure Blob 업로드 테스트를 시작합니다...")
    load_dotenv(dotenv_path='../../.env') # 프로젝트 루트의 .env 파일을 찾기 위해 경로 지정

    connect_str = os.getenv('AZURE_STORAGE_CONNECTION_STRING')
    test_container_name = "a071098container" # 실제 컨테이너 이름

    if not connect_str:
        print("❌ AZURE_STORAGE_CONNECTION_STRING 환경 변수를 찾을 수 없습니다.")
    else:
        try:
            # 테스트할 로컬 파일 경로
            local_file_path = "../resources/templates/blank_singo.pdf"
            # Azure에 저장될 테스트 파일 이름
            test_blob_name = f"test-uploads/{os.path.basename(local_file_path)}"

            # 클라이언트 생성
            test_blob_service_client = BlobServiceClient.from_connection_string(connect_str)
            
            # 파일 읽기
            with open(local_file_path, "rb") as data:
                file_bytes = data.read()

            # 업로드 함수 호출
            url = upload_to_blob(test_blob_service_client, test_container_name, test_blob_name, file_bytes)

            if url:
                print(f"\n✅ 테스트 성공! 파일 URL: {url}")

        except Exception as e:
            print(f"❌ 테스트 중 오류 발생: {e}")