package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "managers", path = "managers")
public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long> {
    Optional<Manager> findByLoginIdAndLoginPassword(String loginId, String loginPassword);
    Optional<Manager> findByLoginId(String loginId);
}