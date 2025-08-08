package aivlebigproject.controller;

import aivlebigproject.dto.VideoResponse;
import aivlebigproject.service.VideoService;
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
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /*
    1. 추모사진들 + 키워드 -> 추모사진 업로드 -> 파이썬 서버에 추모영상 요청
     */
    @PostMapping("/generate")
    public ResponseEntity<VideoResponse> generateVideo(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("memorialId") UUID memorialId,
            @RequestParam("keywords") String keywords) {
        try {
            log.info("비디오 생성 요청 - memorialId: {}, keyword: {}, 이미지 수: {}",
                    memorialId, keywords, images.size());

            Long id = videoService.createVideoRequest(memorialId, keywords, images);

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
