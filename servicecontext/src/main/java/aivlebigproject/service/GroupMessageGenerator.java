package aivlebigproject.service;

import aivlebigproject.domain.*;
import aivlebigproject.domain.dto.FilterCriteria;
import aivlebigproject.domain.dto.SaveGroupMessageCommand;
import aivlebigproject.llm.GptClient;
import aivlebigproject.domain.dto.ParsedResult;
import aivlebigproject.llm.PromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

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
     * 고객 그룹을 대상으로 GPT 메시지를 생성하고 DB에 저장

    public ParsedResult generate(FilterCriteria criteria) {
        List<CustomerInfo> group = filteringService.filterCustomers(criteria);
        if (group.isEmpty()) {
            log.warn("❗ 조건에 맞는 고객이 없습니다: {}", criteria);
            throw new IllegalArgumentException("조건에 맞는 고객이 없습니다.");
        }

        ParsedResult parsed = generateGptMessage(criteria);
        ConversionService s1 = getServiceOrThrow(parsed.getService1());
        ConversionService s2 = getServiceOrThrow(parsed.getService2());

        enrichParsedResult(parsed, s1, s2);

        for (CustomerInfo customer : group) {
            try {
                RecommendMessage msg = buildMessage(customer, criteria, parsed, s1, s2);
                repository.save(msg);
            } catch (Exception ex) {
                log.warn("❌ 고객 ID {} 저장 실패: {}", customer.getId(), ex.getMessage());
            }
        }

        return parsed;
     }*/

    /**
     * GPT 메시지를 생성만 하고, DB에는 저장하지 않음 (미리보기용)
     */
    public ParsedResult preview(FilterCriteria criteria) {
        ParsedResult parsed = generateGptMessage(criteria);
        ConversionService s1 = getServiceOrThrow(parsed.getService1());
        ConversionService s2 = getServiceOrThrow(parsed.getService2());

        enrichParsedResult(parsed, s1, s2);
        return parsed;
    }

    /**
     * GPT 메시지 생성 및 파싱
     */
    private ParsedResult generateGptMessage(FilterCriteria criteria) {
        try {
            List<Map<String, String>> messages = promptBuilder.asChatMessage(criteria);
            String gptResponse = gptClient.callChatGpt(messages);
            return gptClient.parse(gptResponse);
        } catch (Exception e) {
            log.error("❌ GPT 메시지 생성 실패", e);
            throw new RuntimeException("GPT 메시지 생성 실패", e);
        }
    }

    /**
     * 전환 서비스 조회 (없으면 예외)
     */
    private ConversionService getServiceOrThrow(String name) {
        return conversionServiceRepository.findByServiceName(name)
                .orElseThrow(() -> new IllegalArgumentException("전환 서비스 없음: " + name));
    }

    /**
     * ParsedResult에 이미지 및 링크 추가
     */
    private void enrichParsedResult(ParsedResult parsed, ConversionService s1, ConversionService s2) {
        parsed.setService1ImageUrl(s1.getImageUrl());
        parsed.setService2ImageUrl(s2.getImageUrl());
        parsed.setService1DetailedUrl(s1.getDetailedUrl());
        parsed.setService2DetailedUrl(s2.getDetailedUrl());
    }

    /**
     * 최종 RecommendMessage 객체 생성
    private RecommendMessage buildMessage(CustomerInfo customer, FilterCriteria criteria,
                                          ParsedResult parsed, ConversionService s1, ConversionService s2) {
        RecommendMessage msg = new RecommendMessage();
        msg.setCustomerId(customer.getId());
        msg.setMessage(formatFinalMessage(customer, criteria, parsed));
        msg.setServiceId1(s1.getServiceId());
        msg.setServiceId2(s2.getServiceId());
        msg.setCreateMessageDate(LocalDateTime.now());
        msg.setAgeGroup(criteria.getAgeGroup());
        msg.setGender(criteria.getGender());
        msg.setDisease(criteria.getDisease());
        msg.setFamily(criteria.getFamily());
        msg.setImageUrl1(s1.getImageUrl());
        msg.setImageUrl2(s2.getImageUrl());
        msg.setDetailedUrl1(s1.getDetailedUrl());
        msg.setDetailedUrl2(s2.getDetailedUrl());
        return msg;
    }

    public void save(SavePreviewMessageCommand command) {
        RecommendMessage message = RecommendMessage.builder()
                .message(command.getMessage())
                .serviceId1(command.getServiceId1())
                .serviceId2(command.getServiceId2())
                .customerId(command.getCustomerId())
                .ageGroup(command.getFilterCriteria().getAgeGroup())
                .gender(command.getFilterCriteria().getGender())
                .disease(command.getFilterCriteria().getDisease())
                .family(command.getFilterCriteria().getFamily())
                .build();

        repository.save(message);
    }
     */


    /**
     * 고객별 메시지 포맷 템플릿

    private String formatFinalMessage(CustomerInfo customer, FilterCriteria criteria, ParsedResult result) {
        return String.format(
                "%s %s 고객님께 가장 어울리는 전환 서비스를 추천드립니다.\n%s\n\n[서비스 자세히 보기]\n- %s: %s\n- %s: %s",
                criteria.getAgeGroup(),
                criteria.getGender(),
                result.getMessage(),
                result.getService1(), result.getService1DetailedUrl(),
                result.getService2(), result.getService2DetailedUrl()
        );
    }
     */

    public void saveToGroup(SaveGroupMessageCommand command) {
        List<CustomerInfo> group = filteringService.filterCustomers(command.getFilterCriteria());
        for (CustomerInfo customer : group) {
            RecommendMessage msg = RecommendMessage.builder()
                    .customerId(customer.getId())
                    .message(command.getMessage())
                    .serviceId1(command.getServiceId1())
                    .serviceId2(command.getServiceId2())
                    .imageUrl1(command.getImageUrl1())
                    .imageUrl2(command.getImageUrl2())
                    .detailedUrl1(command.getDetailedUrl1())
                    .detailedUrl2(command.getDetailedUrl2())
                    .ageGroup(command.getFilterCriteria().getAgeGroup())
                    .gender(command.getFilterCriteria().getGender())
                    .disease(command.getFilterCriteria().getDisease())
                    .isMarried(command.getFilterCriteria().getIsMarried())
                    .hasChildren(command.getFilterCriteria().getHasChildren())
                    .createMessageDate(LocalDateTime.now())
                    .build();

            repository.save(msg);
        }
    }
}