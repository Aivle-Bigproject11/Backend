package aivlebigproject.infra;

import aivlebigproject.domain.*;
import aivlebigproject.dto.AiRequestDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeathPredictionService {

    private final DeathPredictionRepository deathPredictionRepository;
    private final KafkaPublisher kafkaPublisher;

    public DeathPredictionService(DeathPredictionRepository repo, KafkaPublisher publisher) {
        this.deathPredictionRepository = repo;
        this.kafkaPublisher = publisher;
    }

    public Optional<DeathPrediction> getPredictionAndRequestIfNotExists(AiRequestDto requestDto) {
        DeathPredictionId id = new DeathPredictionId(requestDto.getDate(), requestDto.getRegion());
        Optional<DeathPrediction> prediction = deathPredictionRepository.findById(id);

        if (prediction.isPresent()) return prediction;

        kafkaPublisher.sendAiRequest(requestDto);
        return Optional.empty();
    }
}