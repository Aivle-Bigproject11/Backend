package aivlebigproject.service;

import aivlebigproject.dto.PhotoUploadResponse;
import aivlebigproject.model.Photo;
import aivlebigproject.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AzureBlobService azureBlobService;

    @Transactional
    public PhotoUploadResponse uploadPhoto(MultipartFile file,UUID memorialId,  String title, String description) throws IOException {
        String url = azureBlobService.uploadPhoto(file, memorialId);

        Photo photo = new Photo();
        photo.setPhotoUrl(url);
        photo.setTitle(title);
        photo.setMemorialId(memorialId);
        photo.setDescription(description);
        photo.setUploadedAt(LocalDateTime.now());
        photoRepository.save(photo);

        return PhotoUploadResponse.builder()
                .photoId(photo.getPhotoId())
                .memorialId(photo.getMemorialId())
                .photoUrl(photo.getPhotoUrl())
                .title(photo.getTitle())
                .description(photo.getDescription())
                .uploadedAt(photo.getUploadedAt())
                .build();
    }
}
