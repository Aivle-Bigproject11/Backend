package aivlebigproject.config;

import aivlebigproject.dto.ErrorResponse;
import aivlebigproject.exception.InvalidAuthorizationHeaderException;
import aivlebigproject.exception.MemorialAccessDeniedException;
import aivlebigproject.exception.MemorialNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemorialAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleMemorialAccessDenied(MemorialAccessDeniedException e) {
        ErrorResponse error = new ErrorResponse("MEMORIAL_ACCESS_DENIED", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MemorialNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemorialNotFound(MemorialNotFoundException e) {
        ErrorResponse error = new ErrorResponse("MEMORIAL_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidAuthorizationHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMemorialNotFound(InvalidAuthorizationHeaderException e) {
        ErrorResponse error = new ErrorResponse("INVALID_TOKEN_AUTHORIZATION", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse("INVALID_ARGUMENT", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        ErrorResponse error = new ErrorResponse("FILE_UPLOAD_ERROR", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // 예상치 못한 500 에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        // 로그 남기기
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}