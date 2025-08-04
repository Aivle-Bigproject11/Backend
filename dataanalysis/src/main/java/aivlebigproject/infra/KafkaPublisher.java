package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import aivlebigproject.dto.AiRequestDto;
import aivlebigproject.domain.DeathPredictionEvent; 

@Service
public class KafkaPublisher {

    @Autowired
    private StreamBridge streamBridge;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    // AI 분석 요청을 보낼 때 이 메서드를 사용합니다.
    public void publishAiRequest(AiRequestDto requestDto) {
        DeathPredictionEvent event = new DeathPredictionEvent(
            requestDto.getDate(),
            requestDto.getRegion(),
            requestDto.getPreviousYearDeaths() // 이 필드가 AI 분석의 입력값이라고 가정
        );

        String jsonPayload = null;
        try {
            jsonPayload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            // ... (기존 예외 처리)
        }

        Message<String> message = MessageBuilder
            .withPayload(jsonPayload)
            .setHeader("eventType", event.getEventType())
            .build();

        streamBridge.send(KafkaProcessor.OUTPUT, message);
        System.out.println("### Kafka AI 요청 발행 완료: " + event.getEventType() + " to " + KafkaProcessor.OUTPUT);
    }
}