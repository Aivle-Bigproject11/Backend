package aivlebigproject.infra;

import aivlebigproject.domain.RecommendMessage;
import aivlebigproject.domain.RecommendMessageRepository;
import aivlebigproject.domain.dto.FilterCriteria;
import aivlebigproject.domain.dto.ParsedResult;
import aivlebigproject.domain.dto.SaveGroupMessageCommand;
import aivlebigproject.domain.dto.SavePreviewMessageCommand;
import aivlebigproject.service.GroupMessageGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/recommendMessages")
@RequiredArgsConstructor
public class RecommendMessageController {

    private final GroupMessageGenerator groupMessageGenerator;
    private final RecommendMessageRepository repository;

    /**
     * [1] GPT를 통해 메시지를 생성하되, 저장하지 않고 미리보기 용으로만 반환
     */
    @PostMapping("/preview-message")
    public ResponseEntity<ParsedResult> previewMessage(@RequestBody FilterCriteria criteria) {
        ParsedResult result = groupMessageGenerator.preview(criteria);
        return ResponseEntity.ok(result);
    }

    /**
     * [2] 생성된 메시지를 DB에 저장
     */
    @PostMapping("/save-preview")
    public ResponseEntity<Void> savePreviewMessage(@RequestBody SavePreviewMessageCommand command) {
        RecommendMessage message = RecommendMessage.builder()
                .message(command.getMessage())
                .serviceId1(command.getServiceId1())
                .serviceId2(command.getServiceId2())
                .customerId(command.getCustomerId())
                .imageUrl1(command.getImageUrl1())
                .imageUrl2(command.getImageUrl2())
                .detailedUrl1(command.getDetailedUrl1())
                .detailedUrl2(command.getDetailedUrl2())
                .ageGroup(command.getFilterCriteria().getAgeGroup())
                .gender(command.getFilterCriteria().getGender())
                .disease(command.getFilterCriteria().getDisease())
                .family(command.getFilterCriteria().getFamily())
                .createMessageDate(LocalDateTime.now())
                .build();

        repository.save(message);
        return ResponseEntity.ok().build();
    }

    /**
     * [3] 특정 고객 ID로 모든 메시지 조회
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RecommendMessage>> getMessagesByCustomer(@PathVariable Long customerId) {
        List<RecommendMessage> messages = repository.findByCustomerId(customerId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    /**
     * [4] 특정 고객 ID로 가장 최근 메시지 조회
     */
    @GetMapping("/customer/{customerId}/latest")
    public ResponseEntity<RecommendMessage> getLatestMessageByCustomer(@PathVariable Long customerId) {
        return repository.findTopByCustomerIdOrderByCreateMessageDateDesc(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/generate-group-message")
    public ResponseEntity<Void> saveGroupMessage(@RequestBody SaveGroupMessageCommand command) {
        groupMessageGenerator.saveToGroup(command); // 필터된 고객 전원에게 저장
        return ResponseEntity.ok().build();
    }
}