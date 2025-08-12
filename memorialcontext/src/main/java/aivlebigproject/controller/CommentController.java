package aivlebigproject.controller;

import aivlebigproject.dto.CommentCreateReq;
import aivlebigproject.dto.CommentResponse;
import aivlebigproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value="memorials/{memorialId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID memorialId,
            @RequestBody CommentCreateReq request) {
        CommentResponse response = commentService.createComment(memorialId, request);
        return ResponseEntity.ok(response);
    }
}
//>>> Clean Arch / Inbound Adaptor
