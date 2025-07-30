package aivlebigproject.dto;
import java.time.LocalDate;
import lombok.Data; // lombok.Data 임포트

@Data // Getter, Setter, toString, equals, hashCode 등을 자동으로 생성
public class AiRequestDto {
    private LocalDate date;
    private String region;
    private Long previousYearDeaths; // 전년도 사망자 수 필드
}