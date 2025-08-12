package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "schedules", path = "schedules")
public interface ScheduleRepository
    extends PagingAndSortingRepository<Schedule, Long> {

    // [추가] customerId로 모든 장례일정표 목록을 조회하는 기능
    List<Schedule> findByCustomerId(@Param("customerId") Long customerId);

    // [추가] customerId에 해당하는 가장 최신의 장례일정표 1개만 조회하는 기능
    Optional<Schedule> findFirstByCustomerIdOrderByScheduleIdDesc(@Param("customerId") Long customerId);
}