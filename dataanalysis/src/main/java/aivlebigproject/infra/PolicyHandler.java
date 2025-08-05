package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionId;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.domain.DeathPredictionEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PolicyHandler {

    @Autowired
    DeathPredictionRepository deathPredictionRepository;

    @StreamListener(value = KafkaProcessor.INPUT , condition = "headers['eventType']=='DeathPredictionEvent'")
    public void handlePredictedDeathReceived(@Payload String message) {
        log.info("📥 Kafka 메시지 수신: {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            DeathPredictionEvent event = objectMapper.readValue(message, DeathPredictionEvent.class);

            log.info("📦 이벤트 파싱 완료: eventType={}, date={}, region={}, predictedDeaths={}",
                event.getEventType(), event.getDate(), event.getRegion(), event.getPredictedDeaths());

            // 🔒 방어 로직 추가
            if (event.getDate() == null || event.getRegion() == null || event.getPredictedDeaths() == null) {
                log.warn("⚠️ 누락된 필드 존재. 처리 중단: date={}, region={}, predictedDeaths={}",
                    event.getDate(), event.getRegion(), event.getPredictedDeaths());
                return;
            }

            DeathPredictionId id = new DeathPredictionId(event.getDate(), event.getRegion());

            DeathPrediction deathPrediction = deathPredictionRepository.findById(id).orElseGet(() -> {
                log.info("➕ 새로운 예측 결과 생성: date={}, region={}", id.getDate(), id.getRegion());
                DeathPrediction newPrediction = new DeathPrediction();
                newPrediction.setDate(id.getDate());
                newPrediction.setRegion(id.getRegion());
                return newPrediction;
            });

            deathPrediction.setDeaths(event.getPredictedDeaths());
            deathPredictionRepository.save(deathPrediction);

            log.info("✅ 예측 결과 저장/업데이트 완료: {}", deathPrediction);

        } catch (Exception e) {
            log.error("❌ Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}