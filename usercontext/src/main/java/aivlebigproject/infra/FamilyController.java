package aivlebigproject.infra;

import aivlebigproject.domain.*;
import aivlebigproject.dto.FamilyLoginResponseDto;
import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @RequestMapping(value = "/families/{familyId}/approve", // 경로를 familyId를 받도록 변경
      method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  public Family approveFamily(
      @PathVariable Long familyId, @RequestBody ApproveFamilyCommand approveFamilyCommand)
      throws Exception {
    System.out.println("##### /family/approveFamily called #####");

    // 1. familyId로 유가족 엔티티를 찾습니다.
    Family family = familyRepository
        .findById(familyId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 유가족을 찾을 수 없습니다."));

    // 2. Family 엔티티의 비즈니스 로직을 호출합니다.
    family.approveFamily(approveFamilyCommand);

    // 3. 변경된 엔티티를 반환합니다.
    return family;
  }

  @PostMapping("/families/login")
  public ResponseEntity<FamilyLoginResponseDto> login(@RequestBody Family loginInfo) {
    Optional<Family> familyOptional = familyRepository.findByLoginIdAndLoginPassword(
        loginInfo.getLoginId(), loginInfo.getLoginPassword());

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

  @GetMapping("/families/search-name")
  public ResponseEntity<?> searchByName(@RequestParam String name) {
    List<Family> families = familyService.searchByName(name);
    if (families.isEmpty()) {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(families);
  }

  @GetMapping("/families/search-phone")
  public ResponseEntity<?> searchByPhone(@RequestParam String phone) {
    List<Family> families = familyService.searchByPhone(phone);
    if (families.isEmpty()) {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(families);
  }

  @GetMapping("/families/search-loginId")
  public ResponseEntity<?> searchByLoginId(@RequestParam String loginId) {
    Optional<Family> family = familyService.searchByLoginId(loginId);
    if (family.isEmpty()) {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(family);
  }

  @GetMapping("/families/search-email")
  public ResponseEntity<?> searchByEmail(@RequestParam String email) {
    Optional<Family> family = familyService.searchByEmail(email);
    if (family.isEmpty()) {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(family);
  }

  @GetMapping("/families/find-id")
  public ResponseEntity<String> findLoginId(@RequestParam String name, @RequestParam String email) {
    Optional<String> loginId = familyService.findFamilyLoginId(name, email);

    if (loginId.isPresent()) {
      return ResponseEntity.ok("찾으시는 아이디는: " + loginId.get());
    } else {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
  }

  @GetMapping("/families/memorial")
  public ResponseEntity<?> getFamiliesByMemorialId(@RequestParam UUID memorialId) {
    List<Family> families = familyService.findFamiliesByMemorialId(memorialId);
    if (families.isEmpty()) {
     return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(families);
  }
}
// >>> Clean Arch / Inbound Adaptor
