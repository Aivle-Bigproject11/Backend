package aivlebigproject.infra.controller;

import aivlebigproject.domain.VideoResponse;
import aivlebigproject.infra.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

//<<< Clean Arch / Inbound Adaptor

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/generate")
    public ResponseEntity<VideoResponse> generateVideo(
            @RequestParam("memorialId") UUID memorialId,
            @RequestParam("keyword") String keyword,
            @RequestParam("images") List<MultipartFile> images) {

        try {
            log.info("비디오 생성 요청 - memorialId: {}, keyword: {}, 이미지 수: {}",
                    memorialId, keyword, images.size());

            Long id = videoService.createVideoRequest(memorialId, keyword, images);

            return ResponseEntity.ok(VideoResponse.builder()
                    .id(id)
                    .status("REQUESTED")
                    .message("비디오 생성 요청이 접수되었습니다.")
                    .build());

        } catch (Exception e) {
            log.error("비디오 생성 요청 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(VideoResponse.builder()
                            .id(null)
                            .status("FAILED")
                            .message("비디오 생성 요청 실패: " + e.getMessage())
                            .build());
        }
    }


}
//>>> Clean Arch / Inbound Adaptor
