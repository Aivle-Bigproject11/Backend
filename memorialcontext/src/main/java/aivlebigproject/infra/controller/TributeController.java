package aivlebigproject.infra.controller;

import javax.transaction.Transactional;

import aivlebigproject.domain.repository.TributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/tributes")
@Transactional
public class TributeController {

    @Autowired
    TributeRepository tributeRepository;
}
//>>> Clean Arch / Inbound Adaptor
