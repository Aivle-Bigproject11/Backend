package aivlebigproject.infra;

import aivlebigproject.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/families")
@Transactional
public class FamilyController {

    @Autowired
    FamilyRepository familyRepository;

    @RequestMapping(
        value = "/families/approvefamily",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Family approveFamily(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody ApproveFamilyCommand approveFamilyCommand
    ) throws Exception {
        System.out.println("##### /family/approveFamily  called #####");
        Family family = new Family();
        family.approveFamily(approveFamilyCommand);
        familyRepository.save(family);
        return family;
    }
}
//>>> Clean Arch / Inbound Adaptor
