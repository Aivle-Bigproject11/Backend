package aivlebigproject.infra;

import javax.transaction.Transactional;

import aivlebigproject.domain.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/photos")
@Transactional
public class PhotoController {

    @Autowired
    PhotoRepository photoRepository;
}
//>>> Clean Arch / Inbound Adaptor
