package com.example.demo.domain.member.controller;

import com.example.demo.domain.member.dto.request.MemberCreateRequest;
import com.example.demo.domain.member.dto.request.MemberUpdateRequest;
import com.example.demo.domain.member.dto.response.MemberResponse;
import com.example.demo.domain.member.service.MemberGetService;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.global.common.response.ApiResponse;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController implements MemberSwagger {

    private final MemberService memberService;
    private final MemberGetService memberGetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> createMember(
            @RequestBody @Valid MemberCreateRequest request
    ) {
        return ApiResponse.response(HttpStatus.CREATED, "회원가입 성공", memberService.createMember(request));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberResponse> getMember(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.response(HttpStatus.OK, "회원 단건 조회 성공", memberGetService.getMemberResponse(userDetails.getMemberId()));
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberResponse> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        return ApiResponse.response(HttpStatus.OK, "회원 정보 수정 성공", memberService.updateMember(userDetails.getMemberId(), request));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMemberId());
        return ApiResponse.response(HttpStatus.NO_CONTENT, "회원 탈퇴 성공");
    }
}
