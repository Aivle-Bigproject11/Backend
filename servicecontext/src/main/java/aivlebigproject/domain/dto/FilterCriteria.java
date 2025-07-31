package aivlebigproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FilterCriteria {
    private String ageGroup;
    private String gender;
    private String disease; //질병은 유/무로만 받으려고 함.
    private String family;
}