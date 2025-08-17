package aivlebigproject.controller;

import aivlebigproject.dto.*;
import aivlebigproject.service.MemorialService;
import aivlebigproject.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

//<<< Clean Arch / Inbound Adaptor

@Slf4j
@RestController
@RequestMapping(value="/memorials")
@RequiredArgsConstructor
public class MemorialController {

    private final MemorialService memorialService;
    private final AuthUtil authUtil;


    @GetMapping("/{memorialId}/detail")
    public ResponseEntity<MemorialDetail> getMemorialDetail(
            @PathVariable("memorialId") UUID memorialId
    ) {
        MemorialDetail memorialDetail = memorialService.getMemorialDetail(memorialId);
        return ResponseEntity.ok(memorialDetail);
    }


    @PatchMapping("/{memorialId}/profile-image")
    public ResponseEntity<MemorialProfileResponse> createMemorialProfileImage(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable("memorialId") UUID memorialId,
            @RequestParam("photo") MultipartFile file
    ) throws IOException {
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        MemorialProfileResponse memorialProfileResponse = memorialService.updateProfileImage(memorialId, file, userInfo);
        return ResponseEntity.ok(memorialProfileResponse);
    }

    @DeleteMapping("/{memorialId}/profile-image")
    public ResponseEntity<MemorialProfileResponse> deleteMemorialProfileImage(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable("memorialId") UUID memorialId
    ){
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        MemorialProfileResponse memorialProfileResponse = memorialService.deleteProfileImage(memorialId, userInfo);
        return ResponseEntity.ok(memorialProfileResponse);
    }


    @PostMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> createTribute(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable UUID memorialId,
            @RequestBody TributeGenerationRequest request){
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        TributeResponse tributeResponse = memorialService.createTribute(memorialId,request, userInfo);
        return ResponseEntity.ok(tributeResponse);
    }


    @PatchMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> updateTribute(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable UUID memorialId,
            @Valid @RequestBody TributeUpdateRequest request
    ) {
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        TributeResponse response = memorialService.updateTribute(memorialId, request, userInfo);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> deleteTribute(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable UUID memorialId
    ){
        TokenUserInfo userInfo = authUtil.validateAndGetUserInfo(authHeader);

        TributeResponse response = memorialService.deleteTribute(memorialId, userInfo);
        return ResponseEntity.ok(response);
    }





}