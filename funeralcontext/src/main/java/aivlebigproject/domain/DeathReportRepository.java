package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "deathReports",
    path = "deathReports"
)
public interface DeathReportRepository
    extends PagingAndSortingRepository<DeathReport, Long> {

    // [추가] customerId로 모든 사망신고서 목록을 조회하는 기능
    List<DeathReport> findByCustomerId(@Param("customerId") Long customerId);

    // [추가] customerId에 해당하는 가장 최신의 사망신고서 1개만 조회하는 기능
    Optional<DeathReport> findFirstByCustomerIdOrderByDeathReportIdDesc(@Param("customerId") Long customerId);
}
