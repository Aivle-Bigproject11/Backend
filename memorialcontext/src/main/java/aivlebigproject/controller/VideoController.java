package aivlebigproject.controller;

import aivlebigproject.dto.TokenUserInfo;
import aivlebigproject.dto.VideoResponse;
import aivlebigproject.service.VideoService;
import aivlebigproject.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//<<< Clean Arch / Inbound Adaptor

@Slf4j
@RestController
@RequestMapping("memorials/{memorialId}/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final AuthUtil authUtil;

    /*
    1. 추모사진들 + 키워드 -> 추모사진 업로드 -> 파이썬 서버에 추모영상 요청
     */
    @PostMapping
    public ResponseEntity<VideoResponse> generateVideo(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("memorialId") UUID memorialId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("outroImage") MultipartFile outroImage,
            @RequestParam("keywords") String keywords
    ) throws IOException {
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        VideoResponse response = videoService.createVideoRequest(memorialId, keywords, images, outroImage, userInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
//>>> Clean Arch / Inbound Adaptor
