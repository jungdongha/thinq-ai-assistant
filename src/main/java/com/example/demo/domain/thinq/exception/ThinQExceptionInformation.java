package com.example.demo.domain.thinq.exception;

import com.example.demo.global.common.exception.ExceptionInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ThinQExceptionInformation implements ExceptionInformation {

    THINQ_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "THQ-001", "ThinQ API 호출 중 서버 에러가 발생했습니다."),
    THINQ_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "THQ-002", "ThinQ API 인증에 실패했습니다. PAT 토큰을 확인해주세요."),
    THINQ_FORBIDDEN(HttpStatus.FORBIDDEN, "THQ-003", "ThinQ API 접근 권한이 없습니다."),
    THINQ_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "THQ-004", "ThinQ API 호출 횟수가 초과되었습니다."),
    THINQ_DEVICE_OFFLINE(HttpStatus.SERVICE_UNAVAILABLE, "THQ-005", "대상 디바이스가 오프라인 상태입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
