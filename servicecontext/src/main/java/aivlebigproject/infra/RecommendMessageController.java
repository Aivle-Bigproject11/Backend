package aivlebigproject.infra;

import aivlebigproject.domain.dto.FilterCriteria;
import aivlebigproject.llm.ParsedResult;
import aivlebigproject.service.GroupMessageGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adapter
@RestController
@RequestMapping("/recommendMessages") // 기본 경로 설정
@RequiredArgsConstructor
public class RecommendMessageController {

    private final GroupMessageGenerator groupMessageGenerator;

    /**
     * 조건을 기반으로 고객 그룹을 필터링하고,
     * 해당 그룹에 공통된 GPT 기반 추천 메시지를 생성 및 저장한 후,
     * GPT가 추천한 결과를 반환한다.
     */
    @PostMapping("/generate-group-message")
    public ResponseEntity<ParsedResult> generateGroupMessage(@RequestBody FilterCriteria criteria) {
        ParsedResult result = groupMessageGenerator.generate(criteria);
        return ResponseEntity.ok(result);
    }
}
//>>> Clean Arch / Inbound Adapter