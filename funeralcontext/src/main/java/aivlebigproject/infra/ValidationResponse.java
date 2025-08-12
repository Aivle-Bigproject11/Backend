// 위치: aivlebigproject/infra/ValidationResponse.java
package aivlebigproject.infra;

import java.util.List;
import lombok.Data;

@Data
public class ValidationResponse {
    private List<AiValidationWarning> warnings;

    public ValidationResponse(List<AiValidationWarning> warnings) {
        this.warnings = warnings;
    }
}