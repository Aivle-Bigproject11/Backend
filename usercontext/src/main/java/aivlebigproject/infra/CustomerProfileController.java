package aivlebigproject.infra;

import aivlebigproject.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/customerProfiles")
@Transactional
public class CustomerProfileController {

    // 이전에 주입받던 Repository 대신, Service를 주입받습니다.
    @Autowired
    CustomerProfileService customerProfileService;
}
//>>> Clean Arch / Inbound Adaptor
