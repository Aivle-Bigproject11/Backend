package aivlebigproject.domain;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
        collectionResourceRel = "conversionServices",
        path = "conversionServices"
)
public interface ConversionServiceRepository
        extends PagingAndSortingRepository<ConversionService, Long> {
    Optional<ConversionService> findByServiceName(String serviceName);
}