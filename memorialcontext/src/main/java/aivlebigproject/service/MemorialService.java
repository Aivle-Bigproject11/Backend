package aivlebigproject.service;

import aivlebigproject.dto.*;
import aivlebigproject.event.listener.FuneralInfoRegistered;
import aivlebigproject.model.Comment;
import aivlebigproject.model.Memorial;
import aivlebigproject.model.Photo;
import aivlebigproject.model.Video;
import aivlebigproject.repository.CommentRepository;
import aivlebigproject.repository.MemorialRepository;
import aivlebigproject.repository.PhotoRepository;
import aivlebigproject.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemorialService {

    private final AzureBlobService azureBlobService;

    private final MemorialRepository memorialRepository;
    private final VideoRepository videoRepository;
    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;
    private final ChatGptService chatGptService;


    @Transactional
    public Memorial createMemorial(FuneralInfoRegistered funeralInfoRegistered) {
        Memorial memorial = new Memorial();
        memorial.setCustomerId(funeralInfoRegistered.getCustomerId());
        memorial.setName(funeralInfoRegistered.getDeceasedName());
        memorial.setAge(funeralInfoRegistered.getDeceasedAge());
        memorial.setBirthDate(
                funeralInfoRegistered.getDeceasedBirthOfDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
        memorial.setDeceasedDate(
                funeralInfoRegistered.getDeceasedDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
        memorial.setGender(funeralInfoRegistered.getDeceasedGender());

        log.info("Memorial created: {}", memorial);

        return memorialRepository.save(memorial);
    }

    @Transactional
    public MemorialProfileResponse updateProfileImage(UUID memorialId, MultipartFile profileImage) throws IOException {
        if (!memorialRepository.existsById(memorialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 추모관을 찾을 수 없습니다.: " + memorialId);
        }

        String url = azureBlobService.uploadProfileImage(profileImage, memorialId);
        Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
        memorial.updateProfileImage(url);
        memorialRepository.save(memorial);

        return MemorialProfileResponse.builder()
                .memorialId(memorial.getMemorialId())
                .photoUrl(memorial.getProfileImageUrl())
                .build();
    }

    @Transactional
    public MemorialProfileResponse deleteProfileImage(UUID memorialId) {
        if (!memorialRepository.existsById(memorialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 추모관을 찾을 수 없습니다.: " + memorialId);
        }

        Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
        memorial.setProfileImageUrl(null);
        memorialRepository.save(memorial);

        return MemorialProfileResponse.builder()
                .memorialId(memorial.getMemorialId())
                .photoUrl(memorial.getProfileImageUrl())
                .build();
    }


    public MemorialDetail getMemorialDetail(UUID memorialId) {
        if (!memorialRepository.existsById(memorialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 추모관을 찾을 수 없습니다.: " + memorialId);
        }
        Memorial memorial = memorialRepository.findById(memorialId).orElse(null);

        List<Video> videos = videoRepository.findByMemorialIdOrderByCompletedAtDesc(memorialId);
        List<Comment> comments = commentRepository.findByMemorialIdOrderByCreatedAtDesc(memorialId);
        List<Photo> photos = photoRepository.findByMemorialIdOrderByUploadedAtDesc(memorialId);

        return MemorialDetail.builder()
                .memorialId(memorial.getMemorialId())
                .deceasedName(memorial.getName())
                .profileImageUrl(memorial.getProfileImageUrl())
                .deceasedAge(memorial.getAge())
                .gender(memorial.getGender())
                .birthDate(memorial.getBirthDate())
                .deceasedDate(memorial.getDeceasedDate())
                .tribute(memorial.getTribute())
                .createdAt(memorial.getCreatedAt())
                .photos(photos)
                .videos(videos)
                .comments(comments)
                .build();
    }

    @Transactional
    public TributeResponse createTribute(UUID memorialId, TributeGenerationRequest request) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new EntityNotFoundException("추모관을 찾을 수 없습니다: " + memorialId));

        String prompt = buildTributePrompt(memorial, request);

        String generatedTribute = chatGptService.generateMessage(prompt);

        memorial.updateTribute(generatedTribute);
        memorialRepository.save(memorial);

        return TributeResponse.builder()
                .memorialId(memorialId)
                .tribute(generatedTribute)
                .tributeGeneratedAt(memorial.getTributeGeneratedAt())
                .build();
    }


    public String buildTributePrompt(Memorial memorial, TributeGenerationRequest request){
        StringBuilder prompt = new StringBuilder();

        prompt.append("다음 정보를 바탕으로 따뜻하고 진심어린 추모사를 작성해주세요:\n");
        prompt.append("고인명: ").append(memorial.getName()).append("\n");
        prompt.append("생년월일: ").append(memorial.getBirthDate()).append("\n");
        prompt.append("별세일: ").append(memorial.getDeceasedDate()).append("\n");
        prompt.append("생년월일: ").append(memorial.getBirthDate()).append("\n");
        prompt.append("키워드: ").append(request.getKeywords()).append("\n");
        prompt.append("요청사항:").append(request.getTributeRequest()).append("\n");
        return prompt.toString();
    }

    @Transactional
    public TributeResponse updateTribute(UUID memorialId, TributeUpdateRequest request) {

        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new EntityNotFoundException("추모관을 찾을 수 없습니다: " + memorialId));

        // 사용자 수정 내용 저장
        memorial.updateTribute(request.getTribute());
        memorialRepository.save(memorial);

        return TributeResponse.builder()
                .memorialId(memorialId)
                .tribute(memorial.getTribute())
                .tributeGeneratedAt(memorial.getTributeGeneratedAt())
                .build();

    }

    @Transactional
    public TributeResponse deleteTribute(UUID memorialId) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new EntityNotFoundException("추모관을 찾을 수 없습니다: " + memorialId));
        memorial.setTribute(null);
        memorial.setTributeGeneratedAt(null);
        memorialRepository.save(memorial);

        return TributeResponse.builder()
                .memorialId(memorialId)
                .tribute(memorial.getTribute())
                .tributeGeneratedAt(memorial.getTributeGeneratedAt())
                .build();
    }
}
