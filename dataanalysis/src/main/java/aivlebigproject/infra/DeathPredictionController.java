package aivlebigproject.infra;

import aivlebigproject.domain.DeathPrediction;
import aivlebigproject.domain.DeathPredictionRepository;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.time.YearMonth; // 이 라인은 유지 (POST 요청에서 YearMonth.parse() 사용하므로)

@RestController
@RequestMapping(value="/deathPredictions")
public class DeathPredictionController {

    private final DeathPredictionService deathPredictionService;
    private final DeathPredictionRepository deathPredictionRepository;

    public DeathPredictionController(DeathPredictionService deathPredictionService, DeathPredictionRepository deathPredictionRepository) {
        this.deathPredictionService = deathPredictionService;
        this.deathPredictionRepository = deathPredictionRepository;
    }

    @PostMapping("/request-prediction")
    public ResponseEntity<DeathPrediction> getPredictionAndRequestIfNotExists(@RequestBody AiRequestDto requestDto) {
        System.out.println("### 컨트롤러: 예측 요청 수신. date=" + requestDto.getDate() + ", region=" + requestDto.getRegion());

        Optional<DeathPrediction> prediction = deathPredictionService.getPredictionAndRequestIfNotExists(requestDto); // 수정 필요

        if (prediction.isPresent()) {
            System.out.println("### 컨트롤러: DB에 예측 데이터 존재. 즉시 반환.");
            return new ResponseEntity<>(prediction.get(), HttpStatus.OK);
        } else {
            System.out.println("### 컨트롤러: 예측 데이터 없음. AI 분석 요청 완료. 잠시 후 재조회 필요.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{date}/{region}")
    public ResponseEntity<DeathPrediction> getDeathPrediction(
        @PathVariable String date, // YearMonth -> String으로 변경
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