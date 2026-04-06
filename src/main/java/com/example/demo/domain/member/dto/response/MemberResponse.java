package com.example.demo.domain.member.dto.response;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.entity.Role;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String password,
        String nickname,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getNickName(),
                member.getRole(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
