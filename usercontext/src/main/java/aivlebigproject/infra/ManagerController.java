package aivlebigproject.infra;

import aivlebigproject.domain.JwtUtil;
import aivlebigproject.domain.Manager;
import aivlebigproject.domain.ManagerRepository;
import aivlebigproject.domain.ManagerService; // ManagerService import
import aivlebigproject.dto.*;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class ManagerController {

  @Autowired ManagerRepository managerRepository;

  @Autowired ManagerService managerService;

  // ✨ ID 중복 확인 API 엔드포인트 추가
  @GetMapping("/managers/check-id")
  public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String loginId) {
    boolean isDuplicate = managerService.isLoginIdDuplicate(loginId);
    return ResponseEntity.ok(isDuplicate);
  }

  @PostMapping("/managers/login")
  public ResponseEntity<ManagerLoginResponseDto> login(@RequestBody Manager loginInfo) {
    // 기존 로그인 로직은 ManagerService를 통해 처리할 수도 있습니다.
    // 여기서는 예시를 위해 기존 코드를 유지합니다.
    Optional<Manager> managerOptional =
        managerRepository.findByLoginIdAndLoginPassword(
            loginInfo.getLoginId(), loginInfo.getLoginPassword());

    if (managerOptional.isPresent()) {
      Manager manager = managerOptional.get();
      String token = JwtUtil.generateToken(manager.getLoginId());

      ManagerLoginResponseDto responseDto = new ManagerLoginResponseDto();
      responseDto.setId(manager.getId());
      responseDto.setName(manager.getName());
      responseDto.setToken(token);

      return ResponseEntity.ok(responseDto);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/managers/find-id")
  public ResponseEntity<String> findLoginId(@RequestParam String name, @RequestParam String email) {
    Optional<String> loginId = managerService.findManagerLoginId(name, email);

    if (loginId.isPresent()) {
      return ResponseEntity.ok("찾으시는 아이디는: " + loginId.get());
    } else {
      return ResponseEntity.badRequest().body("입력하신 정보와 일치하는 계정을 찾을 수 없습니다.");
    }
  }
} 
