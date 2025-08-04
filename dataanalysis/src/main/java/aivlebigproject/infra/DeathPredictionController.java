package aivlebigproject.infra;

import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.time.YearMonth; 


@RestController
@RequestMapping(value="/deathPredictions")
public class DeathPredictionController {

    private final DeathPredictionService deathPredictionService;
    private final DeathPredictionRepository deathPredictionRepository; // 기존 조회 엔드포인트를 위해 유지

    // 생성자 주입
    public DeathPredictionController(DeathPredictionService deathPredictionService, DeathPredictionRepository deathPredictionRepository) {
        this.deathPredictionService = deathPredictionService;
        this.deathPredictionRepository = deathPredictionRepository;
    }

    /**
     * 대시보드 접속 시 호출되는 메인 엔드포인트
     * DB 조회 -> 데이터 있으면 반환, 없으면 Kafka로 AI 분석 요청
     * @param requestDto 날짜와 지역 정보가 포함된 요청 DTO
     * @return DB에 데이터가 있으면 예측 결과, 없으면 '204 No Content' 반환
     */
    @PostMapping("/request-prediction")
    public ResponseEntity<DeathPrediction> getPredictionAndRequestIfNotExists(@RequestBody AiRequestDto requestDto) {
        System.out.println("### 컨트롤러: 예측 요청 수신. date=" + requestDto.getDate() + ", region=" + requestDto.getRegion());

        Optional<DeathPrediction> prediction = deathPredictionService.getPredictionAndRequestIfNotExists(requestDto);

        if (prediction.isPresent()) {
            System.out.println("### 컨트롤러: DB에 예측 데이터 존재. 즉시 반환.");
            return new ResponseEntity<>(prediction.get(), HttpStatus.OK);
        } else {
            System.out.println("### 컨트롤러: 예측 데이터 없음. AI 분석 요청 완료. 잠시 후 재조회 필요.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
    }

    // 기존의 /get/all 이나 /{date}/{region} 엔드포인트는 필요에 따라 유지하거나,
    // 위 엔드포인트로 대체하여 사용 가능합니다.
    @GetMapping("/{date}/{region}")
    public ResponseEntity<DeathPrediction> getDeathPrediction(
        @PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) YearMonth date,
        @PathVariable String region
    ) {
        Optional<DeathPrediction> deathPredictionOptional = deathPredictionRepository.findByDateAndRegion(date, region);
        if (deathPredictionOptional.isPresent()) {
            return new ResponseEntity<>(deathPredictionOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}