package com.example.demo.domain.thinq.dto.response;

public record RouteResponse(
        String apiServer,
        String mqttServer,
        String webSocketServer
) {}
