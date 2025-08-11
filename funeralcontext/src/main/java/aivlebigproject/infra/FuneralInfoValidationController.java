// 위치: aivlebigproject/infra/FuneralInfoValidationController.java
package aivlebigproject.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FuneralInfoValidationController {

    @Autowired
    FuneralInfoAiValidator funeralInfoAiValidator;

    @PostMapping("/funeralInfos/validate-fields")
    public ResponseEntity<ValidationResponse> validateFields(
        @RequestBody Map<String, Object> fieldsToValidate
    ) {
        // 1. Validator를 호출하여 FE가 보낸 필드들을 검증합니다.
        List<AiValidationWarning> warningList = funeralInfoAiValidator.validateData(fieldsToValidate);

        // 2. 결과를 ValidationResponse 객체에 담아 FE에 반환합니다.
        ValidationResponse response = new ValidationResponse(warningList);
        return ResponseEntity.ok(response);
    }
}
