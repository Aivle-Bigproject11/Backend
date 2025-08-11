package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        collectionResourceRel = "recommendMessages",
        path = "recommendMessages"
)
public interface RecommendMessageRepository
        extends PagingAndSortingRepository<RecommendMessage, Long> {

    // [1] 특정 고객의 모든 메시지 조회
    List<RecommendMessage> findByCustomerId(Long customerId);

    // [2] 특정 고객의 최신 메시지 1건 조회 (날짜 기준)
    Optional<RecommendMessage> findTopByCustomerIdOrderByCreateMessageDateDesc(Long customerId);
}