package aivlebigprojectminseo.service;

import aivlebigprojectminseo.domain.Memorial;
import aivlebigprojectminseo.domain.Video;
import aivlebigprojectminseo.domain.repository.MemorialRepository;
import aivlebigprojectminseo.domain.repository.VideoRepository;
import aivlebigprojectminseo.domain.RequestedVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemorialRepository memorialRepository;
//    private final AzureBlobService azureBlobService;

    @Transactional
    public Long createVideoRequest(UUID memorialId, String keyword, List<MultipartFile> images) {
        // 1. Video 엔티티 생성 (이미지 URL 제외)
        Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
        Video video = new Video();
        video.setMemorialId(memorialId);
        video.setKeyword(keyword);
        video.setStatus("REQUESTED");
        video.setVideoTitle(memorial.getName()+"님의 추모영상");
        video.setCreatedAt(LocalDateTime.now());
        video.setCompletedAt(LocalDateTime.now());

        Video savedVideo = Video.repository().save(video);

//        List<String> imageUrls = azureBlobService.uploadTempImages(savedVideo.getId(), images);
//        log.info("이미지 업로드 완료: {}개", imageUrls.size());
        //테스트용도로쓰임
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("이미지 들어갔다!");

        // 3. 수동으로 이벤트 발행 (이미지 URL 포함)
        RequestedVideo requestedVideo = new RequestedVideo(savedVideo);
        requestedVideo.setImageUrls(imageUrls);  // 이벤트에만 포함
        requestedVideo.publish(); // @PostPersist 말고 수동 발행

        return savedVideo.getId();
    }
}
