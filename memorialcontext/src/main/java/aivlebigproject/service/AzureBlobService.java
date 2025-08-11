package aivlebigproject.service;

import com.azure.storage.blob.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class AzureBlobService {

    private final BlobContainerClient containerClient;

    public AzureBlobService(
            @Value("${spring.cloud.azure.storage.blob.account-name}") String accountName,
            @Value("${spring.cloud.azure.storage.blob.account-key:}") String accountKey,
            @Value("${spring.cloud.azure.storage.blob.container-name}") String containerName) {

        String connectionString = String.format(
                "DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net",
                accountName, accountKey
        );
        log.info("connectionString: {}", connectionString);

        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        // 특정 컨테이너 클라이언트 생성
        this.containerClient = serviceClient.getBlobContainerClient(containerName);
        log.info("Azure Blob Service 초기화 완료 - Account: {}, Container: {}", accountName, containerName);

    }

    /*
    추모사진 업로드
     */
    public String uploadProfileImage(MultipartFile file, UUID memorialId) throws IOException {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null) {
            extension = "jpg"; // 확장자가 없으면 기본값
        }
        String blobPath = String.format("%s/profile/profile.%s",
                memorialId.toString(), extension);
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }

    /**
     * 추모사진 업로드 (순차번호 기반)
     */
    public String uploadPhoto(MultipartFile file, UUID memorialId) throws IOException {

        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        String blobPath = String.format("%s/photo-album/%s",
                memorialId.toString(), fileName);


        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        log.info("추모사진 업로드 완료: {}", blobPath);
        return blobClient.getBlobUrl();
    }
    /*
    추모 영상용 사진들 업로드
     */
    public String uploadTributePhoto(MultipartFile file, UUID memorialId, Integer index) throws IOException {
        String fileName = index + getFileExtension(file.getOriginalFilename());
        String blobPath = String.format("%s/tribute-video/images/%s",
                memorialId.toString(), fileName);
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }

    public String uploadTributeOutroPhoto(MultipartFile file, UUID memorialId) throws IOException {
        String fileName = "outro" + getFileExtension(file.getOriginalFilename());
        String blobPath = String.format("%s/tribute-video/outro/%s",
                memorialId.toString(), fileName);
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        return blobClient.getBlobUrl();
    }


    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return ".jpg"; // 기본 확장자
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
    }

    public String uploadMemorialVideo(InputStream videoStream, long size, UUID memorialId, String fileName) throws IOException {
        String blobPath = String.format("%s/videos/%s",
                memorialId.toString(), fileName);

        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(videoStream, size, true);

        log.info("추모영상 업로드 완료: {}", blobPath);
        return blobClient.getBlobUrl();
    }

    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

}