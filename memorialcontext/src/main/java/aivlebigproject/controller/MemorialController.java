package aivlebigproject.controller;

import aivlebigproject.dto.*;
import aivlebigproject.service.AzureBlobService;
import aivlebigproject.service.MemorialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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


    @GetMapping("/{memorialId}/detail")
    public ResponseEntity<MemorialDetail> getMemorialDetail(
            @PathVariable("memorialId") UUID memorialId
    ) {
        MemorialDetail memorialDetail = memorialService.getMemorialDetail(memorialId);
        return ResponseEntity.ok(memorialDetail);
    }


    @PatchMapping("/{memorialId}/profile-image")
    public ResponseEntity<MemorialProfileResponse> createMemorialProfileImage(
            @PathVariable("memorialId") UUID memorialId,
            @RequestParam("photo") MultipartFile file
    ) throws IOException {
        MemorialProfileResponse memorialProfileResponse = memorialService.updateProfileImage(memorialId, file);
        return ResponseEntity.ok(memorialProfileResponse);
    }

    @DeleteMapping("/{memorialId}/profile-image")
    public ResponseEntity<MemorialProfileResponse> deleteMemorialProfileImage(
            @PathVariable("memorialId") UUID memorialId
    ){
        MemorialProfileResponse memorialProfileResponse = memorialService.deleteProfileImage(memorialId);
        return ResponseEntity.ok(memorialProfileResponse);
    }


    @PostMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> createTribute(
            @PathVariable UUID memorialId,
            @RequestBody TributeGenerationRequest request){
        TributeResponse tributeResponse = memorialService.createTribute(memorialId,request);
        return ResponseEntity.ok(tributeResponse);
    }


    @PatchMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> updateTribute(
            @PathVariable UUID memorialId,
            @Valid @RequestBody TributeUpdateRequest request
    ) {
        try {
            TributeResponse response = memorialService.updateTribute(memorialId, request);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{memorialId}/tribute")
    public ResponseEntity<TributeResponse> deleteTribute(
            @PathVariable UUID memorialId
    ){
        TributeResponse response = memorialService.deleteTribute(memorialId);
        return ResponseEntity.ok(response);
    }





}