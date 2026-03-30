package com.example.demo.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInformation {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
