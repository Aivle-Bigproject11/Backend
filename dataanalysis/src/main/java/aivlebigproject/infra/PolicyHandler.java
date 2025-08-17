package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    DeathPredictionRepository deathPredictionRepository;

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['eventType']=='DeathPredictionEvent'")
    public void handlePredictedDeathReceived(@Payload String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DeathPredictionEvent event = mapper.readValue(message, DeathPredictionEvent.class);

            if (event.getDate() == null || event.getRegion() == null || event.getPredictedDeaths() == null) {
                log.warn("누락된 필드 있음, 저장 생략");
                return;
            }

            DeathPredictionId id = new DeathPredictionId(event.getDate(), event.getRegion());
            DeathPrediction prediction = deathPredictionRepository.findById(id).orElse(new DeathPrediction(event.getDate(), event.getRegion(), null));
            prediction.setDeaths(event.getPredictedDeaths());
            prediction.setGrowthRate(event.getGrowthRate());
            prediction.setRegionalPercentage(event.getRegionalPercentage());
            prediction.setPreviousYearDeaths(event.getPreviousYearDeaths());
            deathPredictionRepository.save(prediction);

            log.info("저장 완료: {}", prediction);
        } catch (Exception e) {
            log.error("메시지 처리 실패: {}", e.getMessage(), e);
        }
    }
}