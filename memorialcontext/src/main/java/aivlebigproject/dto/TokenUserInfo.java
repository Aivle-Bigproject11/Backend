package aivlebigproject.dto;

import lombok.Getter;

@Getter
public class TokenUserInfo {
    // getter 메서드들
    private final Long userId;
    private final String role;

    public TokenUserInfo(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

}