package com.example.demo.domain.member.controller;

import com.example.demo.domain.member.dto.request.MemberCreateRequest;
import com.example.demo.domain.member.dto.request.MemberUpdateRequest;
import com.example.demo.domain.member.dto.response.MemberResponse;
import com.example.demo.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "회원 단건 조회", description = "ID로 회원 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<MemberResponse> getMember(@PathVariable Long id);

    @Operation(summary = "회원 정보 수정", description = "닉네임을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<MemberResponse> updateMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateRequest request);

    @Operation(summary = "회원 탈퇴", description = "회원을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    ApiResponse<Void> deleteMember(@PathVariable Long id);
}
