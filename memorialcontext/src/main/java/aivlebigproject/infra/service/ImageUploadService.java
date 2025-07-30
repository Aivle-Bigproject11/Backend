//package aivlebigproject.infra.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class ImageUploadService {
//
//    private static final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);
//
//    private final BlobContainerClient blobContainerClient;
//    private final int maxImages;
//    private final List<String> allowedTypes;
//    private final long maxImageSize;
//
//    public ImageUploadService(
//            BlobContainerClient blobContainerClient,
//            @Value("${video.upload.max-images}") int maxImages,
//            @Value("${video.upload.allowed-types}") String allowedTypes,
//            @Value("${video.upload.max-image-size}") long maxImageSize) {
//
//        this.blobContainerClient = blobContainerClient;
//        this.maxImages = maxImages;
//        this.allowedTypes = Arrays.asList(allowedTypes.split(","));
//        this.maxImageSize = maxImageSize;
//    }
//
//    public List<VideoImage> uploadVideoImages(Video video, List<MultipartFile> images) {
//        logger.info("비디오 이미지 업로드 시작: id={}, 이미지 개수={}", video.getId(), images.size());
//
//        // 1. 기본 검증
//        validateImages(images);
//
//        // 2. 각 이미지 업로드
//        List<VideoImage> uploadedImages = new ArrayList<>();
//        for (int i = 0; i < images.size(); i++) {
//            MultipartFile image = images.get(i);
//            VideoImage videoImage = uploadSingleImage(video, image, i);
//            uploadedImages.add(videoImage);
//        }
//
//        logger.info("비디오 이미지 업로드 완료: id={}", video.getId());
//        return uploadedImages;
//    }
//
//    private void validateImages(List<MultipartFile> images) {
//        // 개수 검증
//        if (images.isEmpty()) {
//            throw new IllegalArgumentException("최소 1개의 이미지가 필요합니다.");
//        }
//        if (images.size() > maxImages) {
//            throw new IllegalArgumentException("최대 " + maxImages + "개의 이미지만 업로드 가능합니다.");
//        }
//
//        // 각 이미지 검증
//        for (MultipartFile image : images) {
//            validateSingleImage(image);
//        }
//    }
//
//    private void validateSingleImage(MultipartFile image) {
//        // 빈 파일 검증
//        if (image.isEmpty()) {
//            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다: " + image.getOriginalFilename());
//        }
//
//        // 크기 검증
//        if (image.getSize() > maxImageSize) {
//            throw new IllegalArgumentException("파일 크기가 너무 큽니다: " + image.getOriginalFilename());
//        }
//
//        // 파일 타입 검증
//        String fileExtension = getFileExtension(image.getOriginalFilename());
//        if (!allowedTypes.contains(fileExtension.toLowerCase())) {
//            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + fileExtension);
//        }
//
//        // Content-Type 검증
//        String contentType = image.getContentType();
//        if (contentType == null || !contentType.startsWith("image/")) {
//            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다: " + image.getOriginalFilename());
//        }
//    }
//
//    private VideoImage uploadSingleImage(Video video, MultipartFile image, int index) {
//        try {
//            String imageId = UUID.randomUUID().toString();
//            String blobName = generateBlobName(video.getMemorialId(), imageId, image.getOriginalFilename());
//
//            // Azure Blob Storage에 업로드
//            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
//
//            // HTTP 헤더 설정
//            BlobHttpHeaders headers = new BlobHttpHeaders()
//                    .setContentType(image.getContentType())
//                    .setCacheControl("public, max-age=31536000"); // 1년 캐시
//
//            // 메타데이터 설정
//            blobClient.getBlockBlobClient().uploadWithResponse(
//                    image.getInputStream(),
//                    image.getSize(),
//                    headers,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//
//            // Blob URL 생성
//            String blobUrl = blobClient.getBlobUrl();
//
//            logger.info("이미지 업로드 완료: imageId={}, blobName={}", imageId, blobName);
//
//            // VideoImage 엔티티 생성
//            return new VideoImage(
//                    video,
//                    imageId,
//                    image.getOriginalFilename(),
//                    blobName,
//                    blobUrl,
//                    image.getSize(),
//                    image.getContentType(),
//                    index
//            );
//
//        } catch (IOException e) {
//            logger.error("이미지 업로드 실패: {}", image.getOriginalFilename(), e);
//            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다: " + image.getOriginalFilename(), e);
//        } catch (Exception e) {
//            logger.error("예상치 못한 오류: {}", image.getOriginalFilename(), e);
//            throw new RuntimeException("이미지 업로드 중 예상치 못한 오류가 발생했습니다.", e);
//        }
//    }
//
//    private String generateBlobName(Long memorialId, String imageId, String originalFileName) {
//        String fileExtension = getFileExtension(originalFileName);
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        return String.format("videos/%d/%s/%s.%s", memorialId, timestamp, imageId, fileExtension);
//    }
//
//    private String getFileExtension(String fileName) {
//        if (fileName == null || fileName.isEmpty()) {
//            return "";
//        }
//        int lastDotIndex = fileName.lastIndexOf(".");
//        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
//    }
//
//    public void deleteImage(String blobName) {
//        try {
//            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
//            blobClient.delete();
//            logger.info("이미지 삭제 완료: blobName={}", blobName);
//        } catch (Exception e) {
//            logger.error("이미지 삭제 실패: blobName={}", blobName, e);
//            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
//        }
//    }
//}