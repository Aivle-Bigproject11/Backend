package aivlebigproject.domain.repository;

import aivlebigproject.domain.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository
        extends PagingAndSortingRepository<Video, Long> {
    // 특정 추모관의 비디오 목록 조회
    List<Video> findByMemorialIdOrderByCreatedAtDesc(Long memorialId);

    // 상태별 비디오 조회
    List<Video> findByStatusOrderByCreatedAtDesc(String status);

    // 특정 추모관의 특정 상태 비디오 조회
    List<Video> findByMemorialIdAndStatusOrderByCreatedAtDesc(UUID memorialId, String status);

    // 완료된 비디오만 조회
    @Query("SELECT v FROM Video v WHERE v.status = 'COMPLETED' AND v.memorialId = :memorialId ORDER BY v.completedAt DESC")
    List<Video> findCompletedVideosByMemorialId(@Param("memorialId") UUID memorialId);

    // DELETE 메서드는 제한 (실제 삭제 대신 상태 변경 권장)
    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Video entity);
}
