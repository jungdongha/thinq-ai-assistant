package com.example.demo.domain.member.exception;

import com.example.demo.global.common.exception.ExceptionInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionInformation implements ExceptionInformation {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"MEMBER-002","이미 존재하는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
