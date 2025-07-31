package aivlebigproject.service;

import aivlebigproject.domain.*;
import aivlebigproject.domain.dto.FilterCriteria;
import aivlebigproject.llm.GptClient;
import aivlebigproject.llm.ParsedResult;
import aivlebigproject.llm.PromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupMessageGenerator {

    private final CustomerFilteringService filteringService;
    private final RecommendMessageRepository repository;
    private final ConversionServiceRepository conversionServiceRepository;
    private final GptClient gptClient;
    private final PromptBuilder promptBuilder;

    /**
     * 조건에 맞는 고객을 필터링하고,
     * GPT를 이용해 서비스 추천 메시지를 생성 후,
     * RecommendMessage로 저장한다.
     *
     * @param criteria 필터 조건
     * @return GPT가 생성한 서비스 추천 결과
     */
    public ParsedResult generate(FilterCriteria criteria) {
        List<CustomerInfo> group = filteringService.filterCustomers(criteria);
        if (group.isEmpty()) {
            log.warn("조건에 맞는 고객이 없습니다: {}", criteria);
            throw new IllegalArgumentException("조건에 맞는 고객이 없습니다.");
        }

        try {
            // 1. GPT 프롬프트 생성
            List<Map<String, String>> messages = PromptBuilder.asChatMessage(criteria);

            // 2. GPT 호출 및 응답 파싱
            String gptResponse = gptClient.callChatGpt(messages);
            ParsedResult parsed = GptClient.parse(gptResponse);

            // 3. 각 고객에게 메시지 저장
            for (CustomerInfo customer : group) {
                try {
                    RecommendMessage msg = new RecommendMessage();
                    msg.setCustomerId(customer.getId());
                    msg.setMessage(parsed.getMessage());
                    msg.setServiceId1(findServiceIdByName(parsed.getService1()));
                    msg.setServiceId2(findServiceIdByName(parsed.getService2()));
                    msg.setCreateMessageDate(new Date());
                    msg.setAgeGroup(criteria.getAgeGroup());
                    msg.setGender(criteria.getGender());
                    msg.setDisease(criteria.getDisease());
                    msg.setFamily(criteria.getFamily());

                    repository.save(msg);
                } catch (Exception ex) {
                    log.warn("❌ 고객 ID {} 저장 실패: {}", customer.getId(), ex.getMessage());
                }
            }

            return parsed;

        } catch (Exception e) {
            log.error("전환 서비스 추천 또는 저장 중 오류 발생", e);
            throw new RuntimeException("전환 메시지 생성 실패", e);
        }
    }

    private Long findServiceIdByName(String serviceName) {
        return conversionServiceRepository.findByServiceName(serviceName)
                .map(ConversionService::getServiceId)
                .orElseThrow(() ->
                        new IllegalArgumentException("서비스 이름에 해당하는 전환 서비스가 없습니다: " + serviceName));
    }
}