package aivlebigproject.infra.controller;

import javax.transaction.Transactional;

import aivlebigproject.domain.repository.MemorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/memorials")
@Transactional
public class MemorialController {

    @Autowired
    MemorialRepository memorialRepository;
}
//>>> Clean Arch / Inbound Adaptor
