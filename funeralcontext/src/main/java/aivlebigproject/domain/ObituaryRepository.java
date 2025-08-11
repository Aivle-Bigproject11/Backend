package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List; // [주석] List를 사용하기 위해 import 합니다.
import java.util.Optional; // [주석] Optional을 사용하기 위해 import 합니다.
import org.springframework.data.repository.query.Param; // [주석] @Param을 사용하기 위해 import 합니다.

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "obituaries",
    path = "obituaries"
)
public interface ObituaryRepository
    extends PagingAndSortingRepository<Obituary, Long> {

    // customerId로 모든 부고장 목록을 조회하는 기능
    List<Obituary> findByCustomerId(@Param("customerId") Long customerId);

    // [추가] customerId에 해당하는 부고장 중, 가장 최신의 것(ID가 가장 큰 것) 1개만 조회하는 기능
    Optional<Obituary> findFirstByCustomerIdOrderByObituaryIdDesc(@Param("customerId") Long customerId);
}