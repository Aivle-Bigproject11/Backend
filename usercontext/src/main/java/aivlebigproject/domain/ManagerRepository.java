package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.*;

@RepositoryRestResource(collectionResourceRel = "managers", path = "managers")
public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long> {
    Optional<Manager> findByLoginIdAndLoginPassword(String loginId, String loginPassword);
    Optional<Manager> findByNameAndEmail(String name, String email);
    Optional<Manager> findByLoginId(String loginId);
    List<Manager> findByNameContaining(String name);
    List<Manager> findByPhoneContaining(String phone);
    Optional<Manager> findByEmail(String email);
}