package com.example.demo.domain.member.exception;

import com.example.demo.global.common.exception.BaseException;
import com.example.demo.global.common.exception.ExceptionInformation;

public class MemberException extends BaseException {
    public MemberException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
