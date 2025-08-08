package aivlebigproject.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class TributeUpdateRequest {
    @NotBlank(message = "추모사 내용은 필수입니다")
    @Size(max = 5000, message = "추모사는 5000자 이내로 작성해주세요")
    private String tribute;

}