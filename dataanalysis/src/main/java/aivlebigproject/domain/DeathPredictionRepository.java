package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeathPredictionRepository extends PagingAndSortingRepository<DeathPrediction, DeathPredictionId> {
}