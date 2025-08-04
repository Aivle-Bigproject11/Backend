package aivlebigproject.infra;

import aivlebigproject.dto.AiRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Autowired
    private StreamBridge streamBridge;
    
    public void sendAiRequest(AiRequestDto requestDto) {
        System.out.println("### KafkaPublisher: AI 분석 요청 메시지 발행 시작.");
        
        Message<AiRequestDto> message = MessageBuilder
            .withPayload(requestDto)
            .setHeader("eventType", "AiRequestEvent") // 이벤트 타입은 AiRequestEvent로 유지
            .build();

        streamBridge.send("event-out", message); // 바인딩 이름은 application.yml에 맞게 설정
        System.out.println("### Kafka AI 요청 발행 완료: AiRequestEvent to event-out");
    }
}