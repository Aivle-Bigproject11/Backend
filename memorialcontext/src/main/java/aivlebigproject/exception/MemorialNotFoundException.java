package aivlebigproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemorialNotFoundException extends RuntimeException {
    public MemorialNotFoundException(String memorialId) {
        super("Memorial not found with id: " + memorialId);
    }

    public MemorialNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}