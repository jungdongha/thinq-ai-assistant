package com.example.demo.domain.auth.controller;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.response.TokenResponse;
import com.example.demo.global.common.response.ApiResponse;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Auth", description = "인증 API")
public interface AuthSwagger {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request);

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 새로운 Access Token을 발급합니다.")
    ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Parameter(description = "Refresh Token") @RequestHeader("Refresh-Token") String refreshToken);

    @Operation(summary = "로그아웃", description = "Refresh Token을 무효화합니다.")
    ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails);
}
