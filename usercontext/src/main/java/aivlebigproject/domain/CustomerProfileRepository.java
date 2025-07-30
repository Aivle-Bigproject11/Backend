package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "customerProfiles",
    path = "customerProfiles"
)
public interface CustomerProfileRepository
    extends PagingAndSortingRepository<CustomerProfile, Long> {}
