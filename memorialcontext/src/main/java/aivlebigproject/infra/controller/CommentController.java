package aivlebigproject.infra;

import javax.transaction.Transactional;

import aivlebigproject.domain.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/comments")
@Transactional
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
}
//>>> Clean Arch / Inbound Adaptor
