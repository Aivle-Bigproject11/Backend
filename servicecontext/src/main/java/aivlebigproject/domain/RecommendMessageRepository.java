package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
        collectionResourceRel = "recommendMessages",
        path = "recommendMessages"
)
public interface RecommendMessageRepository
        extends PagingAndSortingRepository<RecommendMessage, Long> {}