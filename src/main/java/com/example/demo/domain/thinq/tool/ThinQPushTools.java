package com.example.demo.domain.thinq.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ThinQPushTools {

    private final RestClient thinQRestClent;

    @Tool(description = "푸시 메시지를 구독 중인 디바이스 ID 목록을 조회합니다.")
    public List<String> getPushSubscriptions(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country
    ) {
        PushListApiResponse apiResponse = thinQRestClent.get()
                .uri("/push")
                .header("x-country", country)
                .retrieve()
                .body(PushListApiResponse.class);

        return apiResponse.response().stream()
                .map(PushItem::deviceId)
                .toList();
    }

    @Tool(description = "디바이스 추가/삭제 알림을 구독 중인 클라이언트 ID 목록을 조회합니다.")
    public List<String> getPushClientSubscriptions(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country
    ) {
        PushClientListApiResponse apiResponse = thinQRestClent.get()
                .uri("/push/devices")
                .header("x-country", country)
                .retrieve()
                .body(PushClientListApiResponse.class);

        return apiResponse.response();
    }

    private record PushListApiResponse(String messageId, String timestamp, List<PushItem> response) {}
    private record PushItem(String deviceId) {}
    private record PushClientListApiResponse(String messageId, String timestamp, List<String> response) {}
}
