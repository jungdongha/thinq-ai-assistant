package com.example.demo.domain.auth.exception;

import com.example.demo.global.common.exception.BaseException;
import com.example.demo.global.common.exception.ExceptionInformation;

public class AuthException extends BaseException {
    public AuthException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
