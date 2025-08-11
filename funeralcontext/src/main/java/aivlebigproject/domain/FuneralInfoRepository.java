package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "funeralInfos",
    path = "funeralInfos"
)
public interface FuneralInfoRepository
    extends PagingAndSortingRepository<FuneralInfo, Long> {}
