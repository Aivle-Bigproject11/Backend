package aivlebigproject.service;

import aivlebigproject.dto.CommentCreateReq;
import aivlebigproject.dto.CommentResponse;
import aivlebigproject.model.Comment;
import aivlebigproject.repository.CommentRepository;
import aivlebigproject.repository.MemorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemorialRepository memorialRepository;

    @Transactional
    public CommentResponse createComment(UUID memorialId, CommentCreateReq request){
        if (!memorialRepository.existsById(memorialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 추모관을 찾을 수 없습니다.: " + memorialId);
        }

        Comment comment = new Comment();
        comment.setMemorialId(memorialId);
        comment.setName(request.getName());
        comment.setContent(request.getContent());
        comment.setRelationship(request.getRelationship());

        Comment saved = commentRepository.save(comment);

        return CommentResponse.builder()
                .commentId(saved.getCommentId())
                .memorialId(saved.getMemorialId())
                .content(saved.getContent())
                .name(saved.getName())
                .relationship(saved.getRelationship())
                .build();
    }
}
