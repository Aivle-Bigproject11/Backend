package aivlebigproject.util;

import aivlebigproject.dto.TokenUserInfo;
import aivlebigproject.exception.InvalidAuthorizationHeaderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public TokenUserInfo validateAndGetUserInfo(String authHeader) {
        String token = extractTokenFromHeader(authHeader);

        if (!jwtUtil.validateToken(token)) {
            throw new InvalidAuthorizationHeaderException("Invalid or expired token");
        }

        return jwtUtil.getUserInfoFromToken(token);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException("Authorization header must start with 'Bearer '");
        }
        return authHeader.substring(7);
    }
}