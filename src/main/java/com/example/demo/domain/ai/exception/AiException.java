package com.example.demo.domain.ai.exception;

import com.example.demo.global.common.exception.BaseException;
import com.example.demo.global.common.exception.ExceptionInformation;

public class AiException extends BaseException {
    public AiException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
