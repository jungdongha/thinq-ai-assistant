package com.example.demo.domain.member.controller;


import com.example.demo.domain.member.dto.response.MemberResponse;
import com.example.demo.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member", description = "회원 관리 API")
public interface MemberSwagger {

    @Operation(summary = "회원가입", description = """

            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "동기화 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류 (Openfire 연동 실패 등)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<MemberResponse> createMember(
    );


}
