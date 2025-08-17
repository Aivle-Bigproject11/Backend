package aivlebigproject.service;

import aivlebigproject.dto.TokenUserInfo;
import aivlebigproject.dto.VideoResponse;
import aivlebigproject.event.listener.VideoCreated;
import aivlebigproject.exception.InvalidAuthorizationHeaderException;
import aivlebigproject.exception.MemorialAccessDeniedException;
import aivlebigproject.exception.MemorialNotFoundException;
import aivlebigproject.model.Memorial;
import aivlebigproject.event.publisher.VideoRequested;
import aivlebigproject.model.Video;
import aivlebigproject.repository.MemorialRepository;
import aivlebigproject.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemorialRepository memorialRepository;
    private final AzureBlobService azureBlobService;

    @Transactional
    public VideoResponse createVideoRequest(UUID memorialId, String keywords, List<MultipartFile> images, MultipartFile outroImage, TokenUserInfo userInfo) throws IOException {
        validateMemorialAccess(memorialId, userInfo);
        // 입력 검증 추가
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("이미지는 최소 1개 이상 업로드해야 합니다");
        }

        if (outroImage == null || outroImage.isEmpty()) {
            throw new IllegalArgumentException("아웃트로 이미지는 필수입니다");
        }

        if (keywords == null || keywords.trim().isEmpty()) {
            throw new IllegalArgumentException("키워드는 필수입니다");
        }

        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new MemorialNotFoundException(memorialId.toString()));

        Video video = new Video();
        video.setMemorialId(memorialId);
        video.setKeywords(keywords);
        video.setStatus("REQUESTED");
        video.setVideoTitle(memorial.getName()+"님의 추모영상");


        Video savedVideo = videoRepository.save(video);

        try {
            List<String> imageUrls = new ArrayList<>();
            Integer idx = 1;
            for (MultipartFile image : images) {
                String url = azureBlobService.uploadTributePhoto(image, memorialId, idx);
                imageUrls.add(url);
                idx++;
            }
            String outroImageUrl = azureBlobService.uploadTributeOutroPhoto(outroImage, memorialId);

            // 3. 수동으로 이벤트 발행 (이미지 URL 포함)
            VideoRequested videoRequested = new VideoRequested(savedVideo);
            videoRequested.setImageUrls(imageUrls);  // 이벤트에만 포함
            videoRequested.setName(memorial.getName());
            videoRequested.setMemorialId(memorialId);
            videoRequested.setBirthDate(memorial.getBirthDate());
            videoRequested.setDeceasedDate(memorial.getDeceasedDate());
            videoRequested.setKeywords(savedVideo.getKeywords());
            videoRequested.setOutroImageUrl(outroImageUrl);

            videoRequested.publishAfterCommit();

            return VideoResponse.builder()
                    .id(video.getVideoId())
                    .status(savedVideo.getStatus())
                    .message("비디오 생성 요청이 성공적으로 수행되었습니다.")
                    .build();
        } catch (IOException e) {
            log.error("파일 업로드 실패: memorialId={}, error={}", memorialId, e.getMessage());
            throw new IOException("파일 업로드에 실패했습니다: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Video saveVideo(VideoCreated videoCreated) {
        Video video = videoRepository.findById(videoCreated.getVideoId()).orElse(null);
        video.saveVideoUrl(videoCreated.getVideoUrl());

        return videoRepository.save(video);
    }

    private void validateMemorialAccess(UUID memorialId, TokenUserInfo userInfo) {
        // MANAGER는 모든 권한
        if ("MANAGER".equals(userInfo.getRole())) {
            return;
        }

        // FAMILY는 해당 추모관의 가족 구성원인지 확인
        if ("FAMILY".equals(userInfo.getRole())) {
            boolean isFamilyMember = memorialRepository
                    .existsByIdAndFamilyListContaining(memorialId, userInfo.getUserId());

            if (!isFamilyMember) {
                throw new MemorialAccessDeniedException(memorialId.toString(), userInfo.getUserId());
            }
            return;
        }

        throw new InvalidAuthorizationHeaderException("Invalid role: " + userInfo.getRole());
    }
}