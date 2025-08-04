package aivlebigproject.infra;

import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.domain.DeathPredictionEvent;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class DeathPredictionService {

    private final DeathPredictionRepository deathPredictionRepository;
    private final KafkaPublisher kafkaPublisher; // 아래에서 구현할 Kafka 발행 서비스

    // 생성자 주입
    public DeathPredictionService(DeathPredictionRepository deathPredictionRepository, KafkaPublisher kafkaPublisher) {
        this.deathPredictionRepository = deathPredictionRepository;
        this.kafkaPublisher = kafkaPublisher;
    }


    public Optional<DeathPrediction> getPredictionAndRequestIfNotExists(AiRequestDto requestDto) {
        System.out.println("### 서비스: 예측 데이터 조회 시작");
        
        // 1. 현재 날짜와 지역을 기준으로 DB에서 예측 결과 조회
        YearMonth date = requestDto.getDate();
        String region = requestDto.getRegion();
        Optional<DeathPrediction> existingPrediction = deathPredictionRepository.findByDateAndRegion(date, region);

        // 2. DB에 데이터가 있으면 즉시 반환
        if (existingPrediction.isPresent()) {
            System.out.println("### 서비스: DB에 예측 데이터 존재. 즉시 반환.");
            return existingPrediction;
        }

        // 3. DB에 데이터가 없으면 AI 분석 요청을 Kafka로 발행
        System.out.println("### 서비스: DB에 예측 데이터 없음. AI 분석 요청을 Kafka로 발행합니다.");
        
        // AiRequestDto의 데이터를 이벤트 객체로 변환
        DeathPredictionEvent event = new DeathPredictionEvent(
            date,
            region,
            requestDto.getPreviousYearDeaths() // AI 요청에 필요한 필드
        );
        
        kafkaPublisher.publishAiRequest(requestDto);
        
        return Optional.empty(); // 데이터가 없으므로 빈 값 반환
    }
}