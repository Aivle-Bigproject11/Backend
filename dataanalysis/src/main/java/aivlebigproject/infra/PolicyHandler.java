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
import lombok.extern.slf4j.Slf4j; // Slf4j 로거 임포트

@Service
@Transactional
@Slf4j // Slf4j 로거 사용을 위한 어노테이션 추가
public class PolicyHandler {

    @Autowired
    DeathPredictionRepository deathPredictionRepository;


    @StreamListener(KafkaProcessor.INPUT)
    public void handlePredictedDeathReceived(@Payload String message) {
        log.info("##### Kafka 메시지 수신: {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            DeathPredictionEvent deathPredictionEvent = objectMapper.readValue(
                message,
                DeathPredictionEvent.class
            );
            // Fast API가 보낸 예측 결과를 로그에 출력
            log.info(
                "##### DeathPredictionEvent 수신: eventType={}, date={}, region={}, predictedDeaths={}",
                deathPredictionEvent.getEventType(),
                deathPredictionEvent.getDate(),
                deathPredictionEvent.getRegion(),
                deathPredictionEvent.getPredictedDeaths()
            );

            // 예측 결과가 유효한지 확인
            if (deathPredictionEvent.getPredictedDeaths() == null) {
                log.warn("##### 수신된 예측 결과에 predictedDeaths 값이 없습니다. 처리를 건너뜁니다.");
                return;
            }

            // 복합키를 사용하여 엔티티 생성 또는 조회
            DeathPredictionId id = new DeathPredictionId(
                deathPredictionEvent.getDate(),
                deathPredictionEvent.getRegion()
            );

            // DB에서 기존 예측 결과가 있는지 확인
            DeathPrediction deathPrediction = deathPredictionRepository.findById(id).orElseGet(() -> {
                log.info("새로운 예측 결과 엔티티 생성 예정: date={}, region={}", id.getDate(), id.getRegion());
                DeathPrediction newPrediction = new DeathPrediction();
                newPrediction.setDate(deathPredictionEvent.getDate());
                newPrediction.setRegion(deathPredictionEvent.getRegion());
                return newPrediction;
            });

            // 예측된 사망자 수 필드를 업데이트 (새로운 필드명 사용)
            deathPrediction.setDeaths(deathPredictionEvent.getPredictedDeaths());

            // DB에 저장
            deathPredictionRepository.save(deathPrediction);
            log.info("##### 예측 결과 DB 저장/업데이트 완료: {}", deathPrediction.toString());

        } catch (Exception e) {
            log.error("##### Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
