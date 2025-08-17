package aivlebigproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MemorialAccessDeniedException extends RuntimeException {
    public MemorialAccessDeniedException(String memorialId, Long userId) {
        super("User " + userId + " is not authorized to access memorial " + memorialId);
    }

    public MemorialAccessDeniedException(String message) {
        super(message);
    }
}