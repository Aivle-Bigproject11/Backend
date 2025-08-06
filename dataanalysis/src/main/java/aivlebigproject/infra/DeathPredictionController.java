package aivlebigproject.infra;
import java.util.List;
import aivlebigproject.domain.*;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/deathPredictions")
public class DeathPredictionController {

    private final DeathPredictionService deathPredictionService;
    private final DeathPredictionRepository deathPredictionRepository;

    public DeathPredictionController(DeathPredictionService service, DeathPredictionRepository repo) {
        this.deathPredictionService = service;
        this.deathPredictionRepository = repo;
    }

    @PostMapping("/request-prediction")
    public ResponseEntity<DeathPrediction> getPredictionAndRequestIfNotExists(@RequestBody AiRequestDto requestDto) {
        Optional<DeathPrediction> prediction = deathPredictionService.getPredictionAndRequestIfNotExists(requestDto);
        return prediction
            .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/{date}/{region}")
    public ResponseEntity<DeathPrediction> getDeathPrediction(@PathVariable String date, @PathVariable String region) {
        DeathPredictionId id = new DeathPredictionId(date, region);
        return deathPredictionRepository.findById(id)
            .map(pred -> new ResponseEntity<>(pred, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
@GetMapping("/by-date/{date}")
    public ResponseEntity<List<DeathPrediction>> getPredictionsByDate(@PathVariable String date) {
        List<DeathPrediction> predictions = deathPredictionRepository.findByIdDate(date);

        if (predictions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(predictions, HttpStatus.OK);
    }

    // 수정: 리포지토리 메서드 이름 변경
    @GetMapping("/by-region/{region}")
    public ResponseEntity<List<DeathPrediction>> getPredictionsByRegion(@PathVariable String region) {
        List<DeathPrediction> predictions = deathPredictionRepository.findByIdRegion(region);

        if (predictions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(predictions, HttpStatus.OK);
    }
}