package com.example.demo.global.common.exception;

import com.example.demo.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.error("BaseException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.response(e.getStatus(), e.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ErrorDetail>>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("ValidationException: {}", e.getMessage());
        List<ErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetail.of(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getHttpStatus())
                .body(ApiResponse.response(
                        ErrorCode.METHOD_ARGUMENT_NOT_VALID.getHttpStatus(),
                        ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(),
                        ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage(),
                        errors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.response(
                        ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                        null
                ));
    }
}
