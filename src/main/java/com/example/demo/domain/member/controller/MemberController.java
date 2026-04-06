package com.example.demo.domain.member.controller;

import com.example.demo.domain.member.dto.request.MemberCreateRequest;
import com.example.demo.domain.member.dto.request.MemberUpdateRequest;
import com.example.demo.domain.member.dto.response.MemberResponse;
import com.example.demo.domain.member.service.MemberGetServcice;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController implements MemberSwagger {

    private final MemberService memberService;
    private final MemberGetServcice memberGetServcice;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> createMember(
            @RequestBody @Valid MemberCreateRequest request
    ) {
        return ApiResponse.response(HttpStatus.CREATED, "회원가입 성공", memberService.createMember(request));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberResponse> getMember(
            @RequestParam @Valid Long id
    ) {
        return ApiResponse.response(HttpStatus.OK, "회원 단건 조회 성공", memberGetServcice.getMemberResposne(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberResponse> updateMember(
            @RequestBody @Valid Long id,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        return ApiResponse.response(HttpStatus.OK, "회원 정보 수정 성공", memberService.updateMember(id,request));
    }
}
