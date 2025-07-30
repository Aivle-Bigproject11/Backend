package aivlebigproject.infra;

import javax.transaction.Transactional;

import aivlebigproject.domain.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/videos")
@Transactional
public class VideoController {

    @Autowired
    VideoRepository videoRepository;
}
//>>> Clean Arch / Inbound Adaptor
