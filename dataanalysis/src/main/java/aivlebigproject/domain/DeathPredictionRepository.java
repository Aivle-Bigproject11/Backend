package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.time.YearMonth; // LocalDate -> YearMonth로 변경
import java.util.Optional;

@RepositoryRestResource(
    collectionResourceRel = "deathPredictions",
    path = "deathPredictions"
)
public interface DeathPredictionRepository
    extends PagingAndSortingRepository<DeathPrediction, DeathPredictionId> {

    Optional<DeathPrediction> findByDateAndRegion(YearMonth date, String region); // <-- YearMonth로 변경
}