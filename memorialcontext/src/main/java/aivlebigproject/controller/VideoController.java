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
@RequestMapping("memorials/{memorialId}/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /*
    1. 추모사진들 + 키워드 -> 추모사진 업로드 -> 파이썬 서버에 추모영상 요청
     */
    @PostMapping
    public ResponseEntity<VideoResponse> generateVideo(
            @PathVariable("memorialId") UUID memorialId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("outroImage") MultipartFile outroImage,
            @RequestParam("keywords") String keywords) {
        try {

            VideoResponse response = videoService.createVideoRequest(memorialId, keywords, images, outroImage);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

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
