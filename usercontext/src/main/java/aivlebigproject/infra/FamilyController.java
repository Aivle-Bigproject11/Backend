package aivlebigproject.infra;

import aivlebigproject.domain.*;
import aivlebigproject.dto.FamilyLoginResponseDto;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import java.util.NoSuchElementException;

@RestController
@Transactional
public class FamilyController {

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    FamilyService familyService;

    @GetMapping("/families/check-id")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String loginId) {
        boolean isDuplicate = familyService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }
    @RequestMapping(
        value = "/families/{familyId}/approve", // 경로를 familyId를 받도록 변경
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Family approveFamily(
        @PathVariable Long familyId,
        @RequestBody ApproveFamilyCommand approveFamilyCommand
    ) throws Exception {
        System.out.println("##### /family/approveFamily called #####");

        // 1. familyId로 유가족 엔티티를 찾습니다.
        Family family = familyRepository.findById(familyId)
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 유가족을 찾을 수 없습니다."));

        // 2. Family 엔티티의 비즈니스 로직을 호출합니다.
        family.approveFamily(approveFamilyCommand);

        // 3. 변경된 엔티티를 반환합니다.
        return family;
    }

    @PostMapping("/families/login")
    public ResponseEntity<FamilyLoginResponseDto> login(@RequestBody Family loginInfo) {
        Optional<Family> familyOptional = familyRepository.findByLoginIdAndLoginPassword(
            loginInfo.getLoginId(),
            loginInfo.getLoginPassword()
        );

        if (familyOptional.isPresent()) {
            Family family = familyOptional.get();
            String token = JwtUtil.generateToken(family.getLoginId());
            
            FamilyLoginResponseDto responseDto = new FamilyLoginResponseDto();
            responseDto.setId(family.getId());
            responseDto.setName(family.getName());
            responseDto.setToken(token);

            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/search-name")
    public ResponseEntity<List<Family>> searchByName(@RequestParam String name) {
        List<Family> families = familyService.searchByName(name);
        return ResponseEntity.ok(families);
    }

    @GetMapping("/search-phone")
    public ResponseEntity<List<Family>> searchByPhone(@RequestParam String phone) {
        List<Family> families = familyService.searchByPhone(phone);
        return ResponseEntity.ok(families);
    }

    @GetMapping("/search-loginId")
    public ResponseEntity<Optional<Family>> searchByLoginId(@RequestParam String loginId) {
        Optional<Family> family = familyService.searchByLoginId(loginId);
        return ResponseEntity.ok(family);
    }

    @GetMapping("/search-email")
    public ResponseEntity<Optional<Family>> searchByEmail(@RequestParam String email) {
        Optional<Family> family = familyService.searchByEmail(email);
        return ResponseEntity.ok(family);
    }

    @GetMapping("/find-login-id")
    public ResponseEntity<String> findLoginId(
            @RequestParam("name") String name,
            @RequestParam("email") String email) {

        Optional<String> loginId = familyService.findFamilyLoginId(name, email);

        if (loginId.isPresent()) {
            // 아이디를 찾았을 경우 성공적으로 응답합니다.
            return ResponseEntity.ok("찾으시는 아이디는: " + loginId.get());
        } else {
            // 아이디를 찾지 못했을 경우 404 Not Found를 반환합니다.
            return ResponseEntity.notFound().build();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
