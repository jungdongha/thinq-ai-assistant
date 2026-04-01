package com.example.demo.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "thinq")
public record ThinQProperties(
        String baseUrl,
        String patToken,
        String apiKey
) {}
