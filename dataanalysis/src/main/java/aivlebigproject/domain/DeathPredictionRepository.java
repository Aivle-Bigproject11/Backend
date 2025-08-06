package aivlebigproject.domain;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeathPredictionRepository extends PagingAndSortingRepository<DeathPrediction, DeathPredictionId> {

    List<DeathPrediction> findByIdDate(String date);
    List<DeathPrediction> findByIdRegion(String region);
}