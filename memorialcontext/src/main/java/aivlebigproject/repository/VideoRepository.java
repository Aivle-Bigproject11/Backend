package aivlebigproject.repository;

import aivlebigproject.model.Video;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository
        extends PagingAndSortingRepository<Video, Long> {
    // 특정 추모관의 비디오 목록 조회
    List<Video> findByMemorialIdOrderByCompletedAtDesc(UUID memorialId);
}
