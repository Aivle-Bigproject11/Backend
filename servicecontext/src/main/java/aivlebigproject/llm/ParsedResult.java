package aivlebigproject.llm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedResult {
//    private Long serviceId1;
//    private Long serviceId2;
    private String service1;
    private String service2;
    private String service1DetailedUrl;
    private String service2DetailedUrl;
    private String service1ImageUrl;
    private String service2ImageUrl;
    private String message;
}