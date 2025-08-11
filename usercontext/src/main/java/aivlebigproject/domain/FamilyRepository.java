package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.*;

@RepositoryRestResource(collectionResourceRel = "families", path = "families")
public interface FamilyRepository extends PagingAndSortingRepository<Family, Long> {
    Optional<Family> findByLoginIdAndLoginPassword(String loginId, String loginPassword);
    List<Family> findByMemorialId(UUID memorialId);
    Optional<Family> findFirstByMemorialId(UUID memorialId);
}