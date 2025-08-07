package aivlebigproject.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "managers", path = "managers")
public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long> {
    
    // loginId와 loginPassword로 관리자를 찾는 사용자 정의 메서드
    Optional<Manager> findByLoginIdAndLoginPassword(String loginId, String loginPassword);
}