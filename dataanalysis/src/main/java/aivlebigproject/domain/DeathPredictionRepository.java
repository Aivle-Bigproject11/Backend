package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.time.YearMonth; // 이 라인은 삭제합니다.

public interface DeathPredictionRepository extends PagingAndSortingRepository<DeathPrediction, DeathPredictionId> {

    // date 매개변수 타입을 String으로 변경
    Optional<DeathPrediction> findByDateAndRegion(String date, String region);
}