package com.example.demo.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    MEMBER("Role Member"),
    ADMIN("Role Admin");
    private final String authority;
}
