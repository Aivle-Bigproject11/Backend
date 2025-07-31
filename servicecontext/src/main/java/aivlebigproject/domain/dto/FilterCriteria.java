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
    private String disease;
    private String family;
}