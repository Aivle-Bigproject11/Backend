package aivlebigproject.infra;

import aivlebigproject.domain.*;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/conversionServices")
@Transactional
public class ConversionServiceController {

    @Autowired
    ConversionServiceRepository conversionServiceRepository;
}
//>>> Clean Arch / Inbound Adaptor