package aivlebigproject.controller;

import aivlebigproject.dto.PhotoUploadResponse;
import aivlebigproject.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("memorials/{memorialId}/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoUploadResponse> uploadPhoto(
            @PathVariable("memorialId") UUID memorialId,
            @RequestPart("photo") MultipartFile photo,
            @RequestParam("title") String title,
            @RequestParam("description") String description
            ) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhoto(photo, memorialId, title, description);
        return ResponseEntity.ok(response);
    }

}
//>>> Clean Arch / Inbound Adaptor
