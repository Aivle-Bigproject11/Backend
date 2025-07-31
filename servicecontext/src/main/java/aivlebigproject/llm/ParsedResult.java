package aivlebigproject.llm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedResult {
    private String service1;
    private String service2;
    private String message;
}