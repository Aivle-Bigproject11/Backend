package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.time.LocalDate; // LocalDate 임포트 추가
import java.util.Optional; // Optional 임포트 추가

@RepositoryRestResource(
    collectionResourceRel = "deathPredictions",
    path = "deathPredictions"
)
public interface DeathPredictionRepository
    extends PagingAndSortingRepository<DeathPrediction, DeathPredictionId> {

    Optional<DeathPrediction> findByDateAndRegion(LocalDate date, String region);
    // ------------------------------------
}
