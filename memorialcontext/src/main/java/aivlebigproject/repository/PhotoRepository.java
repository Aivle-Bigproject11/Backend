package aivlebigproject.repository;

import aivlebigproject.model.Photo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "photos", path = "photos")
public interface PhotoRepository
    extends PagingAndSortingRepository<Photo, Long> {
    List<Photo> findByMemorialIdOrderByUploadedAtDesc(UUID memorialId);
}
