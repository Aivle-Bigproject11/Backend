// 위치: aivlebigproject/infra/AiValidationWarning.java
package aivlebigproject.infra;

import lombok.Data;

@Data
public class AiValidationWarning {
    private String fieldName;
    private String warningDescription;
    private String suggestion;
}
