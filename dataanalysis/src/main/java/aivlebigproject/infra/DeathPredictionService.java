package aivlebigproject.infra;

import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.stereotype.Service;
import java.util.Optional;
// import java.time.YearMonth; // 이 라인은 삭제합니다.

@Service
public class DeathPredictionService {

    private final DeathPredictionRepository deathPredictionRepository;
    private final KafkaPublisher kafkaPublisher; // KafkaPublisher 의존성 추가

    public DeathPredictionService(DeathPredictionRepository deathPredictionRepository, KafkaPublisher kafkaPublisher) {
        this.deathPredictionRepository = deathPredictionRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    // 메서드 시그니처를 AiRequestDto를 받도록 변경
    public Optional<DeathPrediction> getPredictionAndRequestIfNotExists(AiRequestDto requestDto) {
        // DB에서 String 타입의 date로 조회
        Optional<DeathPrediction> prediction = deathPredictionRepository.findByDateAndRegion(
            requestDto.getDate(),
            requestDto.getRegion()
        );

        if (prediction.isPresent()) {
            return prediction;
        } else {
            // Kafka로 AI 분석 요청
            kafkaPublisher.sendAiRequest(requestDto);
            return Optional.empty();
        }
    }
}