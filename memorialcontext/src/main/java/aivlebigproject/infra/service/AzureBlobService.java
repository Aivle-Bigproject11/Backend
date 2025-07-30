package aivlebigproject.infra.service;//package aivlebigprojectminseo.service;
//
//import com.azure.storage.blob.BlobClient;
//import com.azure.storage.blob.BlobContainerClient;
//import com.azure.storage.blob.BlobServiceClient;
//import com.azure.storage.blob.BlobServiceClientBuilder;
//import com.azure.storage.blob.models.BlobHttpHeaders;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class AzureBlobService {
//
//    @Value("${azure.storage.connection-string}")
//    private String connectionString;
//
//    @Value("${azure.storage.container-name}")
//    private String containerName;
//
//    @Value("${azure.storage.base-url}")
//    private String baseUrl;
//
//    private BlobServiceClient blobServiceClient;
//    private BlobContainerClient containerClient;
//
//    @PostConstruct
//    public void init() {
//        try {
//            // Blob Service Client 초기화
//            blobServiceClient = new BlobServiceClientBuilder()
//                    .connectionString(connectionString)
//                    .buildClient();
//
//            // Container Client 초기화
//            containerClient = blobServiceClient.getBlobContainerClient(containerName);
//
//            // 컨테이너가 없으면 생성 (개발환경용)
//            if (!containerClient.exists()) {
//                containerClient.create();
//                log.info("컨테이너 생성됨: {}", containerName);
//            }
//
//            log.info("Azure Blob Storage 초기화 완료");
//        } catch (Exception e) {
//            log.error("Azure Blob Storage 초기화 실패", e);
//            throw new RuntimeException("Azure Blob Storage 연결 실패", e);
//        }
//    }
//
//    /**
//     * 여러 이미지를 임시 폴더에 업로드
//     */
//    public List<String> uploadTempImages(Long id, List<MultipartFile> images) {
//        List<String> imageUrls = new ArrayList<>();
//
//        for (MultipartFile image : images) {
//            try {
//                String imageUrl = uploadTempImage(id, image);
//                imageUrls.add(imageUrl);
//                log.debug("이미지 업로드 성공: {}", imageUrl);
//            } catch (Exception e) {
//                log.error("이미지 업로드 실패: {}", image.getOriginalFilename(), e);
//                throw new RuntimeException("이미지 업로드 실패: " + image.getOriginalFilename(), e);
//            }
//        }
//
//        log.info("총 {}개 이미지 업로드 완료 (id: {})", imageUrls.size(), id);
//        return imageUrls;
//    }
//
//    /**
//     * 단일 이미지를 임시 폴더에 업로드
//     */
//    public String uploadTempImage(Long id, MultipartFile image) throws IOException {
//        // 파일명 검증
//        if (image.isEmpty()) {
//            throw new IllegalArgumentException("빈 파일입니다");
//        }
//
//        // 파일 타입 검증
//        String contentType = image.getContentType();
//        if (contentType == null || !contentType.startsWith("image/")) {
//            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다");
//        }
//
//        // 고유한 파일명 생성
//        String originalFilename = image.getOriginalFilename();
//        String fileExtension = getFileExtension(originalFilename);
//        String uniqueFilename = generateUniqueFilename(id, fileExtension);
//
//        // 임시 폴더 경로
//        String blobName = "temp/" + id + "/" + uniqueFilename;
//
//        try {
//            // Blob 클라이언트 생성
//            BlobClient blobClient = containerClient.getBlobClient(blobName);
//
//            // HTTP 헤더 설정
//            BlobHttpHeaders headers = new BlobHttpHeaders()
//                    .setContentType(contentType)
//                    .setCacheControl("public, max-age=31536000"); // 1년 캐시
//
//            // 파일 업로드
//            blobClient.upload(image.getInputStream(), image.getSize(), true);
//            blobClient.setHttpHeaders(headers);
//
//            // URL 반환
//            String imageUrl = baseUrl + "/" + containerName + "/" + blobName;
//            log.debug("이미지 업로드 완료: {} -> {}", originalFilename, imageUrl);
//
//            return imageUrl;
//
//        } catch (Exception e) {
//            log.error("Azure Blob 업로드 실패: {}", blobName, e);
//            throw new IOException("파일 업로드 실패", e);
//        }
//    }
//
//    /**
//     * 임시 이미지들을 정식 폴더로 이동
//     */
//    public List<String> moveImagesToFinal(Long id, List<String> tempImageUrls) {
//        List<String> finalImageUrls = new ArrayList<>();
//
//        for (String tempUrl : tempImageUrls) {
//            try {
//                String finalUrl = moveImageToFinal(id, tempUrl);
//                finalImageUrls.add(finalUrl);
//            } catch (Exception e) {
//                log.error("이미지 이동 실패: {}", tempUrl, e);
//                // 실패해도 계속 진행
//            }
//        }
//
//        return finalImageUrls;
//    }
//
//    /**
//     * 단일 임시 이미지를 정식 폴더로 이동
//     */
//    private String moveImageToFinal(Long id, String tempUrl) {
//        try {
//            // 임시 URL에서 blob 이름 추출
//            String tempBlobName = extractBlobNameFromUrl(tempUrl);
//            BlobClient tempBlob = containerClient.getBlobClient(tempBlobName);
//
//            // 새로운 blob 이름 생성
//            String filename = tempBlobName.substring(tempBlobName.lastIndexOf("/") + 1);
//            String finalBlobName = "videos/" + id + "/images/" + filename;
//
//            // 복사
//            BlobClient finalBlob = containerClient.getBlobClient(finalBlobName);
//            finalBlob.copyFromUrl(tempBlob.getBlobUrl());
//
//            // 임시 파일 삭제
//            tempBlob.deleteIfExists();
//
//            // 최종 URL 반환
//            return baseUrl + "/" + containerName + "/" + finalBlobName;
//
//        } catch (Exception e) {
//            log.error("이미지 이동 중 오류", e);
//            throw new RuntimeException("이미지 이동 실패", e);
//        }
//    }
//
//    /**
//     * 임시 이미지들 정리
//     */
//    public void cleanupTempImages(Long id) {
//        try {
//            String prefix = "temp/" + id + "/";
//            containerClient.listBlobsByHierarchy(prefix)
//                    .forEach(blobItem -> {
//                        try {
//                            containerClient.getBlobClient(blobItem.getName()).deleteIfExists();
//                            log.debug("임시 파일 삭제: {}", blobItem.getName());
//                        } catch (Exception e) {
//                            log.warn("임시 파일 삭제 실패: {}", blobItem.getName(), e);
//                        }
//                    });
//            log.info("임시 이미지 정리 완료 (id: {})", id);
//        } catch (Exception e) {
//            log.error("임시 이미지 정리 실패 (id: {})", id, e);
//        }
//    }
//
//    // 유틸리티 메서드들
//    private String getFileExtension(String filename) {
//        if (filename == null || !filename.contains(".")) {
//            return ".jpg"; // 기본값
//        }
//        return filename.substring(filename.lastIndexOf("."));
//    }
//
//    private String generateUniqueFilename(Long id, String extension) {
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//        String uuid = UUID.randomUUID().toString().substring(0, 8);
//        return String.format("video_%d_%s_%s%s", id, timestamp, uuid, extension);
//    }
//
//    private String extractBlobNameFromUrl(String url) {
//        // URL에서 blob 이름 추출
//        String containerPath = "/" + containerName + "/";
//        int index = url.indexOf(containerPath);
//        if (index == -1) {
//            throw new IllegalArgumentException("잘못된 blob URL: " + url);
//        }
//        return url.substring(index + containerPath.length());
//    }
//}