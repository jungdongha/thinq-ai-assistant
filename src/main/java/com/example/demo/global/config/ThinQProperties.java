package com.example.demo.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "thinq")
public record ThinQProperties(
        String baseUrl,
        String patToken,
        String apiKey,
        String clientId,
        @DefaultValue("5s") Duration connectTimeout,
        @DefaultValue("30s") Duration readTimeout
) {}
