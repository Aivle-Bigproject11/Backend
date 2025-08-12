// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/CustomerReadModelRepository.java
// ========================================

package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource(
    collectionResourceRel = "customerReadModels",
    path = "customerReadModels"
)
public interface CustomerReadModelRepository
    extends PagingAndSortingRepository<CustomerReadModel, Long> {
    
    // customerId 필드로 CustomerReadModel을 조회하는 기능
    Optional<CustomerReadModel> findByCustomerId(Long customerId);

    // 고객 이름(customerName)으로 Readmodel 목록을 조회하는 기능
    List<CustomerReadModel> findByCustomerName(@Param("name") String customerName);

    // [수정] Readmodel의 기본 키(id) 대신, 원본 고객 ID(customerId)와 이름으로 조회하도록 변경합니다.
    List<CustomerReadModel> findByCustomerIdAndCustomerName(@Param("customerId") Long customerId, @Param("name") String customerName);
}