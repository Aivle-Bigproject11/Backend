package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionId;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.domain.DeathPredictionEvent; // DeathPredictionEvent 임포트 확인
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
// import javax.naming.NameParser; // <-- 사용하지 않으므로 제거
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional // DB 저장 로직이 추가되므로 @Transactional 활성화
public class PolicyHandler {

    @Autowired
    DeathPredictionRepository deathPredictionRepository; // <-- Repository 주입 활성화

    /**
     * KafkaProcessor.INPUT (event-in) 채널에서 메시지를 수신하여 처리합니다.
     * 이 리스너는 FastAPI로부터 오는 DeathPredictionEvent를 받아 처리하고 DB에 저장합니다.
     *
     * @param message Kafka 메시지의 페이로드 (JSON 문자열)
     */
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeathPredicted_handlePredictedDeathReceived(@Payload String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            DeathPredictionEvent deathPredictedEvent = objectMapper.readValue(
                message,
                DeathPredictionEvent.class
            );

            System.out.println(
                "\n\n##### Kafka에서 DeathPredictionEvent 수신 - " + // 이름 변경: DeathPredictedEvent -> DeathPredictionEvent
                deathPredictedEvent.getEventType() +
                ": " +
                deathPredictedEvent.toString() +
                "\n\n"
            );

            // 중요: 이제 예측 결과를 Spring Boot DB에 저장하는 로직을 추가합니다.
            DeathPredictionId id = new DeathPredictionId(
                deathPredictedEvent.getDate(),
                deathPredictedEvent.getRegion()
            );

            DeathPrediction deathPrediction;
            // 기존 예측 결과가 있는지 확인 (업데이트 또는 새로 생성)
            deathPrediction = deathPredictionRepository.findById(id).orElseGet(() -> {
                DeathPrediction newPrediction = new DeathPrediction();
                newPrediction.setDate(deathPredictedEvent.getDate());
                newPrediction.setRegion(deathPredictedEvent.getRegion());
                System.out.println("새로운 예측 결과 생성 예정: " + id.getDate() + ", " + id.getRegion());
                return newPrediction;
            });

            // 예측된 사망자 수 업데이트
            // DeathPredictionEvent의 previousYearDeaths 필드를 PredictedDeaths로 사용한다고 가정.
            // 만약 FastAPI가 다른 필드로 예측값을 보낸다면, DeathPredictionEvent DTO를 수정하거나 여기서 적절히 매핑해야 합니다.
            deathPrediction.setDeaths(deathPredictedEvent.getPreviousYearDeaths()); // <-- 수정: 인스턴스 메서드 호출

            // DB에 저장
            deathPredictionRepository.save(deathPrediction);
            System.out.println("##### 예측 결과 DB 저장/업데이트 완료: " + deathPrediction.toString());

        } catch (Exception e) {
            System.err.println("##### Kafka 메시지 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
