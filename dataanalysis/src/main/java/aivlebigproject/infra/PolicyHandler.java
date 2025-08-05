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
        log.info("##### Kafka 메시지 수신: {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            DeathPredictionEvent deathPredictionEvent = objectMapper.readValue(
                message,
                DeathPredictionEvent.class
            );

            log.info(
                "##### DeathPredictionEvent 수신: eventType={}, date={}, region={}, predictedDeaths={}",
                deathPredictionEvent.getEventType(),
                deathPredictionEvent.getDate(),
                deathPredictionEvent.getRegion(),
                deathPredictionEvent.getPredictedDeaths()
            );

            if (deathPredictionEvent.getPredictedDeaths() == null) {
                log.warn("##### 수신된 예측 결과에 predictedDeaths 값이 없습니다. 처리를 건너뜁니다.");
                return;
            }

            // String 타입의 date를 그대로 사용
            DeathPredictionId id = new DeathPredictionId(
                deathPredictionEvent.getDate(),
                deathPredictionEvent.getRegion()
            );

            DeathPrediction deathPrediction = deathPredictionRepository.findById(id).orElseGet(() -> {
                log.info("새로운 예측 결과 엔티티 생성 예정: date={}, region={}", id.getDate(), id.getRegion());
                DeathPrediction newPrediction = new DeathPrediction();
                newPrediction.setDate(deathPredictionEvent.getDate());
                newPrediction.setRegion(deathPredictionEvent.getRegion());
                return newPrediction;
            });

            deathPrediction.setDeaths(deathPredictionEvent.getPredictedDeaths());
            deathPredictionRepository.save(deathPrediction);
            log.info("##### 예측 결과 DB 저장/업데이트 완료: {}", deathPrediction.toString());

        } catch (Exception e) {
            log.error("##### Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
