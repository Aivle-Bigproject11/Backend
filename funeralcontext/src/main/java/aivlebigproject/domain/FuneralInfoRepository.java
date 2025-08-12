package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "funeralInfos",
    path = "funeralInfos"
)
public interface FuneralInfoRepository
    extends PagingAndSortingRepository<FuneralInfo, Long> {

    // [추가] customerId로 모든 장례정보 목록을 조회하는 기능
    List<FuneralInfo> findByCustomerId(@Param("customerId") Long customerId);

    // [추가] customerId에 해당하는 가장 최신의 장례정보 1개만 조회하는 기능
    Optional<FuneralInfo> findFirstByCustomerIdOrderByFuneralInfoIdDesc(@Param("customerId") Long customerId);
}