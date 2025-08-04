package aivlebigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestDto {
    private String date; // YearMonth -> String으로 변경
    private String region;
    private Long previousYearDeaths;
}