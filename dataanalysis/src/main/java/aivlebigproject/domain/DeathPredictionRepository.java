package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "deathPredictions",
    path = "deathPredictions"
)
public interface DeathPredictionRepository
    extends PagingAndSortingRepository<DeathPrediction, Long> {}
