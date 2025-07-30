package aivlebigproject.infra;

import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.domain.DeathPredictionEvent; // DeathPredictionEvent 임포트 추가
import aivlebigproject.dto.AiRequestDto; // AiRequestDto는 요청 데이터를 받을 때 사용

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/deathPredictions")
@Transactional
public class DeathPredictionController {

    @Autowired
    DeathPredictionRepository deathPredictionRepository;

    /**
     * 새로운 사망 예측 요청을 처리하고, Kafka로 DeathPredictionEvent를 발행합니다.
     * 이 이벤트는 Python AI 서비스가 구독하여 예측을 수행하게 됩니다.
     * 예측 결과는 Python 서비스가 다시 Kafka로 발행하고, PolicyHandler에서 수신하여 DB에 저장합니다.
     */
    @PostMapping
    public ResponseEntity<Void> requestDeathPrediction(@RequestBody AiRequestDto requestDto) {
        try {
            System.out.println("### 예측 요청 수신 (Controller): " + requestDto.toString());

            // AI 예측을 요청하는 이벤트를 생성합니다.
            // AiRequestDto의 데이터를 DeathPredictionEvent에 매핑하여 사용합니다.
            DeathPredictionEvent deathPredictionEvent = new DeathPredictionEvent(
                requestDto.getDate(),
                requestDto.getRegion(),
                requestDto.getPreviousYearDeaths() // 이 필드를 AI 입력으로 사용한다고 가정
            );

            // Kafka로 이벤트 발행
            // AbstractEvent에 정의된 publish() 메서드를 호출합니다.
            deathPredictionEvent.publish();

            System.out.println("### Kafka로 DeathPredictionEvent 발행 완료. AI 예측 대기 중...");

            // 예측 결과는 비동기적으로 Kafka를 통해 PolicyHandler로 수신되므로,
            // 클라이언트에게는 요청이 성공적으로 접수되었음을 알립니다.
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.err.println("### 예측 요청 처리 중 오류 발생 (Controller): " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 날짜와 지역의 사망 예측 데이터를 조회합니다.
     * 이 데이터는 Python AI로부터 Kafka를 통해 수신되어 DB에 저장된 결과입니다.
     */
    @GetMapping("/{date}/{region}")
    public ResponseEntity<DeathPrediction> getDeathPrediction(
        @PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
        @PathVariable String region
    ) {
        // DeathPredictionRepository에 findByDateAndRegion 메서드를 추가해야 합니다.
        Optional<DeathPrediction> deathPredictionOptional = deathPredictionRepository.findByDateAndRegion(date, region);
        if (deathPredictionOptional.isPresent()) {
            System.out.println("### 사망 예측 데이터 조회 (Controller): " + date + ", " + region);
            return new ResponseEntity<>(deathPredictionOptional.get(), HttpStatus.OK);
        } else {
            System.out.println("### 사망 예측 데이터 없음 (Controller): " + date + ", " + region);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 모든 사망 예측 데이터를 조회합니다.
     * 이 데이터는 Python AI로부터 Kafka를 통해 수신되어 DB에 저장된 결과입니다.
     */
    @GetMapping
    public ResponseEntity<List<DeathPrediction>> getAllDeathPredictions() {
        List<DeathPrediction> deathPredictions = (List<DeathPrediction>) deathPredictionRepository.findAll();
        System.out.println("### 모든 사망 예측 데이터 조회 (Controller). 총 " + deathPredictions.size() + "개.");
        return new ResponseEntity<>(deathPredictions, HttpStatus.OK);
    }
}