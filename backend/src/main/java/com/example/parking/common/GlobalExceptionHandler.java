package com.example.parking.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusiness(BusinessException ex) {
        int code = ex.getCode();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (code == ErrorCode.UNAUTHORIZED) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (code == ErrorCode.FORBIDDEN) {
            status = HttpStatus.FORBIDDEN;
        } else if (code == ErrorCode.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(ApiResponse.error(code, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ErrorCode.BAD_REQUEST, msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.BUSINESS_ERROR, ex.getMessage()));
    }
}

