package com.example.demo.domain.thinq.exception;

import com.example.demo.global.common.exception.BaseException;
import com.example.demo.global.common.exception.ExceptionInformation;

public class ThinQException extends BaseException {
    public ThinQException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
