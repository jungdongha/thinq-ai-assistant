package com.example.demo.domain.member.controller;

import com.example.demo.domain.member.dto.request.MemberCreateRequest;
import com.example.demo.domain.member.dto.request.MemberUpdateRequest;
import com.example.demo.domain.member.dto.response.MemberResponse;
import com.example.demo.global.common.response.ApiResponse;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "회원 관리 API")
public interface MemberSwagger {

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일 중복"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
    ApiResponse<MemberResponse> createMember(@RequestBody @Valid MemberCreateRequest request);

    @Operation(summary = "내 정보 조회", description = "토큰으로 본인 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "내 정보 수정", description = "토큰으로 본인 닉네임을 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<MemberResponse> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid MemberUpdateRequest request);

    @Operation(summary = "회원 탈퇴", description = "토큰으로 본인 계정을 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails);
}
