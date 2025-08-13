package aivlebigproject.service;

import aivlebigproject.dto.VideoResponse;
import aivlebigproject.event.listener.VideoCreated;
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
    public VideoResponse createVideoRequest(UUID memorialId, String keywords, List<MultipartFile> images, MultipartFile outroImage) throws IOException {
        // 1. Video 엔티티 생성 (이미지 URL 제외)
        Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
        Video video = new Video();
        video.setMemorialId(memorialId);
        video.setKeywords(keywords);
        video.setStatus("REQUESTED");
        video.setVideoTitle(memorial.getName()+"님의 추모영상");


        Video savedVideo = videoRepository.save(video);

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
    }

    @Transactional
    public Video saveVideo(VideoCreated videoCreated) {
        Video video = videoRepository.findById(videoCreated.getVideoId()).orElse(null);
        video.updateVideo(videoCreated.getVideoUrl());

        return videoRepository.save(video);
    }
}