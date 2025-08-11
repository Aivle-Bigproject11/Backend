package aivlebigproject.dto;

import lombok.Data;

@Data
public class ManagerLoginResponseDto {
    private Long id;
    private String name;
    private String token;
}