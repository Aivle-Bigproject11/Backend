package aivlebigproject.repository;

import aivlebigproject.model.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "comments", path = "comments")
public interface CommentRepository
    extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByMemorialIdOrderByCreatedAtDesc(UUID memorialId);
}
