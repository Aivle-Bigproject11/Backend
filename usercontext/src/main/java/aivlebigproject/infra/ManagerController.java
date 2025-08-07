package aivlebigproject.infra;

import aivlebigproject.domain.Manager;
import aivlebigproject.domain.ManagerRepository;
import aivlebigproject.util.JwtUtil; // JwtUtil 클래스 import
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;

    @PostMapping("/managers/login")
    public ResponseEntity<ManagerLoginResponseDto> login(@RequestBody Manager loginInfo) {
        Optional<Manager> managerOptional = managerRepository.findByLoginIdAndLoginPassword(
            loginInfo.getLoginId(),
            loginInfo.getLoginPassword()
        );

        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();

            // JWT 토큰 생성
            String token = JwtUtil.generateToken(manager.getLoginId());

            // 응답 DTO 생성
            ManagerLoginResponseDto responseDto = new ManagerLoginResponseDto();
            responseDto.setId(manager.getId());
            responseDto.setName(manager.getName());
            responseDto.setToken(token);

            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
